package com.store.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.OutStockCart;

/**
 * @author jeff.zhan
 * @version 2016年11月30日 下午6:54:04
 * 
 */

@DBMaster
public interface OutStockCartMapper {

	List<OutStockCart> getByStoreId(@Param("storeId") long storeId);

	int add(@Param("storeId") long storeId, @Param("productId") long productId, @Param("skuId") long skuId, @Param("count") int count, @Param("currentTime") long currentTime, @Param("cash") double cash);

	List<OutStockCart> getByStoreIdSkuId(@Param("storeId") long storeId, @Param("skuId") long skuId);

	int delete(@Param("id") long id);

	int deleteBySKU(@Param("storeId") long storeId, @Param("skuId") long skuId);

	OutStockCart getByStoreIdSkuIdCash(@Param("storeId") long storeId, @Param("skuId") long skuId, @Param("cash") double cash);

}
