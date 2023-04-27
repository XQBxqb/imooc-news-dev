package com.imooc.utils.extend;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author hp
 * @date 2023-03-08 14:03
 * @explain
 */

@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
@EnableConfigurationProperties(AliyunResource.class)
public class AliyunResource {
   private String  accessKeyId ;
   private String accessKeySecret;

   private String QualityScoreThreshold;

   private int ConfidenceLevel;


    public String getAccessKeyId() {
        return accessKeyId;
    }

    public int getConfidenceLevel() {
        return ConfidenceLevel;
    }

    public void setConfidenceLevel(int confidenceLevel) {
        ConfidenceLevel = confidenceLevel;
    }

    public String getQualityScoreThreshold() {
        return QualityScoreThreshold;
    }

    public void setQualityScoreThreshold(String qualityScoreThreshold) {
        QualityScoreThreshold = qualityScoreThreshold;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
