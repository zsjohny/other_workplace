package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.entity.store.StoreWxaCode;
import com.xiaoleilu.hutool.date.DateUtil;

/**
* 
*/
@Repository
public class StoreWxaCodeAdminMapper{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public StoreWxaCode getByWxaId(long wxaId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("wxaId", wxaId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreWxaCodeAdminMapper.getByWxaId",params);
	}

	

	public StoreWxaCode insertWxaCode(long wxaId,String templateId, String version, String desc) {
		long testUploadTime = DateUtil.current(false);
		StoreWxaCode storeWxaCode = new StoreWxaCode();
		storeWxaCode.setWxaId(wxaId);
		storeWxaCode.setTestTemplate(templateId);
		storeWxaCode.setTestVersion(version);
		storeWxaCode.setTestDesc(desc);
		storeWxaCode.setTestUploadTime(testUploadTime);
		storeWxaCode.setCreateTime(testUploadTime);
		sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreWxaCodeAdminMapper.insertWxaCode", storeWxaCode);
		return storeWxaCode;
	}
	
	public int updateWxaCodeUploadCode(long storeWxaCodeId, String templateId, String version, String desc){
		long now = DateUtil.current(false);
		StoreWxaCode storeWxaCodeNew = new StoreWxaCode();
		storeWxaCodeNew.setId(storeWxaCodeId);
		storeWxaCodeNew.setTestVersion(version);
		storeWxaCodeNew.setTestTemplate(templateId);
		storeWxaCodeNew.setTestDesc(desc);
		storeWxaCodeNew.setTestUploadTime(now);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreWxaCodeAdminMapper.updateWxaCodeUploadCode", storeWxaCodeNew);
	}
	
	public int updateWxaCodeSubmitAudit(Long wxaId) {
		long now = DateUtil.current(false);
		StoreWxaCode storeWxaCode = getByWxaId(wxaId);
		StoreWxaCode storeWxaCodeNew = new StoreWxaCode();
		storeWxaCodeNew.setId(storeWxaCode.getId());
		storeWxaCodeNew.setAuditVersion(storeWxaCode.getTestVersion());
		storeWxaCodeNew.setAuditTemplate(storeWxaCode.getTestTemplate());
		storeWxaCodeNew.setSubmitAuditTime(now);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreWxaCodeAdminMapper.updateWxaCodeSubmitAudit", storeWxaCodeNew);
	}
}