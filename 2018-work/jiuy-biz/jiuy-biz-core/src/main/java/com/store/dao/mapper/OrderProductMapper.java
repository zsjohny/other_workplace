/**
 * 
 */
package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.Product;
import com.store.entity.OrderProduct;

/**
* @author DongZhong
* @version 创建时间: 2016年12月16日 上午10:20:31
*/
@DBMaster
public interface OrderProductMapper {

	/**
	 * @param orderProducts
	 */
	int insertOrderProduct(@Param("orderProductList") List<OrderProduct> orderProductList);

	List<OrderProduct> getOrderProductsByOrderNoBatch(@Param("storeId") long storeId, @Param("orderNoList") Collection<Long> orderNoList);

	List<Product> getProductsByOrderNoBatch(@Param("storeId") long storeId, @Param("orderNoList") Collection<Long> orderNoList);

}
