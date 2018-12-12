package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.UserBankCardDao;
import com.finace.miscroservice.user.mapper.UserBankCardMapper;
import com.finace.miscroservice.user.po.UserBankCardPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 */
@Component
public class UserBankCardDaoImpl implements UserBankCardDao {

     @Autowired
    private UserBankCardMapper userBankCardMapper;


    @Override
    public UserBankCardPO getUserEnableCardByUserId(String userId) {
        return userBankCardMapper.getUserEnableCardByUserId(userId);
    }

    @Override
    public UserBankCardPO getBaknCardByCard(String card) {
        return userBankCardMapper.getBaknCardByCard(card);
    }

    @Override
    public UserBankCardPO getUserEnableCardById(String cardId) {
        return userBankCardMapper.getUserEnableCardById(cardId);
    }

    @Override
    public void addUserBankCard(UserBankCardPO userBankCardPO) {
        userBankCardMapper.addUserBankCard(userBankCardPO);
    }

    @Override
    public UserBankCardPO getAgreeEnableCardByUserId(String userId) {
        return userBankCardMapper.getAgreeEnableCardByUserId(userId);
    }

    @Override
    public UserBankCardPO getAgreeBaknCardByCard(String card) {
        return userBankCardMapper.getAgreeBaknCardByCard(card);
    }
}
