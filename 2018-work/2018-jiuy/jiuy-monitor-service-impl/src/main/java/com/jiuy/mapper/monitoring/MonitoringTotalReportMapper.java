package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringTotalReport; 
import  java.util.*;
import com.jiuy.base.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

public interface MonitoringTotalReportMapper extends BaseMapper<MonitoringTotalReport>{  

    /***  @Costom ***/
	/**   
	 * @Title: oneDayPVSum   
	 * @Description: 每一天所有页面PV的总数
	 * @param:  date
	 * @return: long      
	 * @throws   
	 */
	long oneDayPVTotal(Date date);
	
	/**   
	 * 删除某一天的统计结果  
	 * @param:  date   
	 * @return: int      
	 * @throws   
	 */
	int delteOneDayReport(Date date);


	/**
	 * 查询某个时间段内的汇总
	 * @param beginDate
	 * @param endDate
	 * @date:   2018/4/26 11:21
	 * @author: Aison
	 */
	MonitoringTotalReport sumTotalReport(@Param("beginDate") Date beginDate,@Param("endDate") Date endDate);

}
