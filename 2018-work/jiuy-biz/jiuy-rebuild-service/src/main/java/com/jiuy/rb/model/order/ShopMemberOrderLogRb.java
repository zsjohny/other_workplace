package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 会员订单日志表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月17日 上午 11:26:28
 */
@Data
@ModelName(name = "会员订单日志表", tableName = "shop_member_order_log")
public class ShopMemberOrderLogRb extends Model {  
 
	/**
	 * 主键id
	 */  
	@FieldName(name = "主键id")  
	@PrimaryKey  
	private Long id;  

	/**
	 * 关联yjj_StoreBusiness表的id
	 */  
	@FieldName(name = "关联yjj_StoreBusiness表的id")  
	private Long storeId;  
 
	/**
	 * 关联shop_Member表的id
	 */  
	@FieldName(name = "关联shop_Member表的id")  
	private Long memberId;  
 
	/**
	 * 关联Order表的id
	 */  
	@FieldName(name = "关联Order表的id")  
	private Long orderId;  
 
	/**
	 * 老的订单状态：0:待付款;1:已付款;2:退款中;3:订单关闭;4:订单完成
	 */  
	@FieldName(name = "老的订单状态：0")  
	private Integer oldStatus;  
 
	/**
	 * 新的订单状态：0:待付款;1:已付款;2:退款中;3:订单关闭;4:订单完成
	 */  
	@FieldName(name = "新的订单状态：0")  
	private Integer newStatus;  
 
	/**
	 * 记录创建时间
	 */  
	@FieldName(name = "记录创建时间")  
	private Long createTime;

	/**
	 * 操作人
	 */
	@FieldName(name = "操作人")
	private String operAccount;

	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }