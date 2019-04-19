package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.ExpressSupplier;

/**
 * @author jeff.zhan
 * @version 2016年11月14日 下午3:25:33
 * 
 */

@DBMaster
public interface ExpressSupplierMapper {

	ExpressSupplier getExpressSupplierByEngName(@Param("engName") String engName);
	
}