package com.e_commerce.miscroservice.activity.mapper;

import com.e_commerce.miscroservice.activity.entity.ActivityImageShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author hyf
 * @Date 2019/1/8 16:02
 */
@Mapper
public interface ActivityImageShareMapper {
    /**
     * 根据类型查询分享活动
     *
     * @param type
     * @param shareType
     * @return
     */
    ActivityImageShare findActivityImageShareByType(@Param("type") Integer type, @Param("shareType") Integer shareType);
}
