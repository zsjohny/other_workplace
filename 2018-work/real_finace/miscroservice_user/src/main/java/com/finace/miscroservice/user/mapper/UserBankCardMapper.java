package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.UserBankCardPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 *
 */
@Mapper
public interface UserBankCardMapper {

    /**
     * 根据用户id获取用户银行卡信息
     * @param userId
     * @return
     */
    UserBankCardPO getUserEnableCardByUserId(@Param("userId") String userId);

    /**
     *根据卡号获取用户银行卡信息
     * @param card
     * @return
     */
    UserBankCardPO getBaknCardByCard(@Param("card") String card);

    /**
     * 根据用户id获取用户协议支付银行卡信息
     * @param userId
     * @return
     */
    UserBankCardPO getAgreeEnableCardByUserId(@Param("userId") String userId);

    /**
     *根据卡号获取用户协议支付银行卡信息
     * @param card
     * @return
     */
    UserBankCardPO getAgreeBaknCardByCard(@Param("card") String card);

    /**
     * 根据id获取银行卡信息
     * @param cardId
     * @return
     */
    UserBankCardPO getUserEnableCardById(@Param("cardId") String cardId);

    /**
     * 新增用户银行卡号
     * @param userBankCardPO
     * @return
     */
    int addUserBankCard(UserBankCardPO userBankCardPO);

}
