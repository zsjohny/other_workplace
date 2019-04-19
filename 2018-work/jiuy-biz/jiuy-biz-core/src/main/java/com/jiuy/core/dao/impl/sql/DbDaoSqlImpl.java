package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.dao.DbDao;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.admin.AdminUser;

@Repository
public class DbDaoSqlImpl implements DbDao {

	@Autowired
	private SqlSessionTemplate SqlSessionTemplate;
	
	
	
	
	
	
	
	
	@Override
	public List<Map<String, Long>> getUserInfo( ) {
		Map<String, Object> param = new HashMap<>();
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getUserInfo", param);
	}
	
	
	
	
	
	
	@Override
	public List<Map<String, Object>> getHistoryList(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getHistoryList", param);
	}
	public void updHistoryUserId(long historyId, long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("historyId", historyId);
		param.put("userId", userId);
		SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.updHistoryUserId", param);
	}
	
	
	
	@Override
	public List<Map<String, Object>> getSignList(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getSignList", param);
	}
	public void updSignUserId(long signId, long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("signId", signId);
		param.put("userId", userId);
		SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.updSignUserId", param);
	}
	public void updShoppingCartId(long shoppingCartId, long userId){
		Map<String, Object> param = new HashMap<>();
		param.put("shoppingCartId", shoppingCartId);
		param.put("userId", userId);
		SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.updShoppingCartId", param);
	}
	@Override
	public List<Map<String, Object>> getShoppingCartList(long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getShoppingCartList", param);
	}
	
	public void updUserLikeId(long userLikeId, long userId){
		Map<String, Object> param = new HashMap<>();
		param.put("userLikeId", userLikeId);
		param.put("userId", userId);
		SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.updUserLikeId", param);
	}
	@Override
	public List<Map<String, Object>> getUserLikeList(long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getUserLikeList", param);
	}
	
	
	public void updLogId(long logId, long count){
		Map<String, Object> param = new HashMap<>();
		param.put("logId", logId);
		param.put("count", count);
		SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.updLogId", param);
	}
	@Override
	public List<Map<String, Object>> getLogList(long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.DbDaoSqlImpl.getLogList", param);
	}
	
	
	//    
//    public AdminUser getByProperty(AdminUser user) {
//    	Map<String, Object> params = new HashMap<>();
//    	
//    	params.put("userName", user.getUserName());
//    	params.put("userPassword", user.getUserPassword());    
//    	
//      return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", params);
//    }
//
//    @Override
//    public AdminUser getUser(long userId) {
//    	Map<String, Object> params = new HashMap<>();
//    	
//    	params.put("userId", userId);
//    	
//    	return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", params);
//    }
//
//    @Override
//    public int updateUserPassword(long userId, String oldPassword, String newPassword) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("userId", userId);
//		params.put("oldPassword", oldPassword);
//		params.put("newPassword", newPassword);
//		
//		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.resetPassword", params);
//    }
//
//	@Override
//	public int getRegisterNum() {
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getRegisterNum");
//	}
//	
//	@Override
//	public int getPhoneRegisterNumByTime(long startTime, long endTime) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("startTime", startTime);
//		map.put("endTime", endTime);
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getPhoneRegisterNumByTime", map);
//	}
//	
//	@Override
//	public int getWeixinUserNumByTime(long startTime, long endTime) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("startTime", startTime);
//		map.put("endTime", endTime);
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getWeixinUserNumByTime", map);
//	}
//
//	@Override
//	public int registerCount(long startTime, long endTime) {
//		Map<String, Object> map = new HashMap<>();
//		map.put("startTime", startTime);
//		map.put("endTime", endTime);
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.registerCount", map);
//	}
//
//    @Override
//    public List<AdminUser> search(AdminUser adminUser, PageQuery query) {
//    	Map<String, Object> param = new HashMap<>();
//		
//		param.put("adminUser", adminUser);
//		param.put("pageQuery", query);
//		
//        return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search", param);
//    }
//    
//    @Override
//    public int searchCount(AdminUser adminUser) {
//    	Map<String, Object> param = new HashMap<>();
//		
//		param.put("adminUser", adminUser);
//		
//    	return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchCount", param);
//    }
//    
//	@Override
//	public int add(AdminUser adminUser) {
//		return SqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.add", adminUser);
//	}
//
//	@Override
//	public int remove(long userId) {
//		Map<String, Object> param = new HashMap<>();
//		
//		param.put("userId", userId);
//		param.put("status", -1);
//		
//		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", param);
//	}
//
//	@Override
//	public int resetPassword(long userId, String password) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("userId", userId);
//		params.put("password", password);
//		
//		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", params);
//	}
//
//	@Override
//	public List<AdminUser> search() {
//		return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.search");
//	}
//
//	@Override
//	public List<Map<String, Object>> getCountOfRole() {
//		// TODO Auto-generated method stub
//		return SqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getCountOfRole");
//	}
//
//	@Override
//	public int update(long userId, long roleId, String userPhone) {
//		// TODO Auto-generated method stub
//		Map<String, Object> params = new HashMap<String, Object>();
//		
//		params.put("userId", userId);
//		params.put("roleId", roleId);
//		params.put("userPhone", userPhone);
//		
//		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.update", params);
//	}
//
//	@Override
//	public long searchStoreId(long serviceId) {
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchStoreId", serviceId);
//	}
//
//	@Override
//	public int searchCountStoreId(long serviceId) {
//		// TODO Auto-generated method stub
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.searchCountStoreId", serviceId);
//	}
//
//	@Override
//	public int updateHttpUrl(Map<String, String> params) {
//		return SqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.updateHttpUrl", params);
//	}
//	
//	@Override
//	public User getByYjjNumber(Long yjjNumber) {
//		// TODO Auto-generated method stub
//		return SqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.AdminUserDaoSqlImpl.getByYjjNumber", yjjNumber);
//	}
}
