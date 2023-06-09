package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 昴星
 * @date 2023-06-04 16:24
 * @explain
 */
@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_ARTICLE = "exchange_article";

    public static final String QUEUE_ARTICLE_HTML = "queue_article_html";

    @Bean(EXCHANGE_ARTICLE)
    public Exchange exchange(){
        return ExchangeBuilder.topicExchange(EXCHANGE_ARTICLE).
                              durable(true).
                              build();
    }

    @Bean(QUEUE_ARTICLE_HTML)
    public Queue queue(){
        return new Queue(QUEUE_ARTICLE_HTML);
    }

    @Bean
    public Binding binging(
            @Qualifier(QUEUE_ARTICLE_HTML) Queue queue,
            @Qualifier(EXCHANGE_ARTICLE) Exchange exchange){
       return BindingBuilder.bind(queue).
                            to(exchange).
                            with("article.*").
                            noargs();
    }
}
