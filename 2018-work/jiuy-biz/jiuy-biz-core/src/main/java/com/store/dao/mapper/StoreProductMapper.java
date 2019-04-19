package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.StoreProduct;


/**
 * @author jeff.zhan
 * @version 2016年11月30日 下午5:32:14
 * 
 */

@DBMaster
public interface StoreProductMapper {

//	List<StoreProduct> getByStoreId(@Param("storeId") long storeId);
//
//	List<StoreProduct> getByStoreIdType(@Param("storeId") long storeId, @Param("type") int type);

	int insertStoreProduct(@Param("orderItemList") Collection<ShopStoreOrderItem> orderItemList);

//	List<Map<String, Object>> getProductByStoreId(@Param("storeId") long storeId);
//
//	int getCountBySaleStatus(@Param("type") int type);
//
//	List<Map<String, Object>> getProductByStoreIdType(@Param("pageQuery") PageQuery pageQuery, @Param("storeId") long storeId, @Param("type") int type, @Param("content") String content);
//
	List<StoreProduct> getByStoreIdProductId(@Param("storeId") long storeId, @Param("productId") long productId);
//
	StoreProduct getByStoreIdSkuId(@Param("storeId") long storeId, @Param("skuId") long skuId);
//
//	int outStock(@Param("storeId") long storeId, @Param("skuId") long skuId, @Param("onCount") long onCount, @Param("offCount") int offCount);
//
//	int updateSaleStatus(@Param("storeId") long storeId, @Param("saleStatus") int saleStatus, @Param("productId") int productId);
//
//	int deleteBySkuId(@Param("storeId") long storeId, @Param("skuId") long skuId);
//
//	int getProductByStoreIdTypeCount(@Param("storeId") long storeId, @Param("type") int type, @Param("content") String content);
//

}
