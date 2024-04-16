package com.xk.kkrpc;

import com.xk.kkrpc.register.RegisterConfig;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.register.Register;
import com.xk.kkrpc.register.RegisterFactory;
import com.xk.kkrpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

import static com.xk.kkrpc.constant.RpcConstant.RPC_CONFIG_PREFIX;

/**
 * Rpc框架应用
 * 存放了项目全局用到的变量，双检锁单例模式实现
 */
@Slf4j
public class RpcApplication {
    public static volatile RpcConfig rpcConfig;

    /**
     * 初始化配置 支持自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig) {
        log.info("rpc init, config = {}", newRpcConfig.toString());
        rpcConfig = newRpcConfig;
        // 初始化注册中心
        String registerName = rpcConfig.getRegister();
        Register register = RegisterFactory.getInstance(registerName);
        // todo 注册中心的配置需要从配置文件中读取 后面实现
        RegisterConfig registerConfig = new RegisterConfig();
        register.init(registerConfig);
        // 创建并注册 Shutdown Hook，JVM 退出时执行操作
        Runtime.getRuntime().addShutdownHook(new Thread(register::destroy));
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
