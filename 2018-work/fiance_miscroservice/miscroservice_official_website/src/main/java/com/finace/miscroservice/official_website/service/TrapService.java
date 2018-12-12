package com.finace.miscroservice.official_website.service;


import com.finace.miscroservice.commons.entity.CreditGoAccount;
import com.finace.miscroservice.commons.utils.Response;

/**
 * 圈存 圈提
 */
public interface TrapService {


    /**
     * 获取账户余额
     * @param userId
     * @return
     */
    public Response getAccountBalance(String userId);

    /**
     * 圈存
     * @param phone
     * @param money
     * @param accountType  1--红包账户  2--费用账户
     * @return
     */
    public Response doTrap(String phone, String money, String accountType);

    /**
     * 圈提
     * @param phone
     * @param money
     * @param accountType 1--红包账户  2--费用账户
     * @return
     */
    public Response doRing(String phone, String money, String accountType);


    /**
     * 重置密码
     * @param account
     * @param msgCode
     * @return
     */
    String resetPass(CreditGoAccount account, String msgCode) throws Exception;












}
