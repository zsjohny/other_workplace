package com.jiuy.core.dao.mapper;

import java.util.Collection;

import com.jiuyuan.entity.qianmi.QMProductSKU;

/**
 * @author jeff.zhan
 * @version 2016年10月21日 下午7:04:52
 * 
 */
public interface QMProductSKUDao {

	int batchAdd(Collection<QMProductSKU> qmProductSKUs);

}
