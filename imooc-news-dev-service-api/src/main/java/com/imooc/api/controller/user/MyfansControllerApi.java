package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 昴星
 * @date 2023-05-13 20:56
 * @explain
 */

@RequestMapping("fans")
@Api(value = "用于粉丝相关的controller",tags = {"用于粉丝相关的controller"})
public interface MyfansControllerApi {

    @ApiOperation(value = "查看当前用户是否关注作家",notes = "查看当前用户是否关注作家",httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    public GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "关注该作家",notes = "关注该作家",httpMethod = "POST")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String writerId,
                                  @RequestParam String fanId);

    @ApiOperation(value = "停关作家",notes = "停关作家",httpMethod = "POST")
    @PostMapping("/unfollow")
    public GraceJSONResult unfollow(@RequestParam String writerId,
                                  @RequestParam String fanId);

    @ApiOperation(value = "查询男女粉丝数量", notes = "查询男女粉丝数量", httpMethod = "POST")
    @PostMapping("/queryRatio")
    public GraceJSONResult queryRatio(@RequestParam String writerId);

    @PostMapping("/queryAll")
    @ApiOperation(value = "查询我的所有粉丝", notes = "查询我的所有粉丝", httpMethod="POST")
    public GraceJSONResult queryAll(@RequestParam String writerId,
                                    @ApiParam(name = "page", value = "查询下一页的为第几页")
                                    @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页的每页大小")
                                    @RequestParam Integer pageSize);

    @PostMapping("/queryRatioByRegion")
    @ApiOperation(value = "查询粉丝地域比例", notes = "查询粉丝地域比例", httpMethod="POST")
    public GraceJSONResult queryRatioByRegion(@RequestParam String writerId);

}
