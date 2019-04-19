package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.logistics.LOLocation;

@DBMaster
public interface LOLocationMapper {

	LOLocation getByName(@Param("cityName") String cityName);

}
