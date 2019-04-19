package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.logistics.LOLocation;

import java.util.List;

@DBMaster
public interface LOLocationMapper {

	List<LOLocation> getByName(@Param("cityName") String cityName);

}
