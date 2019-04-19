package com.yujj.dao.mapper;

import com.jiuyuan.dao.annotation.DBMaster;

@DBMaster
public interface IdMapper {

    long getLastLogId();
    
    int updateLastLogId(int count);

}
