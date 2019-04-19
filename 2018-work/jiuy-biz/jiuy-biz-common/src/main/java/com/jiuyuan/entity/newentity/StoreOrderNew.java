package com.jiuyuan.entity.newentity;

import java.io.Serializable;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

/**
 * <p>
 * 批发订单表
 * CREATE TABLE `store_Order` (
  `OrderNo` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单编号，前台展示',
  `orderNo_attachment_str` varchar(10) DEFAULT '' COMMENT '订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付',
  `StoreId` bigint(20) NOT NULL COMMENT '用户id',
  `OrderType` tinyint(4) DEFAULT NULL COMMENT '（该字段已经废弃）0: 普通订单 1:售后订单 2:当面付订单',
  `OrderStatus` tinyint(4) DEFAULT NULL COMMENT '订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)',
  `TotalMoney` decimal(10,2) NOT NULL COMMENT '订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）',
  `TotalPay` decimal(10,2) NOT NULL COMMENT '订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额',
  `platform_total_preferential` decimal(10,2) DEFAULT NULL COMMENT '平台优惠',
  `supplier_total_preferential` decimal(10,2) DEFAULT NULL COMMENT '商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）',
  `supplier_preferential` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商家优惠（不包含改价部分）',
  `supplier_change_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商家改价差额,original_price - TotalPay,该改价差额可以为负',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '订单金额优惠后原始待付款价格，不包含邮费',
  `TotalExpressMoney` decimal(10,2) NOT NULL COMMENT '邮费总价',
  `ExpressInfo` varchar(1024) NOT NULL COMMENT '邮寄信息',
  `CoinUsed` int(11) NOT NULL COMMENT '使用的玖币',
  `Remark` varchar(1024) DEFAULT NULL COMMENT '用户订单备注',
  `Platform` varchar(30) DEFAULT NULL COMMENT '生成订单平台',
  `PlatformVersion` varchar(45) DEFAULT NULL COMMENT '生成订单平台版本号',
  `Ip` varchar(50) DEFAULT NULL COMMENT '客户端ip',
  `PaymentNo` varchar(50) DEFAULT NULL COMMENT '第三方的支付订单号',
  `PaymentType` tinyint(4) DEFAULT '0' COMMENT '使用的第三方支付方式，参考常量PaymentType',
  `ParentId` bigint(20) NOT NULL DEFAULT '0' COMMENT '母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单',
  `MergedId` bigint(20) NOT NULL DEFAULT '0' COMMENT '0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等',
  `LOWarehouseId` bigint(20) NOT NULL DEFAULT '0' COMMENT '仓库id',
  `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-1删除，0正常',
  `CreateTime` bigint(20) NOT NULL COMMENT '创建时间',
  `UpdateTime` bigint(20) NOT NULL COMMENT '更新时间',
  `TotalMarketPrice` decimal(10,2) DEFAULT '0.00' COMMENT '市场价',
  `CancelReason` varchar(45) DEFAULT NULL COMMENT '取消原因',
  `PushTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '推送时间',
  `ExpiredTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单过期时间',
  `PayTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '付款时间',
  `SendTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '发货时间',
  `Commission` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '收益',
  `AvailableCommission` decimal(10,2) DEFAULT '0.00' COMMENT '可提现金额',
  `CommissionPercent` decimal(10,2) DEFAULT '0.00' COMMENT '提成比例',
  `BrandOrder` bigint(20) DEFAULT NULL COMMENT '品牌批发订单编号',
  `TotalBuyCount` int(11) DEFAULT '0' COMMENT '总购买件数',
  `hasWithdrawed` tinyint(4) DEFAULT '0' COMMENT '新功能涉及到旧的订单，所以出现，是否已经提现，0：旧订单，1：未提现，2已提现',
  `supplierId` bigint(20) DEFAULT '0' COMMENT '供应商ID',
  `groundUserId` bigint(20) DEFAULT '0' COMMENT '地推用户id',
  `superIds` varchar(1024) DEFAULT NULL COMMENT '所有上级ids,例如(,1,3,5,6,)',
  `confirmSignedDate` int(11) NOT NULL DEFAULT '0' COMMENT '确认收获日期，格式例：20170909',
  `confirmSignedTime` bigint(20) NOT NULL DEFAULT '0' COMMENT '确认收获时间，时间戳',
  `order_close_type` tinyint(4) DEFAULT '0' COMMENT '订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单',
  `total_refund_cost` decimal(10,2) DEFAULT '0.00' COMMENT '总的退款金额',
  `total_valid_pay` decimal(10,2) DEFAULT '0.00' COMMENT '订单金额折后有效总价，去除退款金额，不包含邮费',
  `refund_underway` tinyint(4) DEFAULT '0' COMMENT '是否是售后进行中：0(否)、1(是)',
  `refund_start_time` bigint(20) DEFAULT '0' COMMENT '售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0',
  `express_name` varchar(1024) DEFAULT NULL COMMENT '收件人姓名',
  `express_phone` varchar(1024) DEFAULT NULL COMMENT '收件人号码',
  `express_address` varchar(1024) DEFAULT NULL COMMENT '收件人地址',
  `auto_take_delivery_pause_time_length` bigint(20) DEFAULT '0' COMMENT '买家自动确认收货总暂停时长(毫秒)',
  `order_supplier_remark` varchar(500) DEFAULT NULL COMMENT '订单供应商备注',
  `order_close_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '订单关闭时间',
  `hangUp` tinyint(4) DEFAULT '0' COMMENT '是否平台挂起 0：否  1：是',
  `locking_order` tinyint(4) DEFAULT '0' COMMENT '是否锁定在支付订单：0:否，不锁定支付订单 1：是，锁定支付订单 2：3.5版本以下的APP版本不支持改价，不包含3.5',
  `restriction_activity_product_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '限购活动商品id',
  PRIMARY KEY (`OrderNo`),
  KEY `idx_orderno` (`OrderNo`),
  KEY `idx_uid_orderno` (`StoreId`,`OrderNo`)
) ENGINE=InnoDB AUTO_INCREMENT=5419 DEFAULT CHARSET=utf8 COMMENT='批发订单表';


 * </p>
 *
 * @author 赵兴林
 * @since 2017-10-11
 */
@TableName("store_Order")
public class StoreOrderNew extends Model<StoreOrderNew> {

    private static final long serialVersionUID = 1L;
    
    public final static int status_normal = 0;//状态:-1删除，0正常
    public final static int status_del = -1;//状态:-1删除，0正常
    
    public final static int OrderType_normal = 0;// 0: 普通订单 1:售后订单 2:当面付订单
    public final static int OrderType_aftersale = 1;// 0: 普通订单 1:售后订单 2:当面付订单
    
    public final static int UNLOCKING_ORDER = 0;//解锁订单
    public final static int LOCKING_ORDER = 1;//锁住订单
    public final static int VERSION_UNSUPPORT = 2;//版本不支持改价
    
    public final static int REFUND_UNDERWAY = 1;
    public final static int REFUND_NOT_UNDERWAY = 0;
    
    
    /**
     * 订单编号，前台展示
     */
	@TableId(value="OrderNo", type= IdType.AUTO)
	private Long OrderNo;
	/**
	 * 订单随机号,用于微信支付，退款时,生成预生成订单,保证订单能够支付
	 */
	@TableField("orderNo_attachment_str")
	private String orderNoAttachmentStr;
    /**
     * 用户id
     */
	private Long StoreId;
    /**
     * 0: 普通订单 1:售后订单 2:当面付订单
     */
	private Integer OrderType;
	/**
     * 订单付款状态：参照参照OrderStatus 
     * 订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
     */
	private Integer OrderStatus;
	/**
     * 订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105、平台客服关闭订单
     */
	@TableField("order_close_type")
	private Integer orderCloseType;
	
	
    /**
     * 订单金额原价总价，不包含邮费（包含平台优惠和商家店铺优惠）
     */
	private Double TotalMoney;
	  /**
     * 订单金额折后总价,不包含邮费,（不包含平台优惠和店铺优惠）,如果有改价,那么该订单金额为改价后金额
     */
	private Double TotalPay;
	/**
	 * 平台优惠
	 */
	@TableField("platform_total_preferential")
	private Double platformTotalPreferential;
	/**
	 * 商家店铺实际优惠,因为改价差额可以为负，所以商家店铺实际优惠可以为负（包含商家优惠和商家改价差额）
	 */
	@TableField("supplier_total_preferential")
	private Double supplierTotalPreferential;
	/**
	 * 商家优惠（不包含改价部分）
	 */
	@TableField("supplier_preferential")
	private Double supplierPreferential;
	/**
	 * 商家改价差额,original_price - TotalPay,该改价差额可以为负
	 */
	@TableField("supplier_change_price")
	private Double supplierChangePrice;
	/**
	 * 订单金额优惠后原始待付款价格，不包含邮费
	 */
	@TableField("original_price")
	private Double originalPrice;
	  /**
     * 订单金额折后有效总价，去除退款金额，不包含邮费
     */
	@TableField("total_valid_pay")
	private Double totalValidPay;

	/**
	 * 是否发放了优惠券 1 是 0 还未发放
	 */
	@TableField("send_coupon")
	private Integer sendCoupon;
	/**
     * 订单种类 1:商品订单,2购买会员订单
     */
	@TableField("classify")
	private Integer classify;

    /**
     * 邮费总价
     */
	private Double TotalExpressMoney;
    /**
     * 邮寄信息
     */
	private String ExpressInfo;
    /**
     * 使用的玖币
     */
	private Integer CoinUsed;
	 /**
     * 用户订单备注
     */
	private String Remark;
	
	
	
    /**
     * 生成订单平台
     */
	private String Platform;
    /**
     * 生成订单平台版本号
     */
	private String PlatformVersion;
    /**
     * 客户端ip
     */
	private String Ip;
    /**
     * 第三方的支付订单号
     */
	private String PaymentNo;
    /**
     * 使用的第三方支付方式，参考常量PaymentType
     */
	private Integer PaymentType;
    /**
     * 母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单
     */
	private Long ParentId;
    /**
     * 0:其他, 需合并订单：自身改为-1并把相应的被合并的订单此字段设为合好的新订单OrderNo, 不需要合并订单：值与自身OrderNo相等
     */
	private Long MergedId;
    /**
     * 仓库id
     */
	private Long LOWarehouseId;
    /**
     * 状态:-1删除，0正常
     */
	private Integer Status;
    /**
     * 创建时间
     */
	private Long CreateTime;
    /**
     * 更新时间
     */
	private Long UpdateTime;
    /**
     * 市场价
     */
	private Double TotalMarketPrice;
    /**
     * 取消原因
     */
	private String CancelReason;
    /**
     * 推送时间
     */
	private Long PushTime;
    /**
     * 订单过期时间
     */
	private Long ExpiredTime;
    /**
     * 付款时间
     */
	private Long PayTime;
    /**
     * 发货时间
     */
	private Long SendTime;
//    /**
//     * 收益（已废弃）
//     */
//	private Double Commission;
//    /**
//     * 可提现金额（已废弃）
//     */
//	private Double AvailableCommission;
    /**
     * 提成比例（已废弃）
     */
	private Double CommissionPercent;
//    /**
//     * 品牌批发订单编号（已废弃）
//     */
//	private Long BrandOrder;
	/**
     * 总购买件数
     */
	private Integer TotalBuyCount;
	
	/**
     * 新功能涉及到旧的订单，所以出现，是否已经提现，0：旧订单，1：未提现，2已提现
     */
	private Integer hasWithdrawed;
	
    /**
     * 供应商ID
     */
	private Long supplierId;
	
	/**
     * 地推用户id
     */
	private Long groundUserId;
    /**
     * 所有上级ids,例如(,1,3,5,6,)
     */
	private String superIds;
    /**
     * 确认收获日期，格式例：20170909
     */
	private Integer confirmSignedDate;
    /**
     * 确认收获日期，格式例：20170909
     */
	private Long confirmSignedTime;
	
	/**
	 * 是否是售后进行中：0(否)、1(是)
	 */
	@TableField("refund_underway")
	private Integer refundUnderway;
	
	/**
	 * 自动确认收货总暂停时长(毫秒)
	 */
	@TableField("auto_take_delivery_pause_time_length")
	private Long autoTakeGeliveryPauseTimeLength;
	
	/**
	 * 售后开始时间时间戳,使用方法,当该订单发起第一笔售后或者平台介入就记录时间戳,当该订单售后完全结束且平台介入结束时，该字段变为0
	 */
	@TableField("refund_start_time")
	private Long refundStartTime;
    
	/**
	 * 总的退款金额
	 */
	@TableField("total_refund_cost")
	private Double totalRefundCost;
	
	/**
	 * 收件人姓名
	 */
	@TableField("express_name")
	private String expressName;
	
	/**
	 * 收件人号码
	 */
	@TableField("express_phone")
	private String expressPhone;
	
	/**
	 * 收件人地址
	 */
	@TableField("express_address")
	private String expressAddress;
	
	 /**
     * 供应商订单备注
     */
	@TableField("order_supplier_remark")
	private String orderSupplierRemark;
	
	/**
     * 订单关闭时间
     */
	@TableField("order_close_time")
	private Long orderCloseTime;
	
	/**
     * 是否挂起 0：否  1：是
     */
	private Integer hangUp;
	
	/**
	 * 是否锁定在支付订单：0：否,不锁定支付订单 1：是,锁定支付订单
	 */
	@TableField("locking_order")
	private Integer lockingOrder;
	
	/**
	 * 限购活动商品id
	 */
	@TableField("restriction_activity_product_id")
	private Long restrictionActivityProductId;

	/**
	 * 0 普通, 1,进货金专属(平台代发货)
	 */
	private Integer type;

	/**
	 * 平台代发货关联的小程序订单
	 */
	@TableField("shop_member_order_id")
	private Long shopMemberOrderId;


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getShopMemberOrderId() {
		return shopMemberOrderId;
	}

	public void setShopMemberOrderId(Long shopMemberOrderId) {
		this.shopMemberOrderId = shopMemberOrderId;
	}

	public String getOrderSupplierRemark() {
		return orderSupplierRemark;
	}

	public void setOrderSupplierRemark(String orderSupplierRemark) {
		this.orderSupplierRemark = orderSupplierRemark;
	}

	public Long getRefundStartTime() {
		return refundStartTime;
	}

	public void setRefundStartTime(Long refundStartTime) {
		this.refundStartTime = refundStartTime;
	}

	public Long getOrderNo() {
		return OrderNo;
	}

	public void setOrderNo(Long OrderNo) {
		this.OrderNo = OrderNo;
	}

	public Long getStoreId() {
		return StoreId;
	}

	public void setStoreId(Long StoreId) {
		this.StoreId = StoreId;
	}

	public Integer getOrderType() {
		return OrderType;
	}

	public void setOrderType(Integer OrderType) {
		this.OrderType = OrderType;
	}

	public Integer getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(Integer OrderStatus) {
		this.OrderStatus = OrderStatus;
	}

	public Integer getClassify() {
		return classify;
	}

	public void setClassify(Integer classify) {
		this.classify = classify;
	}

	public Double getTotalMoney() {
		return TotalMoney;
	}

	public Double getTotalRefundCost() {
		return totalRefundCost;
	}

	public void setTotalRefundCost(Double totalRefundCost) {
		this.totalRefundCost = totalRefundCost;
	}

	public void setTotalMoney(Double TotalMoney) {
		this.TotalMoney = TotalMoney;
	}

	public Double getTotalPay() {
		return TotalPay;
	}

	public void setTotalPay(Double TotalPay) {
		this.TotalPay = TotalPay;
	}
    
	public Double getPlatformTotalPreferential() {
		return platformTotalPreferential;
	}

	public void setPlatformTotalPreferential(Double platformTotalPreferential) {
		this.platformTotalPreferential = platformTotalPreferential;
	}

	public Double getSupplierTotalPreferential() {
		return supplierTotalPreferential;
	}

	public void setSupplierTotalPreferential(Double supplierTotalPreferential) {
		this.supplierTotalPreferential = supplierTotalPreferential;
	}

	public Double getSupplierPreferential() {
		return supplierPreferential;
	}

	public void setSupplierPreferential(Double supplierPreferential) {
		this.supplierPreferential = supplierPreferential;
	}

	public Double getSupplierChangePrice() {
		return supplierChangePrice;
	}

	public void setSupplierChangePrice(Double supplierChangePrice) {
		this.supplierChangePrice = supplierChangePrice;
	}

	public Double getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Double getTotalExpressMoney() {
		return TotalExpressMoney;
	}

	public void setTotalExpressMoney(Double TotalExpressMoney) {
		this.TotalExpressMoney = TotalExpressMoney;
	}

	public String getExpressInfo() {
		return ExpressInfo;
	}

	public void setExpressInfo(String ExpressInfo) {
		this.ExpressInfo = ExpressInfo;
	}

	public Integer getCoinUsed() {
		return CoinUsed;
	}

	public void setCoinUsed(Integer CoinUsed) {
		this.CoinUsed = CoinUsed;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	public String getPlatform() {
		return Platform;
	}

	public void setPlatform(String Platform) {
		this.Platform = Platform;
	}

	public String getPlatformVersion() {
		return PlatformVersion;
	}

	public void setPlatformVersion(String PlatformVersion) {
		this.PlatformVersion = PlatformVersion;
	}

	public String getIp() {
		return Ip;
	}

	public void setIp(String Ip) {
		this.Ip = Ip;
	}

	public String getPaymentNo() {
		return PaymentNo;
	}

	public void setPaymentNo(String PaymentNo) {
		this.PaymentNo = PaymentNo;
	}

	public Integer getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(Integer PaymentType) {
		this.PaymentType = PaymentType;
	}

	public Long getParentId() {
		return ParentId;
	}

	public void setParentId(Long ParentId) {
		this.ParentId = ParentId;
	}

	public Long getMergedId() {
		return MergedId;
	}

	public void setMergedId(Long MergedId) {
		this.MergedId = MergedId;
	}

	public Long getLOWarehouseId() {
		return LOWarehouseId;
	}

	public void setLOWarehouseId(Long LOWarehouseId) {
		this.LOWarehouseId = LOWarehouseId;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Long CreateTime) {
		this.CreateTime = CreateTime;
	}

	public Long getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(Long UpdateTime) {
		this.UpdateTime = UpdateTime;
	}

	public Double getTotalMarketPrice() {
		return TotalMarketPrice;
	}

	public void setTotalMarketPrice(Double TotalMarketPrice) {
		this.TotalMarketPrice = TotalMarketPrice;
	}

	public String getCancelReason() {
		return CancelReason;
	}

	public void setCancelReason(String CancelReason) {
		this.CancelReason = CancelReason;
	}

	public Long getPushTime() {
		return PushTime;
	}

	public void setPushTime(Long PushTime) {
		this.PushTime = PushTime;
	}

	public Long getExpiredTime() {
		return ExpiredTime;
	}

	public void setExpiredTime(Long ExpiredTime) {
		this.ExpiredTime = ExpiredTime;
	}

	public Long getPayTime() {
		return PayTime;
	}

	public void setPayTime(Long PayTime) {
		this.PayTime = PayTime;
	}

	public Long getSendTime() {
		return SendTime;
	}

	public void setSendTime(Long SendTime) {
		this.SendTime = SendTime;
	}

//	public Double getCommission() {
//		return Commission;
//	}
//
//	public void setCommission(Double Commission) {
//		this.Commission = Commission;
//	}
//
//	public Double getAvailableCommission() {
//		return AvailableCommission;
//	}
//
//	public void setAvailableCommission(Double AvailableCommission) {
//		this.AvailableCommission = AvailableCommission;
//	}

	public Double getCommissionPercent() {
		return CommissionPercent;
	}

	public void setCommissionPercent(Double CommissionPercent) {
		this.CommissionPercent = CommissionPercent;
	}

//	public Long getBrandOrder() {
//		return BrandOrder;
//	}
//
//	public void setBrandOrder(Long BrandOrder) {
//		this.BrandOrder = BrandOrder;
//	}

	public Integer getTotalBuyCount() {
		return TotalBuyCount;
	}

	public void setTotalBuyCount(Integer TotalBuyCount) {
		this.TotalBuyCount = TotalBuyCount;
	}

	public Integer getHasWithdrawed() {
		return hasWithdrawed;
	}

	public void setHasWithdrawed(Integer hasWithdrawed) {
		this.hasWithdrawed = hasWithdrawed;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Override
	protected Serializable pkVal() {
		return this.OrderNo;
	}
	
	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public String getSuperIds() {
		return superIds;
	}

	public void setSuperIds(String superIds) {
		this.superIds = superIds;
	}

	public Integer getConfirmSignedDate() {
		return confirmSignedDate;
	}

	public void setConfirmSignedDate(Integer confirmSignedDate) {
		this.confirmSignedDate = confirmSignedDate;
	}

	public Long getConfirmSignedTime() {
		return confirmSignedTime;
	}

	public void setConfirmSignedTime(Long confirmSignedTime) {
		this.confirmSignedTime = confirmSignedTime;
	}

	public Integer getOrderCloseType() {
		return orderCloseType;
	}

	public void setOrderCloseType(Integer orderCloseType) {
		this.orderCloseType = orderCloseType;
	}

	public Double getTotalValidPay() {
		return totalValidPay;
	}

	public void setTotalValidPay(Double totalValidPay) {
		this.totalValidPay = totalValidPay;
	}

	

	public Integer getRefundUnderway() {
		return refundUnderway;
	}

	public void setRefundUnderway(Integer refundUnderway) {
		this.refundUnderway = refundUnderway;
	}

	public Long getAutoTakeGeliveryPauseTimeLength() {
		return autoTakeGeliveryPauseTimeLength;
	}

	public void setAutoTakeGeliveryPauseTimeLength(Long autoTakeGeliveryPauseTimeLength) {
		this.autoTakeGeliveryPauseTimeLength = autoTakeGeliveryPauseTimeLength;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressPhone() {
		return expressPhone;
	}

	public void setExpressPhone(String expressPhone) {
		this.expressPhone = expressPhone;
	}

	public String getExpressAddress() {
		return expressAddress;
	}

	public void setExpressAddress(String expressAddress) {
		this.expressAddress = expressAddress;
	}
	
	public Long getOrderCloseTime() {
		return orderCloseTime;
	}

	public void setOrderCloseTime(Long orderCloseTime) {
		this.orderCloseTime = orderCloseTime;
	}

	public Integer getHangUp() {
		return hangUp;
	}

	public void setHangUp(Integer hangUp) {
		this.hangUp = hangUp;
	}

	public String getOrderNoAttachmentStr() {
		return orderNoAttachmentStr;
	}

	public void setOrderNoAttachmentStr(String orderNoAttachmentStr) {
		this.orderNoAttachmentStr = orderNoAttachmentStr;
	}
    
	
	public Integer getLockingOrder() {
		return lockingOrder;
	}

	public void setLockingOrder(Integer lockingOrder) {
		this.lockingOrder = lockingOrder;
	}

	public Long getRestrictionActivityProductId() {
		return restrictionActivityProductId;
	}

	public void setRestrictionActivityProductId(Long restrictionActivityProductId) {
		this.restrictionActivityProductId = restrictionActivityProductId;
	}


	public Integer getSendCoupon() {
		return sendCoupon;
	}

	public void setSendCoupon(Integer sendCoupon) {
		this.sendCoupon = sendCoupon;
	}

	/**
     * 获取订单关闭时间  V3.2 仇约帆
     * @param storeOrder
     * @return
     */
    public long buildOrderCloseTime(StoreOrderNew storeOrder) {
    	  // TODO V3.2待实现 
    	     //交易关闭的时间，根据不同情况分为：
    	  //1、用户取消待付款订单的时间，
    	  //2、平台取消已付款订单的时间，
    	  //3、售后退货退款：卖家确认收货或者系统自动，仅退款：卖家同意受理售后时间。
    	  //4、未付款订单超时后系统自动关闭订单的时间
    	  long closeTime = 0;
    	  int orderCloseType = storeOrder.getOrderCloseType();//orderCloseType订单关闭类型：0(无需关闭)、101买家主动取消订单、102超时未付款系统自动关闭订单、103全部退款关闭订单、104卖家关闭订单、105平台客服关闭订单
    	  if(orderCloseType == 101){//101买家主动取消订单
    	   closeTime = this.orderCloseTime;
    	  }else if(orderCloseType == 102){//102超时未付款系统自动关闭订单
    	   closeTime = this.orderCloseTime;
    	  }else if(orderCloseType == 103){//103全部退款关闭订单
    	   closeTime = this.orderCloseTime;
    	  }else if(orderCloseType == 104){//104卖家关闭订单
    	   closeTime = 0;//暂时没有实现这个功能
    	  }else if(orderCloseType == 105){//105平台客服关闭订单
    	   closeTime = 0;//老平台取消订单的状态是订单取消，不是订单关闭，所以这个功能暂停，这个时间暂时没有
    	  }
    	  return closeTime;
    }

//	@Override
//	public String toString() {
//		return "StoreOrder [OrderNo=" + OrderNo + ", StoreId=" + StoreId + ", OrderType=" + OrderType + ", OrderStatus="
//				+ OrderStatus + ", TotalMoney=" + TotalMoney + ", TotalPay=" + TotalPay + ", TotalExpressMoney="
//				+ TotalExpressMoney + ", ExpressInfo=" + ExpressInfo + ", CoinUsed=" + CoinUsed + ", Remark=" + Remark
//				+ ", Platform=" + Platform + ", PlatformVersion=" + PlatformVersion + ", Ip=" + Ip + ", PaymentNo="
//				+ PaymentNo + ", PaymentType=" + PaymentType + ", ParentId=" + ParentId + ", MergedId=" + MergedId
//				+ ", LOWarehouseId=" + LOWarehouseId + ", Status=" + Status + ", CreateTime=" + CreateTime
//				+ ", UpdateTime=" + UpdateTime + ", TotalMarketPrice=" + TotalMarketPrice + ", CancelReason="
//				+ CancelReason + ", PushTime=" + PushTime + ", ExpiredTime=" + ExpiredTime + ", PayTime=" + PayTime
//				+ ", SendTime=" + SendTime + ", Commission=" + Commission + ", AvailableCommission="
//				+ AvailableCommission + ", CommissionPercent=" + CommissionPercent + ", BrandOrder=" + BrandOrder
//				+ ", TotalBuyCount=" + TotalBuyCount + ", supplierId=" + supplierId + "]";
//	}

//	public String getExpressNo() {
//		return ExpressNo;
//	}
//
//	public void setExpressNo(String expressNo) {
//		ExpressNo = expressNo;
//	}
//
//	public String getExpressCompamyName() {
//		return ExpressCompamyName;
//	}
//
//	public void setExpressCompamyName(String expressCompamyName) {
//		ExpressCompamyName = expressCompamyName;
//	}

}