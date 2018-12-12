package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import com.finace.miscroservice.user.entity.response.OpenAccountLogResponse;

import java.util.List;

public interface OpenAccountLogDao {
    /**
     * 成功回调用 日志添加
     */
    void addOpenAccountLog(CreditAccountLog accountLog);

    /**
     * 根据 流水号查询
     * @param seqNo
     * @return
     */
    CreditAccountLog findOpenAccountLogBySeqNo(String seqNo, String txTime);

    /**
     * 修改提现状态
     * @param seqNo
     * @param txTime
     * @param isSuccess
     */
    void upWithdrawIsSuccess(String seqNo, String txTime, int isSuccess);

    /**
     * 资金流水
     * @param userId
     * @param txCode
     * @return
     */
    List<OpenAccountLogResponse> findMoneyFlowingWaterByUserId(String userId, String txCode);
}
