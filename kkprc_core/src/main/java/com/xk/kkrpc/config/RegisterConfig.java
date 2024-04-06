package com.xk.kkrpc.config;

import lombok.Data;

/**
 * 配置中心配置类
 */
@Data
public class RegisterConfig {
    /**
     * 注册中心地址
     */
    private String address = "http://localhost:2380";
    /**
     * 注册中心类别
     */
    private String register = "etdc";
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 连接注册中心超时时间
     */
    private Long timeout = 1000L;
}
