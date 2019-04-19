package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.FirstDiscountStatisticsDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.meta.account.User;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.entity.FirstDiscountStatisticsDayBean;
import com.jiuyuan.entity.OrderFirstDiscountLog;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;

/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午10:00:24
*/
@Service
public class FirstDiscountStatisticsServiceImpl implements FirstDiscountStatisticsService{

	@Autowired
	private FirstDiscountStatisticsDao firstDiscountStatisticsDao;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<FirstDiscountStatisticsDayBean> searchDayStatistics() {
		//获得今日0点时间
		long currentTimeMillis = System.currentTimeMillis();
		long todayZeroTime = getDayStartTime(System.currentTimeMillis(), 0);
		FirstDiscountStatisticsDayBean searchTodayStatistics = searchTodayStatistics(todayZeroTime, System.currentTimeMillis());
		searchTodayStatistics.setName("今天");
		FirstDiscountStatisticsDayBean search1DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 1), todayZeroTime);
		search1DayStatistics.setName("昨天");
		FirstDiscountStatisticsDayBean search3DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 3), todayZeroTime);				
		search3DayStatistics.setName("近3日");
		FirstDiscountStatisticsDayBean search7DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 7), todayZeroTime);
		search7DayStatistics.setName("近7日");
		FirstDiscountStatisticsDayBean search30DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 30), todayZeroTime);
		search30DayStatistics.setName("近30日");
		FirstDiscountStatisticsDayBean search90DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 90), todayZeroTime);
		search90DayStatistics.setName("近90日");
		FirstDiscountStatisticsDayBean search180DayStatistics = firstDiscountStatisticsDao.searchDayStatistics(getDayStartTime(currentTimeMillis, 180), todayZeroTime);
		search180DayStatistics.setName("近180日");
		FirstDiscountStatisticsDayBean searchAllDayStatistics = firstDiscountStatisticsDao.searchDayStatistics(0, todayZeroTime);
		searchAllDayStatistics.setName("全部");
		
		List<FirstDiscountStatisticsDayBean> list = new ArrayList<>();
		list.add(searchTodayStatistics);
		list.add(search1DayStatistics);
		list.add(search3DayStatistics);
		list.add(search7DayStatistics);
		list.add(search30DayStatistics);
		list.add(search90DayStatistics);
		list.add(search180DayStatistics);
		list.add(searchAllDayStatistics);
		return list;
	}

	@Override
	public FirstDiscountStatisticsDayBean searchTodayStatistics(long startTime, long endTime) {
		return firstDiscountStatisticsDao.searchTodayStatistics(startTime, endTime);
	}

	/**
	 * 按时间区间查询
	 */
	@Override
	public FirstDiscountStatisticsDayBean timeIntervalStatistics(long startTime, long endTime) {
		//得到今日0点毫秒
		long todayZeroTime = DateUtil.getDayZeroTime(System.currentTimeMillis());
		FirstDiscountStatisticsDayBean todayBean = new FirstDiscountStatisticsDayBean();
		FirstDiscountStatisticsDayBean otherdayBean = new FirstDiscountStatisticsDayBean();
		
		if(endTime<todayZeroTime){		//不包括今天数据 直接查询日报表
			otherdayBean = firstDiscountStatisticsDao.searchDayStatistics(startTime, endTime);
		}else{							//结束时间大于今天
			
			if(startTime>=todayZeroTime){		//只查询今天数据
				todayBean = searchTodayStatistics(startTime, endTime);
			}else{						//今天数据+今天之前的日报表数据
				//今天数据
				todayBean = searchTodayStatistics(todayZeroTime, endTime);
				//昨天之前的报表数据
				otherdayBean = firstDiscountStatisticsDao.searchDayStatistics(startTime, todayZeroTime-1);
			}
		}
		
		return new FirstDiscountStatisticsDayBean(todayBean, otherdayBean);
	}

	/**
	 * 获得某一天前n天的0点时间
	 * @param currentTime
	 * @param day
	 * @return
	 */
	public long getDayStartTime(long currentTime,int day){
		long currentDayZeroTime = DateUtil.getDayZeroTime(currentTime);
		return currentDayZeroTime - DateUtils.MILLIS_PER_DAY * day;
	}

	@Override
	public Map<String, Object> searchRecord(long orderNo, String yjjNumber, double minMoney, double maxMoney,
			long startTime, long endTime,PageQuery pageQuery) {
		Map<String, Object> data = new HashMap<String, Object>();
		
		User user = new User();
		if (!TextUtils.isEmpty(yjjNumber)) {
			try{
				user = userDao.searchOne(Long.parseLong(yjjNumber));
			}catch (Exception e) {
			}
		}
		int count = firstDiscountStatisticsDao.searchRecordCount(orderNo, user.getUserId(), minMoney, maxMoney, startTime, endTime);
		List<OrderFirstDiscountLog> record = firstDiscountStatisticsDao.searchRecord(orderNo, user.getUserId(), minMoney, maxMoney, startTime, endTime, pageQuery);
		
		data.put("list", record);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
		data.put("total", pageQueryResult);
		return data;
	}
	
}
