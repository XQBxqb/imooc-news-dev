package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.CategoryBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 昴星
 * @date 2023-04-12 19:16
 * @explain
 */

@Api(value="admin登录的controller",tags="admin登录")
@RequestMapping("categoryMng")
public interface CategoryMngControllerApi {
    @ApiOperation(value = "获得category",notes="获得category",httpMethod = "POST")
    @PostMapping("/getCatList")
    public GraceJSONResult getCatList () ;

    @ApiOperation(value = "保存或者更新category",notes="保存或者更新category",httpMethod = "POST")
    @PostMapping("/saveOrUpdateCategory")
    public GraceJSONResult saveOrUpdateCategory (@RequestBody CategoryBO categoryBO, BindingResult bindingResult) ;
}
