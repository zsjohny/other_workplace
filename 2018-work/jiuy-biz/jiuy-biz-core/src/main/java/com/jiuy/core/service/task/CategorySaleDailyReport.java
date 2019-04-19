package com.jiuy.core.service.task;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.DayReportMapper;
import com.jiuy.web.controller.util.DateUtil;

/**
 * @author jeff.zhan
 * @version 2016年11月21日 下午2:38:17
 * 
 */
/**
 * #########定时任务##########
 * task.category.sale.daily.report
 * #########################
 * 地推日报表
 */
@Service
public class CategorySaleDailyReport {
	
	@Autowired
	private DayReportMapper dayReportMapper;
	public void execute() {
		dayReportMapper.executeCategorySaleDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd() - DateUtils.MILLIS_PER_DAY);
	}
}
