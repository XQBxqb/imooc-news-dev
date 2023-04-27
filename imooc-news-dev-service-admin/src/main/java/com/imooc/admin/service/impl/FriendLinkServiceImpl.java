package com.imooc.admin.service.impl;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.extend.RedisCommon;
import com.imooc.utils.extend.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-11 19:06
 * @explain
 */
@Service
public class FriendLinkServiceImpl implements FriendLinkService {
    @Autowired
    private FriendLinkRepository friendLinkRepository;

    @Autowired
    private RedisOperator redis;
    @Override
    public List<FriendLinkMO> queryFriendLinkList() {

        List<FriendLinkMO> listMO = friendLinkRepository.findAll();
        return listMO;
    }

    @Override
    public void insertFriendLink(FriendLinkMO friendLinkMO) {
        redis.del(RedisCommon.REDIS_ALL_FRIENDLINK);
        redis.del(RedisCommon.REDIS_NOT_DELETE_FRIENDLINK);
        friendLinkRepository.save(friendLinkMO);
    }

    @Override
    public void deleteFriendLink(String linkId) {
        redis.del(RedisCommon.REDIS_ALL_FRIENDLINK);
        redis.del(RedisCommon.REDIS_NOT_DELETE_FRIENDLINK);
        friendLinkRepository.deleteById(linkId);
    }

    @Override
    public List<FriendLinkMO> queryFriendLinkWithNotDelete() {
        List<FriendLinkMO> friendLinkList = friendLinkRepository.findAllByIsDelete(YesOrNo.NO.type);
        return friendLinkList;
    }

}
