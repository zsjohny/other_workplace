package com.jiuy.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.GrantJiuCoinLogDao;
import com.jiuy.core.dao.JiuCoinDeductDao;
import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.UserCoinDao;
import com.jiuy.core.dao.UserCoinLogDao;
import com.jiuy.core.dao.mapper.UserDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.meta.account.User;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.entity.GrantJiuCoinLog;
import com.jiuyuan.entity.JiuCoinDeductStatisticsDayBean;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月23日 上午11:29:03
*/
@Service
public class JiuCoinServiceImpl implements JiuCoinService {
	
	private static final int SUB_NUMBER = 50000;

	@Autowired
	private UserDao userDao;
	
    @Autowired
    private UserCoinDao userCoinDao;
    
    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private UserCoinLogDao userCoinLogDao;
	
	@Resource
	private GrantJiuCoinLogDao grantJiuCoinLogDao;
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Autowired
	private JiuCoinDeductDao jiuCoinDeductDao;
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public int updateJiuCoin(int type, int coins, Collection<Long> userList, String usersStr) {
		List<Long> userIds = new ArrayList<>();
		String userLogContent = "";
		switch (type) {
		case 1:				//指定用户
			userIds = userIdsOfYjjNumber(userList);
			if(userIds.size()>0){
				userLogContent = userIdsStr(userIds);
			}
			break;
			
		case 2:				//所有用户
			List<User> users = userDao.search();
			for(User user : users){
				userIds.add(user.getUserId());
			}
			break;
			
		case 3:				//排除用户
			List<User> users2 = userDao.excludeSearch(userList);
			for(User user : users2){
				userIds.add(user.getUserId());
			}
			
			if(userIds.size()>0){
				//排除的用户的userid
				List<Long> excludeUserIds = userIdsOfYjjNumber(userList);
				userLogContent = userIdsStr(excludeUserIds);
			}
			break;
			
		case 4:				//指定手机用户
			userIds = userIdsOfPhone(userList);
			if(userIds.size()>0){
				userLogContent = userIdsStr(userIds);
			}
			break;
		}
		
		//输入的用户数量与数据库数量不一致  发放用户信息有误
		if(type!=2){
			if(type==3){
				List<User> allUser = userDao.search();
				if(userIds.size() != (allUser.size()-userList.size())){
					return 0;
				}
			} else {
				if(userIds.size()!=userList.size()){
					return 0;
				}
			}
		}
		
		
		if(userIds.size()>0){
			//每次发放 SUB_NUMBER 名用户
			int count = userIds.size() / SUB_NUMBER;
			List<Long> subUserIds = new ArrayList<>();
			for(int i=0;i<=count;i++){
				if(i==count){
					subUserIds = userIds.subList(i*SUB_NUMBER, userIds.size());
				}else{
					subUserIds = userIds.subList(i*SUB_NUMBER, (i+1)*SUB_NUMBER);
				}
				if(subUserIds!=null && subUserIds.size()>0){
					userCoinDao.addAndUpdateUserCoin(subUserIds, 0, coins, System.currentTimeMillis());
				}
			}
			
			//玖币发放日志
			GrantJiuCoinLog grantJiuCoinLog = new GrantJiuCoinLog(type, usersStr, coins, userIds.size(), coins * userIds.size(), System.currentTimeMillis());
			grantJiuCoinLogDao.addGrantJiuCoinLog(grantJiuCoinLog);
			//用户玖币日志
			userCoinLogDao.addGrantUserCoinLog(UserCoinOperation.GRANT_JIUCOIN.getIntValue(), coins, grantJiuCoinLog.getId(), System.currentTimeMillis(), userLogContent, type);
			
			return 1;
		}else {
			return 0;
		}
		
	}
	
	private String userIdsStr(List<Long> userIds){
		if(userIds == null || userIds.size()==0){
			return "";
		}
		StringBuffer stringBuffer = new StringBuffer();
		for(Long userId : userIds){
			stringBuffer.append(userId + ",");
		}
		return stringBuffer.substring(0, stringBuffer.length()-1);
	}

	private List<Long> userIdsOfYjjNumber(Collection<Long> userList){
		List<User> users = userDao.search(userList);
		ArrayList<Long> userIds = new ArrayList<Long>();
		for(User user : users){
			userIds.add(user.getUserId());
		}
		return userIds;
	}
	
	private List<Long> userIdsOfPhone(Collection<Long> userList){
		List<User> users = userDao.usersOfPhones(userList);
		ArrayList<Long> userIds = new ArrayList<Long>();
		for(User user : users){
			userIds.add(user.getUserId());
		}
		return userIds;
	}

	@Override
	public int batchUpdateJiuCoinDeduction(Collection<Long> productIds,double deductPercent) {
		return productMapper.updateProductDeductPercent(productIds, deductPercent);
	}

	@Override
	public Map<String, Object> searchDeductDetailRecord(PageQuery pageQuery,long productId, String productName, long yjjNumber,
			long startTime, long endTime) {
		Map<String, Object> data = new HashMap<String, Object>();
		ArrayList<Long> productIds = new ArrayList<Long>();
		if(productId!=0){
			productIds.add(productId);
		}
		//根据商品名称找到商品id
		if(!TextUtils.isEmpty(productName)){
			List<Product> productIdsOfName = productMapper.getProductByName(productName);
			if(productIdsOfName!=null && productIdsOfName.size()>0){
				for (Product product : productIdsOfName) {
					productIds.add(product.getId());
				}
			}
		}
		
		//根据俞姐姐号找到userId
		List<Long> userIds = new ArrayList<>();
		if(yjjNumber!=0){
			List<User> users = userDao.fuzzySearchUserByYJJNumber(yjjNumber);
			if(users != null && users.size() > 0){
				for (User user : users) {
					userIds.add(user.getUserId());
				}
			}
		}
		
		int count = orderItemDao.searchDeductDetailRecordCount(productIds.size()>0?productIds:null, userIds.size()>0?userIds:null, startTime, endTime);
		List<Map<String, Object>> record = orderItemDao.searchDeductDetailRecord(pageQuery,productIds.size()>0?productIds:null, userIds.size()>0?userIds:null, startTime, endTime);
		
		data.put("list", record);
		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, count);
		data.put("total", pageQueryResult);
		return data;
	}

	@Override
	public List<Product> searchDeductionProduct() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JiuCoinDeductStatisticsDayBean> sumDayStatistics() {
		//获得今日0点时间
		long currentTimeMillis = System.currentTimeMillis();
		long todayZeroTime = getDayStartTime(System.currentTimeMillis(), 0);
		
		JiuCoinDeductStatisticsDayBean sumTodayStatistics = jiuCoinDeductDao.sumTimeStatistics(todayZeroTime, currentTimeMillis);
		sumTodayStatistics.setName("今天");
		JiuCoinDeductStatisticsDayBean sum1DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 1), todayZeroTime);
		sum1DayStatistics.setName("昨天");
		JiuCoinDeductStatisticsDayBean sum3DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 3), todayZeroTime);
		sum3DayStatistics.setName("近3天");
		JiuCoinDeductStatisticsDayBean sum7DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 7), todayZeroTime);
		sum7DayStatistics.setName("近7天");
		JiuCoinDeductStatisticsDayBean sum30DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 30), todayZeroTime);
		sum30DayStatistics.setName("近30天");
		JiuCoinDeductStatisticsDayBean sum90DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 90), todayZeroTime);
		sum90DayStatistics.setName("近90天");
		JiuCoinDeductStatisticsDayBean sum180DayStatistics = jiuCoinDeductDao.sumDayStatistics(getDayStartTime(currentTimeMillis, 180), todayZeroTime);
		sum180DayStatistics.setName("近180天");
		JiuCoinDeductStatisticsDayBean sumAllDayStatistics = jiuCoinDeductDao.sumDayStatistics(0, todayZeroTime);
		sumAllDayStatistics.setName("全部");
		
		ArrayList<JiuCoinDeductStatisticsDayBean> list = new ArrayList<>();
		list.add(sumTodayStatistics);
		list.add(sum1DayStatistics);
		list.add(sum3DayStatistics);
		list.add(sum7DayStatistics);
		list.add(sum30DayStatistics);
		list.add(sum90DayStatistics);
		list.add(sum180DayStatistics);
		list.add(sumAllDayStatistics);
		
		return list;
	}
	
	@Override
	public JiuCoinDeductStatisticsDayBean sumTimeStatistics(long startTime,long endTime){
		return jiuCoinDeductDao.sumTimeStatistics(startTime, endTime);
	}
	
	/**
	 * 按时间区间查询
	 */
	@Override
	public JiuCoinDeductStatisticsDayBean timeIntervalStatistics(long startTime, long endTime) {
		//得到今日0点毫秒
		long todayZeroTime = DateUtil.getDayZeroTime(System.currentTimeMillis());
		JiuCoinDeductStatisticsDayBean todayBean = new JiuCoinDeductStatisticsDayBean();
		JiuCoinDeductStatisticsDayBean otherdayBean = new JiuCoinDeductStatisticsDayBean();
		
		if(endTime<todayZeroTime){		//不包括今天数据 直接查询日报表
			otherdayBean = jiuCoinDeductDao.sumDayStatistics(startTime, endTime);
		}else{							//结束时间大于今天
			
			if(startTime>=todayZeroTime){		//只查询今天数据
				todayBean = sumTimeStatistics(startTime, endTime);
			}else{						//今天数据+今天之前的日报表数据
				//今天数据
				todayBean = sumTimeStatistics(todayZeroTime, endTime);
				//昨天之前的报表数据
				otherdayBean = jiuCoinDeductDao.sumDayStatistics(startTime, todayZeroTime-1);
			}
		}
		
		return new JiuCoinDeductStatisticsDayBean(todayBean, otherdayBean);
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
	
}
