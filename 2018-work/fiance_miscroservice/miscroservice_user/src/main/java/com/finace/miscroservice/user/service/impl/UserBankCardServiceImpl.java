package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.user.dao.UserBankCardDao;
import com.finace.miscroservice.user.po.UserBankCardPO;
import com.finace.miscroservice.user.service.UserBankCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserBankCardServiceImpl implements UserBankCardService {

    @Autowired
    private UserBankCardDao userBankCardDao;

    @Override
    public UserBankCardPO getUserEnableCardByUserId(String userId) {
        return userBankCardDao.getUserEnableCardByUserId(userId);
    }

    @Override
    public UserBankCardPO getBaknCardByCard(String card) {
        return userBankCardDao.getBaknCardByCard(card);
    }

    @Override
    public UserBankCardPO getUserEnableCardById(String cardId) {
        return userBankCardDao.getUserEnableCardById(cardId);
    }

    @Override
    public void addUserBankCard(UserBankCardPO userBankCardPO) {
        userBankCardDao.addUserBankCard(userBankCardPO);
    }

    @Override
    public UserBankCardPO getAgreeEnableCardByUserId(String userId) {
        return userBankCardDao.getAgreeEnableCardByUserId(userId);
    }

    @Override
    public UserBankCardPO getAgreeBaknCardByCard(String card) {
        return userBankCardDao.getAgreeBaknCardByCard(card);
    }
}
