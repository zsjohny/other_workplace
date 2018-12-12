package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.SginLogPO;

import java.util.List;

/**
 * 签到日志Dao层
 */
public interface SginLogDao {



    /**
     * 新增用户签到日志
     * @param sginLogPO
     * @return
     */
    public int saveSginLog(SginLogPO sginLogPO);


    /**
     * 获取用户的签到日期
     * @param userId
     * @return
     */
    public List<String> getSginLogMonthByUser(String userId);


}
