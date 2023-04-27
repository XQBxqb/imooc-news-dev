package com.imooc.api;

import com.imooc.utils.extend.CookieCommon;
import com.imooc.utils.extend.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-03-14 18:59
 * @explain一些公共条件的封装
 */
public class BaseController {

    @Autowired
    public RedisOperator redisOperator;

    public static final Integer MAX_AGE=30*24*62*60;

    public static final Integer MIN_AGE=0;

    @Value("${website.domain-name}")
    private String DOMAIN_NAME;
    /**
     * @description: 获得错误消息封装
     * @param bindingResult
     * @return: java.util.Map<java.lang.String, java.lang.String>
     * @author: 昴星
     * @time: 2023/3/14 18:50
     */
    public Map<String,String> errorsMap(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        Map<String, String> map = new HashMap<>();
        for (FieldError fieldError : fieldErrors) {
            //错误对应的属性
            String field = fieldError.getField();
            //错误对应的消息
            String message = fieldError.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }

    /**
     * @description:設置cookie時，注意要將httpservletresponse和httpServletrequest一同放到對應的方法內，無論那個是不是用得到
     * @param cookieName
     * @param cookieValue
     * @param response
     * @param request
     * @param MAX_AGE
     * @return: void
     * @author: 昴星
     * @time: 2023/3/15 20:49
     */
    public void setCookie(String cookieName, String cookieValue,  HttpServletResponse response,HttpServletRequest request,Integer MAX_AGE){
        try {
            cookieValue= URLEncoder.encode(cookieValue,"utf-8");
            setCookieValue(cookieName, cookieValue, MAX_AGE, response,request);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCookieValue(String cookieName, String cookieValue, Integer MAX_AGE, HttpServletResponse response,HttpServletRequest request) {
        Cookie cookie=new Cookie(cookieName, cookieValue);
        //ookie.setDomain("imoocnews.com");
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        cookie.setMaxAge(MAX_AGE);
        response.addCookie(cookie);
    }

    public void deleteCookie(String cookieName,
                             HttpServletResponse response,
                             HttpServletRequest request) throws UnsupportedEncodingException {
        String deleteValue = URLEncoder.encode(CookieCommon.DELETE_COOKIE_VALUE, "utf-8");
        setCookie(cookieName,deleteValue,response,request,MIN_AGE);
    }
}
