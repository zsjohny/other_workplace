package com.jiuy.core.service.brandorder;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuy.core.dao.mapper.BrandOrderDao;
import com.jiuy.core.meta.brandorder.BrandOrder;
import com.jiuy.core.meta.brandorder.BrandOrderSO;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 下午4:14:22
 * 
 */

@Service
public class BrandOrderServiceImpl implements BrandOrderService {

	@Autowired
	private BrandOrderDao brandOrderDao;
	
	@Override
	public List<BrandOrder> search(BrandOrderSO brandOrderSO) {
		return brandOrderDao.search(brandOrderSO);
	}

	@Override
	public BrandOrder getByOrderNo(long orderNo) {
		return brandOrderDao.getByOrderNo(orderNo);
	}

}
