/**
 * 
 */
package com.jiuy.core.service;

import java.util.List;

import com.jiuyuan.entity.Dictionary;

/**
 * @author LWS
 *
 */
public interface ConfigurationService {

    public List<Dictionary> loadDictionaryProperty(String groupid,String dictid);
}
