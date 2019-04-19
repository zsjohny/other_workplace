package com.yujj.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yujj.dao.mapper.OrderDiscountLogMapper;

@Service
public class OrderDiscountLogService {
	
	@Autowired
	private OrderDiscountLogMapper orderDiscountLogMapper;
	
	
}
