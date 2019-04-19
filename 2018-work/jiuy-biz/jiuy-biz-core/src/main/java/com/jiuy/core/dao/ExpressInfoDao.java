package com.jiuy.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.ExpressInfo;

public interface ExpressInfoDao extends DomainDao<ExpressInfo, Long> {

    ExpressInfo addItem(ExpressInfo info);

	ExpressInfo getExpressInfoByGroupId(long orderItemGroupId);

	int updateByGroupId(ExpressInfo info);

	int addExpressInfos(List<Map<String, Object>> list);

    List<ExpressInfo> expressInfoOfBlurOrderNo(String expressOrderNo);

    List<ExpressInfo> expressInfoOfOrderNos(Collection<Long> orderNos);

	int remove(Collection<Long> orderNos);

	public int addExpressInfosByExpressInfo(List<ExpressInfo> list);
}
