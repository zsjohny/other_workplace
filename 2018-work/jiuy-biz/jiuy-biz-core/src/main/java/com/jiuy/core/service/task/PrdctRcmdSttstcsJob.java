package com.jiuy.core.service.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuy.core.dao.modelv2.ProductMapper;

public class PrdctRcmdSttstcsJob {
    
	private static final Logger logger = Logger.getLogger(PrdctRcmdSttstcsJob.class);
	
	@Autowired
	private ProductMapper productMapper;
	
	public void execute() {
		logger.info("com.jiuy.core.service.task.PrdctRcmdSttstcsJob.execute");
		productMapper.executeRcmdSttstcs();
	}
}
