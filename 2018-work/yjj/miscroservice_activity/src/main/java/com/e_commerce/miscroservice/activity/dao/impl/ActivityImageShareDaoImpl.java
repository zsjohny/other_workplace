package com.e_commerce.miscroservice.activity.dao.impl;

import com.e_commerce.miscroservice.activity.dao.ActivityImageShareDao;
import com.e_commerce.miscroservice.activity.entity.ActivityImageShare;
import com.e_commerce.miscroservice.activity.mapper.ActivityImageShareMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @Author hyf
 * @Date 2019/1/8 16:02
 */
@Repository
public class ActivityImageShareDaoImpl implements ActivityImageShareDao {

    @Resource
    private ActivityImageShareMapper activityImageShareMapper;

    @Override
    public ActivityImageShare findActivityImageShareByType(Integer type, Integer shareType) {
        return activityImageShareMapper.findActivityImageShareByType(type, shareType);
    }
}
