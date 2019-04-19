package com.jiuyuan.service.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.newentity.TeamBuyActivity;

@Service
public class StoreTeamBuyActivityService{
	
	private static final Logger logger = LoggerFactory.getLogger(StoreTeamBuyActivityService.class);
	
	@Autowired
	private TeamBuyActivityMapper teamBuyActivityMapper;

	/**
	 * 团购管理列表
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
	 * @param page page 分页参数
	 * @return java.util.List<com.jiuyuan.entity.newentity.TeamBuyActivity>
	 * @author Charlie
	 * @date 2018/7/30 10:15
	 */
	public List<TeamBuyActivity> getStoreTeamBuyActivityList(Long storeId,Page<TeamBuyActivity> page) {
//		Wrapper<TeamBuyActivity> wrapper = new EntityWrapper<TeamBuyActivity>().eq("store_id", storeId)
//				.and(" activity_hand_end_time=0 and activity_end_time>"+System.currentTimeMillis()).orderBy("activity_start_time", true)
//				.eq("del_state", 0);
		return teamBuyActivityMapper.listTeamBuyActivity(storeId, System.currentTimeMillis (), page);
	}

	/**
	 * 团购历史列表
	 * @param storeId
	 * @param page
	 * @return
	 */
	public List<TeamBuyActivity> getHistoryList(long storeId, Page<TeamBuyActivity> page) {
		Wrapper<TeamBuyActivity> wrapper = new EntityWrapper<TeamBuyActivity>().eq("store_id", storeId)
				.and(" (activity_hand_end_time>0 or activity_end_time<="+System.currentTimeMillis()+") ").eq("del_state", 0)
				.orderBy("activity_start_time", false);
		return teamBuyActivityMapper.selectPage(page, wrapper);
	}

	/**
	 * 团购活动详情
	 * @param activityId
	 * @return
	 */
	public TeamBuyActivity getStoreTeamBuyActivityItem(Long activityId) {
		return teamBuyActivityMapper.selectById(activityId);
	}
	
}