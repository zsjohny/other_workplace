package com.jiuyuan.service.common;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.WithdrawApplyNewMapper;
import com.jiuyuan.entity.newentity.WithdrawApplyNew;

@Service
public class WithdrawApplyNewService implements IWithdrawApplyNewService {
	
	private static final Logger logger = LoggerFactory.getLogger(WithdrawApplyNewService.class);
	
	@Autowired
	private WithdrawApplyNewMapper withdrawApplyNewMapper;

	@Override
	public WithdrawApplyNew getWithdrawApplyInfoById(long withDrawApplyId) {
		return withdrawApplyNewMapper.selectById(withDrawApplyId);
	}



	@Override
	public List<WithdrawApplyNew> search(Page<Map<String,Object>> page, long tradeId, String tradeNo, int status,
			int type, double startApplyMoney, double endApplyMoney, long startCreateTimeL, long endCreateTimeL,
			long supplierId) {
//		Wrapper<WithdrawApplyNew> wrapper = new EntityWrapper<WithdrawApplyNew>();
//		if(tradeId != -1){
//			//提现订单编号
//			wrapper.like("TradeId", String.valueOf(tradeId));
//		}
//		if(!tradeNo.isEmpty()){
//			//交易编号
//			wrapper.like("TradeNo", tradeNo);
//		}
//		if(supplierId != 0){
//			//商家号
//			wrapper.like("RelatedId", String.valueOf(supplierId));
//		}
//		//状态
//		if(status != -1){
//			wrapper.eq("Status", status);
//		}
//		//申请金额
//		if(startApplyMoney != -1){
//			wrapper.ge("ApplyMoney", startApplyMoney);
//		}
//		if(endApplyMoney != -1){
//			wrapper.le("ApplyMoney", endApplyMoney);
//		}
//		//申请时间
//		if(startCreateTimeL != 0){
//			wrapper.ge("CreateTime", startCreateTimeL);
//		}
//		if(endCreateTimeL != 0){
//			wrapper.ge("CreateTime", endCreateTimeL);
//		}
//		wrapper.orderBy("Status",true);
//		wrapper.orderBy("CreateTime", false);
		return withdrawApplyNewMapper.search(page, tradeId,tradeNo,status,type,startApplyMoney,endApplyMoney,startCreateTimeL,endCreateTimeL,supplierId);
	}



	@Override
	public void withDrawConfirm(long id, Long adminId, double money, int type, String tradeNo, int tradeWay,
			String remark, int status) {
		//先确认该订单的状态
		WithdrawApplyNew withdrawApplyNew = withdrawApplyNewMapper.selectById(id);
		int withdrawApplyNewStatus = withdrawApplyNew.getStatus();
		if(withdrawApplyNewStatus != WithdrawApplyNew.NO_DEAL){
			logger.error("该提现订单已经处理过或者已冻结，无法提交！id："+id);
			throw new RuntimeException("该提现订单已经处理过或者已冻结，无法提交！");
		}
		if(type != 1){
			logger.error("该功能只能实现供应商提现");
			throw new RuntimeException("该功能只能实现供应商提现");
		}
		//交易金额判断
		if(money>withdrawApplyNew.getApplyMoney()){
			logger.error("打款金额超出申请金额，请再次确认！");
			throw new RuntimeException("打款金额超出申请金额，请再次确认！");
		}
		if(status == WithdrawApplyNew.DEAL_SUCCESS){
			if(tradeNo == ""|| tradeWay == 0){
				logger.error("确认打款缺少打款信息！无法提交信息！");
				throw new RuntimeException("确认打款缺少打款信息！无法提交信息！");
			}
		}
		//提交提现订单
		WithdrawApplyNew withdrawApplyNew2 = new WithdrawApplyNew();
		withdrawApplyNew2.setId(id);
		if(adminId != null){
			withdrawApplyNew2.setAdminId(adminId);
		}
		withdrawApplyNew2.setMoney(money);
		withdrawApplyNew2.setTradeNo(tradeNo);
		withdrawApplyNew2.setTradeWay(tradeWay);
		withdrawApplyNew2.setRemark(remark);
		withdrawApplyNew2.setStatus(status);
		withdrawApplyNew2.setUpdateTime(System.currentTimeMillis());
		withdrawApplyNew2.setDealTime(System.currentTimeMillis());
		withdrawApplyNewMapper.updateById(withdrawApplyNew2);
	}






	@Override
	public List<WithdrawApplyNew> getNoDealWithdrawApplyInfoById(long withDrawApplyId) {
		Wrapper<WithdrawApplyNew> wrapper = new EntityWrapper<WithdrawApplyNew>();
		wrapper.eq("RelatedId", withDrawApplyId).eq("Status", WithdrawApplyNew.NO_DEAL);
		return withdrawApplyNewMapper.selectList(wrapper);
	}



	public void updateStatusById(int status, Long id) {
		WithdrawApplyNew withdrawApplyNew = new WithdrawApplyNew();
		withdrawApplyNew.setId(id);
		withdrawApplyNew.setStatus(status);
		if(status == WithdrawApplyNew.FREEZED){
			withdrawApplyNew.setFreezeTime(System.currentTimeMillis());
		}
		if(status == WithdrawApplyNew.NO_DEAL){
			withdrawApplyNew.setFreezeTime(0L);
		}
		withdrawApplyNewMapper.updateById(withdrawApplyNew);
	}



	public List<WithdrawApplyNew> getFreezedWithdrawApplyInfoById(long id) {
		Wrapper<WithdrawApplyNew> wrapper = new EntityWrapper<WithdrawApplyNew>();
		wrapper.eq("RelatedId", id).eq("Status", WithdrawApplyNew.FREEZED);
		return withdrawApplyNewMapper.selectList(wrapper);
	}




	

}
