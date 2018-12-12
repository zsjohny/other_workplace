package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.ChannelBannerPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChannelBannerMapper {

    /**
     * 获取banner信息
     * @param channel
     * @return
     */
    List<ChannelBannerPO> getChannelBanner(@Param("channel") String channel);



}
