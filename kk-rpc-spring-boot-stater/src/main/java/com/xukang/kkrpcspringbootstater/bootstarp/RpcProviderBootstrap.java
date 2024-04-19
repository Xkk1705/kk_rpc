package com.xukang.kkrpcspringbootstater.bootstarp;

import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.model.ServiceMateInfo;
import com.xk.kkrpc.register.LocalRegister;
import com.xk.kkrpc.register.Register;
import com.xk.kkrpc.register.RegisterFactory;
import com.xukang.kkrpcspringbootstater.annotation.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 服务提供者注解实现
 * 注册服务
 */
public class RpcProviderBootstrap implements BeanPostProcessor {
    /**
     * Bean 初始化后执行，注册服务
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 1. 获取属性值
        Class<?> beanClass = bean.getClass();
        RpcService rpcService = beanClass.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 需要注册服务
            // 1. 获取服务基本信息
            Class<?> interfaceClass = rpcService.interfaceClass();
            // 默认值处理
            if (interfaceClass == void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = rpcService.serviceVersion();
            // 本地注册
            LocalRegister.register(serviceName, beanClass);
            // 全局配置
            final RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            // 注册服务到注册中心
            Register registry = RegisterFactory.getInstance(rpcConfig.getRegister());
            ServiceMateInfo serviceMetaInfo = new ServiceMateInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getServerHost());
            serviceMetaInfo.setServicePort(rpcConfig.getServerPort());
            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException(serviceName + " 服务注册失败", e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
