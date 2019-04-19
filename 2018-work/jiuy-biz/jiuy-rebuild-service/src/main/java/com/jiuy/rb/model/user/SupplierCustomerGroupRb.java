package com.jiuy.rb.model.user; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 供应商分组表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月29日 下午 02:42:39
 */
@Data
@ModelName(name = "供应商分组表", tableName = "supplier_customer_group")
public class SupplierCustomerGroupRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 供应商Id
	 */  
	@FieldName(name = "供应商Id")  
	private Long supplierId;  
 
	/**
	 * 分组名称
	 */  
	@FieldName(name = "分组名称")  
	private String groupName;  
 
	/**
	 * 备注
	 */  
	@FieldName(name = "备注")  
	private String remark;  
 
	/**
	 *   状态  -1：删除   0：正常
	 */  
	@FieldName(name = "  状态  -1")  
	private Integer status;  
 
	/**
	 * 默认分组 ：0:否  1:是
	 */  
	@FieldName(name = "默认分组 ：0")  
	private Integer defaultGroup;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Long updateTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }