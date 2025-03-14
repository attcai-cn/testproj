package com.example.springbt.mapper;
import  com.example.springbt.entity.Admin;
import  java.util.List;

public interface AdminMapper {

    List<Admin> selectAll(Admin admin);

    void insertInto(Admin admin);

    Admin selectByUsername(String username);

    void updateById(Admin admin);

    void delById(Integer id);

    Admin selectById(int i);
}
