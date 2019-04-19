package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 会员订单表
 * </p>
 *
 * @author nijin
 * @since 2018-01-11
 */
@TableName("shop_member_order")
public class ShopMemberOrder extends Model<ShopMemberOrder> {

    private static final long serialVersionUID = 1L;
    
    public static final int CANCEL_REASON_TYPE_NO = 0;
    
    public static final int CANCEL_REASON_TYPE_MEMBER = 1;
    
    public static final int CANCEL_REASON_TYPE_STORE = 2;
    
    public static final int CANCEL_REASON_TYPE_SYSTEM = 3;
    
    public static final int CANCEL_REASON_TYPE_STORE_ACTIVITY_CANCEL = 4;
    
  //订单类型
    //到店取货
    public static final int order_type_get_product_at_store = 0;
    //送货上门
    public static final int order_type_delivery = 1;
    
    //秒杀
    public static final int buy_way_miaosha = 2;
    //团购
    public static final int buy_way_tuangou = 1;
    //普通
    public static final int buy_way_putong = 0;
    
    //订单状态
    //待付款
    public static final int order_status_pending_payment = 0;
    //待提货
    public static final int order_status_paid = 1;
    //退款中
    public static final int order_status_refund = 2;
    //订单关闭
    public static final int order_status_order_closed = 3;
    //订单完成
    public static final int order_status_order_fulfillment = 4;
    
    //待发货
    public static final int order_status_pending_delivery = 5;
    
    //已发货
    public static final int order_status_already_shipped =6;
    //第三方支付类型：0(无)、1微信小程序、2微信公众号
    public static final int paymentType_wxa = 1;
    public static final int paymentType_public = 2;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 订单编号
     */
	@TableField("order_number")
	private String orderNumber;
    /**
     * 商家ID
     */
	@TableField("store_id")
	private Long storeId;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private Long memberId;
	/**
	 * 秒杀活动Id
	 */
	@TableField("second_id")
	private Long secondId  ;
	/**
	 * 团购活动Id
	 */
	@TableField("team_id")
	private Long teamId  ;
    /**
     * 订单总金额，含邮费
     */
	@TableField("total_express_and_money")
	private Double totalExpressAndMoney;
    /**
     * 商品总金额，不含邮费
     */
	@TableField("total_money")
	private Double totalMoney;
    /**
     * 实付金额
     */
	@TableField("pay_money")
	private Double payMoney;
    /**
     * 优惠金额
     */
	@TableField("sale_money")
	private Double saleMoney;
    /**
     * 邮费金额
     */
	@TableField("express_money")
	private Double expressMoney;
    /**
     * 商品总件数
     */
	private Integer count;
    /**
     * 商品主图集合，英文逗号分隔
     */
	@TableField("summary_images")
	private String summaryImages;
    /**
     * 订单类型：到店提货或送货上门(0:到店提货;1:送货上门)
     */
	@TableField("order_type")
	private Integer orderType;
    /**
     * 订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
     */
	@TableField("order_status")
	private Integer orderStatus;
    /**
     * 第三方的支付订单号
     */
	@TableField("payment_no")
	private String paymentNo;
    /**
     * 取消类型：0无、1会员取消、2商家取消、3系统自动取消
     */
	@TableField("cancel_reason_type")
	private Integer cancelReasonType;
    /**
     * 取消原因
     */
	@TableField("cancel_reason")
	private String cancelReason;
    /**
     * 交易关闭时间
     */
	@TableField("order_stop_time")
	private Long orderStopTime;
    /**
     * 付款时间
     */
	@TableField("pay_time")
	private Long payTime;
    /**
     * 提货时间
     */
	@TableField("take_delivery_time")
	private Long takeDeliveryTime;
    /**
     * 交易完成时间
     */
	@TableField("order_finish_time")
	private Long orderFinishTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	private Long updateTime;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Long createTime;
    /**
     * 购买用户的昵称
     */
	@TableField("user_nickname")
	private String userNickname;
    /**
     * 用户优惠券ID
     */
	@TableField("coupon_id")
	private Long couponId;
    /**
     * 用户优惠券标题
     */
	@TableField("coupon_name")
	private String couponName;
    /**
     * 用户优惠券限额 满多少可以使用
     */
	@TableField("coupon_limit_money")
	private Double couponLimitMoney;
    /**
     * 是否已经发送订单即将超时通知:0未发送;1已发送
     */
	@TableField("send_message")
	private Integer sendMessage;
    /**
     * 预支付交易会话标识，用于发送服务模板通知
     */
	@TableField("pay_form_id")
	private String payFormId;
    /**
     * 第三方支付类型：0(无)、1微信小程序、2微信公众号
     */
	@TableField("payment_type")
	private Integer paymentType;
    /**
     * 邮递公司中文名称
     */
	@TableField("expree_supplier_cnname")
	private String expreeSupplierCnname;
    /**
     * 邮递公司名称
     */
	@TableField("express_supplier")
	private String expressSupplier;
    /**
     * 邮递运单号
     */
	@TableField("express_no")
	private String expressNo;
    /**
     * 收件人姓名
     */
	@TableField("receiver_name")
	private String receiverName;
    /**
     * 收件人电话
     */
	@TableField("receiver_phone")
	private String receiverPhone;
    /**
     * 收件人地址
     */
	@TableField("receiver_address")
	private String receiverAddress;
	/**
	 * 物流信息
	 */
	@TableField("express_info")
	private String expressInfo;
    /**
     * 发货时间
     */
	@TableField("delivery_time")
	private Long deliveryTime;
    /**
     * 确认收货日期，格式例：20170909
     */
	private Integer confirmSignedDate;
    /**
     * 确认收货时间，时间戳
     */
	private Long confirmSignedTime;
	/**
	 * 给商家的留言
	 */
	private String remark;
	/**
	 * 购买方式 0：普通  1：团购  2：秒杀
	 */
	@TableField("buy_way")
	private Integer buyWay;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public Long getSecondId() {
		return secondId;
	}

	public void setSecondId(Long secondId) {
		this.secondId = secondId;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public Double getTotalExpressAndMoney() {
		return totalExpressAndMoney;
	}

	public void setTotalExpressAndMoney(Double totalExpressAndMoney) {
		this.totalExpressAndMoney = totalExpressAndMoney;
	}

	public Double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public Double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}

	public Double getSaleMoney() {
		return saleMoney;
	}

	public void setSaleMoney(Double saleMoney) {
		this.saleMoney = saleMoney;
	}

	public Double getExpressMoney() {
		return expressMoney;
	}

	public void setExpressMoney(Double expressMoney) {
		this.expressMoney = expressMoney;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getSummaryImages() {
		return summaryImages;
	}

	public void setSummaryImages(String summaryImages) {
		this.summaryImages = summaryImages;
	}

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPaymentNo() {
		return paymentNo;
	}

	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}

	public Integer getCancelReasonType() {
		return cancelReasonType;
	}

	public void setCancelReasonType(Integer cancelReasonType) {
		this.cancelReasonType = cancelReasonType;
	}

	public String getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}

	public Long getOrderStopTime() {
		return orderStopTime;
	}

	public void setOrderStopTime(Long orderStopTime) {
		this.orderStopTime = orderStopTime;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Long getTakeDeliveryTime() {
		return takeDeliveryTime;
	}

	public void setTakeDeliveryTime(Long takeDeliveryTime) {
		this.takeDeliveryTime = takeDeliveryTime;
	}

	public Long getOrderFinishTime() {
		return orderFinishTime;
	}

	public void setOrderFinishTime(Long orderFinishTime) {
		this.orderFinishTime = orderFinishTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public String getCouponName() {
		return couponName;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public Double getCouponLimitMoney() {
		return couponLimitMoney;
	}

	public void setCouponLimitMoney(Double couponLimitMoney) {
		this.couponLimitMoney = couponLimitMoney;
	}

	public Integer getSendMessage() {
		return sendMessage;
	}

	public void setSendMessage(Integer sendMessage) {
		this.sendMessage = sendMessage;
	}

	public String getPayFormId() {
		return payFormId;
	}

	public void setPayFormId(String payFormId) {
		this.payFormId = payFormId;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getExpreeSupplierCnname() {
		return expreeSupplierCnname;
	}

	public void setExpreeSupplierCnname(String expreeSupplierCnname) {
		this.expreeSupplierCnname = expreeSupplierCnname;
	}

	public String getExpressSupplier() {
		return expressSupplier;
	}

	public void setExpressSupplier(String expressSupplier) {
		this.expressSupplier = expressSupplier;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public Long getDeliveryTime() {
		return deliveryTime;
	}

	public void setDeliveryTime(Long deliveryTime) {
		this.deliveryTime = deliveryTime;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getExpressInfo() {
		return expressInfo;
	}

	public void setExpressInfo(String expressInfo) {
		this.expressInfo = expressInfo;
	}
	
	public Integer getBuyWay() {
		return buyWay;
	}

	public void setBuyWay(Integer buyWay) {
		this.buyWay = buyWay;
	}

	@Override
	public String toString() {
		return "ShopMemberOrder [id=" + id + ", orderNumber=" + orderNumber + ", storeId=" + storeId + ", memberId="
				+ memberId + ", totalExpressAndMoney=" + totalExpressAndMoney + ", totalMoney=" + totalMoney
				+ ", payMoney=" + payMoney + ", saleMoney=" + saleMoney + ", expressMoney=" + expressMoney + ", count="
				+ count + ", summaryImages=" + summaryImages + ", orderType=" + orderType + ", orderStatus="
				+ orderStatus + ", paymentNo=" + paymentNo + ", cancelReasonType=" + cancelReasonType
				+ ", cancelReason=" + cancelReason + ", orderStopTime=" + orderStopTime + ", payTime=" + payTime
				+ ", takeDeliveryTime=" + takeDeliveryTime + ", orderFinishTime=" + orderFinishTime + ", updateTime="
				+ updateTime + ", createTime=" + createTime + ", userNickname=" + userNickname + ", couponId="
				+ couponId + ", couponName=" + couponName + ", couponLimitMoney=" + couponLimitMoney + ", sendMessage="
				+ sendMessage + ", payFormId=" + payFormId + ", paymentType=" + paymentType + ", expreeSupplierCnname="
				+ expreeSupplierCnname + ", expressSupplier=" + expressSupplier + ", expressNo=" + expressNo
				+ ", receiverName=" + receiverName + ", receiverPhone=" + receiverPhone + ", receiverAddress="
				+ receiverAddress + ", expressInfo=" + expressInfo + ", deliveryTime=" + deliveryTime
				+ ", confirmSignedDate=" + confirmSignedDate + ", confirmSignedTime=" + confirmSignedTime + ", remark="
				+ remark + ", buyWay=" + buyWay + "]";
	}
}
