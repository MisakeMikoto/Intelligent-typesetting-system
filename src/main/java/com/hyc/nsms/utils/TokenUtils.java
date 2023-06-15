package com.hyc.nsms.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.hyc.nsms.model.entity.User;
import com.hyc.nsms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

//Token生成工具类
@Component
public class TokenUtils {
    private static final long EXPIRE_TIME = 60 * 60 * 1000;  //过期时间1小时

    @Autowired
    public static UserService staticUserService;

    @Autowired
    public UserService userService;

    @PostConstruct
    public void setUserService() {
        staticUserService = userService;
    }

    //生成token
    public static String getToken(String userId, String sign) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        String token = "";
        token = JWT.create().withAudience(userId) // 将 userId 保存到 token 里面
                .withExpiresAt(date) //1小时后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 userName 作为 token 的密钥
        return token;
    }

    //获取当前登录的用户信息
    public static User getCurrentUser() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (!StringUtils.isEmpty(token)) {
                String userId = JWT.decode(token).getAudience().get(0);
                return staticUserService.getById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}
