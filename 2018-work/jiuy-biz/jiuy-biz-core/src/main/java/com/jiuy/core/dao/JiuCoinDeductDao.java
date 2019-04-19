package com.jiuy.core.dao;

import java.util.Map;

import com.jiuyuan.entity.JiuCoinDeductStatisticsDayBean;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月20日 上午9:31:25
*/
public interface JiuCoinDeductDao {
	
	public JiuCoinDeductStatisticsDayBean sumDayStatistics(long startTime,long endTime);
	
	JiuCoinDeductStatisticsDayBean sumTimeStatistics(long startTime,long endTime);
}
