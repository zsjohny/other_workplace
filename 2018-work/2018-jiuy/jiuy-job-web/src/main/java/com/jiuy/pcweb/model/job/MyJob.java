package com.jiuy.pcweb.model.job; 

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
 * @date 2018年08月01日 上午 11:52:10
 */
@Data
@ModelName(name = "", tableName = "t_job")
public class MyJob extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 任务名称
	 */  
	@FieldName(name = "任务名称")  
	private String name;  
 
	/**
	 * 任务描述
	 */  
	@FieldName(name = "任务描述")  
	private String comment;  
 
	/**
	 * 任务状态: 0 新建状态, 1:开发中,:2待测试,3:测试通过,4:测试不通过
	 */  
	@FieldName(name = "任务状态")  
	private Integer status;  
 
	private Long userId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }