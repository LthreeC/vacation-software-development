package com.ynu.mapper;

import com.ynu.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


// @RunWith(SpringRunner.class)注解是一个测试启动器，可以加载Springboot测试注解
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserMapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void insert() {
        User user = new User();
        user.setUsername("fdsa");
        user.setPassword("123456");
        Integer rows = userMapper.insert(user);
        System.out.println("rows=" + rows);
    }

    @Test
    public void updatePasswordByUid() {
        userMapper.updatePasswordByUid(10, "abcd", "root", new Date());

    }

    /**
     * 根据用户id查询用户数据
     * @param uid 用户id
     * @return 匹配的用户数据，如果没有匹配的用户数据，则返回null
     */
    @Test
    public void findByUid() {
        System.out.println(userMapper.findByUid(10));
    }

}

