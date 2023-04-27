package com.imooc.files.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 昴星
 * @date 2023-03-23 21:41
 * @explain
 */

@Controller
public class hello implements HelloControllerApi {
    final static Logger logger= LoggerFactory.getLogger(hello.class);
    @Override
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello");
    }
}
