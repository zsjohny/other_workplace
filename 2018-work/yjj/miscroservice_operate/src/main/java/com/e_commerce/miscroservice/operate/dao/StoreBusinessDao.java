package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/14 13:48
 * @Copyright 玖远网络
 */
public interface StoreBusinessDao {




    /**
     * 根据用户id查询账户
     * @param userId
     * @return
     */
    YjjStoreBusinessAccount findAccountByUserId(Long userId);

    /**
     * 添加新账户
     * @param userId
     */
    void addStroeBusinessAccount(Long userId);
    /**
     * 金额操作
     * @param userId
     * @param money
     * @return
     */
    void updateStoreBusinessAccountMoney(Long userId, Double money);

    /**
     * 金额操作日志添加
     * @param yjjStoreBusinessAccountLog
     */
    void saveStoreBusinessAccountLog(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);
}
