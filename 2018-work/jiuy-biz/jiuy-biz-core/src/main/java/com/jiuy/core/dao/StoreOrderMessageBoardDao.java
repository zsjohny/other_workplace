package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月11日 下午5:16:40
*/
public interface StoreOrderMessageBoardDao {

	int add(StoreOrderMessageBoard storeOrderMessageBoard);

	List<StoreOrderMessageBoard> search(PageQuery pageQuery, long orderNo, List<Integer> types, long adminId, long startTime,
            long endTime);
	
	int searchCount(long orderNo, List<Integer> types, long adminId, long startTime, long endTime);

	StoreOrderMessageBoard getCheckInfo(long orderNo);

	List<StoreOrderMessageBoard> getLastType(List<Integer> types);

	StoreOrderMessageBoard getLastByOrderNo(long storeOrderNo);
}
