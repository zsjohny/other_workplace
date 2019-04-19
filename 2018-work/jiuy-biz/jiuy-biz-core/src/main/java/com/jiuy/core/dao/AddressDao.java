package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.account.Address;


public interface AddressDao {

    List<Address> srchAddress(long userId, String expressInfo);

    List<Address> AddressOfUserIds(Collection<Long> userIds);
    
    List<Address> AddressOfUserIdsStore(Collection<Long> userIds);

	List<Address> searchByUserId(long userId);

}
