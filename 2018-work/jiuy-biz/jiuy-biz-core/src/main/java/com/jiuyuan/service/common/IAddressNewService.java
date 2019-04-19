package com.jiuyuan.service.common;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.AddressNew;

public interface IAddressNewService {

	/**
	 * 根据用户ID获取对应的地址并排序封装
	 * @param userIds
	 * @return
	 */
	Map<Long, List<Address>> addressMapOfUserIdsStore(Collection<Long> userIds);
	/**
	 * 获取指定门店默认收货地址
	 * @param storeId
	 * @return
	 */
	AddressNew getDefAddress(long storeId);
	
	/**
	 * 获取收货地址
	 * @param storeId
	 * @return
	 */
	AddressNew getAddressById(long addressId);
	
	/**
	 * 获取收货地址列表
	 * @param storeId
	 * @return
	 */
	List<AddressNew> getAddressList(long storeId);
	

}