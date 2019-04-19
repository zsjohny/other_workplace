package com.jiuy.core.service.qianmi;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuyuan.entity.qianmi.QMOrder;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMOrderService {

	Map<String, List<QMOrder>> getOrdersOfBuyers(Integer orderStatus, Long mergedId);

	List<QMOrder> search(Collection<Long> mergedIds);

	Map<Long, QMOrder> qMOrderOfNos(Collection<Long> orderNos);

}
