package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.enums.UserStatus;
import com.imooc.pojo.AppUser;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.AppUserService;
import com.imooc.utils.PageUtils;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-12 22:27
 * @explain
 */

@Service
public class AppUserServiceImpl implements AppUserService {
    @Autowired
    private AppUserMapper appUserMapper;


    @Override
    public List<AppUser> queryListByCondition(String nickname,
                                              Integer status,
                                              Date startDate, Date endDate,
                                              Integer page, Integer pageSize) {
        Example userExample = new Example(AppUser.class);
        userExample.orderBy("createdTime").desc();
        Example.Criteria criteria = userExample.createCriteria();
        if (StringUtils.isNotBlank(nickname)) {
            criteria.andLike("nickname", "%" + nickname + "%");
        }
        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("activeStatus", status);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("updatedTime", endDate);
        }
        PageHelper.startPage(page,pageSize);
        List<AppUser> list = appUserMapper.selectByExample(userExample);
        return list;
    }
}
