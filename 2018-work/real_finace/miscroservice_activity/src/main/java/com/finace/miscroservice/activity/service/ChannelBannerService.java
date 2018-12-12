package com.finace.miscroservice.activity.service;

import com.finace.miscroservice.activity.po.ChannelBannerPO;
import com.finace.miscroservice.commons.entity.ChannelBanner;

import java.util.List;

public interface ChannelBannerService {

    /**
     *获取banner信息
     * @param channel
     * @return
     */
    public List<ChannelBanner> getChannelBanner(String channel);





}
