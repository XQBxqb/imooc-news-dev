package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.springframework.stereotype.Service;

/**
 * @author 昴星
 * @date 2023-04-01 9:38
 * @explain
 */
public interface AdminService {
    public AdminUser queryAdminByName(String name);

    public void addNewAdmin(NewAdminBO newAdmin);

    public PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
