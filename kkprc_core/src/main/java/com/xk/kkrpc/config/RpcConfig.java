package com.xk.kkrpc.config;


import com.xk.kkrpc.constant.RegisterConstant;
import com.xk.kkrpc.fault.retry.RetryStrategyKeys;
import com.xk.kkrpc.fault.tolerate.TolerantStrategyKeys;
import com.xk.kkrpc.loadbalancer.LoadBalanceKeys;
import com.xk.kkrpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * 注册中心全局配置类
 */
@Data
public class RpcConfig {
    /**
     * 名称
     */
    private String name = "kangrpc";
    /**
     * 版本
     */
    private String version = "1.0";
    /**
     * 服务器主机名称
     */
    private String serverHost = "127.0.0.1";
    /**
     * 服务器端口
     */
    private Integer serverPort = 8081;
    /**
     * 是否开启mock
     */
    private Boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 负载均衡器
     */
    private String loadbalancer = LoadBalanceKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    private String retry = RetryStrategyKeys.NO_RETRY;

    /**
     * 容错策略
     */
    private String tolerate = TolerantStrategyKeys.FAIL_FAST;

    /**
     * 注册中心
     */
    private String register = RegisterConstant.REGISTER_ETCD;
}
