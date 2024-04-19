package com.xukang.kkrpcspringbootstater.bootstarp;

import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.service.VertxHttpServer;
import com.xukang.kkrpcspringbootstater.annotation.EnableRpc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * RPC框架启动
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * spring 初始化时候之心
     *
     * @param importingClassMetadata
     * @param registry
     */
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 1.获取注解属性
        boolean needServer = (Boolean) importingClassMetadata.getAnnotationAttributes(EnableRpc.class.getName())
                .get("needServer");
        // 2.框架初始化
        RpcApplication.init();
        // 3. 全局配置
        final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        // 4. 启动服务器
        if (needServer) {
            VertxHttpServer vertxHttpServer = new VertxHttpServer();
            vertxHttpServer.doStart(rpcConfig.getServerPort());
            String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
            log.info("rpc service start succeed in:" + url);
        }
    }
}
