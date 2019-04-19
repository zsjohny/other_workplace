package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.account.UserType;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.meta.account.User;

@Service
public class UserManageService {

	@Autowired
	private UserDao userDao;

    public Map<Long, User> usersMapOfIds(Collection<Long> userIds) {
        if(userIds.size() < 1) {
        	return new HashMap<Long, User>();
        }
        
        Map<Long, User> userMap = new HashMap<Long, User>();
        List<User> users = userDao.userOfIds(userIds);

        for (User user : users) {
            userMap.put(user.getUserId(), user);
        }

        return userMap;
    }

	public List<Map<String, Object>> loginPerDay(long startTime, long endTime) {
		return userDao.loginPerDay(startTime, endTime);
	}

    public Map<String, Object> registerPerDay(long startTime, long endTime) {
        List<Map<String, Object>> list = userDao.registerPerDay(startTime, endTime);

        Map<String, Object> map = new HashMap<String, Object>();
        for (Map<String, Object> subMap : list) {
            map.put((String) subMap.get("day"), subMap.get("count"));
        }

        return map;
    }

    public List<User> search() {
        return userDao.search();
    }

    public List<User> excludeSearch(Collection<Long> yJJNumbers) {
    	if (yJJNumbers != null && yJJNumbers.size() < 1) {
			return new ArrayList<User>();
		}
        return userDao.excludeSearch(yJJNumbers);
    }

    public List<User> search(Collection<Long> yJJNumbers) {
    	if (yJJNumbers != null && yJJNumbers.size() < 1) {
			return new ArrayList<User>();
		}
        return userDao.search(yJJNumbers);
    }

	public Map<Long, User> usersMapOfYJJNumbers(Collection<Long> yJJNumbers) {
		if (yJJNumbers.size() < 1) {
			return new HashMap<Long, User>();
		}
		return userDao.usersMapOfYJJNumbers(yJJNumbers);
	}

	public List<Map<String, Object>> uninterruptedSignIn(long startTime, long endTime, int days) {
		return userDao.uninterruptedSignIn(startTime, endTime, days);
	}

	public List<User> search(Collection<String> userRelatedNames, UserType userType) {
		if (userRelatedNames.size() < 1) {
			return new ArrayList<User>();
		}
		return userDao.search(userRelatedNames, userType);
	}

	public User getByUserId(long userId) {
		return userDao.getByUserId(userId);
	}

	public int getUserCount(Long startTime, Long endTime) {
		return userDao.getUserCount(startTime, endTime);
	}

	public List<Map<String, Object>> search(long startTime, long endTime, Integer minJiuCoin, Integer maxJiuCoin) {
		return userDao.getUserCount(startTime, endTime, minJiuCoin, maxJiuCoin);
	}

	public List<User> searchByPhone(List<String> phones) {
		return userDao.searchByPhone(phones);
	}

	public Map<String, Object> registerForTypePerDay(long startTime, long endTime, int i) {
		 List<Map<String, Object>> list = userDao.registerForTypePerDay(startTime, endTime,i);

	        Map<String, Object> map = new HashMap<String, Object>();
	        for (Map<String, Object> subMap : list) {
	            map.put((String) subMap.get("day"), subMap.get("count"));
	        }

	        return map;
	}

	public Map<String, Object> loginDay(long startTime, long endTime) {
        List<Map<String, Object>> list = userDao.loginDay(startTime, endTime);

        Map<String, Object> map = new HashMap<String, Object>();
        for (Map<String, Object> subMap : list) {
            map.put((String) subMap.get("day"), subMap.get("count"));
        }

        return map;
	}
}
