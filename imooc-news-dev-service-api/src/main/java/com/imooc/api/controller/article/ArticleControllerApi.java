package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

/**
 * @author 昴星
 * @date 2023-04-30 13:01
 * @explain
 */
@ApiOperation(value = "文字领域的Controller",tags = {"文字领域的Controller"})
@RequestMapping("article")
public interface ArticleControllerApi {

    @ApiOperation(value = "doLogin",notes="doLogin",httpMethod = "POST")
    @PostMapping("/createArticle")
    public GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO articleBO,
                                         BindingResult bindingResult) ;


    @ApiOperation(value = "查询用户的所有文章列表", notes = "查询用户的所有文章列表")
    @PostMapping("/queryMyList")
    public GraceJSONResult queryMyList(@RequestParam String userId,
            @RequestParam String keyword,
            @RequestParam Integer status,
            @RequestParam Date startDate,
            @RequestParam Date endDate,
            @ApiParam(name = "page", value = "查询下一页的第几页")
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
            @RequestParam Integer pageSize);

    @ApiOperation(value = "人工审核文章",notes="人工审核文章",httpMethod = "POST")
    @PostMapping("/doReview")
    public GraceJSONResult doReview(@RequestParam String articleId,
                                    @RequestParam Integer passOrNot) ;

    @PostMapping("/queryAllList")
    @ApiOperation(value = "管理员查询用户的所有文章列表", notes = "管理员查询用户的所有文章列表", httpMethod = "POST")
    public GraceJSONResult queryAllList(@RequestParam Integer status,
                                        @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                                        @RequestParam Integer page,
                                        @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                                        @RequestParam Integer pageSize);
}
