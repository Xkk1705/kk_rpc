package com.xk.kkrpc.fault.retry;

import cn.hutool.http.HttpResponse;
import com.github.rholder.retry.*;
import com.xk.kkrpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间重试
 */
@Slf4j
public class FixedTimeRetryStrategy implements RetryStrategy {
    @Override
    public Object doRetry(Callable<Object> callable) throws Exception {
        Retryer<Object> retryer = RetryerBuilder.<Object>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数 {}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
