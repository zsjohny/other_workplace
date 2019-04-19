package com.jiuy.core.dao.actionlog;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.ProductActionLog;

/**
* @author zhaoxinglin
*/
@Repository
public class ProductActionLogMapper{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	public List<ProductActionLog> getProductActionLogList( String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime,PageQuery pageQuery) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("actionUserAccount", actionUserAccount);
		params.put("actionUserName", actionUserName);
		params.put("actionType", actionType);
		params.put("actionContent", actionContent);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.actionlog.ProductActionLogMapper.getProductActionLogList",params);
	}
	
	public int getProductActionLogListCount(String actionUserAccount, String actionUserName, String actionType,
			String actionContent, long startTime, long endTime) {
		HashMap<String, Object> params = new HashMap<String,Object>(); 
		params.put("actionUserAccount", actionUserAccount);
		params.put("actionUserName", actionUserName);
		params.put("actionType", actionType);
		params.put("actionContent", actionContent);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.actionlog.ProductActionLogMapper.getProductActionLogListCount",params);
	}


	/**
	 * 
	 * @param productId
	 * @param actionType  操作类型:0取消商品VIP、1设置商品VIP
	 * @param adminUser
	 * @return
	 */
	public int setProdoctAction(Product product, int actionType, AdminUser adminUser) {
		String actionContent = "款号ID："+product.getId()+"，"+product.getName();
		ProductActionLog productActionLog = new ProductActionLog();
		productActionLog.setActionUserId(adminUser.getUserId());
		productActionLog.setActionUserName(adminUser.getUserRealName());
		productActionLog.setActionUserAccount(adminUser.getUserName());
		productActionLog.setProductId(product.getId());
		productActionLog.setActionType(actionType);
		productActionLog.setActionContent(actionContent);
		productActionLog.setCreateTime(System.currentTimeMillis());
		return sqlSessionTemplate.insert("com.jiuy.core.dao.actionlog.ProductActionLogMapper.setProdoctAction", productActionLog);
	}

	
		
	
	
	
	
}