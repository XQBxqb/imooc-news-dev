package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 昴星
 * @date 2023-04-01 9:57
 * @explain
 */

@Api(value="admin登录的controller",tags="admin登录")
@RequestMapping("adminMng")
public interface AdminMngControllerApi {
    @ApiOperation(value = "doLogin",notes="doLogin",httpMethod = "POST")
    @PostMapping("/adminLogin")
    public GraceJSONResult adminLogin(@RequestBody  AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) ;

    @ApiOperation(value = "查询admin的name是否存在",notes="查询admin的name是否存在",httpMethod = "POST")
    @PostMapping("/adminIsExist")
    public GraceJSONResult adminIsExist(@RequestParam  String username) ;

    @ApiOperation(value = "添加admin",notes="添加admin",httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    public GraceJSONResult addNewAdmin(@RequestBody NewAdminBO newAdmin) ;

    @ApiOperation(value = "获取admin列表",notes="获取admin列表",httpMethod = "POST")
    @PostMapping("/getAdminList")
    public GraceJSONResult getAdminList(@RequestParam Integer page,
                                        @RequestParam Integer pageSize) ;

    @ApiOperation(value = "admin注销",notes="admin注销",httpMethod = "POST")
    @PostMapping("/adminLogout")
    public GraceJSONResult adminLogout(@RequestParam String adminId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) ;

    @ApiOperation(value = "admin人脸登录",notes="admin人脸登录",httpMethod = "POST")
    @PostMapping("/adminFaceLogin")
    public GraceJSONResult adminFaceLogin(@RequestBody AdminLoginBO adminLoginBO,
                                       HttpServletRequest request,
                                       HttpServletResponse response) ;
}
