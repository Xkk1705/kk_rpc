package com.xk.kkrpc.register;

import com.xk.kkrpc.constant.RegisterConstant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心工厂
 */
public class RegisterFactory {

    /**
     *  创建注册中心对象
     */
    public static final Map<String, Register> RegisterMap = new ConcurrentHashMap() {{
        put(RegisterConstant.REGISTER_ETCD, new EtcdRegistry());
    }};
    /**
     * 默认注册中心对象
     */
    private static final Register etcdRegister = RegisterMap.get(RegisterConstant.REGISTER_ETCD);

    /**
     * 获取注册中心对象
     *
     * @param registerName
     * @return
     */
    public static Register getInstance(String registerName) {
        return RegisterMap.getOrDefault(registerName, etcdRegister);
    }

}
