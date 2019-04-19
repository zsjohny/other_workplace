package com.jiuy.service.monitoring;

import com.jiuy.base.model.MyPage;
import com.jiuy.base.util.ResponseResult;
import com.jiuy.model.monitoring.*;
import java.util.Map;

/**   
 * 元素据接收及处理的services
 * @author Aison
 * @date   2018年4月18日 下午2:03:27
 * @Copyright 玖远网络
 */  
public interface IMonitoringService {

	/**
	 * 存储一个元数据
	 * @param monitoringMetadata 元数据
	 * @return void
	 */
	void acceptData(MonitoringMetadata monitoringMetadata);
	
	/**
	 * 解析数据
	 * @return void
	 */
	void doParseData();

	/**
	 * 返回设备的列表分页
	 *
	 * @param query 查询对象
	 * @date   2018/4/19 14:13
	 * @author Aison
	 */
	Map<String, Object> deviceReportPage(MonitoringTotalReportQuery query);

	/**
	 * 返回页面报表的分页
	 * @param query 查询对象
	 * @date   2018/4/19 14:13
	 * @author Aison
	 */
	Map<String,Object> pageReportPage(MonitoringPageReportQuery query) ;
	
	/**     
	 * 
	 * 定时解析传递过来的数据
	 * @return void
	 */
	String autoParseData();
	
	
	/**   
	 * 定时统计报表
	 * @return void
	 */
	String autoReport();

	/**
	 * 接收短信推广打开数据
	 * @param monitoringSms
	 * @date:   2018/4/24 15:20
	 * @author: Aison
	 */
	void acceptSmsLog(MonitoringSms monitoringSms,String brandId,String pageCode);

	/**
	 * 查询短信统计列表
	 * @param param
	 * @date:   2018/4/24 16:17
	 * @author: Aison
	 */
	MyPage<Map<String,Object>> smsReport(Map<String,Object> param);

}
