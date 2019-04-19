package com.jiuy.timer.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年05月31日 上午 09:48:02
 */
@Data
public class QrtzJobDetails extends Model {  
 
	@PrimaryKey  
	private String schedName;  
 
	private String jobName;  
 
	private String jobGroup;  
 
	private String description;  
 
	private String jobClassName;  
 
	private String isDurable;  
 
	private String isNonconcurrent;  
 
	private String isUpdateData;  
 
	private String requestsRecovery;  
 
	private byte[] jobData;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }