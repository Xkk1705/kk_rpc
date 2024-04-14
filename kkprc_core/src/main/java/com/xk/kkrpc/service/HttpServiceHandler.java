package com.xk.kkrpc.service;

import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.model.RpcRequest;
import com.xk.kkrpc.model.RpcResponse;
import com.xk.kkrpc.register.LocalRegister;
import com.xk.kkrpc.serializer.Serializer;
import com.xk.kkrpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * HTTP 请求处理
 * 服务器请求处理器
 * 用于自定义封装服务器的系列化、请求、结果返回
 */
public class HttpServiceHandler implements Handler<HttpServerRequest> {
    @Override
    public void handle(HttpServerRequest request) {

        // 指定序列化器
//        final Serializer serializer = new JdkSerializer();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());

        // 记录日志
        System.out.println("Received request: " + request.method() + " " + request.uri());

        // 异步处理 HTTP 请求
        //1. 反序列化请求为对象，并从请求对象中获取参数。
        request.bodyHandler(body -> {
            byte[] bytes = body.getBytes();
            RpcRequest rpcRequest = null;
            try {
                rpcRequest = serializer.deserialize(bytes, RpcRequest.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 构造响应结果对象
            RpcResponse rpcResponse = new RpcResponse();
            // 如果请求为 null，直接返回
            if (rpcRequest == null) {
                rpcResponse.setMessage("rpcRequest is null");
                doResponse(request, rpcResponse, serializer);
                return;
            }

            try {
                //2. 根据服务名称从本地注册器中获取到对应的服务实现类。
                // 获取要调用的服务实现类，通过反射调用
                Class<?> implClass = LocalRegister.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
                //3. 通过反射机制调用方法，得到返回结果。
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());
                // 封装返回结果
                //4. 对返回结果进行封装和序列化，并写入到响应中。
//                String jsonStr = JSONUtil.toJsonStr(result);
                rpcResponse.setData(result);
                rpcResponse.setDataType(method.getReturnType());
                rpcResponse.setMessage("ok");
            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            // 响应
            doResponse(request, rpcResponse, serializer);
        });
    }

    /**
     * 响应
     *
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {
        HttpServerResponse httpServerResponse = request.response()
                .putHeader("content-type", "application/json");
        try {
            // 序列化
            byte[] serialized = serializer.serialize(rpcResponse);
            httpServerResponse.end(Buffer.buffer(serialized));
        } catch (IOException e) {
            e.printStackTrace();
            httpServerResponse.end(Buffer.buffer());
        }
    }
}