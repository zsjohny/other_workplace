package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.AddressDao;
import com.finace.miscroservice.activity.mapper.AddressMapper;
import com.finace.miscroservice.activity.po.AddressPO;
import com.finace.miscroservice.commons.entity.BasePage;
import com.finace.miscroservice.commons.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Component
public class AddressDaoImpl implements AddressDao {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public void saveAddress(AddressPO addressPO) {
          this.addressMapper.saveAddress(addressPO);
    }


    @Override
    public AddressPO getAddressByUserId(String userId) {
        return addressMapper.getAddressByUserId(userId);
    }

    @Override
    public void updateAddress(AddressPO addressPO) {
        addressMapper.updateAddress(addressPO);
    }

    @Override
    public AddressPO getAddressById(String addressId) {
        return addressMapper.getAddressById(addressId);
    }

    @Override
    public void updateAddressStatusByUser(String userId, String status) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("status", status);
        addressMapper.updateAddressStatusByUser(map);
    }

    @Override
    public List<AddressPO> getAddressListByUser(String userId) {
        return addressMapper.getAddressListByUser(userId);
    }

    @Override
    public int delAddressById(String userId,String addressId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("addressId", addressId);
        return addressMapper.delAddressById(map);
    }
}
