package com.store.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.constant.ShopHomeTemplate;
import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.HomeFloorVOShop;

@DBMaster
public interface HomeFloorVOMapper {



	public List<HomeFloorVOShop> getHomeFloors187(@Param("pageQuery") PageQuery pageQuery, @Param("type") int typeValue);
	
//	public int  getStorePVCount(@Param("startTime") long startTime, @Param("endTime") long endTime, @Param("storeId") long storeId);
//	
	
	public int  getStoreProductNum(@Param("storeId") long storeId);
	
	public int  getStoreProductValue( @Param("storeId") long storeId);
	
	public int  getStoreFavoriteToday( @Param("storeId") long storeId, @Param("startTime") long startTime ,@Param("type") long type );
	
	public int  getStorePVCountToday(  @Param("storeId") long storeId, @Param("startTime") long startTime );
	
	public int  getStoreMemberTotal(@Param("storeId") long storeId);
	
	public int  getStoreFavoriteMonth(  @Param("storeId") long storeId, @Param("startTime") long startTime );
	
	public int  getStorePVMonth(  @Param("storeId") long storeId, @Param("startTime") long startTime );
	
	public int  getStorePVTotal( @Param("storeId") long storeId);

	public int getHomeFloorCount187(@Param("type") int typeValue, @Param("relatedId") Long relatedId);

	public List<HomeFloorVOShop> getHomeFloors188(@Param("pageQuery") PageQuery pageQuery, @Param("type") int typeValue, @Param("relatedId") Long relatedId);

	public ShopHomeTemplate getHomeFloorTemplate(@Param("id")long nextHomeTemplateId);
	
}

