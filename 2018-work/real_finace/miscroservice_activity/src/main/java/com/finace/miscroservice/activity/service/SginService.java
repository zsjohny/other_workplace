package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.commons.utils.Response;

/**
 * 签到service
 */
public interface SginService {


    /**
     * 签到
     * @param userId
     * @return
     */
    public Response doSign(String userId);


    /**
     * 获取签到的日期
     * @param userId
     * @return
     */
    public Response getSginDay(String userId);

    /**
     * 用户访问 金豆获得记录
     * @param page
     * @param userId
     * @return
     */
    Response getGoldPeasLog(Integer page, String userId);
}
