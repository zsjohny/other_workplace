package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.ProductSkuFacade;
import com.jiuy.core.dao.AdminUserDao;
import com.jiuy.core.dao.LOWarehouseDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.JiuCoinService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuy.web.controller.util.CollectionUtil;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuy.web.controller.util.model.CommonResponseObject;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuyuan.constant.PropertyName;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.product.SaleStatus;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductPropValue;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.product.ProductSKUVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.ICategoryNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IProductSkuNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.anno.VersionControl;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

@Controller
@RequestMapping("/productsku")
@Login
@VersionControl("1.9.3")
public class ProductSKUController {
	 private static final Logger logger = LoggerFactory.getLogger(ProductSKUController.class);
	    Log log = LogFactory.get();
	    
	    
	    
	@Autowired
	private IProductNewService productNewService;
		    
	@Autowired
	private IProductSkuNewService productSkuNewService;

	@Autowired
	private AdminUserDao adminUserDao;
		
	@Autowired
	private ProductService productService;
	   
    
    @Autowired
    private ProductSkuFacade productSkuFacade;
    
    @Autowired
    private ProductSKUService productSKUService;
    
    @Autowired
    private ErpDelegator erpDelegator;
    
    @Autowired
    private LOWarehouseService lOWarehouseService;
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private BrandLogoServiceImpl brandLogoService;
    
    @Autowired
    private LOWarehouseDao lowarehouseDao;
    
	@Resource
	private JiuCoinService jiuCoinService;
	
	@Autowired
	private ICategoryNewService categoryNewService;
    
    @AdminOperationLog
    @RequestMapping("/management/index")
    public String index(ModelMap modelMap) {
        
        long lastUpdate = productSKUService.lastSyncTime();
        
        modelMap.put("sync_count", productSKUService.syncCount());
        modelMap.put("un_sync_count", productSKUService.unSyncCount());
        modelMap.put("last_update", DateUtil.convertMSEL(lastUpdate));
    	
    	return "page/backend/skumanagement";
    }
    
    @RequestMapping("/management")
    @AdminOperationLog
    @ResponseBody
    public JsonResponse management() {
        JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<>();
        long lastUpdate = productSKUService.lastSyncTime();
        
        data.put("sync_count", productSKUService.syncCount());
        data.put("un_sync_count", productSKUService.unSyncCount());
        data.put("last_update", DateUtil.convertMSEL(lastUpdate));
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
	
    /**
     * 搜索商品
     * {"page":1,"sale_status":"0","sku_status":"0","sort":"3","isBoutique":"-2","validity":"0"}
     * 
     * @param skuStatus 0:全部  1:正常  2:已售罄  3:缺货(库存量小于等于10个为缺货状态)
     * @param validity -2:停用 -1:全部 0:正常
     * @return
     */
    @RequestMapping(value = "/sku/search")
    @ResponseBody
    public JsonResponse srchSkuInfo(@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize,
            @RequestParam(value = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(value = "season", required = false, defaultValue = "-1") long season,
            @RequestParam(value = "year", required = false, defaultValue = "-1") long year,
            @RequestParam(value = "sku_id", required = false, defaultValue = "-1") long skuId,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "sort", required = false, defaultValue = "1")int sortType,
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,//0全部;1上架;2下架;3即将上架
			@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
			@RequestParam(value = "categoryid", required = false, defaultValue = "-1")int categoryId,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus,
    		@RequestParam(value = "remain_min", required = false, defaultValue = "-1") int remainCountMin,
    		@RequestParam(value = "remain_max", required = false, defaultValue = "-1") int remainCountMax,
    		@RequestParam(value = "validity", required = false, defaultValue = "-1") int validity,
    		@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
    		@RequestParam(value = "warehouse_ids", required = false, defaultValue = "")String warehouseIdsStr,
    		@RequestParam(value = "isBoutique", required = false, defaultValue = "-2")int isBoutique) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	PageQuery pageQuery = new PageQuery(page, pageSize);
    	
    	List<Long> warehouseIds = transWarehouseIdsToList(warehouseIdsStr);
    	
        int totalCount = productSkuFacade.srchSkuInfoCount(id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName
        			, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus, validity,type,warehouseIds);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	BeanUtils.copyProperties(pageQuery, pageQueryResult);
    	
        List<ProductSKUVO> skuvos = productSkuFacade.srchSkuInfo(pageQuery, id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName ,sortType
        			, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus, validity, type,warehouseIds,isBoutique);
        
        List<LOWarehouse> warehouses = lOWarehouseService.srchWarehouse(null, "", false);
        
    	data.put("skuInfo", skuvos);
    	data.put("total", pageQueryResult);
    	data.put("warehouses", warehouses);
//    	logger.info("====================================商品查询返回data："+JSON.toJSONString(data));
    	return jsonResponse.setSuccessful().setData(data);
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
    
    /**
     * 商品查询中的玖币抵扣百分比修改
     */
    @RequestMapping(value = "/sku/deduct/batchupdate")
    @ResponseBody
    public JsonResponse srchSkuInfo(@RequestParam(value = "page", required = false, defaultValue = "1")int page, 
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize,
            @RequestParam(value = "id", required = false, defaultValue = "-1") long id,
            @RequestParam(value = "product_ids", required = false, defaultValue = "")String productIdsStr,
            @RequestParam(value = "season", required = false, defaultValue = "-1") long season,
            @RequestParam(value = "year", required = false, defaultValue = "-1") long year,
            @RequestParam(value = "sku_id", required = false, defaultValue = "-1") long skuId,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothes_num", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brand_name", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "sort", required = false, defaultValue = "1")int sortType,
			@RequestParam(value = "sale_status", required = false, defaultValue = "0")int saleStatus,
			@RequestParam(value = "parent_categoryid", required = false, defaultValue = "-1")int parentCategoryId,
			@RequestParam(value = "categoryid", required = false, defaultValue = "-1")int categoryId,
			@RequestParam(value = "sku_status", required = false, defaultValue = "0")int skuStatus,
    		@RequestParam(value = "remain_min", required = false, defaultValue = "-1") int remainCountMin,
    		@RequestParam(value = "remain_max", required = false, defaultValue = "-1") int remainCountMax,
    		@RequestParam(value = "validity", required = false, defaultValue = "-1") int validity,
    		@RequestParam(value = "type", required = false, defaultValue = "-1") int type,
    		@RequestParam(value = "warehouse_ids", required = false, defaultValue = "")String warehouseIdsStr,
    		@RequestParam(value = "deduct_percent", required = true)double deductPercent){
    	Map<String, Object> data = new HashMap<String, Object>();
		JsonResponse jsonResponse = new JsonResponse();
		
    	List<Long> productIdList =  new ArrayList<Long>();
    	//可能会查询出重复的productId 用 Set去重
    	HashSet<Long> productSet = new HashSet<>();
    	if(!TextUtils.isEmpty(productIdsStr)){
    		//根据Id
			productIdList = transToProductIds(productIdsStr);
			for (Long productId : productIdList) {
				productSet.add(productId);
			}
    	}else{
    		PageQuery pageQuery = new PageQuery(page, pageSize);
    		//全选的情况
    		if(page == 0){
    			pageQuery = null;
    		}
    		List<Long> warehouseIds = transWarehouseIdsToList(warehouseIdsStr);
            List<ProductSKUVO> skuvos = productSkuFacade.srchSkuInfo(pageQuery, id, season, year, name, clothesNumber, remainCountMin, remainCountMax, skuId, brandName ,sortType
        			, SaleStatus.getSql(saleStatus), parentCategoryId, categoryId, skuStatus, validity, type,warehouseIds,-2);
            for (ProductSKUVO productSKUVO : skuvos) {
            	productSet.add(productSKUVO.getProductVO().getProduct().getId());
			}
    	}
    	
    	try{
    		//将需要修改抵扣百分比的Id set转换成 List
    		ArrayList<Long> tagList = new ArrayList<>();
    		Iterator<Long> iterator = productSet.iterator();
    		while (iterator.hasNext()) {
				tagList.add(iterator.next());
			}
    		
    		for(Long item : tagList){
    			System.out.println(item+"");
    		}
    		
			if(tagList.size() == 0){
				return jsonResponse.setError("未找到符合条件的商品");
			}
			jiuCoinService.batchUpdateJiuCoinDeduction(tagList,deductPercent);
			return jsonResponse.setSuccessful();
		}catch (Exception e) {
			return jsonResponse.setError("修改玖币抵扣失败");
		}
    	
    }
    
    private List<Long> transToProductIds(String productIdsStr){
		List<Long> productIds = new ArrayList<Long>();
		if(StringUtils.isBlank(productIdsStr)){
			return productIds;
		}
		String[] split = productIdsStr.split(",");
		for(String item : split){
			try { 
				productIds.add(Long.parseLong(item));
			}catch (Exception e) {
				continue;
			}
		}
		return productIds;
	}
    
    /**
     * 
     * @param color 格式：例如 "414,313,344"
     * @param size  格式：例如 "414,313,344"
     * @return
     */
    @RequestMapping(value = "/sku/add", method = RequestMethod.POST)
    @ResponseBody
    @AdminOperationLog
    public JsonResponse addProductSku(@RequestParam(value = "name") String name,
    		@RequestParam(value = "clothes_num") String clothesNumber,
    		@RequestParam(value = "market_price") double marketPrice,
    		@RequestParam(value = "cost_price") double costPrice,
    		@RequestParam(value = "weight") double weight,
    		@RequestParam(value = "warehouse_id") long warehouseId,
    		@RequestParam(value = "remain_keep_time") int remainKeepTime,
    		@RequestParam(value = "brand_id") long brandId,
    		@RequestParam(value = "color") String color,
    		@RequestParam(value = "size") String size,
    		@RequestParam(value = "position", required = false, defaultValue = "") String position,
    		@RequestParam(value = "status", required = false, defaultValue = "-1") int status,
    		@RequestParam(value = "wholeSaleCash", required = false, defaultValue = "0") double wholeSaleCash) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
        List<Product> products = productService.getByClothesNums(CollectionUtil.createSet(clothesNumber));
        if (products.size() > 0) {
        	return jsonResponse.setSuccessful().setResultCode(ResultCode.PRODUCT_ERROR_EXSIT);
        }
        
    	try {
			productSkuFacade.addProductSku(name, clothesNumber, marketPrice, costPrice, weight, warehouseId, remainKeepTime, brandId, color, size, position, status,wholeSaleCash);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    
    /**
     * @param color 格式：例如 "414,313,344"
     * @param size  格式：例如 "414,313,344"
     * @return
     */
    @RequestMapping(value = "/sku/modify", method = RequestMethod.POST)
    @ResponseBody
    @AdminOperationLog
    public JsonResponse modifyProductSku(
    		@RequestParam(value = "clothes_num") String clothesNumber,
    		@RequestParam(value = "warehouse_id") long warehouseId,
    		@RequestParam(value = "remain_keep_time") int remainKeepTime,
    		@RequestParam(value = "brand_id") long brandId,
    		@RequestParam(value = "color") String color,
    		@RequestParam(value = "position", required = false, defaultValue = "") String position,
    		@RequestParam(value = "size") String size) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
        List<Product> products = productService.getByClothesNums(CollectionUtil.createSet(clothesNumber));
        if (products.size() <= 0) {
        	return jsonResponse.setSuccessful().setResultCode(ResultCode.PRODUCT_ERROR_NOT_EXSIT);
        }
    	
    	try {
			productSkuFacade.addProductSku("", clothesNumber, 0, 0, 0, warehouseId, remainKeepTime, brandId, color, size, position, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    /**
     * @param clothesNum
     * @return
     */
    @RequestMapping(value = "/sku/getSkus")
    @ResponseBody
    public JsonResponse getProductSkuByClothesNum(@RequestParam(value = "clothes_num") String clothesNum) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	long brandId = 0;
    	long productId = 0;
    	long LOWarehouseId = 0;
    	List<Map<String,Long>> propertyIds = new ArrayList<Map<String,Long>>();
    	List<Product> products = productService.getByClothesNums(CollectionUtil.createList(clothesNum));
    	if(products!=null && products.size()>0){
    		productId = products.get(0).getId();
    		brandId = products.get(0).getBrandId();
    		LOWarehouseId = products.get(0).getlOWarehouseId();
    	}
        List<ProductSKU> productSKUs = productSkuFacade.GetProductSkuByClothesNum(clothesNum);
        for (ProductSKU productSKU : productSKUs) {
        	propertyIds.add(productSKU.getPropertyNameMap());
		}
        
        data.put("productId", productId);
    	data.put("brandId", brandId);
    	data.put("lOWarehouseId", LOWarehouseId);
    	data.put("propertyIds", propertyIds);    	 
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/sku/update", method = RequestMethod.POST)
    @ResponseBody
    @AdminOperationLog
    public JsonResponse uptProductSku(@RequestParam(value = "id") long id, 
    		@RequestParam(value = "name") String name,
    		@RequestParam(value = "market_price") double marketPrice,
    		@RequestParam(value = "cost_price") double costPrice,
    		@RequestParam(value = "sort") int sort,
    		@RequestParam(value = "remain_count") int remainCount,
    		@RequestParam(value = "remain_count2", required = false, defaultValue = "0") int remainCount2,
    		@RequestParam(value = "remain_keep_time") int remainKeepTime,
    		@RequestParam(value = "skuno") long skuNo,
    		@RequestParam(value = "weight") double weight,
    		@RequestParam(value = "position", required = false, defaultValue = "") String position,
    		@RequestParam(value = "remain_count_lock", required = false, defaultValue = "0") int remainCountLock,
    		@RequestParam(value = "remain_count_start_time", required = false, defaultValue = "") String remainCountStartTime,
    		@RequestParam(value = "remain_count_end_time", required = false, defaultValue = "") String remainCountEndTime,
    		@RequestParam(value = "is_remain_count_lock", required = false, defaultValue = "0") int isRemainCountLock) {
        JsonResponse jsonResponse = new JsonResponse();
        
        long endTimeL = 0L;
    	long startTimeL = 0L;
    	try {
			if(StringUtils.equals(remainCountEndTime, "")) {
				endTimeL = 0L;
			} else {
				endTimeL = DateUtil.convertToMSEL(remainCountEndTime);
			}
			
			if(StringUtils.equals(remainCountStartTime, "")) {
				startTimeL = 0L;
			} else {
				startTimeL = DateUtil.convertToMSEL(remainCountStartTime);
			}
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + remainCountStartTime + " endTime:" + remainCountEndTime);
		}

        productSkuFacade.uptProductSku(id, name, marketPrice, costPrice, weight, position, sort, remainCount, remainCount2, remainKeepTime, skuNo, remainCountLock, startTimeL, endTimeL, isRemainCountLock);

        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }
	
    /**
     * @param clothesNum
     * @param type 0：颜色限制尺寸；1:尺寸限制颜色
     * @param colorId
     * @return
     */
    @RequestMapping(value = "/sku/restrict")
    @ResponseBody
    public JsonResponse skuRestrict(@RequestParam(value = "clothes_num") String clothesNum, 
                                    @RequestParam(value = "type") int type,
                                    @RequestParam(value = "attr_id") long attrId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
        List<Long> propertyMap = productSkuFacade.existProperties(clothesNum, type, attrId);
    	
    	data.put("existPropertyMap", propertyMap);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
	/**
	 * 根据产品ID返回产品的sku信息
	 * 
	 * @param productId
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("/getproductsku")
    @AdminOperationLog
	public String getProductSKUS(@RequestParam("productid") long productId, 
			@RequestParam(value = "page", defaultValue = "1", required = false) int page,
			ModelMap modelMap) {
		List<ProductSKUVO> skus = productService.getProductSKUsByProduct(productId);
		Product product = productService.getProductById(productId);
		
		int saleStatus = -3;
		for(ProductSKUVO skuVO : skus) {
			int status = skuVO.getStatus();
			if(status > saleStatus) {
				saleStatus = status;
			}
		}
		
		modelMap.put("product", product);
		modelMap.put("skus", skus);
		modelMap.put("page", page);
		modelMap.put("max_weight", productService.getMaxWeight());
		modelMap.put("years", getConfiguration(PropertyName.YEAR));
		modelMap.put("seasons", getConfiguration(PropertyName.SEASON));
		modelMap.put("brands", brandLogoService.getBrands());
		modelMap.put("status", saleStatus);
		
		return "page/backend/updatesku";
	}
	
	@RequestMapping("/attribution")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse getAttribution() {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		List<LOWarehouse> warehouses = lOWarehouseService.srchWarehouse(null, "", false);
		data.put("warehouses", warehouses);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 更新产品库存
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ResponseBody
    @AdminOperationLog
	public JsonResponse updateProductSKUS(HttpServletRequest request, @RequestParam("productid") long productid,
			@RequestParam("skus") String[] skus, ModelMap modelMap) {
		JsonResponse jsonResponse = new JsonResponse();
		
		productService.updateProductSKUsByProduct(productid, skus);

		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@RequestMapping("/excel/export")
	@ResponseBody 
    @AdminOperationLog
	public void excelExport(HttpServletResponse response) throws IOException {
		List<Map<String, Object>> list = productSkuFacade.excelOfSku();
		
        String columnNames[] =
            { "品牌", "款号", "颜色", "尺码", "SKU", "上架状态", "所在库房", "库存"};// 列名
        String keys[] = {"brandName" ,"clothesNum","color","size","sku","isOnsale", "warehouse", "remainCount"};//map中的key
        
        ExcelUtil.exportExcel(response, list, keys, columnNames, "productSku");
	}
	
	/**
	 * 根据Sku ID对产品进行下架
	 */
    @AdminOperationLog
	@RequestMapping("/soldoutproductSKUlist")
    @Deprecated
	public String soldoutProductList(HttpServletRequest request, String[] productSKUIds, ModelMap modelMap) {
		int success = -1;
		if (null != productSKUIds && productSKUIds.length != 0) {
			success = productSKUIds.length;
			for (String productSKUIdStr : productSKUIds) {
				long productSKUId = Long.parseLong(productSKUIdStr);
				productSkuFacade.soldoutProductSKUList(productSKUId);
			}
		}
		modelMap.put("success", success);
		return "json";
    }
    
	/**
	 * 根据Sku ID对产品进行下架
	 */
    @AdminOperationLog
	@RequestMapping("/ids/soldout")
    @ResponseBody
	public JsonResponse soldoutIds(HttpServletRequest request, String[] productSKUIds) {
    	JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<>();
    	int success = -1;
		if (null != productSKUIds && productSKUIds.length != 0) {
			success = productSKUIds.length; 
			for (String productSKUIdStr : productSKUIds) {
				long productSKUId = Long.parseLong(productSKUIdStr);
				productSkuFacade.soldoutProductSKUList(productSKUId);
			}
			
		}
		data.put("success", success);
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setCode(success);
    }
    
    /**
     * 推送sku
     */
    @RequestMapping(value = "/sku/sync")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse synchronization() {
    	JsonResponse jsonResponse = new JsonResponse();
    	List<Map<String, Object>> list = erpDelegator.getPushedProduct();
        if (list.size() < 1) {
            return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
        }
        
        Set<String> clothesNos = new HashSet<String>();
        erpDelegator.pushProducts(list, clothesNos);
        
        if(clothesNos.size() > 0) {
        	productSKUService.updatePushTime(clothesNos);
        }
    	
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    /**
     * sku库存同步到本地
     * @return
     */
    @RequestMapping(value = "/sku/count/sync")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse synchronizationCount() {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	erpDelegator.synchronizationCount();
    	
    	return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
	/**
	 * sku上架设置
	 * @return
	 */
	@RequestMapping(value = "/sku/sale")
	@ResponseBody
    @AdminOperationLog
	public JsonResponse setSKUSale(@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
			@RequestParam(value = "product_id", required = false, defaultValue = "") Long productId,
    		@RequestParam(value = "status") int status,
    		@RequestParam(value = "sale_start_time") long saleStartTime,
    		@RequestParam(value = "sale_end_time") long saleEndTime) {
		JsonResponse jsonResponse = new JsonResponse();
        Map<String, Object> data = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
		try {
            map = productSkuFacade.uptProductSku(id, productId, status, saleStartTime, saleEndTime);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
        data.put("detail", map);
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
	}
	
	/**
     * 
     * @param id - 商品id(款号id)
     * @param type 0: 停用， 1：恢复
     * @return
     */
    @RequestMapping(value = "/valid/update")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse update(@RequestParam(value = "sku_nos") String skuNos,
    		@RequestParam(value = "type") int type) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	try {
    		productSkuFacade.validUpdate(skuNos, type);
		} catch(ParameterErrorException e){
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		} catch (Exception e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError("未知错误");
		}
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    @AdminOperationLog
	@RequestMapping(value = "/promotion/count/update")
	@ResponseBody
	public JsonResponse promotionCountUpdate(@RequestParam("promotion_sale_count") Integer promotionSaleCount, 
			@RequestParam("promotion_visit_count") Integer promotionVisitCount, 
			@RequestParam("sku_id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		
		productSKUService.updatePromotionCount(id, promotionSaleCount, promotionVisitCount);
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
    
    /**
     * 根据type返回下拉列表 
     * @param type
     * @return 
     */
    @RequestMapping(value = "/property/list", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse propertyList(@RequestParam("type") int type, @RequestParam(value = "id", defaultValue = "0", required = false) long id) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	switch (type) {
    	case 0:    		
    		//所有列表
    		break;
    	case 1:    		
    		//年份
    		break;
    	case 2:break;
			//季节
    	case 3:break;
			//一级分类
		case 4:break;
			//二级分类
		case 5:break;
    	}

    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
	
    private CommonResponseObject<Object> getConfiguration(PropertyName propertyName) {
        Collection<ProductPropValue> propertyValues = propertyService.getPropertyByNameId(propertyName);
        CommonResponseObject<Object> resp = new CommonResponseObject<Object>();
        if (null != propertyValues && propertyValues.size() != 0) {
            resp.setResult(ResultCode.COMMON_SUCCESS, propertyValues);
        } else {
            resp.setResult(ResultCode.PROPERTY_NOT_FOUND, new ArrayList<ProductPropValue>());
        }
        return resp;
    }
    
    /**
     * 仓库列表 
     * @param type
     * @return 
     */
    @RequestMapping(value = "/lowarehouse/list", method = RequestMethod.GET)
    @ResponseBody
    public  JsonResponse lowarehouseList(@RequestParam(value = "name", required = false, defaultValue = "")String name){
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	List<LOWarehouse> warehouses = lOWarehouseService.srchWarehouse(null, name, false);
    	data.put("list", warehouses);
    	
    	return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(data);
    }
    
    /**
     * 获取(筛选)买手推荐列表
     * @param page	当前是第几页
     * @param pageSize	每页显示几条数据
     * @param id	款号Id
     * @param name	商品名称
     * @param clothesNumber	款号
     * @param brandName		品牌名称
     * @param skuStatus		0:全部 1:正常 2:已售罄 3:缺货(库存量小于等于10个为缺货状态)
     * @param sortType		0:价格升序 1:价格降序 2:日期升序 3:日期降序
     * @param parentCategoryId	一级分类Id
     * @param categoryId		二级分类Id
     * @param vip		VIP商品：-1全部商品、0非VIP商品、1VIP商品
     * @return
     */
    @RequestMapping(value = "/boutiqueproduct/search")
    @ResponseBody
    public JsonResponse searchBoutiqueProduct(@RequestParam(value = "page", required = false, defaultValue = "1")Integer page, 
			@RequestParam(value = "page_size", required = false, defaultValue = "10")Integer pageSize,
            @RequestParam(value = "id", required = false, defaultValue = "-1") Long id,
    		@RequestParam(value = "name", required = false, defaultValue ="") String name,
    		@RequestParam(value = "clothesNumber", required = false, defaultValue = "") String clothesNumber,
			@RequestParam(value = "brandName", required = false, defaultValue = "")String brandName, 
			@RequestParam(value = "skuStatus", required = false, defaultValue = "0")Integer skuStatus,
			@RequestParam(value = "sort", required = false, defaultValue = "-1")Integer sortType,
			@RequestParam(value = "parentCategoryId", required = false, defaultValue = "-1")Integer parentCategoryId,
			@RequestParam(value = "categoryId", required = false, defaultValue = "-1")Integer categoryId,
			@RequestParam( required = false, defaultValue = "-1")Integer vip) {
    	return productSkuFacade.searchBoutiqueProduct(page,pageSize,id,name,clothesNumber,brandName,skuStatus,parentCategoryId,categoryId,sortType,vip);
    }
    
    /**
     * 设置买手推荐/取消买手推荐
     * @param productIds	商品Id数组 例如:1961,1962,1964
     * @param status	状态:0：推荐，-1：取消推荐
     * @return
     */
    @RequestMapping(value = "/boutiqueproduct/set")
    @ResponseBody
    public JsonResponse setBoutiqueProduct(@RequestParam(value = "productIds")String productIds, 
			@RequestParam(value = "status")Integer status) {
    	return productSkuFacade.setBoutiqueProduct(productIds,status);
    }
    
    /**
     * 商品改价
     * @param productId		商品Id
     * @param price		商品批发价格
     * @return
     */
    @RequestMapping(value = "/boutiqueproduct/updatePrice")
    @ResponseBody
    public JsonResponse udpateBoutiqueProductPrice(@RequestParam(value = "productId")String productId, 
			@RequestParam(value = "price")Double price) {
    	return productSkuFacade.udpateBoutiqueProductPrice(productId,price);
    }
    
    /**
     * 买手推荐商品设置VIP商品
     * @param productId		商品Id
     * @param vip		VIP设置：0取消VIP，1设置VIP
     * @return
     */
    @RequestMapping(value = "/boutiqueproduct/setVip")
    @ResponseBody
    public JsonResponse udpateBoutiqueSetVip(@RequestParam(value = "productId")long productId, 
			@RequestParam(value = "vip")int vip,
			HttpServletRequest request) {
    	
    	HttpSession session = request.getSession();
		Long userId = Long.parseLong(session.getAttribute("userid").toString());
		AdminUser adminUser = adminUserDao.getUser(userId);
    	
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			int record = productSkuFacade.udpateBoutiqueSetVip(productId,vip,adminUser);
			if(record!=1){
				return jsonResponse.setError("失败");
			}
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError("失败");
		}
    }
    /**
     * 买手推荐商品更新商品上架时间
     * @return
     */
    @RequestMapping(value = "/boutiqueproduct/updateSaleStartTime")
    @ResponseBody
    public JsonResponse updateSaleStartTime(@RequestParam(value = "productId")String productId) {
    	logger.info("买手推荐商品更新商家上架时间,productId:"+productId);
    	return productSkuFacade.updateSaleStartTime(productId);
    }
    
    /**
     * 获取隐藏的分类列表
     */
    @RequestMapping(value = "/potentialCategory/search")
    @ResponseBody
    public JsonResponse getPotentialCategoryList(@RequestParam(value = "productId") String productId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	List<Map<String,Object>> data = categoryNewService.getPotentialCategoryList(productId);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 保存隐藏的分类列表
     */
    @AdminOperationLog
    @RequestMapping("/potentialCategory/update")
    @ResponseBody
    public JsonResponse updatePotentialCategory(@RequestParam("productId") String productId,
    		                                    @RequestParam("categoryIds") String categoryIds ){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		categoryNewService.updatePotentialCategory(productId,categoryIds);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
    }
   
}
