package com.jiuy.core.service.task;

import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.jiuy.core.business.facade.OrderNewFacade;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.delegate.ErpDelegator;

//物流推送接口
public class WdtOrderJob {

	@Autowired
	private OrderNewFacade orderNewFacade;
	
    @Autowired
    private ErpDelegator erpDelegator;

	public void execute() throws ParseException {
		//合并订单
		orderNewFacade.createMergerd();

		
/**
 * 百世erp功能停用 2017/1/5 added by dongzhong
 */
//		//获取今天的合并订单
//		List<Map<String, Object>> list = null;
//		list = erpDelegator.getToBePushedOrders(DateUtil.getERPTime() - DateUtils.MILLIS_PER_DAY, DateUtil.getERPTime());
//		
//        // 推送已做处理过的订单
//        Set<Long> successOrderNos = new HashSet<Long>();
//        erpDelegator.pushOrders(list, successOrderNos);
//
//        // 更新推送成功的订单时间
//        if (successOrderNos.size() > 0) {
//            erpDelegator.addPushTime(successOrderNos);
//        }
	}
	
}
