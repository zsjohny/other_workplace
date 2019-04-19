package com.jiuyuan.dao.mapper.supplier;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;

/**
 * @author DongZhong
 * @version 创建时间: 2016年12月28日 下午2:58:27
 */
@DBMaster
public interface WhitePhoneMapper {
	int getWhitePhone(@Param("phone") String phone);
}
