package com.example.springbt.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerResponse;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.springbt.entity.Account;
import com.example.springbt.mapper.AdminMapper;
import com.example.springbt.mapper.UserMapper;
import com.example.springbt.services.AdminService;
import com.example.springbt.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

public class tokenUtils {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    static AdminService staticAdminService;
    static UserService staticUserService;

    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
    }

    //创建token
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data)
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1))
                .sign(Algorithm.HMAC256(sign));
    }
    //获取当前用户
    public static Account getCurrentUser() {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        if(StrUtil.isBlank(token)) {
            return null;
        }
        String audience = JWT.decode(token).getAudience().get(0);
        String[] audSplit = audience.split("-");
        String userId = audSplit[0];
        String userRole = audSplit[1];
        Account account = null;

        try {
            if("ADMIN".equals(userRole)) {
                return staticAdminService.selectById(Integer.parseInt(userId));
            } else {
                return staticUserService.selectById(Integer.parseInt(userId));
            }
        } catch (Exception e){
            return null;
        }


    }
}