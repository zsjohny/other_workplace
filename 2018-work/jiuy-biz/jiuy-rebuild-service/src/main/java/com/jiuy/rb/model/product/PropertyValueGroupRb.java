package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 商品属性值组表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Think
 * @version V1.0  
 * @date 2018年07月06日 下午 06:04:37
 */
@Data
@ModelName(name = "商品属性值组表", tableName = "yjj_propertyvaluegroup")
public class PropertyValueGroupRb extends Model {  
 
	/**
	 * 属性值组id
	 */  
	@FieldName(name = "属性值组id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 属性值组名称
	 */  
	@FieldName(name = "属性值组名称")  
	private String groupName;  
 
	/**
	 * 对应属性名id
	 */  
	@FieldName(name = "对应属性名id")  
	private Long propertyNameId;  
 
	/**
	 * 对应商品分类id
	 */  
	@FieldName(name = "对应商品分类id")  
	private Long categoryId;  
 
	/**
	 * 状态:-1删除，0正常
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 排序索引
	 */  
	@FieldName(name = "排序索引")  
	private Integer orderIndex;  
 
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
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }