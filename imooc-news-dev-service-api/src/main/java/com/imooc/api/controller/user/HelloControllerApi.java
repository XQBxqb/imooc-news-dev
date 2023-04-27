package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author hp
 * @date 2023-03-05 15:34
 * @explain
 */
@Api(value = "controller的标题",tags = {"xx功能"})
public interface HelloControllerApi {
    @ApiOperation(value = "hello方法",notes="hello方法",httpMethod = "GET")
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public GraceJSONResult hello();
}
