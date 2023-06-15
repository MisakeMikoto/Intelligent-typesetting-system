package com.hyc.nsms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.hyc.nsms.mapper")
@SpringBootApplication
public class NsmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(NsmsApplication.class, args);
    }
}
