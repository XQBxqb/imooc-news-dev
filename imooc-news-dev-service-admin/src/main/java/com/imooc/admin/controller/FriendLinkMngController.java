package com.imooc.admin.controller;

import com.imooc.admin.service.FriendLinkService;
import com.imooc.api.controller.admin.FriendLinkMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLink;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.extend.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-11 19:02
 * @explain
 */
@RestController
public class FriendLinkMngController implements FriendLinkMngControllerApi {
    @Autowired
    private FriendLinkService friendLinkService;

    @Autowired
    private Sid sid;

    @Override
    public GraceJSONResult getFriendLinkList() {
        List<FriendLinkMO> friendLinkMO = friendLinkService.queryFriendLinkList();
        return GraceJSONResult.ok(friendLinkMO);
    }

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody SaveFriendLink friendLink) {
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(friendLink,friendLinkMO);
        friendLinkMO.setUpdateTime(DateUtil.getCurrentDateTime());
        friendLinkMO.setId(sid.nextShort());
        friendLinkService.insertFriendLink(friendLinkMO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult delete(String linkId) {
        friendLinkService.deleteFriendLink(linkId);
        return GraceJSONResult.ok();
    }
}
