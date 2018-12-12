package com.finace.miscroservice.commons.entity;

public class BorrowTender {

    private double  accountPast; //总额
    private double interestPast; //收益
    private int  repayTimes ;  //回款次数
    public int getRepayTimes() {
        return repayTimes;
    }

    public void setRepayTimes(int repayTimes) {
        this.repayTimes = repayTimes;
    }

    public double getAccountPast() {
        return accountPast;
    }

    public void setAccountPast(double accountPast) {
        this.accountPast = accountPast;
    }

    public double getInterestPast() {
        return interestPast;
    }

    public void setInterestPast(double interestPast) {
        this.interestPast = interestPast;
    }
}
