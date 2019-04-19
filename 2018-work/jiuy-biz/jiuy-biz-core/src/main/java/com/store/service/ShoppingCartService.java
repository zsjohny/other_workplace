package com.store.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.util.CollectionUtil;
import com.store.dao.mapper.ShoppingCartMapper;
import com.store.entity.StoreCartItem;


@Service
public class ShoppingCartService {
//    @Autowired
//    private ShoppingCartMapper shoppingCartMapper;
//    
//    public List<DiscountInfo> getDiscountInfoListByType(int type,  Collection<Long> ids) {
//    	return shoppingCartMapper.getDiscountInfoListByType(type ,ids);
//    }
//    
//    public int removeCartItems(long storeId, Collection<Long> ids) {
//        return shoppingCartMapper.removeCartItemByIds(storeId, ids);
//    }
//    
//    public List<StoreCartItem> getCartItems(long storeId) {
//        return shoppingCartMapper.getCartItems(storeId);
//    }
//    
//    public List<DiscountInfo> getDiscountInfoListById(int type, long id) {
//    	return shoppingCartMapper.getDiscountInfoListById(type ,id);
//    }
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    public List<StoreCartItem> getCartItems(long storeId) {
        return shoppingCartMapper.getCartItems(storeId);
    }
    public List<DiscountInfo> getDiscountInfoListById(int type, long id) {
    	return shoppingCartMapper.getDiscountInfoListById(type ,id);
    }
    public List<DiscountInfo> getDiscountInfoListByType(int type,  Collection<Long> ids) {
    	return shoppingCartMapper.getDiscountInfoListByType(type ,ids);
    }

    public StoreCartItem getCartItem(long storeId, long productId, long skuId) {
        return shoppingCartMapper.getCartItem(storeId, productId, skuId);
    }

    public int addCartItem(StoreCartItem cartItem) {
        return shoppingCartMapper.addCartItem(cartItem);
    }

    public int addCount(long storeId, long productId, long skuId, int count, long time) {
        return shoppingCartMapper.addCount(storeId, productId, skuId, count, time);
    }

    public int removeCartItem(long storeId, long productId, long skuId) {
        return shoppingCartMapper.removeCartItem(storeId, productId, skuId);
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
	public int saveProductInCart(long id, long storeId, long productId, long skuId, int buyCount, int isSelected, long time) {
		return shoppingCartMapper.saveProductInCart(id, storeId, productId, skuId, buyCount, isSelected, time);
	}

	/**
	 * 根据主键id查找购物车记录
	 * @param id
	 * @return
	 */
	public StoreCartItem getCartItemById(long id) {
		return shoppingCartMapper.getCartItemById(id);
	}

	/**
	 * 根据主键id删除购物车记录
	 * @param id
	 * @return
	 */
    public int removeCartItem(long storeId, long id) {
        return removeCartItems(storeId, CollectionUtil.createSet(id));
	}

    public int removeCartItems(long storeId, Collection<Long> ids) {
        return shoppingCartMapper.removeCartItemByIds(storeId, ids);
    }
    
	public int removeById(long storeId, long id) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.removeById(storeId, id);
	}
	public int removeNotClear(long storeId) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.removeNotClear(storeId);
	}
	public int addCartItems(List<StoreCartItem> cartItems) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.addCartItems(cartItems);
	}

	public int batchDeleteCart(long storeId, long productId) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.removeCartItemsByProductId(storeId, productId);
	}
	public int removeByStoreId(long storeId) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.removeByStoreId(storeId);
	}
	public int removeZeroBuyCount(long storeId) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.removeZeroBuyCount(storeId);
	}
	public int batchAdd(List<StoreCartItem> cartItems) {
		// TODO Auto-generated method stub
		return shoppingCartMapper.batchAdd(cartItems);
	}
	public int getTotalCountByStoreId(long storeId) {
		// TODO Auto-generated method stub
		Integer count = shoppingCartMapper.getTotalCountByStoreId(storeId);
		return count == null ? 0 : count;
	}
	
	/**
	 * 获取用户购物车款号数量接口
	 * @param storeId
	 * @return
	 */
	public int getClothesNumberCount(long storeId) {
		return shoppingCartMapper.getClothesNumberCount(storeId);
	}
}
