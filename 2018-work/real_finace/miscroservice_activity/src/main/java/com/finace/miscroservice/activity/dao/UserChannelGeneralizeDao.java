package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.entity.UserChannelGeneralize;

import java.util.List;

public interface UserChannelGeneralizeDao {
    /**
     * 插入 渠道推广
     * @param userChannelGeneralize
     */
    void insertUserChannelGeneralize(UserChannelGeneralize userChannelGeneralize);

    /**
     * 根据 phone：android|ios 和 idfa（android为imei） 查询 渠道推广
     * @param idfa
     * @param phone
     * @return
     */
    UserChannelGeneralize findChannelGeneralize(String idfa, String phone);

    List<String> findChannelGeneralizes();

    void upUserChannelGeneralize(String idfa);
}
