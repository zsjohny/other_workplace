package com.jiuy.core.dao.impl.sql;


import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.ExpressInfoDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.ExpressInfo;

public class ExpressInfoDaoSqlImpl extends DomainDaoSqlSupport<ExpressInfo, Long> implements ExpressInfoDao {

    @Override
    public ExpressInfo addItem(ExpressInfo info) {
        return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.addItem", info);
    }

	@Override
	public ExpressInfo getExpressInfoByGroupId(long orderItemGroupId) {
		return getSqlSession().selectOne("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.getExpressInfoByGroupId", orderItemGroupId);
	}

	@Override
	public int updateByGroupId(ExpressInfo info) {
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.updateByGroupId", info);
	}

	@Override
	public int addExpressInfos(List<Map<String, Object>> list) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("expressInfos", list);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.addExpressInfos", params);
	}

    @Override
    public List<ExpressInfo> expressInfoOfBlurOrderNo(String expressOrderNo) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("expressOrderNo", expressOrderNo);

        return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.expressInfoOfBlurOrderNo",
            params);
    }

	@Override
	public List<ExpressInfo> expressInfoOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().selectList("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.expressInfoOfOrderNos", params);
	}

	@Override
	public int remove(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return getSqlSession().update("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.remove", params);
	}

	@Override
	public int addExpressInfosByExpressInfo(List<ExpressInfo> list) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("expressInfos", list);
		
		return getSqlSession().insert("com.jiuy.core.dao.impl.sql.ExpressInfoDaoSqlImpl.addExpressInfos", params);
	}

}
