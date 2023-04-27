package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
