package com.jiuy.core.service.aftersale;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuy.core.dao.BusinessWithDrawDao;
import com.jiuy.core.service.StoreFinanceLogService;
import com.jiuy.core.service.SupplierFinanceLogService;
import com.jiuyuan.entity.StoreFinanceLog;
import com.jiuyuan.entity.newentity.FinanceLogNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.withdraw.WithDrawApply;
import com.jiuyuan.entity.withdraw.WithDrawApplyVO;

@Service
public class BusinessWithDrawServiceImpl implements BusinessWithDrawService{
	
	private static final Logger logger = LoggerFactory.getLogger(BusinessWithDrawServiceImpl.class);
	private static final int DEALED_STATUS = 1;
	@Resource
	private BusinessWithDrawDao businessWithDrawDao;
	
	@Resource
	private StoreFinanceLogService storeFinanceLogService;
	
	@Resource
	private SupplierFinanceLogService supplierFinanceLogService;
	
	@Override
	public int searchCount(WithDrawApplyVO businessWithDraw) {
		return businessWithDrawDao.searchCount(businessWithDraw);
	}

	@Override
	public List<WithDrawApply> search(PageQuery pageQuery, WithDrawApplyVO businessWithDraw) {
		return businessWithDrawDao.search(pageQuery, businessWithDraw);
	}

	@Override
	public int updateWithDraw(WithDrawApply businessWithDraw) {
		return businessWithDrawDao.updateWithDraw(businessWithDraw);
	}

	@Override
	public WithDrawApply getWithDrawApplyById(long id) {
		return businessWithDrawDao.getById(id);
	}

    @Transactional(rollbackFor=Exception.class)
	public void withDrawConfirm(long id, long relatedId, double money, int type, String tradeNo, int tradeWay,
			String remark) {
		if(type == 0){//是门店提现
			StoreFinanceLog storeFinanceLog = new StoreFinanceLog(id,relatedId,7,System.currentTimeMillis());
			storeFinanceLogService.updateStoreFinanceLog(storeFinanceLog);
		}
		WithDrawApply withDrawApply = getWithDrawApplyById(id);
		if(withDrawApply == null){
			logger.error("提现申请订单ID不存在！");
			throw new RuntimeException("提现申请订单ID不存在！");
		}
		if(withDrawApply.getStatus() == 1){
			logger.error("提现申请订单已经处理！请勿重复处理！");
			throw new RuntimeException("提现申请订单已经处理！请勿重复处理！");
		}
		if(withDrawApply.getRelatedId() !=relatedId){
			logger.error("供应商不符");
			throw new RuntimeException("供应商不符！");
		}
		long tradeId = withDrawApply.getTradeId();
		if(type == 1){
			//判断交易金额是否与申请金额相符
			double applyMoney = withDrawApply.getApplyMoney();
			if(applyMoney!=money){
				logger.error("com.jiuy.web.controller/business/withdraw/confirm ERROR: 交易金额与申请金额不符");
				throw new RuntimeException("交易金额与申请金额不符！");
			}
//			//做收支记录
//			SupplierFinanceLog supplierFinanceLog = new SupplierFinanceLog();
//			supplierFinanceLog.setSupplierId(relatedId);
//			//2：支出-供应商提现申请后审核通过  relatedId代表商家提现申请审批表id'
//			supplierFinanceLog.setType(2);
//			supplierFinanceLog.setRelatedid(id);
//			supplierFinanceLog.setCreatetime(System.currentTimeMillis());
//			supplierFinanceLog.setUpdatetime(System.currentTimeMillis());
//			supplierFinanceLog.setCash(new BigDecimal(money));
//			//获取供应商表中的
//			
//			try {
//				int i = supplierFinanceLogService.addSupplierFinanceLog(supplierFinanceLog);
//				if(i != 1){
//					logger.error("com.jiuy.web.controller/business/withdraw/confirm ERROR: 供应商收支信息生成失败！"+",supplierId:"+relatedId+",tradeId:"+tradeId);
//					throw new RuntimeException("供应商收支信息生成失败！");
//				}
//			} catch (Exception e) {
//				logger.error(e.getMessage());
//				throw new RuntimeException(e.getMessage());
//			}
			
		}
		//更改提现订单状态
		WithDrawApply businessWithDraw = new WithDrawApply(id, money,type, tradeNo,tradeWay, remark,DEALED_STATUS, System.currentTimeMillis());
		int changRow = updateWithDraw(businessWithDraw);
		if(changRow != 1){
			logger.error("com.jiuy.web.controller/business/withdraw/confirm ERROR: " +"错误的提现单 storeId ="+relatedId+",id = "+ id+",tradeId = "+tradeId);
		    throw new RuntimeException("提现订单无法确认打款");
		}		
	}


}
