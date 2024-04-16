package com.xk.kkrpc.fault.tolerate;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理 容错机制
 */
@Slf4j
public class FailSafeTolerateStrategy implements TolerateStrategy{
    @Override
    public Object doTolerate(Map<String, Object> context, Exception e) {
        log.info("服务发生错误了。。。静默处理");
        return new Object();
    }
}
