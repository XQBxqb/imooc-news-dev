package com.imooc.api.interceptors;

import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * @author 昴星
 * @date 2023-03-18 15:18
 * @explain配置处理admin对应的拦截器，拦截未登录
 */
public class AdminMngInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redisOperator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) throws Exception {
        String adminUserId = request.getHeader("adminUserId");
        String adminUserToken = request.getHeader("adminUserToken");
        System.out.println(adminUserToken+"  "+adminUserId);
        if(isTokenExit(adminUserId,adminUserToken)) return true;
        return false;
    }

    /**
     * @description:获取redis找中token
     * @param adminId
     * @param adminToken
     * @return: java.lang.Boolean
     * @author: 昴星
     * @time: 2023/3/18 15:21
     */
    public Boolean isTokenExit(String adminId, String adminToken) throws UnsupportedEncodingException {
        if(StringUtils.isNotBlank(adminId)&&StringUtils.isNotBlank(adminToken)){
            String redisToken = redisOperator.get(RedisCommon.REDIS_ADMIN_TOKEN + ":" + adminId);
            if(redisToken.equals(adminToken)) return true;
            GraceException.display(ResponseStatusEnum.TICKET_INVALID);
        }else
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
        return false;
    }
}
