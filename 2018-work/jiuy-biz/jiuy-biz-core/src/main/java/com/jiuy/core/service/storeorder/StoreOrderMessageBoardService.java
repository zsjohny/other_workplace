package com.jiuy.core.service.storeorder;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月11日 下午4:03:10
*/
public interface StoreOrderMessageBoardService {

	int add(StoreOrderMessageBoard storeOrderMessageBoard);

	List<Map<String, Object>> search(PageQuery pageQuery, long orderNo, List<Integer> types, long adminId, long startTimeL, long endTimeL);

}
