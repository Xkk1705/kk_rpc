package com.xk.kkrpc.bootstarp;

import cn.hutool.core.collection.CollUtil;
import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.model.ServiceMateInfo;
import com.xk.kkrpc.model.ServiceRegisterInfo;
import com.xk.kkrpc.register.LocalRegister;
import com.xk.kkrpc.register.Register;
import com.xk.kkrpc.register.RegisterFactory;
import com.xk.kkrpc.service.VertxHttpServer;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.xk.kkrpc.constant.RegisterConstant.SERVICE_VERSION;

/**
 * 服务提供者初始化
 */
public class ProviderBootstrap {
    public static void init(List<ServiceRegisterInfo> serviceRegisterInfos) {
        // RPC 框架初始化（配置和注册中心）
        RpcApplication.init();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Register register = RegisterFactory.getInstance(RpcApplication.getRpcConfig().getRegister());
        if (CollUtil.isEmpty(serviceRegisterInfos)) {
            return;
        }
        for (ServiceRegisterInfo<?> serviceRegisterInfo : serviceRegisterInfos) {
            String serviceName = serviceRegisterInfo.getServiceName();
            Class<?> impClass = serviceRegisterInfo.getImplClass();
            // 1. 在启动之前注册本地服务
            LocalRegister.register(serviceName, impClass);

            // 在注册中心中注册服务
            ServiceMateInfo serviceMateInfo = new ServiceMateInfo();
            serviceMateInfo.setServiceName(serviceName);
            // todo 服务address在注册的时候需要从提供者配置文件中获取 后面实现
            serviceMateInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMateInfo.setServicePort(rpcConfig.getServerPort());
            serviceMateInfo.setServiceVersion(SERVICE_VERSION);
            try {
                register.register(serviceMateInfo);
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException("注册服务失败", e);
            }
        }
        // 2. 配置服务器请求处理器 按照一定的规范返回请求
        VertxHttpServer vertxHttpServer = new VertxHttpServer();
        vertxHttpServer.doStart(rpcConfig.getServerPort());
        String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
        System.out.println(url);
    }
}
