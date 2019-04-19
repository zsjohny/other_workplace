package com.yujj.business.facade;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.CaptchaType;
import com.jiuyuan.entity.CaptchaParams;
import com.jiuyuan.util.SignatureUtil;
import com.yujj.business.service.common.CaptchaService;

@Service
public class CaptchaFacade {

    private static final byte[] signatureSaltKey = "#I^&edfdkj^d*%io3h@I(Oio34r".getBytes();

    private static final int signatureLength = 8;

    @Autowired
    private CaptchaService captchaService;

    public String getCacheKey(long userId, String nonce, long time) {
        return userId + nonce + time;
    }

    /**
     * 参数说明：CaptchaParams
     */
    public String getAndValidateCacheKey(long userId, String nonce, long time, String signature) {
        String joinedKey = getCacheKey(userId, nonce, time);
        if (!StringUtils.equals(signature, generateSignature(joinedKey))) {
            throw new IllegalArgumentException("Signature mismatch. nonce: " + nonce + ", time: " + time +
                ", signature: " + signature + ".");
        }
        return joinedKey;
    }

    public String getAndValidateCacheKey(long userId, CaptchaParams params) {
        return getAndValidateCacheKey(userId, params.getNonce(), params.getTime(), params.getSignature());
    }

    public BufferedImage create(CaptchaType captchaType, String cacheKey, int cacheSeconds) throws IOException {
        return captchaService.create(captchaType, cacheKey, cacheSeconds);
    }

    public boolean verifyCode(String code, CaptchaType captchaType, String cacheKey, boolean removeIfSuccess) {
        return captchaService.verifyCode(code, captchaType, cacheKey, removeIfSuccess);
    }

    public String generateSignature(String joinedKey) {
        return SignatureUtil.generateSignature(joinedKey, signatureSaltKey, signatureLength);
    }
}
