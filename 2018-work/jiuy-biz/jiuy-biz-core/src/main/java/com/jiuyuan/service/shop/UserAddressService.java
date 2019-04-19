package com.jiuyuan.service.shop;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.dao.mapper.shop.AddressMapper;
import com.jiuyuan.entity.account.Address;

@Service
public class UserAddressService {

	@Autowired
	private AddressMapper addrMapper;

	public List<Address> getUserAddresses(long userId) {
        return addrMapper.loadAddressList(userId);
	}
	
	public Address getUserAddress(long userId, long addressId) {
        return addrMapper.getUserAddress(userId, addressId);
    }
	
	@Transactional(rollbackFor = Exception.class)
    public int addUserAddress(long userId, Address address) {
        if (address.getIsDefault()) {
            addrMapper.removeDefaultAddress(userId);
        }
        address.setUserId(userId);
        address.setStatus(0);
        long time = System.currentTimeMillis();
        address.setCreateTime(time);
        address.setUpdateTime(time);
        return addrMapper.addAddress(address);
    }
	
	 @Transactional(rollbackFor = Exception.class)
	 public int updateUserAddress(long userId, Address address) {
		 long time = System.currentTimeMillis();
		 address.setUpdateTime(time);
//	     if (address.isDefault()) {
//	    	 addrMapper.removeDefaultAddress(userId);
//	     }
	        
	     //更新时,保留原数据
	     Address addr = getUserAddress(userId, address.getAddrId());
	     addr.setStatus(-1);
	     addr.setIsDefault(false);
	     addr.setStoreId(userId);
	     addrMapper.addAddress(addr);
	     
	     return addrMapper.updateAddress(userId, address);
	 }
	 
	 public int removeAddress(long userId, long addressId) {
	     return addrMapper.removeAddress(userId, addressId);
	 }

	 @Transactional(rollbackFor = Exception.class)
	 public void setDefaultAddress(long userId, long addressId) {
	     addrMapper.removeDefaultAddress(userId);
	     addrMapper.setDefaultAddress(userId, addressId);
	 }

//	 public List<AreaProvince> getAreaProvinceList() {
//		 return addrMapper.getAreaProvinceList();
//	 }
	 
//	 public List<AreaCity> getAreaCityList(@Param("provinceCode") long provinceCode){
//		 return addrMapper.getAreaCityList(provinceCode);
//	 }
//	    
//	 public List<AreaDistrict> getAreaDistrictList(@Param("cityCode") long cityCode){
//		 return addrMapper.getAreaDistrictList(cityCode);
//	 }	    

}
