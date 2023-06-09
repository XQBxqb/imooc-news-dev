package com.imooc.admin.controller;

import com.aliyun.core.utils.StringUtils;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.api.controller.admin.FriendLinkMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.DateUtil;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult getFriendLinkList() {
        List<FriendLinkMO> friendLinkMO=null;
        String friendLinkListStr = redis.get(RedisCommon.REDIS_ALL_FRIENDLINK);
        if(!StringUtils.isBlank(friendLinkListStr)){
            friendLinkMO=JsonUtils.jsonToList(friendLinkListStr,FriendLinkMO.class);
        }else{
            friendLinkMO = friendLinkService.queryFriendLinkList();
            redis.set(RedisCommon.REDIS_ALL_FRIENDLINK,JsonUtils.objectToJson(friendLinkMO));
        }
        return GraceJSONResult.ok(friendLinkMO);
    }

    @Override
    public GraceJSONResult saveOrUpdateFriendLink(FriendLinkMO friendLink) {
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

    @Override
    public GraceJSONResult getProtalList() {
        List<FriendLinkMO> friendLinkMOS=null;
        String friendLinkStr = redis.get(RedisCommon.REDIS_NOT_DELETE_FRIENDLINK);
        if(!StringUtils.isBlank(friendLinkStr)){
            friendLinkMOS= JsonUtils.jsonToList(friendLinkStr, FriendLinkMO.class);
        }else{
            friendLinkMOS = friendLinkService.queryFriendLinkWithNotDelete();
            redis.set(RedisCommon.REDIS_NOT_DELETE_FRIENDLINK,JsonUtils.objectToJson(friendLinkMOS));
        }
        return GraceJSONResult.ok(friendLinkMOS);
    }
}
