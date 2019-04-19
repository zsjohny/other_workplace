package com.jiuy.user.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 资源表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月08日 下午 05:01:47
 */
@Data
@ModelName(name = "资源表")
public class UrlResource extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 菜单或者按钮名称
	 */  
	@FieldName(name = "菜单或者按钮名称")  
	private String name;  
 
	/**
	 * 地址:
	 */  
	@FieldName(name = "地址")  
	private String uri;  
 
	/**
	 * 是菜单还是按钮:1 菜单 2按钮
	 */  
	@FieldName(name = "是菜单还是按钮")  
	private Integer isMenu;  
 
	/**
	 * 上级菜单ID
	 */  
	@FieldName(name = "上级菜单ID")  
	private String parentId;  
 
	/**
	 * 功能分组:
	 */  
	@FieldName(name = "功能分组")  
	private String groups;  
 
	/**
	 * 菜单id:
	 */  
	@FieldName(name = "菜单id")  
	private String mId;  
 
	/**
	 * 状态:启用 禁用
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 排序:
	 */  
	@FieldName(name = "排序")  
	private Integer sort;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }