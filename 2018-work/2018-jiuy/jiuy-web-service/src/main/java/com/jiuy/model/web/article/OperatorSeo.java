package com.jiuy.model.web.article; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 官网seo信息
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月31日 上午 11:55:01
 */
@Data
@ModelName(name = "官网seo信息", tableName = "operator_seo")
public class OperatorSeo extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * seo标题
	 */  
	@FieldName(name = "seo标题")  
	private String title;  
 
	/**
	 * seo描述
	 */  
	@FieldName(name = "seo描述")  
	private String descriptor;  
 
	/**
	 * seo关键词
	 */  
	@FieldName(name = "seo关键词")  
	private String keywords;  
 
	/**
	 * seo类型:1:首页 2:页面默认seo
	 */  
	@FieldName(name = "seo类型")  
	private Integer seoType;  
 
	/**
	 * 状态：0:正常 -1:删除
	 */  
	@FieldName(name = "状态：0")  
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
	 * 删除时间
	 */  
	@FieldName(name = "删除时间")  
	private Long deleteTime;  
 
	/**
	 * 创建管理员ID
	 */  
	@FieldName(name = "创建管理员ID")  
	private Long createOperatorId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }