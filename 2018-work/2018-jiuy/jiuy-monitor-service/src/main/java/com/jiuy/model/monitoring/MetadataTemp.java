package com.jiuy.model.monitoring;

import java.util.List;

import lombok.Data;

/**   
 * @ClassName:  MetadataTemp   
 * @Description:用来存放解析的json数据
 * @author: Aison 
 * @date:   2018年4月18日 下午2:26:27    
 * @Copyright: 玖远网络
 */  
@Data
public class MetadataTemp {
	
	private Integer type;
	
	private String comment;
	
	private List<MetadataItem> data;
}
