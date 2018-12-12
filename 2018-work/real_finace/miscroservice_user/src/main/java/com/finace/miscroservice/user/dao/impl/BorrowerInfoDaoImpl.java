package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.BorrowerInfoDao;
import com.finace.miscroservice.user.mapper.BorrowInfoMapper;
import com.finace.miscroservice.user.po.BorrowerInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BorrowerInfoDaoImpl implements BorrowerInfoDao {

    @Autowired
    private BorrowInfoMapper borrowInfoMapper;


    @Override
    public BorrowerInfoPO getBorrowerInfoByUserId(String userId) {
        return borrowInfoMapper.getBorrowerInfoByUserId(userId);
    }

    @Override
    public int addBorrowerInfo(BorrowerInfoPO borrowerInfoPO) {
        return borrowInfoMapper.addBorrowerInfo(borrowerInfoPO);
    }
}
