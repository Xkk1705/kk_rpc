package com.xk.kkrpc.fault.tolerate;

import java.util.Map;

/**
 * 服务转移到其他节点 容错策略
 */
public class FailOverTolerateStrategy implements TolerateStrategy{

    @Override
    public Object doTolerate(Map<String, Object> context, Exception e) {
        // 跟具服务的上下文信息 转发到其他可用节点
        // todo 待实现
        return null;
    }
}
