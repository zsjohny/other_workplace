package com.e_commerce.miscroservice.operate.dao;

import com.e_commerce.miscroservice.commons.entity.activity.ActivityImageShare;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author hyf
 * @Date 2019/1/8 16:37
 */
public interface ActivityImageShareDao {
    /**
     * 根据筛选条件 查找
     * @param type
     * @param shareType
     * @param timeStar
     * @param timeEnd
     * @param pageNumber
     * @param pageSize
     * @return
     */
    List<ActivityImageShare> findAllActivityImageShare(Integer type, Integer shareType, Long timeStar, Long timeEnd, Integer pageNumber, Integer pageSize);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    ActivityImageShare findById(Long id);

    /**
     * 添加
     * @param request
     * @return
     */
    Integer addShareImage(ActivityImageShare request);

    /**
     * 更新
     * @param request
     * @return
     */
    Integer shareImageUpd(ActivityImageShare request);
}
