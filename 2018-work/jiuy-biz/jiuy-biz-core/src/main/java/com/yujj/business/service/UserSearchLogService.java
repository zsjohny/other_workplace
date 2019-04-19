/**
 *
 */
package com.yujj.business.service;

import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.search.UserSearchLog;
import com.yujj.dao.mapper.UserSearchLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DongZhong
 * @version 创建时间: 2016年9月23日 上午9:04:17
 */
@Service
public class UserSearchLogService {
    @Autowired
    private UserSearchLogMapper userSearchLogMapper;


    public int addUserSearchLog(String content, long userId) {
        if (1 == 1) {
            return 1;
        }

        //TODO:清理垃圾日志
        long time = System.currentTimeMillis();

        UserSearchLog userSearchLog = new UserSearchLog();
        userSearchLog.setUserId(userId);
        userSearchLog.setContent(content);
        userSearchLog.setCreateTime(time);
        return userSearchLogMapper.addUserSearchLog(userSearchLog);
    }


    public List<UserSearchLog> getUserSearchLogs(long userId, PageQuery pageQuery) {
        return userSearchLogMapper.getUserSearchLogs(userId, pageQuery);
    }
}
