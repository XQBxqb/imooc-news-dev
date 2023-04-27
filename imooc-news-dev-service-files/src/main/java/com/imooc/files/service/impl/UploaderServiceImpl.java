package com.imooc.files.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.files.FileResource;
import com.imooc.files.service.UploaderService;
import com.imooc.utils.extend.AliyunResource;
import com.imooc.utils.extend.DateUtil;
import org.n3r.idworker.Sid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @author 昴星
 * @date 2023-03-23 22:06
 * @explain FastFileStorageClient是官方提供的fastdfs API接口客户端
 *          MultipartFile和DiskFileItemFactory一样通常是处理长传文件的工具类，不过和前端的整合不同，具体要看运用的前端知识
 */

@Service
public class UploaderServiceImpl  implements UploaderService {

    final static Logger logger= LoggerFactory.getLogger(UploaderServiceImpl.class);
    @Autowired
    public AliyunResource aliyunResource;

    @Autowired
    public FileResource fileResource;

    @Autowired
    public FastFileStorageClient fastFileStorageClient;

    @Autowired
    public Sid sid;

    @Override
    public String uploadFdfs(MultipartFile file,
                             String fileExtName) throws Exception {
         String accessKeyId = aliyunResource.getAccessKeyId();
         String accessKeySecret = aliyunResource.getAccessKeySecret();
         String bucketName= fileResource.getBucketName();
         String endpoint=fileResource.getEndPoint();
         String objectName= fileResource.getObjectName();

         String faceDataBase=fileResource.getFaceDataBase();
         StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(),
                file.getSize(),
                fileExtName, null);
        return storePath.getPath();

    }

    @Override
    public String uploadOOS (MultipartFile file,
                            String userId,
                            String fileExtName,
                            String rootDirectory) throws Exception {
         String accessKeyId = aliyunResource.getAccessKeyId();
         String accessKeySecret = aliyunResource.getAccessKeySecret();
         String bucketName= fileResource.getBucketName();
         String endpoint=fileResource.getEndPoint();
        //创建oss实例
        OSS client =  new  OSSClientBuilder().build(endpoint,
                                                    accessKeyId,
                                                    accessKeySecret);
        InputStream inputStream=file.getInputStream();
        String fileName=sid.nextShort();
        String fileObjectName=rootDirectory+"/"+userId+"/" + fileName+"."+fileExtName;
        //开启网络流
        client.putObject(bucketName, fileObjectName, inputStream);
        inputStream.close();
        client.shutdown();
        return fileObjectName;
    }

    @Override
    public void uploadFaceOOSByStream(InputStream faceStream, String pathName,String fileExtName)  {
        String accessKeyId = aliyunResource.getAccessKeyId();
        String accessKeySecret = aliyunResource.getAccessKeySecret();
        String bucketName= fileResource.getBucketName();
        String endpoint=fileResource.getEndPoint();
        String faceDataBase=fileResource.getFaceDataBase();;
        OSS client =  new  OSSClientBuilder().build(endpoint,
                accessKeyId,
                accessKeySecret);
        byte[] bytes = faceDataBase.getBytes();
        if(bytes==null) logger.warn("---bytes为null-----");
        logger.info(bytes.toString());
        String imgName = DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);
        String imgObjectName=faceDataBase+"/"+pathName+"/"+imgName+fileExtName;
        client.putObject(bucketName, imgObjectName, faceStream);
        client.shutdown();
    }

}
