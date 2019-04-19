package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.List;

import com.jiuyuan.entity.qianmi.QMExpressInfo;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMExpressInfoDao {

	QMExpressInfo add(QMExpressInfo qmExpressInfo);

	List<QMExpressInfo> search(Collection<Long> orderNos);

}
