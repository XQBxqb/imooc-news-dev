package com.imooc.files.controller;

import com.aliyun.core.utils.StringUtils;
import com.imooc.api.controller.files.FileUpLoaderControllerApi;
import com.imooc.expection.GraceException;
import com.imooc.files.FileResource;
import com.imooc.files.service.UploaderService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.FileUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author 昴星
 * @date 2023-03-25 14:05
 * @explain
 */
@RestController
public class FileUpLoaderController implements FileUpLoaderControllerApi {
    final static Logger logger= getLogger(FileUpLoaderController.class);

    @Autowired
    private UploaderService uploaderService;

    @Autowired
    private FileResource fileResource;

    @Autowired
    private GridFSBucket gridFSBucket;


    @Override
    public GraceJSONResult uploadFace(String userId,
                                      MultipartFile file) throws Exception {
        if(StringUtils.isBlank(userId)) return GraceJSONResult.errorMsg("传输异常");
        if(file==null) return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_NULL_ERROR);
        String fileName = file.getOriginalFilename();
        String[] str = fileName.split("\\.");
        String suffixName=str[str.length-1];
        if(!suffixName.equalsIgnoreCase("png")&&
           !suffixName.equalsIgnoreCase("jpg")&&
           !suffixName.equalsIgnoreCase("bmp"))
        {return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_FORMATTER_FAILD);}
        String picPath=uploaderService.uploadOOS(file,userId,suffixName,fileResource.getObjectName());
        if(StringUtils.isBlank(picPath)) return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        //logger.info("--------Path  :  "+ fileResource.getOssHost() +picPath);
        return GraceJSONResult.ok(fileResource.getOssHost()+picPath);
    }

    @Override
    public GraceJSONResult uploadToGridFS(NewAdminBO adminBO) throws Exception {
        String adminBOImg64 = adminBO.getImg64();
        //adminBOImg64.trim()就是删除头尾空白符的字符串。
        byte[] bytes = new BASE64Decoder().decodeBuffer(adminBOImg64);
        InputStream inputStreamOOS = new ByteArrayInputStream(bytes);
        InputStream inputStreamGridFS=new ByteArrayInputStream(bytes);
        //上传到oss中
        uploaderService.uploadFaceOOSByStream(inputStreamOOS, fileResource.getFacePerDataBaseObjectName(), ".png");
        //上传到GridFS中
        ObjectId objectId = gridFSBucket.uploadFromStream(adminBO.getUsername() + ".png", inputStreamGridFS);
        String fileId = objectId.toString();
        return GraceJSONResult.ok(fileId);
    }

    @Override
    public void readInGridFS(String faceId,
                                        HttpServletRequest request,
                                        HttpServletResponse response) throws Exception {

        if(StringUtils.isBlank(faceId)) GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        // 1. 从gridfs中读取
        File adminFace = readGridFSByFaceId(faceId);
        // 2. 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(response, adminFace);
    }



    private File readGridFSByFaceId(String faceId)throws Exception{
        GridFSFindIterable gridFSFiles
                = gridFSBucket.find(Filters.eq("_id", new ObjectId(faceId)));

        GridFSFile gridFS = gridFSFiles.first();

        if (gridFS == null) {
            GraceException.display(ResponseStatusEnum.FILE_NOT_EXIST_ERROR);
        }

        String fileName = gridFS.getFilename();
        System.out.println(fileName);

        // 获取文件流，保存文件到本地或者服务器的临时目录
        File fileTemp = new File("/workspace/temp_face");
        if (!fileTemp.exists()) {
            fileTemp.mkdirs();
        }

        File myFile = new File("/workspace/temp_face/" + fileName);

        // 创建文件输出流
        OutputStream os = new FileOutputStream(myFile);
        // 下载到服务器或者本地
        gridFSBucket.downloadToStream(new ObjectId(faceId), os);

        return myFile;
    }

    @Override
    public GraceJSONResult readImg64ByFaceIdInGridFS(String base60InToOOS,
                                                    String faceId,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response
    ) throws Exception {
        File file = readGridFSByFaceId(faceId);

        String fileBase64 = FileUtils.fileToBase64(file);
        String base64 = URLDecoder.decode(base60InToOOS, "utf-8");
        byte[] bytes = new BASE64Decoder().decodeBuffer(base64);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        uploaderService.uploadFaceOOSByStream(inputStream,fileResource.getFaceTempDataBaseObjectName(),".png");
        return GraceJSONResult.ok(fileBase64);
    }

    @Override
    public GraceJSONResult uploadSomeFiles(String userId, MultipartFile[] files) throws Exception {
        if(StringUtils.isBlank(userId)) return GraceJSONResult.errorMsg("传输异常");
        List<String> imgListUrl= new ArrayList<>();
        for(MultipartFile file:files){
            String fileName = file.getOriginalFilename();
            String[] str = fileName.split("\\.");
            String suffixName=str[str.length-1];
            if(!suffixName.equalsIgnoreCase("png")&&
               !suffixName.equalsIgnoreCase("jpg")&&
               !suffixName.equalsIgnoreCase("bmp"))
            continue;
            String picUrl=uploaderService.uploadOOS(file,userId,suffixName,fileResource.getArticleImgDataBase());
            if(StringUtils.isBlank(picUrl)) continue;
            imgListUrl.add(picUrl);
        }
        return GraceJSONResult.ok(imgListUrl);
    }
}
