package com.xk.kkrpc.provider;

import com.xk.kkrpc.common.model.User;
import com.xk.kkrpc.common.service.UserService;

public class UserServiceImpl implements UserService {
    public User getUser(User user) {
        System.out.println("用户名为：" + user.getName());
        return user;
    }
}
