package com.jiuy.core.dao.mapper;

import java.util.HashMap;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author jeff.zhan
 * @version 2016年11月21日 下午2:42:29
 * 
 */

@Repository
public class DayReportMapperSqlImpl implements DayReportMapper {
	
	@Autowired
	private SqlSessionTemplate sessionTemplate;

	@Override
	public void executeStoreDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeStoreDailyReport", params);
	}
	
	@Override
	public void executeCategorySaleDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeCategorySaleDailyReport", params);
	}

	@Override
	public void executeTemplateSeniorDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeTemplateSeniorDailyReport", params);
	}
	
	
	@Override
	public void executePageProductStatisticsDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executePageProductStatisticsDailyReport", params);
	}
	
	
	@Override
	public void executePageStatisticsDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executePageStatisticsDailyReport", params);
	}

	@Override
	public void executeUserShareDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeUserShareDailyReport", params);
	}

	@Override
	public void executeJiuCoinDeductDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeJiuCoinDeductDailyReport", params);
	}

	@Override
	public void executeFirstDiscountDailyReport(long startTime, long endTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		params.put("endTime", endTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeFirstDiscountDailyReport", params);
	}

	@Override
	public void executeGroundCustomerChangeDailyReport(int date) {
        Map<String, Object> params = new HashMap<>();
		
		params.put("date", date);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeGroundCustomerChangeDailyReport",params);
	}

	@Override
	public void executeGroundUserStatistics(int date) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("date", date);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeGroundUserStatistics",params);
	}
	
	@Override
	public void executeGroundBonusDailyReport(int date) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("date", date);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeGroundBonusDailyReport",params);
	}

	
	@Override
	public void executeProductDetailDailyReport(long startTime) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("startTime", startTime);
		
		sessionTemplate.selectList("com.jiuy.core.dao.mapper.DayReportMapperSqlImpl.executeProductDetailDailyReport",params);
	}
	
	
}
