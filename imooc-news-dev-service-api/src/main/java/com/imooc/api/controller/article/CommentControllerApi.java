package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 昴星
 * @date 2023-04-01 9:57
 * @explain
 */

@Api(value="文章评论业务的controller",tags="文章评论业务的controller")
@RequestMapping("comment")
public interface CommentControllerApi {
    @ApiOperation(value = "用户留言",notes="用户留言",httpMethod = "POST")
    @PostMapping("/createComment")
    public GraceJSONResult createComment(@RequestBody  @Valid CommentReplyBO commentReplyBO,
                                         BindingResult bindingResult) ;

    @ApiOperation(value = "用户留言",notes="用户留言",httpMethod = "GET")
    @GetMapping("/counts")
    public GraceJSONResult counts(@RequestParam String articleId) ;

    @ApiOperation(value = "获取文章的用户评论",notes="获取文章的用户评论",httpMethod = "GET")
    @GetMapping("/list")
    public GraceJSONResult list(@RequestParam String articleId,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize
                                ) ;

    @PostMapping("/delete")
    @ApiOperation(value = "作者删除评论", notes = "作者删除评论", httpMethod = "POST")
    public GraceJSONResult delete(@RequestParam String writerId,
                                  @RequestParam String commentId);

    @PostMapping("/mng")
    @ApiOperation(value = "查询我的评论管理列表", notes = "查询我的评论管理列表", httpMethod = "POST")
    public GraceJSONResult mng(@RequestParam String writerId,
                               @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
                               @RequestParam Integer page,
                               @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
                               @RequestParam Integer pageSize);

}
