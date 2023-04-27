package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author hp
 * @date 2023-03-08 16:37
 * @explain
 */
//requestMapping一般都写在api层（无论是全局的mapping映射还是对应方法的mapping映射），减少controller层冗余，而controller，
//仅加上controller注释，实现接口方法就行
//@RequestMapping("/passport")
@RequestMapping("user")
@Api(value = "用户相关信息",tags = {"用户相关信息的controller"})
public interface UserControllerApi {

    @PostMapping ("/getAccountInfo")
    @ApiOperation(value="获得验证码",notes="获得验证码",httpMethod = "POST")
    public GraceJSONResult getAccountInfo(@RequestParam String userId);

    @PostMapping("/updateUserInfo")
    @ApiOperation(value="更新/修改用户信息",notes="更新/修改用户信息接口",httpMethod = "POST")
    public GraceJSONResult updateUserInfo(
            @RequestBody @Valid UpdateUserInfoBO updateUserInfoBO,
            BindingResult bindingResult);

    @PostMapping("/getUserInfo")
    @ApiOperation(value="获取用户基本信息",notes="获取用户基本信息",httpMethod = "POST")
    public GraceJSONResult getUserInfo(@RequestParam String userId);
}
