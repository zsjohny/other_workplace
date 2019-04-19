package com.jiuy.core.dao.mapper;

import java.util.List;

import com.jiuyuan.entity.qianmi.QMOrderItem;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMOrderItemDao {

	int batchAdd(List<QMOrderItem> qmOrderItems);

	List<QMOrderItem> search(String tid);

}
