package com.finace.miscroservice.user.service;

import com.finace.miscroservice.user.po.UserBankCardPO;

/**
 *
 */
public interface UserBankCardService {

    /**
     * 根据用户id获取用户银行卡信息
     * @param userId
     * @return
     */
    public UserBankCardPO getUserEnableCardByUserId(String userId);

    /**
     *根据卡号获取用户银行卡信息
     * @param card
     * @return
     */
    public UserBankCardPO getBaknCardByCard(String card);

    /**
     * 根据id获取银行卡信息
     * @param cardId
     * @return
     */
    public UserBankCardPO getUserEnableCardById(String cardId);

    /**
     *
     * @param userBankCardPO
     */
    public void addUserBankCard(UserBankCardPO userBankCardPO);

    /**
     * 根据用户id获取用户协议支付银行卡信息
     * @param userId
     * @return
     */
    public UserBankCardPO getAgreeEnableCardByUserId(String userId);

    /**
     *根据卡号获取用户协议支付银行卡信息
     * @param card
     * @return
     */
    public UserBankCardPO getAgreeBaknCardByCard(String card);
}
