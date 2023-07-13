package com.ynu.controller;

import com.ynu.entity.User;
import com.ynu.service.IUserService;
import com.ynu.service.ex.InsertException;
import com.ynu.service.ex.UsernameDuplicatedException;
import com.ynu.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@RestController
@RequestMapping("users")
public class UserController extends BaseController {
    @Autowired
    private IUserService userService;

    @RequestMapping("reg")
//    @ResponseBody
    public JsonResult<Void> reg(User user) {
        userService.reg(user);
        return new JsonResult<>(OK);
    }
}
