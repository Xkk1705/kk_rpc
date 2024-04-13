package com.xk.kkrpc;

import cn.hutool.core.util.IdUtil;
import com.xk.kkrpc.constant.RpcConstant;
import com.xk.kkrpc.model.RpcRequest;
import com.xk.kkrpc.protocol.constant.ProtocolConstant;
import com.xk.kkrpc.protocol.enm.ProtocolMessageSerializerEnum;
import com.xk.kkrpc.protocol.enm.ProtocolMessageStatusEnum;
import com.xk.kkrpc.protocol.enm.ProtocolMessageTypeEnum;
import com.xk.kkrpc.protocol.model.ProtocolMessage;
import com.xk.kkrpc.service.tcp.ProtocolMessageEncoder;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * 测试tcp编码和解码器
 */
public class ProtocolMessageTest {

    @Test
    public void testEncodeAndDecode() throws IOException {
        // 构造消息
        ProtocolMessage<RpcRequest> protocolMessage = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"aaa", "bbb"});
        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer encodeBuffer = ProtocolMessageEncoder.encode(protocolMessage);
        ProtocolMessage<?> message = ProtocolMessageEncoder.ProtocolMessageDecoder.decode(encodeBuffer);
        Assert.assertNotNull(message);
    }

}