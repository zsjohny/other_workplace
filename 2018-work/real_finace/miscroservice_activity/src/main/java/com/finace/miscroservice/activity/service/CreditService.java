package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.commons.utils.Response;

/**
 * 积分service
 */
public interface CreditService {


    /**
     * 投资送金豆
     *
     * @param userId     用户id
     * @param times 投资标的天数
     * @param amt        购买金额
     */
    public void investGrantGlodBean(String userId, Integer times, Double amt);


    /**
     * 首次分享送金豆
     *
     * @param userId
     */
    public Response shareGrantGlodBean(String userId);


}
