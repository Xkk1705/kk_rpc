package com.xk.kkrpc.fault.retry;

import java.util.concurrent.Callable;

/**
 * 重试机制接口
 */
public interface RetryStrategy {
    /**
     * 重试策略的具体实现
     * @param callable
     * @return
     */
    Object doRetry(Callable<Object> callable) throws Exception;
}
