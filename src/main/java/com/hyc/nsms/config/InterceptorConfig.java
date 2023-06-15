package com.hyc.nsms.config;

import com.hyc.nsms.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//拦截器配置
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")// 拦截所有请求，通过判断是否有token注解决定是否需要登录
                .excludePathPatterns(//添加不拦截路径
                        "/user/login",//登录
                        "/user/register",//注册
                        "/files/**"//文件上传和下载
                );
    }
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
