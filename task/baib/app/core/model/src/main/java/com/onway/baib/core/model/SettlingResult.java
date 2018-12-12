package com.onway.baib.core.model;

/**
 * 待到账
 * 
 * @author jiaming.zhu
 * @version $Id: SettlingResult.java, v 0.1 2017年2月8日 上午10:02:06 ZJM Exp $
 */
public class SettlingResult {
    //结算Id
    private String settleId;
    //提交结算日期
    private String settleDate;
    //提交结算时间
    private String settleTime;
    //结算总额
    private String totalAmount;
    //订单数量
    private int    ordersNum;

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public String getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(String settleTime) {
        this.settleTime = settleTime;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getOrdersNum() {
        return ordersNum;
    }

    public void setOrdersNum(int ordersNum) {
        this.ordersNum = ordersNum;
    }

}
