package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ExpressInfo;

/**
 * @author jeff.zhan
 * @version 2016年11月14日 下午3:22:47
 * 
 */

@DBMaster
public interface ExpressInfoMapper {

	    ExpressInfo getUserExpressInfoByOrderNo(@Param("userId") long userId, @Param("orderNo") long orderNo);

		String getExpressChineseNameByExpressSupplier(@Param("expressSupplier")String expressSupplier);

}
