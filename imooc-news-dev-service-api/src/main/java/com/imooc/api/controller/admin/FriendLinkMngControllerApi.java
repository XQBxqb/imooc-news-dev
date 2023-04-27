package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLink;
import com.imooc.pojo.mo.FriendLinkMO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author 昴星
 * @date 2023-04-11 18:58
 * @explain
 */
@ApiOperation(value="友情链接的controller",tags="友情链接的controller")
@RequestMapping("friendLinkMng")
public interface FriendLinkMngControllerApi {
    @ApiOperation(value = "获取所有的友情链接",notes = "获取所有的友情链接",httpMethod = "POST")
    @PostMapping("/getFriendLinkList")
    public GraceJSONResult getFriendLinkList();

    @ApiOperation(value = "增加一个友情链接",notes = "增加一个友情链接",httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody FriendLinkMO friendLinkMO);

    @ApiOperation(value = "删除一个友情链接",notes = "删除一个友情链接",httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String linkId);

    @ApiOperation(value = "首页获取所有的友情链接",notes = "获取所有的友情链接",httpMethod = "GET")
    @GetMapping("/portal/list")
    public GraceJSONResult getProtalList();
}
