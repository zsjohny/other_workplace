package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.Activity;

@DBMaster
public interface ActivityMapper {

    Activity getActivity(@Param("activityCode") String activityCode);

}
