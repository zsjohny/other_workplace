package com.store.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.FinanceLog;

/**
 * @author jeff.zhan
 * @version 2016年11月17日 上午9:18:20
 * 
 */

@DBMaster
public interface FinanceLogMapper {
	
	int addFinanceLog(@Param("financeLog") FinanceLog financeLog);

}