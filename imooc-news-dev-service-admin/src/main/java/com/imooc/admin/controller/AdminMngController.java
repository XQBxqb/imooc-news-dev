package com.imooc.admin.controller;

import com.imooc.admin.service.AdminService;
import com.imooc.api.BaseController;
import com.imooc.api.config.RedisOperator;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import wiremock.org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * @author 昴星
 * @date 2023-04-01 10:49
 * @explain
 */

@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi  {

    final static Logger log=LoggerFactory.getLogger(AdminMngController.class);
    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private FaceVerifyUtils faceVerifyUtils;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response) {
        String adminName=adminLoginBO.getUsername();
        String password=adminLoginBO.getPassword();
        AdminUser adminUser = adminService.queryAdminByName(adminName);
        if(adminUser==null) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        //checkpw(),plaintext:明文，就是前台传来的密码,后面就是hashed过的在数据库内部密码;
        boolean isPwdMatch = BCrypt.checkpw(password, adminUser.getPassword());
        if(!isPwdMatch) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        doLoginSetting(adminUser,request,response);
        //getHeders(request);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        Boolean nameExit = isNameExit(username);
        if(nameExit) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO adminBO) {
        String adminName = adminBO.getAdminName();
        String password = adminBO.getPassword();
        String confirmPassword = adminBO.getConfirmPassword();
        String faceId = adminBO.getFaceId();
        if( StringUtils.isBlank(faceId)){
            if(StringUtils.isBlank(adminName)) return GraceJSONResult.errorMsg("账号名称不能为空");
            if(StringUtils.isBlank(password)||StringUtils.isBlank(confirmPassword)) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            if(!password.equals(confirmPassword)) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }
        adminService.addNewAdmin(adminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        if(page==null) page= PageCommon.DEFAULT_PAGE;
        if(pageSize==null) pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        PagedGridResult adminList = adminService.queryAdminList(page, pageSize);
        return GraceJSONResult.ok(adminList);
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        //删除redis中的token
        redisOperator.del(RedisCommon.REDIS_ADMIN_TOKEN+":"+adminId);

        //删除cookie
        try {
            deleteCookie(CookieCommon.ADMIN_NAME,response,request);
            deleteCookie(CookieCommon.ADMIN_UID,response,request);
            deleteCookie(CookieCommon.ADMIN_UTOKEN,response,request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminFaceLogin(AdminLoginBO adminLoginBO,
                                          HttpServletRequest request,
                                          HttpServletResponse response) {
        //log.info("------进入adminFaceLogin的方法--------adminLoginBO:"+adminLoginBO.getUsername()+"-----"+adminLoginBO.getImg64());
        String adminName=adminLoginBO.getUsername();
        String adminImg64=adminLoginBO.getImg64();
        if(StringUtils.isBlank(adminName)) return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NAME_NULL_ERROR);
        if(StringUtils.isBlank(adminImg64)) return  GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_FACE_NULL_ERROR);
        AdminUser admin = adminService.queryAdminByName(adminName);
        String faceId = admin.getFaceId();
        log.info("--------获得的faceId："+faceId);

        if(StringUtils.isBlank(faceId)) return GraceJSONResult.errorCustom(ResponseStatusEnum.FACE_VERIFY_LOGIN_ERROR);

        String encode=null;

        try {
            encode = URLEncoder.encode(adminImg64, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        //通过faceID在File中通过GridFS查找img
        String imgServerUrl="http://files.imoocnews.com:7004/fs/readImg64ByFaceIdInGridFS/"+encode+"/"+faceId;

        ResponseEntity<GraceJSONResult> resultEntity = restTemplate.getForEntity(imgServerUrl, GraceJSONResult.class);

        GraceJSONResult body = resultEntity.getBody();

        log.info("-----------从file服务中返回的GraceJSONResult:"+resultEntity.getBody());

        String base64DB = (String) body.getData();

        Boolean isVerify;

        try {
             isVerify=faceVerifyUtils.faceVerify(base64DB,adminImg64);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(isVerify) return GraceJSONResult.ok();
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FACE_VERIFY_LOGIN_ERROR);
    }

    ;
    /**
     * @description:用于admin登录之后的设置
     * @param admin
     * @param request
     * @param response
     * @return: void
     * @author: 昴星
     * @time: 2023/4/1 11:07
     */
    private  void doLoginSetting(AdminUser admin,
                               HttpServletRequest request,
                               HttpServletResponse response){
        String token= UUID.randomUUID().toString();
        //保存token到redis
        redisOperator.set(RedisCommon.REDIS_ADMIN_TOKEN+":"+admin.getId(),token);
        //保存基本信息到cookie
        setCookie(CookieCommon.ADMIN_UTOKEN,token,response,request,MAX_AGE);
        setCookie(CookieCommon.ADMIN_UID,admin.getId(),response,request,MAX_AGE);
        setCookie(CookieCommon.ADMIN_NAME,admin.getUsername(),response,request,MAX_AGE);
    }

    private Boolean isNameExit(String name){
        AdminUser adminUser = adminService.queryAdminByName(name);
        if(adminUser!=null) return true;
        return false;
    }
}
