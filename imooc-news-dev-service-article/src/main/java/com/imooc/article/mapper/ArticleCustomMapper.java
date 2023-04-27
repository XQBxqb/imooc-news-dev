package com.imooc.article.mapper;

import com.imooc.mymapper.MyMapper;
import com.imooc.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleCustomMapper extends MyMapper<Article> {
    public void updateAppointToPublic();
}