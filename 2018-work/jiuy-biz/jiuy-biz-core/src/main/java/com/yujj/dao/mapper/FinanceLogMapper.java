package com.yujj.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.FinanceLog;
import com.jiuyuan.entity.query.PageQuery;

/**
 * @author jeff.zhan
 * @version 2016年11月17日 上午9:18:20
 * 
 */

@DBMaster
public interface FinanceLogMapper {

	List<FinanceLog> search(@Param("pageQuery") PageQuery pageQuery, @Param("types") List<Integer> types, @Param("storeId") Long storeId);

	int searchCount(@Param("types") List<Integer> types, @Param("storeId") Long storeId);
	
	int addFinanceLog(@Param("financeLog") FinanceLog financeLog);

}
