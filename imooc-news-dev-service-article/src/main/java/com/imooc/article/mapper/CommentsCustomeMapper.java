package com.imooc.article.mapper;


import com.imooc.mymapper.MyMapper;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.CommentsVO;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CommentsCustomeMapper extends MyMapper<Comments> {
    public List<CommentsVO> queryArticleCommentList(@Param("paramsMap") Map<String, Object> map);
}