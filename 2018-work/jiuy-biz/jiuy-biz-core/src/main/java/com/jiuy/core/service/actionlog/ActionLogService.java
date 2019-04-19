package com.jiuy.core.service.actionlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.actionlog.ProductActionLogMapper;
import com.jiuy.core.dao.actionlog.StoreActionLogMapper;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.store.ProductActionLog;
import com.jiuyuan.entity.store.StoreActionLog;
import com.jiuyuan.util.DateUtil;

/**
 * 操作日志
 * @author qiuyuefan
 */
@Service
public class ActionLogService{
	
	@Autowired
	private ProductActionLogMapper productActionLogMapper;
	
	@Autowired
	private StoreActionLogMapper storeActionLogMapper;
	
	public List<ProductActionLog>  getProductActionLogList(String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime,PageQuery pageQuery) {
		List<ProductActionLog> productActionLogList = productActionLogMapper.getProductActionLogList( actionUserAccount, actionUserName, actionType, actionContent, startTime, endTime, pageQuery);
		for(ProductActionLog productActionLog : productActionLogList){
			productActionLog.setCreateTimeStr(DateUtil.parseLongTime2Str(productActionLog.getCreateTime()));
		}
		return 	productActionLogList;
	}
	public int getProductActionLogListCount(String actionUserAccount, String actionUserName, String actionType,
			String actionContent, long startTime, long endTime ) {
		 return productActionLogMapper.getProductActionLogListCount( actionUserAccount, actionUserName, actionType, actionContent, startTime, endTime);
	}
	
	public List<StoreActionLog>  getStoreActionLogList(String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime,PageQuery pageQuery) {
		List<StoreActionLog> storeActionLogList = storeActionLogMapper.getStoreActionLogList( actionUserAccount, actionUserName, actionType, actionContent, startTime, endTime, pageQuery);
		for(StoreActionLog storeActionLog : storeActionLogList){
			storeActionLog.setCreateTimeStr(DateUtil.parseLongTime2Str(storeActionLog.getCreateTime()));
		}
		return  storeActionLogList;
	}
	
	public int  getStoreActionLogListCount(String actionUserAccount,String actionUserName,String actionType,String actionContent,long startTime,long endTime) {
		return storeActionLogMapper.getStoreActionLogListCount( actionUserAccount, actionUserName, actionType, actionContent, startTime, endTime);
	}
	/**
	 * 设置商品操作
	 * @param productId
	 * @param vip
	 * @param adminUser
	 */
	public int setProdoctAction(Product product, int action, AdminUser adminUser) {
		return productActionLogMapper.setProdoctAction( product,  action,  adminUser);
	}
	
	/**
	 * 设置门店操作
	 * @param productId
	 * @param vip
	 * @param adminUser
	 */
	public int setStoreAction (StoreBusiness storebusiness, int action, AdminUser adminUser) {
		return storeActionLogMapper.setStoreAction( storebusiness,  action,  adminUser);
	}


	
	
}