package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.AddressPO;

import java.util.List;

/**
 * 新增用户地址
 */
public interface AddressDao {

    /**
     * 新增用户地址
     * @param addressPO
     */
    public void saveAddress(AddressPO addressPO);

    /**
     * 获取用户默认地址
     * @param userId
     * @return
     */
    public AddressPO getAddressByUserId(String userId);

    /**
     * 修改用户地址
     * @param addressPO
     */
    public void updateAddress(AddressPO addressPO);


    /**
     * 根据地址id获取地址的信息
     * @param addressId
     * @return
     */
    public AddressPO getAddressById(String addressId);

    /**
     * 修改用户地址状态
     * @param userId
     * @param status
     */
    public void updateAddressStatusByUser(String userId, String status);

    /**
     * 获取用户地址列表
     * @param userId
     * @return
     */
    public List<AddressPO> getAddressListByUser(String userId);


    /**
     * 删除用户地址
     * @param addressId
     * @return
     */
    public int delAddressById(String userId,String addressId);




}
