package com.store.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.newentity.StoreBusiness;

@DBMaster
public interface StoreBusinessMapper {

	StoreBusiness getById(@Param("id") long id);
	StoreBusiness getByIdNew(@Param("id") long id);
	int updateStoreIncome(@Param("id") Long id, @Param("commission") double commission);

	List<StoreBusiness> getAll();

	@MapKey("id")
	Map<Long, StoreBusiness> getAllMap();

	int updMemberCouponTotal(@Param("memberCount")int memberCount, @Param("couponCount")int couponCount, @Param("money")double money,
			@Param("storeId")long storeId);
	List<StoreBusiness> getAllOpenWxaStoreList();

	int updateRate(@Param("rate") double rate,@Param("storeId") long storeId);

	int updateButtonStatus(@Param("synchronousButtonStatus") int synchronousButtonStatus,@Param("storeId") long storeId);
	
}