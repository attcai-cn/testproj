package com.example.springbt.services;

import cn.hutool.core.util.StrUtil;
import com.example.springbt.entity.Account;
import com.example.springbt.entity.Admin;
import com.example.springbt.entity.User;
import com.example.springbt.exception.CustomerException;
import com.example.springbt.mapper.AdminMapper;
import com.example.springbt.mapper.UserMapper;
import com.example.springbt.utils.tokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AdminService {

    @Resource
    AdminMapper adminMapper;

    @Resource
    UserMapper userMapper;

    public String admin(String name) {
        if(name.equals("admin")) {
            return "admin";
        }else{
            throw new CustomerException("账号错误");
        }
    }

    public String user(String name) {
        if(name.equals("user")) {
            return "user";
        }else{
            throw new CustomerException("账号错误");
        }
    }

    public List<Admin> selectAll(Admin admin) {
        return adminMapper.selectAll(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.delById(id);
    }

    public void add(Admin admin) {
        Admin dbAdmin = selectByUsername(admin.getUsername());
        if(dbAdmin != null) {
            throw new CustomerException("账号已存在");
        }

        if(StrUtil.isBlank(admin.getPassword())) {
            admin.setPassword("user123456");
        }
        adminMapper.insertInto(admin);
    }

    public void deleteBatch(List<Admin> admin) {
        for (Admin admin1 : admin) {
            adminMapper.delById(admin1.getId());
        }
    }

    public void update(Admin admin) {
//        Admin dbAdmin = selectByUsername(admin.getUsername());
//        admin.setId(dbAdmin.getId());
        adminMapper.updateById(admin);
    }

    public PageInfo<Admin> selectPage(Integer pageNum, Integer pageSize,Admin admin) {
//        Account account = tokenUtils.getCurrentUser();
        PageHelper.startPage(pageNum, pageSize);  // 开启分页查询
        List<Admin> list = adminMapper.selectAll(admin);
        return PageInfo.of(list);
    }

    public Admin selectByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    public Admin login(Admin admin) {
       Admin adminDb = adminMapper.selectByUsername(admin.getUsername());
        if(adminDb == null) {
            throw new CustomerException("账号不存在");
        }
        if(!adminDb.getPassword().equals(admin.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        if(!Objects.equals(adminDb.getRole(), "ADMIN")) {
            throw new CustomerException("账号非管理员");
        }

        //设置鉴权token返回给前端
        String token = tokenUtils.createToken(adminDb.getId()+"-ADMIN",adminDb.getPassword());
        adminDb.setToken(token);

        return adminDb;
    }

    public Admin selectById(int i) {
        return adminMapper.selectById(i);
    }

//    public Admin userLogin(Admin admin) {
//        Admin adminDb = adminMapper.selectByUsername(admin.getUsername());
//        if(adminDb == null) {
//            throw new CustomerException("账号不存在");
//        }
//        if(!adminDb.getPassword().equals(admin.getPassword())) {
//            throw new CustomerException("账号或密码错误");
//        }
//        if(Objects.equals(adminDb.getRole(), "ADMIN")) {
//            throw new CustomerException("账号非用户");
//        }
//        return adminDb;
//    }

//    public void register(User user) {
//        Admin dbAdmin = selectByUsername(admin.getUsername());
//        User dbUser = userMapper.selectByUsername(user.getUsername());
//        if(dbUser != null) {
//            throw new CustomerException("账号已存在");
//        }
//        if(StrUtil.isBlank(user.getPassword())) {
//            user.setPassword("user123456");
//        }
//        adminMapper.insertInto(admin);
//        userMapper.insertInto(user);
//    }

}
