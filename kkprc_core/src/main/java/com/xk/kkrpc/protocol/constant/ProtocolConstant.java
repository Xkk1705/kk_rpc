package com.xk.kkrpc.protocol.constant;

/**
 * tcp协议全局常量
 */
public interface ProtocolConstant {
    byte PROTOCOL_VERSION = 0x1;
    int MESSAGE_HEADER_LENGTH = 17;
    byte PROTOCOL_MAGIC = 0x1;
}
