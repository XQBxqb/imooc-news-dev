package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author hp
 * @date 2023-03-08 16:37
 * @explain
 */

@RequestMapping("appUser")
@Api(value = "admin用户管理普通用户",tags = "admin用户管理普通用户")
public interface AppUserMngControllerApi {
    @ApiOperation(value = "查询所有用户",notes="查询所有用户",httpMethod = "POST")
    @PostMapping( "/queryAll")
    public GraceJSONResult queryAll(@RequestParam String nickname,
                                    @RequestParam Integer status,
                                    @RequestParam String startDate,
                                    @RequestParam String endDate,
                                    @ApiParam(name = "page", value = "查询下一页的第几个")
                                    @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页的每页数据量")
                                    @RequestParam Integer pageSize);

}
