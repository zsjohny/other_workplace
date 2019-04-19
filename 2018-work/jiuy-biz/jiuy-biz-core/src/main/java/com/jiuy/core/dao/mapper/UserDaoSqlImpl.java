package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.SqlSupport;
import com.jiuy.core.meta.account.User;
import com.jiuyuan.constant.account.UserType;

public class UserDaoSqlImpl extends SqlSupport implements UserDao {

    @Override
    public List<User> userOfIds(Collection<Long> userIds) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("userIds", userIds);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.userOfIds", params);
    }

	@Override
    public List<Map<String, Object>> registerPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.registerPerDay", params);
	}

	@Override
	public List<Map<String, Object>> loginPerDay(long startTime, long endTime) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startTime", startTime);
        params.put("endTime", endTime);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.loginPerDay", params);
	}

    @Override
    public List<User> search() {
        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.search");
    }

    @Override
    public List<User> excludeSearch(Collection<Long> yJJNumbers) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("yJJNumbers", yJJNumbers);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.excludeSearch",params);
    }

    @Override
    public List<User> search(Collection<Long> yJJNumbers) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("yJJNumbers", yJJNumbers);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.search", params);
    }

	@Override
	public Map<Long, User> usersMapOfYJJNumbers(Collection<Long> yjjNumbers) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("yJJNumbers", yjjNumbers);

        return getSqlSession().selectMap("com.jiuy.core.dao.mapper.UserDaoSqlImpl.search", params, "yJJNumber");
	}

	@Override
	public List<Map<String, Object>> uninterruptedSignIn(long startTime, long endTime, int days) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("days", days);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.uninterruptedSignIn", params);
	}

	@Override
	public User searchOne(Long yJJNumber) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("yJJNumber", yJJNumber);

        return getSqlSession().selectOne("com.jiuy.core.dao.mapper.UserDaoSqlImpl.searchOne", params);
	}

	@Override
	public List<User> search(Collection<String> userRelatedNames, UserType userType) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("userRelatedNames", userRelatedNames);
		params.put("userType", userType.getIntValue());

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.search", params);
	}

	@Override
	public User getByUserId(long userId) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("userId", userId);
		return getSqlSession().selectOne("com.jiuy.core.dao.mapper.UserDaoSqlImpl.getByUserId", params);
	}

	@Override
	public int getUserCount(Long startTime, Long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return getSqlSession().selectOne("com.jiuy.core.dao.mapper.UserDaoSqlImpl.getUserCount", params);
	}

	@Override
	public List<Map<String, Object>> getUserCount(long startTime, long endTime, Integer minJiuCoin,
			Integer maxJiuCoin) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("minJiuCoin", minJiuCoin);
		params.put("maxJiuCoin", maxJiuCoin);
		
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.searchByCoin", params);
	}

	@Override
	public List<Map<String, Object>> perMonthRegister(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.perMonthRegister", params);
	}

	@Override
	public List<User> usersOfPhones(Collection<Long> phones) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("phones", phones);

        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.usersOfPhones", params);
	}

	@Override
	public int searchCount(Collection<Long> yJJNumbers) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("yJJNumbers", yJJNumbers);
        
		return getSqlSession().selectOne("com.jiuy.core.dao.mapper.UserDaoSqlImpl.searchCount",params);
	}

	@Override
	public int excludeSearchCount(Collection<Long> yJJNumbers) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("yJJNumbers", yJJNumbers);
        
		return getSqlSession().selectOne("com.jiuy.core.dao.mapper.UserDaoSqlImpl.excludeSearchCount",params);
	}
	
	@Override
	public List<User> searchByPhone(List<String> phones) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("phones", phones);
		
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.searchByPhone", params);
	}

	@Override

	public List<Map<String, Object>> registerForTypePerDay(long startTime, long endTime, int i) {
	      Map<String, Object> params = new HashMap<String, Object>();

	        params.put("startTime", startTime);
	        params.put("endTime", endTime);
	        params.put("userType", i);
	        return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.registerForTypePerDay", params);
	}

	@Override
	public List<Map<String, Object>> loginDay(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();

		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.loginDay", params);
	}

	@Override
	public List<Map<String, Object>> getPerday() {
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.getPerday");
	}

	@Override
	public List<User> fuzzySearchUserByYJJNumber(long yJJNumber) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("yJJNumber", yJJNumber);
		return getSqlSession().selectList("com.jiuy.core.dao.mapper.UserDaoSqlImpl.fuzzySearchUserByYJJNumber", params);
	}

}
