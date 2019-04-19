package com.jiuy.core.service.task;

import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.service.GlobalSettingService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.GlobalSettingName;

/**
 * @author jeff.zhan
 * @version 2016年11月1日 下午8:14:55
 * 
 */

@Service
public class CouponCountAdjustJob {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(CouponCountAdjustJob.class);
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	public void execute() throws ParseException {
		boolean flag = false;
		JSONObject lottery_turntable = globalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY);
		long current_time = System.currentTimeMillis();
		if (!isOnActivityTime(lottery_turntable, current_time)) {
			return;
		}
		
		String acticity_start = lottery_turntable.getString("acticity_start");
		Long start_time = DateUtil.convertToMSEL(acticity_start);
		JSONArray prize = lottery_turntable.getJSONArray("prize");
		for (Object object : prize) {
			JSONObject prize_item = (JSONObject) object;
			if (!needAdjust(prize_item)) {
				continue;
			}
			Integer schedual_type = prize_item.getInteger("schedual_type");
			JSONObject schedual_content = prize_item.getJSONObject("schedual_content");
			Integer time = schedual_content.getInteger("time");
			Integer count = schedual_content.getInteger("count");
			
			flag = adjustCount(schedual_type, time, count, prize_item, start_time, current_time);
		}
		
		if (flag) {
			globalSettingService.update(GlobalSettingName.DRAW_LOTTERY.getStringValue(), lottery_turntable.toJSONString());
		}
	}

	private boolean adjustCount(Integer schedual_type, Integer time, Integer count, JSONObject prize_item,
			Long start_time, long current_time) {
		if (schedual_type == 1) {
			if (onRightTime(start_time, time, current_time)) {
				Integer prize_count = prize_item.getInteger("prize_count");
				prize_item.put("prize_count", prize_count + count);
				return true;
			}
		} else if(schedual_type == 2) {
			if (onFixTime(time, current_time)) {
				prize_item.put("prize_count", count);
				return true;
			}
		}
		return false;
	}

	private boolean onFixTime(Integer time, long current_time) {
		DateTime dateTime = new DateTime(current_time);
		int hourOfDay = dateTime.getHourOfDay();
		int minuteOfHour = dateTime.getMinuteOfHour();
		if (minuteOfHour == 0) {
			if (time == -1 || time == hourOfDay) {
				return true;
			}
		}

		return false;
	}

	private boolean onRightTime(Long start_time, Integer time, long current_time) {
		long gap_minutes = (current_time - start_time) / 1000 / 60;
		if (gap_minutes % time == 0) {
			return true;
		}
		return false;
	}

	private boolean needAdjust(JSONObject prize_item) {
		String schedual_adjust = prize_item.getString("schedual_adjust");
		return StringUtils.equals(schedual_adjust, "YES");
	}

	private boolean isOnActivityTime(JSONObject lottery_turntable, long current_time) throws ParseException {
		String acticity_start = lottery_turntable.getString("acticity_start");
		String activity_end = lottery_turntable.getString("activity_end");
		if (DateUtil.convertToMSEL(acticity_start) > current_time && DateUtil.convertToMSEL(activity_end) < current_time) {
			return false;
		}
		return true;
	}
}
