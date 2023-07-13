package com.ynu.service.impl;

import com.ynu.entity.User;
import com.ynu.mapper.UserMapper;
import com.ynu.service.IUserService;
import com.ynu.service.ex.*;
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

    @Override
    public User login(String username, String password) {
        User result = userMapper.findByUsername(username);
        if (result == null) {
            throw new UserNotFoundException("用户不存在");
        }

        // 将当前密码加密后进行比较
        String oldPassword = result.getPassword();
        String salt = result.getSalt();
        String newMd5Password = getMd5Password(password, salt);
        if (!newMd5Password.equals(oldPassword)) {
            throw new PasswordNotMatchException("密码错误");
        }

        // 判断is_delete
        if (result.getIsDelete() == 1) {
            throw new UserNotFoundException("用户不存在");
        }

        //* 提升系统性能
        //* 数据中转压缩
        User user = new User();
        user.setUid(result.getUid());
        user.setUsername(result.getUsername());
        user.setAvatar(result.getAvatar());
        return user;
    }


    @Override
    public void changePassword(Integer uid, String username, String oldPassword, String newPassword) {
        User result = userMapper.findByUid(uid);
        if (result == null || result.getIsDelete() == 1) {
            throw new UserNotFoundException("用户不存在");
        }
        String oldMd5Password = getMd5Password(oldPassword, result.getSalt());
        if (!result.getPassword().equals(oldMd5Password)) {
            throw new PasswordNotMatchException("密码错误");
        }

        // 更新面膜
        String newMd5Password = getMd5Password(newPassword, result.getSalt());
        Integer rows = userMapper.updatePasswordByUid(uid, newMd5Password, username, new Date());
        if (rows != 1) {
            throw new UpdateException("更新密码时出现未知错误，请联系系统管理员");
        }

    }

    private String getMd5Password(String password, String salt) {
        for (int i = 0; i < 3; ++i) {
            password = DigestUtils.md5DigestAsHex((salt + password + salt).getBytes()).toUpperCase();

        }
        return password;
    }


}
