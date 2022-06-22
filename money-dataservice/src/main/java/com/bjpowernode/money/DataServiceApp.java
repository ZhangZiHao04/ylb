package com.bjpowernode.money;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableDubboConfiguration
@MapperScan("com.bjpowernode.money.mapper")
@SpringBootApplication
public class DataServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(DataServiceApp.class,args);
    }
}