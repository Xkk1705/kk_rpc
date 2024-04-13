package com.xk.kkrpc.register;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.config.RegisterConfig;
import com.xk.kkrpc.model.RegistryServiceCache;
import com.xk.kkrpc.model.ServiceMateInfo;
import io.etcd.jetcd.*;
import io.etcd.jetcd.options.GetOption;
import io.etcd.jetcd.options.PutOption;
import io.etcd.jetcd.watch.WatchEvent;
import io.vertx.core.impl.ConcurrentHashSet;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private static final String ETCD_ROOT_PATH = "/META-INF/rpc/";

    /**
     * 本机注册的节点 key 集合（用于维护续期）
     * 用于心跳检测节点是否存活
     */
    public static final Set<String> localServiceNodeKeySet = new HashSet<>();

    /**
     * 注册服务节点缓存
     */
    public static final RegistryServiceCache registryServiceCache = new RegistryServiceCache();

    /**
     * 监听注册服务节点key
     * 用于解决重复监听
     */
    public static final Set<String> watchRegisterServiceNodeKeySet = new ConcurrentHashSet<>();


    @Override
    public void init(RegisterConfig registerConfig) {
        client = Client.builder().endpoints(registerConfig.getAddress())
                .connectTimeout(Duration
                        .ofMillis(registerConfig.getTimeout())).build();
        kvClient = client.getKVClient();
        heartBeat();// 开启心跳检测机制
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
        // 添加节点信息到本地缓存
        localServiceNodeKeySet.add(registerKye);
        // 新服务注册 把之前的缓存清空
        registryServiceCache.clearRegisterServiceCache();
    }

    @Override
    public void unregister(ServiceMateInfo serviceMateInfo) {
        if (serviceMateInfo == null) {
            return;
        }
        String registerKye = ETCD_ROOT_PATH + serviceMateInfo.getServiceNodeKey();
        kvClient.delete(ByteSequence.from(registerKye, StandardCharsets.UTF_8));
        //删除节点信息在本地缓存
        localServiceNodeKeySet.remove(registerKye);
    }

    @Override
    public List<ServiceMateInfo> serviceDiscovery(String getServiceKey) {
        // 读取缓存中的数据
        List<ServiceMateInfo> serviceMateInfos = registryServiceCache.readServiceCache();
        if (CollUtil.isNotEmpty(serviceMateInfos)) {
            return serviceMateInfos;
        }
        // 这里是前缀搜索 所以和注册服务的键是不同的 取上一层作为 发现的键
        String searchKey = ETCD_ROOT_PATH + getServiceKey + "/";

        // 取出注册中心的注册服务 并返回
        try {
            GetOption getOption = GetOption.builder().isPrefix(true).build();
            List<KeyValue> keyValues = kvClient.
                    get(ByteSequence.from(searchKey, StandardCharsets.UTF_8), getOption)
                    .get()
                    .getKvs();
            // 把获取到的列表映射为ServiceMateInfo 并返回
            List<ServiceMateInfo> serviceMateInfoList = keyValues.stream().map(item -> {
                // 根据serviceRegisterServiceNodeKey监听服务
                String serviceRegisterServiceNodeKey = item.getKey().toString(StandardCharsets.UTF_8);
                watch(serviceRegisterServiceNodeKey);
                String serviceMateInfoJsonStr = item.getValue().toString(StandardCharsets.UTF_8);
                return JSONUtil.toBean(serviceMateInfoJsonStr, ServiceMateInfo.class);
            }).collect(Collectors.toList());
            // 把服务写入缓存
            registryServiceCache.writeServiceCache(serviceMateInfoList);
            return serviceMateInfoList;
        } catch (Exception e) {
            log.info("服务发现失败" + searchKey);
            throw new RuntimeException("服务发现失败", e);
        }
    }

    @Override
    public void heartBeat() {
        // 定时任务执行续约
        CronUtil.schedule("*/10 * * * * *", new Task() {// 每十秒执行一次任务
            @Override
            public void execute() {
                for (String registerServiceKye : localServiceNodeKeySet) {
                    //获取每个节点 判断是否存在
                    try {
                        List<KeyValue> keyValues = kvClient.get(ByteSequence.from(registerServiceKye, StandardCharsets.UTF_8))
                                .get().
                                getKvs();
                        //不存在表示已经宕机 需要重启重新注册
                        if (CollUtil.isEmpty(keyValues)) {
                            continue;
                        }
                        //存在 给节点续期
                        String value = keyValues.get(0).getValue().toString(StandardCharsets.UTF_8);
                        ServiceMateInfo serviceMateInfo = JSONUtil.toBean(value, ServiceMateInfo.class);
                        register(serviceMateInfo);

                    } catch (InterruptedException | ExecutionException e) {
                        throw new RuntimeException("节点续期失败" + registerServiceKye, e);
                    }
                }
            }
        });
        // 支持秒级别定时任务
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    @Override
    public void watch(String registerServiceNodeKey) {
        // 判断监听节点key是否存在
        boolean newWatch = watchRegisterServiceNodeKeySet.add(registerServiceNodeKey);

        if (newWatch) { //不存在监听服务节点
            Watch watchClient = client.getWatchClient();
            watchClient.watch(ByteSequence.from(registerServiceNodeKey, StandardCharsets.UTF_8), watchResponse -> {
                List<WatchEvent> events = watchResponse.getEvents();
                for (WatchEvent event : events) {
                    switch (event.getEventType()) {
                        // key 删除时触发
                        case DELETE:
                            // 清理注册服务缓存
                            registryServiceCache.clearRegisterServiceCache();
                            break;
                        case PUT:
                        default:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void destroy() {
        log.info("当前节点下线");
        // 删除注册中心中的所有注册节点
        for (String registerServiceNodeKey : localServiceNodeKeySet) {
            try {
                kvClient.delete(ByteSequence.from(registerServiceNodeKey, StandardCharsets.UTF_8)).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException("节点下线失败" + registerServiceNodeKey, e);
            }
        }
        // 释放资源
        if (kvClient != null) {
            kvClient.close();
        }
        if (client != null) {
            client.close();
        }
    }
}
