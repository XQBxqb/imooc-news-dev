package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.article.mapper.ArticleCustomMapper;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticleService;
import tk.mybatis.mapper.entity.Example;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Article;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.beans.Transient;
import java.util.Date;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-30 13:26
 * @explain
 */
@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private Sid sid;

    @Autowired
    private ArticleMapper mapper;

    @Autowired
    private ArticleCustomMapper customMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional
    public void createArticle(NewArticleBO articleBO) {
        String articleId = sid.nextShort();
        Article article = new Article();
        BeanUtils.copyProperties(articleBO,article);

        article.setId(articleId);
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setReadCounts(0);
        article.setCommentCounts(0);

        if(article.getIsAppoint()== YesOrNo.YES.type){
            article.setPublishTime(articleBO.getPublishTime());
        }else{
            article.setPublishTime(new Date());
        }
        mapper.insert(article);
    }

    @Override
    public void updateAppointToPublic() {
        customMapper.updateAppointToPublic();
    }

    @Override
    public PagedGridResult getArticleListByCondition(String userId,
                                                     String keyword,
                                                     Integer status,
                                                     Date startDate, Date endDate,
                                                     Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("createTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }
        // 审核中是机审和人审核的两个状态，所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL);
        }
        //isDelete 必须是0
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }

    public PagedGridResult setterPagedGrid(List<?> list,
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
