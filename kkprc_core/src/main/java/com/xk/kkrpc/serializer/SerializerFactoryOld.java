package com.xk.kkrpc.serializer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化器工厂
 */
@Deprecated
public class SerializerFactoryOld {

    /**
     * 初始化序列化器 用于单例模式
     */
    public static final Map<String, Serializer> serializerMap = new ConcurrentHashMap() {{
        put(SerializerKeys.JDK, new JdkSerializer());
        put(SerializerKeys.JSON, new JsonSerializer());
        put(SerializerKeys.KRYO, new KryoSerializer());
        put(SerializerKeys.HESSIAN, new HessianSerializer());
    }};
    /**
     * 默认序列化器对象
     */
    private static final Serializer defaultSerializer = serializerMap.get(SerializerKeys.JDK);

    /**
     * 获取序列化器对象
     *
     * @param serializerKey
     * @return
     */
    public static Serializer getInstance(String serializerKey) {
        return serializerMap.getOrDefault(serializerKey, defaultSerializer);
    }

}
