package com.bjpowernode.money.web.config;

import com.bjpowernode.money.web.config.intercepors.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] addPathPatterns={
                "/loan/**"
        };

        String[] excludePathPatterns={
                "/money/loan/index",
                "/loan/loan",
                "/loan/loanInfo",
                "/loan/page/register",
                "/loan/page/login",
                "/loan/api/login",
                "/loan/login-captcha",
                "/loan/page/register",
                "/loan/api/register",
                "/loan/captcha",
                "/loan/checkPhone",
                "/loan/messageCode",
                "/loan/api/reg-sms-code",
                "/loan/page/realName",
                "/loan/api/realName"
        };

        registry.addInterceptor(userInterceptor)
                .addPathPatterns(addPathPatterns)
                .excludePathPatterns(excludePathPatterns);
    }
}