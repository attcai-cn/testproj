package com.example.springbt.mapper;

import com.example.springbt.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> selectAll(User user);

    void insertInto(User user);

    User selectByUsername(String username);

    void updateById(User user);

    void delById(Integer id);

    User selectById(int i);
}
