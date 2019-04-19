package com.jiuy.rb.service.user;


import com.jiuy.base.model.MyLog;
import com.jiuy.base.model.UserSession;
import com.jiuy.rb.model.user.*;

import java.util.List;

/**
 * 用户相关模块
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 16:04
 * @Copyright 玖远网络
 */
public interface IUserService{

    /**
     * 通过id获取用户信息
     *
     * @param storeId storeId
     * @return StoreBusinessRb
     * @author Aison
     * @date 2018/6/28 16:09
     */
    StoreBusinessRb getStoreBusinessById(Long storeId);


    StoreBusinessRb getStoreBusinessByIdNumber(Long storeId,String number);

    /**
     * 获取供应商
     *
     * @param supplierId supplierId
     * @return com.jiuy.rb.model.user.SupplierUserRb
     * @author Aison
     * @date 2018/6/29 11:32
     */
    SupplierUserRb getSupplierUser(Integer supplierId);


    /**
     * 获取某个供应商的客户信息
     *
     * @param query query
     * @return SupplierCustomerRb
     * @author Aison
     * @date 2018/6/28 16:14
     */
    SupplierCustomerRb getSupplierCustomer(SupplierCustomerRbQuery query);


    /**
     * 查询一个供应商的客户分组列表
     *
     * @param suppierId suppierId
     * @param orderNo   orderNo
     * @return java.util.List<com.jiuy.rb.model.user.SupplierCustomerGroupRbQuery>
     * @author Aison
     * @date 2018/6/29 15:17
     */
    List<SupplierCustomerGroupRbQuery> groupList(Long suppierId, Long orderNo);


    /**
     * 更新门店用户相对于供应商的用户分组
     *
     * @param query       query
     * @param userSession userSession
     * @return com.jiuy.base.model.MyLog<java.lang.Long>
     * @author Aison
     * @date 2018/6/29 15:38
     */
    MyLog<Long> updateStoreGroup(SupplierCustomerRbQuery query, UserSession userSession);

    /**
     * 批量查询用户
     *
     * @param query 查询参数封装
     * @return java.util.List<com.jiuy.rb.model.user.StoreBusinessRb>
     * @author Charlie(唐静)
     * @date 2018/7/3 10:01
     */
    List<StoreBusinessRb> listStoreBusiness(StoreBusinessRbQuery query);


    /**
     * 获取 手机号
     * @param storeId
     * @return
     */
    String getStoreBusinessPhoneById(Long storeId);
}
