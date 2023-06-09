package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticleProtalService;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-05-09 19:36
 * @explain
 */
@Service
public class ArticleProtalServiceImpl implements ArticleProtalService {
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize) {

        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();
        /**
         * 自带隐性查询条件：
         * isPoint为即时发布，表示文章已经直接发布，或者定时任务到点发布
         * isDelete为未删除，表示文章不能展示已经被删除的
         * status为审核通过，表示文章经过机审/人审通过
         */
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
                // category 为空则查询全部，不指定分类
                // keyword 为空则查询全部
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }

    @Override
    public List<Article> queryHostArticle() {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);

        PageHelper.startPage(1, 5);

        List<Article> articles = articleMapper.selectByExample(articleExample);
        return articles;
    }





    @Override
    public List<Article> queryArticleByWriterId(String writerId, Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
        criteria.andEqualTo("publishUserId",writerId);
        PageHelper.startPage(page, pageSize);

        List<Article> articles = articleMapper.selectByExample(articleExample);
        return articles;
    }

    @Override
    public ArticleDetailVO queryArticleDetail(String articleId) {

        Article article = new Article();
        article.setIsDelete(YesOrNo.NO.type);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);
        article.setId(articleId);

        Article select = articleMapper.selectOne(article);

        ArticleDetailVO articleDetailVO = new ArticleDetailVO();

        BeanUtils.copyProperties(select,articleDetailVO);

        articleDetailVO.setCover(select.getArticleCover());
        return articleDetailVO;
    }

    @Override
    public PagedGridResult queryGoodArticleOfWriter(String writerId) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("publishTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
        criteria.andEqualTo("publishUserId",writerId);
        PageHelper.startPage(1, 5);

        List<Article> articles = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(articles,1);
    }

    private PagedGridResult setterPagedGrid(List<?> list,
                                            Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}


