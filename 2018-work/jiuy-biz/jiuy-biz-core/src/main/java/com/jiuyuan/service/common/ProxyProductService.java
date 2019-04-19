/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProxyProductMapper;
import com.jiuyuan.entity.newentity.ProxyOrder;
import com.jiuyuan.entity.newentity.ProxyProduct;

/**
 */
@Service
public class ProxyProductService implements IProxyProductService  {
	private static final Logger logger = LoggerFactory.getLogger(ProxyProductService.class);
	@Autowired
	private ProxyProductMapper proxyProductMapper;
	public List<ProxyProduct> getProxyProductList(){
		Wrapper<ProxyProduct> wrapper = new EntityWrapper<ProxyProduct>();
		wrapper.orderBy("id", false);
		List<ProxyProduct> list = proxyProductMapper.selectList(wrapper);
		return list;
	}
	
	
	
	
	public List<ProxyProduct> getProxyProductList(String name, String note, Page<Map<String,String>> page){
		Wrapper<ProxyProduct> wrapper = new EntityWrapper<ProxyProduct>();
		if(StringUtils.isNotEmpty(name)){
			wrapper.like("name",name);
		}
		if(StringUtils.isNotEmpty(note)){
			wrapper.like("note",note);
		}
		wrapper.orderBy("id", false);
		List<ProxyProduct> list = proxyProductMapper.selectPage(page,wrapper);
		return list;
	}
	 
	
	public void addProxyProduct(String name, int singleUseLimitDay, int renewProtectDay, double price, String note){
		ProxyProduct proxyProduct = getProxyProductByName(name);
		if(proxyProduct != null){
			throw new RuntimeException("创建失败！已有产品“"+name+"”，请换一个产品名称，重新输入");
		}
		ProxyProduct addProxyProduct = new ProxyProduct();
		addProxyProduct.setName(name);
		addProxyProduct.setSingleUseLimitDay(singleUseLimitDay);
		addProxyProduct.setRenewProtectDay(renewProtectDay);
		addProxyProduct.setPrice(price);
		addProxyProduct.setNote(note);
		addProxyProduct.setCreateTime(System.currentTimeMillis());
		proxyProductMapper.insert(addProxyProduct);
	}
	 
	private ProxyProduct getProxyProductByName(String name) {
		Wrapper<ProxyProduct> wrapper = new EntityWrapper<ProxyProduct>();
		if(StringUtils.isNotEmpty(name)){
			wrapper.eq("name",name);
		}
		List<ProxyProduct> list = proxyProductMapper.selectList(wrapper);
		if(list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}




	public void updProxyProduct(long proxyProductId ,String name, int singleUseLimitDay, int renewProtectDay, double price, String note){
		ProxyProduct proxyProduct = new ProxyProduct();
		proxyProduct.setId(proxyProductId);
		proxyProduct.setName(name);
		proxyProduct.setSingleUseLimitDay(singleUseLimitDay);
		proxyProduct.setRenewProtectDay(renewProtectDay);
		proxyProduct.setPrice(price);
		proxyProduct.setNote(note);
		proxyProductMapper.updateById(proxyProduct);
	}

	public ProxyProduct getProxyProduct(long proxyProductId){
		return proxyProductMapper.selectById(proxyProductId);
	}
}