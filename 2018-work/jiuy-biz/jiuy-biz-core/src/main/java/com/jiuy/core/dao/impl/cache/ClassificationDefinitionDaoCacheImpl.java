package com.jiuy.core.dao.impl.cache;

import java.util.List;

import com.jiuy.core.dao.ClassificationDefinitionDao;
import com.jiuy.core.dao.impl.sql.ClassificationDefinitionDaoSqlImpl;
import com.jiuy.core.dao.support.DomainDaoCacheSupport;
import com.jiuyuan.entity.ClassificationDefinition;

/**
 * 
 * @author LWS
 *
 */
public class ClassificationDefinitionDaoCacheImpl extends DomainDaoCacheSupport<ClassificationDefinition, Integer, ClassificationDefinitionDaoSqlImpl> implements ClassificationDefinitionDao {

	@Override
	public List<ClassificationDefinition> loadAll() {
		return sqlDao.loadAll();
	}

	
    
}
