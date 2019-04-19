package com.jiuy.core.business.facade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.jiuy.core.business.assemble.composite.ProductPropAssembler;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.product.ProductBoutique;
import com.jiuy.core.meta.product.ProductVO;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.ProductCategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.UserVisitHistoryService;
import com.jiuy.core.service.actionlog.ActionLogService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.product.SaleStatus;
import com.jiuyuan.entity.Category;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.brand.BrandLogo;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.ProductPropVO;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
@Service
public class ProductSkuFacade {
	
	 private static final Logger logger = LoggerFactory.getLogger(ProductSkuFacade.class);
	    Log log = LogFactory.get();
	    
	    
	@Autowired
	private ActionLogService actionLogService;	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductPropAssembler productPropAssembler;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private LOWarehouseService loWarehouseService;
	
	@Autowired
	private BrandLogoServiceImpl brandLogoService;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private UserVisitHistoryService userVisitHistoryService;
	
	@Autowired
	private StoreBusinessDao storeBusinessDao;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private CategoryService categoryService;
	

	
	
	
	

	
	
	public List<ProductSKUVO> srchSkuInfo(PageQuery pageQuery, long id, long season, long year, String name, String clothesNumber, int remainCountMin,
                                          int remainCountMax, long skuId,  String brandName, int sortType,
                              			  String saleStatus, long parentCategoryId, long categoryId, int skuStatus, int validity, int type, List<Long> warehouseIds,
                              			int isBoutique) {
		List<ProductSKUVO> productSKUVOs = new ArrayList<ProductSKUVO>();
		
        List<ProductSKU> productSKUs =
            productSKUMapper.srchSkuInfo(pageQuery, id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName, sortType
        			, saleStatus, parentCategoryId, categoryId, skuStatus, validity,type,warehouseIds,isBoutique);
        
        Map<Long, LOWarehouse> warehouseMap = loWarehouseService.getWarehouseMap(null);
		
		Set<Long> productIds = new HashSet<Long>();
		for(ProductSKU productSKU : productSKUs) {
			productIds.add(productSKU.getProductId());
		}
		
		Map<Long, Integer> visiteCountByProductId = new HashMap<>();
		if(productIds.size()>0){
			visiteCountByProductId = userVisitHistoryService.visitCountById(productIds, 0);
		}
		
		Map<Long, Product> productMap = productService.productMapOfIds(productIds);
		for(ProductSKU productSKU : productSKUs) {
			ProductSKUVO productSKUVO = new ProductSKUVO();
			BeanUtils.copyProperties(productSKU, productSKUVO);
			
			Product product = productMap.get(productSKU.getProductId());
			if(product==null){
				logger.info("根据SKU的商品ProductId获取Product对象为空，请尽快排查问题！！！！！！！productSKUId:"+productSKU.getId()+"，productSKU.getProductId()："+productSKU.getProductId());
				continue;
			}
			ProductVO productVO = new ProductVO();
			productVO.setProduct(product);
			
			List<ProductCategory> productCategoryList = productCategoryService.search(productSKU.getProductId());
			if(productCategoryList.size()>0){
				long category_Id = productCategoryList.get(0).getCategoryId();
				Category categoryById = categoryService.getCategoryById(category_Id);
				if(categoryById!=null){
					long parentId = categoryById.getParentId();
					if(parentId==0){
						productSKUVO.setParentCategoryName(categoryById.getCategoryName());
						productSKUVO.setParentCategoryId(categoryById.getId());
					}else{
						Category parentCategory = categoryService.getCategoryById(parentId);
						productSKUVO.setChildCategoryName(categoryById.getCategoryName());
						productSKUVO.setCategoryId(categoryById.getId());
						if(parentCategory!=null){
							productSKUVO.setParentCategoryName(parentCategory.getCategoryName());
							productSKUVO.setParentCategoryId(parentCategory.getId());
						}
					}
				}
			}
			
			LOWarehouse warehouse = warehouseMap.get(product.getlOWarehouseId());
			productVO.setWarehouseName(warehouse == null ? "" : warehouse.getName());

			LOWarehouse warehouse2 = warehouseMap.get(product.getlOWarehouseId2());
			productVO.setWarehouseName2(warehouse2 == null ? "" : warehouse2.getName());
			
			Integer visitCount = visiteCountByProductId.get(product.getId());
			productVO.setVisitCount(visitCount == null ? 0 : visitCount);
			
			productSKUVO.setProductVO(productVO);
			
			productSKUVO.setLoWarehouse(warehouse);
		 
			List<ProductPropVO> skuPropVOs = productSKU.getProductProps();
	        productPropAssembler.assemble(skuPropVOs);
	        productSKUVO.setSkuProperties(skuPropVOs);
	        
//	        productSKUVO.setPosition(productSKU.getPosition());
//	        productSKUVO.setRemainCount2(productSKU.getRemainCount2());
//	        productSKUVO.setlOWarehouseId2(productSKU.getlOWarehouseId2());
//	        productSKUVO.setRemainKeepTime(productSKU.getRemainKeepTime());
//	        productSKUVO.setCostPrice(productSKU.getCostPrice());
//	        productSKUVO.setSort(productSKU.getSort());
//	        productSKUVO.setWeight(productSKU.getWeight());
//	        productSKUVO.setMarketPrice(productSKU.getMarketPrice());
//	        productSKUVO.setProductSKU(productSKU);
	        productSKUVOs.add(productSKUVO);
		}
		
//		for (ProductSKUVO productSKUVO : productSKUVOs) {
//			logger.info("==============商品查询返回remainKeepTime："+productSKUVO.getRemainKeepTime()+",position:"+productSKUVO.getPosition()
//					+",remainCount2:"+productSKUVO.getRemainCount2()+",setLOWarehouseId2:"+productSKUVO.getSetLOWarehouseId2()
//					+",remainCount:"+productSKUVO.getRemainCount()+",costPrice:"+productSKUVO.getCostPrice());
//		}
		return productSKUVOs;
	}

    public int srchSkuInfoCount(long id, long season, long year, String name, String clothesNumber, int remainCountMin, int remainCountMax, long skuId, String brandName
			, String saleStatus, long parentCategoryId, long categoryId, int skuStatus, int validity,int type,List<Long> warehouseIds) {
        return productSKUMapper.srchSkuInfoCount(id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName
    			, saleStatus, parentCategoryId, categoryId, skuStatus, validity,type,warehouseIds);
	}
    
    @Transactional(rollbackFor = Exception.class)
	public void addProductSku(String name, String clothesNumber, double marketPrice, double costPrice, double weight, long warehouseId,
			int remainKeepTime, long brandId, String color, String size, String position, int status) throws Exception {
		if(StringUtils.equals(color, "") || StringUtils.equals(size, "")) {
			throw new ParameterErrorException();
		}
		
		List<String> propertyIds = new ArrayList<String>();
		
		long colorId = propertyService.getPropertyNameIdByName("颜色");
		long sizeId = propertyService.getPropertyNameIdByName("尺码");
		
		String[] colors = color.split(",");
		String[] sizes = size.split(",");
		
		for(String colorItem : colors) {
            for (String sizeItem : sizes) {
                propertyIds.add(colorId + ":" + colorItem + ";" + sizeId + ":" + sizeItem);
            }
		}
		
		long time = System.currentTimeMillis();
		
        List<Product> products = productService.getByClothesNums(CollectionUtil.createSet(clothesNumber));
        if (products.size() > 0) {
        	
            Product product = products.get(0);
            long productId = product.getId();
            productMapper.update(productId, brandId);
            productSKUMapper.updateByProductId(CollectionUtil.createSet(productId), brandId);
            productSKUMapper.insertSKUs(product.getName(), productId, propertyIds, marketPrice, costPrice, weight, position, clothesNumber, warehouseId, remainKeepTime, brandId, status);
        } else {
            Product product = new Product();
            product.setClothesNumber(clothesNumber);
            product.setBrandId(brandId);
            product.setMarketPrice((int) marketPrice);
            product.setName(name);
            product.setlOWarehouseId(warehouseId);
            product.setSaleStartTime(time);
            product.setSaleEndTime(time);
            product.setCreateTime(time);
            product.setUpdateTime(time);
            product.setDetailImages("[]");
            product.setSummaryImages("[]");
            product.setStatus(-1);
            product.setPrice(0);
            product.setRestrictCycle(0);
            product.setPromotionStartTime(time);
            product.setPromotionEndTime(time);
            product.setPromotionSetting(0);
            product.setRestrictDayBuy(-1);
            product.setRestrictHistoryBuy(-1);
            product.setShowStatus(0);
            product.setType(1);// 批发/零售

            long productId = productService.insertProduct(product);
            productSKUMapper.insertSKUs(name, productId, propertyIds, marketPrice, costPrice, weight, position, clothesNumber, warehouseId, remainKeepTime, brandId, status);
        }
        
        // 维护Product表 更新Product中LoWareHouseId
        productMapper.updateWarehouseId(CollectionUtil.createList(clothesNumber), warehouseId);
        
	}

    @Transactional(rollbackFor = Exception.class)
	public void addProductSku(String name, String clothesNumber, double marketPrice, double costPrice, double weight, long warehouseId,
			int remainKeepTime, long brandId, String color, String size, String position, int status,double wholeSaleCash) throws Exception {
		if(StringUtils.equals(color, "") || StringUtils.equals(size, "")) {
			throw new ParameterErrorException();
		}
		
		List<String> propertyIds = new ArrayList<String>();
		
		long colorId = propertyService.getPropertyNameIdByName("颜色");
		long sizeId = propertyService.getPropertyNameIdByName("尺码");
		
		String[] colors = color.split(",");
		String[] sizes = size.split(",");
		
		for(String colorItem : colors) {
            for (String sizeItem : sizes) {
                propertyIds.add(colorId + ":" + colorItem + ";" + sizeId + ":" + sizeItem);
            }
		}
		
		long time = System.currentTimeMillis();
		
        List<Product> products = productService.getByClothesNums(CollectionUtil.createSet(clothesNumber));
        if (products.size() > 0) {
        	
            Product product = products.get(0);
            long productId = product.getId();
            productMapper.update(productId, brandId);
            productSKUMapper.updateByProductId(CollectionUtil.createSet(productId), brandId);
            productSKUMapper.insertSKUs(product.getName(), productId, propertyIds, marketPrice, costPrice, weight, position, clothesNumber, warehouseId, remainKeepTime, brandId, status);
        } else {
            Product product = new Product();
            product.setClothesNumber(clothesNumber);
            product.setBrandId(brandId);
            product.setMarketPrice((int) marketPrice);
            product.setName(name);
            product.setlOWarehouseId(warehouseId);
            product.setSaleStartTime(time);
            product.setSaleEndTime(time);
            product.setCreateTime(time);
            product.setUpdateTime(time);
            product.setDetailImages("[]");
            product.setSummaryImages("[]");
            product.setStatus(-1);
            product.setPrice(0);
            product.setRestrictCycle(0);
            product.setPromotionStartTime(time);
            product.setPromotionEndTime(time);
            product.setPromotionSetting(0);
            product.setRestrictDayBuy(-1);
            product.setRestrictHistoryBuy(-1);
            product.setShowStatus(0);
            product.setType(1);// 批发/零售
            product.setWholeSaleCash(wholeSaleCash);

            long productId = productService.insertProduct(product);
            productSKUMapper.insertSKUs(name, productId, propertyIds, marketPrice, costPrice, weight, position, clothesNumber, warehouseId, remainKeepTime, brandId, status);
        }
        
        // 维护Product表 更新Product中LoWareHouseId
        productMapper.updateWarehouseId(CollectionUtil.createList(clothesNumber), warehouseId);
        
	}

    public void uptProductSku(long id, String name, double marketPrice, double costPrice, double weight, String position, int sort, int remainCount, int remainCount2,
                              int remainKeepTime, long skuNo, int remainCountLock, long remainCountStartTime,
                              long remainCountEndTime, int isRemainCountLock) {
        productSKUMapper.uptProductSku(id, name, marketPrice, costPrice, weight, position, sort, remainCount, remainCount2, remainKeepTime, skuNo,
            remainCountLock, remainCountStartTime, remainCountEndTime, isRemainCountLock);
    }
    
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> uptProductSku(long productSKUId, Long productId, int status, long saleStartTime,
                                             long saleEndTime) {
    	long currentProductId = 0;
    	boolean productIsOnShelf1 = true;
    	boolean productIsOnShelf2 = true;
    	
    	 Map<String, Object> map = null;
    	 //根据SKUid进行操作
    	if(productSKUId != -1 && productId == null) {
    		
    		
    		ProductSKU productSKU = productSKUService.searchById(productSKUId);
    		currentProductId = productSKU.getProductId();
    		//执行前产品状态
    		productIsOnShelf1 = productService.isOnShelf(currentProductId);
    		// `Status` tinyint(4) DEFAULT '0' COMMENT '状态:-3废弃，-2停用，-1下架，0正常，1定时上架',
    		long productSKUStatus = productSKU.getStatus();
    		if(productSKU == null ){
    			throw new ParameterErrorException("sku不存在");
    		}
    		//检查商家或定时上架时
    		if (status >= 0) {
    			if (!isMatchedOnSale(productSKU.getProductId())) {
    				throw new ParameterErrorException("信息不全或款号不存在");
    			}
    			if (productSKU.getRemainCount() < 1) {
    				throw new ParameterErrorException("库存不足");
    			}
            }
    		
    		productSKUMapper.uptProductSku(productSKUId, status, saleStartTime, saleEndTime);
    		//获取商品执行后产品状态
    		productIsOnShelf2 = productService.isOnShelf(currentProductId);
    		
    		
    	} else if(productSKUId == -1 && productId != null) {//根据商品进行操作
    		currentProductId = productId;
    		//执行前产品状态
    		productIsOnShelf1 = productService.isOnShelf(productId);
            map = batchSale(productId, status, saleStartTime, saleEndTime);
            //TODO v2.3 根据商品ID进行处理
          //获取商品执行后产品状态
    		productIsOnShelf2 = productService.isOnShelf(productId);
    	}
    	
    	//检查产品状态变化
    	if(productIsOnShelf1 == true && productIsOnShelf2 == false){//上架变下架  //在架变下架进行下架商家商品
//			下架商品时同步更新商家商品并发送通知
    		logger.info("上架变下架,productIsOnShelf1"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
    		productService.synSoldoutShopProduct(currentProductId);
		}else if(productIsOnShelf1 == false && productIsOnShelf2 == true){//下架变上架   //不在架改为在架进行同步上架商家商品，定时上架的情况不考虑
			//商品上架同步商家商品并发送系统通知
			logger.info("下架变上架,productIsOnShelf1"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
			productService.synPutawayShopProduct(currentProductId);
		}else{
			logger.info("商品状态没变化"+"productIsOnShelf1:"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
		}
    	
//    	if (productId == null) {
//			Product product = productSKUMapper.getProductBySkuId(id);
//			productId = product.getId();
//		}
    	//维护product表上下架
//    	updateProductSaleStatus(productId);
    	
        return map;
    }
   
    /**
     * 下架指定SKU
     * @param productSKUId
     */
	public void soldoutProductSKUList(long productSKUId) {
			//下架商品时同步更新商家商品并发送通知
//    		ProductSKU productSKU = productSKUService.searchById(productSKUId);
    		ProductSKU productSKU = productSKUService.skuByNo(CollectionUtil.createList(productSKUId)).get(productSKUId);
    		long productId = productSKU.getProductId();
			//执行前产品状态
    		boolean productIsOnShelf1 = productService.isOnShelf(productId);
    		
			Collection<Long> ids = new ArrayList<Long>();
			ids.add(productSKUId);
			productSKUMapper.deactivateProductSKUByIds(ids);
			
			 //获取商品执行后产品状态
			boolean productIsOnShelf2 = productService.isOnShelf(productId);
			if(productIsOnShelf1 == true && productIsOnShelf2 == false){//上架变下架
				productService.synSoldoutShopProduct(productSKU.getProductId());	
			}else{
				logger.info("下架指定SKU时产品状态未改变无需进行下架商家商品");
			}
	}


	@SuppressWarnings("unused")
	@Deprecated
    private void updateProductSaleStatus(Long productId) {
    	long minSaleStartTime = System.currentTimeMillis();
    	long maxSaleEndTime = -1;
    	int maxStatus = -1;
    	
    	List<ProductSKU> skus = productSKUMapper.skusOfProductIds(CollectionUtil.createList(productId));
    	for (ProductSKU productSKU : skus) {
    		int status = productSKU.getStatus();
    		if (productSKU.getSaleStartTime() == 0 || productSKU.getSaleEndTime() == 0) {
				continue;
			}
    		
    		long saleStartTime = productSKU.getSaleStartTime();
    		long saleEndTime = productSKU.getSaleEndTime();
    		if (status > -1) {
    			if (saleStartTime < minSaleStartTime) {
    				minSaleStartTime = saleStartTime;
    			}
    			if (maxSaleEndTime != 0 && saleEndTime > maxSaleEndTime) {
    				maxSaleEndTime = saleEndTime;
    			}
    			if (status > maxStatus) {
    				maxStatus = status;
    			}
			}
		}
    	
    	//维护Product表
    	productService.update(productId, maxStatus, minSaleStartTime, maxSaleEndTime);
	}

	public List<ProductSKU> GetProductSkuByClothesNum(String clothesNum) {
        return productSKUMapper.getProductSKUs(-1, clothesNum);
    }

    public List<Long> existProperties(String clothesNum, int type, long attrId) {
        List<Long> attrList = new ArrayList<Long>();
		
		List<Product> products = productService.getByClothesNums(CollectionUtil.createList(clothesNum));
		if(products.size() > 0) {
			long productId = products.get(0).getId();
			List<ProductSKU> productSKUs = productSKUMapper.getProductSKUs(productId, "");
			
            if (type == 0) {
                for (ProductSKU productSKU : productSKUs) {
                    String propertyIds = productSKU.getPropertyIds();
                    long color = Long.parseLong(propertyIds.split(";")[0].substring(2));
                    long size = Long.parseLong(propertyIds.split(";")[1].substring(2));
                    if (color == attrId) {
                        attrList.add(size);
                    }
                }
            } else {
                for (ProductSKU productSKU : productSKUs) {
                    String propertyIds = productSKU.getPropertyIds();
                    long color = Long.parseLong(propertyIds.split(";")[0].substring(2));
                    long size = Long.parseLong(propertyIds.split(";")[1].substring(2));
                    if (size == attrId) {
                        attrList.add(color);
                    }
                }
			}
		}
		
        return attrList;
	}

	//数据量很大
	public List<Map<String, Object>> excelOfSku() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<Long, BrandLogo> brandMap = brandLogoService.getBrandMap();
		Map<Long, Product> productMap = productService.productMapOfIds(null);
		Map<Long, LOWarehouse> warehouseMap = loWarehouseService.getWarehouseMap(null);
		
		Set<Long> propertyNameIds = new HashSet<Long>();
		propertyNameIds.add(7L);
		propertyNameIds.add(8L);
		
		Map<Long, ProductPropValue> propertyValueMap = propertyService.getValueMap(propertyNameIds);
		List<ProductSKU> productSKUs = productSKUMapper.loadAllSkus();
		
		Set<Long> productIds = new HashSet<>();
		for(ProductSKU productSKU : productSKUs) {
			productIds.add(productSKU.getProductId());
		}
		Map<Long, List<ProductSKU>> onSaleSKUsOfProductId = productSKUService.onSaleSKUOfProductId(productIds);
		
		for(ProductSKU productSKU : productSKUs) {
			Map<String, Object> map = new HashMap<String, Object>();
			Product product = productMap.get(productSKU.getProductId());
			if(product == null) {
				map.put("brandName", "商品或已删除");
				map.put("clothesNum", "商品或已删除");
				map.put("isOnsale", "商品或已删除");
				map.put("warehouse", "商品或已删除");
			} else {
				BrandLogo brandLogo = brandMap.get(product.getBrandId());
				LOWarehouse loWarehouse = warehouseMap.get(product.getlOWarehouseId());
				
				map.put("brandName", brandLogo != null ? brandLogo.getBrandName() : "品牌不存在");
				map.put("clothesNum", product.getClothesNumber());
				map.put("isOnsale", onSaleSKUsOfProductId.get(productSKU.getProductId()) != null ? "上架" : "下架" );
				map.put("warehouse", loWarehouse != null ? loWarehouse.getName() : "仓库不存在");
			}
			
			map.put("color", propertyValueMap.get(productSKU.getPropertyMap().get(PropertyName.COLOR.getValue()+"")).getPropertyValue());
			map.put("size",	propertyValueMap.get(productSKU.getPropertyMap().get(PropertyName.SIZE.getValue()+"")).getPropertyValue());
			map.put("sku", productSKU.getSkuNo());
			map.put("remainCount", productSKU.getRemainCount());
			
			list.add(map);
		}
		
		return list;
	}


	@Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchSale(Long productId, Integer status, Long saleStartTime, Long saleEndTime) {
    	Map<String, Object> map = new HashMap<String, Object>();
    	
        //下架
        if (status == -1) {
            int count = productSKUMapper.updateByProductId(CollectionUtil.createList(productId), status);
        
            map.put("success", count);
            map.put("fail", 0);
            map.put("detail", null);
            
            return map;
        } else if (status > -1) {
            List<ProductSKU> skus = productSKUMapper.skusOfProductIds(CollectionUtil.createList(productId));
            Set<Long> matched = new HashSet<>();
            Set<String> unMatched = new HashSet<>();

            //信息不全
            if (!isMatchedOnSale(productId)) {
            	 for (ProductSKU productSKU : skus) {
                     unMatched.add(productSKU.getSkuNoStr());
                 }
            	 
            	 map.put("success", 0);
                 map.put("fail", unMatched.size());
                 map.put("detail", unMatched);
                 
                 return map;
            }

            //库存不足
            for (ProductSKU productSKU : skus) {
                if (productSKU.getRemainCount() <= 0 || productSKU.getStatus() == -2) {
                    unMatched.add(productSKU.getSkuNoStr());
                } else {
                    matched.add(productSKU.getId());
                }
            }
            
            int count = productSKUService.updateSkuSaleStatus(matched, status, saleStartTime, saleEndTime);

            map.put("success", count);
            map.put("fail", unMatched.size());
            map.put("detail", unMatched);
            
            return map;
        }
        
        return null;
    }

    private boolean isMatchedOnSale(long productId) {
    	List<Product> list = productMapper.checkInfoCompleted(productId);
    	
    	if(list.size() < 1) {
    		return false;
    	}
    	
    	Product product = list.get(0);
    	if(StringUtils.equals("[]", product.getDetailImages()) 
    			|| StringUtils.equals("[]", product.getSummaryImages())) {
    		return false;
    	}
    	
    	return true;
    }

	public void validUpdate(String skuNosString, int type) {
		String[] skuArray = skuNosString.split(",");
		Set<Long> skuNos = new HashSet<>();
	
		for(String skuIdItem : skuArray) {
			skuNos.add(Long.parseLong(skuIdItem));
		}
		if(type == 0) {
			productSKUService.batchValidityUpdate(skuNos, -2);
		}
		if(type == 1) {
			productSKUService.batchValidityUpdate(skuNos, -1);
		}
	}

	public int updateProductPromotionInfo(ArrayList<Long> productIds, double promotionCash, int promotionJiuCoin,
			long promotionStartTime, long promotionEndTime,int promotionSetting) {
		return productMapper.updateProductPromotionInfo(productIds,promotionCash,promotionJiuCoin,promotionStartTime,promotionEndTime,promotionSetting);
		
	}

	public int updateProductPromotionInfoByDiscount(ArrayList<Long> productIds, double discount,
			long promotionStartTime, long promotionEndTime , int promotionSetting) {
		return productMapper.updateProductPromotionInfoByDiscount(productIds, discount, promotionStartTime, promotionEndTime,promotionSetting);
	}

	/**
	 * 获取(筛选)买手推荐列表
	 * @param page
	 * @param pageSize
	 * @param id
	 * @param name
	 * @param clothesNumber
	 * @param brandName
	 * @param skuStatus
	 * @param parentCategoryId
	 * @param categoryId
	 * @return
	 */
	public JsonResponse searchBoutiqueProduct(Integer page, Integer pageSize, Long id, String name, String clothesNumber,
			String brandName, Integer skuStatus, Integer parentCategoryId, Integer categoryId,Integer sortType,int vip) {
		
		logger.info("买手推荐查询是入参vip:"+vip);
		JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	PageQuery pageQuery = new PageQuery(page, pageSize);
    	
    	
        int totalCount = productSKUMapper.searchBoutiqueProductCount(id, name, clothesNumber,brandName,skuStatus, parentCategoryId, categoryId,sortType,vip);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	BeanUtils.copyProperties(pageQuery, pageQueryResult);
    	
        List<ProductBoutique> productBoutiquelist = productSKUMapper.searchBoutiqueProductInfo(pageQuery, id, name, clothesNumber, brandName ,skuStatus, parentCategoryId, categoryId,sortType,vip);

        Map<String,Map<String,String>> productMap = new HashMap<String,Map<String,String>>();
        List<String> productIdList = new ArrayList<String>();
        for (ProductBoutique productBoutique : productBoutiquelist) {
        	Long productId = productBoutique.getProductId();
			if(productMap.containsKey(productId+"")){
				Map<String, String> map = productMap.get(productId+"");
				int stockOld = Integer.parseInt(map.get("stock"));
				Integer stockNew = productBoutique.getStock();
				if(stockNew==null){
					continue;
				}else{
					map.put("stock", stockNew+stockOld+"");
				}
			}else{
				Map<String,String> map = new HashMap<String,String>();
				map.put("id", productBoutique.getId()+"");
				map.put("productId", productId+"");
				map.put("brandName", productBoutique.getBrandName()+"");
				map.put("product", "名称:"+productBoutique.getName()+" 商品款号:"+productBoutique.getClothesNumber());
				map.put("stock", productBoutique.getStock()+"");
				map.put("vip", String.valueOf(productBoutique.getVip()));
				Double xprice = productBoutique.getXprice();
				if(xprice==null){
					xprice = 0.0;
				}
				map.put("xprice", (double)xprice+"");
				productMap.put(productId+"", map);
				productIdList.add(productId+"");
			}
		}
        
        List<Map<String,String>> productList = new ArrayList<Map<String,String>>();
        for (String productId : productIdList) {
        	Map<String,String> map = productMap.get(productId);
        	productList.add(map);
		}
//        
//        Collections.sort(productList, new Comparator<Product>() {
//			public int compare(Product o1, Product o2) {
//				if(o1.getPromotionLastTime() > o2.getPromotionLastTime()){
//					return 1;
//				}else if(o1.getPromotionLastTime() < o2.getPromotionLastTime()){
//					return -1;
//				}else return 0;
//				
//			}
//		});
        
        data.put("productList", productList);
    	data.put("total", pageQueryResult);
    	logger.info("买手推荐查询是Data:"+JSONObject.toJSONString(data));
    	return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 设置买手推荐/取消买手推荐
	 * @param productIds	商品Id数组 例如:1961,1962,1964
	 * @param status	状态:0：推荐，-1：取消推荐
	 * @return
	 */
	public JsonResponse setBoutiqueProduct(String productIds, Integer status) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			String[] productIdArray = productIds.split(",");
			if(productIdArray.length==0){
				return jsonResponse.setError("请确认已选中商品");
			}
			for (String productId : productIdArray) {
				List<ProductSKU> productSKUs = productSKUMapper.srchSkuInfo(null, Long.parseLong(productId), -1L, -1L, "", "", -1, -1, -1L, "", 1, SaleStatus.getSql(0), -1L, -1L, 0, -1, -1, new ArrayList<Long>(), -2);
				boolean flag = true;
				for (ProductSKU productSKU : productSKUs) {
					if(productSKU.getStatus()>-1){
						flag = false;
					}
				}
				if(flag){
					return jsonResponse.setError("废弃/停用/下架的商品无法设置买手推荐");
				}
				Long updateTime = System.currentTimeMillis();
				int record = 0;
				ProductBoutique productBoutique = productSKUMapper.getBoutiqueProductByProductId(productId);
				if(productBoutique==null){
					if(status==0){
						record = productSKUMapper.insertBoutiqueProduct(Long.parseLong(productId), status,updateTime,updateTime);
					}else{
						return jsonResponse.setError("该商品并没有设置买手推荐");
					}
				}else if(status==0 || status==-1){
					//System.out.println(productBoutique.toString());
					record = productSKUMapper.updateBoutiqueProduct(Long.parseLong(productId), status,updateTime);
				}else{
					return jsonResponse.setError("没有该状态,请确认");
				}
				if(record!=1){
					return jsonResponse.setError(productId+"此商品设置买手推荐/取消买手推荐失败");
				}
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("设置买手推荐/取消买手推荐失败");
		}
	}

	/**
	 * 商品改价
	 * @param productId	商品Id
	 * @param price	商品批发价格
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public JsonResponse udpateBoutiqueProductPrice(String productId, Double price) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			int record = productSKUMapper.udpateBoutiqueProductPrice(productId,price);
			if(record!=1){
				return jsonResponse.setError("买手推荐商品改价失败");
			}
			int count = productSKUMapper.udpateProductWholeSaleCash(productId,price);
			if(count!=1){
				return jsonResponse.setError("商品改价失败");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("商品改价失败");
		}
	}
    /**
     * 设置VIP商品
     * @param productId		商品Id
     * @param vip		VIP设置：0取消VIP，1设置VIP
     * @return
     */
	public int udpateBoutiqueSetVip(long productId, int vip,AdminUser adminUser) {
			int record = productSKUMapper.udpateBoutiqueSetVip(productId,vip);
			if(record == 1){
				
				Product product = productService.getProductById(productId);
				if(product!=null){
					int ret = actionLogService.setProdoctAction(product,vip,adminUser);
					logger.info("设置VIP商品操作日志，ret："+ret);
				}else{
					logger.info("设置VIP商品操作日志时，商品我空，请排查问题，productId："+productId);
				}
				
			}
			return record;
	}
	 /**
     * 更新商品商家上架时间
     * @return
     */
	public JsonResponse updateSaleStartTime(String productId) {
		Long updateTime = System.currentTimeMillis();
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			logger.info("买手推荐商品更新商家上架时间,productId:"+productId+",updateTime:"+updateTime);
			//int ret = productSKUMapper.updateSaleStartTime(productId,updateTime);
			int ret = productSKUMapper.updateBoutiqueProductUpdateTime(productId,updateTime);
			logger.info("买手推荐商品更新商家上架时间,productId:"+productId+",ret:"+ret);
			if(ret!=1){
				return jsonResponse.setError("失败");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
			return jsonResponse.setError("失败");
		}
	}
    
	/**
     * 平台下架商品，同步下架商家商品
     * 
     */
	@Transactional(rollbackFor = Exception.class)
	public void synchronousUndercarriageProduct(List<Long> productSKUIds) {
		//更改商家商品的
		int i = productSKUMapper.batchUpdate(productSKUIds, -1);
		if(i==-1){
			logger.info("平台商品下架状态更改失败！");
			throw new RuntimeException("平台商品下架状态更改失败！");
		}
		List<Long> productIdList = new ArrayList<Long>();
		if(productSKUIds.size() == 1){
			productIdList = productSKUMapper.getProductIdBySKUID(productSKUIds.get(0));
		}
		//获取这些productSKU的商品的Id
		if(productSKUIds.size() > 1){
			productIdList = productSKUMapper.getProductIdBySKUIDS(productSKUIds);
			
		}
		List<Long> saleEndProductIdList = new ArrayList<Long>();
		//获取后对比状态确认是否这些商品在下架
		for(Long id:productIdList){
			boolean flag = productSKUService.comfirmSaleEndStatus(id);
			if(flag){
				saleEndProductIdList.add(id);
			}
		}
		//下架shop_product中所有商家的上架该商品
		for(Long id:saleEndProductIdList){
			//下架该商品
//			productSKUService.saleEndProduct(id);
		}
		
		
	}
}
