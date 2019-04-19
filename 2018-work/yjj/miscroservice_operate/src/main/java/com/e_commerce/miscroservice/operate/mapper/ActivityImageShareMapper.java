package com.e_commerce.miscroservice.operate.mapper;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityImageShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/8 16:37
 */
@Mapper
public interface ActivityImageShareMapper {
    /**
     * 根据筛选条件 查找
     * @param type
     * @param shareType
     * @param timeStar
     * @param timeEnd
     * @return
     */
    List<ActivityImageShare> findAllActivityImageShare(@Param("type") Integer type, @Param("shareType") Integer shareType, @Param("timeStar") Long timeStar, @Param("timeEnd") Long timeEnd);
}
