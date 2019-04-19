package com.jiuy.core.dao.mapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jiuyuan.entity.qianmi.QMExpressInfo;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Repository
public class QMExpressInfoDaoSqlImpl implements QMExpressInfoDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public QMExpressInfo add(QMExpressInfo qmExpressInfo) {
		sqlSessionTemplate.insert("com.jiuy.core.dao.mapper.QMExpressInfoDaoSqlImpl.add", qmExpressInfo);
		return qmExpressInfo;
	}

	@Override
	public List<QMExpressInfo> search(Collection<Long> orderNos) {
		Map<String, Object> params = new HashMap<>();
		
		params.put("orderNos", orderNos);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.mapper.QMExpressInfoDaoSqlImpl.search", params);
	}
	
	
}
