package com.xk.kkrpc.consumer;

import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.common.service.UserService;
import com.xk.kkrpc.consumer.proxy.ServiceProxyFactory;

/**
 * 服务消费者
 */
public class EasyConsumerExample {
    public static void main(String[] args) {
        //调用提供者的getUser方法
        UserService userService = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setName("xukang");
        User user1 = userService.getUser(user);
        System.out.println(user1.getName());
    }
}
