package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 会员订单明细表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月18日 上午 09:18:34
 */
@Data
@ModelName(name = "会员订单明细表", tableName = "shop_member_order_item")
public class ShopMemberOrderItemRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 订单ID
	 */  
	@FieldName(name = "订单ID")  
	private Long orderId;  
 
	/**
	 * 订单编号
	 */  
	@FieldName(name = "订单编号")  
	private String orderNumber;  
 
	/**
	 * 商家商品ID
	 */  
	@FieldName(name = "商家商品ID")  
	private Long shopProductId;  
 
	/**
	 * 商家商品类型(自有商品或平台商品):1是自有商品，0平台商品
	 */  
	@FieldName(name = "商家商品类型(自有商品或平台商品)")  
	private Integer own;  
 
	/**
	 * 平台商品ID
	 */  
	@FieldName(name = "平台商品ID")  
	private Long productId;  
 
	/**
	 * SKUID
	 */  
	@FieldName(name = "SKUID")  
	private Long productSkuId;  
 
	/**
	 * 购买数量
	 */  
	@FieldName(name = "购买数量")  
	private Integer count;  
 
	/**
	 * 商品主图
	 */  
	@FieldName(name = "商品主图")  
	private String summaryImages;  
 
	/**
	 * 商品标题
	 */  
	@FieldName(name = "商品标题")  
	private String name;  
 
	/**
	 * 商品颜色名称
	 */  
	@FieldName(name = "商品颜色名称")  
	private String color;  
 
	/**
	 * 商品尺码名称
	 */  
	@FieldName(name = "商品尺码名称")  
	private String size;  
 
	/**
	 * 商品价格
	 */  
	@FieldName(name = "商品价格")  
	private BigDecimal price;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;

	/**
	 * 购买数量，自营份额
	 */
	@FieldName(name = "购买数量，自营份额")
	private Integer selfCount;

	/**
	 * 购买数量，供应商份额
	 */
	@FieldName(name = "购买数量，供应商份额")
	private Integer supplierCount;

	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }