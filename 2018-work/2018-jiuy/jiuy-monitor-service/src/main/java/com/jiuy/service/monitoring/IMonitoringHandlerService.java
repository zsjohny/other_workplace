package com.jiuy.service.monitoring;

import com.jiuy.model.monitoring.MonitoringMetadata;

import java.util.Date;

/**   
 * 监控日志处理servcice
 * @author Aison
 * @date   2018年4月18日 下午2:02:19
 * @Copyright 玖远网络
 */  
public interface IMonitoringHandlerService {

	
	/**   
	 *实际处理数据的类需要事务支持
	 * @param  metadata
	 */
	 void  doDataParse(MonitoringMetadata metadata);

	/**
	 * 计算device的报表
	 * @param date 需要统计那天的报表
	 */
	 void reportDevice(Date date);

	/**
	 * 要统计那天的页面访问报表 如果为空则统计当天的 页面访问统计
	 * @param today 需要统计那天的页面访问报表 如果为空则统计当天的
	 * @date   2018/4/19 10:31
	 * @author Aison
	 */
	 void reportPage(Date today);
}
