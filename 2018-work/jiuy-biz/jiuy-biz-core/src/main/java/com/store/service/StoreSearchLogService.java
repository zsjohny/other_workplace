/**
 * 
 */
package com.store.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.query.PageQuery;
import com.store.dao.mapper.StoreSearchLogMapper;
import com.store.entity.StoreSearchLog;


/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 上午9:04:17
*/
@Service
public class StoreSearchLogService {
    @Autowired
    private StoreSearchLogMapper userSearchLogMapper;

    
    public int addUserSearchLog(String content, long userId) {
    	long time = System.currentTimeMillis();
    	
    	StoreSearchLog userSearchLog = new StoreSearchLog();
    	userSearchLog.setStoreBusinessId(userId);
    	userSearchLog.setContent(content);
    	userSearchLog.setCreateTime(time);
    	return userSearchLogMapper.addUserSearchLog(userSearchLog);
    }

	
    public List<StoreSearchLog> getUserSearchLogs(long userId,  PageQuery pageQuery) {
		return userSearchLogMapper.getUserSearchLogs(userId, pageQuery);
	}
}
