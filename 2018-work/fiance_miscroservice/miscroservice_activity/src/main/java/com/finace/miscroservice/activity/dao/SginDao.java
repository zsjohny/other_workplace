package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.SginPO;

/**
 * 签到Dao层
 */
public interface SginDao {

    /**
     * 判断用户当前是否签到
     * @param userId
     * @return
     */
    public Integer getNowSginByUser(String userId);

    /**
     * 根据用户id获取签到信息
     * @param userId
     * @return
     */
    public SginPO getSginByUser(String userId);

    /**
     * 新增用户签到信息
     * @param sginPO
     * @return
     */
    public int saveSgin(SginPO sginPO);

    /**
     * 修改用户签到信息
     * @param sginPO
     * @return
     */
    public int updateSgin(SginPO sginPO);
















}
