package com.xk.kkrpc.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import com.xk.kkrpc.model.ServiceMateInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 负载均衡（一致性hash环）
 */
public class ConsistentHashLoadBalancer implements LoadBalancer {
    /**
     * 一致性hash环 虚拟节点
     */
    public final TreeMap<Integer, ServiceMateInfo> virtualNode = new TreeMap<>();

    public static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMateInfo Select(Map<String, Object> requestPara, List<ServiceMateInfo> serviceMateInfoList) {
        // 服务列表为空
        if (CollUtil.isEmpty(serviceMateInfoList)) {
            return null;
        }
        int size = serviceMateInfoList.size();
        if (size == 1) {
            return serviceMateInfoList.get(0);
        }
        // 构建hash环
        for (ServiceMateInfo serviceMateInfo : serviceMateInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMateInfo.getServiceAddress() + "#" + i);
                virtualNode.put(hash, serviceMateInfo);
            }
        }
        // 获取调用请求的hash
        int hash = getHash(requestPara);

        // 选择最接近且大于调用请求的hash虚拟节点
        Map.Entry<Integer, ServiceMateInfo> entry = virtualNode.ceilingEntry(hash);
        if (entry == null) {
            // 如果没有大于等于调用请求hash值的虚拟节点，则返回首部节点
            entry = virtualNode.firstEntry();
        }
        return entry.getValue();
    }

    /**
     * 获取hash值
     *
     * @param key
     * @return
     */
    public int getHash(Object key) {
        return key.hashCode();
    }
}
