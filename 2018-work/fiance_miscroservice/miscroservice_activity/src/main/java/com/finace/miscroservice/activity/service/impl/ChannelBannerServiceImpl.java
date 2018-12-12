package com.finace.miscroservice.activity.service.impl;

import com.finace.miscroservice.activity.dao.ChannelBannerDao;
import com.finace.miscroservice.activity.po.ChannelBannerPO;
import com.finace.miscroservice.activity.service.ChannelBannerService;
import com.finace.miscroservice.commons.entity.ChannelBanner;
import com.finace.miscroservice.commons.utils.tools.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class ChannelBannerServiceImpl implements ChannelBannerService {


    @Autowired
    private ChannelBannerDao channelBannerDao;

    @Override
    public List<ChannelBanner> getChannelBanner(String channel) {
        List<ChannelBanner> cblist = new ArrayList<>();
        List<ChannelBannerPO> list = channelBannerDao.getChannelBanner(channel);
        for (ChannelBannerPO channelBannerPO : list) {
            if( !DateUtils.compareDate(channelBannerPO.getStime(), DateUtils.getNowDateStr())
                    && DateUtils.compareDate(channelBannerPO.getEtime(), DateUtils.getNowDateStr())
                    && channelBannerPO.getStatus() == 1 ){

                ChannelBanner channelBanner = new ChannelBanner();
                BeanUtils.copyProperties(channelBannerPO, channelBanner);
                cblist.add(channelBanner);
            }
        }

        return cblist;
    }
}
