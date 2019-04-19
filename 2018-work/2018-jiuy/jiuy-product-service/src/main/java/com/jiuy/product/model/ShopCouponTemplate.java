package com.jiuy.product.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 优惠券模板表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:44:17
 */
@Data
@ModelName(name = "优惠券模板表", tableName = "shop_coupon_template")
public class ShopCouponTemplate extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商家ID
	 */  
	@FieldName(name = "商家ID")  
	private Long storeId;  
 
	/**
	 * 标题
	 */  
	@FieldName(name = "标题")  
	private String name;  
 
	/**
	 * 是否填写标题：0未填写，1已填写
	 */  
	@FieldName(name = "是否填写标题")  
	private Integer fillinName;  
 
	/**
	 * 面值 元
	 */  
	@FieldName(name = "面值 元")  
	private BigDecimal money;  
 
	/**
	 * 限额 满多少可以使用
	 */  
	@FieldName(name = "限额 满多少可以使用")  
	private BigDecimal limitMoney;  
 
	/**
	 * 领取量
	 */  
	@FieldName(name = "领取量")  
	private Integer getCount;  
 
	/**
	 * 使用量
	 */  
	@FieldName(name = "使用量")  
	private Integer usedCount;  
 
	/**
	 * 可用量（已废弃）
	 */  
	@FieldName(name = "可用量（已废弃）")  
	private Integer availableCount;  
 
	/**
	 * 发放量
	 */  
	@FieldName(name = "发放量")  
	private Integer grantCount;  
 
	/**
	 * 发行量（已废弃）
	 */  
	@FieldName(name = "发行量（已废弃）")  
	private Integer publishCount;  
 
	/**
	 * 有效开始时间
	 */  
	@FieldName(name = "有效开始时间")  
	private Long validityStartTime;  
 
	/**
	 * 有效结束时间
	 */  
	@FieldName(name = "有效结束时间")  
	private Long validityEndTime;  
 
	/**
	 * 状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
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
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }