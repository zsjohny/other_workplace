/**
 * 
 */
package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.ClassificationDefinition;

/**
 * @author LWS
 *
 */
public interface ClassificationDefinitionDao extends DomainDao<ClassificationDefinition, Integer>{

	public List<ClassificationDefinition> loadAll();
}
