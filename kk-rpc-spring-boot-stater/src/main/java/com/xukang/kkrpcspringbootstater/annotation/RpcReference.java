package com.xukang.kkrpcspringbootstater.annotation;

import com.xk.kkrpc.constant.RpcConstant;
import com.xk.kkrpc.fault.retry.RetryStrategyKeys;
import com.xk.kkrpc.fault.tolerate.TolerantStrategyKeys;
import com.xk.kkrpc.loadbalancer.LoadBalanceKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 服务消费者注解，在需要注入服务代理对象的属性上使用，类似 Spring 中的 @Resource 注解。
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface RpcReference {
    /**
     * 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     * 版本
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     * 负载均衡器
     */
    String loadBalancer() default LoadBalanceKeys.ROUND_ROBIN;

    /**
     * 重试策略
     */
    String retryStrategy() default RetryStrategyKeys.NO_RETRY;

    /**
     * 容错策略
     */
    String tolerantStrategy() default TolerantStrategyKeys.FAIL_FAST;

    /**
     * 模拟调用
     */
    boolean mock() default false;
}
