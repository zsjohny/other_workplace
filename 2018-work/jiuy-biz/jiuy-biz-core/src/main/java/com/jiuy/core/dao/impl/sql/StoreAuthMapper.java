package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.StoreAuth;

/**
 * 门店认证信息
* @author zhaoxinglin
*/
@Repository
public class StoreAuthMapper{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public List<StoreAuth> selectAuthList(Integer authState, PageQuery pageQuery,int authType,String keyWord) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("authState", authState);
		params.put("pageQuery", pageQuery);
		params.put("authType", authType);
		params.put("keyWord", keyWord);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreAuthMapper.selectAuthList",params);
	}
	
	public int selectAuthListCount(Integer authState,int authType,String keyWord) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("authState", authState);
		params.put("authType", authType);
		params.put("keyWord", keyWord);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreAuthMapper.selectAuthListCount",params);
	}
	
	public int setAuthPass(long storeAuthId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeAuthId", storeAuthId);
		params.put("authState", StoreAuth.auth_state_audit_pass);
		params.put("time", System.currentTimeMillis());
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreAuthMapper.setAuthPass",params);
	}
	
	public int setAuthNoPass(long storeAuthId,String noPassReason) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeAuthId", storeAuthId);
		params.put("authState", StoreAuth.auth_state_audit_no_pass);
		params.put("noPassReason", noPassReason);
		params.put("time", System.currentTimeMillis());
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreAuthMapper.setAuthPass",params);
	}
	
	
	public List<Map<String,String>> getPreinstallNoPassReasonList(PageQuery pageQuery,int type) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("type", type);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreAuthMapper.getPreinstallNoPassReasonList",params);
	}
	
	/**
	 * 添加预设拒绝理由
	 * @param authReason
	 * @return
	 */
	public int setPreinstallNoPassReason(String reason,int type) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("type", type);
		params.put("reason", reason);
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreAuthMapper.setPreinstallNoPassReason",params);
	}
	/**
	 * 删除预设理由
	 * @param authReasonId
	 * @return
	 */
	public int delPreinstallNoPassReason(long authReasonId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("authReasonId", authReasonId);
		return sqlSessionTemplate.delete("com.jiuy.core.dao.impl.sql.StoreAuthMapper.delPreinstallNoPassReason",params);
	}

	/**
	 * 获取预设拒绝原因列表数量
	 * @return
	 */
	public int getPreinstallNoPassReasonListCount() {
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreAuthMapper.getPreinstallNoPassReasonListCount");
	}
	
}