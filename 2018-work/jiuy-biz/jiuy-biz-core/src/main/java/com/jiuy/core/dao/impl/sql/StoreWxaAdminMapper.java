package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.entity.store.StoreWxaCode;

/**
* 
*/
@Repository
public class StoreWxaAdminMapper{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public StoreWxa getStoreWxaByStoreId(long storeId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.getStoreWxaByStoreId",params);
	}
	/**
	 * 更改小程序发布版二维码
	 * @param storeId
	 * @param onlineWxaQrcodeUrl
	 * @return
	 */
	public int updateWxaOnlineWxaQrcodeUrl(long storeId,String onlineWxaQrcodeUrl) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("onlineWxaQrcodeUrl", onlineWxaQrcodeUrl);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.updateWxaOnlineWxaQrcodeUrl", params);
	}
	
	/**
	 * 设置商户号
	 * @param storeId
	 * @param mchId
	 */
	public int setMchId(long storeId, String mchId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("mchId", mchId);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.setMchId", params);
	}
	/**
	 * 设置商户秘钥
	 * @param storeId
	 * @param payKey
	 */
	public int setPayKey(long storeId, String payKey) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		params.put("payKey", payKey);
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.setPayKey", params);
	}


/**
	 * 取消小程序授权（解除小程序和门店的关联）
	 * @param storeId
	 */
	public void cancelAuth(long storeId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		sqlSessionTemplate.delete("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.cancelAuth", params);
	}

/**
	 * 删除小程序关联记录
	 * @param storeId
	 */
	public void delStoreWxaByStoreId(long storeId) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("storeId", storeId);
		sqlSessionTemplate.delete("com.jiuy.core.dao.impl.sql.StoreWxaAdminMapper.delStoreWxaByStoreId", params);
	}

}