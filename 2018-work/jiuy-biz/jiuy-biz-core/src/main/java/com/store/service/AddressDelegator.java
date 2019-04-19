/**
 * 
 */
package com.store.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.service.shop.UserAddressService;

/**
 * @author LWS
 *
 */
@Service
public class AddressDelegator {
    
    @Autowired
    private UserAddressService userAddressService;
    
    public Map<String, Object> list(UserDetail userDetail) {
        long storeId = userDetail.getId();
        List<Address> addresses = userAddressService.getUserAddresses(storeId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("addresses", addresses);
        return data;
    }

    public Map<String,Object> add(UserDetail userDetail, Address address) {
        // 获取用户当前已有地址信息
        long userId = userDetail.getId();
        List<Address> addresses = userAddressService.getUserAddresses(userId);
        if(null == addresses || addresses.size() == 0){
            // 当前增加第一次地址，直接设置为默认地址
            address.setIsDefault(true);
        }
        userAddressService.addUserAddress(userId, address);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);
        return data;
    }

    public Map<String, Object> update(UserDetail userDetail, Address address) {
        long userId = userDetail.getId();
        userAddressService.updateUserAddress(userId, address);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);
        return data;
    }

    public int remove(long addressId, UserDetail userDetail) {
        return userAddressService.removeAddress(userDetail.getId(), addressId);
    }

    public Map<String, Object> getAddress(long addressId, UserDetail userDetail) {
        Address address = userAddressService.getUserAddress(userDetail.getId(), addressId);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("address", address);
        return data;
    }
    
//    public Map<String, Object> getProvinceList() {
//    	List<AreaProvince> provinceList = userAddressService.getAreaProvinceList();
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	data.put("provinceList", provinceList);
//    	return data;
//    }
//    public Map<String, Object> getCityList(long provinceCode) {
//    	List<AreaCity> cityList = userAddressService.getAreaCityList(provinceCode);
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	data.put("cityList", cityList);
//    	return data;
//    }
//    public Map<String, Object> getDistrictList(long cityCode) {
//    	List<AreaDistrict> distrcictList = userAddressService.getAreaDistrictList(cityCode);
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	data.put("distrcictList", distrcictList);
//    	return data;
//    }

    public void setDefaultAddress(long addressId, UserDetail userDetail) {
        userAddressService.setDefaultAddress(userDetail.getId(), addressId);
    }
}
