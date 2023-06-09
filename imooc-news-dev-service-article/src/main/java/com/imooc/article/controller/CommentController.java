package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.CommentControllerApi;
import com.imooc.article.service.CommentService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.PageCommon;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author 昴星
 * @date 2023-05-19 22:27
 * @explain
 */

@RestController
public class CommentController extends BaseController implements CommentControllerApi {
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisOperator redis;
    @Autowired
    private RestTemplate restTemplate;


    @Override
    public GraceJSONResult createComment(CommentReplyBO commentReplyBO, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            Map<String, String> stringStringMap = errorsMap(bindingResult);
            return GraceJSONResult.errorMap(stringStringMap);
        }
        Set<String> idSet=new HashSet<>();
        idSet.add(commentReplyBO.getCommentUserId());

        String url="http://user.imoocnews.com:7003/user/getUserListByIds?ids="+ JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> response = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult graceJSONResult = response.getBody();
        String listJson = JsonUtils.objectToJson(graceJSONResult.getData());
        AppUserVO appUserVO = JsonUtils.jsonToList(listJson, AppUserVO.class).get(0);

        String nickName=appUserVO.getNickname();
        String face=appUserVO.getFace();
        String fatherId=commentReplyBO.getFatherId();
        String articleId=commentReplyBO.getArticleId();
        String commentUserId=commentReplyBO.getCommentUserId();
        String content=commentReplyBO.getContent();
        // String father_id,String articleId,String commentUserId,String commentUserNickname,String commentUserFace,String content
        Integer res = commentService.createComment(fatherId,articleId,commentUserId,nickName,face,content);
        if(res!=1) return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        return  GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult counts(String articleId) {
        Integer num = redis.getNum(RedisCommon.REDIS_ARTICLE_COMMENT_COUNT + ":" + articleId);
        return GraceJSONResult.ok(num);
    }

    @Override
    public GraceJSONResult list(String articleId, Integer page, Integer pageSize) {
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = commentService.queryArticleComment(articleId,
                page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult delete(String writerId, String commentId) {
        int res = commentService.deleteArticleComment(writerId, commentId);
        if(res!=1) return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult mng(String writerId, Integer page, Integer pageSize) {
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = commentService.queryMyArticleComment(writerId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

}
