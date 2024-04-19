package com.xukang.kkrpcspringbootstater.annotation;


import com.xukang.kkrpcspringbootstater.bootstarp.RpcConsumerBootstrap;
import com.xukang.kkrpcspringbootstater.bootstarp.RpcInitBootstrap;
import com.xukang.kkrpcspringbootstater.bootstarp.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 初始化rpc框架注解 引入rpc框架
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {

    /**
     * 需要启动 server
     *
     * @return
     */
    boolean needServer() default true;
}
