package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.SubscriptDao;
//import com.jiuy.core.service.common.MemcachedService;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;

@Service("subscriptService")
public class SubscriptServiceImpl {
	
	@Resource
	private SubscriptDao subscriptDaoSqlImpl;
	
	@Autowired
	private MemcachedService memcachedService;
	
	/**
	 * 获得角标
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Subscript> getSubscripts(){
		String groupKey = MemcachedKey.GROUP_KEY_SUBSCRIPT;
		String key = "";
		Object object = memcachedService.get(groupKey, key);
		if(object != null){
			return (List<Subscript>)object;
		}
		
		List<Subscript> subscripts = subscriptDaoSqlImpl.getSubscripts();
		memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, subscripts);
		
		return subscripts;
	}
	
	public List<Subscript> search(String name,String description,Long productId, PageQuery pageQuery){
		return subscriptDaoSqlImpl.search(name, description, productId, pageQuery);
	}
	
	/**
	 * 插入Subscript
	 * @param subscript
	 * @return
	 */
	public Subscript addSubscript(Subscript subscript){
		return subscriptDaoSqlImpl.addSubscript(subscript);
	}
	/**
	 * 更新修改Subscript
	 * @param subscript
	 * @return
	 */
	public ResultCode updateSubscript(Subscript subscript){
		long updateTime = System.currentTimeMillis();
		subscript.setUpdateTime(updateTime);
		subscriptDaoSqlImpl.updateSubscript(subscript);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	public ResultCode updateProductSum(Long id, Integer productSum){
		subscriptDaoSqlImpl.updateProductSum(id, productSum);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	public void remove(Collection<Long> subscriptIds){
		subscriptDaoSqlImpl.remove(subscriptIds);
	}
	
	public void deleteByIds(Collection<Long> subscriptIds){
		subscriptDaoSqlImpl.deleteByIds(subscriptIds);
	}
	
	public int searchCount(String name,String description,Long productId){
		return subscriptDaoSqlImpl.searchCount(name,description,productId);
	}
}
