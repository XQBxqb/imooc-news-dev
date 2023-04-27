package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.config.DateConverterConfig;
import com.imooc.api.controller.user.AppUserMngControllerApi;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.AppUserService;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PageUtils;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.DateUtil;
import com.imooc.utils.extend.PageCommon;
import com.imooc.utils.extend.RedisCommon;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hp
 * @date 2023-03-08 16:41
 * @explain
 */

@RestController
public class AppUserMngController implements AppUserMngControllerApi {
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private DateConverterConfig dateConverterConfig;

    @Override
    public GraceJSONResult queryAll(String nickname,
                                    Integer status,
                                    String startDate,
                                    String endDate,
                                    Integer page, Integer pageSize) {
        Date beginDate=null;
        Date edDate=null;
        if(StringUtils.isNotBlank(startDate)) beginDate=dateConverterConfig.parseDate(startDate,"yyyy-MM-dd");
        if(StringUtils.isNotBlank(endDate)) edDate=dateConverterConfig.parseDate(endDate,"yyyy-MM-dd");
        if(page==null) {page= PageCommon.DEFAULT_PAGE;}
        if(pageSize==null) {page=PageCommon.DEFAULT_PAGE_SIZE;}

        List<AppUser> userList = appUserService.queryListByCondition(nickname, status, DateUtil.stringToDate(startDate,DateUtil.ISO_EXPANDED_DATE_FORMAT),
                                                                    DateUtil.stringToDate(endDate,DateUtil.ISO_EXPANDED_DATE_FORMAT), page, pageSize);

        PagedGridResult gridResult = PageUtils.pagedResult(userList, page);

        return GraceJSONResult.ok(gridResult);
    }
}
