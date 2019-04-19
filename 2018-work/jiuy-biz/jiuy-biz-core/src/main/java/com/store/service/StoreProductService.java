package com.store.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropName;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.BrandFilter;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.ProductShare;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.product.Subscript;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.store.dao.mapper.CategoryFilterMapper;
import com.store.dao.mapper.ProductMapper;
import com.store.dao.mapper.StoreProductMapper;
import com.store.entity.OutStockCart;
import com.store.entity.ProductPropNameValuesPair;
import com.store.entity.ProductPropVO;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.entity.StoreProduct;

@Service
public class StoreProductService {

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CategoryFilterMapper categoryFilterMapper;

    @Autowired
    private MemcachedService memcachedService;
        
    @Autowired
    private StoreProductMapper storeProductMapper;
    
    @Autowired
    private ProductSKUService productSKUService;
    
    @Autowired
    private ShopCategoryService shopCategoryService;
	
	@Autowired
	private ProductPropAssemblerShop productPropAssembler;
	
	@Autowired
	private OutStockCartService outStockCartService;

	
    public Map<Long, Product> getProducts() {
    	return productMapper.getProducts();
    }
    
    public ProductVOShop getProductById(long productId, UserDetail userDetail) {
        return getProductMap(CollectionUtil.createSet(productId), userDetail).get(productId);
    }
    
    public Product getProductById(long productId) {
        return getProductMap(CollectionUtil.createSet(productId), null).get(productId);
    }
    
    public RestrictionCombination getRestrictById(long id) {
    	return productMapper.getRestrictById(id);
    }
    
    public Subscript getSubscriptById(long id) {
    	return productMapper.getSubscriptById(id);
    }
    
    public Map<Long, RestrictionCombination> getRestrictByIdSet(Collection<Long> idSet) {
    	return productMapper.getRestrictByIdSet(idSet);
    }

    public List<ProductVOShop> getProductByIds(Collection<Long> productIds, boolean keepOrder, UserDetail userDetail) {
        List<ProductVOShop> result = new ArrayList<ProductVOShop>(productIds.size());
        Map<Long, ProductVOShop> map = getProductMap(productIds, userDetail);

        if (keepOrder) {
            for (Long productId : productIds) {
                result.add(map.get(productId));
            }
        } else {
            result.addAll(map.values());
        }
        return result;
    }
    

    public Map<Long, ProductVOShop> getProductMap(Collection<Long> productIds, UserDetail userDetail) {
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;

        Map<Long, ProductVOShop> result = new HashMap<Long, ProductVOShop>();
        List<Long> idsNotCached = new ArrayList<Long>();
        for (Long id : productIds) {
            String key = String.valueOf(id);
            Object obj = memcachedService.get(groupKey, key);
            if (obj != null) {
                result.put(id, (ProductVOShop) obj);
            } else {
                idsNotCached.add(id);
            }
        }

        if (!idsNotCached.isEmpty()) {
            Map<Long, ProductVOShop> noCachedMap = productMapper.getProductByIds(idsNotCached);
            
        	
            for (long id : noCachedMap.keySet()) {
                String key = String.valueOf(id);
                memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, noCachedMap.get(id));
            }
            result.putAll(noCachedMap);
        }

//    	incomeAssembler.assemble(result, userDetail);
        return result;
    }
    
    
    public List<Map<String, Object>> getProductList15(List<? extends Product> products) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (Product product : products) {
            productList.add(product.toSimpleMap15());
        }
        return productList;
    }

    public int getProductCountOfCategory(Collection<Long> categoryIds) {
        return productMapper.getProductCountOfCategory(categoryIds);
    }

//    public int getProductCountOfCategoryByFilter(long storeId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap,Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, int guideFlag) {
//    	return productMapper.getProductCountOfCategoryByFilter(storeId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, guideFlag, "");
//    }
//    
//    public List<ProductVO> getProductByFilter(long storeId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap, Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, SortType sortType, PageQuery pageQuery, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, int guideFlag) {
//    	return productMapper.getProductByFilter(storeId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale , guideFlag, "");
//    }

    public List<Product> getProductOfCategory(Collection<Long> categoryIds, SortType sortType, PageQuery pageQuery) {
        return productMapper.getProductOfCategory(categoryIds, sortType, pageQuery);
    }
    
    public List<CategoryFilter> getProductFilterByCatId(long id) {
    	return categoryFilterMapper.getProductFilterByCatId(id);
    }
    
    public List<CategoryFilter> getProductFilterByCatIds(Collection<Long> ids) {
    	return categoryFilterMapper.getProductFilterByCatIds(ids);
    }
    public List<BrandFilter> getProductFilterByBrandIds(Collection<Long> ids) {
    	return categoryFilterMapper.getProductFilterByBrandIds(ids);
    }
    

    public int getProductCountOfBrand(Long brandId) {
        return productMapper.getProductCountOfBrand(brandId);
    }
	
    public List<Product> getProductOfBrand(Long brandId, SortType sortType, PageQuery pageQuery) {
        return productMapper.getProductOfBrand(brandId, sortType, pageQuery);
    }
    
    public int getProductCountOfCategoryProperty(Collection<Long> categoryIds, Collection<Long> propertyValueIds) {
        return productMapper.getProductCountOfCategoryProperty(categoryIds, propertyValueIds);
    }

    public List<Product> getProductOfCategoryProperty(Collection<Long> categoryIds, Collection<Long> propertyValueIds,
                                                      SortType sortType, PageQuery pageQuery) {
        return productMapper.getProductOfCategoryProperty(categoryIds, propertyValueIds, sortType, pageQuery);
    }
    
    public List<ProductVOShop> getUserBestSellerProductList186(UserDetail userDetail, PageQuery pageQuery, int guideFlag) {
    	List<ProductVOShop> list = productMapper.getUserBestSellerProductList186(userDetail.getId(), pageQuery , guideFlag);
//    	incomeAssembler.assemble(list, userDetail);
    	return list;
    }
    public List<ProductVOShop> getUserBestSellerProductList186(UserDetail userDetail, PageQuery pageQuery) {
    	
    	return getUserBestSellerProductList186(userDetail, pageQuery , 0);
    }
    
    public List<ProductVOShop> getUserBuyGuessProduct186(UserDetail userDetail, PageQuery pageQuery) {
    	List<ProductVOShop> list = productMapper.getUserBuyGuessProduct186(userDetail.getId(), pageQuery);
//    	incomeAssembler.assemble(list, userDetail);
    	return list;
    }
//    
//    public List<Product> getBestSellerProductList186(PageQuery pageQuery) {
//    	return productMapper.getBestSellerProductList186( pageQuery);
//    }
//    
//    public List<Product> getBuyGuessProduct186(PageQuery pageQuery) {
//    	return productMapper.getBuyGuessProduct186( pageQuery);
//    }

    public int getBestSellerProductCount() {
        return productMapper.getBestSellerProductCount();
    }

    public int updateSaleCount(long productId, int by) {
        return productMapper.updateSaleCount(productId, by);
    }

    public List<Product> getProductBySaleTime(Collection<Long> categoryIds, long startTimeBegin, long startTimeEnd,
                                              SortType sortType) {
        return productMapper.getProductBySaleTime(categoryIds, startTimeBegin, startTimeEnd, sortType);
    }

	public List<Long> getBrandIds(Collection<Long> productIds) {
		return productMapper.getBrandIds(productIds);
	}

	//够买此商品的用户也同时购买了
	public List<Product> getBuyAlsoProduct(Collection<Long> productIds, PageQuery pageQuery, int count) {
		return productMapper.getBuyAlsoProduct(productIds, pageQuery, count);
	}
	
	public List<ProductVOShop> getProductListByIds(Collection<Long> productIds) {
		return productMapper.getProductListByIds(productIds);
	}
	public List<ProductVOShop> getProductMapByOrderNo(String orderNo) {
		return productMapper.getProductMapByOrderNo(orderNo);
	}
	
	//获取门店的相应商品ID的订单购买记录
	public List<RestrictProductVO> getBuyerLogByStoreOrder(long userId, Collection<Long> productIds) {
		return productMapper.getBuyerLogByStoreOrder(userId, productIds);
	}
	public int getZeroBuyerLog(long userId) {
		long time = System.currentTimeMillis();
		return productMapper.getZeroBuyerLog(userId, DateUtil.getDayZeroTime(time));
	}
	
	public int getZeroBuyerMonthly(long userId) {
		long endTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        long startTime = calendar.getTimeInMillis();
		return productMapper.getZeroBuyerMonthly(userId, startTime ,endTime);
	}
	
	public int getUserRestrictBuy(long userId, long restrictId, int days) {
		 long startTime = UtilDate.getPlanDayFromDate(new Date(), 0 - days).getTime();
		 long endTime = System.currentTimeMillis();
		return productMapper.getUserRestrictBuy(userId,restrictId, startTime ,endTime);
	}
	
	//获取用户购买参加的组合限购中各商品Id对应的购买数量
	public int getUserRestrictBuyByStoreOrder(long userId, long restrictId, long startTime,long endTime) {
		return productMapper.getUserRestrictBuyByStoreOrder(userId,restrictId, startTime ,endTime);
	}

	public List<Product> getProductOfWarehouse(Long loWarehouseId, SortType sortType, PageQuery pageQuery) {
		return productMapper.getProductOfWarehouse(loWarehouseId, sortType, pageQuery);
	}

	public int getProductCountOfWarehouse(Long loWarehouseId) {
		return productMapper.getProductCountOfWarehouse(loWarehouseId);
	}

	public Product getProductBySkuNo(long skuNo) {
		// TODO Auto-generated method stub
		return productMapper.getProductBySkuNo(skuNo);
	}

	public Map<String, Object> getSkusInfo(long productId, UserDetail userDetail) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<>();
		Product product = getProductById(productId, userDetail);
		List<StoreProduct> storeProducts = storeProductMapper.getByStoreIdProductId(userDetail.getId(), product.getId());

		result.put("count", calculateCount(storeProducts));
		result.put("name", product.getName());
		result.put("clothes_num", product.getClothesNumber());
		result.put("price", product.getCash());
		result.put("limit_sku_count", listLimitSkuCount(userDetail.getId(), storeProducts));
		result.put("image", product.getImage());
		
		Set<Long> skuIds = gatherSkuIds(storeProducts);
		List<ProductSKU> skus = productSKUService.getProductSKUs(skuIds);
		Map<String, ProductSKU> skuMap = new HashMap<String, ProductSKU>();
        List<ProductPropVO> productPropVOs = new ArrayList<ProductPropVO>();
        for (ProductSKU sku : skus) {
            skuMap.put(sku.getPropertyIds(), sku);
            productPropVOs.addAll(sku.getProductProps());
        }
        
        productPropAssembler.assemble(productPropVOs);

        List<ProductPropNameValuesPair> skuProps = new ArrayList<ProductPropNameValuesPair>();
        Map<Long, ProductPropNameValuesPair> skuPropMap = new HashMap<Long, ProductPropNameValuesPair>();
        for (ProductPropVO propVO : productPropVOs) {
            ProductPropName propName = propVO.getPropName();
            ProductPropNameValuesPair skuProp = skuPropMap.get(propName.getId());
            if (skuProp == null) {
                skuProp = new ProductPropNameValuesPair(propName);
                skuPropMap.put(propName.getId(), skuProp);
                skuProps.add(skuProp);
            }
            skuProp.add(propVO.getPropValue());
        }
        result.put("skuProps", skuProps);
		
		return result;
	}


	private List<Map<String, Object>> listLimitSkuCount(long storeId, List<StoreProduct> storeProducts) {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list = new ArrayList<>();
		for (StoreProduct storeProduct : storeProducts) {
			Map<String, Object> map = new HashMap<>();
			ProductSKU productSKU = productSKUService.getProductSKU(storeProduct.getSkuId());
			map.put("property_ids", productSKU.getPropertyIds());
			
			int cart_count = 0;
			List<OutStockCart> carts = outStockCartService.getByStoreIdSkuId(storeId, storeProduct.getSkuId());
			if (carts.size() > 0) {
				for (OutStockCart outStockCart : carts) {
					cart_count += outStockCart.getCount();
				}
			}
			
			map.put("remain_count", storeProduct.getOnSaleCount() - cart_count);
			
			list.add(map);
		}
		
		return list;
	}

	private Set<Long> gatherSkuIds(List<StoreProduct> storeProducts) {
		// TODO Auto-generated method stub
		Set<Long> skuIds = new HashSet<>();
		for (StoreProduct storeProduct : storeProducts) {
			skuIds.add(storeProduct.getSkuId());
		}
		return skuIds;
	}

	private int calculateCount(List<StoreProduct> storeProducts) {
		// TODO Auto-generated method stub
		int count = 0;
		for (StoreProduct storeProduct : storeProducts) {
			count += (storeProduct.getOnSaleCount() + storeProduct.getOffSaleCount());
		}
		return count;
	}

	/**
	 * @param brandList
	 * @return
	 */
	public Map<Long, List<ProductVOShop>> getProductGroupsByBrandIds(Collection<Long> brandIds, UserDetail userDetail, int guideFlag, int type) {
		// TODO Auto-generated method stub
		
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;
        String key = "brandgroup";
//        Object obj = null;//memcachedService.get(groupKey, key);
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (Map<Long, List<ProductVOShop>>) obj;
        }
        
        Collection<Long> categoryIds = getMenuCategoryIdList();
    	
		
		Map<Long, List<ProductVOShop>> productMapBrandGroup = new HashMap<Long, List<ProductVOShop>>();
		
		List<ProductVOShop> products = productMapper.getProductByBrandIds(brandIds, type, guideFlag, categoryIds);
		for (ProductVOShop productVO : products) {
			List<ProductVOShop> brandProducts = productMapBrandGroup.get(productVO.getBrandId());
			if (brandProducts == null) {
				brandProducts = new ArrayList<ProductVOShop>();
				productMapBrandGroup.put(productVO.getBrandId(), brandProducts);
			}
			/*
			 * 取推荐指数最大的三个商品 todo
			 */
//			if (brandProducts.size() > 2) continue;
			if(guideFlag == 0 && (productVO.getType() == 1 || productVO.getType() == 2)){  //lingshou
				brandProducts.add(productVO);
				
			}else if(guideFlag == 1 && (productVO.getType() == 0 || productVO.getType() == 1)){    //批发
				
				brandProducts.add(productVO);
			}else {
				
				brandProducts.add(productVO);
			}
		}
		
		memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, productMapBrandGroup);
		return productMapBrandGroup;
	}
	
	
	public String getStoreAvailableBrandStr(long storeId) {
		return productMapper.getStoreAvailableBrandStr(storeId);
	}
	public  Collection<Long> getMenuCategoryIdList() {
		Collection<Long> categoryIds = new ArrayList<Long>();
		List<ShopCategory> categorieListAll = shopCategoryService.getCategories();
        List<ShopCategory> categoryList =  shopCategoryService.getParentCategories();
		
        
      //添加子分类到列表
    	for(ShopCategory category : categoryList){
    		for(ShopCategory categoryTemp : categorieListAll){
    			if(categoryTemp.getParentId() == category.getId()){
    				category.getChildCategories().add(categoryTemp);
    			}
    		}
    	}
    	
    	for(ShopCategory categoryTemp: categoryList){
			categoryIds.addAll(categoryTemp.getCategoryIds());
			
		}
    	return categoryIds;
	}

	public Product getProduct(long id) {
		// TODO Auto-generated method stub
		return productMapper.getProduct(id);
	}
	
	
	
	public ProductShare getProductShareByProId(long productId) {
	    	return productMapper.getProductShareByProId(productId);
	}

	public StoreProduct getByStoreIdSkuId(long storeId, long skuId) {
		// TODO Auto-generated method stub
		return storeProductMapper.getByStoreIdSkuId(storeId, skuId);
	}
	
	/**
	 * 获取用户所有下单成功并且参加同一限购活动的商品Id
	 * @param id
	 * @param restrictIds
	 * @return
	 */
	public List<Long> getAllProductIdsByUser(long id, List<Long> restrictIds) {
		return productMapper.getAllProductIdsByUser(id,restrictIds);
	}

	/**
	 * 获取用户对应订单
	 * @param id
	 * @param restrictDayTime
	 * @param restrictionCombinationId 
	 * @return
	 */
	public int getProductCountsByRestrictId(long id, long restrictDayTime, Long restrictionCombinationId) {
		return productMapper.getProductCountsByRestrictId(id, restrictDayTime,restrictionCombinationId);
	}
	
}
