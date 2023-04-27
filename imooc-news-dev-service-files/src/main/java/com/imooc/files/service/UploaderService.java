package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 昴星
 * @date 2023-03-23 22:05
 * @explain
 */
public interface UploaderService {
    /**
     * @description: 使用fastDfs实现文件上传
     * @param file
     * @param fileExtName
     * @return: java.lang.String
     * @author: 昴星
     * @time: 2023/3/25 20:26
     */
    public String uploadFdfs (MultipartFile file, String fileExtName) throws Exception;

    /**
     * @description:使用OOS实现文件上传
     * @param file
     * @param userId
     * @param fileExtName
     * @return: java.lang.String
     * @author: 昴星
     * @time: 2023/3/25 20:26
     */
    public String uploadOOS(MultipartFile file,String userId,String fileExtName,String rootDirectory) throws Exception;
    /**
     * @description:这个就是用于流实现oos的上传
     * @param faceStream
     * @param fileExtName
     * @return: void
     * @author: 昴星
     * @time: 2023/4/8 14:13
     */
    public void uploadFaceOOSByStream(InputStream faceStream,String pathName,String fileExtName);
}
