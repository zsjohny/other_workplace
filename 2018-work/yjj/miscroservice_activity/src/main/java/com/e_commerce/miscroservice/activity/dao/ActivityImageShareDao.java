package com.e_commerce.miscroservice.activity.dao;

import com.e_commerce.miscroservice.activity.entity.ActivityImageShare;

/**
 * @Author hyf
 * @Date 2019/1/8 16:02
 */
public interface ActivityImageShareDao {
    /**
     * 根据类型查询分享活动
     *
     * @param type
     * @param shareType
     * @return
     */
    ActivityImageShare findActivityImageShareByType(Integer type, Integer shareType);
}
