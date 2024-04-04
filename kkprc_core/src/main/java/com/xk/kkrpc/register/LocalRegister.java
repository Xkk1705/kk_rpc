package com.xk.kkrpc.register;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册器
 */
public class LocalRegister {
    /**
     * 注册容器
     */
    public static final Map<String, Class<?>> localRegister = new ConcurrentHashMap<>();

    /**
     * 注册服务
     *
     * @param serviceName
     * @param impClass
     */
    public static void register(String serviceName, Class<?> impClass) {
        localRegister.put(serviceName, impClass);
    }


    /**
     * 获取注册类
     *
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName) {
        return localRegister.get(serviceName);
    }

    /**
     * 删除注册服务
     *
     * @param serviceName
     * @return
     */
    public Class<?> remove(String serviceName) {
        return localRegister.remove(serviceName);
    }

}
