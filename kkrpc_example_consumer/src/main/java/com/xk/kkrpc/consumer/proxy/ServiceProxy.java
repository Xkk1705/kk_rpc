package com.xk.kkrpc.consumer.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.constant.RegisterConstant;
import com.xk.kkrpc.model.RpcRequest;
import com.xk.kkrpc.model.RpcResponse;
import com.xk.kkrpc.model.ServiceMateInfo;
import com.xk.kkrpc.register.Register;
import com.xk.kkrpc.register.RegisterFactory;
import com.xk.kkrpc.serializer.Serializer;
import com.xk.kkrpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 服务类动态代理
 * 代理增强 封装请求参数发送请求
 */
public class ServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //指定序列化
//        Serializer serializer = new JdkSerializer();
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Serializer serializer = SerializerFactory.getInstance(rpcConfig.getSerializer());
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
            Register register = RegisterFactory.getInstance(RpcApplication.getRpcConfig().getRegister());
            ServiceMateInfo serviceMetaInfo = new ServiceMateInfo();
            serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
            serviceMetaInfo.setServiceVersion(RegisterConstant.SERVICE_VERSION);
            //根据前缀key发现服务节点列表
            List<ServiceMateInfo> serviceMateInfos = register.serviceDiscovery(serviceMetaInfo.getServiceKey());
            ServiceMateInfo serviceMateInfo = serviceMateInfos.get(0);
            String serviceAddress = serviceMateInfo.getServiceAddress();
//            String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
            try (HttpResponse httpResponse = HttpRequest.post(serviceAddress)
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
