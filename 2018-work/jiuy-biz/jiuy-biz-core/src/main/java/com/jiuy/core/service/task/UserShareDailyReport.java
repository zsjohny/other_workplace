package com.jiuy.core.service.task;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.DayReportMapper;
import com.jiuy.web.controller.util.DateUtil;
/**
 * 
 * @author chenyuan
 *
 */

@Service
public class UserShareDailyReport {
	
	@Autowired
	private DayReportMapper dayReportMapper;

	public void execute() {
		dayReportMapper.executeUserShareDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd() - DateUtils.MILLIS_PER_DAY);
	}
}
