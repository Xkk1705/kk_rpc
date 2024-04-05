package com.xk.kkrpc.config;


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
}
