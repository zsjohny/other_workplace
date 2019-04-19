package com.e_commerce.miscroservice.activity.service;

import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/10/8 19:08
 * @Copyright 玖远网络
 */
public interface ShopSignService {
    /**
     * 签到
     * @param userId
     * @param conDay
     * @return
     */
    Response doSign(Long userId, Integer conDay);

    /**
     * 当月签到日期
     * @param userId
     * @return
     */
    Response showDays(Long userId);

    /**
     * 金币记录
     * @param page
     * @param userId
     * @return
     */
    Response getGoldCoinLog(Integer page, Long userId);

    /**
     * 获取签到阶段奖励
     * @param userId
     * @param num
     * @return
     */
    Response getPeriodicalPrize(Long userId, Integer num);
//    Response doSign(Long userId, Integer conDay);
}
