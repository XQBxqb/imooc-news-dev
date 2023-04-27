package com.imooc.article.controller;


import com.imooc.api.controller.article.ArticleProtalControllerApi;
import com.imooc.article.service.ArticleProtalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.PageCommon;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import wiremock.net.minidev.json.JSONUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 昴星
 * @date 2023-04-11 19:02
 * @explain
 */
@ResponseBody
@Controller
public class ArticleProtalController implements ArticleProtalControllerApi {
    @Autowired
    private ArticleProtalService articleProtalService;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = articleProtalService.queryIndexArticleList(keyword, category, page, pageSize);
        List<Article> list= (List<Article>) gridResult.getRows();
        Set<String> idSet=new HashSet<>();
        for(Article article:list){
            idSet.add(article.getPublishUserId());
        }
        //通过redistemplate调用usr模块服务
        String url="http://user.imoocnews.com:7003/user/getUserListByIds?ids="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> response = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        if(graceJSONResult.getStatus()!=200){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        List<AppUserVO> appUserVOList=(List<AppUserVO>)graceJSONResult.getData();
        List<IndexArticleVO> indexArticleVOS=new ArrayList<>();
        for(Article article:list){
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            BeanUtils.copyProperties(article, indexArticleVO);
            // 3.1 从userList中获得publisher基本信息
            AppUserVO publisher = getUserIfEqualPublisher(article.getPublishUserId(), appUserVOList);
            // 3.2 把文章放入新的文章list中
            indexArticleVO.setPublisherVO(publisher);
            indexArticleVOS.add(indexArticleVO);
        }
        gridResult.setRows(indexArticleVOS);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult hotList() {
        List<Article> articles = articleProtalService.queryHostArticle();
        return GraceJSONResult.ok(articles);
    }



    /**
     * @description:匹配id对应的appuservo
     * @param publisherId
     * @param publisherList
     * @return: com.imooc.pojo.vo.AppUserVO
     * @author: 昴星
     * @time: 2023/5/9 20:17
     */
    private AppUserVO getUserIfEqualPublisher(String publisherId,
                                              List<AppUserVO> publisherList) {
        for (Object u : publisherList) {
            String str = JsonUtils.objectToJson(u);
            AppUserVO appUser = JsonUtils.jsonToPojo(str, AppUserVO.class);
            if (appUser.getId().equals(publisherId)) {
                return appUser;
            }
        }
        return null;
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        List<Article> articleList = articleProtalService.queryArticleByWriterId(writerId, page, pageSize);
        return GraceJSONResult.ok(articleList);
    }
}
