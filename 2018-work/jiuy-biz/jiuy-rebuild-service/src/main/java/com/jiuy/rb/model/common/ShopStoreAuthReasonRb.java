package com.jiuy.rb.model.common; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 会员认证不通过原因预设
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月13日 下午 02:34:50
 */
@Data
@ModelName(name = "会员认证不通过原因预设")
public class ShopStoreAuthReasonRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 认证审核不通过原因
	 */  
	@FieldName(name = "认证审核不通过原因")  
	private String noPassReason;  
 
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
	 * 拒绝原因类型：0：门店认证1：商品认证2：售后拒绝原因
	 */  
	@FieldName(name = "拒绝原因类型：0：门店认证1：商品认证2：售后拒绝原因")  
	private Integer type;  
 
	/**
	 * 权重
	 */  
	@FieldName(name = "权重")  
	private Integer weight;  
 
	/**
	 * 是否删除 0:否  1:是
	 */  
	@FieldName(name = "是否删除 0")  
	private Integer isDelete;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }