package com.imooc.article.mapper;


import com.imooc.mymapper.MyMapper;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.CommentsVO;
import feign.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public interface CommentsMapper extends MyMapper<Comments> {
}