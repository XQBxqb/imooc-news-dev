package com.imooc.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author hp
 * @date 2023-03-05 15:26
 * @explain
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@MapperScan(basePackages = {"com.imooc.admin.mapper"})
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class,args);
    }
}
