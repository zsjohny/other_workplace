package com.yujj.business.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
import com.jiuyuan.entity.log.Log;
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
import com.yujj.dao.mapper.CategoryFilterMapper;
import com.yujj.dao.mapper.ProductMapper;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductVO;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;
    
    @Autowired
    private CategoryFilterMapper categoryFilterMapper;
    
    @Autowired
    private LogService logService;
    
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MemcachedService memcachedService;


    public Map<Long, Product> getProducts() {
    	return productMapper.getProducts();
    }
    
    public Product getProductById(long productId) {
        return getProductMap(CollectionUtil.createSet(productId)).get(productId);
    }
     
    public ProductShare getProductShareByProId(long productId) {
    	return productMapper.getProductShareByProId(productId);
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

    public List<Product> getProductByIds(Collection<Long> productIds, boolean keepOrder) {
        List<Product> result = new ArrayList<Product>(productIds.size());
        Map<Long, Product> map = getProductMap(productIds);

        if (keepOrder) {
            for (Long productId : productIds) {
                result.add(map.get(productId));
            }
        } else {
            result.addAll(map.values());
        }
        return result;
    }
    

    public Map<Long, Product> getProductMap(Collection<Long> productIds) {
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;

        Map<Long, Product> result = new HashMap<Long, Product>();
        List<Long> idsNotCached = new ArrayList<Long>();
        for (Long id : productIds) {
            String key = String.valueOf(id) + Product.appVersion;
            Object obj = memcachedService.get(groupKey, key);
            if (obj != null) {
                result.put(id, (Product) obj);
            } else {
                idsNotCached.add(id);
            }
        }

        if (!idsNotCached.isEmpty()) {
            Map<Long, Product> noCachedMap = productMapper.getProductByIds(idsNotCached);

            for (long id : noCachedMap.keySet()) {
                String key = String.valueOf(id);
                memcachedService.set(groupKey, key, DateConstants.SECONDS_PER_MINUTE, noCachedMap.get(id) + Product.appVersion);
            }
            result.putAll(noCachedMap);
        }

        return result;
    }
    
    
    public List<Map<String, Object>> getProductList15(List<Product> products) {
        List<Map<String, Object>> productList = new ArrayList<Map<String, Object>>();
        for (Product product : products) {
            productList.add(product.toSimpleMap15());
        }
        return productList;
    }

    public int getProductCountOfCategory(Collection<Long> categoryIds) {
        return productMapper.getProductCountOfCategory(categoryIds);
    }

    public List<Product> getProductOfCategory(Collection<Long> categoryIds, SortType sortType, PageQuery pageQuery) {
        return productMapper.getProductOfCategory(categoryIds, sortType, pageQuery);
    }
    
    public List<CategoryFilter> getProductFilterByCatId(long id) {
    	return categoryFilterMapper.getProductFilterByCatId(id);
    }
    
    public List<CategoryFilter> getProductFilterByCatIds(Collection<Long> ids) {
    	return categoryFilterMapper.getProductFilterByCatIds(ids);
    }
    
    public int getProductCountOfCategoryByFilter(Collection<Long> categoryIds, Map<String, String[]> filterMap,Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, Boolean deductFlag) {
    	return productMapper.getProductCountOfCategoryByFilter(0,categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, deductFlag);
    }
    
    public List<Product> getProductByFilter(Collection<Long> categoryIds, Map<String, String[]> filterMap, Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, SortType sortType, PageQuery pageQuery, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, Boolean deductFlag) {
    	return productMapper.getProductByFilter(0,categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale, deductFlag);
    }
   
    
    public int getProductCountOfCategoryByFilter(long userId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap,Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, double minPrice, double maxPrice, Boolean inStock, Boolean onSale, Boolean deductFlag) {
    	return productMapper.getProductCountOfCategoryByFilter(userId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, minPrice, maxPrice, inStock, onSale, deductFlag);
    }
    
    public List<ProductVO> getProductByFilter(long userId, long brandId, Collection<Long> categoryIds, Map<String, String[]> filterMap, Map<String, String[]> tagFilterMap, Map<String, String[]> colorSizeMap, SortType sortType, PageQuery pageQuery, double minPrice, double maxPrice, Boolean inStock, Boolean onSale,Boolean deductFlag) {
    	return productMapper.getProductByFilter(userId, brandId, categoryIds, filterMap, tagFilterMap, colorSizeMap, sortType, pageQuery, minPrice, maxPrice, inStock, onSale,deductFlag);
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

    public List<Product> getBestSellerProductList(PageQuery pageQuery) {
        return productMapper.getBestSellerProductList(pageQuery);
    }
    
    public List<Product> getUserBestSellerProductList186(long userId, PageQuery pageQuery) {
    	return productMapper.getUserBestSellerProductList186( userId, pageQuery);
    }
    
    public List<Product> getSeeAgainProduct210(long productId, PageQuery pageQuery) {
    	

		int count = pageQuery.getPageSize();
		if (count <= 0 || count >=4) count = 4;
		
    	List<Log> productLogList = logService.getProductLogs(productId);
    	
    	Map<String , Integer> countMap	= new HashMap<>();
    	//搜索该商品最近的500条浏览记录，统计每条记录前后各5款商品，将同款商品的浏览次数累加，取累加次数最高的4*4款商品（不含本身），次数相同的商品按推荐指数》销量》点击量排序.推荐时从4*4中随机取4件进行推荐
    	if (productLogList != null && productLogList.size() >= 50) {
    		//根据log统计商品
    		List<Log> allProductLogList = logService.getAllProductLogs(0);
    		for(int i = 0; i < productLogList.size(); i++){
    			for(int j = 0; j < allProductLogList.size(); j++){
        			if(productLogList.get(i).getLogId() == allProductLogList.get(j).getLogId()){
        				for(int x = j-5; x <= j+5; x++){
        					if(x >= 0 && x < j && !allProductLogList.get(x).getSrcRelatedId().equals(productLogList.get(i).getSrcRelatedId())){
        						if(countMap.get(allProductLogList.get(x).getSrcRelatedId()) == null){
        							countMap.put(allProductLogList.get(x).getSrcRelatedId(), 1);
        						}else{
        							countMap.put(allProductLogList.get(x).getSrcRelatedId(), countMap.get(allProductLogList.get(x).getSrcRelatedId()) + 1);
        						}
        					}
        				}
        				
        			}
        		}
    		}
//    		if(countMap.size() < 4){
//    			return
//    			
//    		}
    		//对map进行排序，取累加最高的16个？
		List<Map.Entry<String,Integer>> mappingList = null; 
		//      	  Map<String,String> map = new HashMap<String,String>(); 
		//      	  map.put("month", "3"); 
		//      	  map.put("bread", "2"); 
		//      	  map.put("attack", "1"); 
		  
		  //通过ArrayList构造函数把map.entrySet()转换成list 
		mappingList = new ArrayList<Map.Entry<String,Integer>>(countMap.entrySet()); 
		//      	  for(Map.Entry<String,Integer> mapping : mappingList){ 
		//         	   System.out.println(mapping.getKey()+":"+mapping.getValue()); 
		//         	  } 
		  //通过比较器实现比较排序 
		Collections.sort(mappingList, new Comparator<Map.Entry<String,Integer>>(){
		public int compare(Map.Entry<String,Integer> mapping1,Map.Entry<String,Integer> mapping2){ 
		      return new Integer(mapping2.getValue()).compareTo(new Integer(mapping1.getValue())); 
		 } 
	    }); 
		  
		Set<Long> idSet = new HashSet<>();
		  
	    if(mappingList.size() <= 4){
			  for(Map.Entry<String,Integer> entry : mappingList){
				  idSet.add(Long.parseLong(entry.getKey()));
				  
			  }
		}
		int maxLength = 16;
		if(mappingList.size() < 16){
			maxLength = mappingList.size();
		}
//		for(Map.Entry<String,Integer> entry : mappingList ){
//			System.out.println(entry.getKey()+"@@"+ entry.getValue());
//		}
      	for (int i = 0; i < count; i++) {  
      	  
            // 取出一个随机数  
            int r = (int) (Math.random() * maxLength);  
  
            idSet.add(Long.parseLong(mappingList.get(r).getKey()));  
  
            // 排除已经取过的值  
            mappingList.remove(r);  
            maxLength--;
        } 
      	List<Product> productList = productMapper.getProductListByCollection(idSet);
      	  
      	return productList;
			
		} else{
			//浏览次数不足50条的商品，默认推荐推荐指数最高的商品4件、15天内销量最高的商品4件、15天内收藏量最高的商品4件、15天内浏览量最高的商品4件，推荐时从4*4中随机取4件进行推荐
			int limit = 4;
			long fifteenDaysBefore =UtilDate.getPlanDayFromDate(new Date(), -15).getTime();
			List<Product> mostRecommendList = productMapper.getProductListMostRecommendList(limit);
			List<Product> bestSellList = productMapper.getProductListBestSell(limit, fifteenDaysBefore);
			List<Product> mostCollectList = productMapper.getProductListMostCollect(limit, fifteenDaysBefore);
			List<Product> mostViewList = productMapper.getProductListMostView(limit, fifteenDaysBefore);
			int existFlag;
			for(Product productTemp : bestSellList){
				existFlag = 0;
				for(Product productAll : mostRecommendList){
					if(productAll.getId() == productTemp.getId()){
						existFlag = 1;
						break;
					}
				}
				if(existFlag == 0){
					mostRecommendList.add(productTemp);
				}
			}
			for(Product productTemp : mostCollectList){
				existFlag = 0;
				for(Product productAll : mostRecommendList){
					if(productAll.getId() == productTemp.getId()){
						existFlag = 1;
						break;
					}
				}
				if(existFlag == 0){
					mostRecommendList.add(productTemp);
				}
			}
			for(Product productTemp : mostViewList){
				existFlag = 0;
				for(Product productAll : mostRecommendList){
					if(productAll.getId() == productTemp.getId()){
						existFlag = 1;
						break;
					}
				}
				if(existFlag == 0){
					mostRecommendList.add(productTemp);
				}
			}
			
			int maxLength = mostRecommendList.size();
			
			//支持1-4的商品数
//			if(maxLength <= 4){
//				return mostRecommendList;
//			}
			List<Product> resultList = new ArrayList<>();
			
			for (int i = 0; i < count; i++) {  
		      	  
	            // 取出一个随机数  
	            int r = (int) (Math.random() * maxLength);  
	  
	            resultList.add(mostRecommendList.get(r));
	  
	            // 排除已经取过的值  
	            mostRecommendList.remove(r);  
	            maxLength--;
	        }
			return resultList;
			
		}
    	
    	
    	
    }
    
    public List<Product> getUserBuyGuessProduct186(long userId, PageQuery pageQuery) {
    	return productMapper.getUserBuyGuessProduct186( userId, pageQuery);
    }
//    
//    public List<Product> getBestSellerProductList186(PageQuery pageQuery) {
//    	return productMapper.getBestSellerProductList186( pageQuery);
//    }
//    
//    public List<Product> getBuyGuessProduct186(PageQuery pageQuery) {
//    	return productMapper.getBuyGuessProduct186( pageQuery);
//    }
    
    public List<Product> getBestSellerProductList185(PageQuery pageQuery, SortType sortType) {
        return productMapper.getBestSellerProductList185(pageQuery, sortType);
    }

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

	public List<RestrictProductVO> getBuyerLog(long userId, Collection<Long> productIds) {
		//删除旧表
//		return productMapper.getBuyerLog(userId, productIds);
		return productMapper.getBuyerLogNew(userId, productIds);
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

	public List<Product> getProductOfWarehouse(Long loWarehouseId, SortType sortType, PageQuery pageQuery) {
		return productMapper.getProductOfWarehouse(loWarehouseId, sortType, pageQuery);
	}

	public int getProductCountOfWarehouse(Long loWarehouseId) {
		return productMapper.getProductCountOfWarehouse(loWarehouseId);
	}
	public List<BrandFilter> getProductFilterByBrandIds(Collection<Long> ids) {
	    return categoryFilterMapper.getProductFilterByBrandIds(ids);
	}
	
	public String getStoreAvailableBrandStr(long storeId) {
		return productMapper.getStoreAvailableBrandStr(storeId);
	}
	

	/**
	 * @param brandList
	 * @return
	 */
	public Map<Long, List<ProductVO>> getProductGroupsByBrandIds(Collection<Long> brandIds, UserDetail userDetail, int type) {
        String groupKey = MemcachedKey.GROUP_KEY_PRODUCT;
        String key = "brandgroup";
//        Object obj = null;//memcachedService.get(groupKey, key);
        Object obj = memcachedService.get(groupKey, key);
        if (obj != null) {
            return (Map<Long, List<ProductVO>>) obj;
        }
        
        Collection<Long> categoryIds = getMenuCategoryIdList();
    	
		
		Map<Long, List<ProductVO>> productMapBrandGroup = new HashMap<Long, List<ProductVO>>();
		
		List<ProductVO> products = productMapper.getProductByBrandIds(brandIds, type, categoryIds);
		for (ProductVO productVO : products) {
			List<ProductVO> brandProducts = productMapBrandGroup.get(productVO.getBrandId());
			if (brandProducts == null) {
				brandProducts = new ArrayList<ProductVO>();
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
		List<Category> categorieListAll = categoryService.getCategories();
        List<Category> categoryList =  categoryService.getParentCategories();
		
        
      //添加子分类到列表
    	for(Category category : categoryList){
    		for(Category categoryTemp : categorieListAll){
    			if(categoryTemp.getParentId() == category.getId()){
    				category.getChildCategories().add(categoryTemp);
    			}
    		}
    	}
    	
    	for(Category categoryTemp: categoryList){
			categoryIds.addAll(categoryTemp.getCategoryIds());
			
		}
    	return categoryIds;
	}


}
