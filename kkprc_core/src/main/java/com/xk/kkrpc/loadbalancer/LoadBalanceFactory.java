package com.xk.kkrpc.loadbalancer;

import com.xk.kkrpc.spi.SpiLoader;

/**
 * 负载均衡器工厂(SPI机制实现)
 */
public class LoadBalanceFactory {

    static {
        SpiLoader.load(LoadBalancer.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final LoadBalancer DEFAULT_LOAD_BALANCE = new RoundRobinLoadBalancer();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static LoadBalancer getInstance(String key) {
        return SpiLoader.getInstance(LoadBalancer.class, key);
    }

}
