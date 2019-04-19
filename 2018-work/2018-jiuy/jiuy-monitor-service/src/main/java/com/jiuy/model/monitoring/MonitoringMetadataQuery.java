package com.jiuy.model.monitoring;
import lombok.Data;

@Data
public class MonitoringMetadataQuery extends MonitoringMetadata{
	
	/**   
	 * @Fields retryCount : 重试次数
	 */   
	private Integer retryCount;

}
