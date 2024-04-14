package com.xk.kkrpc.fault.retry;

/**
 * 重试常量
 */
public interface RetryStrategyKeys {
    /**
     * 不重试
     */
    String NO_RETRY = "no";

    /**
     * 随机
     */
    String FIXED_TIME_RETRY = "fixed";
}
