package com.yujj.dao.mapper;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.ActivityPlace;

/**
 * @author jeff.zhan
 * @version 2016年11月4日 下午4:54:15
 * 
 */

@DBMaster
public interface ActivityPlaceMapper {

	ActivityPlace getById(@Param("id") Long id);

}
