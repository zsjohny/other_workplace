package com.jiuy.rb.model.common;

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 


/**
 * 全局配置表
 *
 * @author Think
 * @version V1.0
 * @date 2018年06月19日 下午 05:44:23
 * @Copyright 玖远网络
 */
@Data
@ModelName(name = "全局配置表", tableName = "yjj_globalsetting")
public class GlobalSettingRb extends Model {
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 属性名
	 */  
	@FieldName(name = "属性名")  
	private String propertyName;  
 
	/**
	 * 属性值
	 */  
	@FieldName(name = "属性值")  
		private String propertyValue;
 
	/**
	 * 属性值
	 */  
	@FieldName(name = "属性值")  
	private Integer status;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 分组的ID
	 */  
	@FieldName(name = "分组的ID")  
	private Integer groupId;  
 
	/**
	 * 组名
	 */  
	@FieldName(name = "组名")  
	private String groupName;

	/**
	 * 描述
	 */
	@FieldName(name = "描述")
	private String description;
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }