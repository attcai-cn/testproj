package com.example.springbt.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.server.HttpServerResponse;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.springbt.common.Result;
import com.example.springbt.entity.Admin;
import com.example.springbt.services.AdminService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.StringUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Resource
    AdminService adminService;

    @GetMapping("/selectAll")  // http://localhost:8080/admin/selectAll
    public Result selectAll() {
        List<Admin> adminList = adminService.selectAll(null);
        return Result.ok(adminList);
    }

    @GetMapping("/selectPage")  // http://localhost:8080/admin/selectPage
    public Result selectPage(@RequestParam(defaultValue = "1")Integer pageNum,
                             @RequestParam(defaultValue = "10")Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> pageInfo =  adminService.selectPage(pageNum, pageSize, admin);
        return Result.ok(pageInfo);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.ok();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.ok();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.ok();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list) {
        adminService.deleteBatch(list);
        return Result.ok();
    }

    // 导出数据
    @GetMapping("/export")
    public Result exportData(Admin admin, HttpServletResponse response) throws Exception {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String time = sdf.format(date);

        String ids = admin.getIds();
        if(StrUtil.isNotBlank(ids)){
            String[] idsArr = ids.split(",");
            admin.setIdsArr(idsArr);
        }


        List<Admin> list = adminService.selectAll(admin); // 获取所有数据

        ExcelWriter writer = ExcelUtil.getWriter(true); // 创建ExcelWriter对象
        writer.setOnlyAlias(true);
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "姓名");
//        writer.addHeaderAlias("password", "密码");
        writer.addHeaderAlias("email", "邮箱");
        writer.addHeaderAlias("phone", "电话");

        writer.write(list); // 将数据写入Excel

        String fileName = URLEncoder.encode("用户信息表"+time+".xlsx", StandardCharsets.UTF_8); // 设置文件名
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8"); // 设置响应类型
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName); // 设置响应头

        //输出流
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os, true); // 将数据写入输出流
        writer.close(); // 关闭writer
        os.close();
        return Result.ok("导出成功");
    }

    // 导入数据
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
//输入流
        InputStream inputStream = file.getInputStream();
        // 创建ExcelReader对象，读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(inputStream);

        reader.addHeaderAlias("账号", "username");
        reader.addHeaderAlias("姓名", "name");
//        reader.addHeaderAlias("密码", "password");
        reader.addHeaderAlias("邮箱", "email");
        reader.addHeaderAlias("电话", "phone");

        // 读取Excel数据
        List<Admin> list = reader.readAll(Admin.class);

        // 批量插入数据
        for (Admin admin : list) {
            adminService.add(admin);
        }

        return Result.ok("导入成功");
    }

}
