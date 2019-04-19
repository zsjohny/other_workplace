package com.jiuy.product.model; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;
import java.util.Date;

/**
 * 优惠券模板表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月10日 下午 04:43:53
 */
@Data
@ModelName(name = "优惠券模板表", tableName = "yjj_coupon_template_new")
public class CouponTemplateNew extends Model {  
 
	/**
	 * 主键
	 */  
	@FieldName(name = "主键")  
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 模板名称
	 */  
	@FieldName(name = "模板名称")  
	private String name;  
 
	/**
	 * 获取规则
	 */  
	@FieldName(name = "获取规则")  
	private String getRule;  
 
	/**
	 * 发放类型:0 立即发放,1自助领取,2新用户注册,3用户购买
	 */  
	@FieldName(name = "发放类型")  
	private Integer sendType;  
 
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
	 * 失效时间: 0 开始时间和结束时间 1是领取后多久
	 */  
	@FieldName(name = "失效时间")  
	private Integer deadlineType;  
 
	/**
	 * 失效时间开始
	 */  
	@FieldName(name = "失效时间开始")  
	private Date deadlineEnd;  
 
	/**
	 * 失效开始时间
	 */  
	@FieldName(name = "失效开始时间")  
	private Date deadlineBegin;  
 
	/**
	 * 领取后多久失效
	 */  
	@FieldName(name = "领取后多久失效")  
	private Integer deadlineCount;  
 
	/**
	 * 优惠券模板系统: 1 门店宝app  2 小程序 
	 */  
	@FieldName(name = "优惠券模板系统")  
	private Integer sysType;  
 
	/**
	 * 每人限领多少
	 */  
	@FieldName(name = "每人限领多少")  
	private Integer eachReceiveCount;  
 
	/**
	 * 发行量
	 */  
	@FieldName(name = "发行量")  
	private Long issueCount;  
 
	/**
	 * 平台注释
	 */  
	@FieldName(name = "平台注释")  
	private String comment;  
 
	/**
	 * 累计被领取了多少
	 */  
	@FieldName(name = "累计被领取了多少")  
	private Long receiveCount;  
 
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
	 * 状态:-1：删除，0：正常，1：停止发行，2已领完,3已失效
	 */  
	@FieldName(name = "状态")  
	private Integer status;  
 
	/**
	 * 订单满足多少可用
	 */  
	@FieldName(name = "订单满足多少可用")  
	private BigDecimal limitMoney;  
 
	/**
	 * 使用范围:1 全场可用，2指定商品可用 3指定分类可用
	 */  
	@FieldName(name = "使用范围")  
	private Integer useRange;  
 
	/**
	 * 对应的ids
	 */  
	@FieldName(name = "对应的ids")  
	private String rangeIds;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Date createTime;  
 
	/**
	 * 修改时间
	 */  
	@FieldName(name = "修改时间")  
	private Date updateTime;  
 
	/**
	 * 领取开始时间
	 */  
	@FieldName(name = "领取开始时间")  
	private Date drawStartTime;  
 
	/**
	 * 领取结束时间
	 */  
	@FieldName(name = "领取结束时间")  
	private Date drawEndTime;  
 
	private Long oldId;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }