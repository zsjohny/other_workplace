package com.finace.miscroservice.user.dao.impl;

import com.finace.miscroservice.user.dao.OpenAccountLogDao;
import com.finace.miscroservice.user.entity.response.OpenAccountLogResponse;
import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import com.finace.miscroservice.user.mapper.OpenAccountLogMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Component
public class OpenAccountLogDaoImpl implements OpenAccountLogDao {
    @Resource
    private OpenAccountLogMapper openAccountLogMapper;
    @Override
    @Transactional
    public void addOpenAccountLog(CreditAccountLog accountLog) {
        openAccountLogMapper.addOpenAccountLog(accountLog);
    }

    @Override
    public CreditAccountLog findOpenAccountLogBySeqNo(String seqNo, String txTime) {
            return openAccountLogMapper.findOpenAccountLogBySeqNo(seqNo,txTime);
    }

    @Override
    @Transactional
    public void upWithdrawIsSuccess(String seqNo, String txTime, int isSuccess) {
        openAccountLogMapper.upWithdrawIsSuccess(seqNo,txTime,isSuccess);
    }

    @Override
    public List<OpenAccountLogResponse> findMoneyFlowingWaterByUserId(String userId, String txCode) {

        return openAccountLogMapper.findMoneyFlowingWaterByUserId(userId,txCode);
    }
}
