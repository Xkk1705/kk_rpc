package com.xk.kkrpc.fault.tolerate;

import com.xk.kkrpc.spi.SpiLoader;

/**
 * 容错策略工厂(SPI机制实现)
 */
public class TolerateFactory {

    static {
        SpiLoader.load(TolerateStrategy.class);
    }

    /**
     * 默认负载均衡器
     */
    private static final TolerateStrategy DEFAULT_TOLERATE_STRATEGY = new FailFastTolerateStrategy();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static TolerateStrategy getInstance(String key) {
        return SpiLoader.getInstance(TolerateStrategy.class, key);
    }

}
