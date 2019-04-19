package com.jiuy.core.service.task;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuy.core.dao.mapper.DayReportMapper;
import com.jiuy.web.controller.util.DateUtil;

/**
* @author WuWanjian
* @version 创建时间: 2017年5月23日 下午4:14:07
*/
public class JiuCoinDeductDailyReport {
	@Autowired
	private DayReportMapper dayReportMapper;
	
	public void execute() {
		dayReportMapper.executeJiuCoinDeductDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd() - DateUtils.MILLIS_PER_DAY);
	}
}
