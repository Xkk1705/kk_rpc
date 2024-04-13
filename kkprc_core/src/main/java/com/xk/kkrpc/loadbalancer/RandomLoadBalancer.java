package com.xk.kkrpc.loadbalancer;

import cn.hutool.core.collection.CollUtil;
import com.xk.kkrpc.model.ServiceMateInfo;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡（随机）
 */
public class RandomLoadBalancer implements LoadBalancer {
    public final Random random = new Random();

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
        return serviceMateInfoList.get(random.nextInt(size));
    }
}
