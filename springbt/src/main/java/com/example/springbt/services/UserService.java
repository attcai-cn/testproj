package com.example.springbt.services;

import cn.hutool.core.util.StrUtil;
import com.example.springbt.entity.Admin;
import com.example.springbt.entity.User;
import com.example.springbt.entity.User;
import com.example.springbt.exception.CustomerException;
import com.example.springbt.mapper.UserMapper;
import com.example.springbt.mapper.UserMapper;
import com.example.springbt.utils.tokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

//    public String admin(String name) {
//        if(name.equals("admin")) {
//            return "admin";
//        }else{
//            throw new CustomerException("账号错误");
//        }
//    }
//
//    public String user(String name) {
//        if(name.equals("user")) {
//            return "user";
//        }else{
//            throw new CustomerException("账号错误");
//        }
//    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public void deleteById(Integer id) {
        userMapper.delById(id);
    }

    public void add(User user) {
        User dbUser = selectByUsername(user.getUsername());
        if(dbUser != null) {
            throw new CustomerException("账号已存在");
        }

        if(StrUtil.isBlank(user.getPassword())) {
            user.setPassword("user123456");
        }
        userMapper.insertInto(user);
    }

    public void deleteBatch(List<User> user) {
        for (User user1 : user) {
            userMapper.delById(user1.getId());
        }
    }

    public void update(User user) {
//        User dbUser = selectByUsername(user.getUsername());
//        user.setId(dbUser.getId());
        userMapper.updateById(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize,User user) {
        PageHelper.startPage(pageNum, pageSize);  // 开启分页查询
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public User selectByUsername(String username) {
        return userMapper.selectByUsername(username);
    }

    public User login(User user) {
       User userDb = userMapper.selectByUsername(user.getUsername());
        if(userDb == null) {
            throw new CustomerException("账号不存在");
        }
        if(!userDb.getPassword().equals(user.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }

        String token = tokenUtils.createToken(userDb.getId()+"-ADMIN",userDb.getPassword());
        userDb.setToken(token);

        return userDb;
    }


    public void register(User user) {
//        Admin dbAdmin = selectByUsername(admin.getUsername());
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if(dbUser != null) {
            throw new CustomerException("账号已存在");
        }
        if(StrUtil.isBlank(user.getPassword())) {
            user.setPassword("user123456");
        }
//        adminMapper.insertInto(admin);
        userMapper.insertInto(user);
    }

    public User selectById(int i) {
        return userMapper.selectById(i);
    }
}
