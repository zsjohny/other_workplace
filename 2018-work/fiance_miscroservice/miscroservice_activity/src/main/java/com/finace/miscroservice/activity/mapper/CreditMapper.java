package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.CreditPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

@Mapper
public interface CreditMapper {

    /**
     * 新增用户金豆信息
     * @param creditPO
     * @return
     */
    int saveCredit(CreditPO creditPO);

    /**
     * 根据用户id  获取用户的金豆信息
     * @param userId
     * @return
     */
    CreditPO getCreditByUserId(@Param("userId") String userId);

    /**
     * 修改用户金豆信息
     * @param map
     * @return
     */
    int updateCreditAddByUserId(Map<String, String> map);

}
