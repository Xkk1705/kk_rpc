package com.xk.kkrpc.fault.tolerate;

import java.util.Map;

/**
 *  容错策略
 */
public interface TolerateStrategy {

    /**
     * 容错方法
     * @param context 上下文（服务请求信息）
     * @param e 错误
     * @return
     */
    Object doTolerate(Map<String,Object> context,Exception e);

}
