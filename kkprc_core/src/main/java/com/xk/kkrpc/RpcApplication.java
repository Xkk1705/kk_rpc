package com.xk.kkrpc;

import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.utils.ConfigUtils;

import static com.xk.kkrpc.contant.RpcConstant.RPC_CONFIG_PREFIX;

/**
 * Rpc框架应用
 * 存放了项目全局用到的变量，双检锁单例模式实现
 */
public class RpcApplication {
    public static volatile RpcConfig rpcConfig;

    /**
     * 初始化配置 支持自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        rpcConfig = newRpcConfig;
    }

    /**
     * 初始化
     */
    public static void init() {
        RpcConfig newRpcConfig;
        try {
            newRpcConfig = ConfigUtils.loadRpcConfig(RpcConfig.class, RPC_CONFIG_PREFIX);
        } catch (Exception e) {
            // 配置加载失败 使用默认配置
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    public static RpcConfig getRpcConfig() {
        if (rpcConfig == null) {
            synchronized (RpcApplication.class) {
                if (rpcConfig == null) {
                    init();
                }
            }
        }
        return rpcConfig;
    }
}
