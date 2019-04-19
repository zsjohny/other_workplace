package com.jiuy.product.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 会员优惠券表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:44:38
 */
@Data
@ModelName(name = "会员优惠券表", tableName = "shop_member_coupon")
public class ShopMemberCoupon extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 商家ID
	 */  
	@FieldName(name = "商家ID")  
	private Long storeId;  
 
	/**
	 * 会员Id
	 */  
	@FieldName(name = "会员Id")  
	private Long memberId;  
 
	/**
	 * 会员昵称
	 */  
	@FieldName(name = "会员昵称")  
	private String memberNicheng;  
 
	/**
	 * 核销操作员Id
	 */  
	@FieldName(name = "核销操作员Id")  
	private Long adminId;  
 
	/**
	 * 核销时间
	 */  
	@FieldName(name = "核销时间")  
	private Long checkTime;  
 
	/**
	 * 核销金额 元
	 */  
	@FieldName(name = "核销金额 元")  
	private BigDecimal checkMoney;  
 
	/**
	 * 优惠券模板Id
	 */  
	@FieldName(name = "优惠券模板Id")  
	private Long couponTemplateId;  
 
	/**
	 * 标题
	 */  
	@FieldName(name = "标题")  
	private String name;  
 
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
	 * 状态:-1：删除，0：正常，1：使用
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