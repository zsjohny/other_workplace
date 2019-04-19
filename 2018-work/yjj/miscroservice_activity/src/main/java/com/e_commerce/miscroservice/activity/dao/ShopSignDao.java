package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.commons.entity.application.activity.Sign;
import com.e_commerce.miscroservice.commons.entity.application.activity.SignLog;
import com.e_commerce.miscroservice.commons.entity.application.system.DataDictionary;

import java.util.List;

/**
 * Create by hyf on 2018/10/8
 */
public interface ShopSignDao {
    /**
     * 判断是否签到
     * @param userId
     * @return
     */
    Integer getNowSignByUser(Long userId);

    /**
     * 用户签到信息
     * @param userId
     * @return
     */
    Sign getSignByUser(Long userId);

    /**
     * 更新
     */
    void updateSign(Sign sign);

    /**
     * 保存签到
     * @param sign
     */
    void saveSign(Sign sign);

    /**
     * 根据字典获取 连续签到配置
     * @param code
     * @param groupCode
     * @return
     */
    DataDictionary getDataDictionary(String code, String groupCode);

    /**
     * 签到日志
     * @param sginLog
     */
    void saveSignLog(SignLog sginLog);

    /**
     * 当月签到日期
     * @param userId
     * @return
     */
    List<String> getSignLogMonthByUser(Long userId);

    /**
     * 获取最近日期的签到记录
     * @param userId
     * @return
     */
    SignLog getSignLogLimitDescByUser(Long userId);
}
