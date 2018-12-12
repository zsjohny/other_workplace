package com.finace.miscroservice.borrow.po;

import com.finace.miscroservice.commons.entity.BasePage;

import java.math.BigDecimal;
import java.util.Date;

public class FinanceBidPO  extends BasePage {
	private static final long serialVersionUID = 2113768056485101909L;
	protected Date gmtCreate;  //发起支付时间
	protected Date gmtWrite;  //支付回调时间
	protected String createUid;
	protected String writeUid;
	protected String ownerId;

	private  int id;
	private String orderSn;// 订单编号
	private String type; // d:定期
	private int borrowId;// 标的ID
	private int userId;
	private int pay;
	private String payChannel;// fuiou:富友支付, v:虚拟, offline:线下支付
	private Date payTime;// 支付时间
	private String channel; // 推广
	private BigDecimal buyAmt;// 元
	private BigDecimal rate;// 利率 10% --> 10.00
	private BigDecimal withdrawProfit;// 提现收益
	private BigDecimal withdrawPrincipal;// 提现本金
	private BigDecimal couponRate;// 加息券额度
	private BigDecimal couponAmt;// 现金券额度
	private String payName; // 支付者名字
	private String payPid; // 支付者身份证
	private String bankCardNo; // 支付者身行卡号
	private String bankNo; // 支付者银行代码
	private String bankName;// 银行的名字
	private Date beginProfit; // 开始计算收益时间
	private Date endProfit; // 结速计算收益时间
	private Date gmtWithdraw; // 回款时间
	private String summary;
	private String status;// new , counting:计息 , repayment, draft
	private Date repayTime;//还款时间
	private String regChannel;//注册渠道
	private String hbid; //福利劵id
	private String contractId;//云合同的合同id
	
	//以下是辅助属性
	private String borrow_name; //标名称
	private Double interest; //待收利息
	private Double account;//标的金额
	private int releaseType;//标发布种类 10--汇付 20--富友
	private Double money;
	private String addtime;
	private String dqr;
	private String qxr;
	private String tender_time;
	private String apr;
	private String repaymentYesinterest;

	private BigDecimal principal;
	private BigDecimal nowProfit;

	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getBorrowId() {
		return borrowId;
	}

	public void setBorrowId(int borrowId) {
		this.borrowId = borrowId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}


	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public BigDecimal getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(BigDecimal buyAmt) {
		this.buyAmt = buyAmt;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public BigDecimal getWithdrawProfit() {
		return withdrawProfit;
	}

	public void setWithdrawProfit(BigDecimal withdrawProfit) {
		this.withdrawProfit = withdrawProfit;
	}

	public BigDecimal getWithdrawPrincipal() {
		return withdrawPrincipal;
	}

	public void setWithdrawPrincipal(BigDecimal withdrawPrincipal) {
		this.withdrawPrincipal = withdrawPrincipal;
	}

	public BigDecimal getCouponRate() {
		return couponRate;
	}

	public void setCouponRate(BigDecimal couponRate) {
		this.couponRate = couponRate;
	}

	public BigDecimal getCouponAmt() {
		return couponAmt;
	}

	public void setCouponAmt(BigDecimal couponAmt) {
		this.couponAmt = couponAmt;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}

	public String getPayPid() {
		return payPid;
	}

	public void setPayPid(String payPid) {
		this.payPid = payPid;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Date getBeginProfit() {
		return beginProfit;
	}

	public void setBeginProfit(Date beginProfit) {
		this.beginProfit = beginProfit;
	}

	public Date getEndProfit() {
		return endProfit;
	}

	public void setEndProfit(Date endProfit) {
		this.endProfit = endProfit;
	}

	public Date getGmtWithdraw() {
		return gmtWithdraw;
	}

	public void setGmtWithdraw(Date gmtWithdraw) {
		this.gmtWithdraw = gmtWithdraw;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getInterest() {
		return interest;
	}

	public void setInterest(Double interest) {
		this.interest = interest;
	}

	public Double getAccount() {
		return account;
	}

	public String getRegChannel() {
		return regChannel;
	}

	public void setRegChannel(String regChannel) {
		this.regChannel = regChannel;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public int getReleaseType() {
		return releaseType;
	}

	public void setReleaseType(int releaseType) {
		this.releaseType = releaseType;
	}

	public String getBorrow_name() {
		return borrow_name;
	}

	public void setBorrow_name(String borrow_name) {
		this.borrow_name = borrow_name;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getAddtime() {
		return addtime;
	}

	public String getDqr() {
		return dqr;
	}

	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}

	public void setDqr(String dqr) {
		this.dqr = dqr;
	}

	public Date getRepayTime() {
		return repayTime;
	}

	public void setRepayTime(Date repayTime) {
		this.repayTime = repayTime;
	}

	public String getTender_time() {
		return tender_time;
	}

	public void setTender_time(String tender_time) {
		this.tender_time = tender_time;
	}

	public String getApr() {
		return apr;
	}

	public void setApr(String apr) {
		this.apr = apr;
	}

	public String getRepaymentYesinterest() {
		return repaymentYesinterest;
	}

	public void setRepaymentYesinterest(String repaymentYesinterest) {
		this.repaymentYesinterest = repaymentYesinterest;
	}

	public String getHbid() {
		return hbid;
	}

	public void setHbid(String hbid) {
		this.hbid = hbid;
	}

	public String getQxr() {
		return qxr;
	}

	public void setQxr(String qxr) {
		this.qxr = qxr;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtWrite() {
		return gmtWrite;
	}

	public void setGmtWrite(Date gmtWrite) {
		this.gmtWrite = gmtWrite;
	}

	public String getCreateUid() {
		return createUid;
	}

	public void setCreateUid(String createUid) {
		this.createUid = createUid;
	}

	public String getWriteUid() {
		return writeUid;
	}

	public void setWriteUid(String writeUid) {
		this.writeUid = writeUid;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public BigDecimal getPrincipal() {
		return principal;
	}

	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}

	public BigDecimal getNowProfit() {
		return nowProfit;
	}

	public void setNowProfit(BigDecimal nowProfit) {
		this.nowProfit = nowProfit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPay() {
		return pay;
	}

	public void setPay(int pay) {
		this.pay = pay;
	}
}
