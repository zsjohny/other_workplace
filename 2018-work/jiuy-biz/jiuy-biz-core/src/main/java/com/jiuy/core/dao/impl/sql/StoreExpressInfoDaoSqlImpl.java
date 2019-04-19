package com.jiuy.core.dao.impl.sql;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.StoreExpressInfoDao;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreExpressInfo;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月8日 下午7:52:33
*/
@Repository
@DBMaster
public class StoreExpressInfoDaoSqlImpl implements StoreExpressInfoDao {

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<StoreExpressInfo> expressInfoOfOrderNos(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreExpressInfoDaoSqlImpl.expressInfoOfOrderNos", params);
	}

	@Override
	public List<StoreExpressInfo> expressInfoOfBlurOrderNo(String expressOrderNo) {
		Map<String, Object> params = new HashMap<String, Object>();

        params.put("expressOrderNo", expressOrderNo);

        return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.StoreExpressInfoDaoSqlImpl.expressInfoOfBlurOrderNo",
            params);
	}

	@Override
	public int remove(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.update("com.jiuy.core.dao.impl.sql.StoreExpressInfoDaoSqlImpl.remove", params);
	}

	@Override
	public int addItem(StoreExpressInfo storeExpressInfo) {
		return sqlSessionTemplate.insert("com.jiuy.core.dao.impl.sql.StoreExpressInfoDaoSqlImpl.addItem", storeExpressInfo);
	}
}
