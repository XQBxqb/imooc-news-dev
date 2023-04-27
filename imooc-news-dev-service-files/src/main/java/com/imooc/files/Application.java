package com.imooc.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author hp
 * @date 2023-03-05 15:26
 * @explain
 */
@ComponentScan(basePackages = {"com.imooc","org.n3r.idworker"})
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})//这里是springboot自动会启动datasource，因为它的依赖在common内，该项目依赖于common层内，所以要手动停止运行
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
