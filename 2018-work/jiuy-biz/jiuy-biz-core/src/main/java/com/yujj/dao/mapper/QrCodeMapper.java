package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.QrCode;

@DBMaster
public interface QrCodeMapper {

    public QrCode getQrCode(@Param("code")String code);

    public int active(@Param("userId")long userId, @Param("qrCode") QrCode qrCode, @Param("time") long time);

}
