package com.finace.miscroservice.user.dao.impl;


import com.finace.miscroservice.commons.entity.UserRedPackets;
import com.finace.miscroservice.user.dao.PcUserDao;

import com.finace.miscroservice.user.entity.po.Register;
import com.finace.miscroservice.user.entity.response.*;
import com.finace.miscroservice.user.mapper.PcUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * user Dao层实现类
 */
@Component
public class PcUserDaoImpl implements PcUserDao {
    @Resource
    private PcUserMapper pcUserMapper;
    @Override
    public List<AccountLogResponse> pcAccountLog(String startTime, String endTime, Integer userId) {
        return pcUserMapper.pcAccountLog(startTime,endTime,userId);
    }

    @Override
    public MyPropertyResponse pcMyProperty(Integer userId) {
        return pcUserMapper.pcMyProperty(userId);
    }

    @Override
    public FinanceMoneyResponse pcBackMoney(Integer userId, String month) {
        return pcUserMapper.pcBackMoney(userId,month);
    }

    @Override
    public List<MyCouponsResponse> pcMyCoupons(Integer type, Integer userId) {
        return pcUserMapper.pcMyCoupons(type,userId);
    }

    /**
     * 我的邀请
     * @param userId
     * @return
     */
    @Override
    public MyInvitationResponse myInvitation(Integer userId) {
        return pcUserMapper.myInvitation(userId);
    }

    @Override
    public UserRedPackets getUserIdInviter(Integer userId, int user_id) {
        return pcUserMapper.getUserIdInviter(userId,user_id);
    }

    @Override
    public MyInformationResponse myInformation(Integer userId) {
        return pcUserMapper.myInformation(userId);
    }

    @Override
    public List<MyFinanceBidResponse> myFinanceBid(String userId) {
        return pcUserMapper.myFinanceBid(userId);
    }
    @Override
    public List<MyBorrowInfoResponse> myBorrowinfoById(String userId,Integer type) {
        return pcUserMapper.myBorrowinfoById(userId,type);
    }
    @Override
    public MyBorrowInfoResponse getInfoByBorrowId(Integer borrowId) {
        return pcUserMapper.getInfoByBorrowId(borrowId);
    }

    @Override
    public void register(String username, String pass) {
        pcUserMapper.register(username,pass);
    }

    @Override
    public Register findRegisterTmp(String username) {
        return pcUserMapper.findRegisterTmp(username);
    }

    @Override
    public void upRegisterTmp(String phone) {
        pcUserMapper.upRegisterTmp(phone);
    }

}
