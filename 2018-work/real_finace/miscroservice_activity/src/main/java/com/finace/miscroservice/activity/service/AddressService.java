package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.activity.po.AddressPO;
import com.finace.miscroservice.commons.utils.Response;
import java.util.List;

public interface AddressService {

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
     * 新增修改收货地址
     * @param userId
     * @param name
     * @param phone
     * @param province
     * @param city
     * @param county
     * @param address
     */
    public Response saveAndUpdateAddress(String userId, String name, String phone, String province, String city, String county, String address, String status, String addressId);


    /**
     * 获取用户地址列表
     * @param userId
     * @return
     */
    public Response getAddressListByUser(String userId, Integer page);


    /**
     * 删除用户地址
     * @param addressId
     * @return
     */
    public Response delAddressById(String userId,String addressId);

}
