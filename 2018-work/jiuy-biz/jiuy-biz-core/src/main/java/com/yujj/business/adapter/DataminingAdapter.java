package com.yujj.business.adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.VisitService;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewVO;
import com.yujj.entity.product.Product;

@Service
public class DataminingAdapter {
	@Autowired
	private ProductService productService;
	
	@Autowired
	private OrderService orderService;
	
    @Autowired
    private VisitService visitService;
    
    @Autowired
    private MemcachedService memcachedService;
	//删除旧表
//	//够买此商品的用户也同时购买了
//	@SuppressWarnings("unchecked")
//	public List<Product> getBuyAlsoProduct(long userId, long orderId, PageQuery pageQuery) {
//		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
//        
//        String key = userId + "buy_also_product";
//        Object obj = memcachedService.get(groupKey, key);
//        
//        if (obj != null) {
//        	return (List<Product>) obj;
//        } else {
//        	List<Long> productIds = new ArrayList<Long>();
//    		List<OrderItem> orderItems = orderService.getOrderItems(userId, CollectionUtil.createList(orderId));
//    		
//    		for(OrderItem orderItem : orderItems) {
//    			productIds.add(orderItem.getProductId());
//    		}
//    		
//    		List<Product> products = getBuyAlsoProduct(productIds, pageQuery);
//        	
//        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, products);
//        	
//        	return products;
//        }
//	}
	//够买此商品的用户也同时购买了  新版订单
	@SuppressWarnings("unchecked")
	public List<Product> getBuyAlsoProductNew(long userId, OrderNew orderNew, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
		
		String key = userId + "buy_also_product";
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<Product>) obj;
		} else {
			List<OrderItem> orderItems = new ArrayList<OrderItem>();
			List<Long> productIds = new ArrayList<Long>();
			if(orderNew.getParentId() == -1){
				
				Set<Long> orderNOs = new HashSet<Long>();
				List<OrderNewVO> childOrderList = orderService.getChildOrderList(userId, CollectionUtil.createList(orderNew.getOrderNo()));
	    		
	    		if(childOrderList != null && childOrderList.size() >0 ){
	    			
		    		for(OrderNew childOrder : childOrderList ){
		    			orderNOs.add(childOrder.getOrderNo());
		    		}
					
					orderItems = orderService.getOrderNewItems(userId, orderNOs);
	    		}
				
			}else{
				orderItems = orderService.getOrderNewItems(userId, CollectionUtil.createList(orderNew.getOrderNo()));
				
			}
			List<Product> products = new ArrayList<Product>();
			if(orderItems != null && orderItems .size() > 0){
				
				for(OrderItem orderItem : orderItems) {
					productIds.add(orderItem.getProductId());
				}
				
				products = getBuyAlsoProduct(productIds, pageQuery);
				
				memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, products);
			}
			
			
			return products;
		}
	}
	
	public List<Product> getBuyAlsoProduct(Collection<Long> productIds, PageQuery pageQuery) {
		int count = pageQuery.getLimit() * 25;
		
		List<Product> products = productService.getBuyAlsoProduct(productIds, pageQuery, count);
		
		List<Product> hotProducts = productService.getBestSellerProductList(pageQuery);
		if(products.size() < pageQuery.getLimit()) {
			
			int i = 0;
			while(pageQuery.getLimit() > products.size()) {
				if(i >= (hotProducts.size() - 1)) {
					products.add(hotProducts.get(hotProducts.size() - 1));
				} else {
					products.add(hotProducts.get(i));
					i++;
				}
			}
		}
		
		List<Product> products2 = new ArrayList<Product>();
		for(int i = 0; i < 4; i++) {
			products2.add(hotProducts.get(i));
		}
		
		return products2;
	} 

	//猜你喜欢
	@SuppressWarnings("unchecked")
	public List<Product> getBuyGuessProduct(long userId, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
        
        String key = userId + "buy_guess_product";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<Product>) obj;
        } else {
        	int count = pageQuery.getLimit() * 25;
        	
        	if(userId <= 0) {
        		return productService.getBestSellerProductList(pageQuery);
        	}
        	
        	List<Product> products = visitService.getBuyGuessProduct(userId, pageQuery, count);
        	List<Product> hotProducts = productService.getBestSellerProductList(pageQuery);
        	if(products.size() < pageQuery.getLimit()) {
        		
        		int i = 0;
        		while(pageQuery.getLimit() > products.size()) {
        			if(i >= (hotProducts.size() - 1)) {
        				products.add(hotProducts.get(hotProducts.size() - 1));
        			} else {
        				products.add(hotProducts.get(i));
        				i++;
        			}
        		}
        	}
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, products);
        	
        	List<Product> products2 = new ArrayList<Product>();
    		for(int i = 0; i < 4; i++) {
    			products2.add(hotProducts.get(i));
    		}
    		
    		return products2;
        }
	}
	
	//猜你喜欢1.8.6
	@SuppressWarnings("unchecked")
	public List<Product> getBuyGuessProduct186(long userId, PageQuery pageQuery) {
		//return new ArrayList<Product>();
		String groupKey = MemcachedKey.GUESS_YOU_LIKE;
		
		String key =  "guess_you_like_" + userId + pageQuery.getPageSize();
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<Product>) obj;
		} else {
//			if(userId <= 0) {
//				return productService.getBuyGuessProduct186(pageQuery);
//			}
			List<Product> guessProducts = productService.getUserBuyGuessProduct186(userId, pageQuery);
		
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, guessProducts);
			
			return guessProducts;
		}
	}
	
	
/*
 * author :czy 20161001
 */
	//看了又看
	@SuppressWarnings("unchecked")
	public List<Product> getSeeAgainProduct186(long userId, PageQuery pageQuery) {
		//return new ArrayList<Product>();
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
		
		String key = userId + "see_again_product"+pageQuery.getPageSize();
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<Product>) obj;
		} else {
//			if(userId <= 0) {
//				return productService.getBestSellerProductList186(pageQuery);
//			}
			List<Product> hotProducts = productService.getUserBestSellerProductList186(userId, pageQuery);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, hotProducts);
			
			
			return hotProducts;
		}
	}
	
	
	/*
	 * author :czy 20170301
	 */
	
	//看了又看210
	@SuppressWarnings("unchecked")
	public List<Product> getSeeAgainProduct210(long productId, PageQuery pageQuery) {
		//return new ArrayList<Product>();
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
		
		String key = productId + "see_again_product"+pageQuery.getPageSize();
		Object obj = memcachedService.get(groupKey, key);
		
		if (obj != null) {
			return (List<Product>) obj;
		} else {
//			if(userId <= 0) {
//				return productService.getBestSellerProductList186(pageQuery);
//			}
			List<Product> hotProducts = productService.getSeeAgainProduct210(productId, pageQuery);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, hotProducts);
			
			
			return hotProducts;
		}
	}
	
	//看了又看
	@SuppressWarnings("unchecked")
	public List<Product> getSeeAgainProduct(long userId, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
        
        String key = userId + "see_again_product";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<Product>) obj;
        } else {
        	int count = pageQuery.getLimit() * 25;
        	
        	if(userId <= 0) {
        		return productService.getBestSellerProductList(pageQuery);
        	}
        	
        	List<Product> products = visitService.getSeeAgainProduct(userId, pageQuery, count);
        	List<Product> hotProducts = productService.getBestSellerProductList(pageQuery);
        	if(products.size() < pageQuery.getLimit()) {
        		
        		int i = 0;
        		while(pageQuery.getLimit() > products.size()) {
        			if(i >= (hotProducts.size() - 1)) {
        				products.add(hotProducts.get(hotProducts.size() - 1));
        			} else {
        				products.add(hotProducts.get(i));
        				i++;
        			}
        		}
        	}
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, products);
        	
        	List<Product> products2 = new ArrayList<Product>();
    		for(int i = 0; i < 4; i++) {
    			products2.add(hotProducts.get(i));
    		}
    		
    		return products2;
        }
	}
	
	
	//为你搭配之类的
	@SuppressWarnings("unchecked")
	public List<Product> getMatchYouProduct(long userId, PageQuery pageQuery) {
		String groupKey = MemcachedKey.GROUP_KEY_ADVERTISEMRNT;
        
        String key = userId + "match_you_product";
        Object obj = memcachedService.get(groupKey, key);
        
        if (obj != null) {
        	return (List<Product>) obj;
        } else {
        	List<Product> products = productService.getBestSellerProductList(pageQuery);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, products);
        	
        	return products;
        }
	}
	
}
