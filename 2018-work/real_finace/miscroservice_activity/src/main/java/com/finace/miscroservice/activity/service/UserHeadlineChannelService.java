package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;

import java.util.Map;

/**
 *
 */
public interface UserHeadlineChannelService {


    /**
     * 新增头条渠道
     * @param userHeadlineChannelPO
     */
    public void addUserHeadlineChannel(UserHeadlineChannelPO userHeadlineChannelPO);

    /**
     * 获取修改头条
     * @return
     */
    public UserHeadlineChannelPO getByImei(String imei, String status);

    /**
     * 修改头条渠道状态
     * @param imei
     * @return
     */
    public int updateStatusByImei(String imei, String status);

}
