package com.jiuy.core.service.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.service.ExpressSupplierService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.util.WdtSkuUtil;

public class WdtLogisticsJob {
	
	private static final Logger logger = LoggerFactory.getLogger("WDT");
    
    @Autowired
    private ErpDelegator erpDelegator;
	
	@Autowired
	private OrderOldService orderNewService;
	
	@Autowired
	private ExpressSupplierService expressSupplierService;
	
	public void execute() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("limit", "20");
		String result = WdtSkuUtil.send(AdminConstants.WDT_LOGISTICS_SYN_QUERY_URL, map);
		if(StringUtils.equals(result, "")) {
			return;
		}
		
		Set<Integer> rec_ids = new HashSet<Integer>();
		List<Map<String, Object>> list = logisticsDataToJson(result, rec_ids);

//		物流同步
		if(list.size() > 0) {
			erpDelegator.updateExpressInfo(list);
		}
		
//		回调 
		Iterator<Integer> iterator = rec_ids.iterator();
		while (iterator.hasNext()) {
			Integer i = iterator.next();
			erpDelegator.syncLogistics(i, true);
		}
		
	}
	
	private List<Map<String, Object>> logisticsDataToJson(String str, Set<Integer> rec_ids) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByCnName();
		
		Object object = JSON.parse(str);
		JSONObject jsonObject = (JSONObject)object;
		String tradesStr = JSON.toJSONString(jsonObject.get("trades"));
		JSONArray jsonArray = StringUtils.isBlank(tradesStr) ? new JSONArray() : JSON.parseArray(tradesStr);
		
		for(Object obj : jsonArray) {
			String tid = (String) ((JSONObject)obj).get("tid");
			
			if (tid.contains("QM")) {
				continue;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
            int rec_id = Integer.parseInt((String) ((JSONObject) obj).get("rec_id"));
			String logistics_name = (String) ((JSONObject)obj).get("logistics_name_erp");
			String logistics_no = (String) ((JSONObject)obj).get("logistics_no");
			
			//需要处理未成功的单子
			if(!isValid(logistics_name, logistics_no, tid, suMap)) {
				erpDelegator.syncLogistics(rec_id, false);
				continue;
			}
			
			ExpressSupplier expressSupplier = suMap.get(logistics_name);
			if (expressSupplier == null) {
				logger.error("找不到对应的物流供应商。params，logistics_name：" +  logistics_name + " rec_id: " + rec_id + ", tid: " + tid);
			}
			
			map.put("orderNo", tid);
			map.put("expressSupplier", expressSupplier.getEngName());
			map.put("expressOrderNo", logistics_no);
			
			list.add(map);
			
			rec_ids.add(rec_id);
		}	
		
		return list;
	}
	
	public boolean isValid(String logistics_name, String logistics_no, String tid, Map<String, ExpressSupplier> suMap) {
		List<Long> orderNos = orderNewService.getOrderNosByOrderStatus(OrderStatus.ALL.getIntValue());
		
		if(StringUtils.equals(logistics_name, "") || StringUtils.equals(logistics_name, null) || 
				StringUtils.equals(logistics_no, "") || StringUtils.equals(logistics_no, null)) {
			logger.error("com.jiuy.core.service.task.WdtLogisticsJob: Param is null or empty, tid:" +  tid +", logistics_no:" + logistics_no + ", logistics_name" + logistics_name);
			return false;
		} else if (!orderNos.contains(Long.parseLong(tid))) {
			logger.error("com.jiuy.core.service.task.WdtLogisticsJob: orderNo not exist, tid:" +  tid +", logistics_no:" + logistics_no + ", logistics_name" + logistics_name);
			return false;
		} else if (suMap.get(logistics_name) == null) {
			logger.error("com.jiuy.core.service.task.WdtLogisticsJob: expressSupport not exist, tid:" +  tid +", logistics_no:" + logistics_no + ", logistics_name" + logistics_name);
			return false;
		}
		
		return true;
	}
	
	
}
