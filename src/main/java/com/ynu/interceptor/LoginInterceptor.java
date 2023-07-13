package com.ynu.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor {
    /**
     * 检测是否有uid数据 -> 没有则重定向到登录页面
     * @param request 请求对象
     * @param response 响应对象
     * @param handler 处理器(url+Controller
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object obj = request.getSession().getAttribute("uid");
        if (obj == null) {
            response.sendRedirect("/web/login.html");
            // 结束后续调用
            return false;
        }
        return true;
    }
}
