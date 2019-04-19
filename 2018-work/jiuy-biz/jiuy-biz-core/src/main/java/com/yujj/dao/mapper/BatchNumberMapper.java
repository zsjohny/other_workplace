package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.BatchNumber;

@DBMaster
public interface BatchNumberMapper {

	BatchNumber getBatchNumber(@Param("batchNo") String batchNo, @Param("supplierCode") String supplierCode,
			@Param("innerCode") int innerCode);

	BatchNumber getBatchNumberPatch(@Param("batchNo") String batchNo, @Param("supplierCode") String supplierCode,
			@Param("innerCode") int innerCode);

}
