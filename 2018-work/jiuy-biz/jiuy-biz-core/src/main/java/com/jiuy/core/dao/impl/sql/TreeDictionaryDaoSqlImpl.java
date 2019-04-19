package com.jiuy.core.dao.impl.sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuy.core.dao.TreeDictionaryDao;
import com.jiuy.core.dao.support.DomainDaoSqlSupport;
import com.jiuyuan.entity.TreeDictionary;

/**
 * Created by Jinwu Zhan on 15/6/2.
 */
public class TreeDictionaryDaoSqlImpl extends DomainDaoSqlSupport<TreeDictionary, String>  implements TreeDictionaryDao {

    @Override
    protected Class<?> getMetaClass() {
        return TreeDictionary.class;
    }


	@Override
	public List<TreeDictionary> loadTreeDictionaryByGroup(String groupId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("groupId", groupId);
		return getSqlSession().selectList(getMetaClassName() + ".loadList", params);
	}

	@Override
	public List<TreeDictionary> loadTreeDictionaryByLevel(String groupId,int dictLevel) {
		return null;
	}
	@Override
	public List<TreeDictionary> loadTreeDictionaryByParentId(String groupId,
			String parentId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("groupId", groupId);
		params.put("parentId", parentId);
		return getSqlSession().selectList(getMetaClassName() + ".loadList", params);
	}


	@Override
	public List<TreeDictionary> loadTreeDictionaryByDictId(String groupId,
			String dictId) {
		return null;
	}

    
}
