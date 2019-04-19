package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;

public interface IProxyProductService {

	public List<ProxyProduct> getProxyProductList(String name, String note,	Page<Map<String,String>> page);

//	public void addProxyProduct(String name, int singleUseLimitDay, int renewProtectDay, double price, String note);

	public void addProxyProduct(String name, int singleUseLimitDay, int renewProtectDay, double price, String note);

	public void updProxyProduct(long proxyProductId ,String name, int singleUseLimitDay, int renewProtectDay, double price, String note);

	public ProxyProduct getProxyProduct(long proxyProductId);

	public List<ProxyProduct> getProxyProductList();


	 

}