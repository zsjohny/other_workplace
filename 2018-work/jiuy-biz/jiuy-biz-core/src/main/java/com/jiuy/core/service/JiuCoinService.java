package com.jiuy.core.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.JiuCoinDeductStatisticsDayBean;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.query.PageQuery;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月23日 上午11:27:18
*/
public interface JiuCoinService {
	
	public int updateJiuCoin(int type,int coins,Collection<Long> users,String usersStr);
	
	public int batchUpdateJiuCoinDeduction(Collection<Long> productIds,double deductPercent);
	
	public List<Product> searchDeductionProduct();
	
	public Map<String, Object> searchDeductDetailRecord(PageQuery pageQuery,long productId,String productName,long yjjNumber,long startTime,long endTime);
	
	public List<JiuCoinDeductStatisticsDayBean> sumDayStatistics();
	
	public JiuCoinDeductStatisticsDayBean sumTimeStatistics(long startTime,long endTime);
	
	public JiuCoinDeductStatisticsDayBean timeIntervalStatistics(long startTime, long endTime);
}
