package com.imooc.user.service;

import com.imooc.pojo.AppUser;

import java.util.Date;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-12 22:26
 * @explain
 */
public interface AppUserService {
    public List<AppUser>queryListByCondition(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);
}
