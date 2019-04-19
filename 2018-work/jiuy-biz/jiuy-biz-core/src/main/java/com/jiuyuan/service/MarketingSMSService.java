/**
 * 
 */
package com.jiuyuan.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.dao.mapper.MarketingSMSMapper;
import com.jiuyuan.entity.newentity.StoreBusiness;

/**
 * @author LWS
 *
 */
@Service
public class MarketingSMSService {
	private static final Logger logger = LoggerFactory.getLogger("PAYMENT");

	@Autowired
	private MarketingSMSMapper marketingSMSMapper;

	public List<StoreBusiness> getTestStores() {
		return marketingSMSMapper.getTestStores();
	}

	public List<Map<String, String>> getOtherStores() {
		return marketingSMSMapper.getOtherStores();
	}
}
