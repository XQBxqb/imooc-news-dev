package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author hp
 * @date 2023-03-05 15:29
 * @explain
 */
@RestController
@RequestMapping("user")
public class hello implements HelloControllerApi {
    @Autowired
    private UserControllerApi userController;
    @Override
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello");
    }


}