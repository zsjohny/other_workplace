package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 

/**
 * 商品临时信息表,用来存放小程序商品草稿状态时临时信息
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author charlie
 * @version V1.0  
 * @date 2018年09月06日 下午 04:02:56
 */
@Data
@ModelName(name = "商品临时信息表,用来存放小程序商品草稿状态时临时信息", tableName = "shop_product_temp_info")
public class ShopProductRbTempInfo extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 小程序商品id
	 */  
	@FieldName(name = "小程序商品id")  
	private Long shopProductId;  
 
	/**
	 * sku json字符串
	 */  
	@FieldName(name = "sku json字符串")  
	private String skuJson;  
 
	/**
	 * 状态 0删除,1正常
	 */  
	@FieldName(name = "状态 0删除,1正常")  
	private Integer status;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }