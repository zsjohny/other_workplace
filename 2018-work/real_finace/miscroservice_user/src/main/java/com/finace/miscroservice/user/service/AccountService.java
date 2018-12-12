package com.finace.miscroservice.user.service;

import com.finace.miscroservice.user.po.AccountCashPO;
import com.finace.miscroservice.user.po.AccountLogPO;
import com.finace.miscroservice.user.po.AccountPO;

import java.util.List;
import java.util.Map;

/**
 * 账户service层接口
 */
public interface AccountService {

    /**
     * 根据用户id获取账户信息
     * @param userId
     * @return
     */
    public AccountPO getAccountByUserId(String userId);

    /**
     * 获取资金明细
     * @param map
     * @return
     */
    public List<AccountLogPO> getZjmxByUserId(Map<String, Object> map, int page);


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
     * @param ac
     * @param alog
     * @param type
     * @return
     */
    public int cashing(AccountCashPO ac, AccountLogPO alog, int type);

    /**
     *
     * @param accountLogPO
     * @return
     */
    public int addAccountLog(AccountLogPO accountLogPO);


}
