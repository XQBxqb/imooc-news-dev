package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
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
    @Override
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello");
    }
}