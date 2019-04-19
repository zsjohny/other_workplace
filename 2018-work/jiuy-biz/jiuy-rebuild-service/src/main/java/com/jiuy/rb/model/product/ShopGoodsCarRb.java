package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 小程序商城购物车
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年09月04日 下午 07:18:37
 */
@Data
@ModelName(name = "小程序商城购物车", tableName = "shop_goods_car")
public class ShopGoodsCarRb extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商品ID
	 */  
	@FieldName(name = "商品ID")  
	private Long productSkuId;  
 
	/**
	 * 会员id
	 */  
	@FieldName(name = "会员id")  
	private Long memberId;  
 
	/**
	 * 商品数量
	 */  
	@FieldName(name = "商品数量")  
	private Long skuNumber;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 购物车中商品状态  -1 删除，0禁用, 1正常，2失效
	 */  
	@FieldName(name = "购物车中商品状态  -1 删除，0禁用, 1正常，2失效")  
	private Integer carSukStatus;  
 
	/**
	 * 门店id
	 */  
	@FieldName(name = "门店id")  
	private Long storeId;  
 
	/**
	 * 商品id
	 */  
	@FieldName(name = "商品id")  
	private Long productId;  
 
	/**
	 * shop_product 表主键
	 */  
	@FieldName(name = "shop_product 表主键")  
	private Long shopProductId;  
 
	/**
	 * 最后修改时间
	 */  
	@FieldName(name = "最后修改时间")  
	private Long lastUpdateTime;  
 
	/**
	 * 是否选择
	 */  
	@FieldName(name = "是否选择")  
	private Integer selected;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }