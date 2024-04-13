package com.xk.kkrpc.loadbalancer;

import com.xk.kkrpc.model.ServiceMateInfo;

import java.util.List;
import java.util.Map;

/**
 * 负载均衡器（服务端）
 */
public interface LoadBalancer {
    /**
     * 负载聚恒器
     * @param requestPara
     * @param serviceMateInfoList 提供服务的服务列表信息
     * @return
     */
    ServiceMateInfo Select(Map<String,Object> requestPara, List<ServiceMateInfo> serviceMateInfoList);
}
