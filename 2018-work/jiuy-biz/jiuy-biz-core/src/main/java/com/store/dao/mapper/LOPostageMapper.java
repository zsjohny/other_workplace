package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.logistics.LOPostage;

@DBMaster
public interface LOPostageMapper {

	LOPostage getPostage(@Param("deliveryLocation") int deliveryLocation, @Param("distributionLocation") int distributionLocationId);

}
