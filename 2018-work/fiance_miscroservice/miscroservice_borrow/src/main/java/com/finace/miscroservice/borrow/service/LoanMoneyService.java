package com.finace.miscroservice.borrow.service;


/**
 * 放款
 */
public interface LoanMoneyService {


    /**
     * 自动放款
     * @param borrowId
     */
    public void LoanMoney(String borrowId);


    /**
     * 自动上标
     * @param borrowGroup
     */
    public void AutoUpBorrow(String borrowGroup);


    /**
     * 给用户发现金红包
     * @param toAccountId
     * @param hbMoney
     */
    public void doGrantHb(String toAccountId, String hbMoney);


    /**
     * 自动上标  协议支付  富有
     * @param group
     */
    void AutoUpAgreeBorrow(String group);
}
