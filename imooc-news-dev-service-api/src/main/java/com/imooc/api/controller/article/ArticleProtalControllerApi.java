package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 昴星
 * @date 2023-05-09 19:42
 * @explain
 */
@Api(value = "门户站点文章业务controller", tags = {"门户站点文章业务controller"})
@RequestMapping("portal/article")
public interface ArticleProtalControllerApi {
    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表",httpMethod = "GET")
    @GetMapping("/list")
    public GraceJSONResult list(@RequestParam String keyword,
                                @RequestParam Integer category,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize);

    @ApiOperation(value = "首页查询热点新闻列表", notes = "首页查询热点新闻列表",httpMethod = "GET")
    @GetMapping("/hotList")
    public GraceJSONResult hotList();

    @ApiOperation(value = "查询一个用户的文章", notes = "查询一个用户的文章",httpMethod = "GET")
    @GetMapping("/queryArticleListOfWriter")
    public GraceJSONResult queryArticleListOfWriter(@RequestParam String writerId,
                                                    @RequestParam Integer page,
                                                    @RequestParam Integer pageSize);

    @GetMapping("/detail")
    @ApiOperation(value = "获取文章的详细信息", notes = "获取文章的详细信息",httpMethod = "GET")
    public GraceJSONResult detail(@RequestParam String articleId);

    @PostMapping("/readArticle")
    @ApiOperation(value = "增加文章的阅读量", notes = "增加文章的阅读量",httpMethod = "POST")
    public GraceJSONResult readArticle(@RequestParam String articleId, HttpServletRequest request);

    @PostMapping("/articleReadCounts")
    @ApiOperation(value = "获得文章阅读量", notes = "获得文章阅读量",httpMethod = "POST")
    public GraceJSONResult articleReadCounts(@RequestParam String articleId, HttpServletRequest request);

    @GetMapping("/queryGoodArticleListOfWriter")
    @ApiOperation(value = "作家页面查询近期佳文", notes = "作家页面查询近期佳文", httpMethod = "GET")
    public GraceJSONResult queryGoodArticleListOfWriter(@RequestParam String writerId);


}
