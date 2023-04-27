package com.imooc.api.interceptors;

import com.imooc.api.wapper.FriendLinkMngSaveMOWrapper;
import com.imooc.expection.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.SaveFriendLink;
import com.imooc.utils.JsonUtils;
import com.mongodb.util.JSON;
import org.springframework.web.servlet.HandlerInterceptor;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 昴星
 * @date 2023-04-11 19:47
 * @explain
 */
public class FriendLinkVerifyUpdateInfo implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        FriendLinkMngSaveMOWrapper saveMOWrapper = new FriendLinkMngSaveMOWrapper(request);
        String body = saveMOWrapper.getBody();
        SaveFriendLink saveFriendLink = JsonUtils.jsonToPojo(body, SaveFriendLink.class);
        if(!isUrl(saveFriendLink.getLinkUrl())) {GraceException.display(ResponseStatusEnum.FAILED);return false;}
        return true;
    }
    /**
     * @description:正则表达式判断url是不是符合格式
     * @param urls
     * @return: boolean
     * @author: 昴星
     * @time: 2023/4/12 18:29
     */
    private boolean isUrl(String urls) {
        if(StringUtils.isBlank(urls)) {
            return  true;
        }
        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";

        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(urls.trim());
        boolean result = mat.matches();
        return result;
    }
}
