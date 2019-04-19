/**
 * 
 */
package com.yujj.web.controller.delegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.account.UserCoin;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.order.ConsumeWrapper;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.entity.product.PayTypeVO;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.VersionUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.assembler.ProductPropAssembler;
import com.yujj.business.assembler.StoreAssembler;
import com.yujj.business.facade.OrderFacade;
import com.yujj.business.facade.ProductFacade;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.BrandService;
import com.yujj.business.service.CategoryService;
import com.yujj.business.service.ExpressInfoService;
import com.yujj.business.service.ExpressService;
import com.yujj.business.service.GlobalSettingService;
import com.yujj.business.service.HomeFloorService;
import com.yujj.business.service.JiuCoinExchangeLogService;
import com.yujj.business.service.LOWarehouseService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductCategoryService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.ProductService;
import com.yujj.business.service.ShoppingCartService;
import com.yujj.business.service.StatisticsService;
import com.yujj.business.service.UserCoinService;
import com.yujj.business.service.YJJUserAddressService;
import com.yujj.entity.Brand;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.entity.order.ExpressInfo;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderItemVO;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewLog;
import com.yujj.entity.order.OrderNewVO;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.Product;
import com.yujj.entity.product.ProductCategory;
import com.yujj.entity.product.ProductPropVO;
import com.yujj.entity.product.ProductSKU;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PayCheckNotEqualException;
import com.yujj.exception.order.PostageNotFoundException;
import com.yujj.exception.order.ProductUnavailableException;
import com.yujj.exception.order.RemainCountLessException;
import com.yujj.exception.order.UserCoinLessException;

/**
 * @author LWS
 */
@Service
public class OrderDelegator {
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");
   
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductSKUService productSKUService;

    @Autowired
    private YJJUserAddressService userAddressService;

    @Autowired
    private UserCoinService userCoinService;

    @Autowired
    private ProductPropAssembler productPropAssembler;
    @Autowired
    private StoreAssembler storeAssembler;

    @Autowired
    private ExpressService expressService;

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private LOWarehouseService lOWarehouseService;
    
    @Autowired
    private ExpressInfoService expressInfoService;

    @Autowired
    private BrandService brandService;
    
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductFacade productFacade;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
    private StatisticsService statisticsService;
    
    @Autowired
    private JiuCoinExchangeLogService jiuCoinExchangeLogService;
    
    @Autowired
    private HomeFloorService homeFloorService;

    //删除旧表
//    public Map<String, Object> getOrderList(OrderStatus orderStatus, PageQuery pageQuery, UserDetail userDetail) {
//        long userId = userDetail.getUserId();
//        int totalCount = orderService.getUserOrderCount(userId, orderStatus);
//        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
//        List<Order> orders = orderService.getUserOrders(userId, orderStatus, pageQuery);
//        Map<String, Object> result = new Hashtable<String, Object>();
//        result.put("orderlist", orders);
//        result.put("pageQuery", pageQueryResult);
//        result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
//
//        if (CollectionUtils.isEmpty(orders)) {
//            return result;
//        }
//
//        Set<Long> orderIds = new HashSet<Long>();
//        //获取某个状态的用户的所有订单(order表)
//        for (Order order : orders) {
//            orderIds.add(order.getId());
//        }
//        
//        Map<Long, List<OrderItemVO>> orderItemsMap = orderFacade.getOrderItemVOMap(userId, orderIds);
//        Map<Long, List<OrderItemGroupVO>> groupMap =
//            orderFacade.getOrderItemGroupVOMap(userId, orderIds, orderItemsMap);
//        for (Order order : orders) {
//            List<OrderItemVO> orderItems = orderItemsMap.get(order.getId());
//            order.setOrderItems(new ArrayList<OrderItem>(orderItems));
//            List<OrderItemGroupVO> groups = groupMap.get(order.getId());
//            order.setOrderItemGroups(new ArrayList<OrderItemGroup>(groups));
//        }
//        
//        //为了统一,写成对象数组形式.实际使用时里面只有一个对象
//        JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//        int minutes = 24 * 60;
//		for(Object obj : jsonArray) {
//			minutes = (int) ((JSONObject)obj).get("overdueMinutes");
//			break;
//		}
//		//只有待付款才有有效期
//		result.put("expireTime", (int)minutes * 60 * 1000L) ;
//
//        return result;
//    }
    
    //取优惠券列表
    public Map<String, Object> getCouponList(OrderCouponStatus status, PageQuery pageQuery, UserDetail userDetail) {
//    	if (status == null){
//    		status =OrderCouponStatus.UNUSED;
//    	}
    	
    	Map<String, Object> result = new Hashtable<String, Object>();
    	int totalCount = orderService.getUserCouponCount(userDetail.getUserId(), status);
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	result.put("pageQuery", pageQueryResult);
    	//取订单优惠券
    	List<Coupon> couponList = orderService.getUserCouponList(userDetail.getUserId(), status, pageQuery);
    	orderService.getCouponLimitContentList(couponList);
    	
    	result.put("couponList", couponList);
    	result.put("view", "查看");
    	result.put("couponFetchFlag", "YES"); //优惠券领取开关
    	result.put("couponCenterUrl", "/static/app/fetchcoupon.html");
    	result.put("couponGetTips", "快去领取吧");
    	result.put("couponHistoryTips", "查看历史优惠券");
    	result.put("myCoupon", "我的优惠券");
    	result.put("noCoupon", "没有可用优惠券");
    	result.put("directionForUse", "使用说明");
    	
    	return result;
    }
    
   
    
    //取历史优惠券列表
    public Map<String, Object> getCouponListHistory(PageQuery pageQuery, UserDetail userDetail) {
//    	if (status == null){
//    		status =OrderCouponStatus.UNUSED;
//    	}
    	
    	Map<String, Object> result = new Hashtable<String, Object>();
    	int totalCount = orderService.getUserCouponCountHistory(userDetail.getUserId());
    	PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	result.put("pageQuery", pageQueryResult);
    	//取订单优惠券
    	List<Coupon> couponList = orderService.getUserCouponListHistory(userDetail.getUserId(), pageQuery);
    	orderService.getCouponLimitContentList(couponList);
    	result.put("couponList", couponList);
    	result.put("view", "查看");
    	result.put("couponCenterUrl", "/static/app/giftcoupon.html");
    	result.put("couponGetTips", "更多优惠券，去领券中心看看");
    	result.put("couponHistoryTips", "查看历史优惠券");
    	result.put("myCoupon", "我的优惠券");
    	result.put("noCoupon", "没有可用优惠券");
    	result.put("directionForUse", "使用说明");
    	
    	return result;
    }
    
    //取优惠券列表
    public Map<String, Object> userExchangeCoupon(String exchangeCode, UserDetail userDetail) {
    	
    	Map<String, Object> result = new Hashtable<String, Object>();
    	//取订单优惠券
    	int num = orderService.getUserCouponCountByCode(exchangeCode);
    	
    	if(num == 1){
    		int updateNum = orderService.userExchangeCouponByCode(userDetail.getUserId(), exchangeCode, userDetail.getUser().getyJJNumber());
    		if(updateNum == 1){
    			result.put("result", "SUCCESS");
    		}else {
    			result.put("result", "FAIL");
    		}
    	}else{
    		result.put("result", "FAIL");
    	}
    	
    //	int num = orderService.userExchangeCoupon(userDetail.getUserId(), exchangeCode);
    
    	
    	return result;
    }
    
    	//从新订单表中取列表
    	public Map<String, Object> getNewOrderList(OrderStatus orderStatus, PageQuery pageQuery, UserDetail userDetail) {
    	long userId = userDetail.getUserId();
//    	if(orderStatus == null){
//    		orderStatus = OrderStatus.DELIVER;
//    	}
    	int totalCount = 0;
    	List<OrderNewVO> orderNews ;
    	PageQueryResult pageQueryResult;
    	Map<String, Object> result = new Hashtable<String, Object>();
    	Set<Long> orderNOs = new HashSet<Long>();
    	List<OrderItemVO> orderItemList;
    	Set<Long> orderNoMap ;
    	Map<Long, List<OrderNewVO>> childOrderMap;
    	Map<Long, List<OrderItemVO>> orderItemsMap;
    	List<OrderNewVO> resultOrderList = new ArrayList<OrderNewVO>();
    	result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
    	
    	//orderStatus=OrderStatus.SUCCESS;
    	//取全部状态订单，包含母订单
    	if(orderStatus == null || orderStatus.equals("") ){
    		
    		totalCount = orderService.getUserNewOrderCount(userId, orderStatus);
    		
    		orderNews = orderService.getUserOrdersNew(userId, orderStatus, pageQuery);
    		if (CollectionUtils.isEmpty(orderNews)) {
    			return result;
    		}
    		
    		//获取某个状态的用户的所有订单(order表)
    		for (OrderNew order : orderNews) {
    			orderNOs.add(order.getOrderNo());
    		}
    		orderItemsMap = orderFacade.getOrderNewItemVOMap(userId, orderNOs);//拆分母订单无法取到对于ITEMS
    		
    		childOrderMap = orderFacade.getChildOrderMap(userId, orderNOs);//
    		
    		for (OrderNewVO order : orderNews) {
    			List<OrderItemVO> orderItems = orderItemsMap.get(order.getOrderNo());
    			if(orderItems != null){ 			
    				order.setOrderItems(new ArrayList<OrderItem>(orderItems));
    			}
    			List<OrderNewVO> childOrderList = childOrderMap.get(order.getOrderNo());
//    			if(childOrderList == null || childOrderList.size() == 0 || (childOrderList != null && childOrderList.size() > 0 && orderStatus == null) ){
//    			}
    			resultOrderList.add(order);
    				
    			if (childOrderList != null && childOrderList.size() > 0) {
    				totalCount += childOrderList.size();
    				order.setChildOrderList(new ArrayList<OrderNewVO>(childOrderList));
    				//插入子订单
    				resultOrderList.addAll(childOrderList);
    				orderNoMap = new HashSet<Long>();
    				for (OrderNew orderNew : childOrderList) {
    					orderNoMap.add(orderNew.getOrderNo());
    				}
    				orderItemList = orderService.getOrderNewItemsVO(userId, orderNoMap);
    				order.setOrderItems(new ArrayList<OrderItem>(orderItemList));
    			}
    			
    		}
    		pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	} else{
    		  //取某状态订单，不包含母订单
	    		totalCount = orderService.getUserNewOrderCountWithoutParent(userId, orderStatus);
	    		pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
	    		orderNews = orderService.getUserOrdersNewWithoutParent(userId, orderStatus, pageQuery);
	    		if (CollectionUtils.isEmpty(orderNews)) {
	    			return result;
	    		}
	    		
	    		//获取某个状态的用户的所有订单(order表)
	    		for (OrderNewVO order : orderNews) {
	    			orderNOs.add(order.getOrderNo());
	    		}
	    		orderItemsMap = orderFacade.getOrderNewItemVOMap(userId, orderNOs);
	    		
	    		
	    		for (OrderNewVO order : orderNews) {
	    			List<OrderItemVO> orderItems = orderItemsMap.get(order.getOrderNo());
	    			if(orderItems != null){ 			
	    				order.setOrderItems(new ArrayList<OrderItem>(orderItems));
	    			}
	    			resultOrderList.add(order);
	    			
	    		}
    		
    	}
    	
    	storeAssembler.assemble(resultOrderList);
    	
    	result.put("orderlist", resultOrderList);
    	
    	result.put("pageQuery", pageQueryResult);
    	
    	
    	//为了统一,写成对象数组形式.实际使用时里面只有一个对象
//    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//    	int minutes = 24 * 60;
//    	for(Object obj : jsonArray) {
//    		minutes = (int) ((JSONObject)obj).get("overdueMinutes");
//    		break;
//    	}
//    	//只有待付款才有有效期
//    	result.put("expireTime", (int)minutes * 60 * 1000L) ;
    	
    	List<String> cancelReasonList = new ArrayList<String>(); 
    	cancelReasonList.add("我不想买了");
    	cancelReasonList.add("信息填写错误");
    	cancelReasonList.add("其它");
    	result.put("cancelReasonList", cancelReasonList);
    	
    	return result;
    }
    public Map<String, Object> getNewOrderListAfterSale(PageQuery pageQuery, UserDetail userDetail, String orderSearchNo) {
    	long userId = userDetail.getUserId();
    	int totalCount = 0;
    	List<OrderNew> orderNews ;
    	PageQueryResult pageQueryResult;
    	Map<String, Object> result = new Hashtable<String, Object>();
    	Set<Long> orderNOs = new HashSet<Long>();
    	Map<Long, List<OrderItemVO>> orderItemsMap;
    	Set<Integer> orderStatuses = new HashSet<Integer>();
    	
    	result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
    	orderStatuses.add(OrderStatus.DELIVER.getIntValue());
//    	orderStatuses.add(OrderStatus.SIGNED.getIntValue());
    	orderStatuses.add(OrderStatus.SUCCESS.getIntValue());
    	

		//取某状态订单，不包含母订单
		totalCount = orderService.getUserNewOrdersCountAfterSale(userId, orderStatuses, orderSearchNo);
		pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
		orderNews = orderService.getUserOrdersNewAfterSale(userId, orderStatuses, pageQuery, orderSearchNo);
		if (CollectionUtils.isEmpty(orderNews)) {
			return result;
		}
		
		//获取某个状态的用户的所有订单(order表)
		for (OrderNew order : orderNews) {
			orderNOs.add(order.getOrderNo());
		}
		orderItemsMap = orderFacade.getOrderNewItemVOMap(userId, orderNOs);
		
		int afterSaleCount = 0;
		for (OrderNew order : orderNews) {
			List<OrderItemVO> orderItems = orderItemsMap.get(order.getOrderNo());
			if(orderItems != null){ 			
				order.setOrderItems(new ArrayList<OrderItem>(orderItems));
			}
			
			for(OrderItemVO orderItem : orderItems){
				orderItem.setAfterSaleFlag(0);	
				if(order.getOrderStatus().equals(OrderStatus.DELIVER) || order.getOrderStatus().equals(OrderStatus.SIGNED) 
						|| (order.getOrderStatus().equals(OrderStatus.SUCCESS) && order.getUpdateTime() > 0 && UtilDate.getPlanDayFromDate(new Date(), -15).getTime() - order.getUpdateTime() < 0)){
					afterSaleCount = afterSaleService.getItemAfterSaleValidCount(userId, orderItem.getId(), orderItem.getOrderNo());
					if(orderItem.getBuyCount() > afterSaleCount){
						orderItem.setAfterSaleFlag(1);
					}
					
				}
				
			}	
		}

    	result.put("orderlist", orderNews);
    	
    	result.put("pageQuery", pageQueryResult);
    	
    	
    	//为了统一,写成对象数组形式.实际使用时里面只有一个对象
//    	JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//    	int minutes = 24 * 60;
//    	for(Object obj : jsonArray) {
//    		minutes = (int) ((JSONObject)obj).get("overdueMinutes");
//    		break;
//    	}
//    	//只有待付款才有有效期
//    	result.put("expireTime", (int)minutes * 60 * 1000L) ;
    	
    	List<String> cancelReasonList = new ArrayList<String>(); 
    	cancelReasonList.add("我不想买了");
    	cancelReasonList.add("信息填写错误");
    	cancelReasonList.add("其它");
    	result.put("cancelReasonList", cancelReasonList);
    	
    	return result;
    }

    @Deprecated
    public Map<String, Object> buildOrder(long skuId, int count, UserDetail userDetail) {
        Map<String, Object> model = new Hashtable<String, Object>();
        ProductSKU productSKU = productSKUService.getProductSKU(skuId);
        if (productSKU == null || productSKU.getRemainCount() < count) {
            String msg = "sku is not available, skuId:" + skuId;
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }
        model.put("sku", productSKU);

        Product product = productService.getProductById(productSKU.getProductId());
        if (product == null || !productSKU.isOnSaling()) {
            String msg = "product is not available, productId:" + productSKU.getProductId();
            logger.warn(msg);
            throw new IllegalStateException(msg);
        }
        model.put("product", product);

        List<ProductPropVO> skuPropVOs = productSKU.getProductProps();
        productPropAssembler.assemble(skuPropVOs);
        model.put("skuProps", skuPropVOs);

        long userId = userDetail.getUserId();
        List<Address> addresses = userAddressService.getUserAddresses(userId);
        model.put("addresses", addresses);

        UserCoin userCoin = userCoinService.getUserCoin(userId);
        if (userCoin != null) {
            model.put("avalCoins", userCoin.getAvalCoins());
            model.put("unavalCoins", userCoin.getUnavalCoins());
        } else {
            model.put("avalCoins", 0);
            model.put("unavalCoins", 0);
        }
        return model;
    }

    //  buildv2入口
    @Deprecated
    public JsonResponse buildOrder17(long userId, String cityName, String[] skuCountPairArray) {
        JsonResponse jsonResponse = new JsonResponse();
        
        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
        for (String skuCountPair : skuCountPairArray) {
            String[] parts = StringUtils.split(skuCountPair, ":");
            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
        }

        //获取用户购买的sku信息
        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
        
        //待确定：根据skuid取sku的bean信息,理论两者应该相等
        if(productSkus.size() != skuCountMap.keySet().size()) {
        	return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_SKU_NOT_MATCH);
        }
        
        List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        Set<Long> productIds = new HashSet<Long>();
        for (ProductSKU sku : productSkus) {
        	int count = skuCountMap.get(sku.getId());
        	if (count > sku.getRemainCount()) {
            	//库存
        		Product product = productService.getProductById(sku.getProductId());
                return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(product.getName() + " 库存不足，请换其他商品进行兑换");
            }
        	
            productIds.add(sku.getProductId());
            List<ProductPropVO> skuProps = sku.getProductProps();
            composites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        
        //限购
        sortProduId(productSkus);
        Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
        List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
        if(products.size() > 0) {
        	String description = productFacade.restrictDetail(products.get(0));
        	return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
        }
        	
        productPropAssembler.assemble(composites);

        Map<Long, Product> productMap = productService.getProductMap(productIds);
        Set<Long> brandIds = new HashSet<Long>();
        for (Product product : productMap.values()) {
            brandIds.add(product.getBrandId());
        }
        Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);

        for (ProductSKU sku : productSkus) {
            
            //判断失效
            if (!sku.isOnSaling() ) {
                throw new ProductUnavailableException();
            }
        }
        
        //计算分类商品的优惠后的价格
        Map<String, Object> price = orderFacade.calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, null, userId);

        //拼装以库存为分类标准的数据，计算邮费
        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
        groupList = orderFacade.wrapperGroupList(productSkus, productMap, skuCountMap, skuPropMap, userId);
        data.put("groupList", groupList);
        
        // 是否显示去凑单区域
        data.put("isShow", true);

        // 温馨提示
        data.put("warmTip", "温馨提示：因商品于不同库房发货，系统已自动分配商品包裹并计算配送费用。");

        List<Address> addresses = userAddressService.getUserAddresses(userId);
        data.put("addresses", addresses);
        
        //temp added by dongzhong 2016-07-13 限制用户一天零元购最多3件
        int zeroCount = orderFacade.calculateZeroBuy(userId, productIds, productSkus);
        int zeroLimit = globalSettingService.getInt(GlobalSettingName.ZERO_LIMIT);
        if (zeroCount > zeroLimit) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每天仅限" + zeroLimit + "件哦");	//"0元购商品超过当天购买件数"+zeroLimit
        
        int zeroCountMonthly = orderFacade.calculateZeroBuyMonthly(userId, productIds, productSkus);
        int zeroLimitMonthly = 5;
        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ZERO_LIMIT_MONTHLY);
		for(Object obj : jsonArrayConfirm) {
			zeroLimitMonthly = (int) ((JSONObject)obj).get("zeroLimitMonthly");
			break;
		}
		if (zeroCountMonthly > zeroLimitMonthly) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每月仅限" + zeroLimitMonthly + "件哦");
        
        double totalPostage = 0;
        //如果这个用户没有地址信息，那么在确认订单之前必定会再次请求该接口
        //没有地址信息,不统计邮费,默认为0
        if(addresses.size() > 0) {
        	if(StringUtils.isEmpty(cityName)) {
        		for(Address address : addresses) {
        			if(address.getIsDefault()) {
        				cityName = address.getProvinceName() + address.getCityName();
        				break;
        			}
        		}
        	}
        	//groupList按照仓库分类好之后 计算运费
        	if(cityName != null && cityName.length() > 0){
        		try {
        			totalPostage = orderFacade.calculatePostage(groupList, cityName, new HashMap<Long, Double>());
        		} catch (DeliveryLocationNotFoundException e1) {
        			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NOT_FOUND);
        		} catch (DeliveryLocationNullException e2) {
        			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NULL);
        		} catch (PostageNotFoundException e3) {
        			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_POSATGE_NOT_FOUND);
        		}
        	}
        }

        //没看懂
//        UserCoin userCoin = userCoinService.getUserCoin(userId);
//        if (userCoin != null) {
//            data.put("avalCoins", userCoin.georiginalPricetAvalCoins());
//            data.put("unavalCoins", userCoin.getUnavalCoins());
//        } else {
//            data.put("avalCoins", 0);
//            data.put("unavalCoins", 0);
//        }
        
        UserCoin userCoin = userCoinService.getUserCoin(userId);
//        if (userCoin == null) {
//            logger.error("user coin can not null, userId:{}", userId);
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        } else if (userCoin.getAllCoins() < (int)price.get("totalJiuCoin")) {
//            logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
//                userCoin.getAllCoins(), (int)price.get("totalJiuCoin"));
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        }
        
        Map<String, Object> price2 = new HashMap<String, Object>();
        int originalPrice = (int) price.get("originalPrice");
        double totalCash = (double) price.get("totalCash");
        double discountCash = (double) price.get("discountCash");
        double prepay = (double) price.get("prepay");
        //实付金额(加上邮费)
        totalCash += totalPostage;
        double jiuCoinDiscount = originalPrice - totalCash + totalPostage - discountCash;
        int totalJiuCoin = (int) price.get("totalJiuCoin");
        
        price2.put("originalPrice", originalPrice);
        //remove postage to adapter app
        price2.put("totalCash", totalCash - totalPostage);
        price2.put("totalJiuCoin", totalJiuCoin);
        price2.put("discountCash", discountCash);
        price2.put("prepay", prepay);
        price2.put("jiuCoinDiscount", jiuCoinDiscount);
        
        double firstDiscount = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
        data.put("firstDiscount", firstDiscount);
        data.put("firstDiscountTips", "新用户首单" + firstDiscount + "元优惠");
        int count = orderService.getUserOrderCountForFirstDiscount(userId);
        if (count == 0 ){
        	data.put("firstDiscountAble", "YES");
        }else {
        	data.put("firstDiscountAble", "NO");
        }
        
        
        data.put("price", price2);
        data.put("postage", totalPostage);
        return jsonResponse.setSuccessful().setData(data);
    }
    
    /*
     * 结算
     */
	public JsonResponse buildOrder185(long userId, String cityName, String[] skuCountPairArray,ClientPlatform clientPlatform,int takeGood) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
    	for (String skuCountPair : skuCountPairArray) {
    		String[] parts = StringUtils.split(skuCountPair, ":");
    		skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    	}
    	
    	//获取用户购买的sku信息
    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
    	
    	//待确定：根据skuid取sku的bean信息,理论两者应该相等
    	if(productSkus.size() != skuCountMap.keySet().size()) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_SKU_NOT_MATCH);
    	}
    	
    	/**
    	 * 积分兑换商品限制
    	 */
    	if (checkLimitJiuCoinExchange(userId, productSkus, skuCountMap)) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("用户积分兑换商品超过次数");
		}
    	
    	/**
    	 * 积分商城是否已下架
    	 */
    	if (checkOnSaleJiuCoinExchange(productSkus)) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("积分商城已下架");
		}
    	
    	List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
    	Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
    	Set<Long> productIds = new HashSet<Long>();
    	for (ProductSKU sku : productSkus) {
    		int count = skuCountMap.get(sku.getId());
    		if (count > sku.getRemainCount()) {
    			//库存
    			Product product = productService.getProductById(sku.getProductId());
    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(product.getName() + " 库存不足");
    		}
    		
    		productIds.add(sku.getProductId());
    		List<ProductPropVO> skuProps = sku.getProductProps();
    		composites.addAll(skuProps);
    		skuPropMap.put(sku.getId(), skuProps);            	
    	}
    	
    	//限购
    	sortProduId(productSkus);
    	Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
    	if(products.size() > 0) {
    		String description = productFacade.restrictDetail(products.get(0));
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
    	}
    	
    	productPropAssembler.assemble(composites);
    	
    	Map<Long, Product> productMap = productService.getProductMap(productIds);
    	Set<Long> brandIds = new HashSet<Long>();
    	for (Product product : productMap.values()) {
    		brandIds.add(product.getBrandId());
    	}
    	Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);
    	for (ProductSKU sku : productSkus) {
//    		Product product = productMap.get(sku.getProductId());
    		//判断失效
    		if ( !sku.isOnSaling() ) {
    			Product product = productService.getProductById(sku.getProductId());
    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(product.getName() + " 库存不足");
    		}
    	}
    	
    	//计算分类商品的优惠后的价格
    	Map<String, Object> price;
    	if(VersionUtil.compareVersion(clientPlatform.getVersion() , "2.1.3") >= 0){
    		price = orderFacade.calculateDiscountV213(productSkus, productIds, productMap, skuCountMap, brandMap, null, userId,"", 0);
    		
    	}else{
    		price = orderFacade.calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, null, userId);
    		
    	}
    	//Map<String, Object> price = orderFacade.calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, null, userId);
    	
    	//拼装以库存为分类标准的数据，计算邮费
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
    	groupList = orderFacade.wrapperGroupList(productSkus, productMap, skuCountMap, skuPropMap,userId);
    	data.put("groupList", groupList);
    	
    	
    	// 是否显示去凑单区域
    	data.put("isShow", true);
    	
    	// 温馨提示
    	data.put("warmTip", "温馨提示：因商品于不同库房发货，系统已自动分配商品包裹并计算配送费用。");
    	List<Address> addresses = null;
    	if(takeGood != 1){
        	addresses = userAddressService.getUserAddresses(userId);
    	}
    	data.put("addresses", addresses);
    	
    	//temp added by dongzhong 2016-07-13 限制用户一天零元购最多3件
    	int zeroCount = orderFacade.calculateZeroBuy(userId, productIds, productSkus);
    	int zeroLimit = globalSettingService.getInt(GlobalSettingName.ZERO_LIMIT);
    	if (zeroCount > zeroLimit) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每天仅限" + zeroLimit + "件哦");	//"0元购商品超过当天购买件数"+zeroLimit
    	
    	int zeroCountMonthly = orderFacade.calculateZeroBuyMonthly(userId, productIds, productSkus);
    	int zeroLimitMonthly = 5;
    	JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ZERO_LIMIT_MONTHLY);
    	for(Object obj : jsonArrayConfirm) {
    		zeroLimitMonthly = (int) ((JSONObject)obj).get("zeroLimitMonthly");
    		break;
    	}
    	if (zeroCountMonthly > zeroLimitMonthly) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每月仅限" + zeroLimitMonthly + "件哦");
    	
    	//统计组合限购，如果为true则为超出限购数量
    	Map<String, Object> restrictResult = orderFacade.calculateUserRestrictBuy(userId, productIds, productMap,  productSkus, skuCountMap);
    	if ((boolean) restrictResult.get("result")) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICT_GROUP).setError("亲，您购买的商品" + restrictResult.get("restrictProductName") + "超过组合限购量了哦");
    	
    	double totalPostage = 0;
    	//如果这个用户没有地址信息，那么在确认订单之前必定会再次请求该接口
    	//没有地址信息,不统计邮费,默认为0
    	if(addresses != null && addresses.size() > 0) {
    		if(StringUtils.isEmpty(cityName)) {
    			for(Address address : addresses) {
    				if(address.getIsDefault()) {
    					cityName = address.getProvinceName() + address.getCityName();
    					break;
    				}
    			}
    		}
    		//groupList按照仓库分类好之后 计算运费
    		if(cityName != null && cityName.length() > 0){
    			
    			try {
    				totalPostage = orderFacade.calculatePostage(groupList, cityName, new HashMap<Long, Double>());
    			} catch (DeliveryLocationNotFoundException e1) {
    				return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NOT_FOUND);
    			} catch (DeliveryLocationNullException e2) {
    				return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NULL);
    			} catch (PostageNotFoundException e3) {
    				return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_POSATGE_NOT_FOUND);
    			}
    		}
    	}
    	
    	//获取用户的玖币数
        UserCoin userCoin = userCoinService.getUserCoin(userId);
        if (userCoin != null) {
            data.put("avalCoins", userCoin.getUnavalCoins());
            data.put("unavalCoins", userCoin.getUnavalCoins());
        } else {
            data.put("avalCoins", 0);
            data.put("unavalCoins", 0);
        }
    	
		if (userCoin == null) {
			logger.error("user coin can not null, userId:{}", userId);
			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
		} else if (userCoin.getAllCoins() < (int)price.get("totalJiuCoin")) {
			logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
					userCoin.getAllCoins(), (int)price.get("totalJiuCoin"));
			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
		}
    	
    	Map<String, Object> price2 = new HashMap<String, Object>();
    	int originalPrice = (int) price.get("originalPrice");
    	double totalCash = (double) price.get("totalCash");
    	double discountCash = (double) price.get("discountCash");
    	double prepay = (double) price.get("prepay");
    	double firstDiscountCash = (double) (price.get("firstDiscountCash"));
    	String firstDiscountAble = (String) price.get("firstDiscountAble");
    	//实付金额(加上邮费)
    	totalCash += totalPostage;
    	double jiuCoinDiscount = originalPrice - totalCash + totalPostage - discountCash;
    	int totalJiuCoin = (int) price.get("totalJiuCoin");
    	double coinRatio = 0.1;
    	if(VersionUtil.compareVersion(clientPlatform.getVersion() , "2.1.3") >= 0){
    		JSONObject jiuCoinAttribute = (JSONObject) globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING).get("jiuCoinAttribute");
    		coinRatio = Double.parseDouble(jiuCoinAttribute.get("worthRmb").toString());
    		
    		int deductCoinNum = (int) price.get("deductCoinNum");
    		price2.put("deductCoinNumDesc", "可用" + deductCoinNum + "个玖币抵扣￥" + String.format("%.2f",deductCoinNum * coinRatio));
    		price2.put("deductCoinNumDesc1", "可用");
    		price2.put("deductCoinNumDesc2", "个玖币抵扣￥");
    		price2.put("deductTitle", "玖币抵扣：");
    		price2.put("deductMoneyStr", "-￥");
    		price2.put("discountCashDesc", "已优惠￥" );
    		price2.put("discountCashDesc2", "优惠￥");
    		price2.put("deductMoney", deductCoinNum * coinRatio);
    		price2.put("deductMoneyStr", String.format("%.2f",deductCoinNum * coinRatio));
    	}
    	
    	
    	price2.put("originalPrice", originalPrice);
    	//remove postage to adapter app
    	price2.put("totalCash", totalCash - totalPostage);
    	price2.put("StringTotalCash", String.format("%.2f",totalCash - totalPostage));
    	price2.put("totalJiuCoin", totalJiuCoin);
    	price2.put("discountCash", discountCash);
    	price2.put("StringDiscountCash", String.format("%.2f",discountCash));
    	price2.put("prepay", prepay);
    	price2.put("StringPrepay", String.format("%.2f",prepay));
    	price2.put("jiuCoinDiscount", jiuCoinDiscount);
    	price2.put("firstDiscountCash", firstDiscountCash);
    	price2.put("StringfirstDiscountCash", String.format("%.2f",firstDiscountCash));
    	price2.put("firstDiscountAble", firstDiscountAble);
 
    	
    	double firstDiscount = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
		data.put("firstDiscount", firstDiscount);
		data.put("StringFirstDiscount", String.format("%.2f",firstDiscount));
    	data.put("firstDiscountTips", "新用户首单" + firstDiscount + "元优惠");
    	int count = orderService.getUserOrderCountForFirstDiscount(userId);
    	if (count == 0 ){
    		data.put("firstDiscountAble", "YES");
    	}else {
    		data.put("firstDiscountAble", "NO");
    	}
    	
    	data.put("price", price2);
    	data.put("postage", totalPostage);
    	data.put("couponFetchFlag", "YES"); //优惠券领取开关
    	//取订单可用优惠券
    	//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌，  5：免邮
    	List<Coupon> couponList = orderService.getUserOrderCoupon(userId, OrderCouponStatus.UNUSED);
    	List<Coupon> couponUsableList = getAvailableCoupon( userId,   prepay , productCountMap,   productMap , productIds,  couponList, totalPostage);
//    	for(Coupon coupon : couponList){
//    		if(coupon.getRangeType() != null && coupon.getRangeType() == 0){
//    			
//    			couponUsableList.add(coupon);
//    			coupon.setActualDiscount(coupon.getMoney());
//    			
//    		}else if(coupon.getRangeType() != null){
//				if(coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
//	    			String limitIds = "";
//	    			String regEx = "\\[[^}]*\\]";
//			    	String str = coupon.getRangeContent(); //coupon.getRangeContent()
//			    	Pattern pat = Pattern.compile(regEx);
//			    	Matcher mat;
//			    	mat = pat.matcher(str);
//					while (mat.find()) {
//						limitIds = mat.group();
//					}
//					limitIds = limitIds.replaceAll("\\[", "").replaceAll("\\]", "");
//					double actualDiscount = 0;
//					
//					int addCount = 0;
//					if(limitIds.length() > 0 ){
//
//						String[] idsArr = limitIds.split(",");
//						if(coupon.getRangeType() != null && coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
//							actualDiscount = 0;
//							addCount = 0;
//							List<String> idsList = new ArrayList<String>();
//							//子分类匹配
//							List<Category> categorieListAll = categoryService.getCategories();
//							
//							//List<Category> categorieList = categoryService.getCategoriesByIdsArr(idsArr);
//							for(String ctgrId : idsArr){
//								idsList.add(ctgrId);
//							}
//							//添加子分类到列表
//							for(Category category : categorieListAll){
//								//添加子分类
//								for(Category categoryTemp : categorieListAll){
//									if(categoryTemp.getParentId() == category.getId()){
//										category.getChildCategories().add(categoryTemp);
//									}
//								}
//								for(String ctgrId : idsArr){
//									if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
//										for(Category childCtgy : category.getChildCategories()){
//											idsList.add(childCtgy.getId() + "");
//										}
//									}
//								}
//							}
//							
//							//计算实际优惠折扣
//							//List<Long> categoryIdList =  productCategoryService.getCategoryIdsAll(productIds);
//							List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
//							if(productCategoryList != null && productCategoryList.size() > 0 ){	
//								String productIdStr = "";
//								for(String id : idsList){
//									for (ProductCategory productCategory : productCategoryList) {
//										if((productCategory.getCategoryId() + "" ).equals(id) && productIdStr.indexOf("," + productCategory.getProductId() + ",") == -1){
//											actualDiscount += productCountMap.get(productCategory.getProductId()) * productMap.get(productCategory.getProductId()).getCash();
//											productIdStr += "," + productCategory.getProductId() + ","; //
//											if(addCount == 0){
//												couponUsableList.add(coupon);
//												addCount++;		
//											}
//										}
////										productCountMap
//											
//									}
//									if(coupon.getMoney() < actualDiscount){
//										actualDiscount = coupon.getMoney();
//									}
//									coupon.setActualDiscount(actualDiscount);
//								}
//							}
//							
//						}else if(coupon.getRangeType() != null && coupon.getRangeType() == 2 && idsArr != null && idsArr.length  == 1){
//							if(Double.parseDouble(limitIds) <= prepay){
//								
//								couponUsableList.add(coupon);
//								coupon.setActualDiscount(coupon.getMoney());
//							}
//						}else if(coupon.getRangeType() != null && coupon.getRangeType() == 4 && idsArr != null && idsArr.length > 0){
//							actualDiscount = 0;
//							addCount = 0;
//							for(String arr : idsArr){
//								
//								for (Product product : productMap.values()) {
//									if((product.getBrandId() + "").equals(arr)){
//										if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
//											actualDiscount += productCountMap.get(product.getId()) * product.getCash();
//										}
//										if(addCount == 0){
//											couponUsableList.add(coupon);
//											addCount++;		
//										}
//									}
//									
//								}
//								
//					    	}
//							if(coupon.getMoney() < actualDiscount){
//								actualDiscount = coupon.getMoney();
//							}
//							coupon.setActualDiscount(actualDiscount);
//						}
//					}
//				}
//			}
//    	}
    	orderService.getCouponLimitContentList(couponList);
    	data.put("couponList", couponUsableList);
    	data.put("view", "查看");
    	data.put("couponCenterUrl", "/static/app/giftcoupon.html");
    	data.put("couponGetTips", "更多优惠券，去领券中心看看");
    	data.put("couponHistoryTips", "查看历史优惠券");
    	data.put("myCoupon", "我的优惠券");
    	data.put("noCoupon", "没有可用优惠券");
    	data.put("directionForUse", "使用说明");
    	data.put("coinRatio", coinRatio);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    
    private boolean checkLimitJiuCoinExchange(long userId, List<ProductSKU> productSkus, Map<Long, Integer> skuCountMap) {
    	JSONObject jiucoin_global_setting = globalSettingService.getJsonObject(GlobalSettingName.JIUCOIN_GLOBAL_SETTING);
    	JSONObject exchangeGoodsSetting = jiucoin_global_setting.getJSONObject("exchangeGoodsSetting");
    	int cycle = exchangeGoodsSetting.getInteger("cycle");
    	int maxCount = exchangeGoodsSetting.getInteger("maxCount");
    	int count = jiuCoinExchangeLogService.getCount(userId, 2, null, DateUtil.getTodayEnd() - cycle * DateUtils.MILLIS_PER_DAY);
    	
    	int buy_count = 0;
    	for (ProductSKU productSKU : productSkus) {
			Product product = productService.getProductById(productSKU.getProductId());
			if (product.getJiuCoin() > 0) {
				buy_count += skuCountMap.get(productSKU.getId());
			}
		}
    	if (buy_count + count > maxCount) {
			return true;
		}
    	
		return false;
	}

	public List<Coupon> getAvailableCoupon(long userId,  double prepay ,Map<Long, Integer> productCountMap,  Map<Long, Product> productMap ,Set<Long> productIds, List<Coupon> couponList, double totalPostage) {
    	//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌   5：免邮
    	List<Coupon> couponUsableList = new ArrayList<Coupon>();
    	List<Coupon> couponShipFreeList = new ArrayList<Coupon>();
    	
    	for(Coupon coupon : couponList){
    		 if( coupon.getRangeType() == 5 && totalPostage > 0){
    			couponShipFreeList.add(coupon);
    		}
    	}

    	if (orderFacade.HasCouponLimit(true, productIds, productMap, prepay)) return couponShipFreeList;
    		        
        //部分优惠券限制使用判断
//        JSONObject partCouponLimitJson = globalSettingService.getJsonObject(GlobalSettingName.SPECIFY_COUPON_LIMIT_SET);
//        List<String> partLimitIdsList = new ArrayList<String>();
//        if(partCouponLimitJson.get("enable") != null && partCouponLimitJson.get("start_time") != null && partCouponLimitJson.get("end_time") != null && partCouponLimitJson.get("category_ids") != null && partCouponLimitJson.get("coupon _template_ids") != null){
//        	SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
//        	long nowTime = System.currentTimeMillis();
//        	long startTime = 0;
//        	long endTime = 0;
//        	String limitCategoryIds = partCouponLimitJson.get("category_ids").toString().replace("[", "").replace("]", "");
//        	try {
//        		startTime = sdf.parse(partCouponLimitJson.get("start_time").toString()).getTime();
//        		endTime = sdf.parse(partCouponLimitJson.get("end_time").toString()).getTime();
//        	} catch (ParseException e) {
//        		
//        		e.printStackTrace();
//        	}
//        	//如果全部分类限制的话，返回空列表
//        	if(partCouponLimitJson.get("enable").equals("YES") && startTime < nowTime && nowTime < endTime  ){
//        		if(limitCategoryIds.equals("0")){
//        			return couponShipFreeList;
//        		} else if(limitCategoryIds.length() > 0 ){
//        			String[] idsArr = limitCategoryIds.split(",");
//        			//子分类匹配
//        			List<Category> categorieListAll = categoryService.getCategories();
//        			for(String ctgrId : idsArr){
//        				partLimitIdsList.add(ctgrId);
//        			}
//        			//添加子分类到列表
//        			for(Category category : categorieListAll){
//        				//添加子分类
//        				for(Category categoryTemp : categorieListAll){
//        					if(categoryTemp.getParentId() == category.getId()){
//        						category.getChildCategories().add(categoryTemp);
//        					}
//        				}
//        				for(String ctgrId : idsArr){
//        					if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
//        						for(Category childCtgy : category.getChildCategories()){
//        							partLimitIdsList.add(childCtgy.getId() + "");
//        						}
//        					}
//        				}
//        			}
//        		}
//        	}
//        	if(partLimitIdsList != null && partLimitIdsList.size() > 0){
//        		
//        		List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
//        		if(productCategoryList != null && productCategoryList.size() > 0 ){	
//        			
//        			for(String id : partLimitIdsList){
//        				for (ProductCategory productCategory : productCategoryList) {
//        					if((productCategory.getCategoryId() + "" ).equals(id)){
//        						return couponShipFreeList;	
//        					}	
//        				}
//        				for (Product product : productMap.values()) {
//        					if((product.getvCategoryId() + "").equals(id)){
//        						return couponShipFreeList;	
//        					}
//        				}
//        			}
//        		}
//        	}
//        }
       
        
    	for(Coupon coupon : couponList){
    		if( coupon.getRangeType() == 0){
    			
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(coupon.getMoney());
    			
    		}else if( coupon.getRangeType() == 5 && totalPostage > 0){
    			
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(totalPostage);
    			
    		}else if(coupon.getRangeType() == 2 && coupon.getLimitMoney() > 0 && coupon.getLimitMoney() <= prepay){
    			
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(coupon.getMoney());
    			
    		}else {
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
					
					int addCount = 0;
					if(limitIds.length() > 0 ){

						String[] idsArr = limitIds.split(",");
						if(coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
							actualDiscount = 0;
							addCount = 0;
							List<String> idsList = new ArrayList<String>();
							//子分类匹配
							List<Category> categorieListAll = categoryService.getCategories();
							
							//List<Category> categorieList = categoryService.getCategoriesByIdsArr(idsArr);
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
							
							//计算实际优惠折扣
							//List<Long> categoryIdList =  productCategoryService.getCategoryIdsAll(productIds);
							List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
							if(productCategoryList != null && productCategoryList.size() > 0 ){	
								String productIdStr = "";
								for(String id : idsList){
									for (ProductCategory productCategory : productCategoryList) {
										if((productCategory.getCategoryId() + "" ).equals(id) && productIdStr.indexOf("," + productCategory.getProductId() + ",") == -1){
											actualDiscount += productCountMap.get(productCategory.getProductId()) * productMap.get(productCategory.getProductId()).getCurrenCash();
											productIdStr += "," + productCategory.getProductId() + ","; //
											if(addCount == 0){
												if(coupon.getLimitMoney() > 0 && coupon.getLimitMoney() <= actualDiscount){
													couponUsableList.add(coupon);
													addCount++;		
												}else if(coupon.getLimitMoney() == 0){
													couponUsableList.add(coupon);
													addCount++;		
												}	
											}
										}
//										productCountMap
											
									}
									if(coupon.getMoney() < actualDiscount){
										actualDiscount = coupon.getMoney();
									}
									coupon.setActualDiscount(actualDiscount);
								}
							}
							
						}else if(coupon.getRangeType() == 2 && idsArr != null && idsArr.length  == 1){
							if(Double.parseDouble(limitIds) <= prepay){
								
								couponUsableList.add(coupon);
								coupon.setActualDiscount(coupon.getMoney());
							}
						}else if(coupon.getRangeType() == 4 && idsArr != null && idsArr.length > 0){
							actualDiscount = 0;
							addCount = 0;
							for(String arr : idsArr){
								
								for (Product product : productMap.values()) {
									if((product.getBrandId() + "").equals(arr)){
										if(productCountMap.get(product.getId()) != null && productCountMap.get(product.getId()) > 0){
											actualDiscount += productCountMap.get(product.getId()) * product.getCurrenCash();
										}
										if(addCount == 0){
											if(coupon.getLimitMoney() > 0 && coupon.getLimitMoney() <= actualDiscount){
												couponUsableList.add(coupon);
												addCount++;		
											}else if(coupon.getLimitMoney() == 0){
												couponUsableList.add(coupon);
												addCount++;		
											}
										}
									}
									
								}
								
					    	}
							if(coupon.getMoney() < actualDiscount){
								actualDiscount = coupon.getMoney();
							}
							coupon.setActualDiscount(actualDiscount);
						}
					}
				}
			}
    	}
    	
    
    	return couponUsableList;
    }


    //  buildv2入口
    public JsonResponse buildOrderXX(long userId, String[] skuCountPairArray) {
        JsonResponse jsonResponse = new JsonResponse();

        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
        for (String skuCountPair : skuCountPairArray) {
            String[] parts = StringUtils.split(skuCountPair, ":");
            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
        }

        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
        List<ProductPropVO> composites = new ArrayList<ProductPropVO>();
        Map<Long, List<ProductPropVO>> skuPropMap = new HashMap<Long, List<ProductPropVO>>();
        Set<Long> productIds = new HashSet<Long>();
        for (ProductSKU sku : productSkus) {
            productIds.add(sku.getProductId());
            List<ProductPropVO> skuProps = sku.getProductProps();
            composites.addAll(skuProps);
            skuPropMap.put(sku.getId(), skuProps);
        }
        productPropAssembler.assemble(composites);

        Map<Long, Product> productMap = productService.getProductMap(productIds);
        Set<Long> brandIds = new HashSet<Long>();
        for (Product product : productMap.values()) {
            brandIds.add(product.getBrandId());
        }
        Map<Long, Brand> brandMap = brandService.getBrandMap(brandIds);

        Map<String, Object> data = new HashMap<String, Object>();
        List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
        data.put("groupList", groupList);
        Map<Long, List<Map<String, Object>>> brandGroup = new HashMap<Long, List<Map<String, Object>>>();
        for (ProductSKU sku : productSkus) {
            List<ProductPropVO> skuProps = skuPropMap.get(sku.getId());
            Product product = productMap.get(sku.getProductId());
            Brand brand = brandMap.get(product.getBrandId());

            Map<String, Object> map = new HashMap<String, Object>();
            Map<String, Object> skuMap = new HashMap<String, Object>();
            skuMap.put("id", sku.getId());
            skuMap.put("skuId", sku.getId());
            skuMap.put("price", sku.getPrice());
            skuMap.put("marketPriceMin", product.getMarketPriceMin());
            skuMap.put("MarketPriceMax", product.getMarketPriceMax());
            skuMap.put("remainCount", sku.getRemainCount());
            skuMap.put("skuSnapshot", buildSkuSnapshot(skuProps));
            map.put("sku", skuMap);
            map.put("skuProps", skuProps);
            map.put("product", product.toSimpleMap15());
            
            List<Map<String, Object>> itemList = brandGroup.get(brand.getBrandId());
            if (itemList == null) {
                Map<String, Object> groupMap = new HashMap<String, Object>();
                groupMap.put("brand", brand);
                itemList = new ArrayList<Map<String, Object>>();
                brandGroup.put(brand.getBrandId(), itemList);
                groupMap.put("itemList", itemList);
                groupList.add(groupMap);
            }
            itemList.add(map);
        }

        List<Address> addresses = userAddressService.getUserAddresses(userId);
        data.put("addresses", addresses);

        UserCoin userCoin = userCoinService.getUserCoin(userId);
        if (userCoin != null) {
            data.put("avalCoins", userCoin.getAvalCoins());
            data.put("unavalCoins", userCoin.getUnavalCoins());
        } else {
            data.put("avalCoins", 0);
            data.put("unavalCoins", 0);
        }
        return jsonResponse.setSuccessful().setData(data);
    }

    
	private String buildSkuSnapshot(List<ProductPropVO> skuProps) {
        StringBuilder builder = new StringBuilder();
        for (ProductPropVO prop : skuProps) {
            builder.append(prop.toString());
            builder.append("  ");
        }
        return builder.toString();
    }

	
	//删除旧表 清除废弃方法
//    /**
//     * 根据不同订单类型计算需付款的价值
//     * @param cityName 
//     * 
//     * @param unavalCoinUsed
//     * @param skuCountPairArray
//     * @param userDetail
//     * @return
//     */
//	@Deprecated
//    public JsonResponse preCalcPrice(long userId, String[] skuCountPairArray) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//
//        Order order = null;
//        ConsumeWrapper consumeWrapper = new ConsumeWrapper();
//        try{
//            order = orderFacade.buildOrderXX(userId, skuCountMap, consumeWrapper);
//        } catch (ProductUnavailableException e1) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
//        } catch (RemainCountLessException e2) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS);
//        } catch (UserCoinLessException e3) {
////            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        }
//
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<String, Object> data = new Hashtable<String, Object>();
//        data.put("originalAmountInCents", consumeWrapper.getOriginalAmountInCents());
//        data.put("unavalCoinUsed", consumeWrapper.getUnavalCoinUsed());
//        // calculate the pay type
//        data.put("pay_type", orderFacade.getPayAmountInCentsXX(order, OrderType.PAY));
//        // calculate the send back type
//        data.put("send_back_type", orderFacade.getPayAmountInCentsXX(order, OrderType.SEND_BACK));
//
//        return jsonResponse.setSuccessful().setData(data);
//    }


    private List<ProductSKU> sortProduId(List<ProductSKU> productSkus) {
    	Collections.sort(productSkus, 
				new Comparator<ProductSKU>() {
			public int compare(ProductSKU o1, ProductSKU o2) {
				return (int) (o2.getProductId() - o1.getProductId());
			}
		});
		return productSkus;
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
	//删除旧表
//	@Deprecated
//	public JsonResponse confirmOrder(String[] skuCountPairArray, int addressId, OrderType orderType, String remark,
//                                     ClientPlatform clientPlatform, String ip, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//        if (orderType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        long userId = userDetail.getUserId();
//        Address address = userAddressService.getUserAddress(userId, addressId);
//        if (address == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//
//        Order order = null;
//        try {
//            order = orderFacade.buildOrderXX(userId, skuCountMap, new ConsumeWrapper());
//        } catch (ProductUnavailableException e1) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
//        } catch (RemainCountLessException e2) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS);
//        } catch (UserCoinLessException e3) {
////            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        }
//
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        orderFacade.createOrder(order, orderType, remark, address, clientPlatform, ip);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("payAmountInCents", order.getPayAmountInCents());
//		data.put("orderNo", order.getOrderNo());
//
//        return jsonResponse.setSuccessful().setData(data);
//    }

	
	//删除旧表
//    @Deprecated
//	synchronized public JsonResponse confirmOrder17(String[] skuCountPairArray, int addressId, OrderType orderType,
//                                     String expressSupplier, String expressOrderNo, String phone, String remark,
//                                     Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash) {
//        JsonResponse jsonResponse = new JsonResponse();
//        
//        if (orderType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        } else if (orderType == OrderType.SEND_BACK) {
//            if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//                StringUtils.isBlank(phone)) {
//                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//            }
//        }
//
//        long userId = userDetail.getUserId();
//        Address address = userAddressService.getUserAddress(userId, addressId);
//        if (address == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//        
//        //限购校验
//        List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
//        sortProduId(productSkus);
//        Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
//        List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
//        if(products.size() > 0) {
//        	String description = productFacade.restrictDetail(products.get(0));
//        	return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
//        }
//
//        Order order = null;
//        ConsumeWrapper consumeWrapper = new ConsumeWrapper();
//        try {
//            order = orderFacade.buildOrder17(userId, skuCountMap, consumeWrapper, address, payCash, clientPlatform.getVersion());
//        } catch (ProductUnavailableException e) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
//        } catch (RemainCountLessException e) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(e.getMessage() + " 库存不足，请换其他商品进行兑换");
//        } catch (UserCoinLessException e) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        } catch (DeliveryLocationNullException e) {
//        	return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NULL);
//		} catch (DeliveryLocationNotFoundException e) {
//			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NOT_FOUND);
//		} catch (PayCheckNotEqualException e) {
//			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PAY_NOT_EQUAL).setError("您选购的商品信息发生了变化，请返回购物车重新结算后提交订单");
//		} catch (PostageNotFoundException e) {
//			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_POSATGE_NOT_FOUND);
//		}
//
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        orderFacade.createOrder17(order, orderType, expressSupplier, expressOrderNo, phone, remark, address,
//            clientPlatform, ip);
//
//        Map<String, Object> data = new HashMap<String, Object>();
////       data.put("payAmountInCents", order.getPayAmountInCents());
////        OrderNew orderNew = orderService.queryOrderNewFromOld(order);
//      //优惠券设置，日志
//    	String orderNo = order.getOrderNo();
//    	//删除旧表有修改注意测试
//    	OrderNew orderNew = orderService.getUserOrderNewByNo(orderNo);
//        
//        
////        data.put("payAmountInCents", consumeWrapper.getCash());
////        data.put("orderNo", order.getOrderNo());
//        data.put("payAmount", orderNew.getTotalPay() + orderNew.getTotalExpressMoney());
//        data.put("orderNoStr", orderNew.getOrderNoStr());
//        data.put("orderNo", order.getOrderNo());
//      
//        data.put("orderNoNew", orderNew.getOrderNo());
//        int  payCents = (int) (orderNew.getTotalPay() + orderNew.getTotalExpressMoney() );
//        data.put("payAmountInCents", payCents);
//        
//        if (cartIds != null && cartIds.length > 0) {
//            shoppingCartService.removeCartItems(userId, CollectionUtil.createSet(cartIds));
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    } 
    //删除旧表
//    public JsonResponse confirmOrder185(double actualDiscount,String[] skuCountPairArray, int addressId, OrderType orderType,
//    		String expressSupplier, String expressOrderNo, String phone, String remark,
//    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash) {
//    	return confirmOrder185(skuCountPairArray,  addressId,  orderType, expressSupplier,  expressOrderNo,  phone,  remark, cartIds,  clientPlatform,  ip,  userDetail,  payCash, null);
//    } 
	
	//删除旧表
//    public JsonResponse confirmOrder185(String[] skuCountPairArray, int addressId, OrderType orderType,
//    		String expressSupplier, String expressOrderNo, String phone, String remark,
//    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash, String couponId) {
//    	return confirmOrder185(skuCountPairArray,  addressId,  orderType, expressSupplier,  expressOrderNo,  phone,  remark, cartIds,  clientPlatform,  ip,  userDetail,  payCash, couponId, null);
//    } 
//    
    /**
     * 生成订单(没有用户分享ID)
     * @return
     */
    public JsonResponse confirmOrder185(String[] skuCountPairArray, int addressId, OrderType orderType,
    		String expressSupplier, String expressOrderNo, String phone, String remark,
    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash, String couponId, String statisticsIds) {
 
    	return confirmOrder185( skuCountPairArray,  addressId,  orderType,
        		 expressSupplier,  expressOrderNo,  phone,  remark,
        		cartIds,  clientPlatform,  ip,  userDetail,  payCash,  couponId,  statisticsIds, 0L, 0, 0);
    } 
    
    /**
     * 生成订单(有用户分享ID)
     * @return
     */
    synchronized public JsonResponse confirmOrder185(String[] skuCountPairArray, int addressId, OrderType orderType,
    		String expressSupplier, String expressOrderNo, String phone, String remark,
    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash, String couponId, String statisticsIds,long userSharedRecordId, int coinDeductFlag,
    		int takeGood) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
//    	if (orderType == null) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//    	}else if (orderType == OrderType.SEND_BACK) {
//    		if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) || StringUtils.isBlank(phone)) {
//    			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//    		}
//    	}
    	//1、获得收货地址
    	long userId = userDetail.getUserId();
    	Address address = null;
    	if(takeGood != 1){
	    	address = userAddressService.getUserAddress(userId, addressId);
	    	if (address == null) {
	    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
	    	}
    	}
    	
    	//2、解析规格和数量
    	Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
    	for (String skuCountPair : skuCountPairArray) {
    		String[] parts = StringUtils.split(skuCountPair, ":");
    		skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    	}
    	
    	//3、限购校验
    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
    	sortProduId(productSkus);

    	//4、积分兑换商品限制
    	if (checkLimitJiuCoinExchange(userId, productSkus, skuCountMap)) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("用户积分兑换商品超过次数");
    	}
    	
    	//5、积分商城是否已下架
    	if (checkOnSaleJiuCoinExchange(productSkus)) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("积分商城已下架");
		}
    	
    	//6、获取符合限购条件的商品
    	Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
    	if(products.size() > 0) {
    		String description = productFacade.restrictDetail(products.get(0));
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
    	}
    	
    	//7、组装订单对象，按仓库进行分组等
    	Order order = null;
    	ConsumeWrapper consumeWrapper = new ConsumeWrapper();
    	try {
    		order = orderFacade.buildOrder185(userId, skuCountMap, consumeWrapper, address, payCash, couponId, clientPlatform,ip,userSharedRecordId , coinDeductFlag,takeGood);
    	} catch (ProductUnavailableException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
    	} catch (RemainCountLessException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(e.getMessage() + " 库存不足，请换其他商品进行兑换");
    	} catch (UserCoinLessException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
    	} catch (DeliveryLocationNullException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NULL);
    	} catch (DeliveryLocationNotFoundException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NOT_FOUND);
    	} catch (PayCheckNotEqualException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PAY_NOT_EQUAL).setError("您选购的商品信息发生了变化，请返回购物车重新结算后提交订单");
    	} catch (PostageNotFoundException e) {
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_POSATGE_NOT_FOUND);
    	}
    	if (order == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	
    	//8、创建订单相关表记录                  long orderNo = orderFacade.createOrder17(order, expressSupplier, expressOrderNo, phone, remark, address,clientPlatform, ip, couponId,userSharedRecordId);
    	long orderNo = orderFacade.createOrderNew17(order,address,clientPlatform, ip, couponId,userSharedRecordId,takeGood);
     	
         //9、添加优惠券使用日志记录
    	 addCouponUseLog(couponId, order.getActualDiscount(), orderNo);
    	 
    	//10、首页统计处理
      	homeStatistics(statisticsIds, order);

    	//11、组装返回数据
      	OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
    	if (orderNew == null) {
     		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
     	}
    	Map<String, Object> data = new HashMap<String, Object>();
//      data.put("payAmountInCents", order.getPayAmountInCents());
    	data.put("couponId", couponId);
//        data.put("payAmountInCents", consumeWrapper.getCash());
//        data.put("orderNo", order.getOrderNo());
    	//System.out.println(orderNew.getTotalPay());
    	//System.out.println(orderNew.getTotalExpressMoney());
    	double payAmount = orderNew.getTotalPay() + orderNew.getTotalExpressMoney();
    	//String StringPayAmount = String.format("%.2f",payAmount);
    	String StringPayAmount = new Formatter().format("%.2f", payAmount).toString();
    	//System.out.println(StringPayAmount);
    	data.put("StringPayAmount",StringPayAmount);
    	data.put("payAmount", orderNew.getTotalPay() + orderNew.getTotalExpressMoney());
    	//orderNew.getTotalPay() + orderNew.getTotalExpressMoney()
    	
    	data.put("orderNoStr", orderNew.getOrderNoStr());
//    	data.put("orderNo", order.getOrderNo());
    	//删除旧表 	旧表已经删除//全部采用新表
    	data.put("orderNo", orderNew.getOrderNo());
    	
    	data.put("orderNoNew", orderNew.getOrderNo());
    	int  payCents = (int) (orderNew.getTotalPay() + orderNew.getTotalExpressMoney() );
    	data.put("payAmountInCents", payCents);
    	
    	if (cartIds != null && cartIds.length > 0) {
    		shoppingCartService.removeCartItems(userId, CollectionUtil.createSet(cartIds));
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }

    private void addCouponUseLog(String couponId, double couponCash, long orderNo) {
    	OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(orderNo));
    	if(couponId != null && couponId.length() > 0  ) {
//        		List<Coupon> couponList = orderService.getCouponByIdArr(couponId, orderNew.getUserId());
    		String[] cidArr = couponId.split(",");
    		orderService.updateCouponUsed(cidArr , orderNew.getOrderNo());
    		for(String  cid : cidArr){
    			CouponUseLog couponUseLog = new CouponUseLog();
    			
    			couponUseLog.setOrderNo(orderNew.getOrderNo());
    			couponUseLog.setUserId(orderNew.getUserId());
    			try {
    				couponUseLog.setCouponId(Long.parseLong(cid));
    			} catch (Exception e) {
    				couponUseLog.setCouponId(0);
    			}
    			couponUseLog.setStatus(0);
    		    double actualDiscount = couponCash; //实际折扣金额
    			couponUseLog.setActualDiscount(actualDiscount);
    			couponUseLog.setCreateTime(System.currentTimeMillis());
    			orderService.insertCouponUseLog(couponUseLog);
    		}
    		
    	}
    }

    /**
     * 首页统计
     * @param statisticsIds
     * @param orderNew
     */
	private void homeStatistics(String statisticsIds, Order order) {
		if(statisticsIds != null && statisticsIds.length() > 0  ) {
    		try {
				String[] idGroupArr = statisticsIds.split(",");
				if(idGroupArr.length > 0){
					List<OrderItem> orderItems = order.getOrderItems();
					//TODO 主要这里的item是否为null没有获取item
//					List<OrderItem> orderItems = orderNew.getOrderItems();
					Set<Long> sidSet = new HashSet<Long>();
					for(String idGroup : idGroupArr){
						String[] idArr = idGroup.split(":");
						
							if(idArr.length > 1 && idArr[0] != null && idArr[1] !=null && idArr[0].length() > 0 && idArr[1] .length() > 0){
								sidSet.add(Long.parseLong(idArr[1]));
								for(OrderItem orderItem: orderItems){
									if((orderItem.getSkuId() + "").equals(idArr[0])){
										orderItem.setStatisticsId(idArr[1]);
									}			
								}	
							}	
					}
					statisticsService.batchUpdateOrderCount(sidSet);
				}
			} catch (Exception e) {
			}
    		
    	}
	}
    //删除旧表
//    /**
//     * 生成订单
//     * @param skuCountPairArray
//     * @param addressId
//     * @param orderType
//     * @param expressSupplier
//     * @param expressOrderNo
//     * @param phone
//     * @param remark
//     * @param cartIds
//     * @param clientPlatform
//     * @param ip
//     * @param userDetail
//     * @param payCash
//     * @param couponId
//     * @param statisticsIds
//     * @param userSharedRecordId
//     * @return
//     */
//    synchronized public JsonResponse confirmOrder213(String[] skuCountPairArray, int addressId, OrderType orderType,
//    		String expressSupplier, String expressOrderNo, String phone, String remark,
//    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash, String couponId, String statisticsIds,long userSharedRecordId) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	data.put("couponId", couponId);
//    	if (orderType == null) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//    	} else if (orderType == OrderType.SEND_BACK) {
//    		if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//    				StringUtils.isBlank(phone)) {
//    			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//    		}
//    	}
//    	
//    	long userId = userDetail.getUserId();
//    	Address address = userAddressService.getUserAddress(userId, addressId);
//    	if (address == null) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//    	}
//    	
//    	
//    	Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//    	for (String skuCountPair : skuCountPairArray) {
//    		String[] parts = StringUtils.split(skuCountPair, ":");
//    		skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//    	}
//    	
//    	//限购校验
//    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
//    	sortProduId(productSkus);
//    	
//    	/**
//    	 * 积分兑换商品限制
//    	 */
//    	if (checkLimitJiuCoinExchange(userId, productSkus, skuCountMap)) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("用户积分兑换商品超过次数");
//    	}
//    	
//    	/**
//    	 * 积分商城是否已下架
//    	 */
//    	if (checkOnSaleJiuCoinExchange(productSkus)) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("积分商城已下架");
//    	}
//    	Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
//    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userId, productCountMap);
//    	if(products.size() > 0) {
//    		String description = productFacade.restrictDetail(products.get(0));
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
//    	}
//    	Order order = null;
//    	ConsumeWrapper consumeWrapper = new ConsumeWrapper();
//    	try {
//    		order = orderFacade.buildOrder213(userId, skuCountMap, consumeWrapper, address, payCash, couponId, clientPlatform.getVersion());
//    	} catch (ProductUnavailableException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
//    	} catch (RemainCountLessException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(e.getMessage() + " 库存不足，请换其他商品进行兑换");
//    	} catch (UserCoinLessException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//    	} catch (DeliveryLocationNullException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NULL);
//    	} catch (DeliveryLocationNotFoundException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_DELIVERYLOCATION_NOT_FOUND);
//    	} catch (PayCheckNotEqualException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PAY_NOT_EQUAL).setError("您选购的商品信息发生了变化，请返回购物车重新结算后提交订单");
//    	} catch (PostageNotFoundException e) {
//    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_POSATGE_NOT_FOUND);
//    	}
//    	if (order == null) {
//    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//    	}
//    	//首页统计处理
//    	if(statisticsIds != null && statisticsIds.length() > 0  ) {
//    		try {
//    			String[] idGroupArr = statisticsIds.split(",");
//    			if(idGroupArr.length > 0){
//    				List<OrderItem> orderItems = order.getOrderItems();
//    				Set<Long> sidSet = new HashSet<Long>();
//    				for(String idGroup : idGroupArr){
//    					String[] idArr = idGroup.split(":");
//    					
//    					if(idArr.length > 1 && idArr[0] != null && idArr[1] !=null && idArr[0].length() > 0 && idArr[1] .length() > 0){
//    						sidSet.add(Long.parseLong(idArr[1]));
//    						for(OrderItem orderItem: orderItems){
//    							if((orderItem.getSkuId() + "").equals(idArr[0])){
//    								orderItem.setStatisticsId(idArr[1]);
//    							}			
//    						}	
//    					}	
//    				}
//    				statisticsService.batchUpdateOrderCount(sidSet);
//    			}
//    		} catch (Exception e) {
//    		}
//    		
//    	}
//    	orderFacade.createOrder17(order, orderType, expressSupplier, expressOrderNo, phone, remark, address,
//    			clientPlatform, ip, couponId,userSharedRecordId);
//    	
//    	
////       data.put("payAmountInCents", order.getPayAmountInCents());
//    	//优惠券设置，日志
////    	OrderNew orderNew = orderService.queryOrderNewFromOld(order);
//    	OrderNew orderNew = orderService.getUserOrderNewByNo(String.valueOf(order.getId()));
//    	if(couponId != null && couponId.length() > 0  ) {
//    		
////    		List<Coupon> couponList = orderService.getCouponByIdArr(couponId, orderNew.getUserId());
//    		String[] cidArr = couponId.split(",");
//    		orderService.updateCouponUsed(cidArr , orderNew.getOrderNo());
//    		for(String  cid : cidArr){
//    			CouponUseLog couponUseLog = new CouponUseLog();
//    			
//    			couponUseLog.setOrderNo(orderNew.getOrderNo());
//    			couponUseLog.setUserId(orderNew.getUserId());
//    			try {
//    				couponUseLog.setCouponId(Long.parseLong(cid));
//    			} catch (Exception e) {
//    				couponUseLog.setCouponId(0);
//    			}
//    			couponUseLog.setStatus(0);
//    			couponUseLog.setActualDiscount(order.getActualDiscount());
//    			couponUseLog.setCreateTime(System.currentTimeMillis());
//    			orderService.insertCouponUseLog(couponUseLog);
//    		}
//    		
//    	}
//    	
////        data.put("payAmountInCents", consumeWrapper.getCash());
////        data.put("orderNo", order.getOrderNo());
//    	//System.out.println(orderNew.getTotalPay());
//    	//System.out.println(orderNew.getTotalExpressMoney());
//    	double payAmount = orderNew.getTotalPay() + orderNew.getTotalExpressMoney();
//    	//String StringPayAmount = String.format("%.2f",payAmount);
//    	String StringPayAmount = new Formatter().format("%.2f", payAmount).toString();
//    	//System.out.println(StringPayAmount);
//    	data.put("StringPayAmount",StringPayAmount);
//    	data.put("payAmount", orderNew.getTotalPay() + orderNew.getTotalExpressMoney());
//    	//orderNew.getTotalPay() + orderNew.getTotalExpressMoney()
//    	
//    	data.put("orderNoStr", orderNew.getOrderNoStr());
//    	data.put("orderNo", orderNew.getOrderNo());//order.getId()
//    	data.put("orderNoNew", orderNew.getOrderNo());
//    	int  payCents = (int) (orderNew.getTotalPay() + orderNew.getTotalExpressMoney() );
//    	data.put("payAmountInCents", payCents);
//    	
//    	if (cartIds != null && cartIds.length > 0) {
//    		shoppingCartService.removeCartItems(userId, CollectionUtil.createSet(cartIds));
//    	}
//    	return jsonResponse.setSuccessful().setData(data);
//    }
    private boolean checkOnSaleJiuCoinExchange(List<ProductSKU> productSkus) {
    	Set<Long> productIds = homeFloorService.getPonintTemplateProudctIds();
    	for (ProductSKU productSKU : productSkus) {
    		Product product = productService.getProductById(productSKU.getProductId());
			if (product.getJiuCoin() > 0) {
				if (!productIds.contains(productSKU.getProductId())) {
					return true;
				}
			}
			
		}
		return false;
	}
    //删除旧表
//	@SuppressWarnings("deprecation")
//	public JsonResponse confirmOrderXX(String[] skuCountPairArray, int addressId, OrderType orderType,
//                                     String expressSupplier, String expressOrderNo, String phone, String remark,
//                                     Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//        if (orderType == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        } else if (orderType == OrderType.SEND_BACK) {
//            if (StringUtils.isBlank(expressSupplier) || StringUtils.isBlank(expressOrderNo) ||
//                StringUtils.isBlank(phone)) {
//                return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//            }
//        }
//
//        long userId = userDetail.getUserId();
//        Address address = userAddressService.getUserAddress(userId, addressId);
//        if (address == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
//        for (String skuCountPair : skuCountPairArray) {
//            String[] parts = StringUtils.split(skuCountPair, ":");
//            skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
//        }
//
//        Order order = null;
//        try {
//            order = orderFacade.buildOrderXX(userId, skuCountMap, new ConsumeWrapper());
//        } catch (ProductUnavailableException e1) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_UNAVAILABLE);
//        } catch (RemainCountLessException e2) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS);
//        } catch (UserCoinLessException e3) {
//            return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//        }
//
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//
//        orderFacade.createOrder(order, orderType, expressSupplier, expressOrderNo, phone, remark, address,
//            clientPlatform, ip);
//
//        Map<String, Object> data = new HashMap<String, Object>();
//        data.put("payAmountInCents", order.getPayAmountInCents());
//        data.put("orderNo", order.getOrderNo());
//        
//        if (cartIds != null && cartIds.length > 0) {
//            shoppingCartService.removeCartItems(userId, CollectionUtil.createSet(cartIds));
//        }
//
//        return jsonResponse.setSuccessful().setData(data);
//    }
	//删除旧表
//    public JsonResponse sendBackConfirm(String orderNo, String expressSupplier, String expressOrderNo, String phone,
//                                        UserDetail userDetail) {
//        JsonResponse jsonResponse = new JsonResponse();
//
//        if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(expressSupplier) ||
//            StringUtils.isBlank(expressOrderNo) || StringUtils.isBlank(phone)) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_PARAMETER_MISSING);
//        }
//
//        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
//        }
//
//        orderService.addSendBack(order, expressSupplier, expressOrderNo, phone);
//
//        return jsonResponse.setSuccessful();
//    }

    public JsonResponse expressQuery(String orderNo, UserDetail userDetail) {
//    	Order order = orderService.getUserOrderByNo(userDetail.getUserId(), orderNo);
    	OrderNew orderNew = orderService.getUserOrderNewByNo(userDetail.getUserId(), orderNo);
        JsonResponse jsonResponse = new JsonResponse();
        if (null != orderNew) {
//            String supplier = orderNew.getExpressSupplier();
        	//TODO 做个修改从物流信息表中取数据
          //取最新的一条物流信息
        	ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getUserId(), Long.parseLong(orderNo));
        	String supplier = info.getExpressSupplier();
        	String expressOrderNo = info.getExpressOrderNo();
            
            Map<String, Object> data = new HashMap<String, Object>();
//            String expressOrderNo = order.getExpressOrderNo();
            if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
                JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
                data.put("data", expressData);
            }
            return jsonResponse.setSuccessful().setData(data);
        }
        return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    }
//    删除旧表
//    public JsonResponse cancelOrder(String orderNo, UserDetail userDetail, ClientPlatform clientPlatform) {
//        JsonResponse jsonResponse = new JsonResponse();
//        long userId = userDetail.getUserId();
//        Order order = orderService.getUserOrderByNo(userId, orderNo);
//        if (order == null) {
//            return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
//        }
//        orderFacade.cancelOrder(order,clientPlatform.getVersion());
//        return jsonResponse.setSuccessful();
//    }
    
    public JsonResponse cancelOrderNew(String orderNo, UserDetail userDetail,  String cancelReason, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getUserId();
    	OrderNew order = orderService.getUserOrderNewByNo(userId, orderNo);
    	if (order == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	order.setCancelReason(cancelReason);
    	orderFacade.cancelOrderNew(order,clientPlatform.getVersion());
    	return jsonResponse.setSuccessful();
    }
    
    public OrderNew getSplitOrderDetail(long userId, String orderNo) {
    	OrderNew order = orderService.getUserOrderNewByNo(userId, orderNo);
    	if(order!=null && order.getParentId() == -1){
    		Set<Long> orderNOs = new HashSet<Long>();
    		orderNOs.add(order.getOrderNo());
    		List<OrderNewVO> childOrderList = orderService.getChildOrderList(userId, orderNOs);
    		
    		orderNOs.clear();
    		for(OrderNew childOrder : childOrderList ){
    			orderNOs.add(childOrder.getOrderNo());
    		}
    		List<OrderItem> orderItemList;
			orderItemList = orderService.getOrderNewItems(userId, orderNOs);
			order.setOrderItems(new ArrayList<OrderItem>(orderItemList));
    		
    		Map<Long, List<OrderItemVO>> orderItemsMap = orderFacade.getOrderNewItemVOMap(userId, orderNOs);
    		
    		for (OrderNew childOrder : childOrderList) {
        		List<OrderItemVO> orderItems = orderItemsMap.get(childOrder.getOrderNo());
        		if(orderItems != null){ 			
        			childOrder.setOrderItems(new ArrayList<OrderItem>(orderItems));
        		}
        	}
    		order.setChildOrderList(childOrderList);
    	}
    	
    	return order;
    }
    public OrderNewVO getOrderNewDetail(long userId, String orderNo) {
    	OrderNewVO order = orderService.getUserOrderNewDetailByNo(userId, orderNo);
    	
	    
	    return order;
    }
    
    public Map<String, String> getNewestTrackInfo(long userId, long orderNo) {
    	//取最新的一条物流信息
    	ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userId, orderNo);
    	String context = "";
    	String time = "";
    	Map<String, String> result = new HashMap<String, String>();
    	if (null == info) {
    		context = "暂无物流信息";
    		result.put("context", context);
        	result.put("time", time); 
        	return result;
    	}
    	JSONObject object;
    	String supplier = info.getExpressSupplier();
    	String expressOrderNo = info.getExpressOrderNo();
    	if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
    		JSONObject expressData = (JSONObject) expressService.queryExpressInfo(supplier, expressOrderNo);
    		if (expressData != null && expressData.get("result") != null && ((JSONObject)expressData.get("result")).get("data") != null){
    			List<JSONObject> trackList= (List<JSONObject>) ((JSONObject)expressData.get("result")).get("data");
    			if(trackList.size()>0){
    				
    				object = trackList.get(trackList.size()-1);
    				if(object != null && object.get("context")!=null){
    					context = (String) object.get("context");
    				}
    				if(object != null && object.get("time")!=null){
    					time = (String) object.get("time");
    				}
    			}
    		}
    	}
    	result.put("context", context);
    	result.put("time", time);
    	
    	return result;
    }
    public JsonResponse getExpressInfo(long userId, long orderNo) {
    	Map<String, String> result = new HashMap<String, String>();
    	JsonResponse jsonResponse = new JsonResponse();
    	// getMemCache
        String groupKey = MemcachedKey.GROUP_KEY_EXPRESS_DETAIL;
        String key = orderNo + "";
        Map<String, String> obj = (Map<String, String>) memcachedService.get(groupKey, key);
        if(obj != null){
        	result.put("orderStatusName", obj.get("orderStatusName"));
        	result.put("wareHouseName", obj.get("wareHouseName"));
        	result.put("expressNumber", obj.get("expressNumber"));
        	result.put("itemNum", obj.get("itemNum"));
        	
        }else{
        	
        	OrderNew  orderNew = orderService.getUserOrderNewByNo(userId, orderNo + "");
        	
        	if (orderNew == null) {
        		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
        	}
        	String orderStatusName = "";
        	String wareHouseName = "";
        	String expressNumber = "";
        	int itemNum = 0;
        	if(orderNew != null){
        		orderStatusName = orderNew.getOrderStatusName();
        		LOWarehouse loWarehouse = lOWarehouseService .getById(orderNew.getlOWarehouseId());
        		if(loWarehouse != null && loWarehouse.getName() != null){
        			wareHouseName = loWarehouse.getName();
        		}
        	}
        	if(orderNew.getOrderType() == 1){
        		ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userId, orderNo);
        		if(serviceTicket != null){
        			expressNumber = serviceTicket.getSellerExpressNo();
        			itemNum = serviceTicket.getApplyReturnCount();	
        		}
        	}else{
        		ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userId, orderNo);
        		if(info != null){
        			expressNumber = info.getExpressOrderNo();
        		}        	
        		List<OrderItem> items = orderService.getOrderNewItemsByOrderNO(userId, orderNo);
        		if(items != null && items.size() > 0){
        			for(OrderItem item: items){
        				itemNum += item.getBuyCount();
        			}
        		}
        		
        	}
        	result.put("itemNum", itemNum + "");
        	result.put("expressNumber", expressNumber);
        	result.put("orderStatusName", orderStatusName);
        	result.put("wareHouseName", wareHouseName);
        	memcachedService.set(groupKey, key, DateConstants.SECONDS_TEN_MINUTES, result);
        }
        return jsonResponse.setSuccessful().setData(result);
    	//return result;
    }
    public Map<String, Object> getOrderSplitDetail( UserDetail userDetail, String orderNo) {
    	Map<String, Object> data = new HashMap<String, Object>();
    	OrderNew order = this.getSplitOrderDetail(userDetail.getUserId(), orderNo);
    	data.put("order", order);
    	
    	double deductibleAmount = 0;
    	double discountAmount = 0;
    	
    	
    	for(OrderItem item : order.getOrderItems() ){
    		deductibleAmount += item.getTotalMarketPrice() - item.getTotalMoney();
    	}
    	discountAmount = order.getTotalMarketPrice() + order.getTotalExpressMoney() - deductibleAmount - order.getTotalPay();
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ORDER_AUTO_CONFIRM_TIME);
    	JSONArray jsonArrayExp = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
        int minuteTime = 60 * 1000;
        int confirmMinutes = 0;
        int payExpMinutes = 0;
		for(Object obj : jsonArrayConfirm) {
			confirmMinutes = (int) ((JSONObject)obj).get("autoConfirmMinutes");
			break;
		}
		for(Object obj : jsonArrayExp) {
			payExpMinutes = (int) ((JSONObject)obj).get("overdueMinutes");
			break;
		}
		if(confirmMinutes <= 0){
			confirmMinutes = 20160;
		}
		if(payExpMinutes <=0){
			payExpMinutes = 1440;
		}
		
    	long createTime = order.getCreateTime();
    	data.put("createTime", sdf.format(new Date(createTime)));
    	if(order.getOrderStatus().getIntValue() > 0){
    		long payTime = 0; 
    		OrderNewLog orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(order.getOrderNo(), OrderStatus.PAID);
    		if(orderNewLog!=null){
    			payTime = orderNewLog.getCreateTime();		
    		}
    		if(payTime > 0){
    			long autoConfirmTime = payTime + minuteTime * confirmMinutes ;
    			data.put("autoConfirmTime", autoConfirmTime);
    			data.put("payTime", sdf.format(new Date(payTime)));
    		} 
    	}
//    	else if(order.getOrderStatus().getIntValue() == 0){
//    		long expireTime = createTime + payExpMinutes * minuteTime;
//    		data.put("expireTime", expireTime);
//    	}
    	
    	
    	
    	data.put("deductibleAmount", deductibleAmount);
    	data.put("discountAmount", discountAmount);
    	
    	String splitReason = "您订单中的商品在不同的库房，故拆分为以下订单分开配送，给您带来的不便敬请谅解";
    	data.put("splitReason", splitReason);
    	
    	String splitMsg = "本订单已按配送包裹拆分成多个订单";
    	data.put("splitMsg", splitMsg);
    	
    	
    	
    	//取最新的一条物流信息
    	
    	Map<String, String> result = this.getNewestTrackInfo(userDetail.getUserId(), order.getOrderNo());
    	data.put("trackContext", result.get("context"));
    	data.put("trackTime", result.get("time"));
    	
    	
    	return data;
    }
    public Map<String, Object> orderPayChoose( UserDetail userDetail) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<PayTypeVO>  payTypeVOList= new ArrayList<PayTypeVO>();
    	PayTypeVO payTypeVO = new PayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/yiwangtong.jpg");
    	payTypeVO.setName("一网通银行卡支付");//（首单支付随机立减1-99元）
    	payTypeVO.setType(5);
    	payTypeVOList.add(payTypeVO);
    	
    	payTypeVO = new PayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/weixin.jpg");
    	payTypeVO.setName("微信支付");
    	payTypeVO.setType(3);
    	payTypeVOList.add(payTypeVO);
    	
    	payTypeVO = new PayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/zhifubao.jpg");
    	payTypeVO.setName("支付宝支付");
    	payTypeVO.setType(2);
    	payTypeVOList.add(payTypeVO);
    	data.put("paylist", payTypeVOList);
    	return data;
    	
    
    
    }
    public Map<String, Object> getOrderDetail( UserDetail userDetail, String orderNo) {
    		
    	Map<String, Object> data = new HashMap<String, Object>();
    	OrderNewVO order = this.getOrderNewDetail(userDetail.getUserId(), orderNo);
    	if(order!=null){
    		data.put("order", order);
    	}
    	
    	storeAssembler.assemble(CollectionUtil.createList(order));
    	
    	double deductibleAmount = 0;  //玖币抵扣金额
    	double discountAmount = 0;		//优惠金额
    	for(OrderItem item : order.getOrderItems() ){
    		deductibleAmount += item.getTotalMarketPrice() - item.getTotalMoney();
    	}
    	discountAmount = order.getTotalMarketPrice() - deductibleAmount - order.getTotalPay();
    	
    	if(discountAmount < 0){
    		discountAmount = 0;
    	}
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ORDER_AUTO_CONFIRM_TIME);
    	JSONArray jsonArrayExp = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
        int minuteTime = 60 * 1000;
        int confirmMinutes = 0;
        int payExpMinutes = 0;
		for(Object obj : jsonArrayConfirm) {
			confirmMinutes = (int) ((JSONObject)obj).get("autoConfirmMinutes");
			
			break;
		}
		for(Object obj : jsonArrayExp) {
			payExpMinutes = (int) ((JSONObject)obj).get("overdueMinutes");
			break;
		}
		if(confirmMinutes <= 0){
			confirmMinutes = 20160;
		}
		if(payExpMinutes <=0){
			payExpMinutes = 1440;
		}
		
    	long createTime = order.getCreateTime();
    	long dealTime = createTime;
    	if(order.getOrderStatus() != null && order.getOrderStatus().getIntValue() == 100){
    		dealTime = order.getUpdateTime();
    	}
    	data.put("dealTime", sdf.format(new Date(dealTime)));
    	if(order.getOrderStatus().getIntValue() > 0){
    		long payTime = 0; 
    		long shipTime = 0; 
    		OrderNewLog orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(order.getOrderNo(), OrderStatus.PAID);
    		if(orderNewLog!=null){
    			payTime = orderNewLog.getCreateTime();		
    		}
    		orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(order.getOrderNo(), OrderStatus.DELIVER);
    		if(orderNewLog!=null){
    			shipTime = orderNewLog.getCreateTime();	
    			data.put("shipTime", sdf.format(new Date(shipTime)));
    		}
    		if (payTime == 0) {
    			payTime = order.getPayTime();
    		}
    		
    		if(payTime > 0){
    			long autoConfirmTime = payTime + minuteTime * confirmMinutes ;
    			data.put("autoConfirmTime", autoConfirmTime);
    			data.put("payTime", sdf.format(new Date(payTime)));
    			data.put("payTypeStr", order.getPaymentType());
    		} 
    	}
//    	else if(order.getOrderStatus().getIntValue() == 0){
//    		long expireTime = createTime + payExpMinutes * minuteTime;
//    		data.put("expireTime", expireTime);
//    	}
    	
    	data.put("deductibleAmount", deductibleAmount);
    	data.put("discountAmount", discountAmount);
    	
    	//取最新的一条物流信息
    	if(order.getOrderStatus().getIntValue() >= 50 && order.getOrderStatus().getIntValue() != 100){
    		
	    	Map<String, String> result = this.getNewestTrackInfo(userDetail.getUserId(), order.getOrderNo());
	    	data.put("trackContext", result.get("context"));
	    	data.put("trackTime", result.get("time"));
    	}
    	
    	String shipMsg = "订单正在处理中，请耐心等待";
    	String closedMsg = "我不想买了";
    	if(order.getCancelReason() != null && order.getCancelReason().length() > 0){
    		closedMsg += "（"+order.getCancelReason()+"）";
    	}
    	String cancelingMsg = "请耐心等待系统处理";
    	String splitMsg = "本订单已按配送包裹拆分成多个订单";
    	
    	String arrivingMsg = "";
    	data.put("shipMsg", shipMsg);
    	data.put("closedMsg", closedMsg);
    	data.put("cancelingMsg", cancelingMsg);
    	data.put("splitMsg", splitMsg);
    	
    	List<String> cancelReasonList = new ArrayList<String>(); 
    	cancelReasonList.add("我不想买了");
    	cancelReasonList.add("信息填写错误");
    	cancelReasonList.add("其它");
    	data.put("cancelReasonList", cancelReasonList);
    	
    	String refundedMsg = "您的退款将于1到3个工作日退返还到支付账户。";
    	data.put("refundedMsg", refundedMsg);
    	
    	
    	return data;
    }
    
    //不分页的优惠券列表
    public List<Coupon> getUserCouponList(long userId, OrderCouponStatus status) {
    	return orderService.getUserOrderCoupon(userId, status);
    }
}
