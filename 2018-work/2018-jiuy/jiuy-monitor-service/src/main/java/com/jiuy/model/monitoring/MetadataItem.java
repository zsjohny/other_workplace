package com.jiuy.model.monitoring;

import lombok.Data;

/**   
 * @ClassName:  MetadataItem   
 * @Description: json序列化的存放数据的类
 * @author: Aison 
 * @date:   2018年4月18日 下午2:32:29    
 * @Copyright: 玖远网络
 */  
@Data
public class MetadataItem {
	
	/**   
	 * @Fields deviceId : 设备id
	 */   
	private String deviceId;
	
	/**   
	 * @Fields phone : 电话号码
	 */   
	private String phone; 
	
	/**   
	 * @Fields openTime : 打开时间  
	 */   
	private Long openTime;
	
	/**   
	 * @Fields closeTime : 关闭时间
	 */   
	private Long closeTime;
	
	/**   
	 * @Fields enterTime : 进入页面时间
	 */   
	private Long enterTime;
	
	/**   
	 * @Fields leaveTime : 离开页面时间
	 */   
	private Long leaveTime;
	
	/**   
	 * @Fields eventCount : 页面事件次数
	 */   
	private Integer eventCount;
	
	/**   
	 * @Fields pageName : 页面名称
	 */   
	private String pageName; 
	
	/**   
	 * @Fields pageCode : 页面编码
	 */   
	private String pageCode; 
	
	/**   
	 * @Fields version : 访问app版本号
	 */   
	private String version; 
	
	/**   
	 * @Fields osName : 设备的系统类型 
	 */   
	private String osName; 
}
