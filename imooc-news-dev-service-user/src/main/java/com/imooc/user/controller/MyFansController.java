package com.imooc.user.controller;

import com.imooc.api.controller.user.MyfansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.service.FanService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.PageCommon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-05-13 20:58
 * @explain
 */
@RestController
public class MyFansController implements MyfansControllerApi {
    @Autowired
    private FanService fanService;

    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean isFan = fanService.isWriterFan(writerId, fanId);
        return GraceJSONResult.ok(isFan);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        boolean res = fanService.followWriter(writerId, fanId);
        if(!res) return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        boolean res = fanService.unfollowWriter(writerId, fanId);
        if(!res) return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        Integer manCount = fanService.queryFansCount(writerId, Sex.man);
        Integer womanCount = fanService.queryFansCount(writerId, Sex.woman);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCount);
        fansCountsVO.setWomanCounts(womanCount);
        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        if(StringUtils.isBlank(writerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
        }
        if(page==null){
            page= PageCommon.DEFAULT_PAGE;
        }
        if(pageSize==null){
            pageSize=PageCommon.DEFAULT_PAGE_SIZE;
        }
        PagedGridResult gridResult = fanService.queryAllFans(writerId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryRatioByRegion(String writerId) {
        if(StringUtils.isBlank(writerId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_RESPONSE_NO_INFO);
        }
        List<RegionRatioVO> regionRatioVOS = fanService.queryRegionRatioList(writerId);
        return GraceJSONResult.ok(regionRatioVOS);
    }

}
