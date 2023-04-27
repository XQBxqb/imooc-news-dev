package com.imooc.user;

import tk.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.IOException;

/**
 * @author hp
 * @date 2023-03-05 15:26
 * @explain
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@MapperScan(basePackages = {"com.imooc.user.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
