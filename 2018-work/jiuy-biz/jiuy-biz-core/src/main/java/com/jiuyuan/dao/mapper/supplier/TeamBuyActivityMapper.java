package com.jiuyuan.dao.mapper.supplier;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.TeamBuyActivity;

import java.util.List;


@DBMaster
public interface TeamBuyActivityMapper extends BaseMapper<TeamBuyActivity>{

	void increaseActivityMemberCount(long teamBuyActivityId);
	
	void updatePrice(@Param("teamBuyActivityId") long teamBuyActivityId,
			         @Param("activityProductPrice") Double activityProductPrice);

	TeamBuyActivity existOrderByTime(
			@Param("storeId") Long storeId,
			@Param("shopProductId") Long shopProductId,
			@Param("currentTime") Long currentTime
	);

	TeamBuyActivity selectTeamBuyActivity(@Param("id") Long id,@Param("storeId") Long storeId, @Param("shopProductId") Long shopProductId, @Param("delStatus") Integer delStatus);

	/**
	 * 门店用户的团购活动列表
	 * <p>
	 * 排序优先顺序依次按条件：
	 *     活动状态为进行中的，
	 *     活动状态为待开始的，
	 *     活动状态为已结束的，
	 *     距离活动即将结束时间最近的，
	 *     距离活动开始时间最近的，
	 *     距离活动已结束最近的
	 * </p>
	 * @param storeId 门店id
	 * @param current 当前时间
	 * @param page 分页参数
	 * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
	 * @author Charlie
	 * @date 2018/7/30 9:17
	 */
    List<TeamBuyActivity> listTeamBuyActivity(@Param ("storeId") Long storeId, @Param ("current")Long current, @Param ("page")Page<TeamBuyActivity> page);

	int increaseBuyUserOrPdcCount(@Param ("id")Long id,
								  @Param ("conditionType")Integer conditionType,
								  @Param ("count")Integer count,
								  @Param ("userCount") Integer userCount);


	/**
	 * 查询根据商品,查询活动时间有交集的活动
	 * <p>
	 *     注意: 没有去重!!!
	 *     思路:
	 *     1.新活动的开始时间<=已有的活动的开始时间 并且 新活动的结束时间>=已有的活动的开始时间
	 *     2.新活动的开始时间>=已有的活动的开始时间 并且 新活动的开始时间<=已有的活动的结束时间
	 * </p>
	 * @param storeId 门店id
	 * @param shopProductId 商品id
	 * @param activityStartTime 已有活动开始时间
	 * @param activityEndTime 已有活动结束时间
	 * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
	 * @author Charlie
	 * @date 2018/8/8 14:10
	 */
	List<TeamBuyActivity> findBetweenSameTimeSliceWithProduct(
    		@Param ("storeId") Long storeId,
			@Param ("shopProductId") Long shopProductId,
			@Param ("activityStartTime") Long activityStartTime,
			@Param ("activityEndTime") Long activityEndTime
	);

    List<TeamBuyActivity> selectOrderByTime(
    		@Param("storeId") Long storeId,
			@Param("shopProductId") Long shopProductId,
			@Param("currentTime") Long currentTime
	);

	/**
	 * 根据活动id查询 历史记录
	 * @param activeId
	 * @return
	 */
	TeamBuyActivity findHistoryTeamById(@Param("activeId") long activeId);

	/**
	 * 拼团状态
	 * @param teamId
	 * @return
	 */
	TeamBuyActivity findTeamInFullById(@Param("teamId") Long teamId);
}