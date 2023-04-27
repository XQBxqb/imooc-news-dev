package com.imooc.admin.service.impl;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

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
    @Override
    public List<FriendLinkMO> queryFriendLinkList() {

        List<FriendLinkMO> listMO = friendLinkRepository.findAll();
        return listMO;
    }

    @Override
    public void insertFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO);
    }

    @Override
    public void deleteFriendLink(String linkId) {
        friendLinkRepository.deleteById(linkId);
    }

}
