package com.yujj.business.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.yujj.dao.mapper.GlobalSettingMapper;

@Service
public class GlobalSettingService {

    @Autowired
    private GlobalSettingMapper globalSettingMapper;

    @Autowired
    private MemcachedService memcachedService;

    public String getSetting(GlobalSettingName name) {
        String groupKey = MemcachedKey.GROUP_KEY_GLOBAL_SETTING;
        String key = name.getStringValue();
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (String) obj;
        }

        String setting = globalSettingMapper.getSetting(name);
        if (setting != null) {
            memcachedService.set(groupKey, key, DateConstants.SECONDS_FIVE_MINUTES, setting);
        }
        return setting;
    }

    public JSONArray getJsonArray(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? new JSONArray() : JSON.parseArray(value);
    }

    public JSONObject getJsonObject(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
    }

    public JSONObject getJsonObjectNoCache(GlobalSettingName name) {
        String value = getSettingNoCache(name);
        return StringUtils.isBlank(value) ? new JSONObject() : JSON.parseObject(value);
    }


    public <T> T getBean(GlobalSettingName name, Class<T> clazz) {
        String value = getSetting(name);
        T obj = null;
        try {
            obj = StringUtils.isBlank(value) ? clazz.newInstance() : JSON.parseObject(value, clazz);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public int getInt(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? 0 : Integer.parseInt(value);
    }

    public double getDouble(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? 0 : Double.parseDouble(value);
    }

    public long getLong(GlobalSettingName name) {
        String value = getSetting(name);
        return StringUtils.isBlank(value) ? 0 : Long.parseLong(value);
    }

    public Long getUpdateTime(GlobalSettingName name) {
        return globalSettingMapper.getUpdateTime(name);
    }

    public int update(GlobalSettingName coupon, String jsonString) {
        return globalSettingMapper.update(coupon.getStringValue(), jsonString);
    }

    public String getSettingNoCache(GlobalSettingName name) {
        return globalSettingMapper.getSetting(name);
    }



}
