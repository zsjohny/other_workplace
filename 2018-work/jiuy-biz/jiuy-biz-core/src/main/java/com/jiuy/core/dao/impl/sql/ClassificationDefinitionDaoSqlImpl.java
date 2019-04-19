package com.jiuy.core.dao.impl.sql;

import java.util.List;

import com.jiuy.core.dao.ClassificationDefinitionDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.ClassificationDefinition;

/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public class ClassificationDefinitionDaoSqlImpl extends DomainDaoSqlSupport<ClassificationDefinition, Integer>  implements ClassificationDefinitionDao {

    @Override
    protected Class<?> getMetaClass() {
        return ClassificationDefinition.class;
    }

	@Override
	public List<ClassificationDefinition> loadAll() {
		return getSqlSession().selectList(getMetaClassName() + ".loadAll");
	}

	
    
}
