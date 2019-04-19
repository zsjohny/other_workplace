package com.store.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.ProductSKU;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.order.ConsumeWrapper;
import com.jiuyuan.entity.order.RestrictProductVO;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.service.shop.UserAddressService;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ProductMapper;
import com.store.dao.mapper.ProductSKUMapper;
import com.store.dao.mapper.StoreOrderMapper;
import com.store.entity.Brand;
import com.store.entity.ProductPropVO;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.entity.ShopCoupon;
import com.store.entity.ShopStoreOrder;
import com.store.enumerate.OrderType;
import com.store.service.brand.ShopBrandService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PayCheckNotEqualException;
import com.yujj.exception.order.PostageNotFoundException;
import com.yujj.exception.order.ProductUnavailableException;
import com.yujj.exception.order.RemainCountLessException;
import com.yujj.exception.order.UserCoinLessException;

/**
* @author QiuYuefan
*/

@Service
public class ShopOrderService{
	
	private static final Log logger = LogFactory.get();
	
	@Autowired
    private ProductSKUMapper productSKUMapper;
	
	@Autowired
    private MemcachedService memcachedService;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private IncomeAssembler incomeAssembler;
	
	@Autowired
    private ProductFacadeShop productFacade;
	
	@Autowired
    private ProductPropAssemblerShop productPropAssembler;
	
	@Autowired
    private ProductServiceShop productService;
	
	@Autowired
    private ShopBrandService shopBrandService;
	
	@Autowired
    private OrderFacade orderFacade;
	
	@Autowired
	private UserAddressService userAddressService;
	
	@Autowired
	private ShopGlobalSettingService globalSettingService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private StoreOrderMapper storeOrderMapper;
	/**
	 * 确认订单（3.0已经奔新接口替代）
	 * @param userDetail
	 * @param cityName
	 * @param skuCountPairArray
	 * @param clientPlatform
	 * @return
	 */
	public JsonResponse buildOrder185(UserDetail<StoreBusiness> userDetail, String cityName, String[] skuCountPairArray,
			ClientPlatform clientPlatform) {
		JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
    	for (String skuCountPair : skuCountPairArray) {
    		String[] parts = StringUtils.split(skuCountPair, ":");
    		skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    	}
    	
    	//获取用户购买的sku信息
    	List<ProductSKU> productSkus = null;
    	if (skuCountMap.keySet().size() < 1) {
    		productSkus =  new ArrayList<>();
		}else{
			productSkus = productSKUMapper.getProductSKUs(skuCountMap.keySet());
		}
    	
    	
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
    			Product product = getProductMap(CollectionUtil.createSet(sku.getProductId()), userDetail).get(sku.getProductId());
    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(product.getName() + " 库存不足，请编辑购物车");
    		}
    		
    		productIds.add(sku.getProductId());
    		List<ProductPropVO> skuProps = sku.getProductProps();
    		composites.addAll(skuProps);
    		skuPropMap.put(sku.getId(), skuProps);            	
    	}
    	
    	//限购
    	sortProduId(productSkus);
    	Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userDetail, productCountMap);
    	if(products.size() > 0) {
    		String description = productFacade.restrictDetail(products.get(0));
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
    	}
    	
    	productPropAssembler.assemble(composites);
    	
    	Map<Long, ProductVOShop> productMap = productService.getProductMap(productIds);

    	incomeAssembler.assemble(productMap, userDetail);
    	Set<Long> brandIds = new HashSet<Long>();
    	for (Product product : productMap.values()) {
    		brandIds.add(product.getBrandId());
    	}
    	
    	Map<Long, Brand> brandMap = shopBrandService.getBrands();
    	
    	for (ProductSKU sku : productSkus) {
//    		Product product = productMap.get(sku.getProductId());
    		//判断失效
    		if ( !sku.getOnSaling() ) {
    			Product product = productService.getProductById(sku.getProductId());
    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_REMAIN_COUNT_LESS).setError(product.getName() + " 库存不足，请编辑购物车");
    		}
    	}
    	
    	//计算分类商品的优惠后的价格
    	Map<String, Object> price = orderFacade.calculateDiscountV185(productSkus, productIds, productMap, skuCountMap, brandMap, null, userDetail.getId(), clientPlatform.getVersion(), "build");
    	
    	//拼装以库存为分类标准的数据，计算邮费
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<Map<String, Object>> groupList = new ArrayList<Map<String, Object>>();
    	groupList = orderFacade.wrapperGroupList(productSkus, productMap, skuCountMap, skuPropMap);
    	data.put("groupList", groupList);
    	
    	
//    	
//    	List<ProductVO> productList = productService.getProductListByIds(productIds);
//    	data.put("productList", ProductUtil.getProductSimpleList(productList));
//    	
//    	//以商品为单位显示
//    	try {
//    		for(Product product : productList){
//    			for(Map<String, Object> skuMap : groupList){
//    				if(skuMap != null && skuMap.get("itemList") != null){
//    					for(Map<String, Object>  item : (List<Map<String, Object>>) skuMap.get("itemList")){
//    						if(item.get("productId") != null && item.get("productId").toString() .equals( product.getId() + "")){
//    							if(product.getSkuList() == null){
//    								product.setSkuList(new ArrayList<Map<String, Object>>());
//    							}
//    							product.getSkuList().add(item);
//    						}
//    					}
//    				}
//    				
//    			}
//    		}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
    	
    	
    	// 是否显示去凑单区域
    	data.put("isShow", true);
    	
    	// 温馨提示
    	data.put("warmTip", "温馨提示：因商品于不同库房发货，系统已自动分配商品包裹并计算配送费用。");
    	
    	List<Address> addresses = userAddressService.getUserAddresses(userDetail.getId());
    	data.put("addresses", addresses);
    	
    	//temp added by dongzhong 2016-07-13 限制用户一天零元购最多3件
    	int zeroCount = orderFacade.calculateZeroBuy(userDetail, productIds, productSkus);
    	int zeroLimit = globalSettingService.getInt(GlobalSettingName.ZERO_LIMIT);
    	if (zeroCount > zeroLimit) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每天仅限" + zeroLimit + "件哦");	//"0元购商品超过当天购买件数"+zeroLimit
    	
    	int zeroCountMonthly = orderFacade.calculateZeroBuyMonthly(userDetail, productIds, productSkus);
    	int zeroLimitMonthly = 5;
    	JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ZERO_LIMIT_MONTHLY);
    	for(Object obj : jsonArrayConfirm) {
    		zeroLimitMonthly = (int) ((JSONObject)obj).get("zeroLimitMonthly");
    		break;
    	}
    	if (zeroCountMonthly > zeroLimitMonthly) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_ZERO_OUT).setError("亲，0元秒杀每月仅限" + zeroLimitMonthly + "件哦");
    	
    	//统计组合限购，如果为true则为超出限购数量
    	Map<String, Object> restrictResult = orderFacade.calculateUserRestrictBuy(userDetail.getId(), productIds, productMap,  productSkus, skuCountMap);
    	if ((boolean) restrictResult.get("result")) return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_RESTRICT_GROUP).setError("亲，您购买的商品" + restrictResult.get("restrictProductName") + "超过组合限购量了哦");
    	
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
    	

    	
//    	if(VersionUtil.compareVersion(clientPlatform.getVersion() , "1.8.11") < 0){
//    		UserCoin userCoin = userCoinService.getUserCoin(userId);
//    		
//    		if (userCoin == null) {
//    			logger.error("user coin can not null, userId:{}", userId);
//    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//    		} else if (userCoin.getAllCoins() < (int)price.get("totalJiuCoin")) {
//    			logger.error("user coin is less than total consume, user coins:{}, totalConsume:{}",
//    					userCoin.getAllCoins(), (int)price.get("totalJiuCoin"));
//    			return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_USER_COIN_LESS);
//    		}
//    	}
    	
    	Map<String, Object> price2 = new HashMap<String, Object>();
    	int originalPrice = (int) price.get("originalPrice");
    	double totalCash = (double) price.get("totalCash");
    	double discountCash = (double) price.get("discountCash");
    	double prepay = (double) price.get("prepay");
    	double totalMoney = (double) price.get("totalMoney");
    	double firstDiscountCash = (double) (price.get("firstDiscountCash"));
    	String totalCommission = (String) (price.get("totalCommission"));
    	String firstDiscountAble = (String) price.get("firstDiscountAble");
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
    	price2.put("totalMoney", totalMoney);
    	price2.put("jiuCoinDiscount", jiuCoinDiscount);
    	price2.put("firstDiscountCash", firstDiscountCash);
    	price2.put("firstDiscountAble", firstDiscountAble);
    	price2.put("totalCommission", totalCommission);
    	price2.put("commissionDeduction", "- ¥ " + totalCommission);
    	
    	double firstDiscount = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
		data.put("firstDiscount", firstDiscount);
    	data.put("firstDiscountTips", "新用户首单" + firstDiscount + "元优惠");
    	int count = orderService.getUserOrderCountForFirstDiscount(userDetail.getId());
    	if (count == 0 ){
    		data.put("firstDiscountAble", "YES");
    	}else {
    		data.put("firstDiscountAble", "NO");
    	}
    	
    	data.put("price", price2);
    	data.put("commissionPercentage", userDetail.getUserDetail().getCommissionPercentage());
    	data.put("postage", totalPostage);
    	data.put("couponFetchFlag", "YES"); //优惠券领取开关
    	//取订单可用优惠券
    	//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌，  5：免邮
    	List<ShopCoupon> couponList = orderService.getUserOrderCoupon(userDetail.getId(), OrderCouponStatus.UNUSED);
    	List<ShopCoupon> couponUsableList = getAvailableCoupon( userDetail.getId(),   prepay , productCountMap,   productMap , productIds,  couponList, totalPostage);
    
    	orderService.getCouponLimitContentList(couponList);
    	data.put("couponList", couponUsableList);
    	data.put("view", "查看");
    	data.put("couponCenterUrl", "/static/app/giftcoupon.html");
    	data.put("couponGetTips", "更多优惠券，去领券中心看看");
    	data.put("couponHistoryTips", "查看历史优惠券");
    	data.put("myCoupon", "我的优惠券");
    	data.put("noCoupon", "没有可用优惠券");
    	data.put("directionForUse", "使用说明");
    	
    	
//    	logger.info("=======================确认订单接口返回数据："+JSON.toJSONString(data));
    	return jsonResponse.setSuccessful().setData(data);
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

    	incomeAssembler.assemble(result, userDetail);
        return result;
    }
	
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
	
	public List<ShopCoupon> getAvailableCoupon(long userId,  double prepay ,Map<Long, Integer> productCountMap,  Map<Long, ProductVOShop> productMap ,Set<Long> productIds, List<ShopCoupon> couponList, double totalPostage) {
    	//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌   5：免邮
    	List<ShopCoupon> couponUsableList = new ArrayList<ShopCoupon>();
    	List<ShopCoupon> couponShipFreeList = new ArrayList<ShopCoupon>();
    	
    	for(ShopCoupon coupon : couponList){
    		 if( coupon.getRangeType() == 5 && totalPostage > 0){
    			couponShipFreeList.add(coupon);
    		}
    	}

    	if (orderFacade.HasCouponLimit(true, productIds, productMap, prepay)) return couponShipFreeList;
    		        
        
        
    	for(ShopCoupon coupon : couponList){
    		if( coupon.getRangeType() == 0){
    			//只显示定额优惠券
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(coupon.getMoney());
    			
    		}else if( coupon.getRangeType() == 5 && totalPostage > 0){
    			//只显示定额优惠券
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(totalPostage);
    			
    		}else if(coupon.getRangeType() == 2 && coupon.getLimitMoney() > 0 && coupon.getLimitMoney() <= prepay){
    			
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(coupon.getMoney());
    			if(prepay < coupon.getMoney()){
    				coupon.setActualDiscount(prepay);
    			}
    			
    		}else if(coupon.getRangeType() == 2 && coupon.getLimitMoney() == 0 ){
    			
    			couponUsableList.add(coupon);
    			coupon.setActualDiscount(coupon.getMoney());
    			if(prepay < coupon.getMoney()){
    				coupon.setActualDiscount(prepay);
    			}
    			
    		}else {
    			//只显示定额优惠券
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
							List<ShopCategory> categorieListAll = shopCategoryService.getCategories();
							
							//List<Category> categorieList = categoryService.getCategoriesByIdsArr(idsArr);
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
	/**
	 * 生成订单（3.0已经被新接口替代）
	 * @param skuCountPairArray
	 * @param addressId
	 * @param orderType
	 * @param expressSupplier
	 * @param expressOrderNo
	 * @param phone
	 * @param remark
	 * @param cartIds
	 * @param clientPlatform
	 * @param ip
	 * @param userDetail
	 * @param payCash
	 * @param couponId
	 * @param statisticsIds
	 * @return
	 */
	synchronized public JsonResponse confirmOrder185(String[] skuCountPairArray, int addressId, OrderType orderType,
    		String expressSupplier, String expressOrderNo, String phone, String remark,
    		Long[] cartIds, ClientPlatform clientPlatform, String ip, UserDetail userDetail, double payCash, String couponId, String statisticsIds) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("couponId", couponId);
    	if (orderType == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	} 

    	
    	long userId = userDetail.getId();
    	Address address = userAddressService.getUserAddress(userId, addressId);
    	if (address == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	
    	Map<Long, Integer> skuCountMap = new HashMap<Long, Integer>();
    	for (String skuCountPair : skuCountPairArray) {
    		String[] parts = StringUtils.split(skuCountPair, ":");
    		skuCountMap.put(Long.parseLong(parts[0]), Integer.parseInt(parts[1]));
    	}
    	
    	//限购校验
    	List<ProductSKU> productSkus = productSKUService.getProductSKUs(skuCountMap.keySet());
    	sortProduId(productSkus);
    	Map<Long, Integer> productCountMap = getProductCountMap(skuCountMap, productSkus);
    	List<RestrictProductVO> products = productFacade.getRestrictInfo(userDetail, productCountMap);
    	if(products.size() > 0) {
    		String description = productFacade.restrictDetail(products.get(0));
    		return jsonResponse.setResultCode(ResultCode.ORDER_ERROR_PRODUCT_RESTRICT).setData(products).setError(description);
    	}
    	
    	ShopStoreOrder order = null;
    	ConsumeWrapper consumeWrapper = new ConsumeWrapper();
    	try {
    		order = orderFacade.buildOrder185(userDetail, skuCountMap, consumeWrapper, address, payCash, couponId, clientPlatform.getVersion());
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

    	orderFacade.createOrder17(order, orderType, expressSupplier, expressOrderNo, phone, remark, address,
    			clientPlatform, ip, couponId);
    	
    	
//       data.put("payAmountInCents", order.getPayAmountInCents());
    	//优惠券设置，日志
//    	OrderNew orderNew = orderService.queryOrderNewFromOld(order);
    	//System.out.println("cf:couponId: " + couponId);
    	if(couponId != null && couponId.length() > 0  ) {
    		
//    		List<Coupon> couponList = orderService.getCouponByIdArr(couponId, orderNew.getUserId());
    		String[] cidArr = couponId.split(",");
    		orderService.updateCouponUsed(cidArr , order.getOrderNo());
    		for(String  cid : cidArr){
    			CouponUseLog couponUseLog = new CouponUseLog();
    			
    			couponUseLog.setOrderNo(order.getOrderNo());
    			couponUseLog.setUserId(order.getStoreId());
    			try {
    				couponUseLog.setCouponId(Long.parseLong(cid));
				} catch (Exception e) {
					System.out.println("confirmV185 ERROR,COUPONID:" + couponId);
					couponUseLog.setCouponId(0);
				}
    			couponUseLog.setStatus(0);
    			couponUseLog.setActualDiscount(order.getActualDiscount());
    			couponUseLog.setCreateTime(System.currentTimeMillis());
    			orderService.insertCouponUseLog(couponUseLog);
    		}
    		
    	}
    	

//        data.put("payAmountInCents", consumeWrapper.getCash());
//        data.put("orderNo", order.getOrderNo());
    	data.put("payAmount", order.getTotalPay() + order.getTotalExpressMoney());
    	data.put("orderNoStr", order.getOrderNoStr());
    	data.put("orderNo", order.getOrderNo());
    	
    	data.put("orderNoNew", order.getOrderNo());
    	int  payCents = (int) (order.getTotalPay() + order.getTotalExpressMoney() );
    	data.put("payAmountInCents", payCents);
    	
    	if (cartIds != null && cartIds.length > 0) {
    		shoppingCartService.removeCartItems(userId, CollectionUtil.createSet(cartIds));
    	}
    	
    	return jsonResponse.setSuccessful().setData(data);
    }

	public ShopStoreOrder getUserOrderNewByNo(long userId, String orderNo) {
    	return storeOrderMapper.getUserOrderByNo(userId, orderNo);
    } 

}