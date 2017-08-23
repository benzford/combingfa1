package com.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"com.seckill.service"})
@MapperScan(basePackages = {"com.seckill.dao"})
public class AppStart {
    public static void main(String[] args) {
        SpringApplication.run(AppStart.class,args);
    }
}
