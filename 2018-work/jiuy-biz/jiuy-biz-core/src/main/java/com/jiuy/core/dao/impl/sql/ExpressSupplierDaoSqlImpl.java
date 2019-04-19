package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import com.jiuy.core.dao.ExpressSupplierDao;
import com.jiuyuan.entity.newentity.ExpressSupplier;

@Repository
public class ExpressSupplierDaoSqlImpl implements ExpressSupplierDao {
	
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	@Override
	public List<ExpressSupplier> search() {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("status", 0);
		
		return sqlSessionTemplate.selectList("com.jiuy.core.dao.impl.sql.ExpressSupplierDaoSqlImpl.search", params);
	}

	@Override
	public Map<String, ExpressSupplier> itemByEngName() {
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.ExpressSupplierDaoSqlImpl.search", "engName");
	}

	@Override
	public Map<String, ExpressSupplier> itemByCnName() {
		return sqlSessionTemplate.selectMap("com.jiuy.core.dao.impl.sql.ExpressSupplierDaoSqlImpl.search", "cnName");
	}

}
