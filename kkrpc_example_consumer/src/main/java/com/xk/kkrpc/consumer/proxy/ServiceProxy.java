package com.xk.kkrpc.consumer.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.model.RpcRequest;
import com.xk.kkrpc.model.RpcResponse;
import com.xk.kkrpc.serializer.JdkSerializer;
import com.xk.kkrpc.serializer.Serializer;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 服务类动态代理
 * 代理增强 封装请求参数发送请求
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化
        Serializer serializer = new JdkSerializer();
        // 封装rpc请求
        RpcRequest rpcRequest = RpcRequest.builder().serviceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .args(args)
                .build();
        // 发送请求
        try {
            // 序列化
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            // 发送请求
            // todo 注意，这里地址被硬编码了（需要使用注册中心和服务发现机制解决）
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
            try (HttpResponse httpResponse = HttpRequest.post(url)
                    .body(bodyBytes)
                    .execute()) {
                byte[] result = httpResponse.bodyBytes();
                // 反序列化
                RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
                return JSONUtil.toBean(rpcResponse.getData(), User.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
