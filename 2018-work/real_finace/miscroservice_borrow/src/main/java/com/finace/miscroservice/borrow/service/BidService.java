package com.finace.miscroservice.borrow.service;

import com.finace.miscroservice.commons.utils.Response;

import javax.servlet.http.HttpServletResponse;

/**
 * 投标
 */
public interface BidService {


    /**
     * 投标
     * @param userId
     * @param amt
     * @param borrowId
     * @param hbid
     * @param channel
     * @return
     */
    Response creditPay(String userId,String amt, Integer borrowId, String hbid, String channel, HttpServletResponse response);

    /**
     * 投标
     * @param bgData
     * @return
     */
    void creditPayNotify(String bgData, HttpServletResponse response);


    /**
     * 放款 合法性校验通知链接
     *
     * @param bgData
     * @return
     */
    void loanMoneyNotify(String bgData, HttpServletResponse response);


    /**
     * 放款 业务结果通知
     * @param bgData
     * @return
     */
    void loanMoneyRetNotify(String bgData, HttpServletResponse response);







}
