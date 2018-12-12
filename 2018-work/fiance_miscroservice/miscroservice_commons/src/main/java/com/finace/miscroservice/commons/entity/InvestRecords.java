package com.finace.miscroservice.commons.entity;

/**
 * 投资记录实体类
 */
public class InvestRecords extends  BasePage{

    /**
     * 标的名称
     */
    private String borrowName;
    /**
     * 标的id
     */
    private String borrowId;
    /**
     * 新增时间
     */
    private String addtime;
    /**
     * 新增时间
     */
    private String tenderTime;
    /**
     * 投资金额
     */
    private String money;
    /**
     * 预收利息
     */
    private String interest;
    /**
     * 到期日
     */
    private String dqr;
    /**
     * 还款利息
     */
    private String repaymentYesinterest;

    private String account;
    private String status;
    private String releaseType;
    private String id;
    private String apr;

    private String hbmoney; //福利券金额
    private String hbtype; //福利券类型
    private String hbid;//福利券id
    private String limitDay; //项目天数
    private String qxr; //起息日
    private String ocinvestment;//投资本金

    private String token;//查看合同需要的token
    private String contractId;//查看合同的id
    private String userId; //借款人id

    private String bmoney;     //借款人资金运用情况
    private String manage;    //借款人经营状况
    private String finance;   //借款人财务状况
    private String repayment; //借款人还款能力变化
    private String overdue;   //借款人逾期情况
    private String appeal;    //借款人涉诉情况
    private String punish;    //借款人受行政处罚情况
    private String track;     //追踪更新时间

    private String repaymentType;

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public String getBmoney() {
        return bmoney;
    }

    public void setBmoney(String bmoney) {
        this.bmoney = bmoney;
    }

    public String getManage() {
        return manage;
    }

    public void setManage(String manage) {
        this.manage = manage;
    }

    public String getFinance() {
        return finance;
    }

    public void setFinance(String finance) {
        this.finance = finance;
    }

    public String getRepayment() {
        return repayment;
    }

    public void setRepayment(String repayment) {
        this.repayment = repayment;
    }

    public String getOverdue() {
        return overdue;
    }

    public void setOverdue(String overdue) {
        this.overdue = overdue;
    }

    public String getAppeal() {
        return appeal;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public String getPunish() {
        return punish;
    }

    public void setPunish(String punish) {
        this.punish = punish;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDqr() {
        return dqr;
    }

    public void setDqr(String dqr) {
        this.dqr = dqr;
    }

    public String getRepaymentYesinterest() {
        return repaymentYesinterest;
    }

    public void setRepaymentYesinterest(String repaymentYesinterest) {
        this.repaymentYesinterest = repaymentYesinterest;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApr() {
        return apr;
    }

    public void setApr(String apr) {
        this.apr = apr;
    }

    public String getHbmoney() {
        return hbmoney;
    }

    public void setHbmoney(String hbmoney) {
        this.hbmoney = hbmoney;
    }

    public String getHbtype() {
        return hbtype;
    }

    public void setHbtype(String hbtype) {
        this.hbtype = hbtype;
    }

    public String getLimitDay() {
        return limitDay;
    }

    public void setLimitDay(String limitDay) {
        this.limitDay = limitDay;
    }

    public String getQxr() {
        return qxr;
    }

    public void setQxr(String qxr) {
        this.qxr = qxr;
    }

    public String getBorrowName() {
        return borrowName;
    }

    public void setBorrowName(String borrowName) {
        this.borrowName = borrowName;
    }

    public String getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(String borrowId) {
        this.borrowId = borrowId;
    }

    public String getTenderTime() {
        return tenderTime;
    }

    public void setTenderTime(String tenderTime) {
        this.tenderTime = tenderTime;
    }

    public String getReleaseType() {
        return releaseType;
    }

    public void setReleaseType(String releaseType) {
        this.releaseType = releaseType;
    }

    public String getHbid() {
        return hbid;
    }

    public void setHbid(String hbid) {
        this.hbid = hbid;
    }

    public String getOcinvestment() {
        return ocinvestment;
    }

    public void setOcinvestment(String ocinvestment) {
        this.ocinvestment = ocinvestment;
    }
}
