package com.jiuy.core.dao;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.order.SendBack;

public interface SendBackDao extends DomainDao<SendBack, Long> {

    public SendBack getSendBackOrderByExpressInfo(String expressSupplier, String expressOrderNo);
}
