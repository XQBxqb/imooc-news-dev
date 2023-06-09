package com.imooc.article.controller;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.api.config.RedisOperator;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.JsonUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

/**
 * @author hp
 * @date 2023-03-05 15:29
 * @explain
 */
@RestController
public class hello implements HelloControllerApi {
    @Autowired
    private RedisOperator redis;
    @Autowired
    private RestTemplate template;
    @Autowired
    private GridFSBucket gridFSBucket;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Value("${RabbitMQ.SERVER.USER}")
    private String serviceOfUser ;

    @Value("${freemarker.html.article}")
    private String article;

    @Override
    public GraceJSONResult hello() {
        return GraceJSONResult.ok("hello");
    }

    @RequestMapping("/builder")
    public GraceJSONResult build(){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,"article.test",
                    "hi,rabbit");
        return GraceJSONResult.ok();
    }

    @PostMapping ("/turnRound")
    public GraceJSONResult turnRound(){
        String url = "http://"+serviceOfUser+"/user/getUserInfo";
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("userId","2006289KKADB4H94");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);
        ResponseEntity<GraceJSONResult> graceJSONResultResponseEntity = template.postForEntity(url, request, GraceJSONResult.class);
        return GraceJSONResult.ok();
    }
}