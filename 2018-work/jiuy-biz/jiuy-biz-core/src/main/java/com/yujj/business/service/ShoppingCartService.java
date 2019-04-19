package com.yujj.business.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.shopping.CartItem;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.dao.mapper.ShoppingCartMapper;

@Service
public class ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    public List<CartItem> getCartItems(long userId) {
        return shoppingCartMapper.getCartItems(userId);
    }
    public List<DiscountInfo> getDiscountInfoListById(int type, long id) {
    	return shoppingCartMapper.getDiscountInfoListById(type ,id);
    }
    public List<DiscountInfo> getDiscountInfoListByType(int type,  Collection<Long> ids) {
    	return shoppingCartMapper.getDiscountInfoListByType(type ,ids);
    }

    public CartItem getCartItem(long userId, long productId, long skuId) {
        return shoppingCartMapper.getCartItem(userId, productId, skuId);
    }

    public int addCartItem(CartItem cartItem) {
        return shoppingCartMapper.addCartItem(cartItem);
    }

    public int addCount(long userId, long productId, long skuId, int count, long time) {
        return shoppingCartMapper.addCount(userId, productId, skuId, count, time);
    }

    public int removeCartItem(long userId, long productId, long skuId) {
        return shoppingCartMapper.removeCartItem(userId, productId, skuId);
    }

    /**
     * 保存购物车信息
     * @param id
     * @param userId
     * @param productId
     * @param skuId
     * @param buyCount
     * @param isSelected
     * @param time
     * @return
     */
	public int saveProductInCart(long id, long userId, long productId, long skuId, int buyCount, int isSelected, long time) {
		return shoppingCartMapper.saveProductInCart(id, userId, productId, skuId, buyCount, isSelected, time);
	}

	/**
	 * 根据主键id查找购物车记录
	 * @param id
	 * @return
	 */
	public CartItem getCartItemById(long id) {
		return shoppingCartMapper.getCartItemById(id);
	}

	/**
	 * 根据主键id删除购物车记录
	 * @param id
	 * @return
	 */
    public int removeCartItem(long userId, long id) {
        return removeCartItems(userId, CollectionUtil.createSet(id));
	}

    public int removeCartItems(long userId, Collection<Long> ids) {
        return shoppingCartMapper.removeCartItemByIds(userId, ids);
    }
}
