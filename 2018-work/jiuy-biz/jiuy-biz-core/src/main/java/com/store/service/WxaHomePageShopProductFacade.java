package com.store.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.PriceUtil;
import com.store.dao.mapper.CategoryMapper;
import com.store.entity.ShopCategory;

/**
 * 
 * @author Administrator
 *
 */
@Service
public class WxaHomePageShopProductFacade {
	private static final Logger logger = LoggerFactory.getLogger(WxaHomePageShopProductFacade.class);
	@Autowired
	private MemcachedService memcachedService;

	@Autowired
	private ShopProductMapper shopProductMapper;

	@Autowired
	private CategoryMapper categoryMapper;

	@Autowired
	private ProductServiceShop productService;
	
//	/**
//	 * 最新小程序首页商品列表
//	 * @param status
//	 * @param page
//	 * @return
//	 */
//	public Map<String, Object> getNewHomePageProductList(Page<ShopProduct> page,
//			StoreBusiness storeBusiness) {
//		//分类永远为0已经和产品确定无需根据分类查询
//		Integer categoryId = 0;
//		
//		
//		Map<String, Object> data = new HashMap<String, Object>();
//		
//		// 1、获取该商家所有在售商品
//		long time40 = System.currentTimeMillis(); 
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeBusiness.getId());
//		long time50 = System.currentTimeMillis(); 
//		long time60 = time50 - time40;
//		logger.info("1、获取该商家所有在售商品总耗时："+time60+",categoryId:"+categoryId+",商家所有在售商品数量onShelfShopProductList.size():"+onShelfShopProductList.size()+",storeId:"+storeBusiness.getId());
//			
//		 //2、从缓存获取上架商家商品的分类列表
//		long time4 = System.currentTimeMillis(); 
//		List<ShopCategory> soldOutShopProductCategoryList = getSoldOutShopProductCategoryListByCache(storeBusiness.getId(),onShelfShopProductList);
//		long time5 = System.currentTimeMillis(); 
//		long time6 = time5 - time4;
//		logger.info(" 2、获取上架商家商品的分类列表总耗时："+time6+",categoryId:"+categoryId+",soldOutShopProductCategoryList.size():"+soldOutShopProductCategoryList.size());
//				
//				 
//		// 3、根据标签获取商家商品列表
//		 long time1 = System.currentTimeMillis(); 
//		List<ShopProduct> shopProductlist = getShopProductListByCategoryId(categoryId, page, onShelfShopProductList,storeBusiness.getVip(),storeBusiness.getId());
//		 long time2 = System.currentTimeMillis(); 
//		 long time3 = time2 - time1;
//		 logger.info("3、根据标签获取商家商品列表总耗时："+time3+",categoryId:"+categoryId+",shopProductlist.size():"+shopProductlist.size());
//		 
//		
//		// 4、组装分类数据，并排重
//		 long time7 = System.currentTimeMillis(); 
//		List<Map<String, Object>> categoryList = getShopCategoryList(soldOutShopProductCategoryList);
//		 long time8 = System.currentTimeMillis(); 
//		 long time9 = time8 - time7;
//		 logger.info("4、组装分类数据，并排重总耗时："+time9+",categoryId:"+categoryId+",categoryList.size():"+categoryList.size());
//		
//		
//		data.put("categoryList", categoryList);
//
//		if (shopProductlist.size() == 0) {
//			data.put("isNoProduct", 0);
//			data.put("isMore", page.hasNext());
//			data.put("text", "没有任何商品");
//		} else {
//			// 4、对商家商品进行置顶排序处理
//			List<Map<String, Object>> productList = sortShopProductByTop(categoryId, shopProductlist);
//			data.put("productList", productList);
//			data.put("isNoProduct", 1);
//			data.put("isMore", page.hasNext());
//		}
//
//		// logger.error("微信小程序首页返回数据:status:"+categoryId+",storeId:"+storeBusiness.getId()+",data.size():"
//		// + data.size()+",isMore"+page.hasNext());
//		return data;
//	}
	
	
	
//	/**
//	 * //1、根据标签获取商家商品列表
//	 * 
//	 * @param status
//	 * @param page
//	 * @param storeId
//	 * @return
//	 */
//	private List<ShopProduct> getShopProductListByCategoryId(Integer categoryId, Page<ShopProduct> page,
//			List<ShopProduct> onShelfShopProductList,int storeBusinessVip,long storeId) {
//
//		 
//		// 2、获取分类ID及其子集分类ID集合
//		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
//		if (categoryId != 0) {
//			shopCategoryList = getShopCategoryList(categoryId);
//		}
//		
//		// 3、剔除商品
//		List<Product> shopProductPlatformProductList = getShopProductPlatformProductListCache(storeId);
//		Map<String, List<Long>> shopProductPlatformProductCategoryIdMap = getShopProductPlatformProductCategoryIdMapCache(storeId);
////		logger.info("=============shopProductPlatformProductCategoryIdMap"+shopProductPlatformProductCategoryIdMap.toString());
//		for (int i = onShelfShopProductList.size() - 1; i >= 0; i--) {
//			
//			ShopProduct shopProduct = onShelfShopProductList.get(i);
//			// 是否是自有商品：1是自有商品，0平台商品
//			int own = shopProduct.getOwn();
//			if (own == ShopProduct.platform_product) {// 平台商品
//				long productId = shopProduct.getProductId();
////				Product product = productService.getProductFromCache(productId);
//				Product product = getProductFromPlatformProductList(productId,storeId,shopProductPlatformProductList);
//				int productVip = product.getVip();
//				// //2、根据商家VIP身份剔除VIP商品
//				if (storeBusinessVip == 0 && productVip == 1) {// 非VIP身份
//					onShelfShopProductList.remove(i);
//				} else {
//					if (categoryId != 0) {
////						List<Long> categoryIds = getCategoryIdsByProductIdCache(productId);
//						List<Long> categoryIds = getShopProductPlatformProductCategoryId(productId,storeId,shopProductPlatformProductCategoryIdMap);
////						logger.info("=============productId:"+productId+",storeId:"+storeId+",categoryIds"+categoryIds.toString());
//						// 不符合该分类则移除
//						if (!checkCategoryId(shopCategoryList, categoryIds)) {
//							onShelfShopProductList.remove(i);
//						}
//					}
//				}
//			} else {// 自有商品
//				if (categoryId != 0) {
//					List<Long> categoryIds = new ArrayList<Long>();
//					categoryIds.add(shopProduct.getCategoryId());
//					// 不符合该分类则移除
//					if (!checkCategoryId(shopCategoryList, categoryIds)) {
//						onShelfShopProductList.remove(i);
//					}
//				}
//			}
//		}
//		 
//		// 3、分页
//		// 每页开始数量
//		int fromIndex = page.getOffset();
//		logger.info("获取小程序首页商品列表，fromIndex："+fromIndex);
//		// 每页结束数量
//		int toIndex = fromIndex + page.getLimit();
//		logger.info("获取小程序首页商品列表，toIndex："+toIndex);
//		// 剩余最大数量
//		int maxIndex = onShelfShopProductList.size();
//		logger.info("获取小程序首页商品列表，maxIndex："+maxIndex);
//		if (maxIndex <= toIndex) {
//			toIndex = maxIndex;
//		}
//		logger.info("获取小程序首页商品列表，toIndex："+toIndex);
//
//		// list截取含头不含尾
//		List<ShopProduct> pageList = onShelfShopProductList.subList(fromIndex, toIndex);
//		return pageList;
//	}
	
	
	
	

	/**
	 * 根据商品ID获取商品分类ID集合
	 * @param productId
	 * @return
	 */
	private List<Long> getCategoryIdsByProductIdCache(long productId) {
		List<Long> categoryIds = new ArrayList<Long>();
		String groupKey = MemcachedKey.GROUP_KEY_categoryIdsByProductId;
		String key = String.valueOf(productId);
		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
//			logger.info("从缓存获取商家商家商品的分类列表obj："+obj); 
			categoryIds = (List<Long>) obj;
//			logger.info("----------------------------------------从缓存获取根据商品ID获取商品分类ID集合categoryIds.size："+categoryIds.size()+",categoryIds.size():"+categoryIds.size()); 
		}else{
			categoryIds = shopProductMapper.getCategoryIdsByProductId(productId);
			int time = DateConstants.SECONDS_PER_DAY;
			memcachedService.set(groupKey, key, time , categoryIds);
//			logger.info("-------------------------------------------从数据库获取根据商品ID获取商品分类ID集合完成,缓存时间time:"+time+",categoryIds.size()："+categoryIds.size()); 
		}
		
		return categoryIds;
	}
	/**
	 * 获取分类ID集合
	 * @param productId
	 * @param storeId
	 * @param shopProductPlatformProductCategoryIdMap
	 * @return
	 */
	private List<Long> getShopProductPlatformProductCategoryId(long productId, long storeId,Map<String, List<Long>> shopProductPlatformProductCategoryIdMap) {
//		logger.info("=============productId:"+productId+",storeId:"+storeId+",shopProductPlatformProductCategoryIdMap:"+shopProductPlatformProductCategoryIdMap.toString());
		List<Long> categoryIds = shopProductPlatformProductCategoryIdMap.get(storeId+"_"+productId);
//		logger.info("=============productId:"+productId+",storeId:"+storeId+",categoryIds:"+categoryIds.toString());
		
		if(categoryIds != null){
			return categoryIds;
		}
		logger.info("没有从缓存的获取分类ID集合，请排查问题，productId："+productId);
		return getCategoryIdsByProductIdCache(productId);
	}
	
	/**
	 * 从列表中找商品
	 * @param productId
	 * @param storeId
	 * @return
	 */
	private Product getProductFromPlatformProductList(long productId, long storeId,List<Product> shopProductPlatformProductList) {
		for(Product product : shopProductPlatformProductList){
			if(product.getId() == productId){
				return product;
			}
		}
		logger.info("没有从缓存的平台商品列表中找到该商品，请排查问题，productId："+productId);
		return productService.getProductFromCache(productId);
	}
	
//	/**
//	 * 获取分类IDMap缓存
//	 * @param storeId
//	 * @return
//	 */
//	private Map<String, List<Long>> getShopProductPlatformProductCategoryIdMapCache(long storeId) {
//		//1、获取在架的商家商品列表
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeId);
//		Map<String, List<Long>> categoryIdsMap = new HashMap<String,List<Long>>();
//		String groupKey = MemcachedKey.GROUP_KEY_categoryIdsMapByProductId;
//		String key = String.valueOf(storeId);
//		Object obj = memcachedService.get(groupKey, key);
//		if (obj != null) {
////			logger.info("从缓存获取商家商家商品的分类列表obj："+obj); 
//			categoryIdsMap = (Map<String, List<Long>>) obj;
////			logger.info("----------------------------------------从缓存获取分类Id集合categoryIdsMap："+categoryIdsMap); 
//		}else{
//			for (ShopProduct shopProduct : onShelfShopProductList) {
//				// 是否是自有商品：1是自有商品，0平台商品
//				int own = shopProduct.getOwn();
//				if (own == ShopProduct.platform_product) {// 平台商品
//					long productId = shopProduct.getProductId();
//					categoryIdsMap.put(storeId+"_"+productId,getCategoryIdsByProductIdCache(productId));
//				}
//			}
//			int time = DateConstants.SECONDS_PER_DAY;
//			memcachedService.set(groupKey, key,time , categoryIdsMap);
////			logger.info("-------------------------------------------从数据库获取分类Id集合categoryIdsMap：:"+categoryIdsMap.toString()); 
//		}
//		return categoryIdsMap;
//	}
	

//	/**
//	 * 从缓存一次性获取商家商品中平台商品集合
//	 * @param storeId
//	 * @return
//	 */
//	private List<Product> getShopProductPlatformProductListCache(long storeId) {
//		//1、获取在架的商家商品列表
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeId);
//		
//		//2、填充商品
//		List<Product> platformProductList = new ArrayList<Product>();
//		String groupKey = MemcachedKey.GROUP_KEY_platformProductList;
//		String key = String.valueOf(storeId);
//		Object obj = memcachedService.get(groupKey, key);
//		if (obj != null) {
//			platformProductList = (List<Product>) obj;
////			logger.info("********************************从缓存一次性获取商家商品中平台商品集合完成，platformProductList.size():"+platformProductList.size());
//		}else{
//			for (ShopProduct shopProduct : onShelfShopProductList) {
//				// 是否是自有商品：1是自有商品，0平台商品
//				int own = shopProduct.getOwn();
//				if (own == ShopProduct.platform_product) {// 平台商品
//					long productId = shopProduct.getProductId();
//					platformProductList.add(productService.getProductFromCache(productId));
//				}
//			}
//			int time = DateConstants.SECONDS_PER_DAY;
//			memcachedService.set(groupKey, key,time , platformProductList);
//			logger.info("************************************从数据库获取商家商品中平台商品集合完成并存入缓存，platformProductList.size():"+platformProductList.size());
//		}
//		return platformProductList;
//	}
	
	/**
	 * 获取分类ID及其子集分类ID集合
	 * 
	 * @param categoryId
	 * @return
	 */
	private List<ShopCategory> getShopCategoryList(Integer categoryId) {
		List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
		String groupKey = MemcachedKey.GROUP_KEY_ShopCategoryList;
		String key = String.valueOf(categoryId);
		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
			logger.info("从缓存中获取获取分类ID及其子集分类ID集合obj："+obj); 
			shopCategoryList = (List<ShopCategory>) obj;
		}else{
			List<Long> categoryIdListFirst = new ArrayList<Long>();
			categoryIdListFirst.add((long) categoryId);
			shopCategoryList = categoryMapper.getCategoryByIds(categoryIdListFirst);
			int time = DateConstants.SECONDS_TEN_MINUTES;
			memcachedService.set(groupKey, key, time, shopCategoryList);
			logger.info("从数据库中获取分类ID及其子集分类ID集合完成并存入缓存完成,缓存时间为time："+time+",shopCategoryList.size()："+shopCategoryList.size()); 
		}
		return shopCategoryList;
	}

	/**
	 * 判断商品分类是否符合条件 true 符合、false不符合
	 * 
	 * @param shopCategoryList
	 * @param categoryIds
	 * @return
	 */
	private boolean checkCategoryId(List<ShopCategory> shopCategoryList, List<Long> categoryIds) {
		if (categoryIds == null || categoryIds.size() <= 0) {
			return false;
		}
		if (shopCategoryList == null || shopCategoryList.size() <= 0) {
			return false;
		}
		for (Long shopProductCategoryId : categoryIds) {
			if (shopProductCategoryId == null) {
				continue;
			}
			for (ShopCategory shopCategory : shopCategoryList) {
				if (shopCategory.getId() == shopProductCategoryId) {
					return true;
				}
			}
		}
		return false;
	}

	

//	/**
//	 * 获取小程序首页商品
//	 * 
//	 * @param status
//	 * @param page
//	 * @return
//	 */
//	public Map<String, Object> getHomePageProductList(Integer categoryId, Page<ShopProduct> page,
//			StoreBusiness storeBusiness) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		
//		// 1、获取该商家所有在售商品
//		long time40 = System.currentTimeMillis(); 
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeBusiness.getId());
//		long time50 = System.currentTimeMillis(); 
//		long time60 = time50 - time40;
//		logger.info("1、获取该商家所有在售商品总耗时："+time60+",categoryId:"+categoryId+",商家所有在售商品数量onShelfShopProductList.size():"+onShelfShopProductList.size()+",storeId:"+storeBusiness.getId());
//			
//		 //2、从缓存获取上架商家商品的分类列表
//		long time4 = System.currentTimeMillis(); 
//		List<ShopCategory> soldOutShopProductCategoryList = getSoldOutShopProductCategoryListByCache(storeBusiness.getId(),onShelfShopProductList);
//		long time5 = System.currentTimeMillis(); 
//		long time6 = time5 - time4;
//		logger.info(" 2、获取上架商家商品的分类列表总耗时："+time6+",categoryId:"+categoryId+",soldOutShopProductCategoryList.size():"+soldOutShopProductCategoryList.size());
//				
//				 
//		// 3、根据标签获取商家商品列表
//		 long time1 = System.currentTimeMillis(); 
//		List<ShopProduct> shopProductlist = getShopProductListByCategoryId(categoryId, page, onShelfShopProductList,storeBusiness.getVip(),storeBusiness.getId());
//		 long time2 = System.currentTimeMillis(); 
//		 long time3 = time2 - time1;
//		 logger.info("3、根据标签获取商家商品列表总耗时："+time3+",categoryId:"+categoryId+",shopProductlist.size():"+shopProductlist.size());
//		 
//		
//		// 4、组装分类数据，并排重
//		 long time7 = System.currentTimeMillis(); 
//		List<Map<String, Object>> categoryList = getShopCategoryList(soldOutShopProductCategoryList);
//		 long time8 = System.currentTimeMillis(); 
//		 long time9 = time8 - time7;
//		 logger.info("4、组装分类数据，并排重总耗时："+time9+",categoryId:"+categoryId+",categoryList.size():"+categoryList.size());
//		
//		
//		data.put("categoryList", categoryList);
//
//		if (shopProductlist.size() == 0) {
//			data.put("isNoProduct", 0);
//			data.put("isMore", page.hasNext());
//			data.put("text", "没有任何商品");
//		} else {
//			// 4、对商家商品进行置顶排序处理
//			List<Map<String, Object>> productList = sortShopProductByTop(categoryId, shopProductlist);
//			data.put("productList", productList);
//			data.put("isNoProduct", 1);
//			data.put("isMore", page.hasNext());
//		}
//
//		// logger.error("微信小程序首页返回数据:status:"+categoryId+",storeId:"+storeBusiness.getId()+",data.size():"
//		// + data.size()+",isMore"+page.hasNext());
//		return data;
//	}

	/**
	 * 从缓存获取商家商家商品的分类列表
	 * @param storeId
	 * @param onShelfShopProductList
	 * @return
	 */
	private List<ShopCategory> getSoldOutShopProductCategoryListByCache(long storeId,List<ShopProduct> onShelfShopProductList) {
		List<ShopCategory> soldOutShopProductCategoryList = new ArrayList<ShopCategory>();
		String groupKey = MemcachedKey.GROUP_KEY_shopSoldOutShopProductCategoryList;
		String key = String.valueOf(storeId);
		Object obj = memcachedService.get(groupKey, key);
		if (obj != null) {
//			logger.info("从缓存获取商家商家商品的分类列表obj："+obj); 
			soldOutShopProductCategoryList = (List<ShopCategory>) obj;
			logger.info("----从缓存获取商家商家商品的分类列表soldOutShopProductCategoryList.size："+soldOutShopProductCategoryList.size()+",onShelfShopProductList.size():"+onShelfShopProductList.size()); 
		}else{
			soldOutShopProductCategoryList = getSoldOutShopProductCategoryList(storeId,onShelfShopProductList);
			int time = DateConstants.SECONDS_PER_DAY;
			memcachedService.set(groupKey, key, time , soldOutShopProductCategoryList);
			logger.info("----从数据库获取商家商家商品的分类列表完成,缓存时间time:"+time+",soldOutShopProductCategoryList.size()："+soldOutShopProductCategoryList.size()+",onShelfShopProductList.size():"+onShelfShopProductList.size()); 
		}
		return soldOutShopProductCategoryList;
	}
	
	/**
	 * 获取上架商家商品的分类列表
	 * 
	 * @param shopProductList
	 * @return
	 */
	private List<ShopCategory> getSoldOutShopProductCategoryList(long storeId,List<ShopProduct> onShelfShopProductList) {
		List<ShopCategory> soldOutShopProductCategoryList = new ArrayList<ShopCategory>();
		//2、
		for (ShopProduct shopProduct : onShelfShopProductList) {
			List<ShopCategory> categoriesOne = new ArrayList<ShopCategory>();
			// 获取指定商品分类列表
			List<Long> categoryIds = categoryMapper.getCategoriesByProductId(shopProduct.getProductId());
			if (categoryIds.size() > 0) {// 有分类则获取分类
				//递归获取分类下的子分类
				List<ShopCategory> shopCategoryList = categoryMapper.getCategoryByIds(categoryIds);
				soldOutShopProductCategoryList.addAll(getCategories(shopCategoryList, categoriesOne));
			}
		}

		//3、按权重排序
		soldOutShopProductCategoryList.sort(new Comparator<ShopCategory>() {
			@Override
			public int compare(ShopCategory s1, ShopCategory s2) {
				int w1 = s1.getWeight();
				int w2 = s2.getWeight();
				if (w1 > w2) {
					return -1;
				}
				if (w2 > w1) {
					return 1;
				}
				return 0;
			}
		});
		// logger.info("小程序首页soldOutShopProductCategoryList:" +
		// soldOutShopProductCategoryList);
		return soldOutShopProductCategoryList;
	}

	private List<Map<String, Object>> sortShopProductByTop(Integer status, List<ShopProduct> shopProductlist) {
		List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> topProductList = new ArrayList<Map<String, Object>>();
		List<Map<String, Object>> unTopProductList = new ArrayList<Map<String, Object>>();
		for (ShopProduct shopProduct : shopProductlist) {
			// logger.error("小程序首页shopProductId:" + shopProduct.getId());
			Map<String, Object> product = new HashMap<String, Object>();
			product.put("id", shopProduct.getId() + "");
			product.put("name", shopProduct.getName());
			product.put("price", PriceUtil.getPriceString(shopProduct.getPrice()));

			// 现货时间为0表示没有现货
			if (shopProduct.getStockTime() == 0 || shopProduct.getTabType() == 1 || status == -998) {
				product.put("isStock", "0");
			} else {
				product.put("isStock", "1");
			}

			product.put("firstImage", shopProduct.getFirstImage());

			// logger.error("wxaHomePage:firstImage:" +
			// shopProduct.getFirstImage());
			product.put("sortTimeMilliSecond", shopProduct.getGroundTime());
			// 上架时间
			product.put("groundTime", shopProduct.getGroundTime());

			if (shopProduct.getTopTime() == 0) {
				topProductList.add(product);
			} else {
				unTopProductList.add(product);
			}
			// productList.add(product);
		}

		// topProductList = sortList(topProductList, "sortTimeMilliSecond");
		// unTopProductList = sortList(unTopProductList, "sortTimeMilliSecond");

		productList.addAll(unTopProductList);
		productList.addAll(topProductList);
		return productList;
	}

	private List<Map<String, Object>> getShopCategoryList(List<ShopCategory> soldOutShopProductCategoryList) {
		List<Map<String, Object>> categoryList = new ArrayList<Map<String, Object>>();

		List<Long> categoryIdList = new ArrayList<Long>();
		for (ShopCategory shopCategory : soldOutShopProductCategoryList) {
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			if (!categoryIdList.contains(shopCategory.getId())) {
				categoryMap.put("categoryId", shopCategory.getId());
				categoryMap.put("categoryName", shopCategory.getCategoryName());
				categoryIdList.add(shopCategory.getId());
				categoryList.add(categoryMap);
			}
		}
		// logger.error("小程序首页categoryList:" + categoryList);
		return categoryList;
	}

	/**
	 * 获取首页所有类别（递归）
	 * 
	 * @param shopCategoryList
	 * @param categoriesNew
	 * @return
	 */
	private List<ShopCategory> getCategories(List<ShopCategory> shopCategoryList, List<ShopCategory> categoriesNew) {
		for (ShopCategory shopCategory : shopCategoryList) {
			if (shopCategory == null || categoriesNew.contains(shopCategory)) {
				continue;
			} else if (shopCategory.getParentId() == 0) {
				categoriesNew.add(shopCategory);
			} else {
				// List<ShopCategory> childCategories =
				// categoryMapper.getCategoriesByParentId(shopCategory.getId());
				List<Long> parentId = new ArrayList<Long>();
				parentId.add(shopCategory.getParentId());
				if (parentId.size() <= 0) {
					continue;
				}
				List<ShopCategory> childCategories = categoryMapper.getCategoryByIds(parentId);
				categoriesNew = getCategories(childCategories, categoriesNew);
			}
		}
		return categoriesNew;
	}

	/**
	 * 获取父级子级所有的标签Id
	 * 
	 * @param shopCategoryList
	 * @param categoryIdListAll
	 * @return
	 */
	private List<Long> getAllCategoryIds(List<ShopCategory> shopCategoryList, List<Long> categoryIdListAll) {
		if (shopCategoryList.size() > 0) {
			for (ShopCategory shopCategory : shopCategoryList) {
				if (shopCategory == null || categoryIdListAll.contains(shopCategory.getId())) {
					continue;
				}
				categoryIdListAll.add(shopCategory.getId());
				List<ShopCategory> childCategories = categoryMapper.getCategoriesByParentId(shopCategory.getId());
				categoryIdListAll = getAllCategoryIds(childCategories, categoryIdListAll);
			}
		}
		return categoryIdListAll;
	}

	private List<Map<String, Object>> sortList(List<Map<String, Object>> list, String sortStr) {
		list.sort(new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return (long) o1.get(sortStr) > (long) o2.get(sortStr) ? -1 : 1;
			}
		});
		return list;
	}

//	/**
//	 * 获取小程序首页商品
//	 * 
//	 * @param status
//	 * @param page
//	 * @return
//	 */
//	public Map<String, Object> getHomepageList(Integer status, Page<ShopProduct> page, Long storeId) {
//		Map<String, Object> data = new HashMap<String, Object>();
//		// 1、获取该商家所有在售商品
//		List<ShopProduct> onShelfShopProductList = shopProductService.getOnShelfShopProductList(storeId);
//
//		logger.info("获取小程序首页商品列表，status：" + status + ",storeId:" + storeId);
//		// 1、根据标签获取商家商品列表
//		List<ShopProduct> shopProductlist = getShopProductListByStatus(status, page, storeId);
//
//		// 2、获取上架商家商品的分类列表
//		List<ShopCategory> soldOutShopProductCategoryList = getSoldOutShopProductCategoryListByCache(storeId,onShelfShopProductList);
//
//		// 3、组装分类数据，并排重
//		List<Map<String, Object>> categoryList = getShopCategoryList(soldOutShopProductCategoryList);
//		data.put("categoryList", categoryList);
//
//		if (shopProductlist.size() == 0) {
//			data.put("isNoProduct", 0);
//			data.put("isMore", page.hasNext());
//			data.put("text", "没有任何商品");
//		} else {
//			// 4、对商家商品进行置顶排序处理
//			List<Map<String, Object>> productList = sortShopProductByTop(status, shopProductlist);
//			data.put("productList", productList);
//			data.put("isNoProduct", 1);
//			data.put("isMore", page.hasNext());
//		}
//
//		logger.error("微信小程序首页返回数据:status:" + status + ",storeId:" + storeId + ",data.size():" + data.size() + ",isMore"
//				+ page.hasNext());
//		return data;
//	}


//	/**
//	 * //1、根据标签获取商家商品列表
//	 * 
//	 * @param status
//	 * @param page
//	 * @param storeId
//	 * @return
//	 */
//	private List<ShopProduct> getShopProductListByStatus(Integer status, Page<ShopProduct> page, Long storeId) {
//		List<ShopProduct> result;
//		logger.info("获取小程序首页商品列表，status：" + status);
//		if (status == 0) {
//			Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("sold_out", 1)
//					.eq("store_id", storeId).orderBy("ground_time", false);
//			result = shopProductService.getShopProductListInfo(page, wrapper);
//		} else if (status == -999) {// 店主精选：1.2版本之前的首页商品列表，从1.2版本之后不再使用
//			Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("tab_type", 0)
//					.eq("sold_out", 1).eq("store_id", storeId).orderBy("ground_time", false);
//			result = shopProductService.getShopProductListInfo(page, wrapper);
//		} else if (status == -998) {// 买手推荐：1.2版本之前的首页商品列表，从1.2版本之后不再使用
//			Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("tab_type", 1)
//					.eq("sold_out", 1).eq("store_id", storeId).orderBy("ground_time", false);
//			result = shopProductService.getShopProductListInfo(page, wrapper);
//		} else {
//			List<Long> productIdList = new ArrayList<Long>();
//			List<Long> categoryIdListAll = new ArrayList<Long>();
//			List<ShopCategory> shopCategoryList = getShopCategoryList(status);
//
//			categoryIdListAll = getAllCategoryIds(shopCategoryList, categoryIdListAll);
//			productIdList = shopProductMapper.getProductIdsByCategoryIds(categoryIdListAll);
//			String productIds = "";
//			for (int i = 0; i < productIdList.size(); i++) {
//				if (i == productIdList.size() - 1) {
//					productIds += (productIdList.get(i) + "");
//				} else {
//					productIds += (productIdList.get(i) + ",");
//				}
//
//			}
//			Wrapper<ShopProduct> wrapper = new EntityWrapper<ShopProduct>().eq("status", 0).eq("sold_out", 1)
//					.eq("store_id", storeId).in("product_id", productIds).orderBy("ground_time", false);
//			result = shopProductService.getShopProductListInfo(page, wrapper);
//			// shopProductMapper.selectList(wrapper);
//		}
//		// logger.info("获取小程序首页商品列表，result："+result);
//		return result;
//	}
}