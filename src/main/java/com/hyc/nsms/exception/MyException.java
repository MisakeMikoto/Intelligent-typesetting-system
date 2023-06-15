package com.hyc.nsms.exception;

import com.hyc.nsms.model.result.ResultCodeEnum;
import lombok.Data;

//自定义全局异常类
@Data
public class MyException extends RuntimeException {
    private Integer code;

    //通过状态码和错误消息创建异常对象
    public MyException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    //接收枚举类型对象
    public MyException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "MyException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
