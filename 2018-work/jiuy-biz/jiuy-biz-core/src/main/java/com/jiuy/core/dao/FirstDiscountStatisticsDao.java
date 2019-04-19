package com.jiuy.core.dao;

import java.util.List;

import com.jiuyuan.entity.FirstDiscountStatisticsDayBean;
import com.jiuyuan.entity.OrderFirstDiscountLog;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午11:00:13
*/
public interface FirstDiscountStatisticsDao {

	public FirstDiscountStatisticsDayBean searchDayStatistics(long startTime, long endTime);
	
	public FirstDiscountStatisticsDayBean searchTodayStatistics(long startTime, long endTime);
	
	public List<OrderFirstDiscountLog> searchRecord(long orderNo, long userId, double minMoney, double maxMoney,
			long startTime, long endTime,PageQuery pageQuery);
	
	public int searchRecordCount(long orderNo, long userId, double minMoney, double maxMoney, long startTime,
			long endTime);
}
