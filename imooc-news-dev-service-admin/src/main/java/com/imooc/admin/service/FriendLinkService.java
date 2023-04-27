package com.imooc.admin.service;

import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-11 19:03
 * @explain
 */


public interface FriendLinkService {
    List<FriendLinkMO> queryFriendLinkList();
    @Transactional
    void insertFriendLink(FriendLinkMO friendLinkMO);
    @Transactional
    void deleteFriendLink(String linkId);

    List<FriendLinkMO> queryFriendLinkWithNotDelete();
}
