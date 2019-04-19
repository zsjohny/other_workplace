package com.jiuyuan.entity.newentity;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 地推人员提现申请表
 * </p>
 *
 * @author 赵兴林
 * @since 2017-11-15
 */
@TableName("ground_withdraw_cash_record")
public class GroundWithdrawCashRecord extends Model<GroundWithdrawCashRecord> {

    private static final long serialVersionUID = 1L;
    
    
    //地推人员提现处理状态：提现申请状态：0(处理中)、1(处理完成)、2(已拒绝)、3(已冻结)
    public static final Integer WITHDRAWCASHSTATUS_DOING = 0;
    public static final Integer WITHDRAWCASHSTATUS_FINISHED = 1;
    public static final Integer WITHDRAWCASHSTATUS_REFUSED = 2;
    public static final Integer WITHDRAWCASHSTATUS_FREEZE  = 3;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 地推人员id
     */
	@TableField("ground_user_id")
	private Long groundUserId;
    /**
     * 地推人员手机号
     */
	@TableField("ground_user_phone")
	private String groundUserPhone;
    /**	
     * 地推人员姓名
     */
	@TableField("ground_user_name")
	private String groundUserName;
    /**
     * 打款人用户ID
     */
	@TableField("admin_id")
	private Long adminId;
    /**
     * 申请单号
     */
	@TableField("apply_no")
	private String applyNo;
    /**
     * 提现申请状态：0(处理中)、1(处理完成)、2(已拒绝)、3(已冻结)
     */
	@TableField("withdraw_cash_status")
	private Integer withdrawCashStatus;
	 /**
     * 提现类型：0(主动申请提现)、1(系统自动提现)
     */
	@TableField("withdraw_type")
	private Integer withdrawType;
    /**
     * 交易金额
     */
	@TableField("transaction_amount")
	private Double transactionAmount;
    /**
     * 提现金额
     */
	@TableField("withdraw_cash_amount")
	private Double withdrawCashAmount;
    /**
     * 交易编号
     */
	@TableField("transaction_no")
	private String transactionNo;
    /**
     * 申请时间
     */
	@TableField("apply_time")
	private Long applyTime;
    /**
     * 确认打款时间
     */
	@TableField("finish_time")
	private Long finishTime;
    /**
     * 打款说明
     */
	private String remark;
    /**
     *  交易方式：0（银行汇款）
     */
	@TableField("transaction_way")
	private Integer transactionWay;
    /**
     * 银行卡号
     */
	@TableField("bank_account_no")
	private String bankAccountNo;
    /**
     * 银行户名
     */
	@TableField("bank_account_name")
	private String bankAccountName;
    /**
     * 开户银行
     */
	@TableField("bank_name")
	private String bankName;
    /**
     * 拒绝原因
     */
	@TableField("refuse_reason")
	private String refuseReason;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getGroundUserId() {
		return groundUserId;
	}

	public void setGroundUserId(Long groundUserId) {
		this.groundUserId = groundUserId;
	}

	public String getGroundUserPhone() {
		return groundUserPhone;
	}

	public void setGroundUserPhone(String groundUserPhone) {
		this.groundUserPhone = groundUserPhone;
	}

	public String getGroundUserName() {
		return groundUserName;
	}

	public void setGroundUserName(String groundUserName) {
		this.groundUserName = groundUserName;
	}

	public Long getAdminId() {
		return adminId;
	}

	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}

	public String getApplyNo() {
		return applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	public Integer getWithdrawCashStatus() {
		return withdrawCashStatus;
	}

	public void setWithdrawCashStatus(Integer withdrawCashStatus) {
		this.withdrawCashStatus = withdrawCashStatus;
	}

	public Integer getWithdrawType() {
		return withdrawType;
	}

	public void setWithdrawType(Integer withdrawType) {
		this.withdrawType = withdrawType;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Double getWithdrawCashAmount() {
		return withdrawCashAmount;
	}

	public void setWithdrawCashAmount(Double withdrawCashAmount) {
		this.withdrawCashAmount = withdrawCashAmount;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Long applyTime) {
		this.applyTime = applyTime;
	}

	public Long getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Long finishTime) {
		this.finishTime = finishTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getTransactionWay() {
		return transactionWay;
	}

	public void setTransactionWay(Integer transactionWay) {
		this.transactionWay = transactionWay;
	}

	public String getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRefuseReason() {
		return refuseReason;
	}

	public void setRefuseReason(String refuseReason) {
		this.refuseReason = refuseReason;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
