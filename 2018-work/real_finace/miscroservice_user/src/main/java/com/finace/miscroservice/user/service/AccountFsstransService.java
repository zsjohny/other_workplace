package com.finace.miscroservice.user.service;

import com.finace.miscroservice.user.po.AccountFsstransPO;

/**
 *生利宝service
 */
public interface AccountFsstransService {


    /**
     * 根据用户id获取用户生利宝
     * @param userId
     * @return
     */
    public AccountFsstransPO getFsstransByUserId(String userId);



}
