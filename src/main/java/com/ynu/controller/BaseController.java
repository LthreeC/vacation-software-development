package com.ynu.controller;

import com.ynu.service.ex.*;
import com.ynu.util.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * *控制层基类
 */
public class BaseController {
    public static final int OK = 200;

    // 项目中产生异常统一处理
    @ExceptionHandler(ServiceException.class) // 统一处理抛出异常
    public JsonResult<Void> handleException(Throwable e) {
        JsonResult<Void> result = new JsonResult<>(e);
        if (e instanceof UsernameDuplicatedException) {
            result.setState(4000);
            result.setMessage("注册失败！用户名已经被占用！");
        } else if (e instanceof InsertException) {
            result.setState(5000);
            result.setMessage("注册失败！插入数据异常！");
        }  else if (e instanceof UserNotFoundException) {
            result.setState(5001);
            result.setMessage("登录失败！用户数据不存在！");
        }  else if (e instanceof PasswordNotMatchException) {
            result.setState(5002);
            result.setMessage("登录失败！密码错误！");
        }
        return result;
    }
}
