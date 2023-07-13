package com.ynu.service;

import com.ynu.entity.User;
import com.ynu.mapper.UserMapper;
import com.ynu.service.ex.ServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTests {
    @Autowired
    private IUserService userMapper;

    @Test
    public void reg() {
        try {
            User user = new User();
            user.setUsername("godd");
            user.setPassword("123456");
            userMapper.reg(user);
            System.out.println("goood");
        } catch (ServiceException e) {
            // 获取对象，名称
            System.out.println(e.getClass().getSimpleName());
            // 获取异常信息
            System.out.println(e.getMessage());
        }
    }

}

