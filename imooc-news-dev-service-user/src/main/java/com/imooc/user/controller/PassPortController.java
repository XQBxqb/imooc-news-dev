package com.imooc.user.controller;

import com.aliyun.core.utils.StringUtils;
import com.imooc.api.BaseController;
import com.imooc.api.config.RedisOperator;
import com.imooc.api.controller.user.PassPortControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * @author hp
 * @date 2023-03-08 16:41
 * @explain
 */
@RestController
public class PassPortController extends BaseController implements PassPortControllerApi {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private UserService userService;

    @Override
    public GraceJSONResult getSMSCode( String mobile,HttpServletRequest request) {
         //根据用户的ip进行限制，限制用户60s内一次验证码
        //获取用户IP
        String userIp = IPUtil.getRequestIp(request);
        redisOperator.setnx60s(RedisCommon.MOBILE_SMSCODE+":"+userIp,userIp);
        //int num0=m+(int)(Matn.randon()*n);		//返回大于等于m小于m+n（不包括m+n）之间的随机数
        //(Math.random()*9+1)这个是获取[,)两边界都存在，为[1,10),这样上界和下界的倍数就是10而不是下界一直为0
        String random=(int)((Math.random()*9+1)*100000)+"";
        try {
            smsUtils.SMSSend(mobile,random);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        //验证码存入redis，用于后续验证。
        redisOperator.set(RedisCommon.MOBILE_SMSCODE+":"+mobile,random,30*60);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult doLogin(RegistLoginBO registLoginBO,
                                   BindingResult bindingResult,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {

        //1.验证传来数据是否有blank
        if(bindingResult.hasErrors()){
            Map<String, String> errorMap = errorsMap(bindingResult);
            return GraceJSONResult.errorMap(errorMap);
        }

        String mobile=registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        String redisSMSCode = redisOperator.get(RedisCommon.MOBILE_SMSCODE + ":" + mobile);
        //StringUtils.isBlank可以验证是否为null和“”和“  ”（空字符串）.equalsIgnoreCase作用一般是用来比较字母的长度和字符是否相同，却不区分大小写
        if(StringUtils.isBlank(redisSMSCode)||!redisSMSCode.equalsIgnoreCase(smsCode)){
            //2.判断redisSMSCode是否过期，过期就会变成空，同样，要判断是否和code 相同。
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }

        //3.判断通过，可以进行注册和登录
        AppUser appUser = userService.queryMobileIsExit(mobile);
        if(appUser !=null && appUser.getActiveStatus()== UserStatus.FROZEN.type){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }
        if(appUser == null){
             appUser= userService.createUser(mobile);
        }

        //4. 保存用户分布式会话的相关操作
        Integer activeStatus = appUser.getActiveStatus();
        if(activeStatus!=UserStatus.FROZEN.type){
            //保存token到redis中
            String uToken= UUID.randomUUID().toString();
            redisOperator.set(RedisCommon.REDIS_USER_TOKEN+":"+appUser.getId(),uToken);
            redisOperator.set(RedisCommon.REDIS_USER_INFO+":"+appUser.getId(), JsonUtils.objectToJson(appUser));

            setCookie(CookieCommon.USER_UTOKEN,uToken,response,request,MAX_AGE);
            setCookie(CookieCommon.USER_UID,appUser.getId(),response,request,MAX_AGE);
        }
        //5.刪除短信驗證碼
        redisOperator.del(RedisCommon.MOBILE_SMSCODE+":"+mobile);
        return GraceJSONResult.ok(appUser.getActiveStatus());
    }

    @Override
    public GraceJSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        redisOperator.del(RedisCommon.REDIS_USER_TOKEN+":"+userId);
        setCookie(CookieCommon.USER_UTOKEN,"",response,request,MIN_AGE);
        setCookie(CookieCommon.USER_UID,"",response,request,MIN_AGE);
        return GraceJSONResult.ok();
    }
}
