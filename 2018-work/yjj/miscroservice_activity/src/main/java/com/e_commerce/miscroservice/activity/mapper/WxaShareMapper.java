package com.e_commerce.miscroservice.activity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/24 13:33
 * @Copyright 玖远网络
 */
@Mapper
public interface WxaShareMapper {


    /**
     * targetUser作为唯一约束插入
     *
     * @param sourceUser nullable
     * @param channelUserId nullable
     * @param targetUser 非空
     * @param shareType nullable
     * @param fansType nullable
     * @return int
     * @author Charlie
     * @date 2018/12/24 13:36
     */
    int safeSave(
            @Param("sourceUser") Long sourceUser,
            @Param("channelUserId") Long channelUserId,
            @Param("targetUser") Long targetUser,
            @Param("shareType") Integer shareType,
            @Param("fansType") Integer fansType);
}
