package com.xk.kkrpc.register;

import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.config.RegisterConfig;
import com.xk.kkrpc.model.ServiceMateInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * etcd注册中心实现类
 */
@Slf4j
public class EtcdRegistry implements Register {

    private Client client;

    private KV kvClient;
    /**
     * 根节点
     */
    private static final String ETCD_ROOT_PATH = "/rpc/";

    @Override
    public void init(RegisterConfig registerConfig) {
        client = Client.builder().endpoints(registerConfig.getAddress())
                .connectTimeout(Duration
                        .ofMillis(registerConfig.getTimeout())).build();
        kvClient = client.getKVClient();
    }

    @Override
    public void register(ServiceMateInfo serviceMateInfo) throws ExecutionException, InterruptedException {
        // 把信息存储到etcd中并设置ttl
        Lease leaseClient = client.getLeaseClient();
        //创建一个30秒的租约
        long leaseId = leaseClient.grant(30).get().getID();
        // 设置键值
        String registerKye = ETCD_ROOT_PATH + serviceMateInfo.getServiceNodeKey();
        ByteSequence key = ByteSequence.from(registerKye, StandardCharsets.UTF_8);
        ByteSequence value = ByteSequence.from(JSONUtil.toJsonStr(serviceMateInfo), StandardCharsets.UTF_8);
        // 把键值对关联起来，并设置过期时间
        PutOption putOption = PutOption.builder().withLeaseId(leaseId).build();
        kvClient.put(key, value, putOption).get();
    }

    @Override
    public void unregister(ServiceMateInfo serviceMateInfo) {
        if (serviceMateInfo == null) {
            return;
        }
        kvClient.delete(ByteSequence.from(ETCD_ROOT_PATH + serviceMateInfo.getServiceNodeKey(), StandardCharsets.UTF_8));
    }

    @Override
    public List<ServiceMateInfo> serviceDiscovery(ServiceMateInfo serviceMateInfo) {
        // 这里是前缀搜索 所以和注册服务的键是不同的 取上一层作为 发现的键
        String searchKey = ETCD_ROOT_PATH + serviceMateInfo.getServiceKey() + "/";;
        // 取出注册中心的注册服务 并返回
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.
                    get(ByteSequence.from(searchKey, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            // 把获取到的列表映射为ServiceMateInfo 并返回
            return keyValues.stream().map(item -> {
                String serviceMateInfoJsonStr = item.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(serviceMateInfoJsonStr, ServiceMateInfo.class);
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.info("服务发现失败" + searchKey);
            throw new RuntimeException("服务发现失败", e);
        }
    }

    @Override
    public void destroy() {
        System.out.println("当前节点下线");
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
