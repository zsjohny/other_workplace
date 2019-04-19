package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;

@DBMaster
public interface SmsRecordMapper {

    int getRecordCount(@Param("phone") String phone, @Param("startTime") long startTime);
    
    int getStoreRecordCount(@Param("phone") String phone, @Param("startTime") long startTime, @Param("sendChannel") int sendChannel);



    int addSmsRecord(@Param("phone") String phone, @Param("content") String content, @Param("time") long time);
    
    int addStoreSmsRecord(@Param("phone") String phone, @Param("content") String content, @Param("time") long time, @Param("type") int src, @Param("sendType") int sendType, @Param("sendChannel") int sendChannel);

}
