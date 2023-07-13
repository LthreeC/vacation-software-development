package com.ynu.service;

import com.ynu.entity.User;

public interface IUserService {
    void reg(User user);

    /**
     * 用户登录
     * @param username
     * @param password
     * @return 登录成功的用户数据
     */
    User login(String username, String password);

    void changePassword(Integer uid, String username, String oldPassword, String Password);
}
