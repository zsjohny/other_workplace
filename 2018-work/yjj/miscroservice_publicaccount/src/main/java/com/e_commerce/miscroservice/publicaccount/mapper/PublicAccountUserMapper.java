package com.e_commerce.miscroservice.publicaccount.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser;
import com.e_commerce.miscroservice.commons.entity.user.PublicAccountUserQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/24 15:26
 * @Copyright 玖远网络
 */
@Mapper
public interface PublicAccountUserMapper{


    /**
     * 更新账户信息
     *
     * @param updInfo updInfo
     * @return int
     * @author Charlie
     * @date 2018/9/24 23:36
     */
    int updateByPrimaryKeySelective(PublicAccountUser updInfo);

    /**
     * 查询用户
     *
     * @param query query
     * @return java.util.List<com.e_commerce.miscroservice.commons.entity.application.user.PublicAccountUser>
     * @author Charlie
     * @date 2018/10/7 20:18
     */
    List<PublicAccountUserQuery> listUser(PublicAccountUserQuery query);

}
