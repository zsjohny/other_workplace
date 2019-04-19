package com.yujj.business.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.VersionUtil;
import com.yujj.dao.mapper.OrderCouponMapper;
import com.yujj.dao.mapper.OrderItemMapper;
import com.yujj.dao.mapper.UserCoinLogMapper;
import com.yujj.dao.mapper.UserCoinMapper;
import com.yujj.entity.account.UserCoinLog;
import com.yujj.entity.account.UserDetail;

@Service
public class UserCoinService {
	private static final Logger logger = LoggerFactory.getLogger(UserCoinService.class);
	@Autowired
	private UserCoinMapper userCoinMapper;

	@Autowired
	private UserCoinLogMapper userCoinLogMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderCouponMapper orderCouponMapper;

	@Autowired
	private MemcachedService memcachedService;

	@Autowired
	private GlobalSettingService globalSettingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JiuCoinExchangeLogService jiuCoinExchangeLogService;
	
	public UserCoin getUserCoin(long userId) {
		return userCoinMapper.getUserCoin(userId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateUserCoin(long userId, int diffAvalCoins, int diffUnavalCoins, String relatedId, long time,
			UserCoinOperation operation, String content, String version) {
		logger.info("分享注册,userId[{}]",userId);
		if (VersionUtil.compareVersion(version, "2.0.0") < 0) {
			return;
		}
		
		if (diffUnavalCoins == 0) {
			return;
		}
		String msg = new StringBuilder("update user coin error!").append("userId:" + userId).append(", ")
				.append("operation:" + operation).append(", ").append("diffAvalCoins:" + diffAvalCoins).append(", ")
				.append("diffUnavalCoins:" + diffUnavalCoins).toString();

		int oldAvalCoins = 0;
		int oldUnavalCoins = 0;
		logger.info("分享注册1,userId[{}],operation[{}]",userId,operation);
		UserCoin userCoin = getUserCoin(userId);
		if (userCoin != null) {
			oldAvalCoins = userCoin.getAvalCoins();
			oldUnavalCoins = userCoin.getUnavalCoins();
		}
		int newAvalCoins = oldAvalCoins + diffAvalCoins;
		int newUnavalCoins = oldUnavalCoins + diffUnavalCoins;
		if (newAvalCoins < 0 || newUnavalCoins < 0) {
			throw new IllegalArgumentException(msg);
		}
		logger.info("分享注册2,userId[{}],operation[{}]",userId,operation);
		int count = 0;
		if (userCoin == null) {
			userCoin = new UserCoin();
			userCoin.setAvalCoins(newAvalCoins);
			userCoin.setUnavalCoins(newUnavalCoins);
			userCoin.setUserId(userId);
			userCoin.setCreateTime(time);
			userCoin.setUpdateTime(time);
			count = userCoinMapper.addUserCoin(userCoin);
		} else {
			// count = userCoinMapper.updateUserCoinNew(userId, 0, time);
			count = userCoinMapper.updateUserCoinNew(userId, diffUnavalCoins, time);
		}
		if (count != 1) {
			throw new IllegalStateException(msg);
		}
		logger.info("分享注册3,userId[{}],operation[{}]",userId,operation);
		UserCoinLog userCoinLog = new UserCoinLog();
		userCoinLog.setUserId(userId);
		userCoinLog.setOperation(operation);
		userCoinLog.setOldAvalCoins(oldAvalCoins);
		userCoinLog.setNewAvalCoins(newAvalCoins);
		userCoinLog.setOldUnavalCoins(oldUnavalCoins);
		userCoinLog.setNewUnavalCoins(newUnavalCoins);
		userCoinLog.setRelatedId(relatedId);
		userCoinLog.setCreateTime(time);
		userCoinLog.setContent(content);
		userCoinLogMapper.addUserCoinLog(userCoinLog);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> pointChangeList(PageQuery pageQuery, UserDetail userDetail) throws ParseException {
		Map<String, Object> data = new HashMap<String, Object>();
		// getMemCache
		String groupKey = MemcachedKey.GROUP_KEY_COIN_CHANGE_LIST;
		String key = userDetail.getUserId() + "all" + pageQuery.getPage();
		Map<String, Object> obj = (Map<String, Object>) memcachedService.get(groupKey, key);
		if (obj != null) {

			data.put("userCoin", obj.get("userCoin"));
			data.put("allList", obj.get("allList"));
			data.put("pageQuery", obj.get("pageQuery"));
			data.put("updateNum", obj.get("updateNum"));
			data.put("expiration_tip", obj.get("expiration_tip"));
			data.put("history_total_coin", obj.get("history_total_coin"));
			data.put("jiucoin_strategy", obj.get("jiucoin_strategy"));
			data.put("record_count", obj.get("record_count"));

		} else {
			// 获取用户玖币数
			UserCoin userCoin = this.getUserCoin(userDetail.getUserId());
			data.put("userCoin", userCoin);
			
			JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
			JSONObject jiuCoinAttribute = jiucoin_global_setting.getJSONObject("jiuCoinAttribute");
			String nextExpirationTime = jiuCoinAttribute.getString("nextExpirationTime");
			long endTime = DateUtil.parseStrTime2Long(nextExpirationTime);
			SimpleDateFormat sdf = new SimpleDateFormat("y/M/d");
			long current = System.currentTimeMillis();
			if (endTime >= current && endTime - current <= DateUtils.MILLIS_PER_DAY * 30) {
				data.put("expiration_tip", sdf.format(new Date(endTime)) + "将过期" + userCoin.getUnavalCoins() + "玖币，请尽快使用");
			} else {
				data.put("expiration_tip", "");
			}
			
			// 获取用户zong列表
			int totalCount = this.getAllListCount(userDetail);
			PageQueryResult pageQueryResultTotal = PageQueryResult.copyFrom(pageQuery, totalCount);
			data.put("pageQuery", pageQueryResultTotal);

			data.put("page", pageQuery.getPage());
			List<UserCoinLog> coinLogListAll = this.allLogList(userDetail, pageQuery);
			Collections.sort(coinLogListAll, new Comparator<UserCoinLog>() {
				public int compare(UserCoinLog o1, UserCoinLog o2) {
					if(o1.getId() < o2.getId()){
						return 1;
					}else if(o1.getId() > o2.getId()){
						return -1;
					}else return 0;
					
				}
			});
			
//						setCoinLogMonthHead(coinLogListAll);
			data.put("allList", coinLogListAll);
			data.put("jiucoin_strategy", "static/app/help.html");
			
			data.put("history_total_coin", this.getHistoryTotalCoin(userDetail.getUserId()));
			data.put("record_count", jiuCoinExchangeLogService.searchCount(userDetail, -1));

			// 更新记录为已阅读
			int updateNum = this.updateReadStatus(userDetail, 1);
			data.put("updateNum", updateNum);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, data);
		}
		return data;
	}
	private int getHistoryTotalCoin(long userId) {
		Integer count = userCoinLogMapper.getHistoryTotalCoin(userId);
		return count == null ? 0 : count;
	}

	public Map<String, Object> pointChangeIncreaseList(PageQuery pageQuery, UserDetail userDetail) {
		Map<String, Object> data = new HashMap<String, Object>();
		// getMemCache
		String groupKey = MemcachedKey.GROUP_KEY_COIN_CHANGE_LIST;
		String key = userDetail.getUserId() + "increase" + pageQuery.getPage();
		Map<String, Object> obj = (Map<String, Object>) memcachedService.get(groupKey, key);
		if (obj != null) {
			
			data.put("userCoin", obj.get("userCoin"));
			data.put("pageQuery", obj.get("pageQuery"));
			data.put("increaseList", obj.get("increaseList"));
			
			data.put("pageQuery", obj.get("pageQueryIncrease"));
			data.put("updateNum", obj.get("updateNum"));
			
		} else {
			
			// 获取用户玖币数
			UserCoin userCoin = this.getUserCoin(userDetail.getUserId());
			data.put("userCoin", userCoin);
			
			
			// 获取用户玖币增加列表
			int totalCount = this.getIncreaseListCount(userDetail);
			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
			data.put("pageQuery", pageQueryResult);
			
			List<UserCoinLog> coinLogList = this.increaseList(userDetail, pageQuery);
			
//			setCoinLogMonthHead(coinLogList);
			data.put("increaseList", coinLogList);
	
			
			// 更新记录为已阅读
			int updateNum = this.updateReadStatus(userDetail, 1);
			data.put("updateNum", updateNum);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, data);
		}
		return data;
	}
	public Map<String, Object> pointChangeReduceList(PageQuery pageQuery, UserDetail userDetail) {
		Map<String, Object> data = new HashMap<String, Object>();
		// getMemCache
		String groupKey = MemcachedKey.GROUP_KEY_COIN_CHANGE_LIST;
		String key = userDetail.getUserId() + "reduce" + pageQuery.getPage();
		Map<String, Object> obj = (Map<String, Object>) memcachedService.get(groupKey, key);
		if (obj != null) {
			
			data.put("userCoin", obj.get("userCoin"));
			data.put("pageQuery", obj.get("pageQuery"));
			
			data.put("reduceList", obj.get("reduceList"));
			
			data.put("pageQuery", obj.get("pageQuery"));
			data.put("updateNum", obj.get("updateNum"));
			
		} else {
			
			// 获取用户玖币数
			UserCoin userCoin = this.getUserCoin(userDetail.getUserId());
			data.put("userCoin", userCoin);
			
		
			
			
			// 获取用户玖币减少列表
			int totalCount = this.getReduceListCount(userDetail);
			PageQueryResult pageQueryResultReduce = PageQueryResult.copyFrom(pageQuery, totalCount);
			data.put("pageQuery", pageQueryResultReduce);
			
			List<UserCoinLog> coinLogListReduce = this.reduceList(userDetail, pageQuery);
			
//			setCoinLogMonthHead(coinLogListReduce);
			data.put("reduceList", coinLogListReduce);
			
			
			// 更新记录为已阅读
			int updateNum = this.updateReadStatus(userDetail, 1);
			data.put("updateNum", updateNum);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, data);
		}
		return data;
	}
	//删除旧表
//	@SuppressWarnings("unchecked")
//	public Map<String, Object> pointExchangeList(PageQuery pageQuery, UserDetail userDetail, int type) throws IllegalAccessException, InvocationTargetException {
//		Map<String, Object> data = new HashMap<String, Object>();
//		// getMemCache
//		String groupKey = MemcachedKey.GROUP_KEY_COIN_CHANGE_LIST;
//		String key = userDetail.getUserId() + "exchange" + pageQuery.getPage() + type;
//		Map<String, Object> obj = (Map<String, Object>) memcachedService.get(groupKey, key);
//		if (obj != null) {
//			data.put("exchangeList", obj.get("exchangeList"));
//			data.put("pageQuery", obj.get("pageQuery"));
//		} else {
//			// 获取用户兑换记录
//			int totalCount = this.getExchangeListCount(userDetail, type);
//			PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//			data.put("pageQuery", pageQueryResult);
//			
//			List<UserCoinLog> coinLogList = this.exchangeList(userDetail, pageQuery, type);
//			
//			for(UserCoinLog userCoinLog : coinLogList) {
//				if(userCoinLog.getOperation() != null && userCoinLog.getOperation() == UserCoinOperation.CLOTHES_EXCHANGE){
//					 List<OrderItem>  items = orderItemMapper.getOrderItems(userDetail.getUserId(), CollectionUtil.createList(Long.parseLong(userCoinLog.getRelatedId())));
//						 
//					 Iterator<OrderItem> stuIter = items.iterator();
//					 while (stuIter.hasNext()) {  
//						OrderItem orderItem = stuIter.next();  
//				        if (orderItem.getTotalUnavalCoinUsed() == 0) {
//							stuIter.remove();  //移除用钱购买的商品	
//				        }
//					 }
//					 userCoinLog.setOrderItemList(items);
//					 userCoinLog.setCount(items.size());
//					    
//				} else if(userCoinLog.getOperation() != null && userCoinLog.getOperation() == UserCoinOperation.COUPON_EXCHANGE){
//					Coupon coupon = orderCouponMapper.getCouponById(Long.parseLong(userCoinLog.getRelatedId()), userDetail.getUserId());
//					userCoinLog.setCoupon(coupon);
//					userCoinLog.setCount(1);
//				}
//			}
//			
//			data.put("exchangeList", coinLogList);
//			
//			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, data);
//		}
//		return data;
//	}

	//设置积分记录起始月份
	public List<UserCoinLog> setCoinLogMonthHead(List<UserCoinLog> coinLogList) {
			String month = "0";
			String monthLog = "0";
			for (UserCoinLog coinLog : coinLogList) {
				monthLog = UtilDate.getDateStrFromMillis(coinLog.getCreateTime(),"MM");
			if (!monthLog.equals(month)) {
	
				coinLog.setMonthBegin(monthLog + "月");
				month = monthLog;
			}else{
				coinLog.setMonthBegin("");
			}
	
		}
			return coinLogList;
	}
	
	public int getUserCoinLogCount(long userId, long startTime, long endTime, UserCoinOperation operation) {
		return userCoinLogMapper.getUserCoinLogCount(userId, startTime, endTime, operation);
	}

	public int getUserCoinLogCountByRelatedId(long userId, String relatedId, UserCoinOperation operation) {
		return userCoinLogMapper.getUserCoinLogCountByRelatedId(userId, relatedId, operation);
	}

	// public List<UserCoinLog> increaseList(UserDetail userDetail) {
	// return userCoinLogMapper.increaseList(userDetail.getUserId());
	// }

	public int getIncreaseListCount(UserDetail userDetail) {
		return userCoinLogMapper.getIncreaseListCount(userDetail.getUserId());
	}

	public int updateReadStatus(UserDetail userDetail, int status) {
		return userCoinLogMapper.updateReadStatus(userDetail.getUserId(), status);
	}

	public List<UserCoinLog> increaseList(UserDetail userDetail, PageQuery pageQuery) {
		return userCoinLogMapper.increaseList(userDetail.getUserId(), pageQuery);
	}

	public int getReduceListCount(UserDetail userDetail) {
		return userCoinLogMapper.getReduceListCount(userDetail.getUserId());
	}
	
	public int getExchangeListCount(UserDetail userDetail, int type) {
		return userCoinLogMapper.getExchangeListCount(userDetail.getUserId(), type);
	}
	
	public List<UserCoinLog> allLogList(UserDetail userDetail, PageQuery pageQuery) {
		return userCoinLogMapper.getAllList(userDetail.getUserId(), pageQuery);
	}
	
	public int getAllListCount(UserDetail userDetail) {
		return userCoinLogMapper.getAllListCount(userDetail.getUserId());
	}

	public int countNewReadStatus(UserDetail userDetail) {
		return userCoinLogMapper.countNewReadStatus(userDetail.getUserId());
	}

	public List<UserCoinLog> reduceList(UserDetail userDetail, PageQuery pageQuery) {
		return userCoinLogMapper.reduceList(userDetail.getUserId(), pageQuery);
	}
	public List<UserCoinLog> exchangeList(UserDetail userDetail, PageQuery pageQuery, int type) {
		return userCoinLogMapper.exchangeList(userDetail.getUserId(), pageQuery, type);
	}

	public int getUserCoinSameLogCount(long userId, Long startTime, Long endTime, UserCoinOperation operation,
			long relatedId) {
		return userCoinLogMapper.getUserCoinSameLogCount(userId, startTime, endTime, operation, relatedId);
	}

	public int getTotalUserCoin(long userId, long startTime, long endTime, UserCoinOperation operation) {
		Integer totalCoin = userCoinLogMapper.getTotalUserCoin(userId, startTime, endTime, operation);
		return totalCoin == null ? 0 : totalCoin;
	}

}
