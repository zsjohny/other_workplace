/**
 * 
 */
package com.store.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.store.entity.StoreCartItem;

/**
 * @author LWS
 *
 */
@DBMaster
public interface ShoppingCartMapper {
    
//    public List<DiscountInfo> getDiscountInfoListByType(@Param("type") int type, @Param("ids") Collection<Long> ids);
//
//	public int removeCartItemByIds(@Param("userId") long userId, @Param("ids") Collection<Long> ids);
//	
//    public List<StoreCartItem> getCartItems(long userId);
//      
//    public List<DiscountInfo> getDiscountInfoListById(@Param("type") int type, @Param("id") long id);
	
    public List<StoreCartItem> getCartItems(long userId);
    
    public List<DiscountInfo> getDiscountInfoListById(@Param("type") int type, @Param("id") long id);
    
    public List<DiscountInfo> getDiscountInfoListByType(@Param("type") int type, @Param("ids") Collection<Long> ids);
	
    
    public StoreCartItem getCartItem(@Param("userId") long userId, @Param("productId") long productId,
                                @Param("skuId") long skuId);

    public int addCartItem(StoreCartItem cartItem);

    public int addCount(@Param("userId") long userId, @Param("productId") long productId, @Param("skuId") long skuId,
                        @Param("count") int count, @Param("time") long time);

    public int removeCartItem(@Param("userId") long userId, @Param("productId") long productId,
                              @Param("skuId") long skuId);

	public int saveProductInCart(@Param("id") long id, @Param("userId") long userId, @Param("productId") long productId, @Param("skuId") long skuId, @Param("buyCount")int buyCount, @Param("isSelected")int isSelected, @Param("time") long time);

	public StoreCartItem getCartItemById(@Param("id") long id);

    public int removeCartItemByIds(@Param("userId") long userId, @Param("ids") Collection<Long> ids);

	public int removeById(@Param("userId") long storeId, @Param("id") long id);

	public int removeNotClear(@Param("storeId") long storeId);

	public int addCartItems(@Param("cartItems") List<StoreCartItem> cartItems);

	public int removeCartItemsByProductId(@Param("storeId") long storeId, @Param("productId") long productId);

	public int removeByStoreId(@Param("storeId") long storeId);

	public int removeZeroBuyCount(@Param("storeId") long storeId);

	public int batchAdd(@Param("cartItems") List<StoreCartItem> cartItems);

	public Integer getTotalCountByStoreId(@Param("storeId") long storeId);

	public int getClothesNumberCount(@Param("storeId")long storeId);

}
