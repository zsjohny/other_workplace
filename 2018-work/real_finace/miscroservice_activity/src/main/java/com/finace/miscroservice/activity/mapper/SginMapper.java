package com.finace.miscroservice.activity.mapper;

import com.finace.miscroservice.activity.po.SginPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 签到mybatis接口
 */
@Mapper
public interface SginMapper {


    /**
     * 判断用户当前是否签到
     * @param userId
     * @return
     */
    Integer getNowSginByUser(@Param("userId") String userId);

    /**
     * 根据用户id获取签到信息
     * @param userId
     * @return
     */
    SginPO getSginByUser(@Param("userId") String userId);

    /**
     * 新增用户签到信息
     * @param sginPO
     * @return
     */
    int saveSgin(SginPO sginPO);

    /**
     * 修改用户签到信息
     * @param sginPO
     * @return
     */
    int updateSgin(SginPO sginPO);


}
