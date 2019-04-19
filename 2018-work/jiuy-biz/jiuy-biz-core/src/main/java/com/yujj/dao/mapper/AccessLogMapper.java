/**
 * 
 */
package com.yujj.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.visit.AccessLog;

/**
 * @author Dongzhong
 *
 */
@DBMaster
public interface AccessLogMapper {
    public int addAccessLog(AccessLog accessLog);
}
