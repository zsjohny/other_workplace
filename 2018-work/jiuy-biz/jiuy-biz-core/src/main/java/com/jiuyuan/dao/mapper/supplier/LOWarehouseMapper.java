package com.jiuyuan.dao.mapper.supplier;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;

@DBMaster
public interface LOWarehouseMapper {

	LOWarehouse getById(@Param("id") long id);

	List<LOWarehouse> srchWarehouse(@Param("pageQuery")PageQuery pageQuery, @Param("name")String name);

}
