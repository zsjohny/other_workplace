package com.finace.miscroservice.activity.mapper;


import com.finace.miscroservice.activity.po.entity.UserChannelGeneralize;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserChannelGeneralizeMapper {
    /**
     * 插入 渠道推广
     * @param userChannelGeneralize
     */
    void insertUserChannelGeneralize(@Param("userChannelGeneralize")UserChannelGeneralize userChannelGeneralize);
    /**
     * 根据 phone：android|ios 和 idfa（android为imei） 查询 渠道推广
     * @param idfa
     * @param phone
     * @return
     */
    UserChannelGeneralize findChannelGeneralize(@Param("idfa")String idfa, @Param("phone")String phone);

    List<String> findChannelGeneralizes();

    void upUserChannelGeneralize(@Param("idfa")String idfa);
}
