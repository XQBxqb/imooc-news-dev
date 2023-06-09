package com.imooc.user.controller;

import com.imooc.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import wiremock.org.apache.commons.lang3.StringUtils;
import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hp
 * @date 2023-03-08 16:41
 * @explain
 */

@ResponseBody
@Controller
public class UserController extends BaseController implements UserControllerApi {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redis;
    @Autowired
    RedisTemplate redisTemplate;
    static final Logger log=LoggerFactory.getLogger(UserController.class);
    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser appUser = getUserById(userId);
        UserAccountInfoVO user=new UserAccountInfoVO();
        BeanUtils.copyProperties(appUser,user);
        return GraceJSONResult.ok(user);
    }

    @Override
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        AppUser appUser = getUserById(userId);
        AppUserVO userInfo=new AppUserVO();
        BeanUtils.copyProperties(appUser,userInfo);

        long myFansCountLong=redis.getNum(RedisCommon.REDIS_MY_FANS_COUNT + ":" + userId);
        long myFollowCountLong=redis.getNum(RedisCommon.REDIS_MY_FOLLOWED_COUNT+":"+userId);
        int myFansCount=(int)myFansCountLong;
        int myFollowCount=(int)myFollowCountLong;

        userInfo.setMyFansCounts(myFansCount);
        userInfo.setMyFollowCounts(myFollowCount);
        return GraceJSONResult.ok(userInfo);
    }

    @Override
    public GraceJSONResult getUserListByIds(String ids) {
        if(StringUtils.isBlank(ids)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        List<String> idList = JsonUtils.jsonToList(ids, String.class);
        List<AppUserVO> appUserVOList=new ArrayList<>();
        for(String id:idList){
            AppUser appUser = getUserById(id);
            AppUserVO appUserVO=new AppUserVO();
            BeanUtils.copyProperties(appUser,appUserVO);
            appUserVOList.add(appUserVO);
        }
        return GraceJSONResult.ok(appUserVOList);
    }

    /**
     * @description:公共方法，会在controller内复用，同时又可以扩展
     * @param userId
     * @return: com.imooc.pojo.AppUser
     * @author: 昴星
     * @time: 2023/3/15 21:14
     */
    private AppUser getUserById(String userId){
        String userJson=redisOperator.get(RedisCommon.REDIS_USER_INFO+":"+userId);
        AppUser appUser= null;
        if(StringUtils.isNotBlank(userJson)){
            appUser= JsonUtils.jsonToPojo(userJson,AppUser.class);
            return appUser;
        }
        appUser=userService.getUserById(userId);
        redisOperator.set(RedisCommon.REDIS_USER_INFO+":"+userId,JsonUtils.objectToJson(appUser));
        return appUser;
    }

}
