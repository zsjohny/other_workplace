package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.logistics.LOWarehouse;

@DBMaster
public interface YJJLOWarehouseMapper {

	LOWarehouse getById(@Param("id") long id);

}
