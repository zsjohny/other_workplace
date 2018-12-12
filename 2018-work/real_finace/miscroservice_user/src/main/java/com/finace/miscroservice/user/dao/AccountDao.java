package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.po.AccountPO;

/**
 * 账户dao层接口
 */
public interface AccountDao {

    /**
     * 根据用户id获取账户信息
     * @param userId
     * @return
     */
    public AccountPO getAccountByUserId(String userId);

    /**
     *
     * @param accountPO
     * @return
     */
    int updateAccount(AccountPO accountPO);



}
