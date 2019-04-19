package com.e_commerce.miscroservice.activity.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/12/26 15:41
 * @Copyright 玖远网络
 */
@Mapper
public interface ChannelUserGatherMapper {

    /**
     * 添加粉丝数量
     *
     * @param id 统计id
     * @param fansCount 粉丝数量
     * @param orderFansCount
     * @return int
     * @author Charlie
     * @date 2018/12/26 16:10
     */
    int appendFansCount(@Param( "id" ) Long id,
                        @Param( "fansCount" ) Integer fansCount,
                        @Param("orderFansCount") int orderFansCount,
                        @Param("fansOrderCount") int fansOrderCount);

}
