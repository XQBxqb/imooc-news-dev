package com.imooc.api.interceptors;

import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.utils.extend.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author 昴星
 * @date 2023-03-18 15:18
 * @explain
 */
public class UserTokenInterceptor implements HandlerInterceptor {

    final static Logger log=LoggerFactory.getLogger(UserTokenInterceptor.class);

    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
        String uid = request.getHeader("headerUserId");
        String token=request.getHeader("headerUserToken");
        if(isTokenExit(uid,token)) return true;
        return false;
    }

    /**
     * @description:获取redis找中token
     * @param userId
     * @param utoken
     * @return: java.lang.Boolean
     * @author: 昴星
     * @time: 2023/3/18 15:21
     */
    public Boolean isTokenExit(String userId, String utoken) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(userId)&&StringUtils.isNotBlank(utoken)){
            String redisToken = redisOperator.get(RedisCommon.REDIS_USER_TOKEN + ":" + userId);
            if(redisToken.equals(utoken)) return true;
            GraceException.display(ResponseStatusEnum.TICKET_INVALID);
        }else
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
        return false;
    }
}
