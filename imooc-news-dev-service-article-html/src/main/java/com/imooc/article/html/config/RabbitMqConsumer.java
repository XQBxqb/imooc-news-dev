package com.imooc.article.html.config;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.article.html.controller.ArticleRabbitMqConfig;
import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 昴星
 * @date 2023-06-04 17:06
 * @explain
 */
@Component
public class RabbitMqConsumer {

    @Autowired
    private ArticleRabbitMqConfig articleRabbitMqConfig;

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_ARTICLE_HTML})
    public void watchQueue(String payload,Message message){
        String routingKey = message.getMessageProperties().getReceivedRoutingKey();
        if(routingKey.equals("article.download")) {
            String[] split = payload.split(",");
            String articleId = split[0];
            String mongodId = split[1];
            articleRabbitMqConfig.upLoadArticleHtmlFromGridFS(articleId,mongodId);
        }else{
            articleRabbitMqConfig.deleteArticle(payload);
        }
    }
}
