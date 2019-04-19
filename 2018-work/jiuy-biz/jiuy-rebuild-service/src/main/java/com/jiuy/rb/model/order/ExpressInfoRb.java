package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 邮寄信息表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月28日 下午 05:24:48
 */
@Data
@ModelName(name = "邮寄信息表", tableName = "store_expressinfo")
public class ExpressInfoRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 订单表OrderNo
	 */  
	@FieldName(name = "订单表OrderNo")  
	private Long orderNo;  
 
	/**
	 * 快递提供商
	 */  
	@FieldName(name = "快递提供商")  
	private String expressSupplier;  
 
	/**
	 * 快递订单号
	 */  
	@FieldName(name = "快递订单号")  
	private String expressOrderNo;  
 
	/**
	 * 快递信息更新时间
	 */  
	@FieldName(name = "快递信息更新时间")  
	private Long expressUpdateTime;  
 
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
	 * 状态
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 物流信息内容
	 */  
	@FieldName(name = "物流信息内容")  
	private String expressInfo;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }