package com.jiuy.core.service.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.service.storeorder.StoreOrderService;

/**
 * @author jeff.zhan
 * @version 2016年12月27日 上午11:07:01
 * 
 */

@Service
public class StoreOrderMergeJob {

	@Autowired
	private StoreOrderService storeOrderService;

	public void execute() {
		// 3.0开发 使用供应商平台发单， 订单合并功能去掉
		// storeOrderService.merge(DateUtil.getERPTime());
	}
}
