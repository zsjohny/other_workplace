/**
 * 
 */
package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.TreeDictionary;

/**
 * @author LWS
 *
 */
public interface TreeDictionaryDao extends DomainDao<TreeDictionary, String>{

	public List<TreeDictionary> loadTreeDictionaryByGroup(String groupId);
	public List<TreeDictionary> loadTreeDictionaryByLevel(String groupId,int dictLevel);
	public List<TreeDictionary> loadTreeDictionaryByParentId(String groupId,String parentId);
	public List<TreeDictionary> loadTreeDictionaryByDictId(String groupId,String dictId);
}
