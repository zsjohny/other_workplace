package com.jiuyuan.dao.mapper.supplier;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.TeamBuyActivity;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.SecondBuyActivity;

import java.util.List;


@DBMaster
public interface SecondBuyActivityMapper extends BaseMapper<SecondBuyActivity> {

    void increaseActivityMemberCount(long activityId);

    void updatePrice(@Param("secondBuyActivityId") long secondBuyActivityId,
                     @Param("activityProductPrice") Double activityProductPrice);

    SecondBuyActivity existOrderByTime(@Param("storeId") Long storeId,
                                       @Param("shopProductId") Long shopProductId,
                                       @Param("currentTime") Long currentTime);

    /**
     * 秒杀管理列表
     * <p>
     * 排序优先顺序依次按条件：
     * 活动状态为进行中的，
     * 活动状态为待开始的，
     * 活动状态为已结束的，
     * 距离活动即将结束时间最近的，
     * 距离活动开始时间最近的，
     * 距离活动已结束最近的
     * </p>
     *
     * @param storeId storeId
     * @param page    page
     * @return java.util.List<com.jiuyuan.entity.newentity.SecondBuyActivity>
     * @author Charlie
     * @date 2018/7/30 10:24
     */
    List<SecondBuyActivity> listSecondBuyActivity(@Param("storeId") Long storeId, @Param("current") Long current, @Param("page") Page<SecondBuyActivity> page);


    /**
     * 更新秒杀活动参与人数,和下单商品数量
     *
     * @param secondId  秒杀活动id
     * @param userCount 更新人数
     * @param buyCount  购买数量
     * @return int
     * @author Charlie
     * @date 2018/8/1 17:19
     */
    int updateJoinUser(@Param("secondId") Long secondId, @Param("count") Integer userCount, @Param("buyCount") Integer buyCount);


    /**
     * 查询根据商品,查询活动时间有交集的活动
     * <p>
     * 注意: 没有去重!!!
     * 思路:
     * 1.新活动的开始时间<=已有的活动的开始时间 并且 新活动的结束时间>=已有的活动的开始时间
     * 2.新活动的开始时间>=已有的活动的开始时间 并且 新活动的开始时间<=已有的活动的结束时间
     * </p>
     *
     * @param storeId           门店id
     * @param shopProductId     商品id
     * @param activityStartTime 已有活动开始时间
     * @param activityEndTime   已有活动结束时间
     * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
     * @author Charlie
     * @date 2018/8/8 14:10
     */
    List<SecondBuyActivity> findBetweenSameTimeSliceWithProduct(
            @Param("storeId") Long storeId,
            @Param("shopProductId") Long shopProductId,
            @Param("activityStartTime") Long activityStartTime,
            @Param("activityEndTime") Long activityEndTime
    );

	/**
	 * 查询活动标志
	 * @param activeId
	 * @return
	 */
	SecondBuyActivity findHaveActivityStatusById(@Param("activeId") long activeId);
    List<SecondBuyActivity> selectOrderByTime(
            @Param("storeId") Long storeId,
            @Param("shopProductId") Long shopProductId,
            @Param("currentTime") Long currentTime
    );

}