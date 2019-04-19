package com.store.service;

import java.util.*;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.util.BizUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.product.SortType;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.BrandFilter;
import com.jiuyuan.entity.product.CategoryFilter;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.store.dao.mapper.CategoryFilterMapper;
import com.store.dao.mapper.CategoryMapper;
import com.store.dao.mapper.ProductMapper;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import org.springframework.util.ObjectUtils;


@Service
public class ProductServiceShop {
	private static final Logger logger = LoggerFactory.getLogger(ProductServiceShop.class);
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private CategoryFilterMapper categoryFilterMapper;

	@Autowired
	private CategoryMapper categoryMapper;


	@Autowired
	private ProductNewMapper productNewMapper;
    
    public ProductVOShop getProductById(long productId) {
        return getProductMap(CollectionUtil.createSet(productId)).get(productId);
    }
    
    public Product getProduct(long productId) {
		return productMapper.getProduct(productId);
	}
    /**
     * 获取平台商品从缓存中
     * 注意缓存时间10分钟
     * @param productId
     * @return
     */
    public Product getProductFromCache(long productId) {
//   	 long time4 = System.currentTimeMillis(); 
    	
//    	logger.info("获取平台商品从缓存中getProductFromCache："+productId); 
    	Product product = null;
    	String groupKey = MemcachedKey.GROUP_KEY_pingtai_product;
		String key = String.valueOf(productId);
//		logger.info("开始从数据库中获取平台商品,key:"+key+",groupKey："+groupKey); 
		Object obj = memcachedService.get(groupKey, key);
//		logger.info("============obj："+obj+"key:"+key+",groupKey："+groupKey);
		if (obj != null) {
//			logger.info("从缓存中获取平台商品obj："+obj); 
			product = (Product) obj;
//			logger.info("从缓存中获取平台商品product："+product); 
		}else{
//			logger.info("开始从数据库中获取平台商品"); 
			product = productMapper.getProduct(productId);
			memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_DAY, product);
//			logger.info("从数据库中获取平台商品完成并存入缓存完成product："+product+"key:"+key+",groupKey："+groupKey+",obj:"+obj); 
		}
//		logger.info("平台商品product："+product); 
		
//		long time5 = System.currentTimeMillis(); 
//		 long time6 = time5 - time4;
//		 logger.info("获取平台商品，time6："+time6);
		return product;
	}
	
	
	//获取用户购买参加的组合限购中各商品Id对应的购买数量
	public int getUserRestrictBuyByStoreOrder(long userId, long restrictId, long startTime,long endTime) {
		return productMapper.getUserRestrictBuyByStoreOrder(userId,restrictId, startTime ,endTime);
	}
    
	
	//获取门店的相应商品ID的订单购买记录
	public List<RestrictProductVO> getBuyerLogByStoreOrder(long userId, Collection<Long> productIds) {
		return productMapper.getBuyerLogByStoreOrder(userId, productIds);
	}
	
	public Map<Long, ProductVOShop> getProductMap(Collection<Long> productIds ) {
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

	public Map<Long, RestrictionCombination> getRestrictByIdSet(Collection<Long> idSet) {
    	return productMapper.getRestrictByIdSet(idSet);
    }
    
    public List<CategoryFilter> getProductFilterByCatId(long id) {
    	return categoryFilterMapper.getProductFilterByCatId(id);
    }

	public int getUserRestrictBuy(long userId, long restrictId, int days) {
		 long startTime = UtilDate.getPlanDayFromDate(new Date(), 0 - days).getTime();
		 long endTime = System.currentTimeMillis();
		return productMapper.getUserRestrictBuy(userId,restrictId, startTime ,endTime);
	}
	
   public List<CategoryFilter> getProductFilterByCatIds(Collection<Long> ids) {
    	return categoryFilterMapper.getProductFilterByCatIds(ids);
    }
   
   public int getProductCountOfCategoryByFilter(int vip,long storeId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap,Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, int guideFlag, String keyWords,int brandType) {
	   return productMapper.getProductCountOfCategoryByFilter(vip,storeId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, guideFlag, keyWords,brandType);
   }
   
   public List<ProductVOShop> getProductByFilter(int vip,long storeId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap, Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, SortType sortType, PageQuery pageQuery, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, int guideFlag, String keyWords,int brandType) {
	   return productMapper.getProductByFilter(vip,storeId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale , guideFlag, keyWords,brandType);
   }

   public List<ProductVOShop> getProductMapByOrderNo(String orderNo) {
	   return productMapper.getProductMapByOrderNo(orderNo);
   }
   
   public List<Long> getBrandIds(Collection<Long> productIds) {
		return productMapper.getBrandIds(productIds);
	}
	
   public List<BrandFilter> getProductFilterByBrandIds(Collection<Long> ids) {
    	return categoryFilterMapper.getProductFilterByBrandIds(ids);
    }
   public List<ProductVOShop> getUserBestSellerProductList186(UserDetail userDetail, PageQuery pageQuery, int guideFlag) {
	  long storeId = userDetail.getId();
   	List<ProductVOShop> list = productMapper.getUserBestSellerProductList186(storeId, pageQuery , guideFlag);
   	//incomeAssembler.assemble(list, userDetail);
   	return list;
   }
	
   public int getBestSellerProductCount() {
       return productMapper.getBestSellerProductCount();
   }

    public List<ProductVOShop> getProductByIds(Collection<Long> productIds, boolean keepOrder, UserDetail userDetail) {
        List<ProductVOShop> result = new ArrayList<ProductVOShop>(productIds.size());
        Map<Long, ProductVOShop> map = getProductMap(productIds);

        if (keepOrder) {
            for (Long productId : productIds) {
                result.add(map.get(productId));
            }
        } else {
            result.addAll(map.values());
        }
        return result;
    }
	
    public Map<Long, Product> getProducts() {
    	return productMapper.getProducts();
    }
    
	public String getStoreAvailableBrandStr(long storeId) {
		return productMapper.getStoreAvailableBrandStr(storeId);
	}

	public int updateSaleCount(long productId, int by) {
        return productMapper.updateSaleCount(productId, by);
    }

	public List<ProductVOShop> getTagProducts(long tagId, long storeId, PageQuery pageQuery) {
		return productMapper.getTagProducts(tagId, storeId, pageQuery);
	}

	public int getTagProductsCount(long tagId, long storeId) {
		return productMapper.getTagProductsCount(tagId, storeId);
	}

	/**
	 *
	 * @return
	 */
	public Map<Long, List<ProductVOShop>> getProductGroupsByBrandIds(Collection<Long> brandIds, UserDetail userDetail, int type) {
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;
        String key = "brandgroup" + type;
        if(type==2 || type==3){
        	key += userDetail.getId();
        }
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (Map<Long, List<ProductVOShop>>) obj;
        }

		Map<Long, List<ProductVOShop>> productMapBrandGroup = new HashMap<>();
//		List<ProductVOShop> old = productMapper.getProductByBrandIds(brandIds, type,-1, null); 不分商品审核和上下架状态
		List<ProductVOShop> products = productMapper.getProductByBrands(brandIds, type,-1, null, Arrays.asList(6));
		for (ProductVOShop productVO : products) {
			List<ProductVOShop> brandProducts = productMapBrandGroup.get(productVO.getBrandId());
			if (brandProducts == null) {
				brandProducts = new ArrayList<>();
				productMapBrandGroup.put(productVO.getBrandId(), brandProducts);
			}
			/*
			 * 取推荐指数最大的三个商品 todo
			 */
//			if (brandProducts.size() > 2) continue;
			if( productVO.getType() == 0 || productVO.getType() == 1){    //批发
				
				brandProducts.add(productVO);
			}
		}
		
		memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, productMapBrandGroup);
		return productMapBrandGroup;
	}
	
	public  Collection<Long> getMenuCategoryIdList() {
		Collection<Long> categoryIds = new ArrayList<Long>();
		List<ShopCategory> categorieListAll = categoryMapper.getCategories();
        List<ShopCategory> categoryList =  categoryMapper.getParentCategories();
		
        
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

	public List<ProductVOShop> getProductByFilterAndTagIds(int vip,long storeId, 
			long brandId, Collection<Long> tagIds, 
			Map<String, String[]> filterMap, Map<String, String[]> tagFilterMap, 
			Map<String, String[]> colorSizeMap, SortType sortType, PageQuery pageQuery, double minPrice, double maxPrice, Boolean inStock, 
			Boolean onSale, int guideFlag, String keyWords,int brandType) {
		return productMapper.getProductByFilterAndTagIds(vip,storeId, brandId, tagIds, filterMap, tagFilterMap, 
				colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale , guideFlag, keyWords,brandType);
	}

	public int getProductCountOfCategoryByFilterAndTagIds(int vip,long storeId, 
			long brandId, Collection<Long> tagIds, 
			Map<String, String[]> filterMap,Map<String, String[]> tagFilterMap, 
			Map<String, String[]> colorSizeMap, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, int guideFlag, 
			String keyWords,int brandType) {
		return productMapper.getProductCountOfCategoryByFilterAndTagIds(vip,storeId, brandId, tagIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, 
				inStock, onSale, guideFlag, keyWords,brandType);
	}

	/**
	 * 每日上新接口
	 * @param
	 * @date:   2018/4/24 9:25
	 * @author: Aison
	 */
	public Page<ProductNew> newProducts(Page<ProductNew> productNewPage,Long firstQueryTime) {
		Condition condition = Condition.create();

		condition.between("last_puton_time",BizUtil.addDay(new Date(),-30).getTime(), System.currentTimeMillis());
		condition.andNew("BrandId <> 2521").eq("state",6);
		condition.orderBy("last_puton_time",false);
		if(firstQueryTime!=null) {
			condition.and("last_puton_time <='"+firstQueryTime+"'");
		}
		productNewPage.setRecords(productNewMapper.selectPage(productNewPage,condition));
		return productNewPage;
	}


	/**
	 * 查询查询合格商品Id (商品上架, 并且库存量>0)
	 *
	 * @param productIdSet
	 * @return java.util.Set<java.lang.Long>
	 * @auther Charlie(唐静)
	 * @date 2018/6/8 15:49
	 */
	public List<Long> checkProduct(Collection<Long> productIdSet) {
		return productMapper.checkProduct(productIdSet);
	}

	/**
	 * 商品列表
	 *
	 * @param pageQuery pageQuery
	 * @param queryCondition 查询条件
	 * @param notInIds 不在的id
	 * @return java.util.List<com.jiuyuan.entity.newentity.ProductNew>
	 * @author Charlie
	 * @date 2018/9/7 15:41
	 */
	public List<ProductNew> productList(Page<Map<String, Object>> pageQuery, ProductNew queryCondition, List<Long> notInIds) {
		Wrapper<ProductNew> filter = new EntityWrapper<> ();
		filter.eq ("Status", 0);
		filter.eq ("state", 6);
		filter.eq ("delState", 0);
		if (queryCondition.getMemberLevel () != null) {
			filter.le ("memberLevel", queryCondition.getMemberLevel ());
		}

		boolean isParallel = false;
		if (StringUtils.isNotBlank (queryCondition.getName ())) {
			filter.and ().like ("Name", queryCondition.getName ());
			isParallel = true;
		}
		if (StringUtils.isNotBlank (queryCondition.getClothesNumber ())) {
			if (isParallel) {
				filter.or ("ClothesNumber LIKE CONCAT('%','"+queryCondition.getClothesNumber ()+"','%')");
			}
			else {
				filter.and ().like ("ClothesNumber", queryCondition.getClothesNumber ());
			}
		}

		//用户已有的供应商同款商品,不展示(先这样,后期应该改成关联查询,notIn不走索引)
		if (!ObjectUtils.isEmpty (notInIds)) {
			filter.and ().notIn ("Id", notInIds);
		}

		filter.orderBy ("upSoldTime", false);
		return productNewMapper.selectPage (pageQuery, filter);
	}
}