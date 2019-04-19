package com.jiuy.core.service;

import java.util.Collection;
import java.util.Map;

import com.jiuyuan.entity.ExpressInfo;

public interface ExpressInfoService {

	Map<Long, ExpressInfo> expressInfoMapOfOrderNos(Collection<Long> allOrderNos);

	int remove(Collection<Long> orderNos);

}
