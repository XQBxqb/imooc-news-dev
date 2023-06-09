package com.imooc.article.html.controller;

import com.aliyuncs.http.HttpUtil;
import com.imooc.api.controller.articleHTML.ArticleHTMLControllerApi;
import com.mongodb.client.gridfs.GridFSBucket;
import java.io.File;

import freemarker.template.Configuration;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 昴星
 * @date 2023-05-26 14:50
 * @explain
 */
@RestController
public class ArticleHTMLController implements ArticleHTMLControllerApi {
    @Autowired
    private GridFSBucket gridFSBucket;

    @Value("${freemarker.html.article}")
    private String articlePath;

    @Override
    public Integer upLoadArticleHtmlFromGridFS(String articleId, String mongodId) {

        try {
            File file = new File(articlePath+File.separator+articleId+".html");
            file.createNewFile();
            OutputStream out = new FileOutputStream(file);
            ObjectId objectId = new ObjectId(mongodId) ;
            gridFSBucket.downloadToStream(objectId,out);
            out.close();
        } catch (IOException e) {
            return HttpStatus.EXPECTATION_FAILED.value();
        }

        return HttpStatus.OK.value();
    }

}
