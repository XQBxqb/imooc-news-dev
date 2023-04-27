package com.imooc.article.task;

import com.imooc.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author 昴星
 * @date 2023-04-30 14:08
 * @explain
 */

//@Configuration
//@EnableScheduling
//public class TaskPublishArticle {
//    @Autowired
//    private ArticleService articleService;
//
//    @Scheduled(cron="0/3 * * * * ?")
//    public void publicArticle(){
//        System.out.println("现在执行的时间为:"+ LocalDateTime.now());
//        articleService.updateAppointToPublic();
//    }
//}


public class TaskPublishArticle{

}