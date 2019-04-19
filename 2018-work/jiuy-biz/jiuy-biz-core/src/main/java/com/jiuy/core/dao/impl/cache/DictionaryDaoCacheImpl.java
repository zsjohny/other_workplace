package com.jiuy.core.dao.impl.cache;

import java.util.List;

import com.jiuy.core.dao.DictionaryDao;
import com.jiuy.core.dao.impl.sql.DictionaryDaoSqlImpl;
import com.jiuy.core.dao.support.DomainDaoCacheSupport;
import com.jiuyuan.entity.Dictionary;

/**
 * 
 * @author LWS
 *
 */
public class DictionaryDaoCacheImpl extends DomainDaoCacheSupport<Dictionary, String, DictionaryDaoSqlImpl>
		implements DictionaryDao {

	@Override
	public List<Dictionary> loadDictionaryByGroup(String groupId, String dictId) {
		return sqlDao.loadDictionaryByGroup(groupId, null);
	}

}
