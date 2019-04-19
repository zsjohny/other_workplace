package com.jiuy.rb.model.order; 

import com.jiuy.base.annotation.FieldName; 
import com.jiuy.base.annotation.ModelName; 
import com.jiuy.base.annotation.PrimaryKey; 
import com.jiuy.base.model.Model;
import com.jiuy.rb.enums.MemberOrderCancelEnum;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 会员订单表
 * <p> 
 * 请不要再映射对象内添加自定义属性 如果需要添加属性请继承后在子类中添加属性
 * 
 * @author Aison
 * @version V1.0  
 * @date 2018年07月17日 上午 11:25:54
 */
@Data
@ModelName(name = "会员订单表", tableName = "shop_member_order")
public class ShopMemberOrderRb extends Model {  
 
	@PrimaryKey  
	private Long id;  
 
	/**
	 * 订单编号
	 */  
	@FieldName(name = "订单编号")  
	private String orderNumber;  
 
	/**
	 * 商家ID
	 */  
	@FieldName(name = "商家ID")  
	private Long storeId;  
 
	/**
	 * 会员ID
	 */  
	@FieldName(name = "会员ID")  
	private Long memberId;  
 
	/**
	 * 团购活动id
	 */  
	@FieldName(name = "团购活动id")  
	private Long teamId;  
 
	/**
	 * 秒杀活动id
	 */  
	@FieldName(name = "秒杀活动id")  
	private Long secondId;  
 
	/**
	 * 订单总金额，含邮费
	 */  
	@FieldName(name = "订单总金额，含邮费")  
	private BigDecimal totalExpressAndMoney;  
 
	/**
	 * 商品总金额，不含邮费
	 */  
	@FieldName(name = "商品总金额，不含邮费")  
	private BigDecimal totalMoney;  
 
	/**
	 * 实付金额
	 */  
	@FieldName(name = "实付金额")  
	private BigDecimal payMoney;  
 
	/**
	 * 优惠金额
	 */  
	@FieldName(name = "优惠金额")  
	private BigDecimal saleMoney;  
 
	/**
	 * 邮费金额
	 */  
	@FieldName(name = "邮费金额")  
	private BigDecimal expressMoney;  
 
	/**
	 * 商品总件数
	 */  
	@FieldName(name = "商品总件数")  
	private Integer count;  
 
	/**
	 * 商品主图集合，英文逗号分隔
	 */  
	@FieldName(name = "商品主图集合，英文逗号分隔")  
	private String summaryImages;  
 
	/**
	 * 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
	 */  
	@FieldName(name = "订单类型：到店提货或送货上门(0")  
	private Integer orderType;  
 
	/**
	 * 订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
	 */  
	@FieldName(name = "订单状态：0")  
	private Integer orderStatus;  
 
	/**
	 * 第三方的支付订单号
	 */  
	@FieldName(name = "第三方的支付订单号")  
	private String paymentNo;  
 
	/**
	 * 取消类型：0无、1会员取消、2商家取消、3系统自动取消 4商家手动结束活动,关闭订单 5 平台客服关闭订单
	 * @see  MemberOrderCancelEnum
	 */  
	@FieldName(name = "取消类型")  
	private Integer cancelReasonType;  
 
	/**
	 * 取消原因
	 */  
	@FieldName(name = "取消原因")  
	private String cancelReason;  
 
	/**
	 * 交易关闭时间
	 */  
	@FieldName(name = "交易关闭时间")  
	private Long orderStopTime;  
 
	/**
	 * 付款时间
	 */  
	@FieldName(name = "付款时间")  
	private Long payTime;  
 
	/**
	 * 提货时间
	 */  
	@FieldName(name = "提货时间")  
	private Long takeDeliveryTime;  
 
	/**
	 * 交易完成时间
	 */  
	@FieldName(name = "交易完成时间")  
	private Long orderFinishTime;  
 
	/**
	 * 更新时间
	 */  
	@FieldName(name = "更新时间")  
	private Long updateTime;  
 
	/**
	 * 创建时间
	 */  
	@FieldName(name = "创建时间")  
	private Long createTime;  
 
	/**
	 * 购买用户的昵称
	 */  
	@FieldName(name = "购买用户的昵称")  
	private String userNickname;  
 
	/**
	 * 用户优惠券ID
	 */  
	@FieldName(name = "用户优惠券ID")  
	private Long couponId;  
 
	/**
	 * 用户优惠券标题
	 */  
	@FieldName(name = "用户优惠券标题")  
	private String couponName;  
 
	/**
	 * 用户优惠券限额 满多少可以使用
	 */  
	@FieldName(name = "用户优惠券限额 满多少可以使用")  
	private BigDecimal couponLimitMoney;  
 
	/**
	 * 是否已经发送订单即将超时通知:0未发送;1已发送
	 */  
	@FieldName(name = "是否已经发送订单即将超时通知")  
	private Integer sendMessage;  
 
	/**
	 * 预支付交易会话标识，用于发送服务模板通知
	 */  
	@FieldName(name = "预支付交易会话标识，用于发送服务模板通知")  
	private String payFormId;  
 
	/**
	 * 第三方支付类型：0(无)、1微信小程序、2微信公众号
	 */  
	@FieldName(name = "第三方支付类型")  
	private Integer paymentType;  
 
	/**
	 * 邮递公司中文名称
	 */  
	@FieldName(name = "邮递公司中文名称")  
	private String expreeSupplierCnname;  
 
	/**
	 * 邮递公司名称
	 */  
	@FieldName(name = "邮递公司名称")  
	private String expressSupplier;  
 
	/**
	 * 邮递运单号
	 */  
	@FieldName(name = "邮递运单号")  
	private String expressNo;  
 
	/**
	 * 收件人姓名
	 */  
	@FieldName(name = "收件人姓名")  
	private String receiverName;  
 
	/**
	 * 收件人电话
	 */  
	@FieldName(name = "收件人电话")  
	private String receiverPhone;  
 
	/**
	 * 收件人地址
	 */  
	@FieldName(name = "收件人地址")  
	private String receiverAddress;  
 
	/**
	 * 发货时间
	 */  
	@FieldName(name = "发货时间")  
	private Long deliveryTime;  
 
	/**
	 * 确认收货日期，格式例：20170909
	 */  
	@FieldName(name = "确认收货日期，格式例")  
	private Integer confirmSignedDate;  
 
	/**
	 * 确认收货时间，时间戳
	 */  
	@FieldName(name = "确认收货时间，时间戳")  
	private Long confirmSignedTime;  
 
	/**
	 * 给商家的留言
	 */  
	@FieldName(name = "给商家的留言")  
	private String remark;  
 
	/**
	 * 物流信息内容
	 */  
	@FieldName(name = "物流信息内容")  
	private String expressInfo;  
 
	/**
	 * 购买方式 0：普通   1：团购  2：秒杀
	 */  
	@FieldName(name = "购买方式 0")  
	private Integer buyWay;  
 
	//请不要手动拓展此类 此类为基础类不允许拓展 如果需要拓展请继承后拓展 
  
 }