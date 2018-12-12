package com.finace.miscroservice.user.service.impl;

import com.finace.miscroservice.user.dao.BorrowerInfoDao;
import com.finace.miscroservice.user.po.BorrowerInfoPO;
import com.finace.miscroservice.user.service.BorrowerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BorrowerInfoServiceImpl implements BorrowerInfoService{

    @Autowired
    private BorrowerInfoDao borrowerInfoDao;


    @Override
    public BorrowerInfoPO getBorrowerInfoByUserId(String userId) {
        return borrowerInfoDao.getBorrowerInfoByUserId(userId);
    }

    @Override
    public int addBorrowerInfo(BorrowerInfoPO borrowerInfoPO) {
        return borrowerInfoDao.addBorrowerInfo(borrowerInfoPO);
    }
}
