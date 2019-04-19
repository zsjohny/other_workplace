package com.jiuy.mapper.monitoring; 
import  com.jiuy.model.monitoring.MonitoringMetadata;
import com.jiuy.base.mapper.BaseMapper;

public interface MonitoringMetadataMapper extends BaseMapper<MonitoringMetadata>{  

     /***  @Costom ***/
	
	/**   
	 * @Title: updateWithVersion   
	 * @Description: 只修改状态为0的数据，解析完成数据后修改元数据的状态
	 * @param:  metadata   
	 * @return: int      
	 * @throws   
	 */
	public int updateWithVersion(MonitoringMetadata metadata);
}
