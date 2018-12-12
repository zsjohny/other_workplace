package com.finace.miscroservice.activity.dao.impl;

import com.finace.miscroservice.activity.dao.ChannelBannerDao;
import com.finace.miscroservice.activity.mapper.ChannelBannerMapper;
import com.finace.miscroservice.activity.po.ChannelBannerPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChannelBannerDaoImpl implements ChannelBannerDao{


    @Autowired
    private ChannelBannerMapper channelBannerMapper;

    @Override
    public List<ChannelBannerPO> getChannelBanner(String channel) {
        return channelBannerMapper.getChannelBanner(channel);
    }
}
