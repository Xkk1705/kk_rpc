package com.xk.kkrpc.consumer.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.xk.kkrpc.RpcApplication;
import com.xk.kkrpc.config.RpcConfig;
import com.xk.kkrpc.constant.RegisterConstant;
import com.xk.kkrpc.fault.retry.RetryStrategy;
import com.xk.kkrpc.fault.retry.RetryStrategyFactory;
import com.xk.kkrpc.loadbalancer.LoadBalanceFactory;
import com.xk.kkrpc.loadbalancer.LoadBalancer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            //获取重试机制
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(RpcApplication.getRpcConfig().getRetry());
            return retryStrategy.doRetry(() -> doRequest(rpcRequest, serializer));
//            return doRequest(rpcRequest, serializer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 向服务端发送请求
     * 负载均衡
     * 重试机制
     *
     * @param rpcRequest
     * @param serializer
     * @return
     * @throws IOException
     */
    public Object doRequest(RpcRequest rpcRequest, Serializer serializer) throws IOException {
        // 序列化
        byte[] bodyBytes = serializer.serialize(rpcRequest);
        // 发送请求
        Register register = RegisterFactory.getInstance(RpcApplication.getRpcConfig().getRegister());
        ServiceMateInfo serviceMetaInfo = new ServiceMateInfo();
        serviceMetaInfo.setServiceName(rpcRequest.getServiceName());
        serviceMetaInfo.setServiceVersion(RegisterConstant.SERVICE_VERSION);
        //根据前缀key发现服务节点列表
        List<ServiceMateInfo> serviceMateInfos = register.serviceDiscovery(serviceMetaInfo.getServiceKey());
        // 使用指定的负载均衡器 获取服务信息
        LoadBalancer loadBalancer = LoadBalanceFactory.getInstance(RpcApplication.getRpcConfig().getLoadbalancer());
        // 将调用方法名作为负载均衡的参数
        Map<String, Object> requestPara = new HashMap<>();
        requestPara.put("methodName", rpcRequest.getMethodName());
        ServiceMateInfo serviceMateInfo = loadBalancer.Select(requestPara, serviceMateInfos);
        String serviceAddress = serviceMateInfo.getServiceAddress();
//            String url = rpcConfig.getServerHost() + ":" + rpcConfig.getServerPort();
        try (HttpResponse httpResponse = HttpRequest.post(serviceAddress)
                .body(bodyBytes)
                .execute()) {
            byte[] result = httpResponse.bodyBytes();
            // 反序列化
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        }
    }
}
