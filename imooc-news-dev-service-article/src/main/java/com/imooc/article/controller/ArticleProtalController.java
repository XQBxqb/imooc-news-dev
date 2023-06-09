package com.imooc.article.controller;


import com.imooc.api.controller.article.ArticleProtalControllerApi;
import com.imooc.article.service.ArticleProtalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.IPUtil;
import com.imooc.utils.extend.PageCommon;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import com.mongodb.client.gridfs.GridFSBucket;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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

    @Autowired
    private RedisOperator redis;

    @Value("${RabbitMQ.SERVER.USER}")
    private String serviceOfUser;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Override
    public GraceJSONResult list(String keyword, Integer category, Integer page, Integer pageSize) {
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = articleProtalService.queryIndexArticleList(keyword, category, page, pageSize);
        String jsonList = JsonUtils.objectToJson(gridResult.getRows());
        List<Article> list = JsonUtils.jsonToList(jsonList, Article.class);
        Set<String> idSet=new HashSet<>();
        List<String> articleIdList=new ArrayList<>();
        Map<String,String> countMap=new HashMap<>();

        for(Article article:list){
            idSet.add(article.getPublishUserId());
            articleIdList.add(RedisCommon.REDIS_ARTICLE_READ_COUNT+":"+article.getId());
        }
        //通过redis过的count
        List<String> readCountList = redis.multiGet(articleIdList);
        System.out.println(JsonUtils.objectToJson(readCountList));
        //通过redistemplate调用usr模块服务
        //String url="http://user.imoocnews.com:7003/user/getUserListByIds?ids="+ JsonUtils.objectToJson(idSet);

        //通过eureka调用服务

        String url="http://"+serviceOfUser+"/user/getUserListByIds?ids="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> response = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        String listJson = JsonUtils.objectToJson(graceJSONResult.getData());
        List<AppUserVO> appUserVOList = JsonUtils.jsonToList(listJson, AppUserVO.class);
        List<IndexArticleVO> indexArticleVOS=new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Article article = list.get(i);
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            BeanUtils.copyProperties(article, indexArticleVO);
            // 3.1 从userList中获得publisher基本信息
            AppUserVO publisher = getUserIfEqualPublisher(article.getPublishUserId(), appUserVOList);
            // 3.2 把文章放入新的文章list中
            indexArticleVO.setPublisherVO(publisher);
            indexArticleVOS.add(indexArticleVO);
            //存入count
            int readCount=0;
            String countStr = readCountList.get(i);
            if(StringUtils.isNotBlank(countStr)){
            readCount=Integer.parseInt(countStr);
            }
            indexArticleVO.setReadCounts(readCount);
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

    @Override
    public GraceJSONResult detail(String articleId) {
        ArticleDetailVO articleDetailV= articleProtalService.queryArticleDetail(articleId);

        Set<String> idSet=new HashSet<>();
        idSet.add(articleDetailV.getPublishUserId());
        String url="http://"+serviceOfUser+"/user/getUserListByIds?ids="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> response = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        String listJson = JsonUtils.objectToJson(graceJSONResult.getData());
        AppUserVO appUserVO = JsonUtils.jsonToList(listJson, AppUserVO.class).get(0);
        articleDetailV.setPublishUserName(appUserVO.getNickname());
        return GraceJSONResult.ok(articleDetailV);
    }

    @Override
    public GraceJSONResult readArticle(String articleId, HttpServletRequest request) {
        String requestIp = IPUtil.getRequestIp(request);
        redis.increment(RedisCommon.REDIS_ARTICLE_READ_COUNT+":"+articleId,1);
        redis.set(RedisCommon.REDIS_IS_ME_READ+":"+articleId+":"+requestIp,"true");
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult articleReadCounts(String articleId, HttpServletRequest request) {
        String coutsStr = redis.get(RedisCommon.REDIS_ARTICLE_READ_COUNT + ":" + articleId);
        Integer articleReadCount=Integer.parseInt(coutsStr);
        return GraceJSONResult.ok(articleReadCount);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articleProtalService.queryGoodArticleOfWriter(writerId);
        return GraceJSONResult.ok(gridResult);
    }
}
