package com.imooc.user.mapper;

import com.imooc.mymapper.MyMapper;
import com.imooc.pojo.AppUser;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Mapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Repository;

/**
 * @author 昴星
 * @date 2023-03-13 20:37
 * @explain
 */
@Repository
public interface AppUserMapper extends MyMapper<AppUser> {
}
