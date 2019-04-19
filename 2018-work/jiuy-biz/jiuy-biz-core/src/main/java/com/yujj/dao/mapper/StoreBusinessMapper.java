package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.yujj.entity.Brand;
import com.yujj.entity.StoreBusiness;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:06:45
 * 
 */
@DBMaster
public interface StoreBusinessMapper {

	StoreBusiness getById(@Param("id") long id);
	
	StoreBusiness getBelongStoreBusinessByUserId(@Param("userId") long userId);

	List<StoreBusiness> getAll();

	int addMemberNumber(@Param("id") Long id);
	
	int updateStoreIncome(@Param("id") Long id, @Param("params") Map<String, Object> params);


    @MapKey("id")
    Map<Long, StoreBusiness> getStoreMap(@Param("storeIds") Collection<Long> storeIds);
}
