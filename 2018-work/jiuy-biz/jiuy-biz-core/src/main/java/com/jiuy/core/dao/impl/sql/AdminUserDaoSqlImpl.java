package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.query.PageQuery;

@Repository
public class AdminUserDaoSqlImpl implements AdminUserDao {

	@Autowired
	private SqlSessionTemplate SqlSessionTemplate;
	
    @Override
    public AdminUser getByProperty(AdminUser user) {
    	Map<String, Object> params = new HashMap<>();
    	
    	params.put("userName", user.getUserName());
    	params.put("userPassword", user.getUserPassword());    
    	
        return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", params);
    }

    @Override
    public AdminUser getUser(long userId) {
    	Map<String, Object> params = new HashMap<>();
    	
    	params.put("userId", userId);
    	
    	return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", params);
    }

    @Override
    public int updateUserPassword(long userId, String oldPassword, String newPassword) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("userId", userId);
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		
		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.resetPassword", params);
    }

	@Override
	public int getRegisterNum() {
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getRegisterNum");
	}
	
	@Override
	public int getPhoneRegisterNumByTime(long startTime, long endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getPhoneRegisterNumByTime", map);
	}
	
	@Override
	public int getWeixinUserNumByTime(long startTime, long endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getWeixinUserNumByTime", map);
	}

	@Override
	public int registerCount(long startTime, long endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("startTime", startTime);
		map.put("endTime", endTime);
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.registerCount", map);
	}

    @Override
    public List<AdminUser> search(AdminUser adminUser, PageQuery query) {
    	Map<String, Object> param = new HashMap<>();
		
		param.put("adminUser", adminUser);
		param.put("pageQuery", query);
		
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", param);
    }
    
    @Override
    public int searchCount(AdminUser adminUser) {
    	Map<String, Object> param = new HashMap<>();
		
		param.put("adminUser", adminUser);
		
    	return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchCount", param);
    }
    
	@Override
	public int add(AdminUser adminUser) {
		return SqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.add", adminUser);
	}

	@Override
	public int remove(long userId) {
		Map<String, Object> param = new HashMap<>();
		
		param.put("userId", userId);
		param.put("status", -1);
		
		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", param);
	}

	@Override
	public int resetPassword(long userId, String password) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("userId", userId);
		params.put("password", password);
		
		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", params);
	}

	@Override
	public List<AdminUser> search() {
		return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search");
	}

	@Override
	public List<Map<String, Object>> getCountOfRole() {
		return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getCountOfRole");
	}

	@Override
	public int update(long userId, long roleId, String userPhone) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("userPhone", userPhone);
		
		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", params);
	}

	@Override
	public long searchStoreId(long serviceId) {
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchStoreId", serviceId);
	}

	@Override
	public int searchCountStoreId(long serviceId) {
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchCountStoreId", serviceId);
	}

	@Override
	public int updateHttpUrl(Map<String, String> params) {
		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.updateHttpUrl", params);
	}
	
	@Override
	public User getByYjjNumber(Long yjjNumber) {
		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getByYjjNumber", yjjNumber);
	}

	@Override
	public long getYjjNumberById(long userId) {
		User user = SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getYjjNumberById", userId);
		if(user != null ){
			return user.getyJJNumber();
			
		}else{
			return 0;
		}
	}
}
