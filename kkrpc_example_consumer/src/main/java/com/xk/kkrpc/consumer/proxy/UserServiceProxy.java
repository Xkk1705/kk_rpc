package com.xk.kkrpc.consumer.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.model.RpcRequest;
import com.xk.kkrpc.model.RpcResponse;
import com.xk.kkrpc.serializer.JdkSerializer;
import com.xk.kkrpc.serializer.Serializer;

import java.io.IOException;

public class UserServiceProxy implements UserService {

    public User getUser(User user) {
        // 指定序列化器
        Serializer serializer = new JdkSerializer();

        // 发请求
        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .parameterTypes(new Class[]{User.class})
                .args(new Object[]{user})
                .build();
        try {
            byte[] bodyBytes = serializer.serialize(rpcRequest);
            byte[] result;
            try (HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bodyBytes)
                    .execute()) {
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return JSONUtil.toBean(rpcResponse.getData(), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}