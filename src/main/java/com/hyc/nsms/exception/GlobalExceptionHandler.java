package com.hyc.nsms.exception;

import com.hyc.nsms.model.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//全局异常处理
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail();
    }

    //自定义异常处理方法
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result error(MyException e) {
        return Result.build(e.getCode(), e.getMessage());
    }
}
