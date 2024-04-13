package com.xk.kkrpc.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import com.xk.kkrpc.model.ServiceMateInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡（轮询）
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    public final  AtomicInteger atomicInteger = new AtomicInteger(0);

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
        // 轮询
        int andIncrement = atomicInteger.getAndIncrement() % size;
        return serviceMateInfoList.get(andIncrement);
    }
}
