package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model; 
import lombok.Data; 
import java.math.BigDecimal;

/**
 * 批发订单表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年08月03日 下午 06:23:39
 */
@Data
@ModelName(name = "批发订单表", tableName = "store_order")
public class StoreOrderRb extends Model {  
 
	/**
	 * 订单编号，前台展示
	 */  
	@FieldName(name = "订单编号，前台展示")  
	@PrimaryKey  
	private Long orderNo;  
 
	/**
	 * 订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付
	 */  
	@FieldName(name = "订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付")  
	private String ordernoAttachmentStr;  
 
	/**
	 * 用户id
	 */  
	@FieldName(name = "用户id")  
	private Long storeId;  
 
	/**
	 * （该字段已经废弃）0: 普通订单 1:售后订单 2:当面付订单
	 */  
	@FieldName(name = "（该字段已经废弃）0")  
	private Integer orderType;  
 
	/**
	 * 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
	 */  
	@FieldName(name = "订单状态")  
	private Integer orderStatus;  
 
	/**
	 * 订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）
	 */  
	@FieldName(name = "订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）")  
	private BigDecimal totalMoney;  
 
	/**
	 * 订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额
	 */  
	@FieldName(name = "订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额")  
	private BigDecimal totalPay;  
 
	/**
	 * 平台优惠
	 */  
	@FieldName(name = "平台优惠")  
	private BigDecimal platformTotalPreferential;  
 
	/**
	 * 商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）
	 */  
	@FieldName(name = "商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）")  
	private BigDecimal supplierTotalPreferential;  
 
	/**
	 * 商家优惠（不包含改价部分）
	 */  
	@FieldName(name = "商家优惠（不包含改价部分）")  
	private BigDecimal supplierPreferential;  
 
	/**
	 * 商家改价差额,original_price - TotalPay,该改价差额可以为负
	 */  
	@FieldName(name = "商家改价差额,original_price - TotalPay,该改价差额可以为负")  
	private BigDecimal supplierChangePrice;  
 
	/**
	 * 订单金额优惠后原始待付款价格，不包含邮费
	 */  
	@FieldName(name = "订单金额优惠后原始待付款价格，不包含邮费")  
	private BigDecimal originalPrice;  
 
	/**
	 * 邮费总价
	 */  
	@FieldName(name = "邮费总价")  
	private BigDecimal totalExpressMoney;  
 
	/**
	 * 邮寄信息
	 */  
	@FieldName(name = "邮寄信息")  
	private String expressInfo;  
 
	/**
	 * 使用的玖币
	 */  
	@FieldName(name = "使用的玖币")  
	private Integer coinUsed;  
 
	/**
	 * 用户订单备注
	 */  
	@FieldName(name = "用户订单备注")  
	private String remark;  
 
	/**
	 * 生成订单平台
	 */  
	@FieldName(name = "生成订单平台")  
	private String platform;  
 
	/**
	 * 生成订单平台版本号
	 */  
	@FieldName(name = "生成订单平台版本号")  
	private String platformVersion;  
 
	/**
	 * 客户端ip
	 */  
	@FieldName(name = "客户端ip")  
	private String ip;  
 
	/**
	 * 第三方的支付订单号
	 */  
	@FieldName(name = "第三方的支付订单号")  
	private String paymentNo;  
 
	/**
	 * 使用的第三方支付方式，参考常量PaymentType
	 */  
	@FieldName(name = "使用的第三方支付方式，参考常量PaymentType")  
	private Integer paymentType;  
 
	/**
	 * 母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单
	 */  
	@FieldName(name = "母订单id 0")  
	private Long parentId;  
 
	/**
	 * 0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等
	 */  
	@FieldName(name = "0")  
	private Long mergedId;  
 
	/**
	 * 仓库id
	 */  
	@FieldName(name = "仓库id")  
	private Long lOWarehouseId;  
 
	/**
	 * 状态:-1删除，0正常
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
 
	/**
	 * 市场价
	 */  
	@FieldName(name = "市场价")  
	private BigDecimal totalMarketPrice;  
 
	/**
	 * 取消原因
	 */  
	@FieldName(name = "取消原因")  
	private String cancelReason;  
 
	/**
	 * 推送时间
	 */  
	@FieldName(name = "推送时间")  
	private Long pushTime;  
 
	/**
	 * 订单过期时间
	 */  
	@FieldName(name = "订单过期时间")  
	private Long expiredTime;  
 
	/**
	 * 付款时间
	 */  
	@FieldName(name = "付款时间")  
	private Long payTime;  
 
	/**
	 * 发货时间
	 */  
	@FieldName(name = "发货时间")  
	private Long sendTime;  
 
	/**
	 * 收益
	 */  
	@FieldName(name = "收益")  
	private BigDecimal commission;  
 
	/**
	 * 可提现金额
	 */  
	@FieldName(name = "可提现金额")  
	private BigDecimal availableCommission;  
 
	/**
	 * 提成比例
	 */  
	@FieldName(name = "提成比例")  
	private BigDecimal commissionPercent;  
 
	/**
	 * 品牌批发订单编号
	 */  
	@FieldName(name = "品牌批发订单编号")  
	private Long brandOrder;  
 
	/**
	 * 总购买件数
	 */  
	@FieldName(name = "总购买件数")  
	private Integer totalBuyCount;  
 
	/**
	 * 新功能涉及到旧的订单，所以出现，是否已经提现，0：旧订单，1：未提现，2已提现
	 */  
	@FieldName(name = "新功能涉及到旧的订单，所以出现，是否已经提现，0")  
	private Integer hasWithdrawed;  
 
	/**
	 * 供应商ID
	 */  
	@FieldName(name = "供应商ID")  
	private Long supplierId;  
 
	/**
	 * 地推用户id
	 */  
	@FieldName(name = "地推用户id")  
	private Long groundUserId;  
 
	/**
	 * 所有上级ids,例如(,1,3,5,6,)
	 */  
	@FieldName(name = "所有上级ids,例如(,1,3,5,6,)")  
	private String superIds;  
 
	/**
	 * 确认收获日期，格式例：20170909
	 */  
	@FieldName(name = "确认收获日期，格式例")  
	private Integer confirmSignedDate;  
 
	/**
	 * 确认收获时间，时间戳
	 */  
	@FieldName(name = "确认收获时间，时间戳")  
	private Long confirmSignedTime;  
 
	/**
	 * 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
	 */  
	@FieldName(name = "订单关闭类型")  
	private Integer orderCloseType;  
 
	/**
	 * 总的退款金额
	 */  
	@FieldName(name = "总的退款金额")  
	private BigDecimal totalRefundCost;  
 
	/**
	 * 订单金额折后有效总价，去除退款金额，不包含邮费
	 */  
	@FieldName(name = "订单金额折后有效总价，去除退款金额，不包含邮费")  
	private BigDecimal totalValidPay;  
 
	/**
	 * 是否是售后进行中：0(否)、1(是)
	 */  
	@FieldName(name = "是否是售后进行中")  
	private Integer refundUnderway;  
 
	/**
	 * 售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0
	 */  
	@FieldName(name = "售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0")  
	private Long refundStartTime;  
 
	/**
	 * 收件人姓名
	 */  
	@FieldName(name = "收件人姓名")  
	private String expressName;  
 
	/**
	 * 收件人号码
	 */  
	@FieldName(name = "收件人号码")  
	private String expressPhone;  
 
	/**
	 * 收件人地址
	 */  
	@FieldName(name = "收件人地址")  
	private String expressAddress;  
 
	/**
	 * 买家自动确认收货总暂停时长(毫秒)
	 */  
	@FieldName(name = "买家自动确认收货总暂停时长(毫秒)")  
	private Long autoTakeDeliveryPauseTimeLength;  
 
	/**
	 * 订单供应商备注
	 */  
	@FieldName(name = "订单供应商备注")  
	private String orderSupplierRemark;  
 
	/**
	 * 订单关闭时间
	 */  
	@FieldName(name = "订单关闭时间")  
	private Long orderCloseTime;  
 
	/**
	 * 是否平台挂起 0：否  1：是
	 */  
	@FieldName(name = "是否平台挂起 0")  
	private Integer hangUp;  
 
	/**
	 * 是否锁定在支付订单：0:否，不锁定支付订单 1：是，锁定支付订单 2：3.5版本以下的APP版本不支持改价，不包含3.5
	 */  
	@FieldName(name = "是否锁定在支付订单：0")  
	private Integer lockingOrder;  
 
	/**
	 * 限购活动商品id
	 */  
	@FieldName(name = "限购活动商品id")  
	private Long restrictionActivityProductId;  
 
	/**
	 * 支付成功后是否发了优惠券:0 否 1 是
	 */  
	@FieldName(name = "支付成功后是否发了优惠券")  
	private Integer sendCoupon;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }