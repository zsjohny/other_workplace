package com.jiuy.core.service.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuy.web.delegate.ErpDelegator;

public class WdtSkuCountSyncJob {
	
	private static final Logger logger = LoggerFactory.getLogger("WDT");
	
    @Autowired
    private ErpDelegator erpDelegator;
	
	public void execute() {
		
		logger.error("WdtSkuCountSyncJob.excute()");
		
		erpDelegator.synchronizationCount();
	}
}
