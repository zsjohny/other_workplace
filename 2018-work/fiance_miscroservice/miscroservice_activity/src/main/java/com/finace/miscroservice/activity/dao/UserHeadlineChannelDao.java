package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.UserHeadlineChannelPO;

import java.util.Map;

/**
 *
 */
public interface UserHeadlineChannelDao {

    /**
     * 获取修改头条
     * @param map
     * @return
     */
    public UserHeadlineChannelPO getByImei(Map<String, Object> map);

    /**
     * 修改头条渠道状态
     * @param map
     * @return
     */
    public int updateStatusByImei(Map<String, Object> map);

    /**
     * 新增头条渠道
     * @param userHeadlineChannelPO
     */
    public void addUserHeadlineChannel(UserHeadlineChannelPO userHeadlineChannelPO);





}
