/**
 * 
 */
package com.yujj.web.controller.delegate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.account.Address;
import com.yujj.business.service.YJJUserAddressService;
import com.yujj.entity.account.UserDetail;
//import com.yujj.entity.account.Address;

/**
 * @author LWS
 *
 */
@Service
public class AddressDelegator {
    
    @Autowired
    private YJJUserAddressService userAddressService;
    
    public Map<String, Object> list(UserDetail userDetail) {
        long userId = userDetail.getUserId();
        List<Address> addresses = userAddressService.getUserAddresses(userId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("addresses", addresses);
        return data;
    }

    public Map<String,Object> add(UserDetail userDetail, Address address) {
        // 获取用户当前已有地址信息
        long userId = userDetail.getUserId();
        List<Address> addresses = userAddressService.getUserAddresses(userId);
        if(null == addresses || addresses.size() == 0){
            // 当前增加第一次地址，直接设置为默认地址
            address.setIsDefault(true);
        }
        Map<String, Object> data = new HashMap<String, Object>();
      
        userAddressService.addUserAddress(userId, address);
        data.put("address", address);
        
        
        
        return data;
    }

    public Map<String, Object> update(UserDetail userDetail, Address address) {
        long userId = userDetail.getUserId();
        Map<String, Object> data = new HashMap<String, Object>();
       
        userAddressService.updateUserAddress(userId, address);
        data.put("address", address);
        return data;
    }

    public int remove(long addressId, UserDetail userDetail) {
        return userAddressService.removeAddress(userDetail.getUserId(), addressId);
    }

    public Map<String, Object> getAddress(long addressId, UserDetail userDetail) {
        Address address = userAddressService.getUserAddress(userDetail.getUserId(), addressId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);
        return data;
    }

    public void setDefaultAddress(long addressId, UserDetail userDetail) {
        userAddressService.setDefaultAddress(userDetail.getUserId(), addressId);
    }
    
    
}
