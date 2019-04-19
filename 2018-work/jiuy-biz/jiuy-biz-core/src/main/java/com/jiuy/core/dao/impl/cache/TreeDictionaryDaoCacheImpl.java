package com.jiuy.core.dao.impl.cache;

import java.util.List;

import com.jiuy.core.dao.TreeDictionaryDao;
import com.jiuy.core.dao.impl.sql.TreeDictionaryDaoSqlImpl;
import com.jiuy.core.dao.support.DomainDaoCacheSupport;
import com.jiuyuan.entity.TreeDictionary;

/**
 * 
 * @author LWS
 *
 */
public class TreeDictionaryDaoCacheImpl extends DomainDaoCacheSupport<TreeDictionary, String, TreeDictionaryDaoSqlImpl> implements TreeDictionaryDao {

	@Override
	public List<TreeDictionary> loadTreeDictionaryByGroup(String groupId) {
		return null;
	}

	@Override
	public List<TreeDictionary> loadTreeDictionaryByLevel(String groupId,
			int dictLevel) {
		return null;
	}

	@Override
	public List<TreeDictionary> loadTreeDictionaryByParentId(String groupId,
			String parentId) {
		return sqlDao.loadTreeDictionaryByParentId(groupId,parentId);
	}

	@Override
	public List<TreeDictionary> loadTreeDictionaryByDictId(String groupId,
			String dictId) {
		return null;
	}

	
    
}
