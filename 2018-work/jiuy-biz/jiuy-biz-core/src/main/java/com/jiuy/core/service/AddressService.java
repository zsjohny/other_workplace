package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.account.Address;


public interface AddressService {

    List<Address> srchAddress(long userId, String expressInfo);

	Map<Long, List<Address>> addressMapOfUserIds(Collection<Long> userIds);
	
	Map<Long, List<Address>> addressMapOfUserIdsStore(Collection<Long> userIds);

	List<Address> searchByUserId(long userId);


}
