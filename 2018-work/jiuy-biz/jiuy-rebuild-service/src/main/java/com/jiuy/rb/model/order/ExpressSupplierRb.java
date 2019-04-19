package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 快递公司管理
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 05:23:16
 */
@Data
@ModelName(name = "快递公司管理", tableName = "yjj_expresssupplier")
public class ExpressSupplierRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 快递供应商中文名
	 */  
	@FieldName(name = "快递供应商中文名")  
	private String cnName;  
 
	/**
	 * 快递供应商英文名
	 */  
	@FieldName(name = "快递供应商英文名")  
	private String engName;  
 
	/**
	 * 网上查询快递信息，快递信息链接前缀 
	 */  
	@FieldName(name = "网上查询快递信息，快递信息链接前缀 ")  
	private String queryLink;  
 
	/**
	 * 状态 -1：删除，0：正常
	 */  
	@FieldName(name = "状态 -1")  
	private Integer status;  
 
	private Long createTime;  
 
	private Long updateTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }