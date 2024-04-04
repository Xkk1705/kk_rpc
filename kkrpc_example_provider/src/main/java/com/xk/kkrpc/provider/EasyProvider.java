package com.xk.kkrpc.provider;

import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.register.LocalRegister;
import com.xk.kkrpc.service.VertxHttpServer;

public class EasyProvider {
    public static void main(String[] args) {
        // 1. 在启动之前注册服务
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);
        // 2. 配置服务器请求处理器 按照一定的规范返回请求
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        vertxHttpServer.doStart(rpcConfig.getServerPort());
    }
}
