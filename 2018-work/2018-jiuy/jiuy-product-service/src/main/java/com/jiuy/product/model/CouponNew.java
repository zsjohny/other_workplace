package com.jiuy.product.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:43:30
 */
@Data
@ModelName(name = "优惠券表", tableName = "yjj_coupon_new")
public class CouponNew extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 小程序用户idS
	 */  
	@FieldName(name = "小程序用户idS")  
	private Long memberId;  
 
	/**
	 * 门店用户id
	 */  
	@FieldName(name = "门店用户id")  
	private Long storeId;  
 
	/**
	 * 用户类型: 1门店宝 2小程序
	 */  
	@FieldName(name = "用户类型")  
	private Integer userType;  
 
	/**
	 * 模板id
	 */  
	@FieldName(name = "模板id")  
	private Long templateId;  
 
	/**
	 * 优惠券面额
	 */  
	@FieldName(name = "优惠券面额")  
	private BigDecimal price;  
 
	/**
	 * 优惠折扣
	 */  
	@FieldName(name = "优惠折扣")  
	private BigDecimal discount;  
 
	/**
	 * 优惠券名称
	 */  
	@FieldName(name = "优惠券名称")  
	private String templateName;  
 
	/**
	 * 是否可用:-2删除 -1:作废  0:未用 1:已使用
	 */  
	@FieldName(name = "是否可用")  
	private Integer status;  
 
	/**
	 * 订单号
	 */  
	@FieldName(name = "订单号")  
	private String orderNo;  
 
	/**
	 * 使用时间区间
	 */  
	@FieldName(name = "使用时间区间")  
	private Date useBeginTime;  
 
	/**
	 * 使用时间区间
	 */  
	@FieldName(name = "使用时间区间")  
	private Date useEndTime;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 发放方:供应商id 或者storeId
	 */  
	@FieldName(name = "发放方")  
	private Long publishUserId;  
 
	/**
	 * 发布方姓名
	 */  
	@FieldName(name = "发布方姓名")  
	private String publishUser;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	/**
	 * 订单满足多少可用
	 */  
	@FieldName(name = "订单满足多少可用")  
	private BigDecimal limitMoney;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }