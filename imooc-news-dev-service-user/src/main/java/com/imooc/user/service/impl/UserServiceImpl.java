package com.imooc.user.service.impl;

import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.expection.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.api.config.RedisOperator;
import org.n3r.idworker.Sid;
import com.imooc.pojo.AppUser;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.UserService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.extend.DateUtil;
import org.slf4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author 昴星
 * @date 2023-03-13 21:00
 * @explain 改实现是基于tk.mybatis实现的，当然可以用mybatisplus实现，更好
 */

@Service
public class UserServiceImpl implements UserService {
    final static Logger logger= getLogger(UserServiceImpl.class);

    @Autowired
    private AppUserMapper appUserMapper;

    @Autowired
    private RedisOperator redisOperator;

    @Autowired
    private Sid sid;//sid就是雪花算法实现的主键生成

    @Override
    public AppUser queryMobileIsExit(String mobile) {
        Example userExample =new Example(AppUser.class);
        Example.Criteria userCriteria=userExample.createCriteria();
        userCriteria.andEqualTo("mobile",mobile);
        AppUser appUser = appUserMapper.selectOneByExample(userExample);
        return appUser;
    }
    /**
     * @description:Transactional是事务的回滚注解，假如有RuntimeEx..或其子类出现报错，那么就会回滚并且给前端报错，但是不能用于try catch和Ex..，一般用于更新数据，查询不需要
     * @param mobile
     * @return: com.imooc.pojo.AppUser
     * @author: 昴星
     * @time: 2023/3/14 18:07
     */
    @Transactional
    @Override
    public AppUser createUser(String mobile) {
        String userId=sid.nextShort();
        AppUser appUser=new AppUser();
        appUser.setId(userId);
        appUser.setMobile(mobile);
        //DesensitizationUtil.commonDisplay(mobile)是用来脱敏信息的，在数据库层存入的是该算法实现的脱敏后的数据
        appUser.setNickname("用户:"+ DesensitizationUtil.commonDisplay(mobile));
        appUser.setBirthday(DateUtil.stringToDate("2000-01-01"));
        appUser.setSex(Sex.secret.type);
        appUser.setActiveStatus(UserStatus.INACTIVE.type);
        appUser.setTotalIncome(0);
        appUser.setFace("https://tupian.qqw21.com/article/UploadPic/2021-1/20211722215388977.jpg");
        appUser.setCreatedTime(new Date());
        appUser.setUpdatedTime(new Date());
        appUserMapper.insert(appUser);
        return appUser;
    }

    @Override
    public AppUser getUserById(String userId) {
        AppUser appUser = appUserMapper.selectByPrimaryKey(userId);
        return appUser;
    }
    @Override
    public AppUser getUser(String userId) {
        return appUserMapper.selectByPrimaryKey(userId);
    }
    @Override
    /**
     * @description:为了更新使redis和数据库的数据保持一致，更新前先删除redis内的数据，在更新数据库以后，再次查询数据库内部的数据，然后把数据更新到redis内部。还可以采用缓存双删
     * @param updateUserInfoBO
     * @return: void
     * @author: 昴星
     * @time: 2023/3/16 22:51
     */
    public void updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        String userId=updateUserInfoBO.getId();
        AppUser userInfo=new AppUser();
        //先删除redis内的数据
        redisOperator.del(RedisCommon.REDIS_USER_INFO+":"+userId);
        BeanUtils.copyProperties(updateUserInfoBO,userInfo);
        userInfo.setActiveStatus(UserStatus.ACTIVE.type);
        userInfo.setUpdatedTime(new Date());
        int res = appUserMapper.updateByPrimaryKeySelective(userInfo);
        if(res!=1) {
            GraceException.display(ResponseStatusEnum.USER_UPDATE_ERROR);
        }
        AppUser user = getUser(userId);
        //设置redis内的数据
        redisOperator.set(RedisCommon.REDIS_USER_INFO+":"+userId, JsonUtils.objectToJson(user));
        // 缓存双删策略
        try {
            Thread.sleep(100);
            redisOperator.del(RedisCommon.REDIS_USER_INFO + ":" + userId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
