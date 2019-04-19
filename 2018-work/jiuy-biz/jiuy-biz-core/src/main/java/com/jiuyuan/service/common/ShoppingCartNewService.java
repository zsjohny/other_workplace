/**
 * 
 */
package com.jiuyuan.service.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.dao.mapper.shop.StoreShoppingCartNewMapper;
import com.jiuyuan.entity.newentity.CartItemNewVO;
import com.jiuyuan.entity.newentity.StoreShoppingCartNew;

/**
 * 购物车
 */
@Service
public class ShoppingCartNewService{
	private static final Logger logger = LoggerFactory.getLogger(ShoppingCartNewService.class);
	
	@Autowired
	private StoreShoppingCartNewMapper storeShoppingCartNewMapper;

	public void removeCartItems(long storeId, Set<Long> storeShoppingCartNewIds) {
		Wrapper<StoreShoppingCartNew> wrapper = new EntityWrapper<StoreShoppingCartNew>().eq("StoreId", storeId).in("Id", storeShoppingCartNewIds);
		storeShoppingCartNewMapper.delete(wrapper);
	}

	public List<StoreShoppingCartNew> getCartList(long storeId) {
		Wrapper<StoreShoppingCartNew> wrapper = new EntityWrapper<StoreShoppingCartNew>();
		wrapper.eq("Status", 0).eq("StoreId", storeId);
		wrapper.orderBy("CreateTime", true);
		return storeShoppingCartNewMapper.selectList(wrapper);
	}

	
	
	public List<CartItemNewVO> getCartItemMapList(long storeId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("storeId", storeId);
		return storeShoppingCartNewMapper.getCartItemMapList(map);
	}

}