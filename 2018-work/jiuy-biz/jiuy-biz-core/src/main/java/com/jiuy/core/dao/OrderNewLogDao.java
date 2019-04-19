package com.jiuy.core.dao;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.OrderNewLog;

public interface OrderNewLogDao {

	List<OrderNewLog> orderNewLogOfOrderNos(Collection<Long> orderNos);

	List<OrderNewLog> orderNewLogPayOfOrderNos(Collection<Long> orderNos);

    int updateLog(long userId, Collection<Long> orderNos, int oldOrderSatus, int newOrderStatus, long currentTime);

	List<OrderNewLog> getByOrderNos(Collection<Long> orderNos, OrderStatus oldStatus, OrderStatus newStatus);

	public int getNewUserBuyCountByTime(long startTime, long endTime);
	
	public int getSaleNumByTime(long startTime, long endTime);
	
	public int getSaleProductCountByTime(long startTime, long endTime);

	public List<Map<String, Object>> saleCountPerProduct(long startTime, long endTime);

    public List<Map<String, Object>> saleCountPerCategory(long startTime, long endTime);

    public List<Map<String, Object>> saleCountPerBrand(long startTime, long endTime);

	public List<Map<String, Object>> rankSaleLocation(long startTime, long endTime);
	
	public List<Map<String, Object>> rankSaleLocationUser(long startTime, long endTime);
	
    public List<Map<String, Object>> rankBuyers(long startTime, long endTime);

    public List<Map<String, Object>> rankBuyersRecordsTime(long startTime, long endTime, int sequence);

	public List<Map<String, Object>> hotSaleCategory(long startTime, long endTime, int count, long categoryId);

	public List<Map<String, Object>> hotSaleOfActivity(long startTime, long endTime);

    public List<Map<String, Object>> saleOrderCountPerDay(long startTime, long endTime);

    public List<Map<String, Object>> saleProductCountPerDay(long startTime, long endTime);
    
    public List<Map<String, Object>> saleOrderMoneytPerDay(long startTime, long endTime);
    
    public List<Map<String, Object>> rankProductHotsale(long startTime, long endTime, Collection<Long> seasonIds);

	int getNewUserOrderCountPerDay(long newUserStartTime, long newUserEndTime);

	Map<String, BigDecimal> getNewUserProductCountPerDay(long newUserStartTime, long newUserEndTime);	

    public List<Map<String, Object>> productsale(Collection<Long> seasonIds);

	public List<Map<String, Object>> productSkuSale(Collection<Long> seasonIds);

}
