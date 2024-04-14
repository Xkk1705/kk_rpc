package com.xk.kkrpc.fault.retry;


import java.util.concurrent.Callable;

/**
 * 重试策略 不重试
 */
public class NoRetryStrategy implements RetryStrategy {
    @Override
    public Object doRetry(Callable<Object> callable) throws Exception {
        return callable.call();
    }
}
