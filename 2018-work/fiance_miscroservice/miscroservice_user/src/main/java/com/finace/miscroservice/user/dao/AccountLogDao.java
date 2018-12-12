package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.po.AccountLogPO;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface AccountLogDao {

    /**
     * 获取资金明细
     * @param map
     * @return
     */
    public List<AccountLogPO> getZjmxByUserId(Map<String, Object> map, int page);

    /**
     *
     * @param accountLogPO
     * @return
     */
    public int addAccountLog(AccountLogPO accountLogPO);

    /**
     * 添加accountLog 新增 seqNo交易流水
     * @param accountLog
     */
    void addAccountLogSeqNo(AccountLogPO accountLog);

    /**
     * 根据交易流水号 修改操作code
     * @param seqNo
     * @param code
     */
    void upAccountLogBySeqNo(String seqNo, String code, String remark);
}
