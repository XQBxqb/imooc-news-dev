package com.imooc.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author hp
 * @date 2023-03-05 15:26
 * @explain
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@MapperScan(basePackages = {"com.imooc.article.mapper"})
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class,args);
    }
}
