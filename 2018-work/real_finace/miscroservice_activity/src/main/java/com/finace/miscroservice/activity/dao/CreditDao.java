package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.CreditPO;

import java.util.Map;

/**
 * 金豆Dao层
 */
public interface CreditDao {


    /**
     * 新增用户金豆信息
     * @param creditPO
     * @return
     */
    public int saveCredit(CreditPO creditPO);


    /**
     * 根据用户id  获取用户的金豆信息
     * @param userId
     * @return
     */
    public CreditPO getCreditByUserId(String userId);

    /**
     * 修改用户金豆信息
     * @param userId
     * @return
     */
    public int updateCreditAddByUserId(String userId, String val);
}
