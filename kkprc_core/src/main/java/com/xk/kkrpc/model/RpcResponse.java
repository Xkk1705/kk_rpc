package com.xk.kkrpc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 远程调用响应类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcResponse implements Serializable {
    /**
     * 响应数据
     */
    private String data;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应数据类型（预留）
     */
    private Class<?> dataType;

    /**
     * 异常信息
     */
    private Exception exception;

}
