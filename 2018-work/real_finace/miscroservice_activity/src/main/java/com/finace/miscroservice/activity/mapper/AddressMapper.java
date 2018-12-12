package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.AddressPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Mapper
public interface AddressMapper {


    /**
     * 新增用户地址
     * @param addressPO
     */
    void saveAddress(AddressPO addressPO);

    /**
     * 获取用户默认地址
     * @param userId
     * @return
     */
    AddressPO getAddressByUserId(@Param("userId") String userId);

    /**
     * 修改用户地址
     * @param addressPO
     */
    void updateAddress(AddressPO addressPO);

    /**
     * 根据地址id获取地址的信息
     * @param addressId
     * @return
     */
    AddressPO getAddressById(String addressId);

    /**
     * 修改用户地址状态
     * @param map
     */
    void updateAddressStatusByUser(Map<String, String> map);

    /**
     * 获取用户地址列表
     * @param userId
     * @return
     */
    List<AddressPO> getAddressListByUser(@Param("userId") String userId);

    /**
     * 删除用户地址
     * @param map
     * @return
     */
    int delAddressById(Map<String, String> map);


}
