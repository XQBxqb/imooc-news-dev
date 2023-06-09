package com.imooc.article.restTemplateUtils;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Set;

/**
 * @author 昴星
 * @date 2023-05-21 9:44
 * @explain
 */
@Component
public class RestTemplateMethod {

    @Autowired
    private RestTemplate restTemplate;
    public  List<AppUserVO> getAppUserVoList(String url){
        ResponseEntity<GraceJSONResult> response = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        String listJson = JsonUtils.objectToJson(graceJSONResult.getData());
        List<AppUserVO> appUserVOList = JsonUtils.jsonToList(listJson, AppUserVO.class);
        return appUserVOList;
    }
}
