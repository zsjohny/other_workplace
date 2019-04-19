package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.AddressDao;
import com.jiuyuan.entity.account.Address;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressDao addressDao;

    @Override
    public List<Address> srchAddress(long userId, String expressInfo) {
        return addressDao.srchAddress(userId, expressInfo);
    }

	@Override
	public Map<Long, List<Address>> addressMapOfUserIds(Collection<Long> userIds) {
		Map<Long, List<Address>> addressMap = new HashMap<Long, List<Address>>();
		
		if(userIds.size() < 1) {
			return new HashMap<Long, List<Address>>();
		}
		List<Address> addrs = addressDao.AddressOfUserIds(userIds);
//		Collections.sort(addrs, 
//				new Comparator<Address>() {
//			public int compare(Address o1, Address o2) {
//				return (int) (o2.getUserId() - o1.getUserId());
//			}
//		});
		Collections.sort(addrs, (Address a, Address b) -> {
		    return (int) (b.getUserId() - a.getUserId());
		});
		
		if(addrs.size() < 1) {
			return new HashMap<Long, List<Address>>();
		}
		
		long lastUserId = 0;
		List<Address> addresses = null;
		for(Address addr : addrs) {
			if(lastUserId != addr.getUserId()) {
				addresses = new ArrayList<Address>();
				lastUserId = addr.getUserId();
				addressMap.put(lastUserId, addresses);
			}
			addresses.add(addr);
		}
		
		return addressMap;
	}
	
	@Override
	public Map<Long, List<Address>> addressMapOfUserIdsStore(Collection<Long> userIds) {
		Map<Long, List<Address>> addressMap = new HashMap<Long, List<Address>>();
		
		if(userIds.size() < 1) {
			return new HashMap<Long, List<Address>>();
		}
		List<Address> addrs = addressDao.AddressOfUserIdsStore(userIds);
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

	@Override
	public List<Address> searchByUserId(long userId) {
		return addressDao.searchByUserId(userId);
	}

}
