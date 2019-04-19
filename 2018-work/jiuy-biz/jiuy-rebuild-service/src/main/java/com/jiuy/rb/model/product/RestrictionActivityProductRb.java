package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 限购活动商品表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月15日 下午 05:06:25
 */
@Data
@ModelName(name = "限购活动商品表", tableName = "yjj_restriction_activity_product")
public class RestrictionActivityProductRb extends Model {  
 
	/**
	 * id
	 */  
	@FieldName(name = "id")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 原商品id
	 */  
	@FieldName(name = "原商品id")  
	private Long productId;  
 
	/**
	 * 活动商品名称
	 */  
	@FieldName(name = "活动商品名称")  
	private String productName;  
 
	/**
	 * 活动商品款号
	 */  
	@FieldName(name = "活动商品款号")  
	private String clothesNumber;  
 
	/**
	 * 0:待上架;1:已上架;2:已下架;3:已删除
	 */  
	@FieldName(name = "0")  
	private Integer productStatus;  
 
	/**
	 * 活动商品的sku个数
	 */  
	@FieldName(name = "活动商品的sku个数")  
	private Integer skuCount;  
 
	/**
	 * 活动商品预览图
	 */  
	@FieldName(name = "活动商品预览图")  
	private String mainImage;  
 
	/**
	 * 默认活动商品推广图片,原商品橱窗图
	 */  
	@FieldName(name = "默认活动商品推广图片,原商品橱窗图")  
	private String showcaseImage;  
 
	/**
	 * 活动商品推广图片
	 */  
	@FieldName(name = "活动商品推广图片")  
	private String promotionImage;  
 
	/**
	 * 活动当前剩余库存量
	 */  
	@FieldName(name = "活动当前剩余库存量")  
	private Integer remainCount;  
 
	/**
	 * 活动总库存量
	 */  
	@FieldName(name = "活动总库存量")  
	private Integer totalRemainCount;  
 
	/**
	 * 销量
	 */  
	@FieldName(name = "销量")  
	private Integer saleCount;  
 
	/**
	 * 限购数量
	 */  
	@FieldName(name = "限购数量")  
	private Integer restrictionCount;  
 
	/**
	 * 活动商品价格
	 */  
	@FieldName(name = "活动商品价格")  
	private BigDecimal activityProductPrice;  
 
	/**
	 * 活动商品原价
	 */  
	@FieldName(name = "活动商品原价")  
	private BigDecimal productPrice;  
 
	/**
	 * 活动开始时间
	 */  
	@FieldName(name = "活动开始时间")  
	private Long activityBeginTime;  
 
	/**
	 * 活动结束时间
	 */  
	@FieldName(name = "活动结束时间")  
	private Long activityEndTime;  
 
	/**
	 * 活动商品上架时间
	 */  
	@FieldName(name = "活动商品上架时间")  
	private Long activityProductShelfTime;  
 
	/**
	 * 活动商品下架时间
	 */  
	@FieldName(name = "活动商品下架时间")  
	private Long activityProductDropOffTime;  
 
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
 
	/**
	 * 最小购买量/起订量
	 */  
	@FieldName(name = "最小购买量/起订量")  
	private Integer miniPurchaseCount;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }