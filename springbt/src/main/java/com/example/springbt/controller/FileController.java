package com.example.springbt.controller;


import cn.hutool.core.io.FileUtil;
import com.example.springbt.common.Result;
import com.example.springbt.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/download/{fileName}")
    public void downloadFile(@PathVariable String fileName, HttpServletResponse response) throws Exception {

        //文件下载路径： localhost:8080/files/download/文件名
        String filePath = System.getProperty("user.dir") + "/files/" + fileName;
        boolean exist = FileUtil.exist(filePath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }

        byte[] data = FileUtil.readBytes(filePath);
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
        ServletOutputStream os = response.getOutputStream();
        os.write(data);
        os.flush();
        os.close();
    }

    @PostMapping("/upload")
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        //文件上传路径： localhost:8080/files/upload
        String filePath = System.getProperty("user.dir") + "/files/";
        if(!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        byte[] data = file.getBytes();
        String fileName =  System.currentTimeMillis() + "_" +file.getOriginalFilename();
        URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        FileUtil.writeBytes(data, filePath + fileName);
        String url = "http://localhost:8080/files/download/" + fileName;
        return Result.ok(url);
    }
}
