package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-05-09 19:35
 * @explain
 */
public interface ArticleProtalService {
    PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize);

    public List<Article>  queryHostArticle();

    public List<Article> queryArticleByWriterId(String writerId, Integer page, Integer pageSize);

    public ArticleDetailVO queryArticleDetail(String articleId);

    public PagedGridResult queryGoodArticleOfWriter(String writerId);


}
