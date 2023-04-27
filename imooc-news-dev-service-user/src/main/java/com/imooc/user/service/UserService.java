package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

/**
 * @author 昴星
 * @date 2023-03-13 21:00
 * @explain
 */
public interface UserService {
    public AppUser queryMobileIsExit(String mobile);
    public AppUser createUser(String mobile);

    public AppUser getUserById(String userId);

    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);

    public AppUser getUser(String userId);
}
