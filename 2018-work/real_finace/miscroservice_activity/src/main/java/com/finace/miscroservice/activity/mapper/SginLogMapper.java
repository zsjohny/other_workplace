package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.SginLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 签到日志mybatis接口
 */
@Mapper
public interface SginLogMapper {

    /**
     * 新增用户签到日志
     * @param sginLogPO
     * @return
     */
    int saveSginLog(SginLogPO sginLogPO);

    /**
     * 获取用户的签到日期
     * @param userId
     * @return
     */
    List<String>  getSginLogMonthByUser(@Param("userId") String userId);


}
