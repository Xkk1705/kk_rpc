package com.xk.kkrpc.consumer.proxy;

import java.lang.reflect.Proxy;

/**
 * 注册服务代理 工厂
 */
public class ServiceProxyFactory {

    public static <T> T getProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class[]{serviceClass},
                new ServiceProxy());
    }
}
