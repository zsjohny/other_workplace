package com.jiuy.core.service;

import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.FirstDiscountStatisticsDayBean;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午9:59:26
*/
public interface FirstDiscountStatisticsService {
	
	List<FirstDiscountStatisticsDayBean> searchDayStatistics();
	
	FirstDiscountStatisticsDayBean searchTodayStatistics(long startTime, long endTime);
	
	FirstDiscountStatisticsDayBean timeIntervalStatistics(long startTime, long endTime);
	
	Map<String, Object> searchRecord(long orderNo, String yjjNumber, double minMoney, double maxMoney,
			long startTime, long endTime,PageQuery pageQuery);
}
