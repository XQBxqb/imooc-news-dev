package com.imooc.files;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author 昴星
 * @date 2023-03-25 16:08
 * @explain
 */

@Component
@PropertySource("classpath:file-${spring.profiles.active}.properties")
@ConfigurationProperties(prefix = "file")
public class FileResource {

    private String host;

    private String endPoint;

    private String bucketName;

    private String faceTempDataBaseObjectName;

    private String facePerDataBaseObjectName;

    private String articleImgDataBase;

    public String getArticleImgDataBase() {
        return articleImgDataBase;
    }

    public void setArticleImgDataBase(String articleImgDataBase) {
        this.articleImgDataBase = articleImgDataBase;
    }

    public String getFaceDataBase() {
        return faceDataBase;
    }

    public void setFaceDataBase(String faceDataBase) {
        this.faceDataBase = faceDataBase;
    }

    private String faceDataBase;

    public String getFacePerDataBaseObjectName() {
        return facePerDataBaseObjectName;
    }

    public void setFacePerDataBaseObjectName(String facePerDataBaseObjectName) {
        this.facePerDataBaseObjectName = facePerDataBaseObjectName;
    }

    public String getFaceTempDataBaseObjectName() {
        return faceTempDataBaseObjectName;
    }

    public void setFaceTempDataBaseObjectName(String faceTempDataBaseObjectName) {
        this.faceTempDataBaseObjectName = faceTempDataBaseObjectName;
    }

    private String objectName;


    public String getOssHost() {
        return ossHost;
    }

    public void setOssHost(String ossHost) {
        this.ossHost = ossHost;
    }

    private String ossHost;
    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}
