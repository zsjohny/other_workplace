package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.HongbaoLogPO;

import java.util.Map;

/**
 *
 */
public interface HongbaoLogDao {

    /**
     *
     * @param map
     * @return
     */
    public HongbaoLogPO getHongbaoLogByUserId(Map<String, Object> map);

    /**
     * 新增红包日志
     * @param hongbaoLogPO
     */
    public void addHongbaoLog(HongbaoLogPO hongbaoLogPO);

    /**
     * 修改红包日志
     * @param hongbaoLogPO
     */
    public void updateHongbaoLog(HongbaoLogPO hongbaoLogPO);





}
