package com.yujj.business.service.common;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.captcha.CaptchaCode;
import com.jiuyuan.util.captcha.CaptchaUtil;

@Service
public class CaptchaService {

    @Autowired
    private MemcachedService memcachedService;

    public BufferedImage create(CaptchaType captchaType, String cacheKey, int cacheSeconds) throws IOException {
        CaptchaCode captchaCode =
            CaptchaUtil.generateCaptchaCode(captchaType.getWidth(), captchaType.getHeight(), captchaType.getLength());
        
        String groupKey = MemcachedKey.GROUP_KEY_CAPTCHA_CODE;
        String key = getKey(captchaType, cacheKey);
        memcachedService.set(groupKey, key, cacheSeconds, captchaCode.getCode());

        return captchaCode.getImage();
    }

    public boolean verifyCode(String code, CaptchaType captchaType, String cacheKey, boolean removeIfSuccess) {
        String groupKey = MemcachedKey.GROUP_KEY_CAPTCHA_CODE;
        String key = getKey(captchaType, cacheKey);
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            String cachedCode = (String) obj;
            if (StringUtils.equalsIgnoreCase(cachedCode, code)) {
                if (removeIfSuccess) {
                    memcachedService.remove(groupKey, key);
                }
                return true;
            } else {
                String validGroupKey = MemcachedKey.GROUP_KEY_CAPTCHA_CODE_VALIDATE;
                String validKey = key + "#" + cachedCode;
                Integer invalidCount = (Integer) memcachedService.get(validGroupKey, validKey);
                if (invalidCount == null) {
                    invalidCount = 0;
                }

                if (++invalidCount > captchaType.getLimitValidTime()) {
                    memcachedService.remove(groupKey, key);
                } else {
                    memcachedService.set(validGroupKey, validKey, DateConstants.SECONDS_FIVE_MINUTES, invalidCount);
                }
            }
        }

        return false;
    }

    private String getKey(CaptchaType captchaType, String cacheKey) {
        return captchaType.getStringValue() + "#" + cacheKey;
    }
}
