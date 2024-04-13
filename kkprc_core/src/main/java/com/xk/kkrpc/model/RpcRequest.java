package com.xk.kkrpc.model;

import com.xk.kkrpc.serializer.Serializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 远程调用请求类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RpcRequest implements Serializable {
    /**
     * 服务名
     */
    private String serviceName;

    /**
     * 服务方法名
     */
    private String methodName;


    /**
     * 服务方法名
     */
    private String version;


    /**
     * 方法参数类型列表
     */
    private Class<?>[] parameterTypes;

    /**
     * 方法参数
     */
    private Object[] args;
}
