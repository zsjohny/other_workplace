package com.finace.miscroservice.user.entity.response;

import com.finace.miscroservice.user.entity.BaseEntity;


/**
 * Created by hyf on 2018/3/7.
 */
public class MyPropertyResponse extends BaseEntity {
    private Double amountMoney;//总资产
    private Double amountWaitGetMoney; //总待收
    private Double frozenAmount; //冻结金额
    private Double cumulativeInvestmentAmount;//累计投资金额
    private Double accumulatedIncome;//累计收益
    private Double waitPrincipal;//待收本金
    private Double waitProfit;//待收利息

    public MyPropertyResponse() {
    }

    public MyPropertyResponse(Double amountMoney, Double amountWaitGetMoney, Double frozenAmount, Double cumulativeInvestmentAmount, Double accumulatedIncome, Double waitPrincipal, Double waitProfit) {
        this.amountMoney = amountMoney;
        this.amountWaitGetMoney = amountWaitGetMoney;
        this.frozenAmount = frozenAmount;
        this.cumulativeInvestmentAmount = cumulativeInvestmentAmount;
        this.accumulatedIncome = accumulatedIncome;
        this.waitPrincipal = waitPrincipal;
        this.waitProfit = waitProfit;
    }

    public Double getAmountMoney() {
        return amountMoney;
    }

    public void setAmountMoney(Double amountMoney) {
        this.amountMoney = amountMoney;
    }

    public Double getAmountWaitGetMoney() {
        return amountWaitGetMoney;
    }

    public void setAmountWaitGetMoney(Double amountWaitGetMoney) {
        this.amountWaitGetMoney = amountWaitGetMoney;
    }

    public Double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Double getCumulativeInvestmentAmount() {
        return cumulativeInvestmentAmount;
    }

    public void setCumulativeInvestmentAmount(Double cumulativeInvestmentAmount) {
        this.cumulativeInvestmentAmount = cumulativeInvestmentAmount;
    }

    public Double getAccumulatedIncome() {
        return accumulatedIncome;
    }

    public void setAccumulatedIncome(Double accumulatedIncome) {
        this.accumulatedIncome = accumulatedIncome;
    }

    public Double getWaitPrincipal() {
        return waitPrincipal;
    }

    public void setWaitPrincipal(Double waitPrincipal) {
        this.waitPrincipal = waitPrincipal;
    }

    public Double getWaitProfit() {
        return waitProfit;
    }

    public void setWaitProfit(Double waitProfit) {
        this.waitProfit = waitProfit;
    }
}
