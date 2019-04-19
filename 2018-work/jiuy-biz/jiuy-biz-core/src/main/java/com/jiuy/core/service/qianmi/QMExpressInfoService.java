package com.jiuy.core.service.qianmi;

import java.util.Collection;
import java.util.Map;

import com.jiuyuan.entity.qianmi.QMExpressInfo;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public interface QMExpressInfoService {

	Map<Long, QMExpressInfo> expressInfoOfNos(Collection<Long> orderNos);

}
