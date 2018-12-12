package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.AccountFsstransPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 生利宝
 */
@Mapper
public interface AccountFsstransMapper {


    /**
     * 根据用户id获取用户生利宝
     * @param userId
     * @return
     */
     AccountFsstransPO getFsstransByUserId(@Param("userId") String userId);




}
