package com.ynu.service.impl;

import com.ynu.entity.User;
import com.ynu.mapper.UserMapper;
import com.ynu.service.IUserService;
import com.ynu.service.ex.InsertException;
import com.ynu.service.ex.UsernameDuplicatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.UUID;

// Service 将当前类对象给spring管理，自动创建对象以及维护
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public void reg(User user) {
        // 这里检测注册，先判断是否注册
        String username = user.getUsername();
        User result = userMapper.findByUsername(username);
        if (result != null) {
            // 已经注册，抛出异常
            throw new UsernameDuplicatedException("用户名(" + username + ")被占用");
        }

        // 密码加密处理 md5
        // 串+password+串 --》交给md5加密 连续加载三次
        // 盐值 + password + 盐值 （就是一个随机的字符串
        String oldPassword = user.getPassword();
        // 随机生成盐值
        String salt = UUID.randomUUID().toString().toUpperCase();

        user.setSalt(salt);

        String md5Password = getMd5Password(oldPassword, salt);
        user.setPassword(md5Password);

        // 补全信息
        user.setIsDelete(0);
        user.setCreatedUser(user.getUsername());
        user.setModifiedUser(user.getUsername());
        Date date = new Date();
        user.setCreatedTime(date);
        user.setModifiedTime(date);

        Integer rows = userMapper.insert(user);
        if (rows != 1) {
            throw new InsertException("插入用户数据时出现未知错误，请联系系统管理员k");
        }
    }

    private String getMd5Password(String password, String salt) {
        for (int i = 0; i < 3; ++i) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();

        }
        return password;
    }
}
