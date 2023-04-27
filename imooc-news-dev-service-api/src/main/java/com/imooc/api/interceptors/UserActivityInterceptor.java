package com.imooc.api.interceptors;

import com.imooc.enums.UserStatus;
import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.utils.extend.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-03-18 16:45
 * @explain
 */
public class UserActivityInterceptor implements HandlerInterceptor {
    static final Logger log= LoggerFactory.getLogger(UserTokenInterceptor.class);

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String id = request.getHeader("headerUserId");
        String token=request.getHeader("headerUserToken");
        if(id==null) {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        String userJson = redisOperator.get(RedisCommon.REDIS_USER_INFO + ":" + id);
        AppUser user= JsonUtils.jsonToPojo(userJson, AppUser.class);
        if(user.getActiveStatus()==null||user.getActiveStatus()== UserStatus.FROZEN.type){
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        return true;
    }
}
