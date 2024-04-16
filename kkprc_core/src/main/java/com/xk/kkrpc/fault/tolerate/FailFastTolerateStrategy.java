package com.xk.kkrpc.fault.tolerate;

import java.util.Map;

/**
 * 快速失败容错策略
 */
public class FailFastTolerateStrategy implements TolerateStrategy{

    @Override
    public Object doTolerate(Map<String, Object> context, Exception e) {
        throw new RuntimeException("服务发生错误了",e);
    }
}
