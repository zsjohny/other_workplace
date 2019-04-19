package com.jiuy.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.ProductSkuFacade;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SaleStatus;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

/**
* @author WuWanjian
* @version 创建时间: 2016年10月31日 下午9:20:54
*/
@Controller
@RequestMapping("/productpromotion")
@Login
public class ProductPromotionSettingController {
	 private static final Logger logger = LoggerFactory.getLogger(ProductPromotionSettingController.class);
	 
		
	    @Autowired
	    private ProductService productService;
	    
	@Autowired
    private ProductSkuFacade productSkuFacade;
	
	@Autowired
    private ProductSKUService productSKUService;
	
	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@AdminOperationLog
    @RequestMapping("/promotionmanagement/index")
    public String index(ModelMap modelMap) {
        
        long lastUpdate = productSKUService.lastSyncTime();
        
        modelMap.put("sync_count", productSKUService.syncCount());
        modelMap.put("un_sync_count", productSKUService.unSyncCount());
        modelMap.put("last_update", DateUtil.convertMSEL(lastUpdate));
    	
    	return "page/backend/promotionmanagement";
    }

	@AdminOperationLog
	@RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse updateProductPromotionInfo(@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(value = "season", required = false, defaultValue = "-1") long season,
            @RequestParam(value = "year", required = false, defaultValue = "-1") long year,
            @RequestParam(value = "sku_id", required = false, defaultValue = "-1") long skuId,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "sort", required = false, defaultValue = "1")int sortType,
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
			@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
			@RequestParam(value = "categoryid", required = false, defaultValue = "-1")long categoryId,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus,
    		@RequestParam(value = "remain_min", required = false, defaultValue = "-1") int remainCountMin,
    		@RequestParam(value = "remain_max", required = false, defaultValue = "-1") int remainCountMax,
    		@RequestParam(value = "validity", required = false, defaultValue = "-1") int validity,
    		@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
    		@RequestParam(value = "promotion_type")int promotionType,
    		@RequestParam(value = "promotion_jiucoin")int promotionJiuCoin,
    		@RequestParam(value = "promotion_cash")double promotionCash,
    		@RequestParam(value = "discount")double discount,
    		@RequestParam(value = "promotion_starttime")long promotionStartTime,
    		@RequestParam(value = "promotion_endtime")long promotionEndTime,
    		@RequestParam(value = "warehouse_ids", required = false, defaultValue = "")String warehouseIdsStr){
		
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	List<Long> warehouseIds = transWarehouseIdsToList(warehouseIdsStr);
    	
    	List<ProductSKU> productSKUs =
                productSKUMapper.srchSkuInfo(null, id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName, sortType
            			, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus, validity,type,warehouseIds,-2);
    	ArrayList<Long> productIds = new ArrayList<Long>();
		for(ProductSKU productSKU : productSKUs) {
			productIds.add(productSKU.getProductId());
		}
    	
    	if(productIds.size()>0){
    		if(promotionType == 0){		//促销价无效
    			productSkuFacade.updateProductPromotionInfo(productIds,promotionCash,promotionJiuCoin,promotionStartTime,promotionEndTime,promotionType);
    		}else {
    			if(discount==0){	//根据手动设置的价格和玖币数 改变促销价
        			productSkuFacade.updateProductPromotionInfo(productIds,promotionCash,promotionJiuCoin,promotionStartTime,promotionEndTime,promotionType);
        		}else{				//按折扣统一改变促销价
        			productSkuFacade.updateProductPromotionInfoByDiscount(productIds, discount, promotionStartTime, promotionEndTime,promotionType);
        		}
    		}
    	}else {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("没有找到对应商品");
    	}
    	
    	return jsonResponse.setSuccessful();
    }

	@AdminOperationLog
	@RequestMapping(value = "/batchupdate/sale", method = RequestMethod.POST)
    @ResponseBody
	public JsonResponse batchSKUSaleOrSaleout(@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
    	@RequestParam(value = "season", required = false, defaultValue = "-1") long season,
    	@RequestParam(value = "year", required = false, defaultValue = "-1") long year,
    	@RequestParam(value = "sku_id", required = false, defaultValue = "-1") long skuId,
    	@RequestParam(value = "name", required = false, defaultValue ="") String name,
    	@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
    	@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
    	@RequestParam(value = "sort", required = false, defaultValue = "1")int sortType,
    	@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
    	@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
    	@RequestParam(value = "categoryid", required = false, defaultValue = "-1")long categoryId,
    	@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus,
    	@RequestParam(value = "remain_min", required = false, defaultValue = "-1") int remainCountMin,
    	@RequestParam(value = "remain_max", required = false, defaultValue = "-1") int remainCountMax,
    	@RequestParam(value = "validity", required = false, defaultValue = "-1") int validity,
    	@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
    	@RequestParam(value = "sale_start_time")long saleStartTime,
    	@RequestParam(value = "sale_end_time")long saleEndTime,
    	@RequestParam(value = "status")int status,
    	@RequestParam(value = "warehouse_ids", required = false, defaultValue = "")String warehouseIdsStr){
    		
		
		long currentProductId = id;
        	JsonResponse jsonResponse = new JsonResponse();
        	
        	List<Long> warehouseIds = transWarehouseIdsToList(warehouseIdsStr);

        	List<ProductSKU> productSKUs =
                    productSKUMapper.srchSkuInfo(null, id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName, sortType
                			, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus, validity,type,warehouseIds,-2);
        	ArrayList<Long> productSKUIds = new ArrayList<Long>();
    		for(ProductSKU productSKU : productSKUs) {
    			productSKUIds.add(productSKU.getId());
    		}
        	
        	if(productSKUIds.size()>0){
        		
        		//执行前产品状态
        		boolean productIsOnShelf1 = productService.isOnShelf(currentProductId);
        		
        		
        		
        		//根据status
        		if(status==-1){		//下架
        			if(saleStartTime!=0 || saleEndTime != 0){
        				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("上下架时间应为0");
        			}
        			productSKUMapper.updateSkuSaleStatus(productSKUIds,status,saleStartTime,saleEndTime);
        		} else if(status > -1){		//上架		status=1 需判断下架时间大于上架时间
        			if(status==0){
        				if(saleEndTime!=0) {
            				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("下架时间应为0");
						}
        			}
        			if(status==1){
        				if(saleStartTime>saleEndTime){
            				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("下架时间应大于上架时间");
            			}
        			}
        			productSKUMapper.updateSkuSaleStatus(productSKUIds,status,saleStartTime,saleEndTime);
        		}
        		
        		 //获取商品执行后产品状态
    			boolean productIsOnShelf2 = productService.isOnShelf(currentProductId);
        		
    			//检查产品状态变化
    	    	if(productIsOnShelf1 == true && productIsOnShelf2 == false){//上架变下架  //在架变下架进行下架商家商品
//    				下架商品时同步更新商家商品并发送通知
    	    		logger.info("上架变下架,productIsOnShelf1"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
    	    		productService.synSoldoutShopProduct(currentProductId);
    			}else if(productIsOnShelf1 == false && productIsOnShelf2 == true){//下架变上架   //不在架改为在架进行同步上架商家商品，定时上架的情况不考虑
    				//商品上架同步商家商品并发送系统通知
    				logger.info("下架变上架,productIsOnShelf1"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
    				productService.synPutawayShopProduct(currentProductId);
    			}else{
    				logger.info("商品状态没变化"+"productIsOnShelf1:"+productIsOnShelf1+",productIsOnShelf2:"+productIsOnShelf2);
    			}
        	}else {
        		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("没有找到对应商品");
        	}
        	
        	return jsonResponse.setSuccessful();
	}
	
	private List<Long> transWarehouseIdsToList(String warehouseIdsStr){
    	ArrayList<Long> warehouseIds = new ArrayList<Long>();
    	if(StringUtils.equals("", warehouseIdsStr)){
    		return warehouseIds;
    	}
    	String[] str = warehouseIdsStr.split(",");
    	for(String item : str){
    		warehouseIds.add(Long.parseLong(item));
    	}
    	return warehouseIds;
    }
}
