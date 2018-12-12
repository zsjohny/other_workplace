package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.HongbaoLogPO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

/**
 *
 */
@Mapper
public interface HongbaoLogMapper {

    /**
     * 获取用户红包日志信息
     * @param map
     * @return
     */
      HongbaoLogPO getHongbaoLogByUserId(Map<String, Object> map);

    /**
     * 新增红包日志
     * @param hongbaoLogPO
     */
    void addHongbaoLog(HongbaoLogPO hongbaoLogPO);

    /**
     * 修改红包日志
     * @param hongbaoLogPO
     */
    void updateHongbaoLog(HongbaoLogPO hongbaoLogPO);



}
