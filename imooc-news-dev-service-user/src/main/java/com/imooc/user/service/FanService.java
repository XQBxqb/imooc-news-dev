package com.imooc.user.service;

import com.imooc.enums.Sex;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-05-13 21:00
 * @explain
 */
public interface FanService {
    public boolean isWriterFan(String writerId, String userId);

    public boolean followWriter(String writerId,String userId);

    public boolean unfollowWriter(String writerId,String userId);

    public Integer queryFansCount(String userId, Sex sex);

    public PagedGridResult queryAllFans(String writerId, Integer page, Integer pageSize);

    public List<RegionRatioVO> queryRegionRatioList(String writerId);
}
