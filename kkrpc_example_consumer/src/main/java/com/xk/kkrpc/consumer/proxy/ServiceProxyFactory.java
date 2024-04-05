package com.xk.kkrpc.consumer.proxy;

import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.config.RpcConfig;

import java.lang.reflect.Proxy;

/**
 * 注册服务代理 工厂
 */
public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (rpcConfig.getMock()) {
            return getMockProxy(serviceClass);
        }

        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }

    /**
     * 获取mock代理类
     *
     * @param serviceClass
     * @param <T>
     * @return
     */
    private static <T> T getMockProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceMockProxy());
    }
}
