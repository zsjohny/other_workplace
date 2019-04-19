package com.e_commerce.miscroservice.order.service.impl;

import com.e_commerce.miscroservice.commons.entity.colligate.YjjConfig;
import com.e_commerce.miscroservice.order.mapper.ConfigMapper;
import com.e_commerce.miscroservice.order.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Autowired
    ConfigMapper configMapper;

    @Autowired
    @Qualifier("userStrHashRedisTemplate")
    private ValueOperations<String, String> redisStrService;

    @Override
    public String getConfigValueByKey(String key) {

        String value = "";
        try{
            value = redisStrService.get("config_"+key);
            if(value==null){
                YjjConfig configValue = configMapper.getConfigValue(key);
                if(configValue==null){
                    return "";
                }
                redisStrService.set("config_"+key,configValue.getKeyValue(),1, TimeUnit.DAYS);
                return configValue.getKeyValue();
            }
        }catch (Exception e){

        }
        return value;
    }
}
