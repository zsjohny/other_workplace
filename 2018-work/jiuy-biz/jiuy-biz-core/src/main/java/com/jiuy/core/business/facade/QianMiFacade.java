package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.dao.mapper.QMExpressInfoDao;
import com.jiuy.core.dao.mapper.QMOrderDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.service.ExpressSupplierService;
import com.jiuy.core.service.qianmi.QMOrderService;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuyuan.constant.AdminConstants;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.entity.newentity.ExpressSupplier;
import com.jiuyuan.entity.qianmi.QMExpressInfo;
import com.jiuyuan.entity.qianmi.QMOrder;
import com.jiuyuan.util.WdtSkuUtil;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Service
public class QianMiFacade {

	private static final Logger logger = LoggerFactory.getLogger("QianMiLogger");
	
	@Autowired
	private QMOrderService qMOrderService;
	
	@Autowired
	private QMOrderDao qMOrderDao;
	
	@Autowired
	private ExpressSupplierService expressSupplierService;
	
	@Autowired
	private QMExpressInfoDao qMExpressInfoDao;
	
	@Autowired
	private ErpDelegator erpDelegator;
	
	public void mergeOrders() {
		Map<String, List<QMOrder>> ordersOfBuyers = qMOrderService.getOrdersOfBuyers(10, 0L);
		for (Map.Entry<String, List<QMOrder>> entry : ordersOfBuyers.entrySet()) {
			List<QMOrder> qmOrders = entry.getValue();
			handleSameTypeOrders(qmOrders);
		}
	}

	private void handleSameTypeOrders(List<QMOrder> qmOrders) {
		if (qmOrders.size() < 1) {
			logger.error("QianMiFacade.mergeOrders() ERROR: 至少存在一个订单！");
		}
		
		if (qmOrders.size() == 1) {
			transToMergedQMOrder(qmOrders.get(0));
		} else {
			createMergedQMOrder(qmOrders);
		}
	}

	private void createMergedQMOrder(List<QMOrder> qmOrders) {
		QMOrder qmOrder = new QMOrder();
		BeanUtils.copyProperties(qmOrders.get(0), qmOrder);
		qmOrder.setTid(null);
		qmOrder.setMergedId(-1L);
		
		QMOrder mergedQMOrder = qMOrderDao.add(qmOrder);
		qMOrderDao.update(mergedQMOrder.getOrderNo(), mergedQMOrder.getOrderNo() + "", null);
		
		Set<Long> orderNos = getOrderNos(qmOrders);
		qMOrderDao.batchUpdate(orderNos, mergedQMOrder.getOrderNo(), null, null);
	}

	private Set<Long> getOrderNos(List<QMOrder> qmOrders) {
		Set<Long> orderNos = new HashSet<>();
		
		for (QMOrder order : qmOrders) {
			orderNos.add(order.getOrderNo());
		}
		return orderNos;
	}

	private void transToMergedQMOrder(QMOrder qmOrder) {
		qMOrderDao.update(qmOrder.getOrderNo(), null, qmOrder.getOrderNo());
	}

	public void recordPushTime(Collection<Long> orderNos) {
		List<QMOrder> qmOrders = qMOrderService.search(orderNos);
		Set<Long> another_nos = getOrderNos(qmOrders);
		orderNos.addAll(another_nos);
		
		qMOrderDao.batchUpdate(orderNos, null, System.currentTimeMillis(), null);
	}

	public List<Map<String, Object>> putInCollection(String result, Set<Integer> rec_ids, Set<Long> orderNos) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		Map<String, ExpressSupplier> suMap = expressSupplierService.itemByCnName();
		
		Object object = JSON.parse(result);
		JSONObject jsonObject = (JSONObject)object;
		String tradesStr = JSON.toJSONString(jsonObject.get("trades"));
		JSONArray jsonArray = StringUtils.isBlank(tradesStr) ? new JSONArray() : JSON.parseArray(tradesStr);
		
		for(Object obj : jsonArray) {
			String tid = ((JSONObject)obj).getString("tid");
			
			if (!tid.contains("QM")) {
				continue;
			}
			
			Map<String, Object> map = new HashMap<String, Object>();
            Integer rec_id = ((JSONObject) obj).getInteger("rec_id");
			String logistics_name = ((JSONObject)obj).getString("logistics_name_erp");
			String logistics_no = ((JSONObject)obj).getString("logistics_no");
			
			//需要处理未成功的单子
			if(!isValid(logistics_name, logistics_no, tid, suMap)) {
				erpDelegator.syncLogistics(rec_id, false);
				continue;
			}
			
			ExpressSupplier expressSupplier = suMap.get(logistics_name);
			if (expressSupplier == null) {
				logger.error("找不到对应的物流供应商。params，logistics_name：" +  logistics_name + " rec_id: " + rec_id + ", tid: " + tid);
			}
			
			map.put("orderNo", tid.substring(2));
			map.put("expressSupplier", expressSupplier.getEngName());
			map.put("expressOrderNo", logistics_no);
			
			list.add(map);
			
			rec_ids.add(rec_id);
			orderNos.add(Long.parseLong(tid.substring(2)));
		}	
		
		return list;
	}
	
	public boolean isValid(String logistics_name, String logistics_no, String tid, Map<String, ExpressSupplier> suMap) {
		List<QMOrder> qmOrders = qMOrderDao.search(10, null, null);
		Set<Long> orderNos = getOrderNos(qmOrders);
		
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
	
	public void syncLogisticsToERP(Set<Integer> rec_ids) {
		Iterator<Integer> iterator = rec_ids.iterator();
		while (iterator.hasNext()) {
			Integer rec_id = iterator.next();
			erpDelegator.syncLogistics(rec_id, true);
		}
	}
	
	public String getLogisticsFromErp() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("limit", "20");
		return WdtSkuUtil.send(AdminConstants.WDT_LOGISTICS_SYN_QUERY_URL, map);
	}

	public void updateLogisticso(List<Map<String, Object>> list) {
		if (list.size() < 1) {
			return;
		}
    	
		Set<Long> orderNos = gatherOrderNosFromMap(list);
    	Map<Long, QMOrder> qMOrderMap = qMOrderService.qMOrderOfNos(orderNos);
    	
    	qMOrderDao.batchUpdate(orderNos, null, null, OrderStatus.DELIVER.getIntValue());
    	
    	//和后台发货接口共用
    	for(Map<String, Object> map : list) {
    		Long orderNo = Long.parseLong(((String) map.get("orderNo")).substring(2));
    		String logistics_name = (String) map.get("expressSupplier");
    		String logistics_no = (String) map.get("expressOrderNo");
    		
    		QMOrder qmOrder = qMOrderMap.get(orderNo);
    		if (qmOrder == null) {
				throw new ParameterErrorException("QianMiFacade.updateExpressInfo() ERROR! 未找到改订单！订单号：" + orderNo);
			}
    		
    		QMExpressInfo qmExpressInfo = assembleExpressInfo(qmOrder, logistics_name, logistics_no);
    		qMExpressInfoDao.add(qmExpressInfo);
    	}
	}

	private QMExpressInfo assembleExpressInfo(QMOrder qmOrder, String logistics_name, String logistics_no) {
		long time = System.currentTimeMillis();
		QMExpressInfo qmExpressInfo = new QMExpressInfo();
		qmExpressInfo.setBuyerNick(qmOrder.getBuyerNick());
		qmExpressInfo.setCreateTime(time);
		qmExpressInfo.setExpressNo(logistics_no);
		qmExpressInfo.setExpressSupplier(logistics_name);
		qmExpressInfo.setOrderNo(qmOrder.getOrderNo());
		qmExpressInfo.setStatus(0);
		qmExpressInfo.setUpdateTime(time);
		
		return qmExpressInfo;
	}

	private Set<Long> gatherOrderNosFromMap(List<Map<String, Object>> list) {
		Set<Long> orderNos = new HashSet<Long>();
    	for(Map<String, Object> map : list) {
    		Long orderNo = Long.parseLong(((String) map.get("orderNo")).substring(2));
    		orderNos.add(orderNo);
    	}
    	return orderNos;
	}

}
