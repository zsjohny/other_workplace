package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.QrCode;
import com.yujj.dao.mapper.QrCodeMapper;

@Service
public class QrCodeService {

    @Autowired
    private QrCodeMapper qrCodeMapper;

    public QrCode getQrCode(String code) {
        return qrCodeMapper.getQrCode(code);
    }

    public int active(long userId, QrCode qrCode, long time) {
        return qrCodeMapper.active(userId,qrCode, time);
    }

}
