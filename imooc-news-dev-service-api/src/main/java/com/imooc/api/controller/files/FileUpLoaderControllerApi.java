package com.imooc.api.controller.files;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author hp
 * @date 2023-03-05 15:34
 * @explain
 */
@Api(value = "文件上传controller",tags = {"文件上传controller"})
@RequestMapping("fs")
public interface FileUpLoaderControllerApi {
    /**
     * @description:这个和下面的不同，它的功能是在user服务中，更改用户头像到oss中
     * @param userId
     * @param file
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/4/8 13:36
     */
    @PostMapping ("/uploadFace")
    public GraceJSONResult uploadFace(@RequestParam String userId, MultipartFile file) throws Exception;
    /**
     * @description:文件上传到GridFS，由于一些阿里云因素，需要将图片 上传到oss中
     * @param adminBO
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/4/3 20:28
     */
    @PostMapping("/uploadToGridFS")
    public GraceJSONResult uploadToGridFS(@RequestBody NewAdminBO adminBO) throws Exception;

    /**
     * @description: 从GridFS读取图片到前端
     * @param faceId
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/4/3 21:36
     */
    @GetMapping("/readInGridFS")
    public void readInGridFS(String faceId,
                             HttpServletRequest request,
                             HttpServletResponse response) throws Exception;
    /**
     * @description:这个是服务之间调用的，用于人脸验证中获取ImgBase64以及上传图片到oss
     * @param faceId
     * @param request
     * @param response
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/4/5 20:06
     */
    @GetMapping("/readImg64ByFaceIdInGridFS/{base60InToOOS}/{faceId}")
    public GraceJSONResult readImg64ByFaceIdInGridFS(@PathVariable(value = "base60InToOOS") String base60InToOOS,
                                                     @PathVariable(value = "faceId") String faceId,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response) throws Exception;

    /**
     * @description:用于用户实现发布文章多图片处理
     * @param userId
     * @param files
     * @return: com.imooc.grace.result.GraceJSONResult
     * @author: 昴星
     * @time: 2023/4/27 21:38
     */
    @PostMapping ("/uploadSomeFiles")
    public GraceJSONResult uploadSomeFiles(@RequestParam String userId, MultipartFile[] files) throws Exception;
}
