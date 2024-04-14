package com.xk.kkrpc.fault.retry;

import com.xk.kkrpc.loadbalancer.LoadBalancer;
import com.xk.kkrpc.loadbalancer.RoundRobinLoadBalancer;
import com.xk.kkrpc.spi.SpiLoader;

/**
 * 重试机制(SPI机制实现)
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试机制
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key) {
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
