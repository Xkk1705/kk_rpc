package com.example.examplespringbootprovider;

import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.common.service.UserService;
import com.xukang.kkrpcspringbootstater.annotation.RpcService;
import org.springframework.stereotype.Service;

@Service
@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public User getUser(User user) {
        System.out.println(user.getName());
        return user;
    }
}
