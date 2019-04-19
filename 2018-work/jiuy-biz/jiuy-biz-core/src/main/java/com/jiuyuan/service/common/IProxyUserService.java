package com.jiuyuan.service.common;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProxyUser;

public interface IProxyUserService {

	public int getProxyProductTotalCount(long proxyProductId);

	public int getReceiveProxyUserCount(long proxyProductId);

	public List<ProxyUser> getProxyUserList(String proxyUserNum, String proxyUserName, String proxyUserFullName,
			String proxyUserPhone, String idCardNo, int proxyState, Page<Map<String, String>> page);

	public void addProxyUser(String proxyUserName, String proxyUserFullName, String proxyUserPhone, String province,
			String city, String county, String idCardNo, long proxyProductId, int proxyState) throws IOException;

	public void updProxyUser(long proxyUserId, String proxyUserName, String proxyUserFullName, String proxyUserPhone,
			String province, String city, String county, String idCardNo, int proxyState);

	public ProxyUser getProxyUser(long proxyUserId);


	/**
	 * 退还库存
	 */
	public void returnStockCount(long proxyUserId, int incrStockCount);

	public Map<String, Object> detail(long proxyUserId);

	public Map<String, Object> home(long proxyUserId);
	
	public void reduceStockCount(long proxyUserId, int reduceStockCount);

	public void incrSellOutCount(long proxyUserId,int proxyProductCount);
	
	public void incrStockCount(long proxyUserId, int incrStockCount,long adminId);

	public Map<String, Object> getProxyUserStatistics(String proxyUserNum, String proxyUserName, String proxyUserFullName, String proxyUserPhone, String idCardNo, int proxyState);
	

}