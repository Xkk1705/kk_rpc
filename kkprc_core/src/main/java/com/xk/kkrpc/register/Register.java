package com.xk.kkrpc.register;

import com.xk.kkrpc.model.ServiceMateInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 注册中心接口（规范）
 */
public interface Register {
    /**
     * 初始化服务
     * @param registerConfig
     */
    void init(RegisterConfig registerConfig);

    /**
     * 注册服务
     *
     * @param serviceMateInfo
     */
    void register(ServiceMateInfo serviceMateInfo) throws ExecutionException, InterruptedException;

    /**
     * 删除服务
     *
     * @param serviceMateInfo
     */
    void unregister(ServiceMateInfo serviceMateInfo);


    /**
     * 发现服务
     *
     * @param ServiceKey
     * @return
     */
    List<ServiceMateInfo> serviceDiscovery(String ServiceKey);

    /**
     * 心跳检测
     */
    void heartBeat();

    /**
     * 根据注册服务节点 监听etcd注册中心的服务变化
     */
    void watch(String registerServiceNodeKey);

    /**
     * 服务销毁
     */
    void destroy();


}
