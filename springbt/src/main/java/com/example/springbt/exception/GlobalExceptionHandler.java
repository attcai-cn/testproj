package com.example.springbt.exception;

import com.example.springbt.common.Result;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import org.slf4j.Logger;

// 全局异常捕获
@ControllerAdvice("com.example.springbt.controller")
public class GlobalExceptionHandler {
    private static final Logger Log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody //result->json
    public Result error(Exception e){
        Log.error("系统异常",e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(CustomerException.class)
    @ResponseBody
    public Result error(CustomerException e){
        Log.error("自定义异常",e);
        return Result.error(e.getCode(),e.getMsg());
    }

}
