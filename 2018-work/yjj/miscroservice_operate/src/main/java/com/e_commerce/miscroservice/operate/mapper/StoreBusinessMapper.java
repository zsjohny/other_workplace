package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.application.user.StoreBusinessVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/11/9 15:15
 * @Copyright 玖远网络
 */
@Mapper
public interface StoreBusinessMapper{

    /**
     * 根据主键查
     */
    StoreBusinessVo selectByPrimaryKey(Long storeId);


    /**
     * 金额操作
     * @param userId
     * @param money
     */
    void updateStoreBusinessAccountMoney(@Param("userId") Long userId, @Param("money") Double money);
}
