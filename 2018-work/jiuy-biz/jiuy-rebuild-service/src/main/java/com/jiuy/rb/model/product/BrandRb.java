package com.jiuy.rb.model.product; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 品牌表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年06月14日 下午 04:29:18
 */
@Data
@ModelName(name = "品牌表", tableName = "yjj_brand")
public class BrandRb extends Model {  
 
	/**
	 * 品牌表主键
	 */  
	@FieldName(name = "品牌表主键")  
	@PrimaryKey  
	private Integer id;  
 
	private Long brandId;  
 
	/**
	 * 品牌名称
	 */  
	@FieldName(name = "品牌名称")  
	private String brandName;  
 
	/**
	 * 品牌logo
	 */  
	@FieldName(name = "品牌logo")  
	private String logo;  
 
	/**
	 * 状态:1:禁用,0:正常,-1:删除
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
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
 
	private String cnName;  
 
	private String description;  
 
	/**
	 * 权重-排序
	 */  
	@FieldName(name = "权重-排序")  
	private Integer weight;  
 
	/**
	 * 品牌标识
	 */  
	@FieldName(name = "品牌标识")  
	private String brandIdentity;  
 
	/**
	 * 是否优惠 0:否 1：是
	 */  
	@FieldName(name = "是否优惠 0")  
	private Integer isDiscount;  
 
	/**
	 * 满额立减条件
	 */  
	@FieldName(name = "满额立减条件")  
	private BigDecimal exceedMoney;  
 
	/**
	 * 满额立减数
	 */  
	@FieldName(name = "满额立减数")  
	private BigDecimal minusMoney;  
 
	/**
	 * 品牌商品款号前缀
	 */  
	@FieldName(name = "品牌商品款号前缀")  
	private String clothNumberPrefix;  
 
	/**
	 * 品牌类型：0：未设置 1：高档，2：中档
	 */  
	@FieldName(name = "品牌类型")  
	private Integer brandType;  
 
	/**
	 * 品牌推广图
	 */  
	@FieldName(name = "品牌推广图")  
	private String brandPromotionImg;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }