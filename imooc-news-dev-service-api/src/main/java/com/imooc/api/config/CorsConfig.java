package com.imooc.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


/*
 * @description:跨域问题的配置类
 * @author: 昴星
 * @time: 2023/3/11 13:58
 */
@Configuration
public class CorsConfig {

    public CorsConfig() {
    }

    @Bean
    public CorsFilter corsFilter() {
        // 1. 添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        //允许跨域请求的域名
        //config.addAllowedOrigin("http://www.imoocnews.com");
        config.addAllowedOrigin("http://www.imoocnews.com:9091");
        config.addAllowedOrigin("http://writer.imoocnews.com:9091");
        config.addAllowedOrigin("http://admin.imoocnews.com:9091");
        config.addAllowedOrigin("http://files.imoocnews.com:9091");
        // 设置是否发送cookie信息
        config.setAllowCredentials(true);
        // 设置允许请求的方式
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        // 2. 为url添加映射路径,/**就是拦截所有请求
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);
        // 3. 返回重新定义好的corsSource
        return new CorsFilter(corsSource);
    }

}
