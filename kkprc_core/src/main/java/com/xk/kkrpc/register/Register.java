package com.xk.kkrpc.register;

import com.xk.kkrpc.config.RegisterConfig;
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
     * 发现服务11
     *
     * @param serviceMateInfo
     * @return
     */
    List<ServiceMateInfo> serviceDiscovery(ServiceMateInfo serviceMateInfo);

    /**
     * 服务销毁
     */
    void destroy();


}
