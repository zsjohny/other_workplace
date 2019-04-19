package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.util.Date;

/**
 * 商品销量
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月15日 下午 07:34:35
 */
@Data
@ModelName(name = "商品销量", tableName = "yjj_sales_volume_product")
public class SalesVolumeProductRb extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商品主键
	 */  
	@FieldName(name = "商品主键")  
	private Long productId;  
 
	/**
	 * 商品虚拟销量
	 */  
	@FieldName(name = "商品虚拟销量")
	private Long salesVolume;  
 
	/**
	 * 收藏数量
	 */  
	@FieldName(name = "收藏数量")  
	private Long collectionCount;  
 
	/**
	 * 点赞数量
	 */  
	@FieldName(name = "点赞数量")  
	private Long starCount;  
 
	/**
	 * 下单数量
	 */  
	@FieldName(name = "下单数量")  
	private Long orderCount;  
 
	/**
	 * 成功订单的数量
	 */  
	@FieldName(name = "实际销量,成功订单的购买数量")
	private Long orderSuccessCount;  
 
	/**
	 * 退款数量
	 */  
	@FieldName(name = "退款数量")  
	private Long refundCount;  
 
	/**
	 * 浏览次数
	 */  
	@FieldName(name = "浏览次数")  
	private Long viewCount;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	/**
	 * 商品类型:1app普通商品,2app限时抢购商品,50门店用户普通商品,51门店用户团购活动,52门店用户秒杀活动
	 */
	@FieldName(name = "商品类型")  
	private Integer productType;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }