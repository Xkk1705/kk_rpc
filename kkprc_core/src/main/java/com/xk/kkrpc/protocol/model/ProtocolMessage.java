package com.xk.kkrpc.protocol.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tcp消息类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {
    /**
     * 消息头
     */
    private Header header;

    /**
     * 消息体
     */
    private T Body;
    @Data
    public static class Header {
        /**
         * 魔数 保证安全性
         */
        private byte magic;

        /**
         * 协议版本
         */
        private byte version;

        /**
         * 消息类型
         */
        private byte type;

        /**
         * 消息序列化方式
         */
        private byte serializer;
        /**
         * 消息状态
         */
        private byte status;
        /**
         * 消息体长度
         */
        private int bodyLength;
        /**
         * 请求id
         */
        private long requestId;

    }
}
