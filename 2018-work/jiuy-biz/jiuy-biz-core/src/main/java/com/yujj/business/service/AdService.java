package com.yujj.business.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ad.AdEnum;
import com.jiuyuan.entity.AdConfig;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.AdMapper;

@Service
public class AdService {

    @Autowired
    private AdMapper adMapper;

    @Autowired
    private MemcachedService memcachedService;

    @SuppressWarnings("unchecked")
    public List<AdConfig> getAdsByType(AdEnum adType) {
        String groupKey = MemcachedKey.GROUP_KEY_AD_CONFIG;
        String key = adType.name();
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (List<AdConfig>) obj;
        }

        List<AdConfig> adConfigs = adMapper.getAdsByType(adType);
        if (!adConfigs.isEmpty()) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, adConfigs);
        }
        return adConfigs;
    }

}
