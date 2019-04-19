package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 商家提现申请审批表
 * </p>
 *
 * @author nijin
 * @since 2017-10-16
 */
@TableName("yjj_WithdrawApply")
public class WithdrawApplyNew extends Model<WithdrawApplyNew> {

    private static final long serialVersionUID = 1L;
    
    public static final int TYPE_SUPPLIER = 1;
    
    //状态 0 未处理 1 交易完成 2 已拒绝 3 已冻结
    public static final int NO_DEAL = 0;
    public static final int DEAL_SUCCESS = 1;
    public static final int REFUSEED = 2;
    public static final int FREEZED = 3;

	@TableId(value="Id", type= IdType.AUTO)
	private Long Id;
    /**
     * 门店、品牌商家号
     */
	private Long RelatedId;
    /**
     * 订单编号
     */
	private Long TradeId;
    /**
     * 创建时间
     */
	private Long CreateTime;
	private Long UpdateTime;
    /**
     * 状态 0 未处理 1 交易完成 2 已拒绝 3 已冻结
     */
	private Integer Status;
    /**
     * 商家类型 0 门店 1 品牌货款 2 品牌物流 3 品牌
     */
	private Integer Type;
    /**
     * 申请金额
     */
	private Double ApplyMoney;
    /**
     * 交易金额
     */
	private Double Money;
    /**
     * 财务退款方式 1:支付宝 3:微信 5:银行汇款
     */
	private Integer TradeWay;
    /**
     * 交易关联人姓名
     */
	private String TradeName;
    /**
     * 交易账号
     */
	private String TradeAccount;
    /**
     * 交易银行名称
     */
	private String TradeBankName;
    /**
     * 处理时间
     */
	private Long DealTime;
    /**
     * 打款说明
     */
	private String Remark;
    /**
     * 新交易编号
     */
	private String TradeNo;
	/**
	 * 冻结时间
	 */
	@TableField("freeze_time")
	private Long freezeTime;
	/**
	 * 操作人的ID
	 */
	@TableField("admin_id")
	private Long adminId;
	


	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public Long getFreezeTime() {
		return freezeTime;
	}

	public void setFreezeTime(Long freezeTime) {
		this.freezeTime = freezeTime;
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long Id) {
		this.Id = Id;
	}

	public Long getRelatedId() {
		return RelatedId;
	}

	public void setRelatedId(Long RelatedId) {
		this.RelatedId = RelatedId;
	}

	public Long getTradeId() {
		return TradeId;
	}

	public void setTradeId(Long TradeId) {
		this.TradeId = TradeId;
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

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer Status) {
		this.Status = Status;
	}

	public Integer getType() {
		return Type;
	}

	public void setType(Integer Type) {
		this.Type = Type;
	}

	public Double getApplyMoney() {
		return ApplyMoney;
	}

	public void setApplyMoney(Double ApplyMoney) {
		this.ApplyMoney = ApplyMoney;
	}

	public Double getMoney() {
		return Money;
	}

	public void setMoney(Double Money) {
		this.Money = Money;
	}

	public Integer getTradeWay() {
		return TradeWay;
	}

	public void setTradeWay(Integer TradeWay) {
		this.TradeWay = TradeWay;
	}

	public String getTradeName() {
		return TradeName;
	}

	public void setTradeName(String TradeName) {
		this.TradeName = TradeName;
	}

	public String getTradeAccount() {
		return TradeAccount;
	}

	public void setTradeAccount(String TradeAccount) {
		this.TradeAccount = TradeAccount;
	}

	public String getTradeBankName() {
		return TradeBankName;
	}

	public void setTradeBankName(String TradeBankName) {
		this.TradeBankName = TradeBankName;
	}

	public Long getDealTime() {
		return DealTime;
	}

	public void setDealTime(Long DealTime) {
		this.DealTime = DealTime;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String Remark) {
		this.Remark = Remark;
	}

	public String getTradeNo() {
		return TradeNo;
	}

	public void setTradeNo(String TradeNo) {
		this.TradeNo = TradeNo;
	}

	@Override
	protected Serializable pkVal() {
		return this.Id;
	}

}
