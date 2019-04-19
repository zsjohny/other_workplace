package com.jiuy.core.service.order;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.OrderNewLog;

public interface OrderNewLogService {

	List<OrderNewLog> orderNewLogOfOrderNos(Collection<Long> orderNos);

	List<OrderNewLog> orderNewLogPayOfOrderNos(Collection<Long> orderNos);

	List<Map<String, Object>> hotSaleCategory(long startTime, long endTime, int count, long id);
	
	List<Map<String, Object>> hotSaleOfActivity(long startTime, long endTime);
	
    Map<String, Object> saleOrderCountPerDay(long startTime, long endTime);

    Map<String, Object> saleProductCountPerDay(long startTime, long endTime);

	Map<String, Object> saleOrderMoneytPerDay(long startTime, long endTime);
	
	List<Map<String, Object>> rankProductHotsale(long startTime, long endTime, Collection<Long> seasonIds);

	int getNewUserOrderCountPerDay(long newUserStartTime, long newUserEndTime);

	int getNewUserProductCountPerDay(long newUserStartTime, long newUserEndTime);

	Map<Long, Map<String, Object>> productSale(Collection<Long> seasonIds);
	
	
	Map<Long, Map<String, Object>> productSkuSale(Collection<Long> seasonIds);
	

}
