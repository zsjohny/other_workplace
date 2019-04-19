package com.yujj.business.facade;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.constant.coupon.CouponGetWay;
import com.jiuyuan.entity.Activity;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.DrawLottery;
import com.jiuyuan.entity.DrawLotteryLog;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.query.PageQuery;
import com.yujj.business.service.ActivityService;
import com.yujj.business.service.CouponTemplateService;
import com.yujj.business.service.DrawLotteryService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.OrderCouponService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.UserService;
import com.yujj.dao.mapper.DrawLotteryLogMapper;
import com.yujj.dao.mapper.DrawLotteryMapper;
import com.yujj.entity.account.User;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponTemplate;

/**
 * @author jeff.zhan
 * @version 2016年11月2日 下午1:22:27
 * 
 */
@Service
public class ActivityFacade {
	
	private static final Logger logger = LoggerFactory.getLogger(ActivityFacade.class);
	
	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private UserCoinService userCoinService;
	
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private OrderCouponService orderCouponService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DrawLotteryLogMapper drawLotteryLogMapper;
    
    @Autowired
    private DrawLotteryMapper drawLotteryMapper;

    @Autowired
    private CouponTemplateService couponTemplateService;
    
    @Autowired
    private DrawLotteryService drawLotteryService ;
    
    @Transactional(rollbackFor = Exception.class)
	public synchronized Map<String, Object> drawLottery(UserDetail userDetail, ClientPlatform clientPlatform) throws ParseException {
    	long userId = userDetail.getUserId();
    	Map<String, Object> map = new HashMap<>();
    	
		long current_time = System.currentTimeMillis();
		JSONObject draw_lottery = globalSettingService.getJsonObjectNoCache(GlobalSettingName.DRAW_LOTTERY);
		if (!isOnActivityTime(draw_lottery, current_time)) {
			map.put("status", -1);
			map.put("description", "未在抽奖活动期间!");
			return map;
		}
		
		if (!checkTimes(userId, draw_lottery, map)) {
			return map;
		}
		
		//扣玖币
		UserCoin userCoin = userCoinService.getUserCoin(userId);
		if (userCoin == null || userCoin.getUnavalCoins() < draw_lottery.getInteger("cost_jiucoin")) {
			map.put("status", -4);
			map.put("description", "用户玖币不足");
			return map;
		}
		map.put("user_coin", userCoin.getUnavalCoins() - draw_lottery.getInteger("cost_jiucoin"));
		
		Activity activity = activityService.getActivity("drawLottery");
		userCoinService.updateUserCoin(userId, 0, -draw_lottery.getInteger("cost_jiucoin"), activity.getActivityCode(), System.currentTimeMillis(), UserCoinOperation.ACTIVITY, null, clientPlatform.getVersion());

		//更新奖品数量
		JSONObject draw_lottery_new = updateLotteryCount(current_time);
		
		//抽奖,未抽中
		if (!drawLottery(draw_lottery_new, map)) {
			DrawLotteryLog drawLotteryLog = new DrawLotteryLog(1, "", "", userId, -1, current_time, -1, draw_lottery.getInteger("cost_jiucoin"), 0);
			drawLotteryLogMapper.add(drawLotteryLog);
			map.put("status", 0);
			map.put("description", "抽奖成功");
			return map;
		}

		map.put("status", 1);
		map.put("description", "中奖");

		//发奖
		Long type = Long.parseLong(map.get("type").toString());
		if (type.equals(1L)) {
			try {
				orderCouponService.getCoupon(Long.parseLong(map.get("related_id").toString()), 1, userId, CouponGetWay.DRAW_LOTTERY, true);
				map.put("coupon", couponTemplateService.search(Long.parseLong(map.get("related_id").toString())));
			} catch (Exception e) {
				
				logger.error("ActivityFacade: 用户" + userId + "抽中奖品但代金券发放失败,错误：" + e.getMessage());
				
				DrawLotteryLog drawLotteryLog = new DrawLotteryLog(1, "", "", userId, -1, current_time, -1, draw_lottery.getInteger("cost_jiucoin"), 0);
				drawLotteryLogMapper.add(drawLotteryLog);
				map.put("status", 0);
				map.put("description", "抽奖成功");
				return map;
			}
		} else if (type.equals(2L)) {
			Integer jiuCoin = Integer.parseInt(map.get("jiu_coin").toString());
			userCoinService.updateUserCoin(userId, 0, jiuCoin, -1 + "", current_time, UserCoinOperation.DRAW_LOTTERY_JIUCOIN, null, clientPlatform.getVersion());
		}
		
		DrawLotteryLog drawLotteryLog = new DrawLotteryLog(Integer.parseInt(map.get("type").toString()), map.get("name").toString(), 
				map.get("rank_name").toString(), userId, Long.parseLong(map.get("id").toString()), current_time, 0, draw_lottery.getInteger("cost_jiucoin"), Integer.parseInt(map.get("jiu_coin").toString()));
		
		drawLotteryLogMapper.add(drawLotteryLog);
		return map;
	}
	
	private boolean checkTimes(long userId, JSONObject draw_lottery, Map<String, Object> map) throws ParseException {
		String acticity_start = draw_lottery.getString("acticity_start");
		String activity_end = draw_lottery.getString("activity_end");
		List<DrawLotteryLog> drawLotteryLogs = drawLotteryLogMapper.getDrawLottery(userId, DateUtil.parseStrTime2Long(acticity_start), DateUtil.parseStrTime2Long(activity_end));
		JSONObject limit = draw_lottery.getJSONObject("limit");
		Integer everyday = limit.getInteger("everyday");
		Integer total = limit.getInteger("total");
		
		if (total != -1 && total <= drawLotteryLogs.size()) {
			map.put("status", -2);
			map.put("description", "活动期间超出抽奖次数!");
			return false;
//			throw new ParameterErrorException("活动期间超出抽奖次数!");
		}
				
		List<DrawLotteryLog> dList = drawLotteryLogMapper.getDrawLottery(userId, DateUtil.getTodayStart(), DateUtil.getTodayEnd());
		if (everyday != -1 && everyday <= dList.size()) {
			map.put("status", -3);
			map.put("description", "今天超出抽奖次数!");
			return false;
//			throw new ParameterErrorException("今天超出抽奖次数!");
		}
		return true;
	}

	private boolean drawLottery(JSONObject draw_lottery, Map<String, Object> map) {
		Random random = new Random();  
		int num = random.nextInt(100);
		JSONArray prize = draw_lottery.getJSONArray("prize");
		int sum = 0;
		for (Object object : prize) {
			JSONObject prize_item = (JSONObject) object;
			Integer percent = prize_item.getInteger("percent");
			sum += percent;
			if (sum > num) {
				Integer count = prize_item.getInteger("count");
				if (count == 0) return false;
				
				if (count == -1) {
					prize_item.put("count", -1);
				} else {
					prize_item.put("count", count-1);
				}
				globalSettingService.update(GlobalSettingName.DRAW_LOTTERY, draw_lottery.toJSONString());
				
				map.put("type", prize_item.getInteger("type"));
				map.put("related_id", prize_item.getLong("related_id") == null ? -1L : prize_item.getLong("related_id"));
				map.put("jiu_coin", prize_item.getInteger("jiu_coin") == null ? 0 : prize_item.getInteger("jiu_coin"));
				map.put("id", prize_item.getLong("id"));
				map.put("name", prize_item.getString("name") == null ? "" : prize_item.getString("name"));
				map.put("rank_name", prize_item.getString("rank_name") == null ? "" : prize_item.getString("rank_name"));
				return true;
			}
		}
		
		return false;
	}

	private JSONObject updateLotteryCount(long current_time) throws ParseException {
		JSONObject draw_lottery = globalSettingService.getJsonObjectNoCache(GlobalSettingName.DRAW_LOTTERY);
		JSONArray prize = draw_lottery.getJSONArray("prize");
		boolean flag = false;
		for (Object object : prize) {
			JSONObject prize_item = (JSONObject) object;
			if (!needAdjust(prize_item)){
				continue;
			}
				
			if (adjustCount(prize_item, current_time)) {
				flag = true;
			}
		}
		
		if (flag) {
			globalSettingService.update(GlobalSettingName.DRAW_LOTTERY, draw_lottery.toJSONString());
		}
		
		return draw_lottery;
	}

	private boolean adjustCount(JSONObject prize_item, long current_time) throws ParseException {
		Integer adjust_type = prize_item.getInteger("adjust_type");
		Long last_adjust_time = prize_item.getLong("last_adjust_time");
		Integer adjust_time = prize_item.getInteger("adjust_time");
		Integer adjust_count = prize_item.getInteger("adjust_count");
		
		if (adjust_type == 1) {
			if (onRightTime(last_adjust_time, adjust_time, current_time)) {
				long gap_minutes = Math.abs(current_time - last_adjust_time) / 1000 / 60; 
				int times = (int) (gap_minutes / adjust_time);
				
				prize_item.put("count", prize_item.getInteger("count") + times * adjust_count);
				prize_item.put("last_adjust_time", last_adjust_time + times * adjust_time * 60 * 1000);
				drawLotteryMapper.updateLastAdjustTime(prize_item.getLong("id"), last_adjust_time + times * adjust_time * 60 * 1000);
				return true;
			}
		} else if(adjust_type == 2) {
			if (onFixTime(last_adjust_time, adjust_time, current_time)) {
				DateTime dateTime = new DateTime(current_time);
				DateTime dTime = null;
				if (adjust_time == -1) {
					dTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), dateTime.getHourOfDay(), 0);
				} else {
					dTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), adjust_time, 0);
				}
				
				prize_item.put("count", adjust_count);
				prize_item.put("last_adjust_time", dTime.getMillis());
				drawLotteryMapper.updateLastAdjustTime(prize_item.getLong("id"), dTime.getMillis());
				return true;
			}
		}
		return false;
	}

	private boolean onFixTime(Long last_adjust_time, Integer time, long current_time) {
		if ((current_time - last_adjust_time) >= DateUtils.MILLIS_PER_DAY )
			return true;
		if (time == -1) {
			if ((current_time - last_adjust_time) > DateUtils.MILLIS_PER_HOUR) {
				return true;
			}
		} else {
			if ((current_time - last_adjust_time) > DateUtils.MILLIS_PER_DAY) {
				return true;
			}
		}
		
		return false;
	}

	private boolean onRightTime(Long last_adjust_time, Integer time, long current_time) {
		long gap_minutes = (current_time - last_adjust_time) / 1000 / 60;
		if (gap_minutes >= time) {
			return true;
		}
		return false;
	}

	private boolean needAdjust(JSONObject prize_item) {
		Integer adjust_status = prize_item.getInteger("adjust_status");
		return adjust_status == 0;
	}

	private boolean isOnActivityTime(JSONObject draw_lottery, long current_time) throws ParseException {
		String acticity_start = draw_lottery.getString("acticity_start");
		String activity_end = draw_lottery.getString("activity_end");
		if (DateUtil.parseStrTime2Long(acticity_start) > current_time || DateUtil.parseStrTime2Long(activity_end) < current_time) {
			return false;
		}
		return true;
	}

	public Map<String, Object> getLotteryList() {
		Map<String, Object> map = new HashMap<>();
		JSONObject draw_lottery = globalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY);
		JSONArray prize = draw_lottery.getJSONArray("prize");
		if (prize == null || prize.size() < 1) {
			return new HashMap<>();
		}
		DrawLottery drawLottery = drawLotteryMapper.getFirstLottery();
		if (drawLottery == null) {
			return new HashMap<>();
		}
		Long id = drawLottery.getId();
		
		map.put("first_list", assembleDrawLotteryLog(drawLotteryLogMapper.getFirstPrice(id, 20), drawLottery));
		map.put("second_list", assembleDrawLotteryLog(drawLotteryLogMapper.getOtherPrice(id, 40), drawLottery));
		
		return map;
	}

	private List<Map<String, Object>> assembleDrawLotteryLog(List<DrawLotteryLog> drawLotteryLogs, DrawLottery drawLottery) {
		List<Map<String, Object>> results = new ArrayList<>();
		for (DrawLotteryLog drawLotteryLog : drawLotteryLogs) {
			Map<String, Object> log = new HashMap<>();
			log.put("name", drawLotteryLog.getName());
			log.put("rank_name", drawLotteryLog.getRankName());
			User user = userService.getUser(drawLotteryLog.getUserId());

			if (user.getUserPhone() == null) {
				continue;
			}
			log.put("user_phone", user.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
//			log.put("coupon_name", couponTemplateService.search(drawLottery.getRelatedId()).getName());
			results.add(log);
		}
		return results;
	}

	public List<Map<String, Object>> getDrawRecordByUser(long userId, PageQuery pageQuery, int status, int type) {
		List<DrawLotteryLog> logs = drawLotteryLogMapper.getByUser(userId, pageQuery, status, type);
		List<Map<String, Object>> results = new ArrayList<>();
    	for (DrawLotteryLog log : logs) {
    		Map<String, Object> result = new HashMap<>();
    		result.put("name", log.getName());
    		result.put("time", log.getCreateTime());
    		result.put("rank_name", log.getRankName());
    		result.put("gain_coin", log.getCount());
    		
    		DrawLottery drawLottery = drawLotteryService.getById(log.getRelatedId());
    		CouponTemplate couponTemplate = couponTemplateService.search(drawLottery.getRelatedId());
    		result.put("coupon_name", couponTemplate == null ? "": couponTemplate.getName());
    		if (drawLottery.getProductId() != null) {
    			result.put("product_id", drawLottery.getProductId());
			}
    		results.add(result);
    	}
    	
    	return results;
	}

	public List<Map<String, Object>> getLotteryUsersLogs(PageQuery pageQuery, int status, int type) {
		List<Map<String, Object>> results = new ArrayList<>();
		List<DrawLotteryLog> list = drawLotteryLogMapper.getAllUser(pageQuery, 0, 1);
		for (DrawLotteryLog drawLotteryLog : list) {
			Map<String, Object> result = new HashMap<>();
			result.put("name", drawLotteryLog.getName());
			result.put("rank_name", drawLotteryLog.getRankName());
			User user = userService.getUser(drawLotteryLog.getUserId());
			
			if (user.getUserPhone() == null) {
				continue;
			}
			result.put("user_phone", user.getUserPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
			
			results.add(result);
		}
		return results;
	}

	public Object getLotteryInfo() {
		JSONObject draw_lottery = globalSettingService.getJsonObject(GlobalSettingName.DRAW_LOTTERY);
		for (Object obj : draw_lottery.getJSONArray("prize")) {
			JSONObject prize_item = (JSONObject) obj;
			CouponTemplate couponTemplate = couponTemplateService.search(prize_item.getLong("related_id"));
			if (couponTemplate == null) {
				continue;
			}
			prize_item.put("coupon_name", couponTemplate.getName());
			prize_item.put("coupon_money", couponTemplate.getMoney());
			prize_item.put("limit_money", couponTemplate.getLimitMoney());
			prize_item.put("coupon_type", couponTemplate.getRangeType());
		}
		return draw_lottery;
	}

}
