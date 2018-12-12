package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.po.AccountCashPO;

public interface AccountCashDao {


    /**
     *
     * @param id
     * @return
     */
    public AccountCashPO getAccountCashById(int id);

    /**
     *
     * @param accountCashPO
     */
    public int  addAccountCash(AccountCashPO accountCashPO);

    /**
     *
     * @param accountCashPO
     */
    public int updateAccountCash(AccountCashPO accountCashPO);


}
