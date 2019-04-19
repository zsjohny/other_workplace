package com.jiuy.core.business.facade;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.StoreFinanceLogService;
import com.jiuy.core.service.aftersale.BusinessWithDrawService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuyuan.entity.StoreFinanceLog;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.withdraw.WithDrawApply;

@Service
public class StoreFinanceLogFacade {
	
	@Resource
	private StoreFinanceLogService storeFinanceLogService;
	
	@Resource
	private BusinessWithDrawService businessWithDrawService;
	
	@Resource
	private StoreBusinessService storebusinessService;
	
	@Transactional(rollbackFor = Exception.class)
	public void withdrawFeedBack(long id, long relatedId, double money, String remark) {
		//添加一条收入记录并更新原记录
		storeFinanceLogService.addFinanceLogon(new StoreFinanceLog(id,relatedId,8,money,System.currentTimeMillis()));
		
		long time =System.currentTimeMillis();
		StoreFinanceLog storeFinanceLog = new StoreFinanceLog(id,relatedId,9,time);
		storeFinanceLogService.updateStoreFinanceLog(storeFinanceLog);
		
		//返回门店的可提现金额及总金额
		StoreBusiness storeBusiness = storebusinessService.searchBusinessById(relatedId);
		storeBusiness.setCashIncome(storeBusiness.getCashIncome()+ money);
		storeBusiness.setAvailableBalance(storeBusiness.getAvailableBalance() + money);
		storebusinessService.updateMoney(storeBusiness);
		
		WithDrawApply businessWithDraw = new WithDrawApply(id , 1, remark, 1,time );
		int changRow = businessWithDrawService.updateWithDraw(businessWithDraw);
		if(changRow != 1){
			throw new ParameterErrorException("com.jiuy.web.controller/business/withdraw/confirm ERROR: " +"错误的提现单 storeId ="+relatedId+",id = "+ id);
		}
		
	}	
	
}
