package com.imooc.admin.repository;

import com.imooc.pojo.mo.FriendLinkMO;
import io.swagger.models.auth.In;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 昴星
 * @date 2023-04-11 19:07
 * @explain
 */
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {
    List<FriendLinkMO> findAllByIsDelete(Integer isDelete);
}
