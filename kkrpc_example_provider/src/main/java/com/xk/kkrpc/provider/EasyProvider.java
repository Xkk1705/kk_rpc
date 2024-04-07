package com.xk.kkrpc.provider;

import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.model.ServiceMateInfo;
import com.xk.kkrpc.register.LocalRegister;
import com.xk.kkrpc.register.Register;
import com.xk.kkrpc.register.RegisterFactory;
import com.xk.kkrpc.service.VertxHttpServer;

import java.util.concurrent.ExecutionException;

import static com.xk.kkrpc.constant.RegisterConstant.SERVICE_VERSION;

public class EasyProvider {
    public static void main(String[] args) {
        // 1. 在启动之前注册本地服务
        LocalRegister.register(UserService.class.getName(), UserServiceImpl.class);

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 在注册中心中注册服务
        Register register = RegisterFactory.getInstance(RpcApplication.getRpcConfig().getRegister());
        ServiceMateInfo serviceMateInfo = new ServiceMateInfo();
        serviceMateInfo.setServiceName(UserService.class.getName());
        // todo 服务address在注册的时候需要从提供者配置文件中获取 后面实现
        serviceMateInfo.setServiceHost(rpcConfig.getServerHost());
        serviceMateInfo.setServicePort(rpcConfig.getServerPort());
        serviceMateInfo.setServiceVersion(SERVICE_VERSION);
        try {
            register.register(serviceMateInfo);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("注册服务失败", e);
        }

        // 2. 配置服务器请求处理器 按照一定的规范返回请求
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(rpcConfig.getServerPort());
        String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
        System.out.println(url);
        System.out.println(rpcConfig.getMock());
        System.out.println(rpcConfig.getSerializer());
    }
}
