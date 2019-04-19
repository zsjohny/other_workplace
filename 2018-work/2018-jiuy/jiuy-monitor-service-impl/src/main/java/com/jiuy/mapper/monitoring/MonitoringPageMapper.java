package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringPage; 
import  java.util.*;
import com.jiuy.base.mapper.BaseMapper;

public interface MonitoringPageMapper extends BaseMapper<MonitoringPage>{  

     /***  @Costom ***/
	
	/**   
	 * @Title: pageUv   
	 * @Description: 页面Uv
	 * @param:  date 目标时间  
	 * @return: Map<String,String>      
	 * @throws   
	 */
	public List<Map<String,String>> pageUV(Date date);
	
	/**   
	 * @Title: pagePv   
	 * @Description: 页面Pv
	 * @param: date   
	 * @return: Map<String,String>      
	 * @throws   
	 */
	public List<Map<String,String>> pagePV(Date date);
	
	/**   
	 * @Title: pageStayTimeCount   
	 * @Description: 某个页面所有停留总和
	 * @param: date   
	 * @return: Map<String,String>      
	 * @throws   
	 */
	public List<Map<String,String>> pageStayTimeCount(Date date);
	
}
