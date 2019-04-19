package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.ActivityMapper;

@Service
public class ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private MemcachedService memcachedService;

    public Activity getActivity(String activityCode) {
        String groupKey = MemcachedKey.GROUP_KEY_ACTIVITY;
        String key = activityCode;
        Object obj = memcachedService.get(groupKey, key);
        if (obj instanceof Activity) {
            return (Activity) obj;
        } else if (obj != null) {
            return null;
        }

        Activity activity = activityMapper.getActivity(activityCode);
        memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, activity == null ? "1" : activity);
        return activity;
    }

}
