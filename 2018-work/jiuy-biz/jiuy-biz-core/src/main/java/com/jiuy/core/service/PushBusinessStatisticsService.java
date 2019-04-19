package com.jiuy.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.StoreOrderDao;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.storeorder.StoreOrder;
import com.jiuyuan.util.CollectionUtil;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午4:28:28
*/
@Service
public class PushBusinessStatisticsService {
	
	private static final Logger logger = Logger.getLogger(PushBusinessStatisticsService.class);

	@Resource
	private StoreBusinessDao storeBusinessDao;
	
	@Resource
	private StoreOrderDao storeOrderDao;
	
	/**
	 * 获取推荐人统计数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getReferrerList(long startTime, long endTime) {
		List<StoreBusiness> pushBusinessStatisticsList = storeBusinessDao.getReferrerPhoneNumberStoreList();
		Map<String,List<StoreBusiness>> pushBusinessStatisticsMap = new HashMap<String,List<StoreBusiness>>();
		for (StoreBusiness storeBusiness : pushBusinessStatisticsList) {
			String phone = storeBusiness.getOrganizationNo();
			List<StoreBusiness> storeBusinessList = new ArrayList<StoreBusiness>();
			if(pushBusinessStatisticsMap.containsKey(phone)){
				storeBusinessList = pushBusinessStatisticsMap.get(phone);
			}
			storeBusinessList.add(storeBusiness);
			pushBusinessStatisticsMap.put(phone, storeBusinessList);
		}
		return getData(pushBusinessStatisticsMap,startTime,endTime,"phone");
	}
	

	/**
	 * 获取地区统计数据
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<Map<String, Object>> getAreaList(long startTime, long endTime) {
		List<StoreBusiness> pushBusinessStatisticsList = storeBusinessDao.getReferrerPhoneNumberStoreList();
		Map<String,List<StoreBusiness>> pushBusinessStatisticsMap = new HashMap<String,List<StoreBusiness>>();
		for (StoreBusiness storeBusiness : pushBusinessStatisticsList) {
			String province = storeBusiness.getProvince();
			List<StoreBusiness> storeBusinessList = new ArrayList<StoreBusiness>();
			if(pushBusinessStatisticsMap.containsKey(province)){
				storeBusinessList = pushBusinessStatisticsMap.get(province);
			}
			storeBusinessList.add(storeBusiness);
			pushBusinessStatisticsMap.put(province, storeBusinessList);
		}
		return getData(pushBusinessStatisticsMap,startTime,endTime,"province");
	}
	
	/**
	 * 封装数据
	 * @param pushBusinessStatisticsMap
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	private List<Map<String,Object>> getData(Map<String,List<StoreBusiness>> pushBusinessStatisticsMap,long startTime, long endTime,String keyWord){
		long startBeginTime = startTime;
		long endBeginTime = endTime;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
		for (String key : pushBusinessStatisticsMap.keySet()) {
			startTime = startBeginTime;
			endTime = endBeginTime;
			List<StoreBusiness> list = pushBusinessStatisticsMap.get(key);
			Map<String,Object> map = new HashMap<String,Object>();
			int allRegisterCount = list.size();
			int allRctivateCount = 0;
			if("phone".equals(keyWord)){
				//获取推荐人总激活数
				allRctivateCount = storeOrderDao.getReferrerAllRctivateCount(key);
			}else if("province".equals(keyWord)){
				//获取地区总激活数
				allRctivateCount = storeOrderDao.getAreaAllRctivateCount(key);
			}
			map.put(keyWord, key);
			map.put("allRegisterCount",allRegisterCount);
			map.put("allRctivateCount", allRctivateCount);
//			int index = 1;
			while(startTime<endTime){
				long time = startTime + 60 * 60 * 24 * 1000;
				String today = simpleDateFormat.format(new Date(startTime));
				int registerCount = 0;
				int activateCount = 0;
				for (StoreBusiness storeBusiness : list) {
					long createTime = storeBusiness.getCreateTime();
					if(createTime>=startTime && createTime<endTime){
						registerCount += 1;
					}
					List<StoreOrder> storeOrderList = storeOrderDao.getByStoreIds(CollectionUtil.createList(storeBusiness.getId()));
					long storeOrderStartTime = Long.MAX_VALUE;
					for (StoreOrder storeOrder : storeOrderList) {
						long payTime = storeOrder.getPayTime();
						if(payTime!=0 && storeOrderStartTime>payTime){
							storeOrderStartTime = payTime;
						}
					}
					if(storeOrderStartTime>=startTime && storeOrderStartTime<endTime){
						activateCount += 1;
					}
				}
				map.put(today + "RegisterCount", registerCount);
				map.put(today + "ActivateCount", activateCount);
				startTime = time;
//				allRegisterCount += registerCount;
//				allRctivateCount += activateCount;
//				index ++;
			}
			result.add(map);
		}
		return result;
	}
}
