package com.example.springbt.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springbt.entity.Account;
import com.example.springbt.entity.Admin;
import com.example.springbt.exception.CustomerException;
import com.example.springbt.services.AdminService;
import com.example.springbt.services.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {

    @Resource
    AdminService adminService;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        String token = request.getHeader("Token");
        if(StrUtil.isEmpty(token)) {
            //如果没有拿到，获取请求参数中的token
            token = request.getParameter("Token");
        }
        if(StrUtil.isBlank(token)) {
            throw new CustomerException("401","您无权限");
        }

        //验证token
        String audience = JWT.decode(token).getAudience().getFirst();
        String[] audSplit = audience.split("-");
        String userId = audSplit[0];
        String userRole = audSplit[1];
        Account account = null;

        try {
            if("ADMIN".equals(userRole)) {
                account = adminService.selectById(Integer.parseInt(userId));
            } else {
                account = userService.selectById(Integer.parseInt(userId));
            }
        } catch (Exception e){
            throw new CustomerException("401","您无权限");
        }
        if(account== null) {
            throw new CustomerException("401","您无权限");
        }

        //验证token签名
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
        try {
            jwtVerifier.verify(token);
        } catch (Exception e) {
            throw new CustomerException("401","您无权限");
        }
        return true;

    }
}
