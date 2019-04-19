package com.store.dao.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.query.PageQuery;
import com.store.entity.StoreFavorite;

/**
 * @author jeff.zhan
 * @version 2016年12月10日 下午2:43:13
 * 
 */

@DBMaster
public interface StoreFavoriteMapper {

	int add(StoreFavorite storeFavorite);

	StoreFavorite getFavorite(@Param("storeId") long storeId, @Param("relatedId") long relatedId, @Param("type") int type);

	int cancel(StoreFavorite storeFavorite);

	int searchCountByType(@Param("storeId") long storeId, @Param("type") int type);

	List<StoreFavorite> searchByType(@Param("pageQuery") PageQuery pageQuery, @Param("storeId") long storeId, @Param("type") int type);

}
