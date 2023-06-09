package com.imooc.user.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.enums.Sex;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fans;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.FanService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import com.imooc.utils.extend.RegionCommonList;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-05-13 21:01
 * @explain
 */

@Service
public class FanServiceImpl implements FanService {
    @Autowired
    private Sid sid;

    @Autowired
    private FansMapper fansMapper;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private AppUserMapper appUserMapper;
    @Override
    public boolean isWriterFan(String writerId, String userId) {
        Fans fan = new Fans();
        fan.setFanId(userId);
        fan.setWriterId(writerId);
        int count = fansMapper.selectCount(fan);
        return count > 0 ? true:false;
    }
    @Transactional
    @Override
    public boolean followWriter(String writerId, String userId) {
        String id = sid.nextShort();
        Fans fan = new Fans();
        fan.setId(id);
        fan.setWriterId(writerId);
        fan.setFanId(userId);
        AppUser appUser = appUserMapper.selectByPrimaryKey(userId);
        fan.setFanNickname(appUser.getNickname());
        fan.setFace(appUser.getFace());
        fan.setSex(appUser.getSex());
        fan.setProvince(appUser.getProvince());
        redisOperator.increment(RedisCommon.REDIS_MY_FANS_COUNT+":"+writerId,1);
        redisOperator.increment(RedisCommon.REDIS_MY_FOLLOWED_COUNT+":"+userId,1);

        int res = fansMapper.insert(fan);
        return res!=0?true:false;
    }
    @Transactional
    @Override
    public boolean unfollowWriter(String writerId, String userId) {
        Fans fan = new Fans();
        fan.setWriterId(writerId);
        fan.setFanId(userId);
        redisOperator.decrement(RedisCommon.REDIS_MY_FANS_COUNT+":"+writerId,1);
        redisOperator.decrement(RedisCommon.REDIS_MY_FOLLOWED_COUNT+":"+userId,1);
        int delete = fansMapper.delete(fan);
        return delete == 1 ? true:false;
    }

    @Override
    public Integer queryFansCount(String userId, Sex sex) {
        Fans fan = new Fans();
        fan.setWriterId(userId);
        fan.setSex(sex.type);
        int count = fansMapper.selectCount(fan);
        return count;
    }

    @Override
    public PagedGridResult queryAllFans(String writerId, Integer page, Integer pageSize) {
        Fans fan=new Fans();
        fan.setWriterId(writerId);
        PageHelper.startPage(page,pageSize);
        List<Fans> fansList = fansMapper.select(fan);
        return setterPagedGrid(fansList,page);
    }

    @Override
    public List<RegionRatioVO> queryRegionRatioList(String writerId) {
        List<RegionRatioVO> regionRatioVOS = new ArrayList<>();
        String[] regionsName=RegionCommonList.regions;
        RegionRatioVO ratioVO = new RegionRatioVO();
        for(String regionName:regionsName){
            Fans fans = new Fans();
            fans.setWriterId(writerId);
            fans.setProvince(regionName);
            int count = fansMapper.selectCount(fans);
            ratioVO.setValue(count);
            ratioVO.setName(regionName);
            regionRatioVOS.add(ratioVO);
        }
        return regionRatioVOS;
    }

    public PagedGridResult setterPagedGrid(List<?> list,
                                           Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setRows(list);
        gridResult.setPage(page);
        gridResult.setRecords(pageList.getTotal());
        gridResult.setTotal(pageList.getPages());
        return gridResult;
    }
}
