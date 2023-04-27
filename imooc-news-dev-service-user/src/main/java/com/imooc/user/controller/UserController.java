package com.imooc.user.controller;

import com.imooc.utils.JsonUtils;
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
import com.imooc.utils.extend.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author hp
 * @date 2023-03-08 16:41
 * @explain
 */

@RestController
public class UserController extends BaseController implements UserControllerApi {
    @Autowired
    UserService userService;

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
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO, BindingResult bindingResult) {
        //校验BO
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = errorsMap(bindingResult);
            return GraceJSONResult.errorMap(errorMap);
        }
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

        return GraceJSONResult.ok(userInfo);
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
