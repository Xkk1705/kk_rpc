package com.xk.kkrpc.provider;

import com.xk.kkrpc.bootstarp.ProviderBootstrap;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

public class EasyProvider {
    public static void main(String[] args) {
        // 要注册的服务
        List<ServiceRegisterInfo> serviceRegisterInfoList = new ArrayList<>();
        ServiceRegisterInfo serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);
        serviceRegisterInfoList.add(serviceRegisterInfo);

        // 服务提供者初始化
        ProviderBootstrap.init(serviceRegisterInfoList);
    }
}
