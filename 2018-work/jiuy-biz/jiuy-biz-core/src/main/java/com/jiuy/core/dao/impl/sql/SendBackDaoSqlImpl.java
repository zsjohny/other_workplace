package com.jiuy.core.dao.impl.sql;

import com.jiuy.core.dao.SendBackDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.order.SendBack;

public class SendBackDaoSqlImpl extends DomainDaoSqlSupport<SendBack, Long> implements SendBackDao {
	
	protected Class<SendBack> getMetaClass() {
		return SendBack.class;
	}

    @Override
    public SendBack getSendBackOrderByExpressInfo(String expressSupplier, String expressOrderNo) {
        return null;
    }

    
}
