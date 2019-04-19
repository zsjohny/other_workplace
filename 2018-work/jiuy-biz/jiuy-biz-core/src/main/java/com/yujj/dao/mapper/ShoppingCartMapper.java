/**
 * 
 */
package com.yujj.dao.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jiuyuan.dao.annotation.DBMaster;
import com.jiuyuan.entity.shopping.CartItem;
import com.jiuyuan.entity.shopping.DiscountInfo;

/**
 * @author LWS
 *
 */
@DBMaster
public interface ShoppingCartMapper {

    public List<CartItem> getCartItems(long userId);
    
    public List<DiscountInfo> getDiscountInfoListById(@Param("type") int type, @Param("id") long id);
    
    public List<DiscountInfo> getDiscountInfoListByType(@Param("type") int type, @Param("ids") Collection<Long> ids);
	
    
    public CartItem getCartItem(@Param("userId") long userId, @Param("productId") long productId,
                                @Param("skuId") long skuId);

    public int addCartItem(CartItem cartItem);

    public int addCount(@Param("userId") long userId, @Param("productId") long productId, @Param("skuId") long skuId,
                        @Param("count") int count, @Param("time") long time);

    public int removeCartItem(@Param("userId") long userId, @Param("productId") long productId,
                              @Param("skuId") long skuId);

	public int saveProductInCart(@Param("id") long id, @Param("userId") long userId, @Param("productId") long productId, @Param("skuId") long skuId, @Param("buyCount")int buyCount, @Param("isSelected")int isSelected, @Param("time") long time);

	public CartItem getCartItemById(@Param("id") long id);

    public int removeCartItemByIds(@Param("userId") long userId, @Param("ids") Collection<Long> ids);
	
}
