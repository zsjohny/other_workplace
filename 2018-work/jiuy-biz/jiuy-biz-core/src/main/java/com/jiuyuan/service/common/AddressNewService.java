package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.shop.AddressMapper;
import com.jiuyuan.dao.mapper.supplier.AddressNewMapper;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.AddressNew;
import com.jiuyuan.entity.newentity.ProductSkuNew;

@Service
public class AddressNewService implements IAddressNewService {

	@Autowired
	private AddressMapper addrMapper;
	
	@Autowired
	private AddressNewMapper addressNewMapper;
	
	/**
	 * 获取指定门店默认收货地址
	 * @param storeId
	 * @return
	 */
	public AddressNew getDefAddress(long storeId){
		Wrapper<AddressNew> wrapper = new EntityWrapper<AddressNew>();
		wrapper.eq("StoreId",storeId);//
		wrapper.ge("Status",0);
		wrapper.ge("IsDefault",1);
		List<AddressNew> list = addressNewMapper.selectList(wrapper);
		if(list.size() > 0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 * 获取收货地址
	 * @param storeId
	 * @return
	 */
	public AddressNew getAddressById(long addressId){
		return addressNewMapper.selectById(addressId);
	}
	
	/**
	 * 获取收货地址列表
	 * @param storeId
	 * @return
	 */
	public List<AddressNew> getAddressList(long storeId){
		Wrapper<AddressNew> wrapper = new EntityWrapper<AddressNew>();
		wrapper.eq("StoreId",storeId);//
		wrapper.ge("Status",0);
		List<AddressNew> list = addressNewMapper.selectList(wrapper);
		return list;
	}
	

	@Override
	public Map<Long, List<Address>> addressMapOfUserIdsStore(Collection<Long> userIds) {
		Map<Long, List<Address>> addressMap = new HashMap<Long, List<Address>>();
		
		if(userIds.size() < 1) {
			return new HashMap<Long, List<Address>>();
		}
		List<Address> addrs = addrMapper.AddressOfUserIdsStore(userIds);
//		Collections.sort(addrs, 
//				new Comparator<Address>() {
//			public int compare(Address o1, Address o2) {
//				return (int) (o2.getUserId() - o1.getUserId());
//			}
//		});
		Collections.sort(addrs, (Address a, Address b) -> {
			return (int) (b.getStoreId() - a.getStoreId());
		});
		
		if(addrs.size() < 1) {
			return new HashMap<Long, List<Address>>();
		}
		
		long lastUserId = 0;
		List<Address> addresses = null;
		for(Address addr : addrs) {
			if(lastUserId != addr.getStoreId()) {
				addresses = new ArrayList<Address>();
				lastUserId = addr.getStoreId();
				addressMap.put(lastUserId, addresses);
			}
			addresses.add(addr);
		}
		
		return addressMap;
	}
	
}