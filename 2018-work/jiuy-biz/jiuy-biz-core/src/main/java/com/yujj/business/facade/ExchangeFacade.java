package com.yujj.business.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.BatchNumber;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.QrCode;
import com.yujj.business.service.QrCodeService;
import com.yujj.business.service.UserCoinService;

@Service
public class ExchangeFacade {
    
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");

    @Autowired
    private QrCodeService qrCodeService;

    @Autowired
    private UserCoinService userCoinService;

    @Transactional(rollbackFor = Exception.class)
    public void exchange(long userId, QrCode qrCode, BatchNumber batchNumber, ClientPlatform clientPlatform) {
        long time = System.currentTimeMillis();

        int count = qrCodeService.active(userId,qrCode, time);
        if (count != 1) {
            String msg = "active qrcode error, qrCode: " + qrCode.getQrcode_id();
            logger.error(msg);
            throw new IllegalStateException(msg);
        }

        try {
            int diffUnavalCoins = batchNumber.getToken_ratio();
            String relatedId = qrCode.getQrcode_id();
            userCoinService.updateUserCoin(userId, 0, 0, relatedId, time, UserCoinOperation.EXCHANGE, null, clientPlatform.getVersion());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }

}
