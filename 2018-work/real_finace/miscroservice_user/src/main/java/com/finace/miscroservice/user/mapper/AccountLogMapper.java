package com.finace.miscroservice.user.mapper;

import com.finace.miscroservice.user.po.AccountLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 *
 */
@Mapper
public interface AccountLogMapper {


    /**
     * 获取资金明细
     * @return
     */
     List<AccountLogPO> getZjmxByUserId(Map<String, Object> map);

    /**
     *
     * @param accountLogPO
     * @return
     */
     int addAccountLog(AccountLogPO accountLogPO);

    /**
     * 添加accountLog 新增 seqNo交易流水
     * @param accountLog
     */
    void addAccountLogSeqNo(AccountLogPO accountLog);

    /**
     * 根据交易流水号 操作code
     * @param seqNo
     * @param code
     */
    void upAccountLogBySeqNo(@Param("seqNo") String seqNo, @Param("code") String code,@Param("remark") String remark);
}
