package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.entity.response.OpenAccountLogResponse;
import com.finace.miscroservice.user.entity.po.CreditAccountLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OpenAccountLogMapper {
    /**
     * 成功回调用 日志添加
     */
    void addOpenAccountLog(@Param("accountLog")CreditAccountLog accountLog);

    /**
     * 根据流水号 查询日志
     * @param seqNo
     * @return
     */
    CreditAccountLog findOpenAccountLogBySeqNo(@Param("seqNo") String seqNo, @Param("txTime") String txTime);

    /**
     * 修改提现状态
     * @param seqNo
     * @param txTime
     * @param isSuccess
     */
    void upWithdrawIsSuccess(@Param("seqNo")String seqNo, @Param("txTime")String txTime,@Param("isSuccess") int isSuccess);

    /**
     * 查询资金流水
     * @param userId
     * @param txCode
     * @return
     */
    List<OpenAccountLogResponse> findMoneyFlowingWaterByUserId(@Param("userId")String userId, @Param("txCode")String txCode);
}
