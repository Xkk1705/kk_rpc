package com.xk.kkrpc.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务类动态代理
 * Mock 虚拟值返回
 */
public class ServiceMockProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> returnType = method.getReturnType();
        return getMockResult(returnType);
    }

    /**
     * 更具返回类型 返回mock数据
     *
     * @param type
     * @return
     */
    private Object getMockResult(Class<?> type) {
        // 基本类型
        if (type.isPrimitive()) {// 是否为基本数据类型
            //Boolean.TYPE, Character.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Void.TYPE
            if (type == boolean.class) {
                return false;
            } else if (type == short.class) {
                return (short) 0;
            } else if (type == int.class) {
                return 0;
            } else if (type == long.class) {
                return 0L;
            }
        }
        // 对象类型
        return null;
    }
}
