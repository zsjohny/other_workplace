package com.finace.miscroservice.user.po;


public class AccountCashPO {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1492837924645111016L;

	private int id;

	/**
     * account_cash.user_id
     * 用户ID
     */
    private int userId;

    /**
     * account_cash.status
     * 状态
     */
    private int status;

    /**
     * account_cash.account
     * 账号
     */
    private String account;

    /**
     * account_cash.bank
     * 所属银行
     */
    private String bank;

    /**
     * account_cash.branch
     * 支行
     */
    private String branch;

    /**
     * account_cash.total
     * ܶ
     */
    private String total;

    /**
     * account_cash.credited
     * 到账总额
     */
    private String credited;

    /**
     * account_cash.fee
     * 手续费
     */
    private String fee;

    /**
     * account_cash.verify_userid
     * 
     */
    private Long verifyUserid;

    /**
     * account_cash.verify_time
     * 
     */
    private String verifyTime;

    /**
     * account_cash.verify_remark
     * 
     */
    private String verifyRemark;

    /**
     * account_cash.addtime
     * 
     */
    private String addtime;

    /**
     * account_cash.addip
     * 
     */
    private String addip;

    /**
     * account_cash.hongbao
     * 
     */
    private String hongbao;
    
    
    private String trustTradeNo;
    
    private Integer trustStatus;
    
    private Integer version;
   
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCredited() {
        return credited;
    }

    public void setCredited(String credited) {
        this.credited = credited;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Long getVerifyUserid() {
        return verifyUserid;
    }

    public void setVerifyUserid(Long verifyUserid) {
        this.verifyUserid = verifyUserid;
    }

    public String getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(String verifyTime) {
        this.verifyTime = verifyTime;
    }

    public String getVerifyRemark() {
        return verifyRemark;
    }

    public void setVerifyRemark(String verifyRemark) {
        this.verifyRemark = verifyRemark;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getAddip() {
        return addip;
    }

    public void setAddip(String addip) {
        this.addip = addip;
    }

    public String getHongbao() {
        return hongbao;
    }

    public void setHongbao(String hongbao) {
        this.hongbao = hongbao;
    }

    public String getTrustTradeNo() {
        return trustTradeNo;
    }

    public void setTrustTradeNo(String trustTradeNo) {
        this.trustTradeNo = trustTradeNo;
    }

    public Integer getTrustStatus() {
        return trustStatus;
    }

    public void setTrustStatus(Integer trustStatus) {
        this.trustStatus = trustStatus;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}