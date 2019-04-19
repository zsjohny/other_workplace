package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProxyCustomer;
import com.jiuyuan.entity.newentity.ProxyOrder;

public interface IProxyCustomerService {


	public int getProxyCustomerCount(long proxyUserId);

	public List<Map<String,Object>> list(String customerName, String customerPhone, int status, Integer maxSurplusDays,
			Integer minSurplusDays, long proxyUserId, Page<Map<String,Object>> page);

	public Map<String, Object> detail(long proxyUserId, long proxyCustomerId);

	public void applyMiniprogram(String applyName, String registerPhoneNumber, String comfirmPhoneNumber,
			long proxyUserId);

	public Map<String, Object> getCustomerStatistics(long proxyUserId);
	
	public int getProtectPeriodCustomerCount(long proxyUserId, long currentTime);
	
	public int getUsingPeriodCustomerCount(long proxyUserId, long currentTime);
	
	public void addProxyCustomer(ProxyOrder order); 
	 

}