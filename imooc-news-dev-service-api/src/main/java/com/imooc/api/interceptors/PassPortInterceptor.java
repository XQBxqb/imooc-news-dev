package com.imooc.api.interceptors;


import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.extend.IPUtil;
import com.imooc.api.config.RedisOperator;
import com.imooc.utils.extend.RedisCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：昴星
 * @date 2023-03-11 14:18
 * @explain：实现登录的拦截器
 * 一般api层和业务的实现层分开，实现层就是单纯地实现业务，而一些可以分离业务的，比如说拦截器，可以写在api层，这样能够降低业务层的复杂性，业务层的功能越单纯，维护的成本也越低
 */

public class PassPortInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redisOperator;

    String MOBILE_SMSCODE = RedisCommon.MOBILE_SMSCODE;


    /**
     * @description:处理controller接受请求之前，进行拦截，拦截，返回false那么进行拦截，返回true，进入controller，这里是通过判断redis内存储的key值是否存在进行拦截
     * @param request
     * @param response
     * @param handler
     * @return: boolean
     * @author: 昴星
     * @time: 2023/3/11 14:19
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)  throws Exception {
        String Ip = IPUtil.getRequestIp(request);
        boolean keyIsExit = redisOperator.keyIsExit(MOBILE_SMSCODE + ":" + Ip);
        if(keyIsExit){
            GraceException.display(ResponseStatusEnum.SMS_NEED_WAIT_ERROR);
            return false;
        }
        return true;
    }

    /**
     * @description:处理在controller接受请求以后，渲染页面之前，进行拦截
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @return: void
     * @author: 昴星
     * @time: 2023/3/11 14:21
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
    /**
     * @description:处理在controller之后，渲染页面以后，进行拦截
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @return: void
     * @author: 昴星
     * @time: 2023/3/11 14:22
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
