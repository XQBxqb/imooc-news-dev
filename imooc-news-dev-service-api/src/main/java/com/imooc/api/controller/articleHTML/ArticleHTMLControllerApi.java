package com.imooc.api.controller.articleHTML;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 昴星
 * @date 2023-04-30 13:01
 * @explain
 */
@ApiOperation(value = "文字领域的Controller",tags = {"文字领域的Controller"})
@RequestMapping("article/html")
public interface ArticleHTMLControllerApi {

    @GetMapping("/upLoadArticleHtmlFromGridFS")
    public Integer upLoadArticleHtmlFromGridFS(@RequestParam String articleId,
                                               @RequestParam String mongodId);
}
