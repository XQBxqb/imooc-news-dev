package com.imooc.api.config;

import com.imooc.api.interceptors.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 昴星
 * @date 2023-03-11 14:50
 * @explain 配置拦截器
 */

@Configuration
public class InterceptorConfigInterceptor implements WebMvcConfigurer {
    @Bean
    public PassPortInterceptor passPortInterceptor(){
       return new PassPortInterceptor();
   }

    @Bean
    public UserTokenInterceptor userInterceptor(){return new UserTokenInterceptor();}

    @Bean
    public UserActivityInterceptor userActivityInterceptor(){return new UserActivityInterceptor();}

    @Bean
    public AdminMngInterceptor adminTokenInterceptor(){return new AdminMngInterceptor();}

    @Bean
    public FriendLinkVerifyUpdateInfo friendLinkVerifyUpdateInfo(){return new FriendLinkVerifyUpdateInfo();}

    @Bean
    public ArticleIsReadInterceptor articleIsReadInterceptor(){return new ArticleIsReadInterceptor();}


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       //addInterceptor参数是实现HandlerIn..的实现类
        registry.addInterceptor(passPortInterceptor())
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");
                //.addPathPatterns("/user/getUserInfo");

        registry.addInterceptor(adminTokenInterceptor())
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/adminFaceLogin")
                .addPathPatterns("/adminMng/adminLogout")
                .addPathPatterns("/appUser/queryAll")
                .addPathPatterns("/categoryMng/getCatList")
                .addPathPatterns("/categoryMng/saveOrUpdateCategory");

        registry.addInterceptor(friendLinkVerifyUpdateInfo())
                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink");

        registry.addInterceptor(articleIsReadInterceptor())
                .addPathPatterns("/portal/article/readArticle");
    }
}
