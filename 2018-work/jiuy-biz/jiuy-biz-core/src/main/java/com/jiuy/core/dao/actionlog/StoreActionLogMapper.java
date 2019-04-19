package com.jiuy.core.dao.actionlog;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.StoreActionLog;

/**
 * 门店认证信息
* @author zhaoxinglin
*/
@Repository
public class StoreActionLogMapper{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public List<StoreActionLog> getStoreActionLogList( String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime,PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("actionUserAccount", actionUserAccount);
		params.put("actionUserName", actionUserName);
		params.put("actionType", actionType);
		params.put("actionContent", actionContent);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.actionlog.StoreActionLogMapper.getStoreActionLogList",params);
	}
	public int getStoreActionLogListCount( String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("actionUserAccount", actionUserAccount);
		params.put("actionUserName", actionUserName);
		params.put("actionType", actionType);
		params.put("actionContent", actionContent);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.actionlog.StoreActionLogMapper.getStoreActionLogListCount",params);
	}
	
	public int setStoreAction(StoreBusiness storebusiness, int actionType, AdminUser adminUser) {
		String actionContent = storebusiness.getBusinessName()+"，"+storebusiness.getPhoneNumber();
		StoreActionLog storeActionLog = new StoreActionLog();
		storeActionLog.setActionUserId(adminUser.getUserId());
		storeActionLog.setActionUserName(adminUser.getUserRealName());
		storeActionLog.setActionUserAccount(adminUser.getUserName());
		storeActionLog.setStoreId(storebusiness.getId());
		storeActionLog.setActionType(actionType);
		storeActionLog.setActionContent(actionContent);
		storeActionLog.setCreateTime(System.currentTimeMillis());
		return sqlSessionTemplate.insert("com.jiuy.core.dao.actionlog.StoreActionLogMapper.setStoreAction", storeActionLog);
	}

}