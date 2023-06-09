package com.imooc.article.controller;

import com.aliyun.core.utils.StringUtils;
import com.imooc.api.config.RabbitMQConfig;
import com.imooc.api.config.RedisOperator;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.BingResultMapUtils;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.PageCommon;
import com.imooc.utils.extend.RedisCommon;
import com.mongodb.client.gridfs.GridFSBucket;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 昴星
 * @date 2023-04-30 13:02
 * @explain
 */

@RestController
public class ArticleController implements ArticleControllerApi {
    @Autowired
    private RedisOperator redis;
    @Autowired
    private RestTemplate template;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${RabbitMQ.SERVER.USER}")
    private String serviceOfUser ;

    @Value("${freemarker.html.article}")
    private String article;


    @Override
    public GraceJSONResult createArticle(NewArticleBO articleBO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            Map<String,String> errResult = BingResultMapUtils.errorsMap(bindingResult);
            return GraceJSONResult.errorMap(errResult);
        }
        if(articleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type){
            if(StringUtils.isBlank(articleBO.getArticleCover()))
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
        }else articleBO.setArticleCover("");
        List<Category> categoryList=null;
        Category c=null;
        String categoryListStr = redis.get(RedisCommon.REDIS_ALL_CATEGROY);
        categoryList = JsonUtils.jsonToList(categoryListStr, Category.class);
        for(Category category:categoryList){
            if(category.getId()==articleBO.getCategoryId()){
                c=category;
            }
        }
        if(c==null) return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        articleService.createArticle(articleBO);

        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryMyList(String userId,
                                       String keyword,
                                       Integer status,
                                       Date startDate,
                                       Date endDate, Integer page, Integer pageSize) {
        if(StringUtils.isBlank(userId))
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        if(page==null)
            page= PageCommon.DEFAULT_PAGE;
        if(pageSize==null)
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        PagedGridResult pagedGridResult = articleService.getArticleListByCondition(userId,
                keyword,
                status,
                startDate,
                endDate, page, pageSize);
        return GraceJSONResult.ok(pagedGridResult);
    }

    @Override
    public GraceJSONResult doReview(String articleId, Integer passOrNot) {
        Integer articleStatus=null;
        if(passOrNot== YesOrNo.YES.type){
            articleStatus= ArticleReviewStatus.SUCCESS.type;
        }else if(passOrNot == YesOrNo.NO.type){
            articleStatus=ArticleReviewStatus.FAILED.type;
        }else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        articleService.updateArticleStatus(articleId,articleStatus);
        String mongoId = null;
        try {
        mongoId = createAritlceHmtlByFtl(articleId);
        } catch (Exception e) {
        }
        upLoadArticleHtmlToRabbitMq(articleId, mongoId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {
        if(page==null){
            page=PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = articleService.queryArticleListAdmin(status, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    private String createAritlceHmtlByFtl(String articleId) throws Exception{

        ArticleDetailVO articleDetail = getDetail(articleId);
        Configuration cfg=new Configuration(Configuration.getVersion());
        Map<String,ArticleDetailVO> map= new HashMap<>();
        map.put("articleDetail",articleDetail);
        String resourcePath = this.getClass().getResource("/").getPath();
        File file = new File(resourcePath + "templates");
        System.out.println(file.getPath());
        cfg.setDirectoryForTemplateLoading(file);
        Template template = cfg.getTemplate("detail.ftl","utf-8");
        String templateIntoString = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);

        InputStream inputStream = IOUtils.toInputStream(templateIntoString);

        ObjectId objectId = gridFSBucket.uploadFromStream(articleId + ".html", inputStream);
        inputStream.close();
        return objectId.toString();
    }

    private ArticleDetailVO getDetail(String articleId){
        String url="http://article.imoocnews.com:7002/portal/article/detail?articleId="+ articleId;
        ResponseEntity<GraceJSONResult> response = template.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        String articleDetailJson = JsonUtils.objectToJson(graceJSONResult.getData());
        ArticleDetailVO detailVO = JsonUtils.jsonToPojo(articleDetailJson, ArticleDetailVO.class);
        return detailVO;
    }
   /*
        这里是通过restTemplate通知消费者,upLoadArticleHtmlToRabbitMq是通过rabbitMq上传，消费者监听
        private Integer downLoadArticleFromGridFS(String articleId,String monogId){
        String url="http://article.imoocnews.com:7002/article/html/upLoadArticleHtmlFromGridFS?" +
                   "articleId="+ articleId + "&&mongodId=" + monogId;
        ResponseEntity<Integer> response = template.getForEntity(url, Integer.class);
        Integer status = response.getBody();

        return status;
    }*/

    private void upLoadArticleHtmlToRabbitMq(String articleId,String mongodId){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                            "article.download",
                                articleId+","+mongodId);
    }
}
