package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.RefundStatus;
import com.jiuyuan.dao.mapper.supplier.GroundBonusGrantMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.newentity.ground.GroundBonusGrant;
import com.jiuyuan.entity.newentity.ground.GroundBonusRule;
import com.jiuyuan.util.DoubleUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Service
public class GroundBonusGrantService {
	private static final Log logger = LogFactory.get(RefundOrderService.class);
	
	@Autowired
	private SupplierOrderMapper supplierOrderMapper;
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private IRefundOrderService refundOrderService;
	
	@Autowired
	private GroundBonusGrantMapper groundBonusGrantMapper;
    /**
     * 退地推人员带入账
     * @param orderNo
     * @param refundCost
     * @param refundOrderId
     */
	public void reduceToEnterIntoAccount(Long orderNo, double refundCost, Long refundOrderId) {
		//地推人员奖金重新计算
		Wrapper<GroundBonusGrant> groundBonusGrantWrapper = new EntityWrapper<GroundBonusGrant>(); 
		List<Integer> bonusRuleList = new ArrayList<Integer>();
		bonusRuleList.add(GroundBonusGrant.BONUS_TYPE_FIRST_STAGE);
		bonusRuleList.add(GroundBonusGrant.BONUS_TYPE_SECOND_STAGE);
		bonusRuleList.add(GroundBonusGrant.BONUS_TYPE_THIRD_STAGE);
		groundBonusGrantWrapper.in("bonus_type", bonusRuleList).eq("related_id", orderNo);
		List<GroundBonusGrant> groundBonusGrantList = groundBonusGrantMapper.selectList(groundBonusGrantWrapper);
		if(groundBonusGrantList.size() == 0){
			logger.error("找不到该订单的地推人员奖金，无需进行退待入账！");
			return;
		}
        //订单金额
		double total = 0;
		//获取该订单信息
		StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(orderNo);
		total = storeOrderNew.getTotalPay();
		//获取该订单中已成功退款的金额
		List<RefundOrder> refundOrderList = refundOrderService.getRefundOrderWhenSuccess(orderNo);
		//计算退款金额
		double refundTotal = refundCost;
	    for(RefundOrder refundOrder2:refundOrderList){
	    	refundTotal = DoubleUtil.add(refundTotal, refundOrder2.getRefundCost());
	    }
	    //地推人员奖金计算，实付金额不含邮费
		total = DoubleUtil.sub(total, refundTotal);
//		if(total < 0){
//			logger.info("实付金额为负的，请排查问题！");
//			throw new RuntimeException("实付金额为负的，无法进行退款操作！");
//		}
		//update奖金
		for(GroundBonusGrant groundBonusGrant : groundBonusGrantList){
			GroundBonusGrant groundBonusGrant2 = new GroundBonusGrant();
			double bonusRule = groundBonusGrant.getBonusRule();
			//计算奖金
			double cash = DoubleUtil.mul(total, bonusRule);
			groundBonusGrant2.setOrderPrice(total);//订单金额
			groundBonusGrant2.setCash(cash);//奖金
			groundBonusGrant2.setId(groundBonusGrant.getId());//id
			groundBonusGrantMapper.updateById(groundBonusGrant2);
		}
		
		
	}
	

}
