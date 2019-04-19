package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.entity.account.Address;
import com.yujj.dao.mapper.YJJAddressMapper;
//import com.yujj.entity.account.Address;

@Service
public class YJJUserAddressService {

	@Autowired
	private YJJAddressMapper addrMapper;

	public List<Address> getUserAddresses(long userId) {
		List<Address> addrList = addrMapper.loadAddressList(userId);
		for (Address address : addrList) {
			if(address.getDistrictName()==null){
				address.setDistrictName("");
			}
		}
		return addrList;
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
        if(address.getDistrictName()==null){
        	address.setDistrictName("");
        }
        return addrMapper.addAddress(address);
    }

    @Transactional(rollbackFor = Exception.class)
    public int updateUserAddress(long userId, Address address) {
        long time = System.currentTimeMillis();
        address.setUpdateTime(time);
        //boolean flag = true;
//        if (address.isDefault()) {
//        	flag = false;
//            addrMapper.removeDefaultAddress(userId);
//        }
        //更新时,保留原数据
        Address addr = getUserAddress(userId, address.getAddrId());
        addr.setStatus(-1);
        addr.setIsDefault(false);
        addrMapper.addAddress(addr);
        
        if(address.getDistrictName()==null){
        	address.setDistrictName("");
        }
        return addrMapper.updateAddress(userId, address);
    }

    public Address getUserAddress(long userId, long addressId) {
        return addrMapper.getUserAddress(userId, addressId);
    }

    public int removeAddress(long userId, long addressId) {
        return addrMapper.removeAddress(userId, addressId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void setDefaultAddress(long userId, long addressId) {
        addrMapper.removeDefaultAddress(userId);
        addrMapper.setDefaultAddress(userId, addressId);
    }

}
