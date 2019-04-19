package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.constant.account.UserType;
import com.jiuy.core.meta.account.User;

public interface UserDao {

    public List<User> userOfIds(Collection<Long> userIds);

	public List<Map<String, Object>> loginPerDay(long startTime, long endTime);

    public List<Map<String, Object>> registerPerDay(long startTime, long endTime);

    public List<User> search();

    public List<User> excludeSearch(Collection<Long> yJJNumbers);

    public List<User> search(Collection<Long> yJJNumbers);

	public Map<Long, User> usersMapOfYJJNumbers(Collection<Long> yjjNumbers);

	public List<Map<String, Object>> uninterruptedSignIn(long startTime, long endTime, int days);

	public User searchOne(Long yJJNumber);

	public List<User> search(Collection<String> userRelatedNames, UserType userType);

	public User getByUserId(long userId);

	public int getUserCount(Long startTime, Long endTime);

	public List<Map<String, Object>> getUserCount(long startTime, long endTime, Integer minJiuCoin, Integer maxJiuCoin);

	public List<Map<String, Object>> perMonthRegister(long startTime, long endTime);
	
	public List<User> usersOfPhones(Collection<Long> phones);
	
	public int searchCount(Collection<Long> yJJNumbers);
	
	public int excludeSearchCount(Collection<Long> yJJNumbers);
	
	public List<User> searchByPhone(List<String> phones);

	public List<Map<String, Object>> getPerday();
	
	public List<Map<String, Object>> registerForTypePerDay(long startTime, long endTime, int i);

	public List<Map<String, Object>> loginDay(long startTime, long endTime);
	
	public List<User> fuzzySearchUserByYJJNumber(long yJJNumber);

}
