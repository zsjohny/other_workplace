package com.jiuyuan.service.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.entity.newentity.SecondBuyActivity;


@Service
public class StoreSecondBuyActivityService{
	
	private static final Logger logger = LoggerFactory.getLogger(StoreSecondBuyActivityService.class);
	
	@Autowired
	private SecondBuyActivityMapper secondBuyActivityMapper;



	/**
	 * 秒杀管理列表
	 *
	 * @param storeId storeId
	 * @param page page
	 * @return java.util.List<com.jiuyuan.entity.newentity.SecondBuyActivity>
	 * @author Charlie
	 * @date 2018/7/30 10:24
	 */
	public List<SecondBuyActivity> getStoreSecondBuyActivityList(Long storeId,Page<SecondBuyActivity> page) {
//		Wrapper<SecondBuyActivity> wrapper = new EntityWrapper<SecondBuyActivity>().eq("store_id", storeId)
//				.and(" activity_hand_end_time=0 and activity_end_time>"+System.currentTimeMillis()).orderBy("activity_start_time", true)
//				.eq("del_state", 0);
//		return secondBuyActivityMapper.selectPage(page, wrapper);
        List<SecondBuyActivity> list = secondBuyActivityMapper.listSecondBuyActivity (storeId, System.currentTimeMillis (), page);
        return list;
	}

	/**
	 * 秒杀历史列表
	 * @param storeId
	 * @param page
	 * @return
	 */
	public List<SecondBuyActivity> getHistoryList(long storeId, Page<SecondBuyActivity> page) {
		Wrapper<SecondBuyActivity> wrapper = new EntityWrapper<SecondBuyActivity>().eq("store_id", storeId)
				.and(" (activity_hand_end_time>0 or activity_end_time<="+System.currentTimeMillis()+") ").eq("del_state", 0)
				.orderBy("activity_start_time", false);
		return secondBuyActivityMapper.selectPage(page, wrapper);
	}

	/**
	 * 秒杀活动详情
	 * @param activityId
	 * @return
	 */
	public SecondBuyActivity getStoreSecondBuyActivityItem(Long activityId) {
		return secondBuyActivityMapper.selectById(activityId);
	}
	
}