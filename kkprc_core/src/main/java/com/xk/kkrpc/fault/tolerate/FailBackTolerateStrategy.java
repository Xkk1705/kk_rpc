package com.xk.kkrpc.fault.tolerate;

import java.util.Map;

/**
 * 服务降级 容错策略
 */
public class FailBackTolerateStrategy implements TolerateStrategy{

    @Override
    public Object doTolerate(Map<String, Object> context, Exception e) {
        // 获取降级服务并调用
        // todo 待实现
        return null;
    }
}
