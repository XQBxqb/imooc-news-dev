package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author hp
 * @date 2023-03-08 16:37
 * @explain
 */
//requestMapping一般都写在api层（无论是全局的mapping映射还是对应方法的mapping映射），减少controller层冗余，而controller，
//仅加上controller注释，实现接口方法就行
//@RequestMapping("/passport")
@RequestMapping("passport")
@Api(value = "用户注册和登录",tags = {"用户发送验证码的controller"})
public interface PassPortControllerApi {

    @GetMapping ("/getSMSCode")
    @ApiOperation(value="获得验证码",notes="获得验证码",httpMethod = "GET")
    public GraceJSONResult getSMSCode(
                                     @RequestParam("mobile") String mobile,
                                     HttpServletRequest request);
    /**
     * @description: 前端传送一个对象，接受前端对象来处理controller层，一般取名BO（从视图层传来，BASIC OBJECT）
     * @param registLoginBO，前段传过来JSON对象，必须要RequestBody解析成类的对象，@Valid是用来检验前端发送的数据，在BO类中，通过NOTBlank来个Valid配合进行检测
     * @param bindingResult 它和Valid进行配合来处理检验异常的处理。
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/3/14 18:18
     */
    @PostMapping("/doLogin")
    @ApiOperation(value="一键注册登录接口",notes="一键注册登录接口",httpMethod = "POST")
    public GraceJSONResult doLogin(
            @RequestBody @Valid RegistLoginBO registLoginBO,
            BindingResult bindingResult,HttpServletRequest request,
            HttpServletResponse response);

    @PostMapping("/logout")
    @ApiOperation(value="退出登录接口",notes="退出登录接口",httpMethod = "POST")
    public GraceJSONResult logout(
            @RequestParam String userId
            ,HttpServletRequest request,
            HttpServletResponse response);
}
