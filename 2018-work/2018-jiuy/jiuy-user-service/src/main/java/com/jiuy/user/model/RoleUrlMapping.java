package com.jiuy.user.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 角色与资源关联表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月08日 下午 05:00:32
 */
@Data
@ModelName(name = "角色与资源关联表")
public class RoleUrlMapping extends Model {  
 
	/**
	 * 主键:
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 角色id
	 */  
	@FieldName(name = "角色id")  
	private Long roleId;  
 
	/**
	 * 菜单id
	 */  
	@FieldName(name = "菜单id")  
	private Long uriId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }