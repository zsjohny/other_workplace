package com.jiuy.core.dao.mapper;
/**
 * @author jeff.zhan
 * @version 2016年11月21日 下午2:42:12
 * 
 */

public interface DayReportMapper {

	void executeStoreDailyReport(long startTime, long endTime);
	
	void executeCategorySaleDailyReport(long startTime, long endTime);
	
	
	void executeTemplateSeniorDailyReport(long startTime, long endTime);
	
	void executePageProductStatisticsDailyReport(long startTime, long endTime);
	
	void executePageStatisticsDailyReport(long startTime, long endTime);

	void executeUserShareDailyReport(long startTime, long endTime);
	
	void executeJiuCoinDeductDailyReport(long startTime, long endTime);
	
	void executeFirstDiscountDailyReport(long startTime, long endTime);
	
	void executeGroundCustomerChangeDailyReport(int date);
	
	void executeGroundUserStatistics(int date);

	void executeGroundBonusDailyReport(int date);
	
	void executeProductDetailDailyReport(long startTime);

}