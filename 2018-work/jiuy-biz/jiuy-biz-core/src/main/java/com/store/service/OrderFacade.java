package com.store.service;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiuyuan.common.CouponRbRef;
import com.jiuyuan.dao.mapper.CommonRefMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.Logistics;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.OrderNewLogMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOPostage;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.ProductNew;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.order.ConsumeWrapper;
import com.jiuyuan.entity.order.OrderItemGroup;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.service.common.ILOWarehouseNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.store.entity.Brand;
import com.store.entity.OrderProduct;
import com.store.entity.ProductPropVO;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.entity.ShopCoupon;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemVO;
import com.store.enumerate.OrderType;
import com.store.service.brand.ShopBrandService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.entity.order.OrderDiscountLog;
import com.yujj.exception.order.CouponUnavailableException;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PayCheckNotEqualException;
import com.yujj.exception.order.ProductUnavailableException;
import com.yujj.exception.order.RemainCountLessException;

@Service
public class OrderFacade {
	
	private static final Log logger = LogFactory.get();
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private IProductNewService productNewService;
	
	
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	
	@Autowired
	private ShoppingCartFacade shoppingCartFacade;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private StoreBusinessServiceShop storeBusinessService;
	
	@Autowired
	private ShopBrandService shopBrandService;
	
	@Autowired
	private ILOWarehouseNewService loWarehouseService;
	
	@Autowired
	private ProductServiceShop productService;
	
	@Autowired
	private LOLocationService loLocationService;
	
	@Autowired
	private LOPostageService loPostageService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductPropAssemblerShop productPropAssembler;
	
	@Autowired
	private OrderNewLogMapper orderNewLogMapper;
	
	@Autowired(required = false)
    private List<OrderHandler> orderHandlers;
	
	@Autowired
	private ProductAssembler productAssembler;

	@Autowired
	private CommonRefMapper commonRefMapper;

	
	/**
     * 分类计算优惠价格
     * @param productSkus
     * @param productIds
     * @param productMap
     * @param skuCountMap
     * @param brandMap
     * @param orderDiscountLogs 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> calculateDiscountV185(List<ProductSKU> productSkus, Set<Long> productIds,
    		Map<Long, ProductVOShop> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long userId ,String version, String step) {
    	
    	return calculateDiscountV185( productSkus, productIds,
        		 productMap,  skuCountMap, brandMap,  orderDiscountLogs,  userId,  null, version, step);
    }
    
    /**
     * 分类计算优惠价格
     * @param productSkus
     * @param productIds
     * @param productMap
     * @param skuCountMap
     * @param brandMap
     * @param orderDiscountLogs 
     * @return
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> calculateDiscountV185(List<ProductSKU> productSkus, Set<Long> productIds,
    		Map<Long, ProductVOShop> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long storeId, String couponId, String version, String step) {
    	Map<String, Object> price = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> virtualGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> categoryGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	List<Map<String, Object>> normalGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	//新增字段,获取商品对应的虚拟分类,不存在则为null
    	Map<Long, ShopCategory> categoryMap = productCategoryService.getProductVirtualCategory(productIds);
    	List<ProductSKU> virtualProductSKUs = new ArrayList<ProductSKU>();
    	List<ProductSKU> normalProductSKUs = new ArrayList<ProductSKU>();
    	
    	Set<Long> virtualCategoryId = new HashSet<Long>();
    	Set<Long> brandIds = new HashSet<Long>();
    	
    	//市场总价
    	int originalPrice = 0;
    	
    	
    	//区分虚拟分类和普通分类的商品,放入不同的容器
    	for(ProductSKU productSKU : productSkus) {
    		long productId = productSKU.getProductId();
    		if(categoryMap.get(productId) != null) {
    			virtualProductSKUs.add(productSKU);
    		} else {
    			normalProductSKUs.add(productSKU);
    		}
    		
    		//统计市场总价
    		Product product = productMap.get(productSKU.getProductId());
    		int count = skuCountMap.get(productSKU.getId());
    		if(product.getMarketPriceMax() > 0) {
    			originalPrice += count * product.getMarketPriceMax(); // market price是元存储!!!
    		} else {
    			originalPrice += count * product.getMarketPrice(); // market price是元存储!!!
    		}
    		
    	}
    	
    	//全场满减数据
    	 List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
         DiscountInfo discountInfoTemp;
//         JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
         JSONArray jsonArrayDiscount = null;
         if(jsonArrayDiscount != null && jsonArrayDiscount.size() > 0){
         	
         	for(Object obj : jsonArrayDiscount) {
         		discountInfoTemp = new DiscountInfo();
         		discountInfoTemp.setFull(Double.parseDouble(((JSONObject)obj).get("full").toString()));
         		discountInfoTemp.setMinus(Double.parseDouble(((JSONObject)obj).get("minus").toString()));
         		allDiscountInfoList.add(discountInfoTemp);
         	}
         	
         }
    	
    	
    	//虚拟分类包装
    	for(ProductSKU virtualSKU: virtualProductSKUs) {
    		long productId = virtualSKU.getProductId();
    		Product product = productMap.get(virtualSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		int buyCount = skuCountMap.get(virtualSKU.getId());
    		itemMap.put("skuId", virtualSKU.getId());
    		itemMap.put("buyCount",buyCount );
    		itemMap.put("remainCount", virtualSKU.getRemainCount());
//    		itemMap.put("cash", product.getWholeSaleCash());
    		itemMap.put("cash", getLadderPrice(skuCountMap, virtualProductSKUs, productMap,productId));
    		itemMap.put("minLadderPrice", product.getMinLadderPrice());//最小阶梯价格
    		itemMap.put("maxLadderPrice", product.getMaxLadderPrice());//最大阶梯价格
    		itemMap.put("ladderPriceJson", product.getLadderPriceJson());//阶梯价格JSON
    		itemMap.put("supplierId", product.getSupplierId());//供应商ID
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		
    		ShopCategory virtualCat = categoryMap.get(virtualSKU.getProductId());
    		List<Map<String, Object>> itemList = categoryGroup.get(virtualCat.getId());
    		if (itemList == null) {
    			Map<String, Object> groupMap = new HashMap<String, Object>();
    			itemList = new ArrayList<Map<String, Object>>();
    			categoryGroup.put(virtualCat.getId(), itemList);
    			groupMap.put("itemList", itemList);
    			groupMap.put("category", virtualCat);
    			virtualGroupList.add(groupMap);
    			virtualCategoryId.add(virtualCat.getId());
    		}
    		itemList.add(itemMap);
    	}
    	
    	
    	
    	
    	//普通分类封装
    	for(ProductSKU normalSKU : normalProductSKUs) {
    		long productId = normalSKU.getProductId();
    		Product product = productMap.get(normalSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		int buyCount = skuCountMap.get(normalSKU.getId());
    		itemMap.put("skuId", normalSKU.getId());
    		itemMap.put("buyCount", buyCount);
    		itemMap.put("remainCount", normalSKU.getRemainCount());
//    		itemMap.put("cash", product.getWholeSaleCash());
    		//根据sku所属商品购买数量计算sku单价
//    		itemMap.put("cash", getLadderPrice(skuCountMap, normalProductSKUs, product));
    		itemMap.put("cash", getLadderPrice(skuCountMap, normalProductSKUs, productMap,productId));
    		itemMap.put("minLadderPrice", product.getMinLadderPrice());//最小阶梯价格
    		itemMap.put("maxLadderPrice", product.getMaxLadderPrice());//最大阶梯价格
    		itemMap.put("ladderPriceJson", product.getLadderPriceJson());//阶梯价格JSON
    		itemMap.put("supplierId", product.getSupplierId());//供应商ID
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		
    		Brand brand = brandMap.get(product.getBrandId());
    		List<Map<String, Object>> itemList = brandGroup.get(brand.getBrandId());
    		if (itemList == null) {
    			Map<String, Object> groupMap = new HashMap<String, Object>();
    			itemList = new ArrayList<Map<String, Object>>();
    			brandGroup.put(brand.getBrandId(), itemList);
    			groupMap.put("itemList", itemList);
    			groupMap.put("brand", brand);
    			normalGroupList.add(groupMap);
    			brandIds.add(brand.getBrandId());
    		}
    		itemList.add(itemMap);
    	}
    	
    	double totalCash = 0;
    	double totalMoney = 0;//item总价
    	int totalJiuCoin = 0;
    	//不打折应付
    	double prepay = 0;
    	//优惠值
    	double discountCash = 0;
    	
    	
    	//首单优惠 门店暂时没有
//    	double firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
    	double firstDiscountCash = 0;
    	
    	boolean couponConflict = false;
    	boolean otherCouponFlag = false;
    	int couponCoExist = 0;

    	List<ShopCoupon> couponList = new ArrayList<ShopCoupon>();
    	if(couponId != null && couponId.length() > 0 ) {


    		couponList = orderService.getCouponByIdArr(couponId, storeId);
    		for(ShopCoupon couponTemp : couponList){
    			if(couponTemp != null &&  couponTemp.getCoexist() == 0){
    				couponCoExist ++;
        		}
    			if(couponTemp != null &&  couponTemp.getIsLimit() == 1 && couponTemp.getStatus() == 0){
        			couponConflict = true;
        		}else if(couponTemp == null ||  couponTemp.getStatus() != 0){
        			throw new CouponUnavailableException();
        		}
    			 if( couponTemp.getRangeType() != 5){
    				 otherCouponFlag = true;
    			 }
    			

    		}
    		if(couponCoExist >= 2){
    			throw new CouponUnavailableException();
    		}
    		//待校验优惠券类型使用todo
    		
    		JSONObject couponLimitJson = globalSettingService.getJsonObjectNoCache(GlobalSettingName.STORE_COUPON_LIMIT_SET);
            List<String> limitIdsList = new ArrayList<String>();
            if(couponLimitJson.get("enable") != null && couponLimitJson.get("start_time") != null && couponLimitJson.get("end_time") != null && couponLimitJson.get("category_ids") != null){
            	SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
            	long nowTime = System.currentTimeMillis();
            	long startTime = 0;
            	long endTime = 0;
            	String limitCategoryIds = couponLimitJson.get("category_ids").toString().replace("[", "").replace("]", "");
            	try {
    				startTime = sdf.parse(couponLimitJson.get("start_time").toString()).getTime();
    				endTime = sdf.parse(couponLimitJson.get("end_time").toString()).getTime();
    			} catch (ParseException e) {
    			
    				e.printStackTrace();
    			}
            	//如果全部分类限制的话，返回空列表
            	if(couponLimitJson.get("enable").equals("YES") && startTime < nowTime && nowTime < endTime  ){
            		if(limitCategoryIds.equals("0") && otherCouponFlag){
            			throw new CouponUnavailableException();
            		} else if(limitCategoryIds.length() > 0 ){
            			//4月20号注释，暂时只有满减券@@@@
//            				String[] idsArr = limitCategoryIds.split(",");
//            					//子分类匹配
//            					List<Category> categorieListAll = categoryService.getCategories();
//            					for(String ctgrId : idsArr){
//            						limitIdsList.add(ctgrId);
//            					}
//            					//添加子分类到列表
//            					for(Category category : categorieListAll){
//            						//添加子分类
//            						for(Category categoryTemp : categorieListAll){
//            							if(categoryTemp.getParentId() == category.getId()){
//            								category.getChildCategories().add(categoryTemp);
//            							}
//            						}
//            						for(String ctgrId : idsArr){
//            							if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
//            								for(Category childCtgy : category.getChildCategories()){
//            									limitIdsList.add(childCtgy.getId() + "");
//            								}
//            							}
//            						}
//            					}
            			}
            	}
            	if(limitIdsList != null && limitIdsList.size() > 0){
            		
            		List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
            		if(productCategoryList != null && productCategoryList.size() > 0 ){	
            			
            			for(String id : limitIdsList){
            				//System.out.println("id：" + id);
            				for (ProductCategory productCategory : productCategoryList) {
            					if((productCategory.getCategoryId() + "" ).equals(id) && otherCouponFlag){
            						throw new CouponUnavailableException();
            					}	
            				}
            				for (Product product : productMap.values()) {
            					if((product.getvCategoryId() + "").equals(id) && otherCouponFlag){
            						throw new CouponUnavailableException();
            					}
            				}
            			}
            		}
            	}
            }
    		
    	}
    	
    	//取复合优惠信息
    	Map<Long, List<DiscountInfo>> discountInfoMapCat =new HashMap<Long, List<DiscountInfo>>();
    	if(virtualCategoryId.size() > 0 && allDiscountInfoList.size() == 0 && !couponConflict){
    		discountInfoMapCat = shoppingCartFacade.queryDiscountInfoListMap(0, virtualCategoryId);  		
    	}
    	Map<Long, List<DiscountInfo>> discountInfoMapBrand = new HashMap<Long, List<DiscountInfo>>();
    	if(brandIds != null && brandIds.size()>0 && allDiscountInfoList.size() == 0 && !couponConflict){
    		discountInfoMapBrand = shoppingCartFacade.queryDiscountInfoListMap(1, brandIds);	
    	}
    	List<DiscountInfo> discountInfoList;
    	
    	
    	//虚拟分类商品的价格统计
    	for(Map<String, Object> virtualGroupMap: virtualGroupList) {
    		ShopCategory category = (ShopCategory)virtualGroupMap.get("category");
    		
    		List<Map<String, Object>> itemList = (List<Map<String, Object>>)virtualGroupMap.get("itemList");
    		int buyCount = 0;
    		double subTotalCash = 0;
    		for(Map<String, Object> itemMap : itemList) {
    			//如果购买件数超出库存数
    			if((int)itemMap.get("buyCount") > (int)itemMap.get("remainCount")) {
    				throw new RemainCountLessException();
    			}
    			buyCount = (int)itemMap.get("buyCount");
    			prepay += buyCount * (double)itemMap.get("cash");
    			subTotalCash += buyCount * (double)itemMap.get("cash");
    			totalMoney += buyCount * (double)itemMap.get("cash");
    			totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    		}
    		//旧虚拟分类满减
//    		if(category.getIsDiscount() == 1 && subTotalCash >= category.getExceedMoney()) {
//    			subTotalCash -= category.getMinusMoney(); 
//    			discountCash += category.getMinusMoney(); 
//    			
//    			if(orderDiscountLogs != null) {
//    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
//    				orderDiscountLog.setComment("满" + category.getExceedMoney() + "减" + category.getMinusMoney());
//    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
//    				orderDiscountLog.setType(1);
//    				orderDiscountLog.setDiscount(category.getMinusMoney());
//    				orderDiscountLog.setRelatedId(category.getId());
//    				
//    				orderDiscountLogs.add(orderDiscountLog);
//    			}
//    		}
    		//新虚拟分类满减@
    		double maxFull = 0;
    		double minus = 0;
    		long discountId = 0;
    		discountInfoList = new ArrayList<DiscountInfo>();
    		discountInfoList = discountInfoMapCat.get(category.getId());
    		if(discountInfoList != null && discountInfoList.size() > 0){
    			
//    			for(DiscountInfo discountInfo : discountInfoList){
//    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
//    					maxFull = discountInfo.getFull();
//    					minus = discountInfo.getMinus();
//    					discountId = discountInfo.getId();
//    				}
//    			}
    			subTotalCash -= minus; 
    			discountCash += minus; 
    			
    			if(orderDiscountLogs != null) {
    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    				orderDiscountLog.setComment("满" + maxFull + "减" + minus);
    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
    				orderDiscountLog.setType(0);
    				orderDiscountLog.setDiscount(minus);
    				orderDiscountLog.setRelatedId(discountId);
    				
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    		}
    		
    		
    		totalCash += subTotalCash;
    	}
    	
    	//普通商品的价格统计
    	for(Map<String, Object> normalGroupMap : normalGroupList) {
    		Brand brand = (Brand)normalGroupMap.get("brand");
    		
    		List<Map<String, Object>> itemList = (List<Map<String, Object>>)normalGroupMap.get("itemList");
    		int buyCount = 0;
    		double subTotalCash = 0;
    		for(Map<String, Object> itemMap : itemList) {
    			//如果购买件数超出库存数
    			if((int)itemMap.get("buyCount") > (int)itemMap.get("remainCount")) {
    				throw new RemainCountLessException();
    			}
    			buyCount = (int)itemMap.get("buyCount");
    			prepay += buyCount * (double)itemMap.get("cash");
    			subTotalCash += buyCount * (double)itemMap.get("cash");
    			totalMoney += buyCount * (double)itemMap.get("cash");
    			totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    		}
    		//旧品牌满减
//    		if(brand.getIsDiscount() == 1 && subTotalCash >= brand.getExceedMoney()) {
//    			subTotalCash -= brand.getMinusMoney(); 
//    			discountCash += brand.getMinusMoney(); 
//    			
//    			if(orderDiscountLogs != null) {
//    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
//    				orderDiscountLog.setComment("满" + brand.getExceedMoney() + "减" + brand.getMinusMoney());
//    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
//    				orderDiscountLog.setType(0);
//    				orderDiscountLog.setDiscount(brand.getMinusMoney());
//    				orderDiscountLog.setRelatedId(brand.getId());
//    				
//    				orderDiscountLogs.add(orderDiscountLog);
//    			}
//    		}
    		
    		//新品牌满减@
    		double maxFull = 0;
    		double minus = 0;
    		long discountId = 0;
    		discountInfoList = new ArrayList<DiscountInfo>();
    		discountInfoList = discountInfoMapBrand.get(brand.getBrandId());
    		if(discountInfoList != null && discountInfoList.size() > 0){
    			
//    			for(DiscountInfo discountInfo : discountInfoList){
//    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
//    					maxFull = discountInfo.getFull();
//    					minus = discountInfo.getMinus();
//    					discountId = discountInfo.getId();
//    				}
//    			}
    			subTotalCash -= minus; 
    			discountCash += minus; 
    			
    			if(orderDiscountLogs != null) {
    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    				orderDiscountLog.setComment("满" + maxFull + "减" + minus);
    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
    				orderDiscountLog.setType(1);
    				orderDiscountLog.setDiscount(minus);
    				orderDiscountLog.setRelatedId(discountId);
    				
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    		}
    		
    		totalCash += subTotalCash;
    		
    	}
    	
    	
    	//计算全场满减优惠金额
//    	if(allDiscountInfoList != null && allDiscountInfoList.size() > 0 && !couponConflict){
//    		double maxFull = 0;
//    		double minus = 0;
//    		
//			for(DiscountInfo discountInfo : allDiscountInfoList){
//				if(totalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
//					maxFull = discountInfo.getFull();
//					minus = discountInfo.getMinus();
//					//discountId = discountInfo.getId();
//				}
//			}
//			
//			if(minus < totalCash ){
//    			totalCash -= minus;
//    			discountCash += minus; 
//    			
//    		}else{
//    			minus = totalCash;
//    			totalCash = 0.0;
//    			discountCash += minus; 
//    		}
//			if(orderDiscountLogs != null) {
//				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
//				orderDiscountLog.setComment("全场满" + maxFull + "减" + minus);
//				orderDiscountLog.setCreateTime(System.currentTimeMillis());
//				orderDiscountLog.setType(3);
//				orderDiscountLog.setDiscount(minus);
//				//orderDiscountLog.setRelatedId(discountId);
//				
//				orderDiscountLogs.add(orderDiscountLog);
//			}
//		}
    	
    	//计算优惠券减扣
    	double couponCash = 0;

    	for(ShopCoupon coupon : couponList){
    		
    		//免邮券
    		if (coupon != null &&  coupon.getRangeType() == 5 && coupon.getStatus() == 0 ){
    			//免邮标记
    	    	price.put("postFreeFlag", "YES");
    		}
    		
    		if (coupon != null &&  coupon.getMoney() > 0 && coupon.getStatus() == 0 ){
    			Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
    			//计算优惠券实际优惠金额
    			coupon.setActualDiscount(coupon.getMoney());

//    		if(coupon.getRangeType() != null && coupon.getRangeType() == 0){
//    			coupon.setActualDiscount(coupon.getMoney());
//    			
//    		}else
    			if(coupon.getRangeType() >= -1){
    				if( coupon.getRangeType() == 2 && coupon.getLimitMoney() > 0 ){
						if(coupon.getLimitMoney() <= prepay){
							coupon.setActualDiscount(coupon.getMoney());
						}else{
							coupon.setActualDiscount(0);
						}
					}else if(coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
    					String limitIds = "";
    					String regEx = "\\[[^}]*\\]";
    					String str = coupon.getRangeContent(); //coupon.getRangeContent()
    					Pattern pat = Pattern.compile(regEx);
    					Matcher mat;
    					mat = pat.matcher(str);
    					while (mat.find()) {
    						//System.out.println(mat.group());
    						limitIds = mat.group();
    					}
    					limitIds = limitIds.replaceAll("\\[", "").replaceAll("\\]", "");
    					double actualDiscount = 0;
    					
    					if(limitIds.length() > 0 ){
    						
    						String[] idsArr = limitIds.split(",");
    						if( coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
    							
    							List<String> idsList = new ArrayList<String>();
    							//子分类匹配
    							List<ShopCategory> categorieListAll = shopCategoryService.getCategories();
    							for(String ctgrId : idsArr){
    								idsList.add(ctgrId);
    							}
    							//添加子分类到列表  
    							for(ShopCategory category : categorieListAll){
    								//添加子分类
    								for(ShopCategory categoryTemp : categorieListAll){
    									if(categoryTemp.getParentId() == category.getId()){
    										category.getChildCategories().add(categoryTemp);
    									}
    								}
    								for(String ctgrId : idsArr){
    									if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
    										for(ShopCategory childCtgy : category.getChildCategories()){
    											idsList.add(childCtgy.getId() + "");
    										}
    									}
    								}
    							}
    							
    							//计算实际优惠折扣  商品与品类是多对多关系
    							//List<Long> categoryIdList =  productCategoryService.getCategoryIdsAll(productIds);
    							List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
    							if(productCategoryList != null && productCategoryList.size() > 0 ){
    								String productIdStr = "";
    								for(String id : idsList){
    									for (ProductCategory productCategory : productCategoryList) {
    										if((productCategory.getCategoryId() + "" ).equals(id) && productIdStr.indexOf("," + productCategory.getProductId() + ",") == -1){
    											actualDiscount += productCountMap.get(productCategory.getProductId()) * productMap.get(productCategory.getProductId()).getWholeSaleCash();
    											productIdStr += "," + productCategory.getProductId() + ",";
    										}
    									}
    									
    								}
    								
    								//虚拟分类匹配
    								for(String arr : idsArr){
        								for (Product product : productMap.values()) {
        									if((product.getvCategoryId() + "").equals(arr)){
        										if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
        											actualDiscount += productCountMap.get(product.getId()) * product.getWholeSaleCash();
        											productIdStr += "," + product.getId() + ",";
        										}
        										
        									}
        								}
        							}
    							}
    							
    						}else if( coupon.getRangeType() == 2 && idsArr != null && idsArr.length  == 1){
    							if(Double.parseDouble(limitIds) <= prepay){
    								coupon.setActualDiscount(coupon.getMoney());
    							}else{
    								coupon.setActualDiscount(0);
    							}
    						}else if( coupon.getRangeType() == 4 && idsArr != null && idsArr.length > 0){
    							for(String arr : idsArr){
    								
    								for (Product product : productMap.values()) {
    									if((product.getBrandId() + "").equals(arr)){
    										if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
    											actualDiscount += productCountMap.get(product.getId()) * product.getWholeSaleCash();
    										}
    										
    									}
    									
    								}
    								
    							}
    						}
    						if( (coupon.getRangeType() == 1 || coupon.getRangeType() == 4) && idsArr != null && idsArr.length > 0){
	    						//限额优惠券检验使用金额是否达标
								if(coupon.getLimitMoney() > 0 && actualDiscount < coupon.getLimitMoney() ){
									actualDiscount = 0;
									System.out.println("优惠券使用异常：user:" + storeId + ",couponId:" + coupon.getId() );
								}else if(coupon.getMoney() < actualDiscount){
									actualDiscount = coupon.getMoney();
								}
								//System.out.println("优惠券使用:" + actualDiscount);
								coupon.setActualDiscount(actualDiscount);
    						}
    					}
    				}
    			}

    			
    			double subCouponCash = coupon.getActualDiscount();
    			if(subCouponCash < totalCash ){
    				totalCash -= subCouponCash;
    				
    			}else{
    				subCouponCash = totalCash;
    				totalCash = 0.0;
    			}
    			couponCash += subCouponCash;
    			if(orderDiscountLogs != null && coupon.getMoney() > 0) {
    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    				orderDiscountLog.setComment("优惠券优惠" + subCouponCash);
    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
    				orderDiscountLog.setType(4);
    				orderDiscountLog.setDiscount(coupon.getMoney());
    				orderDiscountLog.setRelatedId(storeId);
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    			//int updtNum = orderService.updateCouponUsed(coupon.getId());
    		}
    	}
	
    	
    	
    	 
    	//计算首单优惠金额 暂时不用
//    	int count = orderService.getUserOrderCountForFirstDiscount(storeId);
    	int count = 1;
    	if (count == 0 && totalCash > 0 ){
    		if(firstDiscountCash < totalCash ){
    			totalCash -= firstDiscountCash;
    			
    		}else{
    			firstDiscountCash = totalCash;
    			totalCash = 0.0;
    		}
    		if(orderDiscountLogs != null && firstDiscountCash > 0) {
    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    			orderDiscountLog.setComment("首单优惠" + firstDiscountCash);
    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
    			orderDiscountLog.setType(2);
    			orderDiscountLog.setDiscount(firstDiscountCash);
    			orderDiscountLog.setRelatedId(storeId);
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}else {
    		firstDiscountCash = 0.0;
    		
    	}
    	
    	
    	StoreBusiness storeBusiness = storeBusinessService.getById(storeId);
    	if (storeBusiness != null) {
    		//总收益
    		double totalCommission = totalCash * storeBusiness.getCommissionPercentage() / 100;
    		
//    		if (version.compareTo("10.0.2") >= 0) {
//        		
//    			if(totalCommission > 0 ){
//    				totalCash -= totalCommission;
//    				if(orderDiscountLogs != null && step != null && step.equals("confirm")) {
//    					OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
//    					orderDiscountLog.setComment("收益抵扣" + firstDiscountCash);
//    					orderDiscountLog.setCreateTime(System.currentTimeMillis());
//    					orderDiscountLog.setType(4);
//    					orderDiscountLog.setDiscount(totalCommission);
//    					orderDiscountLog.setRelatedId(storeId);
//    					orderDiscountLogs.add(orderDiscountLog);
//    				}
//    			}
//        	}
    		price.put("totalCommission", String.format("%.2f", totalCommission));
    		
    	}
    	
    
    	
    	//市场总价
    	price.put("originalPrice", originalPrice);
    	//优惠券优惠金额
    	price.put("couponCash", couponCash);

    	//不打折的俞姐姐APP总价(不包含邮费)
    	price.put("prepay", prepay);
    	//打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	price.put("totalCash", totalCash);
    	//不打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	price.put("totalMoney", totalMoney);
    	//比市场参考价便宜了多少,玖币抵扣
    	price.put("jiuCoinDiscount", originalPrice - totalCash);
    	//满减活动折扣的金额数目，优惠抵扣
    	price.put("discountCash", discountCash);	
    	if(firstDiscountCash > 0){
    		//首单优惠活动折扣的金额数目，优惠抵扣
    		price.put("firstDiscountCash", firstDiscountCash);	
    		
    		price.put("firstDiscountAble", "YES");
    	}else {
    		price.put("firstDiscountCash", 0.0);	
    		price.put("firstDiscountAble", "NO");
    	}
    	//所需的玖币价
    	price.put("totalJiuCoin", totalJiuCoin);
    	
    	return price;
    }
   
		

	/**
     * 根据SKU所属商品的购买件数获取该sku的阶梯单价
     * @param skuCountMap
     * @param normalProductSKUs
     * @param product
     * @return
     */
    private double getLadderPrice(Map<Long, Integer> skuCountMap, List<ProductSKU> productSKUs,
		Map<Long, ProductVOShop> productMap, long productId) {
		Product product = productMap.get(productId);
		
		int productBuyCount = 0;
		for(ProductSKU skuObj : productSKUs) {
			long skuProductId = skuObj.getProductId();
			if(skuProductId == product.getId()){
				int sukBuyCount = skuCountMap.get(skuObj.getId());
				productBuyCount = productBuyCount + sukBuyCount;
			}
		}
		String ladderPriceJson = product.getLadderPriceJson();
		return ProductNew.buildCurrentLadderPriceByBuyCount(ladderPriceJson,productBuyCount);
	}
    
    private Map<Long, Integer> getProductCountMap(Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus) {
    	long lastProductId = productSkus.get(0).getProductId();
        int buyCount = 0;
        Map<Long, Integer> productCountMap = new HashMap<Long, Integer>();
        
        for (ProductSKU sku : productSkus) {
            if(sku.getProductId() != lastProductId) {
            	productCountMap.put(lastProductId, buyCount);
            	
            	lastProductId = sku.getProductId();
            	buyCount = 0;
            }
            buyCount += skuCountMap.get(sku.getId());
        }
        productCountMap.put(lastProductId, buyCount);
        
        return productCountMap;
	}
    
    public List<Map<String, Object>> wrapperGroupList(List<ProductSKU> productSkus, Map<Long, ProductVOShop> productMap,
            Map<Long, Integer> skuCountMap,
            Map<Long, List<ProductPropVO>> skuPropMap) {
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		Map<Long, List<Map<String, Object>>> warehouseGroup = new HashMap<Long, List<Map<String, Object>>>();
		Map<Long, List<ProductVOShop>> warehouseGroup1 = new HashMap<Long, List<ProductVOShop>>();
		
		Map<Long, ProductVOShop> productMaps = new HashMap<Long, ProductVOShop>();
		for (ProductSKU sku : productSkus) {
			ProductVOShop product = productMap.get(sku.getProductId());
			
			Map<String, Object> itemMap = new HashMap<String, Object>();
			Map<String, Object> skuMap = new HashMap<String, Object>();
			int buyCount = skuCountMap.get(sku.getId());
			skuMap.put("id", sku.getId());
			skuMap.put("skuId", sku.getId());
			skuMap.put("buyCount", buyCount);
//			skuMap.put("price", sku.getPrice());
			skuMap.put("price", product.getWholeSaleCash());
			skuMap.put("currentLadderPriceByBuyCount", ProductNew.buildCurrentLadderPriceByBuyCount(product.getLadderPriceJson(),buyCount));
			skuMap.put("minLadderPrice", product.getMinLadderPrice());//最小阶梯价格
			skuMap.put("maxLadderPrice", product.getMaxLadderPrice());//最大阶梯价格
			skuMap.put("ladderPriceJson", product.getLadderPriceJson());//阶梯价格JSON
			skuMap.put("supplierId", product.getSupplierId());//供应商ID
			if(product.getSetLOWarehouseId2() == 1 && skuCountMap.get(sku.getId()) <= sku.getRemainCount2()){
				skuMap.put("remainCount", sku.getRemainCount2());
			}else{
				skuMap.put("remainCount", sku.getRemainCount());   	
			}
			itemMap.put("sku", skuMap);
			itemMap.put("productId", product.getId());
//			itemMap.put("product", product.toSimpleMap15());
			itemMap.put("product", product.toSimpleMap15(buyCount));
			itemMap.put("brand", shopBrandService.getBrand(product.getBrandId()).toSimpleMap());
		
			String color = "";
			String size = "";
			if (skuPropMap != null) {
				List<ProductPropVO> skuProps = skuPropMap.get(sku.getId());
				skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
				for (ProductPropVO skuProp : skuProps) {
					if (skuProp.getPropName().getPropertyName().equals("颜色"))
						color = skuProp.getPropValue().getPropertyValue();
			
			
					if (skuProp.getPropName().getPropertyName().equals("尺码"))
						size = skuProp.getPropValue().getPropertyValue();
				}
			}
		
			List<ProductVOShop> productList = warehouseGroup1.get(product.getlOWarehouseId());
		
			List<Map<String, Object>> itemList = warehouseGroup.get(product.getlOWarehouseId());
		
			if (itemList == null) {
				Map<String, Object> groupMap = new HashMap<String, Object>();
				itemList = new ArrayList<Map<String, Object>>();
				warehouseGroup.put(product.getlOWarehouseId(), itemList);
				productList = new ArrayList<ProductVOShop>();
				warehouseGroup1.put(product.getlOWarehouseId(), productList);
				groupMap.put("itemList", itemList);
				groupMap.put("productList", productList);
				if(product.getSetLOWarehouseId2() == 1 && skuCountMap.get(sku.getId()) <= sku.getRemainCount2()){
					groupMap.put("warehouse", loWarehouseService.getById(product.getlOWarehouseId2()));    	
				}else{
					groupMap.put("warehouse", loWarehouseService.getById(product.getlOWarehouseId()));
				}
				groupList.add(groupMap);
			}
		
			if (productMaps.containsKey(product.getId())) {
				ProductVOShop productVO = productMaps.get(product.getId());
				productVO.getSkuList().add(skuMap);
				productVO.getColors().add(color);
				productVO.getSizes().add(size);
				int count = productVO.getCount()+skuCountMap.get(sku.getId());
				productVO.setCount(count);
				productVO.setCurrentLadderPriceByBuyCount(ProductNew.buildCurrentLadderPriceByBuyCount(productVO.getLadderPriceJson(),count));
			} else {
				List<Map<String, Object>> skuList = new ArrayList<Map<String, Object>>();
				skuList.add(skuMap);
				
				Set<String> colorSet = new HashSet<String>();
				colorSet.add(color);
				Set<String> sizeSet = new HashSet<String>();
				sizeSet.add(size);
				product.setSkuList(skuList);
				product.setColors(colorSet);
				product.setSizes(sizeSet);
				product.setCount(skuCountMap.get(sku.getId()));
				product.setCurrentLadderPriceByBuyCount(ProductNew.buildCurrentLadderPriceByBuyCount(product.getLadderPriceJson(),buyCount));
				product.setBrand(shopBrandService.getBrands().get(product.getBrandId()).toSimpleMap());
				productMaps.put(product.getId(), product);
				productList.add(productMaps.get(product.getId()));
			}
		
			itemList.add(itemMap);
		}
		return groupList;
	}
    
    private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
        StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProps) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }
    
    /**
	 * 计算0元限购
	 * @return
	 */
    public int calculateZeroBuy(UserDetail userDetail, Set<Long> productIds, List<ProductSKU> productSkus) {


        Map<Long, ProductVOShop> productMap = productService.getProductMap(productIds);

        int dayZeroCount = 0;
        int curZeroCount = 0;
        //获取用户当天的0元购买量
        dayZeroCount = productService.getZeroBuyerLog(userDetail.getId());
        
    	for(ProductSKU productSKU : productSkus) {
    		
    		//统计市场总价
    		Product product = productMap.get(productSKU.getProductId());

            if (product.getWholeSaleCash() < 1.0) curZeroCount++;
            
    	}
    	
    	return curZeroCount == 0 ? curZeroCount : dayZeroCount + curZeroCount;
	}

	/**
     * 计算月度0元限购
     * @return
     */
    public int calculateZeroBuyMonthly(UserDetail userDetail, Set<Long> productIds, List<ProductSKU> productSkus) {
    	
    	
    	Map<Long, ProductVOShop> productMap = productService.getProductMap(productIds);
    	
    	int monthlyZeroCount = 0;
    	int curZeroCount = 0;
    	//获取用户当月的0元购买量
    	monthlyZeroCount = productService.getZeroBuyerMonthly(userDetail.getId());
    	
    	for(ProductSKU productSKU : productSkus) {
    		
    		//统计市场总价
    		Product product = productMap.get(productSKU.getProductId());
    		
    		if (product.getWholeSaleCash() < 1.0) curZeroCount++;
    		
    	}
    	
    	return curZeroCount == 0 ? curZeroCount : monthlyZeroCount + curZeroCount;
    }

	/**
     * 计算组合限购
     * @return
     */
    public Map<String, Object> calculateUserRestrictBuy(long userId ,Set<Long> productIds, Map<Long, ProductVOShop> productMap,List<ProductSKU> productSkus, Map<Long, Integer> skuCountMap ) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	boolean result = false;
    	String restrictProductName = "";
    	int periodCount = 0;
    	int dayCount = 0;
    	int buyNum = 0;
    	long restrictId = 0;
    	RestrictionCombination restrict;
    	Set<Long> restrictIdSet = new HashSet<Long>();
    	
    	for(Map.Entry<Long, ProductVOShop> product : productMap.entrySet()){
    		restrictIdSet.add(product.getValue().getRestrictId());
    	}
    	Map<Long, RestrictionCombination> restrictMap = productService.getRestrictByIdSet(restrictIdSet);
    	
    	if(restrictMap != null && restrictMap.size() > 0){
    		for(Map.Entry<Long, ProductVOShop> product : productMap.entrySet()){
    			buyNum = 0;
    			//统计订单中商品购买数
    			for(ProductSKU productSKU : productSkus){
    				if(productSKU.getProductId() == product.getKey()){
    					buyNum += skuCountMap.get(productSKU.getId());
    				}		
    			}
    			restrictId = product.getValue().getRestrictId();
    			if(restrictId > 0){
    				restrict = restrictMap.get(restrictId);
    				if(restrict != null){
    					if(restrict.getHistorySetting() == 1 && System.currentTimeMillis() > restrict.getHistoryStartTime() && restrict.getHistoryBuy() > 0){
    						
    						//获取用户历史组合限购数量
    						periodCount = productService.getUserRestrictBuy(userId, restrictId, restrict.getHistoryCycle() );
    						if(periodCount + buyNum > restrict.getHistoryBuy()){
    							result = true;
    							restrictProductName = product.getValue().getName();
    							break;
    						}
    						
    					}
    					if(restrict.getDaySetting() == 1 && System.currentTimeMillis() > restrict.getDayStartTime() && restrict.getDayBuy() > 0){
    						
    						//获取用户单日组合限购数量
    						dayCount = productService.getUserRestrictBuy(userId, restrictId, 1 );
    						if(dayCount + buyNum > restrict.getDayBuy()){
    							result = true;
    							restrictProductName = product.getValue().getName();
    							break;
    						}
    						
    					}
    					
    				}
    			}
    			
    		}
    	}
    	
    	resultMap.put("result", result);
    	resultMap.put("restrictProductName", restrictProductName);
    	return resultMap;
//    	return curZeroCount == 0 ? curZeroCount : monthlyZeroCount + curZeroCount;
    }

	 /**
     * 计算邮费
     * @param groupList 按照发货地(仓库)归类的商品包裹
     * @param cityName 省份+城市 例如"浙江省杭州市"
     * @return
     */
	@SuppressWarnings("unchecked")
	public double calculatePostage(List<Map<String, Object>> groupList, String cityName, Map<Long, Double> subPostage) {
		//目的地为空抛出异常
    	if(StringUtils.isEmpty(cityName)) {
    		throw new DeliveryLocationNullException();
    	}

    	//获取收货地id信息
    	//填写的市,数据表中未查到抛出异常
		LOLocation location = loLocationService.getFirstCityByName(cityName);
    	if(location == null) {
    		throw new DeliveryLocationNotFoundException();
    	}

    	int totalPostage = 0;
    	int distributionLocationId = location.getId();
    	for(Map<String, Object> groupMap : groupList) {
    		//这个包裹里有几件商品
    		int totalBuyCount = 0;
    		LOWarehouse loWarehouse = (LOWarehouse)groupMap.get("warehouse");
    		
    		if(loWarehouse == null){
    			logger.info("物流仓库为空，请尽快排查问题！groupMap："+groupMap);
    			throw new RuntimeException("物流仓库信息为空");
    		}
    		
    		for(Map<String, Object> itemMap: (List<Map<String, Object>>) groupMap.get("itemList")) {
    			Map<String, Object> skuMap = (Map<String, Object>)itemMap.get("sku");
    			totalBuyCount += (int)skuMap.get("buyCount");
    		}
    		
    		//如果满足包邮条件
    		if(loWarehouse != null && loWarehouse.getIsFree() == Logistics.DISCOUNT.getIntValue() && loWarehouse.getFreeCount() <= totalBuyCount) {
    			groupMap.put("itemPostage", 0.00);
    			subPostage.put(loWarehouse.getId(), 0.00);
    			continue;
    		}
//    		发货地
    		int deliveryLocation = loWarehouse.getDeliveryLocation();
    		
    		double postage = 0;//邮费
//    		logger.info("查找发货地到配送地对应的邮费记录，deliveryLocation："+deliveryLocation+",distributionLocationId:"+distributionLocationId);
    		LOPostage loPostage = loPostageService.getPostage(deliveryLocation, distributionLocationId);
    		if(loPostage == null) {
    			postage = 0;
    			logger.info("查找发货地到配送地对应的邮费记录为空，没有找到邮费配置邮费经产品人员确认直接为0，deliveryLocation："+deliveryLocation+",distributionLocationId:"+distributionLocationId);
//    			throw new PostageNotFoundException();
    		}else{
    			postage = loPostage.getPostage();
    			logger.info("查找发货地到配送地对应的邮费记录，deliveryLocation："+deliveryLocation+",distributionLocationId:"+distributionLocationId+",loPostage"+JSON.toJSONString(loPostage));
    		}
    		subPostage.put(loWarehouse.getId(), postage);
			totalPostage += postage;
			groupMap.put("itemPostage", postage);
    	}
    	
		return totalPostage;
	}

	/**
     * 是否有优惠券限用
     * 限用条件 added by 董仲 2016-12-08
     * @param otherCouponFlag 非免邮标志
     * @param productIds 
     * @param productMap 
     * @param totalMoney 订单总金额
     * @return
     */
    public boolean HasCouponLimit(boolean otherCouponFlag, Set<Long> productIds, Map<Long, ProductVOShop> productMap, double totalMoney) {
    	boolean bRet = false;
	
		//待校验优惠券类型使用todo

    	logger.error("HasCouponLimit, productIds:" + productIds + ", totalMoney:" +totalMoney);
    	
		JSONObject couponLimitJson = globalSettingService.getJsonObjectNoCache(GlobalSettingName.COUPON_LIMIT_SET);
        List<String> limitIdsList = new ArrayList<String>();
        if(couponLimitJson.get("enable") != null && couponLimitJson.get("start_time") != null && couponLimitJson.get("end_time") != null && couponLimitJson.get("category_ids") != null){
        	
        	JSONArray limitConditions = couponLimitJson.getJSONArray("limit_condition");
        	if (limitConditions != null) {
        		for (Object object : limitConditions) {
        			JSONObject limitCondition = (JSONObject)object;
        			String enable = limitCondition.getString("enable");
        			
        			if (enable != null && StringUtils.equals(enable, "YES")) {
        				double limit_total_money = limitCondition.getDoubleValue("total_money");
            			
        				if (totalMoney >= limit_total_money)  {
        			      	
        		        	SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        		        	long nowTime = System.currentTimeMillis();
        		        	long startTime = 0;
        		        	long endTime = 0;
        		        	String limitCategoryIds = couponLimitJson.get("category_ids").toString().replace("[", "").replace("]", "");
        		        	try {
        						startTime = sdf.parse(couponLimitJson.get("start_time").toString()).getTime();
        						endTime = sdf.parse(couponLimitJson.get("end_time").toString()).getTime();
        					} catch (ParseException e) {
        					
        						e.printStackTrace();
        					}
        		        	//如果全部分类限制的话，返回空列表
        		        	if(couponLimitJson.get("enable").equals("YES") && startTime < nowTime && nowTime < endTime  ){
        		        		if(limitCategoryIds.equals("0") && otherCouponFlag){
        		        			return true;
        		        		} else if(limitCategoryIds.length() > 0 ){
        		        				String[] idsArr = limitCategoryIds.split(",");
        		        					//子分类匹配
        		        					List<ShopCategory> categorieListAll = shopCategoryService.getCategories();
        		        					for(String ctgrId : idsArr){
        		        						limitIdsList.add(ctgrId);
        		        					}
        		        					//添加子分类到列表
        		        					for(ShopCategory category : categorieListAll){
        		        						//添加子分类
        		        						for(ShopCategory categoryTemp : categorieListAll){
        		        							if(categoryTemp.getParentId() == category.getId()){
        		        								category.getChildCategories().add(categoryTemp);
        		        							}
        		        						}
        		        						for(String ctgrId : idsArr){
        		        							if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
        		        								for(ShopCategory childCtgy : category.getChildCategories()){
        		        									limitIdsList.add(childCtgy.getId() + "");
        		        								}
        		        							}
        		        						}
        		        					}
        		        			}
        		        	}
        		        	if(limitIdsList != null && limitIdsList.size() > 0){
        		        		
        		        		List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
        		        		if(productCategoryList != null && productCategoryList.size() > 0 ){	
        		        			
        		        			for(String id : limitIdsList){
        		        				for (ProductCategory productCategory : productCategoryList) {
        		        					if((productCategory.getCategoryId() + "" ).equals(id) && otherCouponFlag){
        		        						return true;
        		        					}	
        		        				}
        		        				for (Product product : productMap.values()) {
        		        					if((product.getvCategoryId() + "").equals(id) && otherCouponFlag){
        		        						return true;
        		        					}
        		        				}
        		        			}
        		        		}
        		        	}
        				}
        				else {
        					return false;
        				}
        			}        					
				}
        	}
        	  
        }
    	
    	
    	return bRet;
    }
    /**
     * 组装订单信息
     * @param userDetail
     * @param skuCountMap
     * @param consumeWrapper
     * @param address
     * @param payCash
     * @param couponId
     * @param version
     * @return
     */
	public ShopStoreOrder buildOrder185(UserDetail userDetail, Map<Long, Integer> skuCountMap, ConsumeWrapper consumeWrapper, Address address, double payCash, String couponId, String version) {
    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
    	Set<Long> productIds = new HashSet<Long>();
    	for (ProductSKU sku : productSkus) {
    		int count = skuCountMap.get(sku.getId());
    		Product product = productService.getProductById(sku.getProductId());
    		if (count > sku.getRemainCount() && product.getSetLOWarehouseId2() == 0) {
    			//提交失败之一：库存不足
    			throw new RemainCountLessException(product.getName());
    		}else if (count > sku.getRemainCount2() && count > sku.getRemainCount() && product.getSetLOWarehouseId2() == 1){
    			//提交失败之一：库存不足
    			throw new RemainCountLessException(product.getName());
    		}
    		if(sku.getIsRemainCountLock() == 1 && count > sku.getRemainCount() - sku.getRemainCountLock() && System.currentTimeMillis() > sku.getRemainCountStartTime() && System.currentTimeMillis() < sku.getRemainCountEndTime()){
    			//提交失败：购买数量大于锁库存后的库存量
    			
    			throw new RemainCountLessException(product.getName());
    			
    		}
    		productIds.add(sku.getProductId());
    	}
    	
    	if (productIds.isEmpty()) {
    		return null;
    	}
    	
    	Map<Long, ProductVOShop> productMap = productService.getProductMap(productIds);
    	
    	//统计组合限购，如果为true则为超出限购数量
    	Map<String, Object> restrictResult = calculateUserRestrictBuy(userDetail.getId(), productIds, productMap,  productSkus, skuCountMap);
    	if ((boolean) restrictResult.get("result")) 
    		throw new RemainCountLessException((String) restrictResult.get("restrictProductName"));
    		//return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICT_GROUP).setError("亲，您购买的商品" +  + "超过组合限购量了哦");
    	
    	
    	
    	int originalAmountInCents = 0;
    	
    	for (ProductSKU sku : productSkus) {
    		ProductVOShop product = productMap.get(sku.getProductId());
    		//判断失效
    		if (product == null || !sku.getOnSaling()) {
    			throw new ProductUnavailableException();
    		}
    		int count = skuCountMap.get(sku.getId());
    		product.setCount(product.getCount() + count);
    		if(product.getMarketPriceMax() > 0) {
    			originalAmountInCents += count * product.getMarketPriceMax(); // market price是元存储!!!
    		} else {
    			originalAmountInCents += count * product.getMarketPrice(); // market price是元存储!!!
    		}
    	}
    	consumeWrapper.setOriginalAmountInCents(originalAmountInCents);
    	
    	return buildOrder185(userDetail, skuCountMap, productSkus, productMap, consumeWrapper, address, productIds, payCash, couponId, version);
    }
	
	/**
     * 生成订单(包括包裹订单和单个子订单)
     * @param skuCountMap
     * @param productSkus 
     * @param productMap <productId, Product>
     * @param consumeWrapper
     * @param address 
     * @param productIds
     */
    private ShopStoreOrder buildOrder185(UserDetail userDetail, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
    		Map<Long, ProductVOShop> productMap, ConsumeWrapper consumeWrapper, Address address, Set<Long> productIds, double payCash, String couponId, String version) {
//      groupList按照仓库分类好之后 计算运费
    	String cityName = address.getProvinceName() + address.getCityName();
    	List<Map<String, Object>> groupList = wrapperGroupList(productSkus, productMap, skuCountMap, null);
    	Map<Long, Double> subPostage = new HashMap<Long, Double>();
    	double totalPostage = calculatePostage(groupList, cityName, subPostage);
    	
    	List<OrderDiscountLog> orderDiscountLogs = new ArrayList<OrderDiscountLog>();
//      计算商品费用
    	Set<Long> brandIds = new HashSet<Long>();
    	
    	
    	//供应商
    	long supplierId = 0;
    	for (Product product : productMap.values()) {
    		brandIds.add(product.getBrandId());
    		supplierId = product.getSupplierId();
    	}
    	Map<Long, Brand> brandMap = shopBrandService.getBrands();
    	Map<String, Object> price = calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, orderDiscountLogs, userDetail.getId(), couponId, version, "confirm");
    	if(price.get("postFreeFlag") != null && price.get("postFreeFlag").equals("YES")){
    		totalPostage = 0;
    	}
    	
    	double totalConsume = (double)price.get("totalCash") + totalPostage;
    	double couponCash = (double)price.get("couponCash");
    	//如果和建立订单时的价格不一致
    	
    	DecimalFormat df   = new DecimalFormat("#.00");   
    	if(!df.format(payCash) .equals (df.format(totalConsume)) && payCash != -1) {
    		throw new PayCheckNotEqualException();
    	}
    	
    	long time = System.currentTimeMillis();
    	List<ShopStoreOrderItem> orderItems = buildOrderItems17(userDetail.getId(), skuCountMap, productSkus, productMap, time);
    	List<OrderItemGroup> orderItemGroups = buildOrderItemGroups17(userDetail.getId(), orderItems, time, subPostage);
    	List<OrderProduct> orderProducts = buildOrderProducts17(userDetail.getId(), productMap, time, subPostage);
    	
    	int totalUnavalCoinUsed = 0;
    	for (OrderItemGroup group : orderItemGroups) {
    		totalUnavalCoinUsed += group.getTotalUnavalCoinUsed();
    	}
    	
    	ShopStoreOrder order = new ShopStoreOrder();
    	order.setOrderNo(Long.parseLong(DateUtil.format(time, "HHmmssSSS") + RandomStringUtils.randomNumeric(8)));//yyyyMMddHHmmssSSS
    	order.setStoreId(userDetail.getId());

    	order.setTotalMoney((double)price.get("totalMoney"));
    	order.setTotalExpressMoney(totalPostage);
    	order.setTotalPay((double)price.get("totalCash"));
    	order.setStatus(0);
    	order.setCreateTime(time);
    	order.setUpdateTime(time);
    	order.setOrderItems(orderItems);
    	order.setOrderProductList(orderProducts);
    	order.setOrderType(OrderType.NORMAL.getIntValue());
    	
    	order.setSupplierId(supplierId);
    	
    	double originalMarketPrice = 0;
    	for(ShopStoreOrderItem item : orderItems){
    		originalMarketPrice += item.getTotalMarketPrice();
    	}
    	order.setCoinUsed(totalUnavalCoinUsed);
    	order.setTotalMarketPrice(originalMarketPrice);
    	//实际支付金额计算提成
//    	order.setCommission(order.getTotalPay()* userDetail.getStoreBusiness().getCommissionPercentage()/100);
    	

    	order.setActualDiscount(couponCash);
    	
    	//计算订单支付过期时间
    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
    	int expiredTime = 24 * 60;
    	for(Object obj : jsonArray) {
    		expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
    		break;
    	}
    	
    	for (ProductSKU sku : productSkus) {
    		if(sku.getRemainKeepTime() > 0 && sku.getRemainKeepTime() < expiredTime){
    			expiredTime = sku.getRemainKeepTime();
    		}
    	}
    	order.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);
    	

    	
    	consumeWrapper.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
    	consumeWrapper.setCash(totalConsume);
    	
    	return order;
    }
    /**
     * 组装订单明细数据
     * @param userId
     * @param skuCountMap
     * @param productSkus
     * @param productMap
     * @param time
     * @return
     */
    private List<ShopStoreOrderItem> buildOrderItems17(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
            Map<Long, ProductVOShop> productMap, long time) {
		List<ShopStoreOrderItem> orderItems = new ArrayList<ShopStoreOrderItem>();
		
		List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
		Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
		for (ProductSKU sku : productSkus) {
			List<ProductPropVO> skuProps = sku.getProductProps();
			composites.addAll(skuProps);
			skuPropMap.put(sku.getId(), skuProps);
		}
		productPropAssembler.assemble(composites);
		
		for (ProductSKU sku : productSkus) {
			long productId = sku.getProductId();
			//根据商品购买数量获取对应阶梯价格
			double currentLadderPrice = getLadderPrice(skuCountMap, productSkus, productMap,productId);//product.buildCurrentLadderPriceByBuyCount(buyCount);
			
			int buyCount = skuCountMap.get(sku.getId());
			Product product = productMap.get(sku.getProductId());
		
		
			ShopStoreOrderItem orderItem = new ShopStoreOrderItem();
			orderItem.setStoreId(userId);
			orderItem.setProductId(product.getId());
			orderItem.setSkuId(sku.getId());
			orderItem.setSupplierId(product.getSupplierId());
		
			//修改价格规则
//			orderItem.setMoney(product.getWholeSaleCash());
			orderItem.setMoney(currentLadderPrice);
			
			orderItem.setUnavalCoinUsed(0);
//			orderItem.setTotalMoney(product.getWholeSaleCash() * buyCount);
			orderItem.setTotalMoney(currentLadderPrice * buyCount);
			orderItem.setTotalUnavalCoinUsed(product.getCurrentJiuCoin() * buyCount);
			orderItem.setExpressMoney(0);// FIXME
			orderItem.setTotalExpressMoney(0);// FIXME
			orderItem.setBuyCount(buyCount);
			orderItem.setSkuSnapshot(buildSkuSnapshot(sku, skuPropMap));
			orderItem.setStatus(0);
			orderItem.setCreateTime(time);
			orderItem.setUpdateTime(time);
			orderItem.setBrandId(product.getBrandId());
			orderItem.setlOWarehouseId(product.getlOWarehouseId());
			orderItem.setMarketPrice(product.getMarketPrice());
			orderItem.setTotalMarketPrice(buyCount * product.getMarketPrice());
//			orderItem.setTotalPay(buyCount * product.getWholeSaleCash());
			orderItem.setTotalPay(currentLadderPrice * buyCount);
			orderItem.setPosition(sku.getPosition());
			
			if(product.getSetLOWarehouseId2() == 1 && sku.getRemainCount2() >= buyCount){
		
		
				orderItem.setlOWarehouseId(product.getlOWarehouseId2());
				orderItems.add(orderItem);
				//}else{
				//orderItem.setlOWarehouseId(product.getlOWarehouseId2());
				//orderItem.setBuyCount(sku.getRemainCount2());
				//orderItems.add(orderItem);
				//try {
				//StoreOrderItem orderItemSecond = (StoreOrderItem) orderItem.clone();
				//orderItemSecond.setBuyCount(buyCount - sku.getRemainCount2());
				//orderItemSecond.setlOWarehouseId(product.getlOWarehouseId());
				//orderItems.add(orderItemSecond);
				//} catch (CloneNotSupportedException e) {
				//// TODO Auto-generated catch block
				//e.printStackTrace();
				//}
				//}	
			}else{
			orderItems.add(orderItem);
		
			}
		
		}
		
		return orderItems;
    }
    
   
    
    private String buildSkuSnapshot(ProductSKU sku, Map<Long, List<ProductPropVO>> skuPropMap) {
        StringBuilder builder = new StringBuilder();
        List<ProductPropVO> props = skuPropMap.get(sku.getId());
        for (ProductPropVO prop : props) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }
    
    private List<OrderItemGroup> buildOrderItemGroups17(long userId, List<ShopStoreOrderItem> orderItems, long time, Map<Long, Double> subPostage) {
        List<OrderItemGroup> result = new ArrayList<OrderItemGroup>();
        Map<Long, OrderItemGroup> groupMap = new HashMap<Long, OrderItemGroup>();
        for (ShopStoreOrderItem item : orderItems) {
            long brandId = item.getBrandId();
        	long warehouseId = item.getlOWarehouseId();
        	
        	OrderItemGroup group = groupMap.get(warehouseId);
            if (group == null) {
                group = new OrderItemGroup();
                group.setUserId(userId);
                
                group.setBrandId(brandId);
                group.setStatus(0);
                group.setCreateTime(time);
                group.setUpdateTime(time);
                group.setlOWarehouseId(warehouseId);

                result.add(group);
                groupMap.put(warehouseId, group);
                if(subPostage.get(warehouseId) == null){
                	group.setTotalExpressMoney(0);
                }else{
                	group.setTotalExpressMoney(subPostage.get(warehouseId));
                }
            }
            group.setTotalMoney(group.getTotalMoney() + item.getTotalMoney());
            group.setTotalUnavalCoinUsed(group.getTotalUnavalCoinUsed() + item.getTotalUnavalCoinUsed());
        }
        return result;
    }
    
    /**
	 * @param storeIdid
	 * @param productMap
	 * @param time
	 * @param subPostage
	 * @return
	 */
	private List<OrderProduct> buildOrderProducts17(long storeId, Map<Long, ProductVOShop> productMap, long time,
			Map<Long, Double> subPostage) {
		// TODO Auto-generated method stub
		
		List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
		
//		System.out.println("siz"+productMap.size());
		for (Map.Entry<Long, ProductVOShop> entry: productMap.entrySet()) {
			ProductVOShop product = entry.getValue();
			//根据购买数量获取对应阶梯价格
			double currentLadderPrice = ProductNew.buildCurrentLadderPriceByBuyCount(product.getLadderPriceJson(),product.getCount());
			
			OrderProduct orderProduct = new OrderProduct();
			orderProduct.setBrandId(product.getBrandId());
			orderProduct.setBuyCount(product.getCount());
			orderProduct.setCommission(product.getIncome());
			orderProduct.setCreateTime(time);
			orderProduct.setMarketPrice(product.getMarketPrice());
//			orderProduct.setMoney(product.getWholeSaleCash());
			orderProduct.setMoney(currentLadderPrice);
			orderProduct.setProductId(product.getId());
			orderProduct.setStoreId(storeId);
			orderProduct.setTotalCommission(product.getIncome()*product.getCount());
			orderProduct.setTotalMarketPrice(product.getMarketPrice()*product.getCount());
//			orderProduct.setTotalMoney(product.getWholeSaleCash()*product.getCount());
			orderProduct.setTotalMoney(currentLadderPrice * product.getCount());
			orderProduct.setUpdateTime(time);
			orderProduct.setWarehouseId(product.getlOWarehouseId());
			
			orderProducts.add(orderProduct);
			
			// OrderNo需要在创建订单之后立即插入
		}
		
	
		
		return orderProducts;
	}

	@Transactional(rollbackFor = Exception.class)
    public void createOrder17(ShopStoreOrder order, OrderType orderType, String expressSupplier, String expressOrderNo,
                            String phone, String remark, Address address, ClientPlatform clientPlatform, String ip, String couponId) {
        if (orderType == null) {
            String msg = "order type can not be null";
            logger.error(msg);
            throw new IllegalArgumentException(msg);
        }
//        else if (orderType == OrderType.SEND_BACK) {
//            if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//                StringUtils.isBlank(phone)) {
//                String msg =
//                    "send back order is leak of args, expressSupplier:" + expressSupplier + ", expressOrderNo:" +
//                        expressOrderNo + ", phone:" + phone;
//                logger.error(msg);
//                throw new IllegalArgumentException(msg);
//            }
//        }

        order.setRemark(remark);
        order.setExpressInfo(address.getExpressInfo());
        order.setExpress_name(address.getReceiverName());
        order.setExpress_phone(StringUtils.defaultString(address.getTelephone(), address.getFixPhone()));
        order.setExpress_address(address.getAddrFull());
        order.setPlatform(clientPlatform.getPlatform().getValue());
        order.setPlatformVersion(clientPlatform.getVersion());
        order.setIp(ip);

        order.setOrderType(orderType.getIntValue());
        double totalConsume = order.getTotalPay() + order.getTotalExpressMoney();
        order.setOrderStatus(totalConsume > 0.001 ? OrderStatus.UNPAID.getIntValue() : OrderStatus.PAID.getIntValue());


        
//        order.setOrderStatus(OrderStatus.UNPAID);
//    	orderService.createOrder18(order);

    	
        //如果是0元购，则插入日志0-10订单状态
        if(totalConsume > 0.001) {
        	order.setOrderStatus(OrderStatus.UNPAID.getIntValue());
        	//orderService.createOrder17(order);
        	//1.8新订单创建
        	orderService.createOrder18(order, couponId);
        	
        } else {
        	order.setOrderStatus(OrderStatus.PAID.getIntValue());
        	//orderService.createOrder17(order);
        	ShopStoreOrder storeOrder = orderService.createOrder18(order, couponId);
        	orderService.splitOrderNew(order.getStoreId()+"", order);
        	
        	
        	OrderNewLog orderNewLog = new OrderNewLog();
        	orderNewLog.setStoreId(storeOrder.getStoreId());
        	orderNewLog.setOrderNo(storeOrder.getOrderNo());
        	orderNewLog.setNewStatus(OrderStatus.PAID.getIntValue());
        	orderNewLog.setOldStatus(OrderStatus.UNPAID.getIntValue());
        	orderNewLog.setCreateTime(System.currentTimeMillis());
        	orderNewLogMapper.addOrderLog(orderNewLog);
        	      	
        }

          //生成订单不增加商品销售量，支付成功才增加
        if (orderHandlers != null) {
            for (OrderHandler handler : orderHandlers) {
                handler.postOrderCreation(order, clientPlatform.getVersion());
            }
        }
    }

	public Map<Long, List<ShopStoreOrderItemVO>> getOrderNewItemVOMap(UserDetail userDetail, Collection<Long> orderNos) {
		long time4_1 = System.currentTimeMillis();
		
    	//在OrderItem里获取具体的订单中的商品
    	List<ShopStoreOrderItem> orderItems = orderService.getOrderNewItems(userDetail.getId(), orderNos);
    	long time4_2 = System.currentTimeMillis();
		logger.info("time4_2："+(time4_2-time4_1));
    	Map<Long, List<ShopStoreOrderItemVO>> result = new HashMap<Long, List<ShopStoreOrderItemVO>>();
    	List<ShopStoreOrderItemVO> composites = new ArrayList<ShopStoreOrderItemVO>();
    	for (ShopStoreOrderItem orderItem : orderItems) {
    		long time4_3_1 = System.currentTimeMillis();
    		ShopStoreOrderItemVO vo = new ShopStoreOrderItemVO();
    		BeanUtil.copyProperties(vo, orderItem);//拷贝字段耗时比较严重待确认方案后修改
    		copyCopyProperties(vo, orderItem);
    		long time4_3_2 = System.currentTimeMillis();
    		logger.info("time4_3_2："+(time4_3_2-time4_3_1));
    		vo.setBuyCount(orderItem.getBuyCount());
    		vo.setColorStr(orderItem.getSkuSnapshot().split("  ")[0].trim());
    		vo.setSizeStr(orderItem.getSkuSnapshot().split("  ")[1].trim());
    		long time4_3_3 = System.currentTimeMillis();
    		logger.info("time4_3_3："+(time4_3_3-time4_3_2));
//			平台商品状态:0已上架、1已下架、2已删除
    		String platformProductState = productNewService.getPlatformProductState(orderItem.getProductId());
    		vo.setPlatformProductState(platformProductState);
    		long time4_3_4 = System.currentTimeMillis();
    		logger.info("time4_3_4："+(time4_3_4-time4_3_3));
    		long orderNo = orderItem.getOrderNo();
    		List<ShopStoreOrderItemVO> list = result.get(orderNo);
    		if (list == null) {
    			list = new ArrayList<ShopStoreOrderItemVO>();
    			result.put(orderNo, list);
    			
    		}
    		list.add(vo);
    		composites.add(vo);
    		long time4_3_5 = System.currentTimeMillis();
    		logger.info("time4_3_5："+(time4_3_5-time4_3_4));
    	}
    	long time4_3 = System.currentTimeMillis();
		logger.info("time4_3："+(time4_3-time4_2));
    	productAssembler.assemble(composites, userDetail);//耗时750毫秒，耗时太严重，待确定优化方案
    	long time4_4 = System.currentTimeMillis();
		logger.info("time4_4："+(time4_4-time4_3));
    	return result;
    }

	private void copyCopyProperties(ShopStoreOrderItemVO vo, ShopStoreOrderItem orderItem) {
		vo.setId(orderItem.getId());
		vo.setOrderNo(orderItem.getOrderNo());
		vo.setOrderId(orderItem.getOrderId());
		vo.setStoreId(orderItem.getStoreId());
		vo.setProductId(orderItem.getProductId());
		vo.setSkuId(orderItem.getSkuId());
		vo.setTotalMoney(orderItem.getTotalMoney());
		vo.setTotalExpressMoney(orderItem.getTotalExpressMoney());
		vo.setMoney(orderItem.getMoney());
		vo.setExpressMoney(orderItem.getExpressMoney());
		vo.setTotalUnavalCoinUsed(orderItem.getTotalUnavalCoinUsed());
		vo.setUnavalCoinUsed(orderItem.getUnavalCoinUsed());
		vo.setAfterSaleFlag(orderItem.getAfterSaleFlag());
		
		vo.setBuyCount(orderItem.getBuyCount());
		vo.setSkuSnapshot(orderItem.getSkuSnapshot());
		vo.setPosition(orderItem.getPosition());
		vo.setStatus(orderItem.getStatus());
		vo.setTotalPay(orderItem.getTotalPay());
		vo.setTotalMarketPrice(orderItem.getTotalMarketPrice());
		vo.setMarketPrice(orderItem.getMarketPrice());
//		vo.setProductSKU(orderItem.getProductSKU());
	}

	public Map<Long, List<ShopStoreOrder>> getChildOrderMap(UserDetail userDetail, Collection<Long> orderNOs) {
    	//在OrderItem里获取具体的订单中的商品
    	List<ShopStoreOrderItemVO> orderItemList;
     	List<ShopStoreOrder> childOrderList = orderService.getChildOrderList(userDetail.getId(), orderNOs);
    	Map<Long, List<ShopStoreOrder>> result = new HashMap<Long, List<ShopStoreOrder>>();
    	 for (ShopStoreOrder orderNew : childOrderList) {
             long parentId = orderNew.getParentId();
             List<ShopStoreOrder> list = result.get(parentId);
             if (list == null) {
                 list = new ArrayList<ShopStoreOrder>();
                 result.put(parentId, list);
             }
             orderItemList = orderService.getOrderNewItemsVO(userDetail, CollectionUtil.createSet(orderNew.getOrderNo()));
             orderNew.setOrderItems(new ArrayList<ShopStoreOrderItem>(orderItemList));
             list.add(orderNew);
        
         }
    	
    	return result;
    }

	public Map<Long, List<OrderProduct>> getOrderProductMap(UserDetail userDetail, Collection<Long> orderNOs) {

    	List<OrderProduct> orderProductList = orderService.getOrderProductsByOrderNoBatch(userDetail.getId(), orderNOs);
    	List<Product> productList = orderService.getProductsByOrderNoBatch(userDetail.getId(), orderNOs);
    	Map<Long, List<OrderProduct>> result = new HashMap<Long, List<OrderProduct>>();
    	for (OrderProduct orderProduct: orderProductList) {
    		for(Product product :productList){
    			if(product.getId() == orderProduct.getProductId()){
    				orderProduct.setProduct(product);
    			}
    		}
    		long orderNo = orderProduct.getOrderNo();
    		List<OrderProduct> list = result.get(orderNo);
    		if (list == null) {
    			list = new ArrayList<OrderProduct>();
    			result.put(orderNo, list);
    		}
    		list.add(orderProduct);
    	}
    	return result;
    }

	@Transactional(rollbackFor = Exception.class)
    public void cancelOrderNew(ShopStoreOrder order, String version) {
    	if(order.getOrderStatus() != OrderStatus.UNPAID.getIntValue()){
    		 String msg = "order status error, orderNo:" + order.getOrderNo() + ", order status:" + order.getOrderStatus();
    	            logger.error(msg);
    	            throw new IllegalArgumentException(msg);
    	}
    	
    	long time = System.currentTimeMillis();
    	List<ShopStoreOrderItem> orderItems =
    			orderService.getOrderNewItems(order.getStoreId(), CollectionUtil.createList(order.getOrderNo()));
    	order.setOrderItems(orderItems);
    	if (order.getOrderStatus() == OrderStatus.UNPAID.getIntValue()) {
    		orderService.cancelOrderNew(order, time);


    		Map<String,Object> param = new HashMap<>(5);
    		param.put("orderNo",order.getOrderNo());
			CouponRbRef couponRbRef = commonRefMapper.selectCouponByParam(param);

			if(couponRbRef!=null) {
				CouponUseLog couponUseLog = new CouponUseLog();
				couponUseLog.setOrderNo(order.getOrderNo());
				couponUseLog.setUserId(order.getStoreId());
				couponUseLog.setCouponId(couponRbRef.getId());
				couponUseLog.setStatus(1);
				couponUseLog.setCreateTime(System.currentTimeMillis());
				orderService.updateCouponUseLog(couponUseLog);

				int rs = commonRefMapper.updateCouponStatus(couponRbRef.getId(),order.getOrderNo(),1,0);
				if(rs==0) {
					throw new RuntimeException("修改优惠券状态失败");
				}
			}

//    		List<ShopCoupon> couponList = orderService.getUserCouponListByOrderNo(order.getOrderNo());
//    		if(couponList != null && couponList.size() > 0){
//    			orderService.updateCouponUnuse(order.getOrderNo());
//    			for(ShopCoupon coupon : couponList){
//    				CouponUseLog couponUseLog = new CouponUseLog();
//    	    		couponUseLog.setOrderNo(order.getOrderNo());
//    	    		couponUseLog.setUserId(order.getStoreId());
//    	    		couponUseLog.setCouponId(coupon.getId());
//    	    		couponUseLog.setStatus(1);
//    	    		couponUseLog.setActualDiscount(coupon.getActualDiscount());
//    	    		couponUseLog.setCreateTime(System.currentTimeMillis());
//    	    		orderService.updateCouponUseLog(couponUseLog);
//    			}
//    		}
//    		List<ShopCoupon> couponAllMemberList = orderService.getAllMemberCouponListByOrderNo(order.getOrderNo());
//    		if(couponAllMemberList != null && couponAllMemberList.size() > 0){
//    			for(ShopCoupon coupon : couponAllMemberList){
//    				CouponUseLog couponUseLog = new CouponUseLog();
//    				couponUseLog.setOrderNo(order.getOrderNo());
//    				couponUseLog.setUserId(order.getStoreId());
//    				couponUseLog.setCouponId(coupon.getId());
//    				couponUseLog.setStatus(1);
//    				couponUseLog.setActualDiscount(coupon.getMoney());
//    				couponUseLog.setCreateTime(System.currentTimeMillis());
//    				orderService.updateCouponUseLog(couponUseLog);
//    			}
//    		}
    	} else {
    		orderService.updateOrderNewStatus(order, OrderStatus.REFUNDING, OrderStatus.getByIntValue(order.getOrderStatus()), time);
    	}
    	
    	if (orderHandlers != null) {
    		for (OrderHandler handler : orderHandlers) {
    			handler.postOrderCancel(order,version);
    		}
    	}
    }
	
}
