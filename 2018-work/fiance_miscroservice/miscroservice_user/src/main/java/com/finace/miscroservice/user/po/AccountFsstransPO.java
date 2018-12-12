package com.finace.miscroservice.user.po;

/**
 * 生利宝实体类
 */
public class AccountFsstransPO {

    private int id;

    private int userId;

    /**更新数据标示*/
    private int status;
    /**生利宝余额*/
    private double total;

    /**生利宝昨天收益*/
    private double yesterdayInterest;

    /**生利宝累计收益*/
    private String totalInterest;

    /**生利宝昨天收益率*/
    private String yesterdayApr;

    /**生利宝最近7天收益率*/
    private String lastSevenDayApr;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getYesterdayInterest() {
        return yesterdayInterest;
    }

    public void setYesterdayInterest(double yesterdayInterest) {
        this.yesterdayInterest = yesterdayInterest;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getYesterdayApr() {
        return yesterdayApr;
    }

    public void setYesterdayApr(String yesterdayApr) {
        this.yesterdayApr = yesterdayApr;
    }

    public String getLastSevenDayApr() {
        return lastSevenDayApr;
    }

    public void setLastSevenDayApr(String lastSevenDayApr) {
        this.lastSevenDayApr = lastSevenDayApr;
    }
}
