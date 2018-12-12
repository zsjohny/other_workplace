package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.CreditLogPO;

import java.util.List;

public interface CreditLogDao {


    /**
     * 新增金豆日志
     * @param creditLogPO
     * @return
     */
    public int saveCreditLog(CreditLogPO creditLogPO);


    /**
     * 获取金豆日志分页信息
     * @param userId
     * @return
     */
    public List<CreditLogPO> getCreditLogByUserId(String userId);
    /**
     * 获取金豆日志信息数量
     * @param userId
     * @return
     */
    Integer getCreditLogSizeByUserId(String userId);
}
