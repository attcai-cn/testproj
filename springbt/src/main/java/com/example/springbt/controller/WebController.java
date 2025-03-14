package com.example.springbt.controller;


import cn.hutool.json.JSONUtil;
import com.example.springbt.common.Result;
import com.example.springbt.entity.Account;
import com.example.springbt.entity.Admin;
import com.example.springbt.entity.User;
import com.example.springbt.services.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.example.springbt.services.AdminService;

@RestController
public class WebController {

    @Resource
    AdminService adminServ;

    @Resource
    UserService userServ;

    @GetMapping("/hello")
    public Result hello() {
        return Result.ok("hello!!");
    }

    @GetMapping("/user")
    public Result user(String name) {
        String user = adminServ.user(name);
        return Result.ok(user);
    }



    @GetMapping("/admin")
    public Result admin(String name) {
        String admin = adminServ.admin(name);
        return Result.ok(admin);
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        String accStr = JSONUtil.toJsonStr(account);
        if(account.getRole().equals("ADMIN")) {
            Admin admin = JSONUtil.toBean(accStr, Admin.class);
            Admin dbAdmin = adminServ.login(admin);
            return Result.ok(dbAdmin);
        } else {
            User user = JSONUtil.toBean(accStr, User.class);
            User dbUser = userServ.login(user);
            return Result.ok(dbUser);
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userServ.register(user);
        return Result.ok();
    }

    @PostMapping("/userLogin")
    public Result userLogin(@RequestBody User user) {
        User dbUser = userServ.login(user);
        return Result.ok(dbUser);
    }
}
