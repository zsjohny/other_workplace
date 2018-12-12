package com.finace.miscroservice.borrow.dao.impl;

import com.finace.miscroservice.borrow.dao.ReturnsDetailedDao;
import com.finace.miscroservice.borrow.entity.response.HMoneyResponse;
import com.finace.miscroservice.borrow.entity.response.ReturnsDetailedResponse;
import com.finace.miscroservice.borrow.mapper.ReturnsDetailedMapper;
import com.finace.miscroservice.commons.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ReturnsDetailedDaoImpl implements ReturnsDetailedDao{
    @Resource
    private ReturnsDetailedMapper returnsDetailedMapper;

    @Override
    public ReturnsDetailedResponse findAmtBack(Integer userId) {
        return returnsDetailedMapper.findAmtBack(userId);
    }

    @Override
    public ReturnsDetailedResponse findWaitBack(Integer userId) {
        return returnsDetailedMapper.findWaitBack(userId);
    }

    @Override
    public User findUserByUserId(Integer userId) {
        return returnsDetailedMapper.findUserByUserId(userId);
    }

    @Override
    public HMoneyResponse findHuifu(Integer userId) {
        return returnsDetailedMapper.findHuifu(userId);
    }

    @Override
    public Double findAmtInvestmentByUserId(Integer userId) {
        return returnsDetailedMapper.findAmtInvestmentByUserId(userId);
    }
}
