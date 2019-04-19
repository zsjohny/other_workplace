package com.jiuy.core.service.task;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.DayReportMapper;
import com.jiuyuan.service.common.IGroundCustomerStageChangeService;
import com.jiuyuan.util.DateUtil;

/**
 * @author jeff.zhan
 * @version 2016年11月21日 下午2:38:17
 * 
 */

@Service
public class StoreDailyReport {
	private static final Logger logger = Logger.getLogger(StoreDailyReport.class);
	
	@Autowired
	private DayReportMapper dayReportMapper;
	
	@Autowired    private IGroundCustomerStageChangeService groundCustomerStageChangeService;
	
	public void execute() {
		//门店结算提现的功能在2017-7-7停止
//		dayReportMapper.executeStoreDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY, DateUtil.getTodayEnd() - DateUtils.MILLIS_PER_DAY);
//		groundCustomerStageChangeService.executeGroundCustomerStageChange(DateUtil.getyesterday());
		logger.info("开始统计日报表，并维护地推人员信息！");
		//统计指定日期地推人员的个人和团队的8个阶段的增加或减少的客户数，根据地推人员客户阶段变化计划表数据计算,这里计算昨日的
		dayReportMapper.executeGroundCustomerChangeDailyReport(DateUtil.getyesterday());
		//维护每日维护地推人员信息表
		dayReportMapper.executeGroundUserStatistics(DateUtil.getyesterday());
		//生成奖金动态日报表
		dayReportMapper.executeGroundBonusDailyReport(DateUtil.getyesterday());
		logger.info("结束统计日报表，并维护地推人员信息！");
		
		//生成商品访问统计日报表
		dayReportMapper.executeProductDetailDailyReport(DateUtil.getTodayStart() - DateUtils.MILLIS_PER_DAY);
	}
}
