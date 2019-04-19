package com.e_commerce.miscroservice.store.dao;


import com.e_commerce.miscroservice.store.entity.vo.SupplierUser;

/**
 * 用户
 */
public interface SupplierUserDao {


    /**
     * 查询用户
     * @param supplierId
     * @return
     */
    SupplierUser getSupplierUserInfo(long supplierId);
}
