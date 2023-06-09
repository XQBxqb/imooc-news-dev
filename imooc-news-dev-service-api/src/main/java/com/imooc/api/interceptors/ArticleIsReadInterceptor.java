package com.imooc.api.interceptors;

import com.imooc.utils.extend.IPUtil;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 昴星
 * @date 2023-04-11 19:47
 * @explain
 */
public class ArticleIsReadInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisOperator redis;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String articleId = request.getParameter("articleId");
        String requestIp = IPUtil.getRequestIp(request);
        String isRead = redis.get(RedisCommon.REDIS_IS_ME_READ + ":" + articleId + ":" + requestIp);
        if(StringUtils.isNotBlank(isRead)) return false;
        return true;
    }

}
