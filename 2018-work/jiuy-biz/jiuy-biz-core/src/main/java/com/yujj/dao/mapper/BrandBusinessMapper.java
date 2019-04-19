package com.yujj.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.BrandBusiness;
import com.yujj.entity.StoreBusiness;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:06:45
 * 
 */
@DBMaster
public interface BrandBusinessMapper {

	BrandBusiness getById(@Param("id") long id);
	
	BrandBusiness getByBrandId(@Param("brandId") long brandId);

	
}
