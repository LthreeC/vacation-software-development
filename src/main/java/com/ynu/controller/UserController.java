package com.ynu.controller;

import com.ynu.entity.User;
import com.ynu.service.IUserService;
import com.ynu.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    /**
     * 接受数据方式：请求参数列表设置为pojo类型
     * @param user
     * @return
     */

    @RequestMapping("reg")
//    @ResponseBody
    public JsonResult<Void> reg(User user) {
        userService.reg(user);
        return new JsonResult<>(OK);
    }

    @RequestMapping("login")
    public JsonResult<User> login(String username, String password) {
        User user = userService.login(username, password);
        return new JsonResult<User>(OK, user);
    }
}
