package com.yujj.business.facade;

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

import com.jiuyuan.dao.mapper.CommonRefMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.weaver.ast.Var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.Logistics;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOPostage;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.order.ConsumeWrapper;
import com.jiuyuan.entity.order.OrderItemGroup;
import com.jiuyuan.entity.product.RestrictionCombination;
import com.jiuyuan.entity.shopping.CartItemVO;
import com.jiuyuan.entity.shopping.DiscountInfo;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.VersionUtil;
import com.yujj.business.assembler.BrandAssembler;
import com.yujj.business.assembler.ProductAssembler;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.handler.order.OrderHandler;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.LOLocationService;
import com.yujj.business.service.LOPostageService;
import com.yujj.business.service.LOWarehouseService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductCategoryService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.UserCoinService;
import com.yujj.dao.mapper.OrderLogMapper;
import com.yujj.dao.mapper.OrderNewLogMapper;
import com.yujj.entity.Brand;
//import com.yujj.entity.account.Address;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderDiscountLog;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderItemVO;
import com.yujj.entity.order.OrderLog;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewLog;
import com.yujj.entity.order.OrderNewVO;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductCategory;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;
import com.yujj.exception.order.CouponUnavailableException;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PayCheckNotEqualException;
import com.yujj.exception.order.PostageNotFoundException;
import com.yujj.exception.order.ProductUnavailableException;
import com.yujj.exception.order.RemainCountLessException;
import com.yujj.exception.order.UserCoinLessException;

@Service
public class OrderFacade {

    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private ProductPropAssembler productPropAssembler;

    @Autowired
    private ProductAssembler productAssembler;

    @Autowired
    private BrandAssembler brandAssembler;

    @Autowired(required = false)
    private List<OrderHandler> orderHandlers;
    
    @Autowired
    private LOLocationService loLocationService;
    
    @Autowired
    private LOPostageService loPostageService;

    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private LOWarehouseService loWarehouseService;
    
    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ShoppingCartFacade shoppingCartFacade;
    
    @Autowired
    private OrderLogMapper orderLogMapper;
    
    @Autowired
    private OrderNewLogMapper orderNewLogMapper;

    @Autowired
	private CommonRefMapper commonRefMapper;
    
//    public Order buildOrder17(long userId, Map<Long, Integer> skuCountMap, ConsumeWrapper consumeWrapper, Address address, double payCash, String version) {
//        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
//        Set<Long> productIds = new HashSet<Long>();
//        for (ProductSKU sku : productSkus) {
//            int count = skuCountMap.get(sku.getId());
//            if (count > sku.getRemainCount()) {
//            	//提交失败之一：库存不足
//            	Product product = productService.getProductById(sku.getProductId());
//                throw new RemainCountLessException(product.getName());
//            }
//            if(sku.getIsRemainCountLock() == 1 && count > sku.getRemainCount() - sku.getRemainCountLock() && System.currentTimeMillis() > sku.getRemainCountStartTime() && System.currentTimeMillis() < sku.getRemainCountEndTime()){
//            	//提交失败：购买数量大于锁库存后的库存量
//            	Product product = productService.getProductById(sku.getProductId());
//                throw new RemainCountLessException(product.getName());
//            	
//            }
//            productIds.add(sku.getProductId());
//        }
//
//        if (productIds.isEmpty()) {
//            return null;
//        }
//
//        Map<Long, Product> productMap = productService.getProductMap(productIds);
//        int originalAmountInCents = 0;
//
//        for (ProductSKU sku : productSkus) {
//            Product product = productMap.get(sku.getProductId());
//            //判断失效
//            if (product == null || !sku.isOnSaling()) {
//                throw new ProductUnavailableException();
//            }
//            int count = skuCountMap.get(sku.getId());
//            if(product.getMarketPriceMax() > 0) {
//            	originalAmountInCents += count * product.getMarketPriceMax(); // market price是元存储!!!
//            } else {
//            	originalAmountInCents += count * product.getMarketPrice(); // market price是元存储!!!
//            }
//        }
//        consumeWrapper.setOriginalAmountInCents(originalAmountInCents);
//
//        return buildOrder17(userId, skuCountMap, productSkus, productMap, consumeWrapper, address, productIds, payCash, version);
//    }  
    
    public Order buildOrder185(long userId, Map<Long, Integer> skuCountMap, ConsumeWrapper consumeWrapper, Address address, double payCash, String couponId, ClientPlatform clientPlatform,String ip,long userSharedRecordId,int coinDeductFlag,
    		int takeGood) {
    	
    	String version = clientPlatform.getVersion();
    	
    	//1、获取商品集合
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
//    		if (count > sku.getRemainCount()) {
//    			//提交失败之一：库存不足
//    			throw new RemainCountLessException(product.getName());
//    		}
    		if(sku.getIsRemainCountLock() == 1 && count > sku.getRemainCount() - sku.getRemainCountLock() && System.currentTimeMillis() > sku.getRemainCountStartTime() && System.currentTimeMillis() < sku.getRemainCountEndTime()){
    			//提交失败：购买数量大于锁库存后的库存量
//    			Product product = productService.getProductById(sku.getProductId());
    			throw new RemainCountLessException(product.getName());
    			
    		}
    		productIds.add(sku.getProductId());
    	}
    	
    	if (productIds.isEmpty()) {
    		return null;
    	}
    	Map<Long, Product> productMap = productService.getProductMap(productIds);
    	
    	//2、统计组合限购，如果为true则为超出限购数量
    	Map<String, Object> restrictResult = calculateUserRestrictBuy(userId, productIds, productMap,  productSkus, skuCountMap);
    	if ((boolean) restrictResult.get("result")){
    		throw new RemainCountLessException((String) restrictResult.get("restrictProductName"));
    		//return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICT_GROUP).setError("亲，您购买的商品" +  + "超过组合限购量了哦");
    	}
    	
    	//3、消费价格信息
    	int originalAmountInCents = 0;
    	for (ProductSKU sku : productSkus) {
    		Product product = productMap.get(sku.getProductId());
    		//判断失效
    		if (product == null || !sku.isOnSaling()) {
    			throw new ProductUnavailableException();
    		}
    		int count = skuCountMap.get(sku.getId());
    		if(product.getMarketPriceMax() > 0) {
    			originalAmountInCents += count * product.getMarketPriceMax(); // market price是元存储!!!
    		} else {
    			originalAmountInCents += count * product.getMarketPrice(); // market price是元存储!!!
    		}
    	}
    	consumeWrapper.setOriginalAmountInCents(originalAmountInCents);
    	
    	return buildOrder185(userId, skuCountMap, productSkus, productMap, consumeWrapper, address, productIds, payCash, couponId, version,clientPlatform, ip,userSharedRecordId, coinDeductFlag,takeGood);
    }  
    
//    public Order buildOrder213(long userId, Map<Long, Integer> skuCountMap, ConsumeWrapper consumeWrapper, Address address, double payCash, String couponId, ClientPlatform clientPlatform,String ip,long userSharedRecordId) {
//    	
//    	String version = clientPlatform.getVersion();
//    	
//    	//1、获取商品集合
//    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
//    	Set<Long> productIds = new HashSet<Long>();
//    	for (ProductSKU sku : productSkus) {
//    		int count = skuCountMap.get(sku.getId());
//    		Product product = productService.getProductById(sku.getProductId());
//    		if (count > sku.getRemainCount() && product.getSetLOWarehouseId2() == 0) {
//    			//提交失败之一：库存不足
//    			throw new RemainCountLessException(product.getName());
//    		}else if (count > sku.getRemainCount2() && count > sku.getRemainCount() && product.getSetLOWarehouseId2() == 1){
//    			//提交失败之一：库存不足
//    			throw new RemainCountLessException(product.getName());
//    		}
////    		if (count > sku.getRemainCount()) {
////    			//提交失败之一：库存不足
////    			throw new RemainCountLessException(product.getName());
////    		}
//    		if(sku.getIsRemainCountLock() == 1 && count > sku.getRemainCount() - sku.getRemainCountLock() && System.currentTimeMillis() > sku.getRemainCountStartTime() && System.currentTimeMillis() < sku.getRemainCountEndTime()){
//    			//提交失败：购买数量大于锁库存后的库存量
////    			Product product = productService.getProductById(sku.getProductId());
//    			throw new RemainCountLessException(product.getName());
//    			
//    		}
//    		productIds.add(sku.getProductId());
//    	}
//    	
//    	if (productIds.isEmpty()) {
//    		return null;
//    	}
//    	Map<Long, Product> productMap = productService.getProductMap(productIds);
//    	
//    	//2、统计组合限购，如果为true则为超出限购数量
//    	Map<String, Object> restrictResult = calculateUserRestrictBuy(userId, productIds, productMap,  productSkus, skuCountMap);
//    	if ((boolean) restrictResult.get("result")){
//    		throw new RemainCountLessException((String) restrictResult.get("restrictProductName"));
//    		//return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICT_GROUP).setError("亲，您购买的商品" +  + "超过组合限购量了哦");
//    	}
//    	
//    	//3、消费价格信息
//    	int originalAmountInCents = 0;
//    	for (ProductSKU sku : productSkus) {
//    		Product product = productMap.get(sku.getProductId());
//    		//判断失效
//    		if (product == null || !sku.isOnSaling()) {
//    			throw new ProductUnavailableException();
//    		}
//    		int count = skuCountMap.get(sku.getId());
//    		if(product.getMarketPriceMax() > 0) {
//    			originalAmountInCents += count * product.getMarketPriceMax(); // market price是元存储!!!
//    		} else {
//    			originalAmountInCents += count * product.getMarketPrice(); // market price是元存储!!!
//    		}
//    	}
//    	consumeWrapper.setOriginalAmountInCents(originalAmountInCents);
//    	
//    	return buildOrder185(userId, skuCountMap, productSkus, productMap, consumeWrapper, address, productIds, payCash, couponId, version,clientPlatform, ip,userSharedRecordId);
//    }  
    

    
//    public Order buildOrderXX(long userId, Map<Long, Integer> skuCountMap, ConsumeWrapper consumeWrapper) {
//        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
//        Set<Long> productIds = new HashSet<Long>();
//        for (ProductSKU sku : productSkus) {
//            int count = skuCountMap.get(sku.getId());
//            if (count > sku.getRemainCount()) {
//                throw new RemainCountLessException();
//            }
//            productIds.add(sku.getProductId());
//        }
//
//        if (productIds.isEmpty()) {
//            return null;
//        }
//
//        Map<Long, Product> productMap = productService.getProductMap(productIds);
//        int originalAmountInCents = 0;
//        for (ProductSKU sku : productSkus) {
//            Product product = productMap.get(sku.getProductId());
//            if (product == null || !sku.isOnSaling()) {
//                throw new ProductUnavailableException();
//            }
//            int count = skuCountMap.get(sku.getId());
//            if(product.getMarketPriceMax() > 0) {
//            	originalAmountInCents += count * product.getMarketPriceMax() * 100; 
//            } else {
//            	originalAmountInCents += count * product.getMarketPrice() * 100;
//            }
//        }
//        consumeWrapper.setOriginalAmountInCents(originalAmountInCents);
//
//        return buildOrderXX(userId, skuCountMap, productSkus, productMap, consumeWrapper);
//    }

//    /**
//     * 生成订单(包括包裹订单和单个子订单)
//     * @param userId
//     * @param skuCountMap
//     * @param productSkus 
//     * @param productMap <productId, Product>
//     * @param consumeWrapper
//     * @param address 
//     * @param productIds
//     * @param totalCash 
//     * @return
//     */
//    private Order buildOrder17(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
//                             Map<Long, Product> productMap, ConsumeWrapper consumeWrapper, Address address, Set<Long> productIds, double payCash, String version) {
////      groupList按照仓库分类好之后 计算运费
//    	String cityName = address.getProvinceName() + address.getCityName();
//        List<Map<String, Object>> groupList = wrapperGroupList(productSkus, productMap, skuCountMap, null, userId);
//    	Map<Long, Double> subPostage = new HashMap<Long, Double>();
//		double totalPostage = calculatePostage(groupList, cityName, subPostage);
//        
//		List<OrderDiscountLog> orderDiscountLogs = new ArrayList<OrderDiscountLog>();
////      计算商品费用
//    	Set<Long> brandIds = new HashSet<Long>();
//    	for (Product product : productMap.values()) {
//    		brandIds.add(product.getBrandId());
//    	}
//    	Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);
//    	Map<String, Object> price = calculateDiscount(productSkus, productIds, productMap, skuCountMap, brandMap, orderDiscountLogs);
//    	
//    	double totalConsume = (double)price.get("totalCash") + totalPostage;
//    	//如果和建立订单时的价格不一致
//    	if(payCash != totalConsume && payCash != -1) {
//    		throw new PayCheckNotEqualException();
//    	}
//    	
//    	long time = System.currentTimeMillis();
//        List<OrderItem> orderItems = buildOrderItems17(userId, skuCountMap, productSkus, productMap, time);
//        List<OrderItemGroup> orderItemGroups = buildOrderItemGroups17(userId, orderItems, time, subPostage);
//
//        int totalUnavalCoinUsed = 0;
//        for (OrderItemGroup group : orderItemGroups) {
//            totalUnavalCoinUsed += group.getTotalUnavalCoinUsed();
//        }
//        
//        Order order = new Order();
//        order.setOrderNo(DateUtil.format(time, "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(8));
//        order.setUserId(userId);
//        order.setOrderStatus(OrderStatus.UNPAID);
//        order.setTotalMoney((double)price.get("totalCash"));
//        order.setTotalExpressMoney(totalPostage);
//        order.setStatus(0);
//        order.setCreateTime(time);
//        order.setUpdateTime(time);
//        order.setOrderItems(orderItems);
//        order.setOrderItemGroups(orderItemGroups);
//        order.setOrderDiscountLogs(orderDiscountLogs);
//        
//        //计算订单支付过期时间
//        JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//        int expiredTime = 24 * 60;
//    	for(Object obj : jsonArray) {
//    		expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
//    		break;
//    	}
//
//        for (ProductSKU sku : productSkus) {
//        	if(sku.getRemainKeepTime() > 0 && sku.getRemainKeepTime() < expiredTime){
//        		expiredTime = sku.getRemainKeepTime();
//        	}
//        }
//        order.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);
//        
//        if(VersionUtil.compareVersion(version , "1.8.11") != 0){
//        	
//	        UserCoin userCoin = userCoinService.getUserCoin(userId);
//	        if (userCoin == null) {
//	            logger.error("user coin can not null, userId:{}", userId);
//	            throw new UserCoinLessException();                            
//	        } else if (userCoin.getAllCoins() < totalConsume) {
//	            logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
//	                userCoin.getAllCoins(), totalConsume);
//	            throw new UserCoinLessException();
//	        }
//	        
//	        int avalCoinUsed = userCoin.getAvalCoins() >= totalUnavalCoinUsed ? totalUnavalCoinUsed : userCoin.getAvalCoins();
//	        order.setAvalCoinUsed(avalCoinUsed);
//	        order.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
//        }
//        
//        consumeWrapper.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
//        consumeWrapper.setCash(totalConsume);
//        
//        return order;
//    }
    
    /**
     * 生成订单(包括包裹订单和单个子订单)
     * @param userId
     * @param skuCountMap
     * @param productSkus 
     * @param productMap <productId, Product>
     * @param consumeWrapper
     * @param address 
     * @param productIds
     * @param totalCash 
     * @return
     */
    /**
     * @param userId
     * @param skuCountMap
     * @param productSkus
     * @param productMap
     * @param consumeWrapper
     * @param address
     * @param productIds
     * @param payCash
     * @param couponId
     * @param version
     * @param clientPlatform
     * @param ip
     * @param userSharedRecordId
     * @param coinDeductFlag
     * @return
     */
    private Order buildOrder185(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
    		Map<Long, Product> productMap, ConsumeWrapper consumeWrapper, Address address, Set<Long> productIds, double payCash, String couponId, String version ,ClientPlatform clientPlatform,String ip,long userSharedRecordId, int coinDeductFlag
    		,int takeGood) {
    	
    	
//      groupList按照仓库分类好之后 计算运费
    	
    	//1、按仓库分组
    	List<Map<String, Object>> groupList = wrapperGroupList(productSkus, productMap, skuCountMap, null ,userId);
    	Map<Long, Double> subPostage = new HashMap<Long, Double>();
    	//2、计算总邮费
    	double totalPostage = 0;
    	if(takeGood != 1){
	    	String cityName = address.getProvinceName() + address.getCityName();
	    	totalPostage = calculatePostage(groupList, cityName, subPostage);
    	}
    	
    	List<OrderDiscountLog> orderDiscountLogs = new ArrayList<OrderDiscountLog>();
    	// 3、计算商品费用
    	Set<Long> brandIds = new HashSet<Long>();
    	for (Product product : productMap.values()) {
    		brandIds.add(product.getBrandId());
    	}
    	Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);
    	Map<String, Object> price;
    	double discountProportion = 1;
    	if(VersionUtil.compareVersion(version , "2.1.3") >= 0){
    		price = calculateDiscountV213(productSkus, productIds, productMap, skuCountMap, brandMap, orderDiscountLogs ,userId, couponId,coinDeductFlag);

    		discountProportion = (double)price.get("discountProportion");
    		
    	}else{
    		price = calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, orderDiscountLogs ,userId, couponId);
    		
    	}
    	if(price.get("postFreeFlag") != null && price.get("postFreeFlag").equals("YES")){
    		totalPostage = 0;
    	}
    	
    	double totalConsume = (double)price.get("totalCash") + totalPostage;
    	
   
    	double couponCash = (double)price.get("couponCash");
    	int deductCoinNum = (int)price.get("deductCoinNum");
    	
    	//如果和建立订单时的价格不一致
    	java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");  
    	if(!df.format(payCash).equals( df.format(totalConsume)) && payCash != -1) {
    		System.out.println("PayCheckNotEqualException,payCash:"+payCash);
    		System.out.println("totalConsume:"+totalConsume);
    		throw new PayCheckNotEqualException();
    	}
    	
    	
    	
    	
    	long time = System.currentTimeMillis();
    	List<OrderItem> orderItems = buildOrderItems17(userId, skuCountMap, productSkus, productMap, time, discountProportion, deductCoinNum);
    	List<OrderItemGroup> orderItemGroups = buildOrderItemGroups17(userId, orderItems, time, subPostage);
    	
    	int totalUnavalCoinUsed = 0;
    	for (OrderItemGroup group : orderItemGroups) {
    		totalUnavalCoinUsed += group.getTotalUnavalCoinUsed();
    	}
    	
    	Order order = new Order();
    	order.setOrderNo(DateUtil.format(time, "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(8));
    	order.setUserId(userId);
    	order.setOrderStatus(OrderStatus.UNPAID);
    	order.setTotalMoney((double)price.get("totalCash"));
    	order.setTotalExpressMoney(totalPostage);
    	order.setStatus(0);
    	order.setCreateTime(time);
    	order.setUpdateTime(time);
    	order.setOrderItems(orderItems);
    	order.setOrderItemGroups(orderItemGroups);
    	order.setOrderDiscountLogs(orderDiscountLogs);
    	
    	order.setActualDiscount(couponCash);
    	if(coinDeductFlag == 1 && deductCoinNum > 0){
    		order.setDeductCoinNum(deductCoinNum);
    		
    	}
    	
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
    	
//    	if(VersionUtil.compareVersion(version , "1.8.11") < 0){
    		
	    	UserCoin userCoin = userCoinService.getUserCoin(userId);
	    	if (userCoin == null) {
	    		logger.error("user coin can not null, userId:{}", userId);
	    		throw new UserCoinLessException();                            
	    	} else if (userCoin.getAllCoins() < (int)price.get("totalJiuCoin")) {
	    		logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
	    				userCoin.getAllCoins(), (int)price.get("totalJiuCoin"));
	    		throw new UserCoinLessException();
	    	}
	    	
	    	int avalCoinUsed = userCoin.getAvalCoins() >= totalUnavalCoinUsed ? totalUnavalCoinUsed : userCoin.getAvalCoins();
	    	order.setAvalCoinUsed(avalCoinUsed);
	    	order.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
//    	}
    	
    	consumeWrapper.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
    	consumeWrapper.setCash(totalConsume);
        
    	return order;
    }


    
//    /**
//     * 玖币抵扣 新首单优惠
//     * @param userId
//     * @param skuCountMap
//     * @param productSkus 
//     * @param productMap <productId, Product>
//     * @param consumeWrapper
//     * @param address 
//     * @param productIds
//     * @param totalCash 
//     * @return
//     */
//    private Order buildOrder213(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
//    		Map<Long, Product> productMap, ConsumeWrapper consumeWrapper, Address address, Set<Long> productIds, double payCash, String couponId, String version) {
////      groupList按照仓库分类好之后 计算运费
//    	String cityName = address.getProvinceName() + address.getCityName();
//    	List<Map<String, Object>> groupList = wrapperGroupList(productSkus, productMap, skuCountMap, null ,userId);
//    	Map<Long, Double> subPostage = new HashMap<Long, Double>();
//    	double totalPostage = calculatePostage(groupList, cityName, subPostage);
//    	
//    	List<OrderDiscountLog> orderDiscountLogs = new ArrayList<OrderDiscountLog>();
////      计算商品费用
//    	Set<Long> brandIds = new HashSet<Long>();
//    	for (Product product : productMap.values()) {
//    		brandIds.add(product.getBrandId());
//    	}
//    	Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);
//    	Map<String, Object> price = calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, orderDiscountLogs ,userId, couponId);
//    	if(price.get("postFreeFlag") != null && price.get("postFreeFlag").equals("YES")){
//    		totalPostage = 0;
//    	}
//    	
//    	double totalConsume = (double)price.get("totalCash") + totalPostage;
//    	double couponCash = (double)price.get("couponCash");
//    	int deductCoinNum = (int)price.get("deductCoinNum");
//    	
//    	//如果和建立订单时的价格不一致
//    	if(payCash != totalConsume && payCash != -1) {
//    		throw new PayCheckNotEqualException();
//    	}
//    	
//    	
//    	
//    	long time = System.currentTimeMillis();
//    	List<OrderItem> orderItems = buildOrderItems17(userId, skuCountMap, productSkus, productMap, time);
//    	List<OrderItemGroup> orderItemGroups = buildOrderItemGroups17(userId, orderItems, time, subPostage);
//    	
//    	int totalUnavalCoinUsed = 0;
//    	for (OrderItemGroup group : orderItemGroups) {
//    		totalUnavalCoinUsed += group.getTotalUnavalCoinUsed();
//    	}
//    	
//    	Order order = new Order();
//    	order.setOrderNo(DateUtil.format(time, "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(8));
//    	order.setUserId(userId);
//    	order.setOrderStatus(OrderStatus.UNPAID);
//    	order.setTotalMoney((double)price.get("totalCash"));
//    	order.setTotalExpressMoney(totalPostage);
//    	order.setStatus(0);
//    	order.setCreateTime(time);
//    	order.setUpdateTime(time);
//    	order.setOrderItems(orderItems);
//    	order.setOrderItemGroups(orderItemGroups);
//    	order.setOrderDiscountLogs(orderDiscountLogs);
//    	
//    	order.setActualDiscount(couponCash);
//    	
//    	//计算订单支付过期时间
//    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//    	int expiredTime = 24 * 60;
//    	for(Object obj : jsonArray) {
//    		expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
//    		break;
//    	}
//    	
//    	for (ProductSKU sku : productSkus) {
//    		if(sku.getRemainKeepTime() > 0 && sku.getRemainKeepTime() < expiredTime){
//    			expiredTime = sku.getRemainKeepTime();
//    		}
//    	}
//    	order.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);
//    	
////    	if(VersionUtil.compareVersion(version , "1.8.11") < 0){
//    	
//    	UserCoin userCoin = userCoinService.getUserCoin(userId);
//    	if (userCoin == null) {
//    		logger.error("user coin can not null, userId:{}", userId);
//    		throw new UserCoinLessException();                            
//    	} else if (userCoin.getAllCoins() < (int)price.get("totalJiuCoin")) {
//    		logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
//    				userCoin.getAllCoins(), (int)price.get("totalJiuCoin"));
//    		throw new UserCoinLessException();
//    	}
//    	
//    	int avalCoinUsed = userCoin.getAvalCoins() >= totalUnavalCoinUsed ? totalUnavalCoinUsed : userCoin.getAvalCoins();
//    	order.setAvalCoinUsed(avalCoinUsed);
//    	order.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
////    	}
//    	
//    	consumeWrapper.setUnavalCoinUsed((int)price.get("totalJiuCoin"));
//    	consumeWrapper.setCash(totalConsume);
//    	
//    	return order;
//    }
//    /**
//     * 生成订单(包括包裹订单和单个子订单)
//     * @param userId
//     * @param skuCountMap
//     * @param productSkus 
//     * @param productMap <productId, Product>
//     * @param consumeWrapper
//     * @param address 
//     * @param productIds
//     * @param totalCash 
//     * @return
//     */
//
//    private Order buildOrderXX(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
//                             Map<Long, Product> productMap, ConsumeWrapper consumeWrapper) {
//        long time = System.currentTimeMillis();
//        List<OrderItem> orderItems = buildOrderItemsXX(userId, skuCountMap, productSkus, productMap, time);
//        List<OrderItemGroup> orderItemGroups = buildOrderItemGroupsXX(userId, orderItems, time);
//
//        int totalMoney = 0;
//        int totalExpressMoney = 0;
//        for (OrderItemGroup group : orderItemGroups) {
//            totalMoney += group.getTotalMoney();
//            totalExpressMoney += group.getTotalExpressMoney();
//        }
//
//        Order order = new Order();
//        //老订单表已经废弃，这个订单编号已经不再使用直接天真为零
////        order.setOrderNo(DateUtil.format(time, "yyyyMMddHHmmssSSS") + RandomStringUtils.randomNumeric(8));
//        order.setOrderNo("1000000000000000000000000");
//        order.setUserId(userId);
//        order.setOrderStatus(OrderStatus.UNPAID);
//        order.setTotalMoney(totalMoney);
//        order.setTotalExpressMoney(totalExpressMoney);
//        order.setStatus(0);
//        order.setCreateTime(time);
//        order.setUpdateTime(time);
//        order.setOrderItems(orderItems);
//        order.setOrderItemGroups(orderItemGroups);
//     
//        	
//	        int totalConsume = totalMoney + totalExpressMoney;
//	        UserCoin userCoin = userCoinService.getUserCoin(userId);
//	        if (userCoin == null) {
//	            logger.error("user coin can not null, userId:{}", userId);
//	            throw new UserCoinLessException();
//	        } else if (userCoin.getAllCoins() < totalConsume){
//	            logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
//	                userCoin.getAllCoins(), totalConsume);
//	            throw new UserCoinLessException();
//	        }
//	        
//	        int avalCoinUsed = userCoin.getAvalCoins() >= totalConsume ? totalConsume : userCoin.getAvalCoins();
//	        int unavalCoinUsed = totalConsume - avalCoinUsed;
//	        order.setAvalCoinUsed(avalCoinUsed);
//	        order.setUnavalCoinUsed(unavalCoinUsed);
//	        consumeWrapper.setUnavalCoinUsed(unavalCoinUsed);
//        
//
//        return order;
//    }

    //按仓库分组
    public List<Map<String, Object>> wrapperGroupList(List<ProductSKU> productSkus, Map<Long, Product> productMap,
                                                      Map<Long, Integer> skuCountMap,
                                                      Map<Long, List<ProductPropVO>> skuPropMap, long userId) {
		List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
		Map<Long, List<Map<String, Object>>> warehouseGroup = new HashMap<Long, List<Map<String, Object>>>();
		List<CartItemVO> cartItems  = null;
		cartItems = shoppingCartFacade.getCartItemVOs(userId);
		if(cartItems == null) {
			cartItems = new ArrayList<CartItemVO>();
		}

		for (ProductSKU sku : productSkus) {
            Product product = productMap.get(sku.getProductId());

            Map<String, Object> itemMap = new HashMap<String, Object>();
            Map<String, Object> skuMap = new HashMap<String, Object>();
            skuMap.put("id", sku.getId());
            skuMap.put("skuId", sku.getId());
            skuMap.put("buyCount", skuCountMap.get(sku.getId()));
            skuMap.put("price", sku.getPrice());
            skuMap.put("remainCount", sku.getRemainCount());
            if(product.getSetLOWarehouseId2() == 1 && skuCountMap.get(sku.getId()) <= sku.getRemainCount2()){
            	skuMap.put("remainCount", sku.getRemainCount2());
            }else{
            	skuMap.put("remainCount", sku.getRemainCount());   	
            }
            itemMap.put("sku", skuMap);
            itemMap.put("product", product.toSimpleMap15());
            itemMap.put("brand", brandService.getBrand(product.getBrandId()).toSimpleMap());
            
            if (skuPropMap != null) {
                List<ProductPropVO> skuProps = skuPropMap.get(sku.getId());
                skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
            }
            for(CartItemVO cartItem : cartItems){
            	if(cartItem.getSkuId() == sku.getId()){
            		itemMap.put("logIds", cartItem.getLogIds());
            	}
            }
            
            
        	List<Map<String, Object>> itemList = warehouseGroup.get(product.getlOWarehouseId());
            if (itemList == null) {
                Map<String, Object> groupMap = new HashMap<String, Object>();
                itemList = new ArrayList<Map<String, Object>>();
                warehouseGroup.put(product.getlOWarehouseId(), itemList);
                groupMap.put("itemList", itemList);
                if(product.getSetLOWarehouseId2() == 1 && skuCountMap.get(sku.getId()) <= sku.getRemainCount2()){
                	groupMap.put("warehouse", loWarehouseService.getById(product.getlOWarehouseId2()));    	
                }else{
                	groupMap.put("warehouse", loWarehouseService.getById(product.getlOWarehouseId()));
                }
//                groupMap.put("warehouse", loWarehouseService.getById(product.getlOWarehouseId()));
                groupList.add(groupMap);
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

	private List<OrderItem> buildOrderItems17(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
                                            Map<Long, Product> productMap, long time, double discountProportion, int deductCoinNum) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();

        List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        for (ProductSKU sku : productSkus) {
            List<ProductPropVO> skuProps = sku.getProductProps();
            composites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        productPropAssembler.assemble(composites);
        
        int userCoinNum = 0;
    	UserCoin userCoin =userCoinService.getUserCoin(userId);
    	if(userCoin != null){
    		userCoinNum = userCoin.getUnavalCoins();		
    	}

        for (ProductSKU sku : productSkus) {
            int buyCount = skuCountMap.get(sku.getId());
            Product product = productMap.get(sku.getProductId());
            

            
            
            
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(userId);
            orderItem.setProductId(product.getId());
            orderItem.setSkuId(sku.getId());
            //写入抵扣玖币数量
            if(product.getDeductPercent() > 0 && deductCoinNum > 0){
            	double counTemp = 10 * (product.getDeductPercent()/100) * product.getCurrenCash() * discountProportion;
            	int subDeductNum =(int)(buyCount * counTemp);
            	if(deductCoinNum < subDeductNum){
            		subDeductNum = deductCoinNum;
            	}
            	deductCoinNum -= subDeductNum;
            	orderItem.setDeductCoinNum(subDeductNum);
            	
            }

//            修改价格规则
            orderItem.setMoney(product.getCurrenCash());
            if(product.getCurrentJiuCoin() > 0){
            	orderItem.setUnavalCoinUsed(product.getCurrentJiuCoin());
            	orderItem.setTotalMoney(0);
            	
            }else{
	            orderItem.setUnavalCoinUsed(0);
	            orderItem.setTotalMoney(product.getCurrenCash() * buyCount);
            	
            }
            orderItem.setWholesaleType(product.getType());
            orderItem.setTotalUnavalCoinUsed(product.getCurrentJiuCoin() * buyCount);
            orderItem.setExpressMoney(0);
            orderItem.setTotalExpressMoney(0);
            orderItem.setBuyCount(buyCount);
            orderItem.setSkuSnapshot(buildSkuSnapshot(sku, skuPropMap));
            orderItem.setStatus(0);
            orderItem.setCreateTime(time);
            orderItem.setUpdateTime(time);
            orderItem.setBrandId(product.getBrandId());
            orderItem.setlOWarehouseId(product.getlOWarehouseId());
            orderItem.setMarketPrice(product.getMarketPrice());
            orderItem.setTotalMarketPrice(buyCount * product.getMarketPrice());
            orderItem.setPosition(sku.getPosition());
            if(product.getSetLOWarehouseId2() == 1 && sku.getRemainCount2() >= buyCount){
            	orderItem.setlOWarehouseId(product.getlOWarehouseId2());
            	orderItems.add(orderItem);
            }else{
            	orderItems.add(orderItem);
            	
            }
            
//            orderItems.add(orderItem);
        }

        return orderItems;
    }

	

//    private List<OrderItem> buildOrderItemsXX(long userId, Map<Long, Integer> skuCountMap, List<ProductSKU> productSkus,
//                                            Map<Long, Product> productMap, long time) {
//        List<OrderItem> orderItems = new ArrayList<OrderItem>();
//
//        List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
//        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
//        for (ProductSKU sku : productSkus) {
//            List<ProductPropVO> skuProps = sku.getProductProps();
//            composites.addAll(skuProps);
//            skuPropMap.put(sku.getId(), skuProps);
//        }
//        productPropAssembler.assemble(composites);
//
//        for (ProductSKU sku : productSkus) {
//            int buyCount = skuCountMap.get(sku.getId());
//            Product prodcut = productMap.get(sku.getProductId());
//            
//            OrderItem orderItem = new OrderItem();
//            orderItem.setUserId(userId);
//            orderItem.setProductId(prodcut.getId());
//            orderItem.setSkuId(sku.getId());
//            orderItem.setMoney(sku.getPrice());
//            orderItem.setExpressMoney(0);
//            orderItem.setTotalMoney(sku.getPrice() * buyCount);
//            orderItem.setTotalExpressMoney(0);
//            orderItem.setBuyCount(buyCount);
//            orderItem.setSkuSnapshot(buildSkuSnapshot(sku, skuPropMap));
//            orderItem.setStatus(0);
//            orderItem.setCreateTime(time);
//            orderItem.setUpdateTime(time);
//            orderItem.setBrandId(prodcut.getBrandId());
//            
//            orderItems.add(orderItem);
//        }
//
//        return orderItems;
//    }

	
	
    private String buildSkuSnapshot(ProductSKU sku, Map<Long, List<ProductPropVO>> skuPropMap) {
        StringBuilder builder = new StringBuilder();
        List<ProductPropVO> props = skuPropMap.get(sku.getId());
        for (ProductPropVO prop : props) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }
    
    private List<OrderItemGroup> buildOrderItemGroups17(long userId, List<OrderItem> orderItems, long time, Map<Long, Double> subPostage) {
        List<OrderItemGroup> result = new ArrayList<OrderItemGroup>();
        Map<Long, OrderItemGroup> groupMap = new HashMap<Long, OrderItemGroup>();
        for (OrderItem item : orderItems) {
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
                group.setTotalExpressMoney(subPostage.size() < 1 ? 0 :subPostage.get(warehouseId));
            }
            group.setTotalMoney(group.getTotalMoney() + item.getTotalMoney());
            group.setTotalUnavalCoinUsed(group.getTotalUnavalCoinUsed() + item.getTotalUnavalCoinUsed());
        }
        return result;
    }

    
    
//    private List<OrderItemGroup> buildOrderItemGroupsXX(long userId, List<OrderItem> orderItems, long time) {
//        List<OrderItemGroup> result = new ArrayList<OrderItemGroup>();
//        Map<Long, OrderItemGroup> groupMap = new HashMap<Long, OrderItemGroup>();
//        for (OrderItem item : orderItems) {
//            long brandId = item.getBrandId();
//            OrderItemGroup group = groupMap.get(brandId);
//            if (group == null) {
//                group = new OrderItemGroup();
//                group.setUserId(userId);
//                group.setBrandId(brandId);
//                group.setStatus(0);
//                group.setCreateTime(time);
//                group.setUpdateTime(time);
//
//                result.add(group);
//                groupMap.put(brandId, group);
//            }
//            group.setTotalMoney(group.getTotalMoney() + item.getTotalMoney());
//            group.setTotalExpressMoney(group.getTotalExpressMoney() + item.getTotalExpressMoney());
//        }
//        return result;
//    }
    //删除旧表 很明显该接口已经废弃
//    @Deprecated
//    @Transactional(rollbackFor = Exception.class)
//    public void createOrder(Order order, OrderType orderType, String remark, Address address,
//                            ClientPlatform clientPlatform, String ip) {
//        if (orderType == null) {
//            String msg = "order type can not be null";
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        order.setRemark(remark);
//        order.setExpressInfo(address.getExpressInfo());
//        order.setPlatform(clientPlatform.getPlatform().getValue());
//        order.setPlatformVersion(clientPlatform.getVersion());
//        order.setIp(ip);
//
//        order.setOrderType(orderType);
//        int payAmountInCents = getPayAmountInCentsXX(order, orderType);
//        order.setPayAmountInCents(payAmountInCents);
//        order.setOrderStatus(payAmountInCents == 0 ? orderType == OrderType.SEND_BACK ? OrderStatus.UNCHECK
//                        : OrderStatus.PAID : OrderStatus.UNPAID);
//
//        orderService.createOrderXX(order);
//
//        if(orderHandlers != null) {
//            for (OrderHandler handler : orderHandlers) {
//                handler.postOrderCreation(order, clientPlatform.getVersion());
//            }
//        }
//    }
    
    
    //删除旧表
//    public void createOrder17(Order order, OrderType orderType, String expressSupplier, String expressOrderNo,
//            String phone, String remark, Address address, ClientPlatform clientPlatform, String ip ) {
//    	createOrder17( order,  orderType,  expressSupplier,  expressOrderNo,
//                 phone,  remark,  address,  clientPlatform,  ip, null ,0L);
//    }
    
    
    @Transactional(rollbackFor = Exception.class)
    public long createOrderNew17(Order order  , Address address, ClientPlatform clientPlatform, String ip, String couponId,long userSharedRecordId,int takeGood) {
    	
//        if (orderType == null) {
//            String msg = "order type can not be null";
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        } else if (orderType == OrderType.SEND_BACK) {
//            if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//                StringUtils.isBlank(phone)) {
//                String msg =
//                    "send back order is leak of args, expressSupplier:" + expressSupplier + ", expressOrderNo:" +
//                        expressOrderNo + ", phone:" + phone;
//                logger.error(msg);
//                throw new IllegalArgumentException(msg);
//            }
//        }

//        order.setRemark(remark);
        order.setExpressInfo(address == null? "到店取货,无,到店取货":address.getExpressInfo());
        order.setPlatform(clientPlatform.getPlatform().getValue());
        order.setPlatformVersion(clientPlatform.getVersion());
        order.setIp(ip);

//        order.setOrderType(orderType);
        double totalConsume = order.getTotalMoney() + order.getTotalExpressMoney();
        order.setOrderStatus(totalConsume > 0.001 ? OrderStatus.UNPAID : OrderStatus.PAID);

        //删除回寄配置费用计算
//        int payAmountInCents = getPayAmountInCents17(order, orderType);
//        order.setPayAmountInCents(payAmountInCents);
        
        //如果是0元购，则插入日志0-10订单状态
        OrderNew orderNew = null;
        if(totalConsume > 0.001) {//非0元购
        	order.setOrderStatus(OrderStatus.UNPAID);
        	//orderService.createOrder17(order);
        	//1.8新订单创建
        	orderNew = orderService.createOrder18(order, couponId,userSharedRecordId,takeGood);
        } else {//0元购
        	order.setOrderStatus(OrderStatus.PAID);
        	//orderService.createOrder17(order);

        	orderNew = orderService.createOrder18(order, couponId,userSharedRecordId,takeGood);
//        	0元购则立即拆掉，非0元购会在支付成功后拆单
        	orderService.splitOrderNew(order.getUserId(), orderNew.getOrderNo());

        	

        	
        	//添加支付日志
        	OrderNewLog orderNewLog = new OrderNewLog();
        	orderNewLog.setUserId(orderNew.getUserId());
        	orderNewLog.setOrderNo(orderNew.getOrderNo());
        	orderNewLog.setNewStatus(OrderStatus.PAID);
        	orderNewLog.setOldStatus(OrderStatus.UNPAID);
        	orderNewLog.setCreateTime(System.currentTimeMillis());
        	orderNewLogMapper.addOrderLog(orderNewLog);
        	
        }

        if (orderHandlers != null) {
            for (OrderHandler handler : orderHandlers) {
                handler.postOrderCreation(order, clientPlatform.getVersion());
            }
        }
        
        return orderNew.getOrderNo() ;
    }
    //删除旧表 很明显该接口已经废弃
//    @Deprecated
//    @Transactional(rollbackFor = Exception.class)
//    public void createOrder(Order order, OrderType orderType, String expressSupplier, String expressOrderNo,
//                            String phone, String remark, Address address, ClientPlatform clientPlatform, String ip) {
//        if (orderType == null) {
//            String msg = "order type can not be null";
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        } else if (orderType == OrderType.SEND_BACK) {
//            if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//                StringUtils.isBlank(phone)) {
//                String msg =
//                    "send back order is leak of args, expressSupplier:" + expressSupplier + ", expressOrderNo:" +
//                        expressOrderNo + ", phone:" + phone;
//                logger.error(msg);
//                throw new IllegalArgumentException(msg);
//            }
//        }
//
//        order.setRemark(remark);
//        order.setExpressInfo(address.getExpressInfo());
//        order.setPlatform(clientPlatform.getPlatform().getValue());
//        order.setPlatformVersion(clientPlatform.getVersion());
//        order.setIp(ip);
//
//        order.setOrderType(orderType);
//        int payAmountInCents = getPayAmountInCentsXX(order, orderType);
//        order.setPayAmountInCents(payAmountInCents);
//        order.setOrderStatus(payAmountInCents == 0 ? orderType == OrderType.SEND_BACK ? OrderStatus.UNCHECK
//                        : OrderStatus.PAID : OrderStatus.UNPAID);
//
//        orderService.createOrderXX(order);
//        if (orderType == OrderType.SEND_BACK) {
//            orderService.addSendBackUncheck(order, expressSupplier, expressOrderNo, phone);
//        }
//
//        if (orderHandlers != null) {
//            for (OrderHandler handler : orderHandlers) {
//                handler.postOrderCreation(order ,clientPlatform.getVersion());
//            }
//        }
//    }

//    public int getPayAmountInCents17(Order order, OrderType orderType) {
//        int payAmountInCents = 0;
//        if (orderType == OrderType.PAY) {
//        	payAmountInCents = (int) ((order.getTotalMoney() + order.getTotalExpressMoney()) * 100);
//        } else if (orderType == OrderType.SEND_BACK) {
//            for (OrderItem item : order.getOrderItems()) {
//                payAmountInCents += orderType.getPayCentsPerUnit() * item.getBuyCount();
//            }
//        }
//        return payAmountInCents;
//    }

    //删除旧表该功能已经废弃
//    @Deprecated
//    public int getPayAmountInCentsXX(Order order, OrderType orderType) {
//        int payAmountInCents = 0;
//        if (orderType == OrderType.PAY) {
//            payAmountInCents = order.getUnavalCoinUsed() * orderType.getPayCentsPerUnit();
//        } else if (orderType == OrderType.SEND_BACK) {
//            for (OrderItem item : order.getOrderItems()) {
//                payAmountInCents += orderType.getPayCentsPerUnit() * item.getBuyCount();
//            }
//        }
//        return payAmountInCents;
//    }
    
    
    //删除旧表，如果有调用再打开
//    @Transactional(rollbackFor = Exception.class)
//    public void cancelOrder(Order order, String version) {
//        if (!order.canCancel()) {
//            String msg =
//                "order status error, orderNo:" + order.getOrderNo() + ", order status:" + order.getOrderStatus();
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        long time = System.currentTimeMillis();
//        List<OrderItem> orderItems =
//            orderService.getOrderItems(order.getUserId(), CollectionUtil.createList(order.getId()));
//        order.setOrderItems(orderItems);
//        if (order.getOrderStatus() == OrderStatus.UNPAID) {
//            orderService.cancelOrder(order, time);
//        } else {
//            orderService.updateOrderStatus(order, OrderStatus.REFUNDING, order.getOrderStatus(), time);
//        }
//
//        if (orderHandlers != null) {
//            for (OrderHandler handler : orderHandlers) {
//                handler.postOrderCancel(order, version);
//            }
//        }
//    }
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderNew(OrderNew orderNew, String version) {
    	if(orderNew.getOrderStatus() != OrderStatus.UNPAID){
    		 String msg = "order status error, orderNo:" + orderNew.getOrderNo() + ", order status:" + orderNew.getOrderStatus();
    	            logger.error(msg);
    	            throw new IllegalArgumentException(msg);
    	}
    	
    	long time = System.currentTimeMillis();
    	List<OrderItem> orderItems =
    			orderService.getOrderNewItems(orderNew.getUserId(), CollectionUtil.createList(orderNew.getOrderNo()));
    	
    	orderNew.setOrderItems(orderItems);
    	
    	if (orderNew.getOrderStatus() == OrderStatus.UNPAID) {
    		orderService.cancelOrderNew(orderNew, time);
    		//退回优惠券
    		List<Coupon> couponList = orderService.getUserCouponListByOrderNo(orderNew.getOrderNo());
    		if(couponList != null && couponList.size() > 0){
    			orderService.updateCouponUnuse(orderNew.getOrderNo());
    			for(Coupon coupon : couponList){
    				CouponUseLog couponUseLog = new CouponUseLog();
    	    		couponUseLog.setOrderNo(orderNew.getOrderNo());
    	    		couponUseLog.setUserId(orderNew.getUserId());
    	    		couponUseLog.setCouponId(coupon.getId());
    	    		couponUseLog.setStatus(1);
    	    		couponUseLog.setActualDiscount(coupon.getActualDiscount());
    	    		couponUseLog.setCreateTime(System.currentTimeMillis());
    	    		orderService.insertCouponUseLog(couponUseLog);
    			}
    		}
    		
    		
    		
    	} else {
    		orderService.updateOrderNewStatus(orderNew, OrderStatus.REFUNDING, orderNew.getOrderStatus(), time);
    	}
    	
    	if (orderHandlers != null) {
    		for (OrderHandler handler : orderHandlers) {
    			handler.postOrderNewCancel(orderNew,version);
    		}
    	}
    }
    
    
    
    
    //删除旧表
//    public List<OrderItemVO> getOrderItemVOs(long userId, long orderId) {
//        Map<Long, List<OrderItemVO>> map = getOrderItemVOMap(userId, CollectionUtil.createList(orderId));
//        return map.get(orderId);
//    }
//    public List<OrderItemVO> getOrderItemVOs(long userId, Order order) {
//        Map<Long, List<OrderItemVO>> map = getOrderItemVOMap(userId, CollectionUtil.createList(order.getId()));
//        return map.get(order.getId());
//    }
    
    
    
    //删除旧表
//    public Map<Long, List<OrderItemVO>> getOrderItemVOMap(long userId, Collection<Long> orderNos) {
//    	//在OrderItem里获取具体的订单中的商品
//        List<OrderItem> orderItems = orderService.getOrderItems(userId, orderNos);
//        Map<Long, List<OrderItemVO>> result = new HashMap<Long, List<OrderItemVO>>();
//        List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
//        for (OrderItem orderItem : orderItems) {
//            OrderItemVO vo = new OrderItemVO();
//            BeanUtil.copyProperties(vo, orderItem);
//
//            long orderId = orderItem.getOrderId();
//            List<OrderItemVO> list = result.get(orderId);
//            if (list == null) {
//                list = new ArrayList<OrderItemVO>();
//                result.put(orderId, list);
//            }
//            list.add(vo);
//            composites.add(vo);
//        }
//        productAssembler.assemble(composites);
//
//        return result;
//    }
    
    public List<OrderItemVO> getOrderNewItemVOs(long userId, long orderNo) {
        Map<Long, List<OrderItemVO>> map = getOrderNewItemVOMap(userId, CollectionUtil.createList(orderNo));
        return map.get(orderNo);
    }
    
    public Map<Long, List<OrderItemVO>> getOrderNewItemVOMap(long userId, Collection<Long> orderNos) {
    	//在OrderItem里获取具体的订单中的商品
    	List<OrderItem> orderItems = orderService.getOrderNewItems(userId, orderNos);
    	Map<Long, List<OrderItemVO>> result = new HashMap<Long, List<OrderItemVO>>();
    	
    	List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
    	for (OrderItem orderItem : orderItems) {
    		OrderItemVO vo = new OrderItemVO();
    		BeanUtil.copyProperties(vo, orderItem);
    		
    		long orderNo = orderItem.getOrderNo();
    		List<OrderItemVO> list = result.get(orderNo);
    		if (list == null) {
    			list = new ArrayList<OrderItemVO>();
    			result.put(orderNo, list);
    			
    		}
    		list.add(vo);
    		composites.add(vo);
    	}
    	productAssembler.assemble(composites);
    	
    	return result;
    }
    
    public Map<Long, OrderItemVO> getServiceItemVOMap(long userId, Collection<Long> orderItemIds) {
    	//在OrderItem里获取具体的订单中的商品
    	List<OrderItem> orderItems = orderService.getOrderNewItemsByItemIds(userId, orderItemIds);
    	Set<Long> skuIds = new HashSet<Long>();
    	for (OrderItem orderItem : orderItems) {
    		skuIds.add(orderItem.getSkuId());
    	}
    	List<ProductSKU> productSKUList = new ArrayList<ProductSKU>();
    	if(skuIds.size()>0){
    		productSKUList = productSKUService.getProductSKUs(skuIds);
    	}
    	Map<Long, OrderItemVO> result = new HashMap<Long, OrderItemVO>();
    	
    	List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
    	for (OrderItem orderItem : orderItems) {
    		if(productSKUList != null && productSKUList.size() > 0){
    			for (ProductSKU productSKU : productSKUList) {
    				if (productSKU.getId() == orderItem.getSkuId()){
    					orderItem.setProductSKU(productSKU);
    					break;
    				}
    			}
    		}
    		OrderItemVO vo = new OrderItemVO();
    		BeanUtil.copyProperties(vo, orderItem);
    		
    		long itemId = orderItem.getId();
    		OrderItemVO orderItemVO = result.get(itemId);
    		if (orderItemVO == null) {
    			result.put(itemId, vo);
    			
    		}
    		composites.add(vo);
    	}
    	productAssembler.assemble(composites);
    	
    	return result;
    }
    
    public Map<Long, List<OrderNewVO>> getChildOrderMap(long userId, Collection<Long> orderNOs) {
    	//在OrderItem里获取具体的订单中的商品
    	List<OrderItemVO> orderItemList;
     	List<OrderNewVO> childOrderList = orderService.getChildOrderList(userId, orderNOs);
    	Map<Long, List<OrderNewVO>> result = new HashMap<Long, List<OrderNewVO>>();
    	 for (OrderNewVO orderNew : childOrderList) {
             long parentId = orderNew.getParentId();
             List<OrderNewVO> list = result.get(parentId);
             if (list == null) {
                 list = new ArrayList<OrderNewVO>();
                 result.put(parentId, list);
             }
             orderItemList = orderService.getOrderNewItemsVO(userId, CollectionUtil.createSet(orderNew.getOrderNo()));
             orderNew.setOrderItems(new ArrayList<OrderItem>(orderItemList));
             list.add(orderNew);
        
         }
    	
    	return result;
    }
    //删除旧表
//    public Map<Long, List<OrderItemGroupVO>> getOrderItemGroupVOMap(long userId, Collection<Long> orderIds,
//                                                                    Map<Long, List<OrderItemVO>> orderItemsMap) {
//
//        List<OrderItemGroup> orderItemGroups = orderService.getOrderItemGroups(userId, orderIds);
//        Map<Long, List<OrderItemGroupVO>> result = new HashMap<Long, List<OrderItemGroupVO>>();
//        List<OrderItemGroupVO> composites = new ArrayList<OrderItemGroupVO>();
//        for (OrderItemGroup orderItemGroup : orderItemGroups) {
//            OrderItemGroupVO vo = new OrderItemGroupVO();
//            BeanUtil.copyProperties(vo, orderItemGroup);
//            long id = orderItemGroup.getId();
//            long orderId = orderItemGroup.getOrderId();
//            List<OrderItemVO> orderItemVOs = new ArrayList<OrderItemVO>();
//            for(OrderItemVO orderItemVO : orderItemsMap.get(orderId)) {
//            	if(orderItemVO.getGroupId() == id) {
//            		orderItemVOs.add(orderItemVO);
//            	}
//            }
//            vo.setOrderItems(orderItemVOs);
//
//            List<OrderItemGroupVO> list = result.get(orderId);
//            if (list == null) {
//                list = new ArrayList<OrderItemGroupVO>();
//                result.put(orderId, list);
//            }
//            list.add(vo);
//            composites.add(vo);
//        }
//        brandAssembler.assemble(composites);
//
//        return result;
//    }

    @SuppressWarnings("unused")
    private Map<Long, List<OrderItemVO>> splitAsBrandMap(List<OrderItemVO> orderItemVOs) {
        Map<Long, List<OrderItemVO>> result = new HashMap<Long, List<OrderItemVO>>();
        for (OrderItemVO vo : orderItemVOs) {
            long brandId = vo.getBrandId();
            List<OrderItemVO> list = result.get(brandId);
            if (list == null) {
                list = new ArrayList<OrderItemVO>();
                result.put(brandId, list);
            }
            list.add(vo);
        }
        return result;
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
    	LOLocation location = loLocationService.getByName(cityName);
    	if(location == null) {
    		throw new DeliveryLocationNotFoundException();
    	}
    	
    	int totalPostage = 0;
    	int distributionLocationId = location.getId();
    	for(Map<String, Object> groupMap : groupList) {
    		//这个包裹里有几件商品
    		int totalBuyCount = 0;
    		LOWarehouse loWarehouse = (LOWarehouse)groupMap.get("warehouse");
    		
    		for(Map<String, Object> itemMap: (List<Map<String, Object>>) groupMap.get("itemList")) {
    			Map<String, Object> skuMap = (Map<String, Object>)itemMap.get("sku");
    			totalBuyCount += (int)skuMap.get("buyCount");
    		}
    		
    		//如果满足包邮条件
    		if(loWarehouse.getIsFree() == Logistics.DISCOUNT.getIntValue() && loWarehouse.getFreeCount() <= totalBuyCount) {
    			groupMap.put("itemPostage", 0.00);
    			subPostage.put(loWarehouse.getId(), 0.00);
    			continue;
    		}
    		
    		LOPostage loPostage = loPostageService.getPostage(loWarehouse.getDeliveryLocation(), distributionLocationId);
    		if(loPostage == null) {
    			throw new PostageNotFoundException();
    		}
    		subPostage.put(loWarehouse.getId(), loPostage.getPostage());
			totalPostage += loPostage.getPostage();
			groupMap.put("itemPostage", loPostage.getPostage());
    	}
    	
		return totalPostage;
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
    public Map<String, Object> calculateDiscount(List<ProductSKU> productSkus, Set<Long> productIds,
			Map<Long, Product> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs) {
		Map<String, Object> price = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> virtualGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> categoryGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	List<Map<String, Object>> normalGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	//新增字段,获取商品对应的虚拟分类,不存在则为null
        Map<Long, Category> categoryMap = productCategoryService.getProductVirtualCategory(productIds);
        List<ProductSKU> virtualProductSKUs = new ArrayList<ProductSKU>();
        List<ProductSKU> normalProductSKUs = new ArrayList<ProductSKU>();
        
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
        		
    	
    	//虚拟分类包装
    	for(ProductSKU virtualSKU: virtualProductSKUs) {
            Product product = productMap.get(virtualSKU.getProductId());

            Map<String, Object> itemMap = new HashMap<String, Object>();
            itemMap.put("skuId", virtualSKU.getId());
            itemMap.put("buyCount", skuCountMap.get(virtualSKU.getId()));
            itemMap.put("remainCount", virtualSKU.getRemainCount());
            itemMap.put("cash", product.getCurrenCash());
            itemMap.put("jiuCoin", product.getCurrentJiuCoin());
            
            Category virtualCat = categoryMap.get(virtualSKU.getProductId());
        	List<Map<String, Object>> itemList = categoryGroup.get(virtualCat.getId());
            if (itemList == null) {
                Map<String, Object> groupMap = new HashMap<String, Object>();
                itemList = new ArrayList<Map<String, Object>>();
                categoryGroup.put(virtualCat.getId(), itemList);
                groupMap.put("itemList", itemList);
                groupMap.put("category", virtualCat);
                virtualGroupList.add(groupMap);
            }
            itemList.add(itemMap);
    	}
    	
    	//普通分类封装
    	for(ProductSKU normalSKU : normalProductSKUs) {
            Product product = productMap.get(normalSKU.getProductId());

            Map<String, Object> itemMap = new HashMap<String, Object>();
            
            itemMap.put("skuId", normalSKU.getId());
            itemMap.put("buyCount", skuCountMap.get(normalSKU.getId()));
            itemMap.put("remainCount", normalSKU.getRemainCount());
            itemMap.put("cash", product.getCurrenCash());
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
            }
            itemList.add(itemMap);
    	}
    	
    	double totalCash = 0;
    	int totalJiuCoin = 0;
    	//不打折应付
    	double prepay = 0;
    	//优惠值
    	double discountCash = 0;
    	//虚拟分类商品的价格统计
    	for(Map<String, Object> virtualGroupMap: virtualGroupList) {
    		Category category = (Category)virtualGroupMap.get("category");
    		
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
    			totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    		}
    		
    		if(category.getIsDiscount() == 1 && subTotalCash >= category.getExceedMoney()) {
    			subTotalCash -= category.getMinusMoney(); 
    			discountCash += category.getMinusMoney(); 
    			
    			if(orderDiscountLogs != null) {
    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    				orderDiscountLog.setComment("满" + category.getExceedMoney() + "减" + category.getMinusMoney());
    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
    				orderDiscountLog.setType(1);
    				orderDiscountLog.setDiscount(category.getMinusMoney());
    				orderDiscountLog.setRelatedId(category.getId());
    				
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
    			totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    		}
    		
    		if(brand.getIsDiscount() == 1 && subTotalCash >= brand.getExceedMoney()) {
    			subTotalCash -= brand.getMinusMoney(); 
    			discountCash += brand.getMinusMoney(); 
    			
    			if(orderDiscountLogs != null) {
    				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    				orderDiscountLog.setComment("满" + brand.getExceedMoney() + "减" + brand.getMinusMoney());
    				orderDiscountLog.setCreateTime(System.currentTimeMillis());
    				orderDiscountLog.setType(0);
    				orderDiscountLog.setDiscount(brand.getMinusMoney());
    				orderDiscountLog.setRelatedId(brand.getId());
    				
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    		}
    		totalCash += subTotalCash;
    	}
    	
    	//市场总价
    	price.put("originalPrice", originalPrice);
    	//不打折的俞姐姐APP总价(不包含邮费)
    	price.put("prepay", prepay);
    	//打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	price.put("totalCash", totalCash);
    	//比市场参考价便宜了多少,玖币抵扣
    	price.put("jiuCoinDiscount", originalPrice - totalCash);
    	//满减活动折扣的金额数目，优惠抵扣
    	price.put("discountCash", discountCash);
    	
    	//所需的玖币价
    	price.put("totalJiuCoin", totalJiuCoin);
    	
		return price;
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
    		Map<Long, Product> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long userId) {
    	
    	return calculateDiscountV185( productSkus, productIds,
        		 productMap,  skuCountMap, brandMap,  orderDiscountLogs,  userId,  null);
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
    public boolean HasCouponLimit(boolean otherCouponFlag, Set<Long> productIds, Map<Long, Product> productMap, double totalMoney) {
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
        		        					List<Category> categorieListAll = categoryService.getCategories();
        		        					for(String ctgrId : idsArr){
        		        						limitIdsList.add(ctgrId);
        		        					}
        		        					//添加子分类到列表
        		        					for(Category category : categorieListAll){
        		        						//添加子分类
        		        						for(Category categoryTemp : categorieListAll){
        		        							if(categoryTemp.getParentId() == category.getId()){
        		        								category.getChildCategories().add(categoryTemp);
        		        							}
        		        						}
        		        						for(String ctgrId : idsArr){
        		        							if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
        		        								for(Category childCtgy : category.getChildCategories()){
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
    		Map<Long, Product> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long userId, String couponId) {
    	Map<String, Object> price = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> virtualGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> categoryGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	List<Map<String, Object>> normalGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	//新增字段,获取商品对应的虚拟分类,不存在则为null
    	Map<Long, Category> categoryMap = productCategoryService.getProductVirtualCategory(productIds);
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
    	int userCoinNum = 0;
    	UserCoin userCoin =userCoinService.getUserCoin(userId);
    	if(userCoin != null){
    		userCoinNum = userCoin.getUnavalCoins();		
    	}
  
    	
    	//全场满减数据
    	 List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
         DiscountInfo discountInfoTemp;
         JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
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
    		Product product = productMap.get(virtualSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		itemMap.put("skuId", virtualSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(virtualSKU.getId()));
    		itemMap.put("remainCount", virtualSKU.getRemainCount());
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		if(product.getCurrentJiuCoin() > 0) {
    			itemMap.put("cash", 0.00);
    		} else {
    			itemMap.put("cash", product.getCurrenCash());
    		}
    		
    		Category virtualCat = categoryMap.get(virtualSKU.getProductId());
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
    		Product product = productMap.get(normalSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		
    		itemMap.put("skuId", normalSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(normalSKU.getId()));
    		itemMap.put("remainCount", normalSKU.getRemainCount());
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		if(product.getCurrentJiuCoin() > 0) {
    			itemMap.put("cash", 0.00);
    		} else {
    			itemMap.put("cash", product.getCurrenCash());
    		}
    		
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
    	
    	int totalJiuCoin = 0;
    	//不打折应付
    	double prepay = 0;
    	//优惠值
    	double discountCash = 0;
    	
    	
    	//首单优惠
    	double firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
    	
    	boolean couponConflict = false;
    	boolean otherCouponFlag = false;
    	int couponCoExist = 0;
//    	int notFreeShipCoupon = 0;
//    	int limitCouponNum = 0;
    	//优惠券
   // 	Coupon coupon = new Coupon();
    	List<Coupon> couponList = new ArrayList<Coupon>();
       	if(couponId != null && couponId.length() > 0 ) {

//    		coupon = orderService.getCouponById(Long.parseLong(couponId), userId);
//    		if(coupon != null && coupon.getIsLimit() != null && coupon.getIsLimit() == 1 && coupon.getStatus() == 0){
//    			couponConflict = true;
//    		}else if(coupon == null || coupon.getStatus() == null || coupon.getStatus() != 0){
//    			throw new CouponUnavailableException();
//    		}
    		couponList = orderService.getCouponByIdArr(couponId, userId);
    		for(Coupon couponTemp : couponList){
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
    		Category category = (Category)virtualGroupMap.get("category");
    		
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
    			
    			for(DiscountInfo discountInfo : discountInfoList){
    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    					maxFull = discountInfo.getFull();
    					minus = discountInfo.getMinus();
    					discountId = discountInfo.getId();
    				}
    			}
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
    			
    			for(DiscountInfo discountInfo : discountInfoList){
    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    					maxFull = discountInfo.getFull();
    					minus = discountInfo.getMinus();
    					discountId = discountInfo.getId();
    				}
    			}
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
    	
    	if (HasCouponLimit(otherCouponFlag, productIds, productMap, prepay)) throw new CouponUnavailableException();
    	
    	//计算全场满减优惠金额
    	if(allDiscountInfoList != null && allDiscountInfoList.size() > 0 && !couponConflict){
    		double maxFull = 0;
    		double minus = 0;
    		
			for(DiscountInfo discountInfo : allDiscountInfoList){
				if(totalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
					maxFull = discountInfo.getFull();
					minus = discountInfo.getMinus();
					//discountId = discountInfo.getId();
				}
			}
			
			if(minus < totalCash ){
    			totalCash -= minus;
    			discountCash += minus; 
    			
    		}else{
    			minus = totalCash;
    			totalCash = 0.0;
    			discountCash += minus; 
    		}
			if(orderDiscountLogs != null) {
				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
				orderDiscountLog.setComment("全场满" + maxFull + "减" + minus);
				orderDiscountLog.setCreateTime(System.currentTimeMillis());
				orderDiscountLog.setType(3);
				orderDiscountLog.setDiscount(minus);
				//orderDiscountLog.setRelatedId(discountId);
				
				orderDiscountLogs.add(orderDiscountLog);
			}
		}
    	
    	//计算优惠券减扣
    	double couponCash = 0;

    	for(Coupon coupon : couponList){
    		
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
    				if(coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
    					String limitIds = "";
    					String regEx = "\\[[^}]*\\]";
    					String str = coupon.getRangeContent(); //coupon.getRangeContent()
    					Pattern pat = Pattern.compile(regEx);
    					Matcher mat;
    					mat = pat.matcher(str);
    					while (mat.find()) {
    						limitIds = mat.group();
    					}
    					limitIds = limitIds.replaceAll("\\[", "").replaceAll("\\]", "");
    					double actualDiscount = 0;
    					
    					if(limitIds.length() > 0 ){
    						
    						String[] idsArr = limitIds.split(",");
    						if( coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
    							
    							List<String> idsList = new ArrayList<String>();
    							//子分类匹配
    							List<Category> categorieListAll = categoryService.getCategories();
    							for(String ctgrId : idsArr){
    								idsList.add(ctgrId);
    							}
    							//添加子分类到列表  
    							for(Category category : categorieListAll){
    								//添加子分类
    								for(Category categoryTemp : categorieListAll){
    									if(categoryTemp.getParentId() == category.getId()){
    										category.getChildCategories().add(categoryTemp);
    									}
    								}
    								for(String ctgrId : idsArr){
    									if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
    										for(Category childCtgy : category.getChildCategories()){
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
    											actualDiscount += productCountMap.get(productCategory.getProductId()) * productMap.get(productCategory.getProductId()).getCurrenCash();
    											productIdStr += "," + productCategory.getProductId() + ",";
    										}
    									}
    									
    								}
    								
    								//虚拟分类匹配
    								for(String arr : idsArr){
        								for (Product product : productMap.values()) {
        									if((product.getvCategoryId() + "").equals(arr)){
        										if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
        											actualDiscount += productCountMap.get(product.getId()) * product.getCurrenCash();
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
    											actualDiscount += productCountMap.get(product.getId()) * product.getCurrenCash();
    										}
    										
    									}
    									
    								}
    								
    							}
    						}
    						if( (coupon.getRangeType() == 1 || coupon.getRangeType() == 4) && idsArr != null && idsArr.length > 0){
	    						//限额优惠券检验使用金额是否达标
								if(coupon.getLimitMoney() > 0 && actualDiscount < coupon.getLimitMoney() ){
									actualDiscount = 0;
								}else if(coupon.getMoney() < actualDiscount){
									actualDiscount = coupon.getMoney();
								}
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
    				orderDiscountLog.setRelatedId(userId);
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    			//int updtNum = orderService.updateCouponUsed(coupon.getId());
    		}
    	}
	
    	
    	
    	
    	//计算首单优惠金额
    	int count = orderService.getUserOrderCountForFirstDiscount(userId);
    	if (count == 0 && totalCash > 0 ){
    		if(firstDiscountCash < totalCash ){
    			totalCash -= firstDiscountCash;
    		}else{
    			firstDiscountCash = totalCash;
    			totalCash = 0.0;
    		}
    		//discountCash += firstDiscountCash;
    		if(orderDiscountLogs != null && firstDiscountCash > 0) {
    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    			orderDiscountLog.setComment("首单优惠" + firstDiscountCash);
    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
    			orderDiscountLog.setType(2);
    			orderDiscountLog.setDiscount(firstDiscountCash);
    			orderDiscountLog.setRelatedId(userId);
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}else {
    		firstDiscountCash = 0.0;
    		
    	}
    	

    	
    
    	
    	//市场总价
    	price.put("originalPrice", originalPrice);
    	//优惠券优惠金额
    	price.put("couponCash", couponCash);
    	//玖币抵扣优惠金额
    	price.put("deductCoinNum", 0);

    	//不打折的俞姐姐APP总价(不包含邮费)
    	price.put("prepay", prepay);
    	//打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	price.put("totalCash", totalCash);
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
    public Map<String, Object> calculateDiscountV213(List<ProductSKU> productSkus, Set<Long> productIds,
    		Map<Long, Product> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long userId, String couponId, int coinDeductFlag) {
    	Map<String, Object> price = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> virtualGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> categoryGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	List<Map<String, Object>> normalGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	//新增字段,获取商品对应的虚拟分类,不存在则为null
    	Map<Long, Category> categoryMap = productCategoryService.getProductVirtualCategory(productIds);
    	List<ProductSKU> virtualProductSKUs = new ArrayList<ProductSKU>();
    	List<ProductSKU> normalProductSKUs = new ArrayList<ProductSKU>();
    	
    	Set<Long> virtualCategoryId = new HashSet<Long>();
    	Set<Long> brandIds = new HashSet<Long>();
    	
    	//市场总价
    	int originalPrice = 0;
    	
    	int deductCoinNumMax = 0;
    	
    	int deductCoinNum = 0;
    	
    	
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
    	int userCoinNum = 0;
    	UserCoin userCoin =userCoinService.getUserCoin(userId);
    	if(userCoin != null){
    		userCoinNum = userCoin.getUnavalCoins();		
    	}

    	
    	//全场满减数据
    	List<DiscountInfo> allDiscountInfoList = new ArrayList<DiscountInfo>();
    	DiscountInfo discountInfoTemp;
    	JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
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
    		Product product = productMap.get(virtualSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		itemMap.put("skuId", virtualSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(virtualSKU.getId()));
    		itemMap.put("remainCount", virtualSKU.getRemainCount());
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		if(product.getCurrentJiuCoin() > 0) {
    			itemMap.put("cash", 0.00);
    		} else {
    			itemMap.put("cash", product.getCurrenCash());
    		}
    		
    		Category virtualCat = categoryMap.get(virtualSKU.getProductId());
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
    		Product product = productMap.get(normalSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		
    		itemMap.put("skuId", normalSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(normalSKU.getId()));
    		itemMap.put("remainCount", normalSKU.getRemainCount());
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		if(product.getCurrentJiuCoin() > 0) {
    			itemMap.put("cash", 0.00);
    		} else {
    			itemMap.put("cash", product.getCurrenCash());
    		}
    		
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
    	double totalDiscountCash = 0;
    	int totalJiuCoin = 0;
    	//不打折应付
    	double prepay = 0;
    	//优惠值
    	double discountCash = 0;
    	
    	
    	//首单优惠
    	double firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
    	
    	boolean couponConflict = false;
    	boolean otherCouponFlag = false;
    	int couponCoExist = 0;
//    	int notFreeShipCoupon = 0;
//    	int limitCouponNum = 0;
    	//优惠券
    	// 	Coupon coupon = new Coupon();
    	List<Coupon> couponList = new ArrayList<Coupon>();
    	if(couponId != null && couponId.length() > 0 ) {
    		
//    		coupon = orderService.getCouponById(Long.parseLong(couponId), userId);
//    		if(coupon != null && coupon.getIsLimit() != null && coupon.getIsLimit() == 1 && coupon.getStatus() == 0){
//    			couponConflict = true;
//    		}else if(coupon == null || coupon.getStatus() == null || coupon.getStatus() != 0){
//    			throw new CouponUnavailableException();
//    		}
    		couponList = orderService.getCouponByIdArr(couponId, userId);
    		for(Coupon couponTemp : couponList){
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
    		Category category = (Category)virtualGroupMap.get("category");
    		
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
    			
    			for(DiscountInfo discountInfo : discountInfoList){
    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    					maxFull = discountInfo.getFull();
    					minus = discountInfo.getMinus();
    					discountId = discountInfo.getId();
    				}
    			}
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
    			
    			for(DiscountInfo discountInfo : discountInfoList){
    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    					maxFull = discountInfo.getFull();
    					minus = discountInfo.getMinus();
    					discountId = discountInfo.getId();
    				}
    			}
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
    	
    	if (HasCouponLimit(otherCouponFlag, productIds, productMap, prepay)) throw new CouponUnavailableException();
    	
    	//计算全场满减优惠金额
    	if(allDiscountInfoList != null && allDiscountInfoList.size() > 0 && !couponConflict){
    		double maxFull = 0;
    		double minus = 0;
    		
    		for(DiscountInfo discountInfo : allDiscountInfoList){
    			if(totalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    				maxFull = discountInfo.getFull();
    				minus = discountInfo.getMinus();
    				//discountId = discountInfo.getId();
    			}
    		}
    		
    		if(minus < totalCash ){
    			totalCash -= minus;
    			discountCash += minus; 
    			
    		}else{
    			minus = totalCash;
    			totalCash = 0.0;
    			discountCash += minus; 
    		}
    		if(orderDiscountLogs != null) {
    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    			orderDiscountLog.setComment("全场满" + maxFull + "减" + minus);
    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
    			orderDiscountLog.setType(3);
    			orderDiscountLog.setDiscount(minus);
    			//orderDiscountLog.setRelatedId(discountId);
    			
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}
    	
    	//计算优惠券减扣
    	double couponCash = 0;
    	
    	for(Coupon coupon : couponList){
    		
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
    				if(coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
    					String limitIds = "";
    					String regEx = "\\[[^}]*\\]";
    					String str = coupon.getRangeContent(); //coupon.getRangeContent()
    					Pattern pat = Pattern.compile(regEx);
    					Matcher mat;
    					mat = pat.matcher(str);
    					while (mat.find()) {
    						limitIds = mat.group();
    					}
    					limitIds = limitIds.replaceAll("\\[", "").replaceAll("\\]", "");
    					double actualDiscount = 0;
    					
    					if(limitIds.length() > 0 ){
    						
    						String[] idsArr = limitIds.split(",");
    						if( coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
    							
    							List<String> idsList = new ArrayList<String>();
    							//子分类匹配
    							List<Category> categorieListAll = categoryService.getCategories();
    							for(String ctgrId : idsArr){
    								idsList.add(ctgrId);
    							}
    							//添加子分类到列表  
    							for(Category category : categorieListAll){
    								//添加子分类
    								for(Category categoryTemp : categorieListAll){
    									if(categoryTemp.getParentId() == category.getId()){
    										category.getChildCategories().add(categoryTemp);
    									}
    								}
    								for(String ctgrId : idsArr){
    									if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
    										for(Category childCtgy : category.getChildCategories()){
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
    											actualDiscount += productCountMap.get(productCategory.getProductId()) * productMap.get(productCategory.getProductId()).getCurrenCash();
    											productIdStr += "," + productCategory.getProductId() + ",";
    										}
    									}
    									
    								}
    								
    								//虚拟分类匹配
    								for(String arr : idsArr){
    									for (Product product : productMap.values()) {
    										if((product.getvCategoryId() + "").equals(arr)){
    											if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
    												actualDiscount += productCountMap.get(product.getId()) * product.getCurrenCash();
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
    											actualDiscount += productCountMap.get(product.getId()) * product.getCurrenCash();
    										}
    										
    									}
    									
    								}
    								
    							}
    						}
    						if( (coupon.getRangeType() == 1 || coupon.getRangeType() == 4) && idsArr != null && idsArr.length > 0){
    							//限额优惠券检验使用金额是否达标
    							if(coupon.getLimitMoney() > 0 && actualDiscount < coupon.getLimitMoney() ){
    								actualDiscount = 0;
    							}else if(coupon.getMoney() < actualDiscount){
    								actualDiscount = coupon.getMoney();
    							}
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
    				orderDiscountLog.setRelatedId(userId);
    				orderDiscountLogs.add(orderDiscountLog);
    			}
    			//int updtNum = orderService.updateCouponUsed(coupon.getId());
    		}
    	}
    	
    	
    	
    	
    	//计算首单优惠金额
    	int count = orderService.getUserOrderCountForFirstDiscount(userId);
    	if (count == 0 && totalCash > 0 ){
    		if(firstDiscountCash < totalCash ){
    			totalCash -= firstDiscountCash;
    		}else{
    			firstDiscountCash = totalCash;
    			totalCash = 0.0;
    		}
    		//discountCash += firstDiscountCash;
    		if(orderDiscountLogs != null && firstDiscountCash > 0) {
    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    			orderDiscountLog.setComment("首单优惠" + firstDiscountCash);
    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
    			orderDiscountLog.setType(2);
    			orderDiscountLog.setDiscount(firstDiscountCash);
    			orderDiscountLog.setRelatedId(userId);
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}else {
    		firstDiscountCash = 0.0;
    		
    	}
    	
    	//计算玖币抵扣优惠金额
    	JSONObject jiuCoinAttribute = (JSONObject) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING).get("jiuCoinAttribute");
		double coinRatio = Double.parseDouble(jiuCoinAttribute.get("worthRmb").toString());
		if(coinRatio == 0){
			coinRatio =0.1;
		}
    	for(ProductSKU productSKU : productSkus) {
    		
    		Product product = productMap.get(productSKU.getProductId());
    		count = skuCountMap.get(productSKU.getId());
    		if(product.getDeductPercent() > 0 && prepay > 0){
    			double counTemp =  (product.getDeductPercent()/100) * product.getCurrenCash() * totalCash / prepay / coinRatio;
    			deductCoinNumMax += Math.floor(count * counTemp);
    			//System.out.println(product.getId()+product.getName()+"@"+deductCoinNumMax);
    		}
    	}
    	deductCoinNum = deductCoinNumMax;
    	if(deductCoinNumMax > userCoinNum){
    		deductCoinNum = userCoinNum;
    	}
    	double discountProportion = 1;
    	if(prepay > 0){
    		discountProportion = totalCash / prepay;
    	}
    	if(deductCoinNum > 0 && totalCash > 0 && deductCoinNum * coinRatio > totalCash ){
			deductCoinNum = (int) (totalCash * coinRatio);
		}
    	if (deductCoinNum > 0 && totalCash > 0 && coinDeductFlag == 1 ){
   		
    		totalCash -= deductCoinNum * coinRatio;
    			
    		
    		discountCash += deductCoinNum * coinRatio;
    		if(orderDiscountLogs != null && firstDiscountCash > 0) {
    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
    			orderDiscountLog.setComment("玖币抵扣" + deductCoinNum * coinRatio);
    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
    			orderDiscountLog.setType(4);
    			orderDiscountLog.setDiscount(deductCoinNum * coinRatio);
    			orderDiscountLog.setRelatedId(userId);
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}
//    	//计算玖币抵扣优惠金额
//    	for(ProductSKU productSKU : productSkus) {
//    		
//    		Product product = productMap.get(productSKU.getProductId());
//    		count = skuCountMap.get(productSKU.getId());
//    		if(product.getDeductPercent() > 0 && prepay > 0){
//    			double counTemp = 10 * (product.getDeductPercent()/100) * product.getCurrenCash() * totalCash / prepay;
//    			deductCoinNumMax += Math.floor(count * counTemp);
//    			//System.out.println(product.getId()+product.getName()+"@"+deductCoinNumMax);
//    		}
//    	}
//    	deductCoinNum = deductCoinNumMax;
//    	if(deductCoinNumMax > userCoinNum){
//    		deductCoinNum = userCoinNum;
//    	}
//    	double discountProportion = 1;
//    	if(prepay > 0){
//    		discountProportion = totalCash / prepay;
//    	}
//    	if(deductCoinNum > 0 && totalCash > 0 && deductCoinNum * 0.1 > totalCash ){
//			deductCoinNum = (int) totalCash / 10;
//		}
//    	if (deductCoinNum > 0 && totalCash > 0 && coinDeductFlag == 1 ){
//   		
//    		totalCash -= deductCoinNum * 0.1;
//    			
//    		
//    		discountCash += deductCoinNum * 0.1;
//    		if(orderDiscountLogs != null && firstDiscountCash > 0) {
//    			OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
//    			orderDiscountLog.setComment("玖币抵扣" + deductCoinNum * 0.1);
//    			orderDiscountLog.setCreateTime(System.currentTimeMillis());
//    			orderDiscountLog.setType(4);
//    			orderDiscountLog.setDiscount(deductCoinNum * 0.1);
//    			orderDiscountLog.setRelatedId(userId);
//    			orderDiscountLogs.add(orderDiscountLog);
//    		}
//    	}
    	
    	
    	
    	//市场总价
    	price.put("originalPrice", originalPrice);
    	//优惠券优惠金额
    	price.put("couponCash", couponCash);
    	//玖币抵扣数量
    	price.put("deductCoinNum", deductCoinNum);
    	
    	//不打折的俞姐姐APP总价(不包含邮费)
    	price.put("prepay", prepay);
    	//打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	price.put("totalCash", totalCash);
    	// 玖币抵扣前的折扣比例
    	price.put("discountProportion", discountProportion);
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
    public Map<String, Object> calculateDiscountV572(List<ProductSKU> productSkus, Set<Long> productIds,
    		Map<Long, Product> productMap, Map<Long, Integer> skuCountMap, Map<Long, Brand> brandMap, List<OrderDiscountLog> orderDiscountLogs, long userId) {
    	Map<String, Object> price = new HashMap<String, Object>();
    	
    	List<Map<String, Object>> virtualGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> categoryGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	List<Map<String, Object>> normalGroupList = new ArrayList<Map<String, Object>>();
    	Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
    	
    	//新增字段,获取商品对应的虚拟分类,不存在则为null
    	Map<Long, Category> categoryMap = productCategoryService.getProductVirtualCategory(productIds);
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
         JSONArray jsonArrayDiscount = globalSettingService.getJsonArray(GlobalSettingName.ALL_DISCOUNT);
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
    		Product product = productMap.get(virtualSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		itemMap.put("skuId", virtualSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(virtualSKU.getId()));
    		itemMap.put("remainCount", virtualSKU.getRemainCount());
    		itemMap.put("cash", product.getCurrenCash());
    		itemMap.put("jiuCoin", product.getCurrentJiuCoin());
    		
    		Category virtualCat = categoryMap.get(virtualSKU.getProductId());
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
    		Product product = productMap.get(normalSKU.getProductId());
    		
    		Map<String, Object> itemMap = new HashMap<String, Object>();
    		
    		itemMap.put("skuId", normalSKU.getId());
    		itemMap.put("buyCount", skuCountMap.get(normalSKU.getId()));
    		itemMap.put("remainCount", normalSKU.getRemainCount());
    		itemMap.put("cash", product.getCurrenCash());
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
    	
    	double serviceCash = 0;
    	
    	int totalJiuCoin = 0;
    	//不打折应付
    	double prepay = 0;
    	//优惠值
    	double discountCash = 0;
    	
    	
    	//首单优惠
    	double firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
    	
    	//取复合优惠信息
    	Map<Long, List<DiscountInfo>> discountInfoMapCat =new HashMap<Long, List<DiscountInfo>>();
    	if(virtualCategoryId.size() > 0 && allDiscountInfoList.size() == 0){
    		discountInfoMapCat = shoppingCartFacade.queryDiscountInfoListMap(0, virtualCategoryId);  		
    	}
    	Map<Long, List<DiscountInfo>> discountInfoMapBrand = new HashMap<Long, List<DiscountInfo>>();
    	if(brandIds != null && brandIds.size()>0 && allDiscountInfoList.size() == 0){
    		discountInfoMapBrand = shoppingCartFacade.queryDiscountInfoListMap(1, brandIds);	
    	}
    	List<DiscountInfo> discountInfoList;
    	
    	
    	//虚拟分类商品的价格统计
    	for(Map<String, Object> virtualGroupMap: virtualGroupList) {
    		Category category = (Category)virtualGroupMap.get("category");
    		
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
    			
    			for(DiscountInfo discountInfo : discountInfoList){
    				if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    					maxFull = discountInfo.getFull();
    					minus = discountInfo.getMinus();
    					discountId = discountInfo.getId();
    				}
    			}
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
    		//不是服务品牌的计算价格优惠
    		if(brand != null && brand.getBrandId() != 572){
    			
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
    				totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    			}

    			
    			//新品牌满减@
    			double maxFull = 0;
    			double minus = 0;
    			long discountId = 0;
    			discountInfoList = new ArrayList<DiscountInfo>();
    			discountInfoList = discountInfoMapBrand.get(brand.getBrandId());
    			if(discountInfoList != null && discountInfoList.size() > 0){
    				
    				for(DiscountInfo discountInfo : discountInfoList){
    					if(subTotalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
    						maxFull = discountInfo.getFull();
    						minus = discountInfo.getMinus();
    						discountId = discountInfo.getId();
    					}
    				}
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
    		}else if(brand != null && brand.getBrandId() == 572){
    			//服务商品单独计算
    			List<Map<String, Object>> itemList = (List<Map<String, Object>>)normalGroupMap.get("itemList");
    			int buyCount = 0;
    			//double subTotalCash = 0;
    			for(Map<String, Object> itemMap : itemList) {
    				//如果购买件数超出库存数
    				if((int)itemMap.get("buyCount") > (int)itemMap.get("remainCount")) {
    					throw new RemainCountLessException();
    				}
    				buyCount = (int)itemMap.get("buyCount");
    				prepay += buyCount * (double)itemMap.get("cash");
    				serviceCash += buyCount * (double)itemMap.get("cash");
    				totalJiuCoin += buyCount * (int)itemMap.get("jiuCoin");
    			}
    		}
    		
    	}
    	
    	
    	//计算全场满减优惠金额
    	if(allDiscountInfoList != null && allDiscountInfoList.size() > 0){
    		double maxFull = 0;
    		double minus = 0;
    		
			for(DiscountInfo discountInfo : allDiscountInfoList){
				if(totalCash >= discountInfo.getFull() && discountInfo.getFull() > maxFull ){
					maxFull = discountInfo.getFull();
					minus = discountInfo.getMinus();
					//discountId = discountInfo.getId();
				}
			}
			
			if(minus < totalCash ){
    			totalCash -= minus;
    			discountCash += minus; 
    			
    		}else{
    			minus = totalCash;
    			totalCash = 0.0;
    			discountCash += minus; 
    		}
			if(orderDiscountLogs != null) {
				OrderDiscountLog orderDiscountLog = new OrderDiscountLog();
				orderDiscountLog.setComment("全场满" + maxFull + "减" + minus);
				orderDiscountLog.setCreateTime(System.currentTimeMillis());
				orderDiscountLog.setType(3);
				orderDiscountLog.setDiscount(minus);
				//orderDiscountLog.setRelatedId(discountId);
				
				orderDiscountLogs.add(orderDiscountLog);
			}
		}
    	
    	
    	//计算首单优惠金额
    	int count = orderService.getUserOrderCountForFirstDiscount(userId);
    	if (count == 0 ){
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
    			orderDiscountLog.setRelatedId(userId);
    			orderDiscountLogs.add(orderDiscountLog);
    		}
    	}else {
    		firstDiscountCash = 0.0;
    		
    	}
    	
    
    	
    	//市场总价
    	price.put("originalPrice", originalPrice);
    	//不打折的俞姐姐APP总价(不包含邮费)
    	price.put("prepay", prepay);
    	//打折的俞姐姐APP总价(不包含邮费),不含邮费的实付金额
    	
    	if(serviceCash > 0){
    		totalCash += serviceCash;
    	}
    	price.put("totalCash", totalCash);
    	
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
	 * 计算0元限购
	 * @return
	 */
    public int calculateZeroBuy(long userId, Set<Long> productIds, List<ProductSKU> productSkus) {


        Map<Long, Product> productMap = productService.getProductMap(productIds);

        int dayZeroCount = 0;
        int curZeroCount = 0;
        //获取用户当天的0元购买量
        dayZeroCount = productService.getZeroBuyerLog(userId);
        
    	for(ProductSKU productSKU : productSkus) {
    		
    		//统计市场总价
    		Product product = productMap.get(productSKU.getProductId());

            if (product.getCurrenCash() < 0.01) curZeroCount++;
            
    	}
    	
    	return curZeroCount == 0 ? curZeroCount : dayZeroCount + curZeroCount;
	}
    /**
     * 计算月度0元限购
     * @return
     */
    public int calculateZeroBuyMonthly(long userId, Set<Long> productIds, List<ProductSKU> productSkus) {
    	
    	
    	Map<Long, Product> productMap = productService.getProductMap(productIds);
    	
    	int monthlyZeroCount = 0;
    	int curZeroCount = 0;
    	//获取用户当月的0元购买量
    	monthlyZeroCount = productService.getZeroBuyerMonthly(userId);
    	
    	for(ProductSKU productSKU : productSkus) {
    		
    		//统计市场总价
    		Product product = productMap.get(productSKU.getProductId());
    		
    		if (product.getCurrenCash() < 0.01) curZeroCount++;
    		
    	}
    	
    	return curZeroCount == 0 ? curZeroCount : monthlyZeroCount + curZeroCount;
    }
    /**
     * 计算组合限购
     * @return
     */
    public Map<String, Object> calculateUserRestrictBuy(long userId ,Set<Long> productIds, Map<Long, Product> productMap,List<ProductSKU> productSkus, Map<Long, Integer> skuCountMap ) {
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	boolean result = false;
    	String restrictProductName = "";
    	int periodCount = 0;
    	int dayCount = 0;
    	int buyNum = 0;
    	long restrictId = 0;
    	RestrictionCombination restrict;
    	Set<Long> restrictIdSet = new HashSet<Long>();
    	
    	for(Map.Entry<Long, Product> product : productMap.entrySet()){
    		restrictIdSet.add(product.getValue().getRestrictId());
    	}
    	Map<Long, RestrictionCombination> restrictMap = productService.getRestrictByIdSet(restrictIdSet);
    	
    	if(restrictMap != null && restrictMap.size() > 0){
    		for(Map.Entry<Long, Product> product : productMap.entrySet()){
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
    
}
