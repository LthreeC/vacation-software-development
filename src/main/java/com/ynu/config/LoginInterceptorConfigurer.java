package com.ynu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 加载当前类到springboot容器中
public class LoginInterceptorConfigurer implements WebMvcConfigurer {
    // 配置白名单
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        HandlerInterceptor interceptor = new LoginInterceptor();
//        List<String> patterns = new ArrayList<>();
//        patterns.add("/bootstrap3/**");
//        patterns.add("/css/**");
//        patterns.add("/images/**");
//        patterns.add("/js/**");
//        patterns.add("/web/register.html");
//        patterns.add("/web/login.html");
//        patterns.add("/web/index.html");
//        patterns.add("/web/product.html");
//        patterns.add("/users/reg");
//        patterns.add("/users/login");
//
//
//        // 完成拦截器的注册
//        registry.addInterceptor(interceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns(patterns);
    // 表示要拦截的url是什么
    }
}
