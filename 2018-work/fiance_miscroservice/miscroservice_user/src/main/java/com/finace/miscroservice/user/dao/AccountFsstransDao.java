package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.po.AccountFsstransPO;

/**
 * 生利宝dao层
 */
public interface AccountFsstransDao {


    /**
     * 根据用户id获取用户生利宝
     * @param userId
     * @return
     */
    public AccountFsstransPO getFsstransByUserId(String userId);


}
