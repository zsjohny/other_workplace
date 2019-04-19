package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 订单日志记录
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月29日 上午 11:06:08
 */
@Data
@ModelName(name = "订单日志记录", tableName = "store_orderlog")
public class StoreOrderLogRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 关联User表的userId
	 */  
	@FieldName(name = "关联User表的userId")  
	private Long storeId;  
 
	/**
	 * 关联Order表的id
	 */  
	@FieldName(name = "关联Order表的id")  
	private Long orderNo;  
 
	/**
	 * 老的订单状态
	 */  
	@FieldName(name = "老的订单状态")  
	private Integer oldStatus;  
 
	/**
	 * 更新的订单状态
	 */  
	@FieldName(name = "更新的订单状态")  
	private Integer newStatus;  
 
	/**
	 * 记录创建时间
	 */  
	@FieldName(name = "记录创建时间")  
	private Long createTime;  
 
	private Long updateTime;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }