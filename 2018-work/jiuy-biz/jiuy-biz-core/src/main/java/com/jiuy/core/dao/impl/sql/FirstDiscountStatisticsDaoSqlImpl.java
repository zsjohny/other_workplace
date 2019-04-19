package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.FirstDiscountStatisticsDao;
import com.jiuyuan.entity.FirstDiscountStatisticsDayBean;
import com.jiuyuan.entity.OrderFirstDiscountLog;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午11:01:27
*/
@Repository
public class FirstDiscountStatisticsDaoSqlImpl implements FirstDiscountStatisticsDao{

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
	//查询日统计数据
	@Override
	public FirstDiscountStatisticsDayBean searchDayStatistics(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		FirstDiscountStatisticsDayBean result = sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.FirstDiscountStatisticsDaoSqlImpl.searchDayStatistics", params);
		return result == null ? new FirstDiscountStatisticsDayBean():result;
	}
	//今日统计数据
	@Override
	public FirstDiscountStatisticsDayBean searchTodayStatistics(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.FirstDiscountStatisticsDaoSqlImpl.searchTodayStatistics", params);
	}
	@Override
	public List<OrderFirstDiscountLog> searchRecord(long orderNo, long userId, double minMoney, double maxMoney,
			long startTime, long endTime,PageQuery pageQuery) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("userId", userId);
		params.put("minMoney", minMoney);
		params.put("maxMoney", maxMoney);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		params.put("pageQuery", pageQuery);
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.FirstDiscountStatisticsDaoSqlImpl.searchRecord",params);
	}
	@Override
	public int searchRecordCount(long orderNo, long userId, double minMoney, double maxMoney, long startTime,
			long endTime) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderNo", orderNo);
		params.put("userId", userId);
		params.put("minMoney", minMoney);
		params.put("maxMoney", maxMoney);
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		return sqlSessionTemplate.selectOne("com.jiuy.core.dao.impl.sql.FirstDiscountStatisticsDaoSqlImpl.searchRecordCount",params);
	}
	
	
}
