package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-30 13:25
 * @explain
 */
public interface ArticleService {
    public void createArticle(NewArticleBO articleBO);

    public void  updateAppointToPublic();

    void updateArticleStatus(String articleID,Integer articleStatus);

    public PagedGridResult getArticleListByCondition(String userId,
                                                     String keyword,
                                                     Integer status,
                                                     Date startDate,
                                                     Date endDate, Integer page, Integer pageSize);

    public PagedGridResult queryArticleListAdmin(Integer status,Integer page,Integer pageSize);

}
