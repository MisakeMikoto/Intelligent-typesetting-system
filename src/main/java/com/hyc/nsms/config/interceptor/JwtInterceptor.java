package com.hyc.nsms.config.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hyc.nsms.exception.MyException;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.model.result.ResultCodeEnum;
import com.hyc.nsms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//token拦截器
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) {
        String token = httpServletRequest.getHeader("token");// 从 http 请求头中取出 token

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        // 执行认证
        if (StringUtils.isEmpty(token)) {
            throw new MyException(ResultCodeEnum.TOKEN_ERROR);
        }
        // 获取token中的userId
        String userId;
        try {
            userId = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException j) {
            throw new MyException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 根据token中的userId查询数据库
        User user = userService.getById(userId);
        if (user == null) {
            throw new MyException(ResultCodeEnum.USER_NOT_EXISTS);
        }

        // 用户名加签验证 token
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(user.getUsername())).build();
        try {
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new MyException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }

        return true;
    }
}
