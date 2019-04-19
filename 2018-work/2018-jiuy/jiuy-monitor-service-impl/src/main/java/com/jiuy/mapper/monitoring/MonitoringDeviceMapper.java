package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringDevice; 
import  java.util.*;

import com.jiuy.base.mapper.BaseMapper;

public interface MonitoringDeviceMapper extends BaseMapper<MonitoringDevice>{  

     /***  @Costom ***/
	
	/**   
	 * @Title: opendDeviceCount   
	 * @Description: 打开设备的数量 独立设备
	 * @param:  date 统计那天
	 * @return: int      
	 * @throws   
	 */
	public long opendDeviceCount(Date date);
	
	/**   
	 * @Title: openAppCount   
	 * @Description: 打开app的数量
	 * @param:  date 统计那天
	 * @return: int      
	 * @throws   
	 */
	public long openAppCount(Date date);
	
	/**   
	 * @Title: appStayTimeCount   
	 * @Description: app总停留时间
	 * @param:  date 统计那天
	 * @return: int      
	 * @throws   
	 */
	public long appStayTimeCount(Date date);
}
