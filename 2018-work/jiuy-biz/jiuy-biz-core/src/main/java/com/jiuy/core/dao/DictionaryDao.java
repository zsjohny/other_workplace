/**
 * 
 */
package com.jiuy.core.dao;

import java.util.List;

import com.jiuy.core.dao.support.DomainDao;
import com.jiuyuan.entity.Dictionary;

/**
 * @author LWS
 *
 */
public interface DictionaryDao extends DomainDao<Dictionary, String>{

	public List<Dictionary> loadDictionaryByGroup(String groupId, String dictId);

}
