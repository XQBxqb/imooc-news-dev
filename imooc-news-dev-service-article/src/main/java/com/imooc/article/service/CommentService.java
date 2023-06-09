package com.imooc.article.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.vo.CommentsVO;
import com.imooc.utils.PagedGridResult;

/**
 * @author 昴星
 * @date 2023-04-01 9:38
 * @explain
 */
public interface CommentService {
    public Integer createComment(String fatherId,String articleId,String commentUserId,String commentUserNickname,String commentUserFace,String content);

    public PagedGridResult queryArticleComment(String articleId, Integer page, Integer pageSize);

    public int deleteArticleComment(String writerId,String commentId);

    public PagedGridResult queryMyArticleComment(String writerId,Integer page,Integer pageSize);
}
