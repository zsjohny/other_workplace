package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.qianmi.QMOrder;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMOrderDao {

	QMOrder add(QMOrder qmOrder);

	List<QMOrder> search(Integer orderStatus, Long mergedId, String sortSql);

	int update(Long orderNo, String tid, Long mergedId);

	int batchUpdate(Collection<Long> orderNos, Long mergedId, Long pushTime, Integer orderStatus);

	List<QMOrder> getUnpushedMergedQMOrders(Long startTime, Long endTime);

	List<QMOrder> search(Collection<Long> mergedIds, Collection<Long> orderNos);

}
