package com.e_commerce.miscroservice.publicaccount.service.impl;

import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.commons.utils.SmsUtils;
import com.e_commerce.miscroservice.publicaccount.service.proxy.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @version V1.0
 * @date 2018/9/25 9:25
 * @Copyright 玖远网络
 */
@Service
public class SmsServiceImpl implements SmsService{

    private static final int AUTH_CODE_MAX_COUNT = 10;
    private static final int VALIDITY_DAY = 1;

    private Log logger = Log.getInstance(SmsServiceImpl.class);
    @Resource(name = "strRedisTemplate")
    private RedisTemplate<String, String> strRedisTemplate;

    /**
     * 发送验证码
     *
     * @param key key
     * @param phone phone
     * @return boolean
     * @author Charlie
     * @date 2018/9/25 10:45
     */
    @Override
    public boolean sendAuthCode(RedisKeyEnum key, String phone) {
        String redisKey = key.toKey () + ":" + phone;
        String countStr = strRedisTemplate.opsForValue ().get (redisKey);
        if (BeanKit.notNull (countStr)) {
            int count = Integer.parseInt (countStr);
            if (count >= AUTH_CODE_MAX_COUNT) {
                logger.info ("推送短信验证码 key:{},使用次数超限count:{}",key, count);
                return Boolean.FALSE;
            }

            boolean isSuccess = SmsUtils.isSuccess (SmsUtils.sendCode (phone));
            if (isSuccess) {
                //increment暂时用了报错, 先覆盖
//                strRedisTemplate.opsForValue ().increment (redisKey, 1);
                strRedisTemplate.opsForValue ().set (redisKey, 1+count+"");
                return Boolean.TRUE;
            }

        }

        boolean isSuccess = SmsUtils.isSuccess (SmsUtils.sendCode (phone));
        if (isSuccess) {
            //有效期一天
            strRedisTemplate.opsForValue ().set (redisKey, "1", VALIDITY_DAY, TimeUnit.DAYS);
            String afterSetCount = strRedisTemplate.opsForValue ().get (redisKey);
            logger.info ("发送验证码 redisKey:{},afterSetCount:{}", redisKey, afterSetCount);
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
