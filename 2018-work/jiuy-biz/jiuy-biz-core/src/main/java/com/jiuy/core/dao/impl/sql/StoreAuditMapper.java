package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSON;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.StoreAudit;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author qiuyuefan
*/
@Repository
public class StoreAuditMapper{

	private static final Logger logger = Logger.getLogger(StoreAuditMapper.class);
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public List<StoreAudit> selectAuditList(Integer status, PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		
		params.put("status", status);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreAuditMapper.selectAuditList",params);
	}

	public Integer updateAuditStatus(Long id, Integer status,  AdminUser userinfo, String refuseReason) {
		long time = System.currentTimeMillis();
		HashMap<String, Object> params = new HashMap<String,Object>(); 

		params.put("id", id);
		params.put("status", status);
		params.put("updateTime", time);
		params.put("auditPerson", userinfo.getUserName());
		params.put("auditTime", time);
		params.put("auditId", userinfo.getUserId());
		params.put("refuseReason", refuseReason);
		logger.info("修改新门店审核信息，params："+JSON.toJSONString(params));
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreAuditMapper.updateAuditStatus",params);
	}

	public int selectAuditCount(Integer status, Integer isVip, String keyWord, PageQuery pageQuery, 
			long startTime, long endTime, String phoneNumber, String userName, long storeBusinessId, 
			String storeBusinessName, String storeBusinessAddress, String referenceNumber) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 

		params.put("status", status);
		params.put("vip", isVip);
		params.put("keyWord", keyWord);
		params.put("pageQuery", pageQuery);
		params.put("registTimeStart", startTime);
		params.put("registTimeEnd", endTime);
		params.put("phoneNumber", phoneNumber);
		params.put("userName", userName);
		params.put("storeBusinessId", storeBusinessId);
//		params.put("storeType", storeType);
		params.put("storeBusinessName", storeBusinessName);
		params.put("storeBusinessAddress", storeBusinessAddress);
		params.put("referenceNumber", referenceNumber);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreAuditMapper.selectAuditCount",params);
	}

	public StoreAudit selectAuditById(Long id) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 

		params.put("id", id);
//		params.put("status", status);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreAuditMapper.selectAuditById",params);
	}

	public List<StoreAudit> selectAuditByStoreId(long storeId, Integer status) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 

		params.put("storeId", storeId);
		params.put("status", status);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreAuditMapper.selectAuditByStoreId",params);
	}

}