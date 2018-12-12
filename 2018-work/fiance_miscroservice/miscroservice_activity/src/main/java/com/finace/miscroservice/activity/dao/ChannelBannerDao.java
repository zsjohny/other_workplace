package com.finace.miscroservice.activity.dao;

import com.finace.miscroservice.activity.po.ChannelBannerPO;

import java.util.List;

public interface ChannelBannerDao {


    /**
     * 获取banner信息
     * @param channel
     * @return
     */
    public List<ChannelBannerPO> getChannelBanner(String channel);
}
