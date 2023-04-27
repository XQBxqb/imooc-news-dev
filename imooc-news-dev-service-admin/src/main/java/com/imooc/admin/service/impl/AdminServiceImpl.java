package com.imooc.admin.service.impl;

import com.github.pagehelper.PageInfo;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminService;
import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wiremock.org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-01 9:39
 * @explain
 */

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    AdminUserMapper adminUserMapper;

    @Autowired
    private Sid sid;

    @Autowired
    AdminUserMapper mapper;

    @Override
    public AdminUser queryAdminByName(String name) {
        Example adminExample=new Example(AdminUser.class);
        Example.Criteria criteria=adminExample.createCriteria();
        criteria.andEqualTo("username",name);
        AdminUser admin=adminUserMapper.selectOneByExample(adminExample);
        return admin;
    }

    @Transactional
    @Override
    public void addNewAdmin(NewAdminBO newAdmin) {
        String adminId=sid.nextShort();

        AdminUser adminUser=new AdminUser();
        adminUser.setUsername(newAdmin.getUsername());
        adminUser.setAdminName(newAdmin.getAdminName());
        String pwsHashed= BCrypt.hashpw(newAdmin.getPassword(),BCrypt.gensalt());
        adminUser.setPassword(pwsHashed);
        adminUser.setId(adminId);
        if(StringUtils.isNotBlank(newAdmin.getFaceId())) adminUser.setFaceId(newAdmin.getFaceId());

        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());
        int res=mapper.insert(adminUser);
        if(res!=1) GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example adminExample=new Example(AdminUser.class);
        adminExample.orderBy("createdTime").desc();
        List<AdminUser> adminUsers = adminUserMapper.selectByExample(adminExample);
        PagedGridResult pagedResult = getPagedResult(page, adminUsers);
        return pagedResult;
    }

    private PagedGridResult getPagedResult(Integer page,List<?> resultList){
        PageInfo<?> pageInfo=new PageInfo<>(resultList);
        PagedGridResult gridResult = new PagedGridResult();
        gridResult.setPage(page);
        gridResult.setRecords(pageInfo.getPages());
        gridResult.setRows(resultList);
        gridResult.setTotal(pageInfo.getTotal());
        return gridResult;
    }
}
