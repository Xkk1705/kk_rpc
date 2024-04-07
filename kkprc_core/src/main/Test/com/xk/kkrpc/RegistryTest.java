package com.xk.kkrpc;

import com.xk.kkrpc.config.RegisterConfig;
import com.xk.kkrpc.register.EtcdRegistry;
import com.xk.kkrpc.register.Register;
import org.junit.Before;
import org.junit.Test;

public class RegistryTest {

    final Register registry = new EtcdRegistry();

    @Before
    public void init() {
        RegisterConfig registryConfig = new RegisterConfig();
        registryConfig.setAddress("http://localhost:2379");
        registry.init(registryConfig);
    }


    @Test
    public void heartBeat() throws Exception {
        // init 方法中已经执行心跳检测了
        // register();
        // 阻塞 1 分钟
        Thread.sleep(60 * 1000L);
    }
}