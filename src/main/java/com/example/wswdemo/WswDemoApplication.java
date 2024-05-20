package com.example.wswdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.wswdemo.mapper")
public class WswDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(WswDemoApplication.class, args);
    }

}


