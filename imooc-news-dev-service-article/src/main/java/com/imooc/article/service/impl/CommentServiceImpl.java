package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.article.mapper.CommentsCustomeMapper;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.service.ArticleProtalService;
import com.imooc.article.service.CommentService;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.CommentsVO;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 昴星
 * @date 2023-05-19 22:29
 * @explain
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentsMapper commentsMapper;
    @Autowired
    private CommentsCustomeMapper customeMapper;

    @Autowired
    private Sid sid;
    @Autowired
    private ArticleProtalService articleProtalService;
    @Autowired
    private RedisOperator redis;


    @Override
    @Transactional
    public Integer createComment(String fatherId,String articleId,String commentUserId,String commentUserNickname,String commentUserFace,String content) {
        Comments comments = new Comments();

        ArticleDetailVO detailVO = articleProtalService.queryArticleDetail(articleId);
        String writerId=detailVO.getPublishUserId();
        String articleTitle = detailVO.getTitle();
        String articleCover = detailVO.getCover();

        String commentId = sid.nextShort();
        comments.setId(commentId);
        comments.setWriterId(writerId);
        comments.setFatherId(fatherId);
        comments.setCommentUserNickname(commentUserNickname);
        comments.setArticleId(articleId);
        comments.setArticleTitle(articleTitle);
        comments.setArticleCover(articleCover);
        comments.setCommentUserId(commentUserId);
        comments.setCommentUserFace(commentUserFace);
        comments.setContent(content);
        comments.setCreateTime(new Date());
        int res = commentsMapper.insert(comments);
        redis.increment(RedisCommon.REDIS_ARTICLE_COMMENT_COUNT+":"+articleId,1);
        return res;
    }

    @Override
    public PagedGridResult queryArticleComment(String articleId, Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map=new HashMap<>();
        map.put("articleId",articleId);
        List<CommentsVO> commentsVOList = customeMapper.queryArticleCommentList(map);
        return setterPagedGrid(commentsVOList,page);
    }

    @Override
    @Transactional
    public int deleteArticleComment(String writerId, String commentId) {
        Comments comments = new Comments();
        comments.setId(commentId);
        comments.setWriterId(writerId);
        int res = commentsMapper.insert(comments);
        return res;
    }

    @Override
    public PagedGridResult queryMyArticleComment(String writerId, Integer page, Integer pageSize) {
        Comments comments = new Comments();
        comments.setWriterId(writerId);
        PageHelper.startPage(page,pageSize);
        List<Comments> select = commentsMapper.select(comments);
        return setterPagedGrid(select,page);
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
