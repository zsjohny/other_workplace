package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.DictionaryDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.Dictionary;


/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public class DictionaryDaoSqlImpl extends DomainDaoSqlSupport<Dictionary, String>  implements DictionaryDao {

    @Override
    protected Class<?> getMetaClass() {
        return Dictionary.class;
    }

	@Override
	public List<Dictionary> loadDictionaryByGroup(String groupId,String dictId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("groupId", groupId);
		params.put("dictId", dictId);
		return getSqlSession().selectList(getMetaClassName() + ".loadList", params);
	}

    
}
