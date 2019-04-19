package com.e_commerce.miscroservice.store.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccount;
import com.e_commerce.miscroservice.commons.entity.application.user.YjjStoreBusinessAccountLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface cashJournelAccountMapper {
    /**
     * 添加流水
     * @param yjjStoreBusinessAccountLog
     */
    void insertMyCapitalLog(@Param("yjjStoreBusinessAccountLog") YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog);

    /**
     * 更改账户金额
     * @param yjjStoreBusinessAccount
     */
    void updateMyAccount(@Param("yjjStoreBusinessAccount") YjjStoreBusinessAccount yjjStoreBusinessAccount);
}
