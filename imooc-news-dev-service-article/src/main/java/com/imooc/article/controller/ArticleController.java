package com.imooc.article.controller;

import com.aliyun.core.utils.StringUtils;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewLevel;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.BingResultMapUtils;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.PageCommon;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.utils.extend.RedisOperator;
import org.apache.catalina.startup.Catalina;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.BindingResultUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
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
    private ArticleService articleService;

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


}
