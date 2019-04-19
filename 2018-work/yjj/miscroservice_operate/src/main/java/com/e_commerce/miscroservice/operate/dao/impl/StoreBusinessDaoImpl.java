package com.e_commerce.miscroservice.operate.dao.impl;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisOperaterUtil;
import com.e_commerce.miscroservice.commons.helper.plug.mybatis.util.MybatisSqlWhereBuild;
import com.e_commerce.miscroservice.operate.dao.StoreBusinessDao;
import com.e_commerce.miscroservice.operate.mapper.StoreBusinessMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/14 13:48
 * @Copyright 玖远网络
 */
@Repository
public class StoreBusinessDaoImpl implements StoreBusinessDao {


    @Autowired
    private StoreBusinessMapper storeBusinessMapper;

    /**
     * 根据用户id查询账户
     * @param userId
     * @return
     */
    @Override
    public YjjStoreBusinessAccount findAccountByUserId(Long userId) {
        MybatisSqlWhereBuild mybatisSqlWhereBuild = new MybatisSqlWhereBuild(YjjStoreBusinessAccount.class);
        mybatisSqlWhereBuild.eq(YjjStoreBusinessAccount::getUserId,userId);
        YjjStoreBusinessAccount yjjStoreBusinessAccount = MybatisOperaterUtil.getInstance().findOne(new YjjStoreBusinessAccount(),mybatisSqlWhereBuild);
        return yjjStoreBusinessAccount;
    }
    /**
     * 添加新账户
     * @param userId
     */
    @Override
    public void addStroeBusinessAccount(Long userId) {
        YjjStoreBusinessAccount yjjStoreBusinessAccount = new YjjStoreBusinessAccount();
        yjjStoreBusinessAccount.setUserId(userId);
        MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccount);
    }
    /**
     * 金额操作
     * @param userId
     * @param money
     * @return
     */
    @Override
    public void updateStoreBusinessAccountMoney(Long userId, Double money) {
       storeBusinessMapper.updateStoreBusinessAccountMoney(userId,money);
    }

    @Override
    public void saveStoreBusinessAccountLog(YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog) {
        MybatisOperaterUtil.getInstance().save(yjjStoreBusinessAccountLog);
    }
}
