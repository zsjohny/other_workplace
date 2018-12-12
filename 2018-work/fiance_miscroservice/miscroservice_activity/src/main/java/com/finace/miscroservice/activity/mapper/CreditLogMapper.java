package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.CreditLogPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface CreditLogMapper {


    /**
     * 新增金豆日志
     * @param creditLogPO
     * @return
     */
    int saveCreditLog(CreditLogPO creditLogPO);


    /**
     * 获取金豆日志分页信息
     * @param userId
     * @return
     */
    List<CreditLogPO> getCreditLogByUserId(@Param("userId") String userId);

    /**
     * 获取金豆日志信息数量
     * @param userId
     * @return
     */
    Integer getCreditLogSizeByUserId(@Param("userId") String userId);
}
