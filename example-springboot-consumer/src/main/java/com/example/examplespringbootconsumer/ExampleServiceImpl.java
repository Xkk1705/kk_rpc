package com.example.examplespringbootconsumer;

import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.common.service.UserService;
import com.xukang.kkrpcspringbootstater.annotation.RpcReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
public class ExampleServiceImpl {
    @RpcReference
    private UserService userService;

    public void test() {
        User user = new User();
        user.setName("xukang");
        User resultUser = userService.getUser(user);
        System.out.println(resultUser.getName());
    }

}
