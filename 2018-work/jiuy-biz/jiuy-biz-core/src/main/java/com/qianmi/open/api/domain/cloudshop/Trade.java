package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 交易信息
 *
 * @author auto
 * @since 2.0
 */
public class Trade extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 代下单用户编号
	 */
	@ApiField("add_user_id")
	private String addUserId;

	/**
	 * 代下单用户姓名
	 */
	@ApiField("add_user_name")
	private String addUserName;

	/**
	 * 卖家自定义的销售区域，国家标准编码
	 */
	@ApiField("area_id")
	private String areaId;

	/**
	 * 买家留言
	 */
	@ApiField("buyer_message")
	private String buyerMessage;

	/**
	 * 买家会员编号
	 */
	@ApiField("buyer_nick")
	private String buyerNick;

	/**
	 * 买家会员姓名
	 */
	@ApiField("buyer_nick_name")
	private String buyerNickName;

	/**
	 * 订单完成状态, -1:全部, 0:进行中, 1:已完成, 2:已作废
	 */
	@ApiField("complete_status")
	private String completeStatus;

	/**
	 *  卖家发货时间格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("consign_time")
	private String consignTime;

	/**
	 *  交易创建时间格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("created")
	private String created;

	/**
	 * 订单发货状态, -1:全部, 0:未发货, 1:已发货, 2:已退货
	 */
	@ApiField("deliver_status")
	private String deliverStatus;

	/**
	 * 系统优惠金额，订单中所有商品单的优惠金额总和，单位:元, 2位小数
	 */
	@ApiField("discount_fee")
	private String discountFee;

	/**
	 *  交易完结时间格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("end_time")
	private String endTime;

	/**
	 * 订单流程状态
	 */
	@ApiField("flow_status")
	private String flowStatus;

	/**
	 * 订单使用积分, 2位小数
	 */
	@ApiField("integral")
	private String integral;

	/**
	 * 积分抵消金额，下单时使用积分抵消的金额, 单位:元, 2位小数
	 */
	@ApiField("integral_fee")
	private String integralFee;

	/**
	 * 是否已开发票
	 */
	@ApiField("invoice_flag")
	private String invoiceFlag;

	/**
	 * 发票抬头
	 */
	@ApiField("invoice_title")
	private String invoiceTitle;

	/**
	 * 发票类型，1：普通发票，2：增值税发票。如果不开发票，则此项为空
	 */
	@ApiField("invoice_type")
	private String invoiceType;

	/**
	 * 是否屏蔽发货
	 */
	@ApiField("is_sh_ship")
	private Boolean isShShip;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 卖家备注
	 */
	@ApiField("memo")
	private String memo;

	/**
	 *  交易修改时间格式: yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("modified")
	private String modified;

	/**
	 * order订单数量
	 */
	@ApiField("num")
	private Integer num;

	/**
	 * 订单列表
	 */
	@ApiListField("orders")
	@ApiField("order")
	private List<Order> orders;

	/**
	 * 网点店铺名称
	 */
	@ApiField("p_shop_name")
	private String pShopName;

	/**
	 * 支付银行编号
	 */
	@ApiField("pay_bank_code")
	private String payBankCode;

	/**
	 * 支付网关编号
	 */
	@ApiField("pay_gate_code")
	private String payGateCode;

	/**
	 * 支付网关名称
	 */
	@ApiField("pay_gate_name")
	private String payGateName;

	/**
	 * 千米网支付单编号
	 */
	@ApiField("pay_order_no")
	private String payOrderNo;

	/**
	 * 支付手续费, 单位:元, 2位小数
	 */
	@ApiField("pay_rate")
	private String payRate;

	/**
	 * 订单支付状态, -1:全部, 0:未支付, 1:已支付, 2:已退款
	 */
	@ApiField("pay_status")
	private String payStatus;

	/**
	 *  买家付款时间格式：yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("pay_time")
	private String payTime;

	/**
	 * 支付方式编号
	 */
	@ApiField("pay_type_id")
	private String payTypeId;

	/**
	 * 支付方式名称
	 */
	@ApiField("pay_type_name")
	private String payTypeName;

	/**
	 * 订单实付金额, 单位:元, 2位小数
	 */
	@ApiField("payment")
	private String payment;

	/**
	 * 物流总额, 单位:元, 2位小数
	 */
	@ApiField("post_fee")
	private String postFee;

	/**
	 * 详细地址
	 */
	@ApiField("reciver_address")
	private String reciverAddress;

	/**
	 * 收件人所在城市
	 */
	@ApiField("reciver_city")
	private String reciverCity;

	/**
	 * 收件人所在区县
	 */
	@ApiField("reciver_district")
	private String reciverDistrict;

	/**
	 * 收件人手机
	 */
	@ApiField("reciver_mobile")
	private String reciverMobile;

	/**
	 * 收件人姓名
	 */
	@ApiField("reciver_name")
	private String reciverName;

	/**
	 * 收件人电话
	 */
	@ApiField("reciver_phone")
	private String reciverPhone;

	/**
	 * 收件人所在省份
	 */
	@ApiField("reciver_state")
	private String reciverState;

	/**
	 * 收件地址邮编
	 */
	@ApiField("reciver_zip")
	private String reciverZip;

	/**
	 * 订单奖励积分
	 */
	@ApiField("reward_integral")
	private String rewardIntegral;

	/**
	 * 销售人员编号
	 */
	@ApiField("sale_user_id")
	private String saleUserId;

	/**
	 * 销售人员姓名
	 */
	@ApiField("sale_user_name")
	private String saleUserName;

	/**
	 * 卖家编号
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 卖家昵称
	 */
	@ApiField("seller_nick_name")
	private String sellerNickName;

	/**
	 * 卖家发货地址
	 */
	@ApiField("sender_address")
	private String senderAddress;

	/**
	 * 交易的发货方式编号
	 */
	@ApiField("ship_type_id")
	private String shipTypeId;

	/**
	 * 交易的发货方式名称
	 */
	@ApiField("ship_type_name")
	private String shipTypeName;

	/**
	 * 站点分类，1：云订货（D2P），2：云商城（D2C）
	 */
	@ApiField("site")
	private Integer site;

	/**
	 * 订单编号, T开头
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 订单自动确认收货时间, 单位:天, 时间从卖家已发货开始计算, 默认10
	 */
	@ApiField("timeout_action_time")
	private Integer timeoutActionTime;

	/**
	 * 商品金额(商品单价*数量), 单位:元, 2位小数
	 */
	@ApiField("total_fee")
	private String totalFee;

	/**
	 * 订单交易总额, 商品金额(total_fee) - 优惠金额(discount_fee) + 物流总额(post_fee), 单位:元, 2位小数
	 */
	@ApiField("total_trade_fee")
	private String totalTradeFee;

	/**
	 * 订单类型:0自营 ，1代销
	 */
	@ApiField("trade_flag")
	private Integer tradeFlag;

	/**
	 * 订单应付金额，订单总额(total_trade_fee) - 积分抵消金额(integral_fee), 单位:元, 2位小数
	 */
	@ApiField("trade_pay_fee")
	private String tradePayFee;

	/**
	 * 会员类型，1:C会员，4:B会员
	 */
	@ApiField("user_type")
	private String userType;

	public String getAddUserId() {
		return this.addUserId;
	}
	public void setAddUserId(String addUserId) {
		this.addUserId = addUserId;
	}

	public String getAddUserName() {
		return this.addUserName;
	}
	public void setAddUserName(String addUserName) {
		this.addUserName = addUserName;
	}

	public String getAreaId() {
		return this.areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getBuyerMessage() {
		return this.buyerMessage;
	}
	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getBuyerNick() {
		return this.buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getBuyerNickName() {
		return this.buyerNickName;
	}
	public void setBuyerNickName(String buyerNickName) {
		this.buyerNickName = buyerNickName;
	}

	public String getCompleteStatus() {
		return this.completeStatus;
	}
	public void setCompleteStatus(String completeStatus) {
		this.completeStatus = completeStatus;
	}

	public String getConsignTime() {
		return this.consignTime;
	}
	public void setConsignTime(String consignTime) {
		this.consignTime = consignTime;
	}

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public String getDeliverStatus() {
		return this.deliverStatus;
	}
	public void setDeliverStatus(String deliverStatus) {
		this.deliverStatus = deliverStatus;
	}

	public String getDiscountFee() {
		return this.discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}

	public String getEndTime() {
		return this.endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getFlowStatus() {
		return this.flowStatus;
	}
	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}

	public String getIntegral() {
		return this.integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getIntegralFee() {
		return this.integralFee;
	}
	public void setIntegralFee(String integralFee) {
		this.integralFee = integralFee;
	}

	public String getInvoiceFlag() {
		return this.invoiceFlag;
	}
	public void setInvoiceFlag(String invoiceFlag) {
		this.invoiceFlag = invoiceFlag;
	}

	public String getInvoiceTitle() {
		return this.invoiceTitle;
	}
	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getInvoiceType() {
		return this.invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public Boolean getIsShShip() {
		return this.isShShip;
	}
	public void setIsShShip(Boolean isShShip) {
		this.isShShip = isShShip;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMemo() {
		return this.memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getModified() {
		return this.modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	public Integer getNum() {
		return this.num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}

	public List<Order> getOrders() {
		return this.orders;
	}
	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public String getpShopName() {
		return this.pShopName;
	}
	public void setpShopName(String pShopName) {
		this.pShopName = pShopName;
	}

	public String getPayBankCode() {
		return this.payBankCode;
	}
	public void setPayBankCode(String payBankCode) {
		this.payBankCode = payBankCode;
	}

	public String getPayGateCode() {
		return this.payGateCode;
	}
	public void setPayGateCode(String payGateCode) {
		this.payGateCode = payGateCode;
	}

	public String getPayGateName() {
		return this.payGateName;
	}
	public void setPayGateName(String payGateName) {
		this.payGateName = payGateName;
	}

	public String getPayOrderNo() {
		return this.payOrderNo;
	}
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getPayRate() {
		return this.payRate;
	}
	public void setPayRate(String payRate) {
		this.payRate = payRate;
	}

	public String getPayStatus() {
		return this.payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayTime() {
		return this.payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getPayTypeId() {
		return this.payTypeId;
	}
	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeName() {
		return this.payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getPayment() {
		return this.payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getPostFee() {
		return this.postFee;
	}
	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}

	public String getReciverAddress() {
		return this.reciverAddress;
	}
	public void setReciverAddress(String reciverAddress) {
		this.reciverAddress = reciverAddress;
	}

	public String getReciverCity() {
		return this.reciverCity;
	}
	public void setReciverCity(String reciverCity) {
		this.reciverCity = reciverCity;
	}

	public String getReciverDistrict() {
		return this.reciverDistrict;
	}
	public void setReciverDistrict(String reciverDistrict) {
		this.reciverDistrict = reciverDistrict;
	}

	public String getReciverMobile() {
		return this.reciverMobile;
	}
	public void setReciverMobile(String reciverMobile) {
		this.reciverMobile = reciverMobile;
	}

	public String getReciverName() {
		return this.reciverName;
	}
	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	public String getReciverPhone() {
		return this.reciverPhone;
	}
	public void setReciverPhone(String reciverPhone) {
		this.reciverPhone = reciverPhone;
	}

	public String getReciverState() {
		return this.reciverState;
	}
	public void setReciverState(String reciverState) {
		this.reciverState = reciverState;
	}

	public String getReciverZip() {
		return this.reciverZip;
	}
	public void setReciverZip(String reciverZip) {
		this.reciverZip = reciverZip;
	}

	public String getRewardIntegral() {
		return this.rewardIntegral;
	}
	public void setRewardIntegral(String rewardIntegral) {
		this.rewardIntegral = rewardIntegral;
	}

	public String getSaleUserId() {
		return this.saleUserId;
	}
	public void setSaleUserId(String saleUserId) {
		this.saleUserId = saleUserId;
	}

	public String getSaleUserName() {
		return this.saleUserName;
	}
	public void setSaleUserName(String saleUserName) {
		this.saleUserName = saleUserName;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getSellerNickName() {
		return this.sellerNickName;
	}
	public void setSellerNickName(String sellerNickName) {
		this.sellerNickName = sellerNickName;
	}

	public String getSenderAddress() {
		return this.senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getShipTypeId() {
		return this.shipTypeId;
	}
	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}

	public String getShipTypeName() {
		return this.shipTypeName;
	}
	public void setShipTypeName(String shipTypeName) {
		this.shipTypeName = shipTypeName;
	}

	public Integer getSite() {
		return this.site;
	}
	public void setSite(Integer site) {
		this.site = site;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public Integer getTimeoutActionTime() {
		return this.timeoutActionTime;
	}
	public void setTimeoutActionTime(Integer timeoutActionTime) {
		this.timeoutActionTime = timeoutActionTime;
	}

	public String getTotalFee() {
		return this.totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getTotalTradeFee() {
		return this.totalTradeFee;
	}
	public void setTotalTradeFee(String totalTradeFee) {
		this.totalTradeFee = totalTradeFee;
	}

	public Integer getTradeFlag() {
		return this.tradeFlag;
	}
	public void setTradeFlag(Integer tradeFlag) {
		this.tradeFlag = tradeFlag;
	}

	public String getTradePayFee() {
		return this.tradePayFee;
	}
	public void setTradePayFee(String tradePayFee) {
		this.tradePayFee = tradePayFee;
	}

	public String getUserType() {
		return this.userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

}