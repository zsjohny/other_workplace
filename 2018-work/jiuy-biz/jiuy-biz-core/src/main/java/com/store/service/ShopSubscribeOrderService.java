package com.store.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.store.dao.mapper.SubscribeOrderMapper;
import com.store.entity.SubscribeOrder;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* 预约订单
*/

@Service
public class ShopSubscribeOrderService{
	
	private static final Log logger = LogFactory.get();
	
	
	@Autowired
	ShopProductService shopProductService;
	
	@Autowired
	SubscribeOrderMapper subscribeOrderMapper;
	
	public Page<SubscribeOrder> getList(Page page,long storeId) {
		Wrapper<SubscribeOrder> wrapper = new EntityWrapper<SubscribeOrder>();
		wrapper.eq("store_id", storeId).orderBy("create_time", false);
		page.setRecords(subscribeOrderMapper.selectPage(page, wrapper));
		return page;
	}

	
}