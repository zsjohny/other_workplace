/**
 * 
 */
package com.store.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.store.enumerate.OrderStatusEnums;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.Logistics;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.GroundConditionRuleMapper;
import com.jiuyuan.dao.mapper.supplier.GroundUserMapper;
import com.jiuyuan.dao.mapper.supplier.OrderNewLogMapper;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.StoreMapper;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.ProductCategory;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOPostage;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.SupplierCustomer;
import com.jiuyuan.entity.newentity.SupplierCustomerGroup;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.newentity.ground.GroundConstant;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.service.common.GroundBonusGrantFacade;
import com.jiuyuan.service.common.GroundConditionRuleService;
import com.jiuyuan.service.common.ILOWarehouseNewService;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.service.common.ISupplierCustomer;
import com.jiuyuan.service.common.ISupplierCustomerGroupService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.jiuyuan.util.DateUtil;
import com.store.dao.mapper.CouponUseLogMapper;
import com.store.dao.mapper.OrderCouponMapper;
import com.store.dao.mapper.OrderItemMapper;
import com.store.dao.mapper.OrderProductMapper;
import com.store.dao.mapper.StoreOrderMapper;
import com.store.dao.mapper.StoreProductMapper;
import com.store.entity.OrderAfterSaleCountVO;
import com.store.entity.OrderProduct;
import com.store.entity.ProductVOShop;
import com.store.entity.ShopCategory;
import com.store.entity.ShopCoupon;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemVO;
import com.store.enumerate.OrderType;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PostageNotFoundException;
/**
 * @author LWS
 *
 */
@Service
public class OrderService {
	
	private static final Log logger = LogFactory.get();
	
	private static final int NO_WITHDRAW = 1;
	
	@Autowired
	private IUserNewService userNewService;
	
	@Autowired
	private ProductSKUService productSKUService;
	@Autowired
	private IProductNewService productNewService;
	
	
	
	@Autowired
	private OrderCouponMapper orderCouponMapper;
	
	@Autowired
	private StoreOrderMapper storeOrderMapper;
	
	@Autowired
	private ShopCategoryService shopCategoryService;
	
	@Autowired
	private IUserNewService userService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
    
	@Autowired
	private OrderProductMapper orderProductMapper;
	
	@Autowired
	private LOLocationService loLocationService;
	
	@Autowired
	private ILOWarehouseNewService lOWarehouseService;
	
	@Autowired
	private LOPostageService loPostageService;
	
	@Autowired
	private CouponUseLogMapper couponUseLogMapper;
	
//	@Autowired
//	private StoreBusinessMapper storeBusinessMapper;
	
//	@Autowired
//	private FinanceLogMapper financeLogMapper;
	
	@Autowired
	private OrderNewLogMapper orderNewLogMapper;
	
	@Autowired
	private ProductAssembler productAssembler;
	
	@Autowired
	private ProductServiceShop productService;
	
	@Autowired
	private AfterSaleService afterSaleService;
	
	@Autowired
	private StoreProductMapper storeProductMapper;
	
	@Autowired
	private GroundUserMapper groundUserMapper;
	
	@Autowired
	private GroundBonusGrantFacade groundBonusGrantFacade;
	
	@Autowired
	private GroundConditionRuleMapper groundConditionRuleMapper;
	
	@Autowired
	private GroundConditionRuleService groundConditionRuleService;
	@Autowired
	private ISupplierCustomer supplierCustomerService;
	@Autowired
	private ISupplierCustomerGroupService supplierCustomerGroupService;
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
//	@Autowired
//	private StoreBusinessNewMapper storeBusinessNewMapper;
	
	@Autowired
	private StoreMapper storeMapper;
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;
	
	@Autowired
	private RestrictionActivityProductMapper restrictionActivityProductMapper;
	
    public List<ShopCoupon> getCouponByIdArr(String ids, long userId) {
    	return orderCouponMapper.getCouponByIdArr(ids, userId);
    }

	public int getUserOrderCountForFirstDiscount(long userId) {
    	return storeOrderMapper.getUserOrderCountForFirstDiscount(userId);
    }

	//不分页的优惠券列表
    public List<ShopCoupon> getUserOrderCoupon(long userId, OrderCouponStatus status) {
    	return orderCouponMapper.getUserOrderCoupon(userId, status.getIntValue());
    }

	//取优惠券限制文字
	public List<ShopCoupon>  getCouponLimitContentList(List<ShopCoupon> couponList) {
	for(ShopCoupon coupon : couponList){
		List<String> useTipsList = new ArrayList<String>();
		DecimalFormat sal = new DecimalFormat("#.##");//.format(d)
		String useTips = "本券使用说明：\n";
		String useTag = "";
//		if( coupon.getIsLimit() == 1){
//			useTips += ("1、本券不可使用于优惠活动。\n");
//		}else{
//			useTips += ("1、本券可与优惠活动同时使用。\n");
//		}
		String limitContent = "";
		if(coupon.getRangeType() == 0 || coupon.getRangeType() == 5 ){
			useTips += "1、平台所有商品均可使用。";
		}else if( coupon.getRangeType() == 2){
			//本券不限品类，订单总额满100元可以使用。     本券不限品类，不限订单总额。
			if (coupon.getLimitMoney() > 0) {
				limitContent += "1、本券不限品类，订单总额满" + sal.format(coupon.getLimitMoney()) + "元可以使用。";
				useTag = "满" + sal.format(coupon.getLimitMoney()) + "可用";
			}else{
				limitContent += "1、本券不限品类，不限订单总额。";
				//useTag = "满" + limitIds + "可用";
			}
			useTips += limitContent;
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
					if(limitIds.length() > 0 ){
						String[] idsArr = limitIds.split(",");
						if( coupon.getRangeType() == 1){
							useTag = "特定品类可用";
							coupon.setRelatedUrl(Constants.SERVER_URL + "/mobile/mainview/productbycategory186.json?categoryid=" + limitIds);
							List<ShopCategory> categorieList = shopCategoryService.getCategoriesByIdsArr(idsArr);
							if(categorieList != null && categorieList.size() > 0){
								String categoryName = "";
								for(ShopCategory category : categorieList){
									categoryName += category.getCategoryName()+"，";
								}
								categoryName = categoryName.substring(0,categoryName.length()-1);
								limitContent += "2、仅限用于购买" + categoryName + "品类中的商品";
								if(coupon.getLimitMoney() > 0){
									limitContent += "，满" + coupon.getLimitMoney() + "可用。";
								}else{
									limitContent += "。";
								}
							}
						}else if( coupon.getRangeType() == 4){
//							useTag = "特定品牌可用";
//							coupon.setRelatedUrl(Constants.SERVER_URL + "/mobile/mainview/productbycategory186.json?categoryid=40&propGroup=6:" + limitIds);
//							List<Brand> brandList = brandService.getBrandListByArr(idsArr);
//							if(brandList != null && brandList.size() > 0){
//								String brandName = "";
//								for(Brand brand : brandList){
//									brandName += brand.getBrandName()+"，";
//								}
//								brandName = brandName.substring(0,brandName.length()-1);
//								limitContent = "2、仅限用于购买" + brandName + "品牌的商品";
//								if(coupon.getLimitMoney() > 0){
//									limitContent += "，满" + coupon.getLimitMoney() + "可用。";
//								}else{
//									limitContent += "。";
//								}
//						}
					}
	    		}
					useTips += limitContent;
			}
		}
		if(coupon.getCoexist() == 1){
			//本券不可与其他优惠券同时使用。
			useTips += "\n2、本券可以与其他优惠券同时使用。";
		}else {
			useTips += "\n2、本券不可以与其它优惠券同时使用。";
		}
		//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌 ，5:免邮券
		useTipsList.add(useTips);
		useTipsList.add(useTag);
		coupon.setCouponUseTips(useTipsList);
    		
    	}
	return couponList;
}

	@Transactional(rollbackFor = Exception.class)
    public ShopStoreOrder createOrder18(ShopStoreOrder order, String couponId) {
    	if (CollectionUtils.isEmpty(order.getOrderItems())) {
    		String msg = "order items can not empty!";
    		logger.error(msg);
    		throw new IllegalArgumentException(msg);
    	}
    	//double totalCommission = 0;
    	int totalBuyCount = 0;
    	StoreBusiness storeBusiness = userService.getStoreBusinessByStoreId(order.getStoreId());
    	for (ShopStoreOrderItem orderItem : order.getOrderItems()) {
//			double totalPayItem = 0.00;
//            orderItem.setTotalPay(totalPayItem);
    		//totalCommission += storeBusiness.getCommissionPercentage() * orderItem.getTotalPay()/100;
    		totalBuyCount += orderItem.getBuyCount();
    		orderItem.setOrderNo(order.getOrderNo());//插入新订单orderno	
    		orderItem.setTotalAvailableCommission(storeBusiness.getCommissionPercentage() * orderItem.getTotalPay()/100);
    	}
    	//order.setCommission(totalCommission);
    	order.setTotalBuyCount(totalBuyCount);
    	order.setHasWithdrawed(NO_WITHDRAW);
    	storeOrderMapper.insertOrder(order);
    	//插入新订单表
//    	OrderNew orderNew = createNewOrderFromOld(order, order.getOrderItems());
    //	double discount = orderNew.getTotalPay()/orderNew.getTotalMoney();
    	long orderNo = order.getOrderNo();
    	double discount;
    	if(order.getTotalPay() > 0 && order.getTotalMoney() > 0){
    		discount  = order.getTotalPay()/order.getTotalMoney();
    	}else{
    		discount = 0;
    	}
    	for (ShopStoreOrderItem orderItem : order.getOrderItems()) {    
			orderItem.setOrderNo(order.getOrderNo());//插入新订单orderno
			//orderItem.setParentId(order.getOrderNo());//插入新订单orderno
			orderItem.setTotalPay(discount * orderItem.getTotalMoney());
		}
    	if(couponId != null && couponId.length() > 0){
    		calculateItemPrice(order,  order.getOrderItems(),  couponId);
    	}
    	orderItemMapper.insertOrderItems(orderNo, order.getOrderItems());
    	//sync table store_product
//    	if(order.getOrderItems().size() > 0){
//    		storeProductMapper.insertStoreProduct(order.getOrderItems());
//    	}
    	if (order.getOrderProducts() != null && order.getOrderProducts().size() > 0) {
    		for(OrderProduct orderProduct : order.getOrderProducts()){
    			orderProduct.setOrderNo(orderNo);
    		}
//    		System.out.println("order.getOrderProductsSize:"+order.getOrderProducts().size());
    		orderProductMapper.insertOrderProduct(order.getOrderProducts());
		}
//    	if(order.getOrderDiscountLogs().size() > 0) {
//    		for(OrderDiscountLog orderDiscountLog : order.getOrderDiscountLogs()){
//    			orderDiscountLog.setOrderNo(orderNew.getOrderNo());
//    		}
//    		orderDiscountLogMapper.insertOrderDiscountLogs(orderId, order.getOrderDiscountLogs());
//    	}
    	return order;
    }
	
	public void calculateItemPrice(ShopStoreOrder order, List<ShopStoreOrderItem> itemList, String couponId) {
		int needDeal = 0;
		double actualMatchAmount = 0;
		double unmatchAmount = 0;
		double actualDiscountAmount = 0;
		double actualDiscount = 0;
		double unmatchDiscount = 0;
		double allDiscount = 0;
		List<ShopCoupon> couponList = getCouponByIdArr(couponId, order.getStoreId());
		for(ShopCoupon coupon : couponList){
			needDeal = 0;
//		Coupon coupon = this.getCouponById(Long.parseLong(couponId), order.getUserId());
			if(coupon != null && (coupon.getRangeType() == 1 || coupon.getRangeType() == 4)){
				needDeal = 1;
			}
			System.out.println("orderConfirm:  needDeal:" + needDeal);
			if(needDeal == 1 && coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
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
				if(limitIds.length() > 0 ){
					
					String[] idsArr = limitIds.split(",");
					Set<Long> productIds = new HashSet<Long>();
					for (ShopStoreOrderItem orderItem : itemList) {
						productIds.add(orderItem.getProductId());
					}
//					//计算首单优惠金额
//			    	int count = getUserOrderCountForFirstDiscount(order.getUserId());
//			    	double firstDiscountCash = 0;
//			    	if (count == 0 ){
//			    		//首单优惠
//			        	firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
//			    		if(firstDiscountCash < totalCash ){
//			    			totalCash -= firstDiscountCash;
//			    		}else{
//			    			firstDiscountCash = totalCash;
//			    			totalCash = 0.0;
//			    		}
//			    	}else {
//			    		firstDiscountCash = 0.0;
//			    	}
					//子分类匹配
					List<ShopCategory> categorieListAll = new ArrayList<ShopCategory>();
					List<String> idsList = new ArrayList<String>();
					if(coupon.getRangeType() == 1){
						categorieListAll = shopCategoryService.getCategories();
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
								//System.out.println("category.getChildCategories().size():"+category.getChildCategories().size());
								if(ctgrId .equals(category.getId() + "") && category.getParentId() == 0 && category.getChildCategories() != null && category.getChildCategories().size() > 0){
									for(ShopCategory childCtgy : category.getChildCategories()){
										idsList.add(childCtgy.getId() + "");
									}
								}
							}
						}
					}
					if(coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
						//第一次遍历计算实际优惠折扣
						//List<Long> categoryIdList =  productCategoryService.getCategoryIdsAll(productIds);
						List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
						if(productCategoryList != null && productCategoryList.size() > 0 ){	
							String productSKUIdStr = "";
							for(String id : idsList){
								for (ProductCategory productCategory : productCategoryList) {
									if((productCategory.getCategoryId() + "" ).equals(id) ){
										for (ShopStoreOrderItem orderItem : itemList) {
											if(orderItem.getProductId() == productCategory.getProductId() && productSKUIdStr.indexOf("," + orderItem.getSkuId() + ",") == -1){
												actualMatchAmount += orderItem.getTotalMoney();
												productSKUIdStr += "," + orderItem.getSkuId() + ",";
											}
										}
										//productIdStr += "," + productCategory.getProductId() + ","; //
										
									}
									
								}
							}
						}
						
						
						
					}else if(coupon.getRangeType() == 4 && idsArr != null && idsArr.length > 0){	
						for(String arr : idsArr){
							
							for (ShopStoreOrderItem orderItem : itemList) {
								if((orderItem.getBrandId() + "").equals(arr)){	
									actualMatchAmount += orderItem.getTotalMoney();
								}
							}
						}
					}
					actualDiscountAmount = actualMatchAmount;
					if(coupon.getMoney() < actualMatchAmount){
						actualDiscountAmount = coupon.getMoney();
					}
					unmatchAmount = order.getTotalMoney() - actualMatchAmount;
					if((unmatchAmount + (actualMatchAmount - actualDiscountAmount)) > 0){
						unmatchDiscount = (unmatchAmount + (actualMatchAmount - actualDiscountAmount) - (order.getTotalMoney() - order.getTotalPay() - actualDiscountAmount)) /(unmatchAmount + (actualMatchAmount - actualDiscountAmount));
					}else{
						unmatchDiscount = 0;
					}
					System.out.println("actualDiscountAmount:" + actualDiscountAmount + ",actualMatchAmount" + actualMatchAmount  + ",unmatchAmount:" + unmatchAmount);
					allDiscount = (order.getTotalPay() + actualDiscountAmount) / order.getTotalMoney();
					if(allDiscount > 1){
						allDiscount = 1;
					}
					if(actualMatchAmount > 0){
						actualDiscount = (order.getTotalPay() - unmatchAmount * unmatchDiscount)/actualMatchAmount;
					}
					if(actualDiscount < 0){
						actualDiscount = 0;
					}
					if(actualDiscount > 1){
						actualDiscount = 1;
					}
					System.out.println("actualDiscount:" + actualDiscount + ",allDiscount" + allDiscount+ ",unmatchDiscount" + unmatchDiscount);
					//第二次遍历设置实际优惠价格
					if( coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
						for (ShopStoreOrderItem orderItem : itemList) {
							orderItem.setTotalPay(orderItem.getTotalMoney() * unmatchDiscount);	
						}
						List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
						if(productCategoryList != null && productCategoryList.size() > 0 ){	
							String productSKUIdStr = "";
							for(String id : idsList){
								for (ProductCategory productCategory : productCategoryList) {
									if((productCategory.getCategoryId() + "" ).equals(id) ){
										for (ShopStoreOrderItem orderItem : itemList) {
											if(orderItem.getProductId() == productCategory.getProductId()&& productSKUIdStr.indexOf("," + orderItem.getSkuId() + ",") == -1){	
												orderItem.setTotalPay(orderItem.getTotalMoney() * actualDiscount);
												productSKUIdStr += "," + orderItem.getSkuId() + ",";
											}
										}
										//productIdStr += "," + productCategory.getProductId() + ","; //
									}
								}
							}
						}
					}else if(coupon.getRangeType() == 4 && idsArr != null && idsArr.length > 0){
						for (ShopStoreOrderItem orderItem : itemList) {
							orderItem.setTotalPay(orderItem.getTotalMoney() * unmatchDiscount);	
						}
						for(String arr : idsArr){
							for (ShopStoreOrderItem orderItem : itemList) {
								if((orderItem.getBrandId() + "").equals(arr)){	
									orderItem.setTotalPay(orderItem.getTotalMoney() * actualDiscount);
								}
							}
						}
					}
				}
			}
		}
    }

	public void splitOrderNew(String userId,ShopStoreOrder order) {
    	long orderNewNo = 0;
        List<ShopStoreOrderItem>  items = orderItemMapper.getOrderItems(order.getStoreId(), CollectionUtil.createList(order.getOrderNo()));
        if(items != null&&items.size() > 0){
        	orderNewNo = items.get(0).getOrderNo();
        	ShopStoreOrder orderNew = storeOrderMapper.getUserOrderByNoOnly(orderNewNo + "");
        	//int count = orderNewMapper.updateOrderPayStatus(orderNew.getOrderNo(), "1702488579", PaymentType.BANKCARD_PAY, OrderStatus.PAID, OrderStatus.UNPAID, System.currentTimeMillis());
        	splitOrderNewDetail(orderNew , items, 0);
        }	
    }
	
	public void splitOrderNewDetail(ShopStoreOrder orderNew, List<ShopStoreOrderItem> items, double commission) {
		long orderNewNo = orderNew.getOrderNo();
//		母订单id 0:其他  -1:已拆分订单, OrderNo:没有子订单  && parentId != -1
		long parentId = orderNew.getParentId();
		if(parentId == -1) {
			logger.info("如果订单parentId为-1表示不需要拆单，则直接返回，parentId"+parentId);
			return ;
		}else{
			logger.info("如果订单parentId不会为-1进行拆单操作，parentId："+parentId);
		}
		
		
	
		ShopStoreOrder orderTemp = storeOrderMapper.getByOrderNo(orderNewNo);
//		orderTemp.setPaymentNo(orderNew.getPaymentNo());
//		orderTemp.setPaymentType(orderNew.getPaymentType());
		if(orderNew!=null){
			//更新支付状态
			//int count = orderNewMapper.updateOrderPayStatus(orderNew.getOrderNo(), "1702488579", PaymentType.BANKCARD_PAY, OrderStatus.PAID, OrderStatus.UNPAID, System.currentTimeMillis());
			Map<Long , Double > priceMap = new HashMap<Long , Double >();
			Map<Long , Double > marketPriceMap = new HashMap<Long , Double >();
			Map<Long , Integer > coinMap = new HashMap<Long , Integer >();
			Map<Long , Integer > countMap = new HashMap<Long , Integer >();
			Map<Long , Double > itemPayMap = new HashMap<Long , Double >();
			long lOWarehouseId = 0;
			double discount = 0;
			if(orderNew.getTotalMoney() > 0){
				discount = orderNew.getTotalPay() / orderNew.getTotalMoney();
				
			}
			int lOWarehouseIdCount = 0;
			long parentlOWarehouseId = 0;
			//第一个循环统计价格信息和数量信息
			for(ShopStoreOrderItem orderItem : items ){
				parentlOWarehouseId = orderItem.getlOWarehouseId();
				try{
					if(orderItem.getlOWarehouseId() != lOWarehouseId){
						lOWarehouseId = orderItem.getlOWarehouseId();
						lOWarehouseIdCount++;
						priceMap.put(lOWarehouseId , orderItem.getTotalMoney());
						marketPriceMap.put(lOWarehouseId , orderItem.getTotalMarketPrice());
						countMap.put(lOWarehouseId , orderItem.getBuyCount());
						coinMap.put(lOWarehouseId , orderItem.getTotalUnavalCoinUsed());
						itemPayMap.put(lOWarehouseId , orderItem.getTotalPay());
					}	else{
						priceMap.put(lOWarehouseId , priceMap.get(lOWarehouseId) + orderItem.getTotalMoney() );
						marketPriceMap.put(lOWarehouseId , marketPriceMap.get(lOWarehouseId) + orderItem.getTotalMarketPrice() );
						countMap.put(lOWarehouseId , countMap.get(lOWarehouseId) + orderItem.getBuyCount());
						coinMap.put(lOWarehouseId , coinMap.get(lOWarehouseId) + orderItem.getTotalUnavalCoinUsed());
						itemPayMap.put(lOWarehouseId , itemPayMap.get(lOWarehouseId) + orderItem.getTotalPay());
					}
				}catch (Exception e) {
				    e.printStackTrace();
				}
			}
			//目的地为空抛出异常
	    	if(StringUtils.isEmpty(orderNew.getExpressInfo())) {
	    		throw new DeliveryLocationNullException();
	    	}
	    	//获取收货地id信息
	    	//填写的市,数据表中未查到抛出异常
			LOLocation location = loLocationService.getFirstCityByName (orderNew.getExpressInfo());
			if(location == null ) {
				logger.info("根据发货信息获得地址失败，请尽快排查问题！！！！！！！！！！！！！！！！！orderNew.getExpressInfo()："+orderNew.getExpressInfo());
				throw new DeliveryLocationNotFoundException();
			}


	    	int distributionLocationId = location.getId();
	    	LOWarehouse loWarehouse = new LOWarehouse();
			lOWarehouseId = 0;
			

			//需要拆分子订单
			if(lOWarehouseIdCount > 1){
				int i = 0 ;
				double cms = 0;
				double partCms = 0;
				for(ShopStoreOrderItem orderItem : items ){
					i++;
					if(orderItem.getlOWarehouseId() != lOWarehouseId){
						lOWarehouseId = orderItem.getlOWarehouseId();
						orderTemp.setParentId(orderNewNo);
						orderTemp.setTotalMoney(priceMap.get(lOWarehouseId));//item总价
						orderTemp.setTotalPay(itemPayMap.get(lOWarehouseId));//item总折后价
//						orderTemp.setTotalPay(priceMap.get(lOWarehouseId) * discount);//item总折后价
						orderTemp.setCoinUsed(coinMap.get(lOWarehouseId));//子订单总玖币消费
						orderTemp.setlOWarehouseId(lOWarehouseId);
						orderTemp.setTotalMarketPrice(marketPriceMap.get(lOWarehouseId));
						orderTemp.setTotalBuyCount(countMap.get(lOWarehouseId));
						if(orderNew.getTotalPay() > 0){
							partCms = (itemPayMap.get(lOWarehouseId)/ orderNew.getTotalPay()) * commission;
							if(i == items.size()){
								partCms = commission - cms;
							}
							orderTemp.setCommission(partCms);
							cms += partCms;
						}
						loWarehouse = lOWarehouseService.getById(lOWarehouseId);
						//计算运费
						//整单包邮则自订单包邮，可处理免邮券拆单
						if(orderNew.getTotalExpressMoney() == 0) {
	    	    			orderTemp.setTotalExpressMoney(0);
	    	    		}else if(loWarehouse.getIsFree() == Logistics.DISCOUNT.getIntValue() && loWarehouse.getFreeCount() <= countMap.get(lOWarehouseId)) {
	    	    			//如果满足包邮条件
	    	    			orderTemp.setTotalExpressMoney(0);
	    	    		}else{
	    	    			//不满足包邮条件
	    	    			LOPostage loPostage = loPostageService.getPostage(loWarehouse.getDeliveryLocation(), distributionLocationId);
	    	    			if(loPostage == null) {
	    	    				throw new PostageNotFoundException();
	    	    			}
	    	    			orderTemp.setTotalExpressMoney(loPostage.getPostage());//设置子订单运费
	    	    		}
						
						//获取仓库对应的供应商
						UserNew supplier = userNewService.getSupplierByLowarehouseId(lOWarehouseId);//.getSupplierLowarehouse(lowarehouseId)
						if(supplier == null){
							logger.info("根据仓库ID获取对应供应商失败，请尽快排查问题,lOWarehouseId:"+lOWarehouseId);
							throw new RuntimeException("根据仓库ID获取对应供应商失败，请尽快排查问题,lOWarehouseId:"+lOWarehouseId);
						}
						orderTemp.setSupplierId(supplier.getId());
						logger.info("支付回调拆单填充订单供应商ID成功orderTemp.getSupplierId():"+orderTemp.getSupplierId());
//						拆分订单根据仓库ID获取供应商ID并填充到子订单中
						logger.info("拆分订单根据仓库ID获取供应商ID并填充到子订单中,supplier.getId():"+supplier.getId()+"orderNo"+ orderNew.getOrderNo()+",orderTemp:"+JSON.toJSONString(orderTemp));
	    	    		storeOrderMapper.insertOrder(orderTemp);
					}
					//待优化 ，批量更新item 的orderNo
					orderItemMapper.updateOrderNo(orderItem.getId(), orderTemp.getOrderNo(), System.currentTimeMillis());
				}
				storeOrderMapper.updateOrderParentId(orderNewNo , -1 ,0 , System.currentTimeMillis());
				//不需要拆分子订单 默认都不需根据仓库要拆单
			}else {
				storeOrderMapper.updateOrderParentId(orderNew.getOrderNo() , orderNew.getOrderNo(), parentlOWarehouseId , System.currentTimeMillis());
			}
		}else{
			logger.info("订单不能为空，请尽快排查问题！！！！！！！！！！！！！！！！！");
		}
}
//	/**
//	 * 获得仓库对应供应商ID
//	 * @param lOWarehouseId
//	 * @return
//	 */
//	private long getSupplierId(long lOWarehouseId) {
//
//		return 0;
//	}

	public int updateCouponUsed(String[] idArr, long orderNo) {
    	long time = System.currentTimeMillis();
    	
    	if(idArr != null && idArr.length > 0){
    		//System.out.println("cf2:couponId: " + idArr.length);
    		return orderCouponMapper.updateCouponUsed(idArr, orderNo, time, OrderCouponStatus.USED.getIntValue(), OrderCouponStatus.UNUSED.getIntValue() );
    		
    	}else {
    		return 0;
    	}
    }

	public int insertCouponUseLog(CouponUseLog couponUseLog) {
    	return couponUseLogMapper.insertCouponUseLog(couponUseLog);
    }
	
	public int updateCouponUseLog(CouponUseLog couponUseLog) {
    	return couponUseLogMapper.updateCouponUseLog(couponUseLog);
    }

	public ShopStoreOrder getUserOrderNewByNo(String orderNo) {
    	return storeOrderMapper.getUserOrderByNoOnly(orderNo);
    }
	
	public ShopStoreOrder getUserOrderNewByNo(long userId, String orderNo) {
    	return storeOrderMapper.getUserOrderByNo(userId, orderNo);
    }

	@Transactional(rollbackFor = Exception.class)
    public void updateOrderPayStatus(ShopStoreOrder order, String paymentNo, PaymentType paymentType, OrderStatus newStatus,
                                  OrderStatus oldStatus, long time ,String version) {
		
		logger.info("支付成功修改订单状态！updateOrderPayStatus，order："+JSON.toJSONString(order));
    	int count = 0;
        //orderNew操作
        List<ShopStoreOrderItem>  items = orderItemMapper.getOrderNewItems(order.getStoreId(), CollectionUtil.createList(order.getOrderNo()));
        	if(order != null){
        		logger.info("更新支付状态开始，order.getOrderNo()："+order.getOrderNo());
	        	count = storeOrderMapper.updateOrderPayStatus(order.getOrderNo(), paymentNo, paymentType.getIntValue(), newStatus.getIntValue(), oldStatus.getIntValue(), time);
	        	logger.info("更新支付状态结束，count："+count+",order.getOrderNo():"+order.getOrderNo());
	        	
	        	//判断并设置为供应商客户
				boolean needSetCustomer2Supplier = isNeedsetCustomer2Supplier (order);
				if (needSetCustomer2Supplier) {
					setCustomer2Supplier(order);
	        		logger.info("供应商客户设置结束");
				}else {
					logger.info ("不需要绑定供应商与门店用户的客户关系 orderClassify[{}]",order.getClassify ());
				}

	        	//支付成功时发放交易奖金
                logger.info("支付成功时发放交易奖金:开始orderNo："+order.getOrderNo());
                if(order.getParentId()>0){
                	//没有子订单，发放交易奖金
                	long groundUserId = order.getGroundUserId();
                	long storeId = order.getStoreId();
        			long orderNo = order.getOrderNo();
                	if(groundUserId != 0){
                    	grantDealBonuses(groundUserId,storeId,orderNo);
            		}else{
            			logger.info("该门店ID："+storeId+"，没有地推人员，无需发放奖金！");
            		}
        			
                }else{
                	//有子订单
                	//更改子订单为已付款状态
    	        	long parentOrderNo = order.getOrderNo();
    	        	logger.info("更新子订单支付状态开始，parentOrderNo"+parentOrderNo);
    	        	storeOrderMapper.updateOrderPayStatusByParentOrderNo(parentOrderNo, paymentNo, paymentType.getIntValue(), newStatus.getIntValue(), oldStatus.getIntValue(), time);
    	        	logger.info("更新子订单支付状态结束，parentOrderNo"+parentOrderNo);
                	//获取子订单列表
                	List<ShopStoreOrder> shopStoreOrderList = storeOrderMapper.getStoreOrderByParentId(order.getOrderNo());
                	//根据对子订单进行发放奖金
                	for (ShopStoreOrder shopStoreOrder : shopStoreOrderList) {
                			//根据子订单发放交易奖金
                			long groundUserId = shopStoreOrder.getGroundUserId();
                			long storeId = shopStoreOrder.getStoreId();
                			long orderNo = shopStoreOrder.getOrderNo();
                			if(groundUserId != 0){
                				grantDealBonuses(groundUserId,storeId,orderNo);
                			}else{
                				logger.info("该门店ID："+storeId+"，没有地推人员，无需发放奖金！");
                			}
					}
                
                }

                //是否需要拆单
				boolean needSplitOrder = isNeedSplitOrder (order, count);
				if(needSplitOrder){
					logger.info ("订单需要拆单处理");
	        		//拆单
	        		splitOrderNewDetail(order , items, 0);
	        	}
	        	else {
					logger.info ("订单不需要拆单");
				}
	        	OrderNewLog orderNewLog = new OrderNewLog();
	        	orderNewLog.setStoreId(order.getStoreId());
	        	orderNewLog.setOrderNo(order.getOrderNo());
	        	orderNewLog.setNewStatus(newStatus.getIntValue());
	        	orderNewLog.setOldStatus(oldStatus.getIntValue());
	        	orderNewLog.setCreateTime(time);
	        	orderNewLogMapper.addOrderLog(orderNewLog);
        	}
    }

	/**
	 * 是否需要拆单
	 *
	 * @param order order
	 * @param count count
	 * @return boolean
	 * @author Charlie
	 * @date 2018/8/17 15:12
	 */
	private boolean isNeedSplitOrder(ShopStoreOrder order, int count) {
		if (order.getClassify () == 2) {
			//会员订单不需要拆单
			return false;
		}
		if (count == 1 && order.getOrderType () == OrderType.NORMAL.getIntValue ()) {
			//原来的逻辑
			return true;
		}
		return false;
	}

	/**
	 * 是否需要绑定门店和供应商的客户关系
	 *
	 * @param order order
	 * @return boolean
	 * @author Charlie
	 * @date 2018/8/17 15:05
	 */
	private boolean isNeedsetCustomer2Supplier(ShopStoreOrder order) {
		if (order.getClassify () == 2) {
			//购买会员不需要配置
			return false;
		}
		return true;
	}

	/**
	 * 设置为供应商客户
	 * @param order
	 */
	private void setCustomer2Supplier(ShopStoreOrder order) {
		logger.info("判断用户是不是供应商客户可开始");
    	long storeId1 = order.getStoreId();
    	long supplierId = order.getSupplierId();
    	StoreBusiness storeBusiness = storeBusinessNewService.getById(storeId1);
//    	SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByStoreId(storeId1 , supplierId);
    	String phoneNumber = storeBusiness.getPhoneNumber();
    	SupplierCustomer supplierCustomer = supplierCustomerService.getCustomerByPhone(phoneNumber, supplierId);
    	if (supplierCustomer ==null ) {
    		supplierCustomer = new SupplierCustomer();
    		supplierCustomer.setSupplierId(supplierId);
    		supplierCustomer.setBusinessAddress(storeBusiness.getBusinessAddress());
    		supplierCustomer.setCity(storeBusiness.getCity());
    		supplierCustomer.setStoreId(storeId1);
    		supplierCustomer.setProvince(storeBusiness.getProvince());
    		supplierCustomer.setCustomerName(storeBusiness.getLegalPerson());
    		supplierCustomer.setBusinessName(storeBusiness.getBusinessName());
    		supplierCustomer.setPhoneNumber(phoneNumber);
    		supplierCustomer.setCreateTime(System.currentTimeMillis());
    		supplierCustomer.setUpdateTime(System.currentTimeMillis());
    		supplierCustomer.setCustomerType(1);//状态 0：新客户 1：老客户
    		SupplierCustomerGroup  supplierCustomerGroup = supplierCustomerGroupService.getDefaultGroup(supplierId);
    		if (supplierCustomerGroup != null ) {
    			supplierCustomer.setGroupId(supplierCustomerGroup.getId());
    			logger.info("供应商添加客户，设置默认分组");
    			supplierCustomerService.insert(supplierCustomer);
			}else{
				logger.info("用户第一次付款成功时，设置为供应商客户时失败！该供应商没有默认分组！supplierId："+supplierId);
			}
		}else{
			SupplierCustomer newSupplierCustomer = new SupplierCustomer();
			if(supplierCustomer.getStoreId() == null || 
			   supplierCustomer.getStoreId() == 0		){//如果該客戶是該供應商的新客戶
				newSupplierCustomer.setId(supplierCustomer.getId());
				newSupplierCustomer.setBusinessAddress(storeBusiness.getBusinessAddress());
				newSupplierCustomer.setCity(storeBusiness.getCity());
				newSupplierCustomer.setProvince(storeBusiness.getProvince());
				newSupplierCustomer.setCustomerName(storeBusiness.getLegalPerson());
				newSupplierCustomer.setBusinessName(storeBusiness.getBusinessName());
				newSupplierCustomer.setUpdateTime(System.currentTimeMillis());
				newSupplierCustomer.setStoreId(storeId1);
				newSupplierCustomer.setCustomerType(1);//状态 0：新客户 1：老客户
				logger.info("supplierCustomerGroup:获取供应商分组");
				SupplierCustomerGroup  supplierCustomerGroup = supplierCustomerGroupService.getDefaultGroup(supplierId);
				if (supplierCustomerGroup != null ) {
					supplierCustomer.setGroupId(supplierCustomerGroup.getId());
					logger.info("添加新客户！");
					supplierCustomerService.update(newSupplierCustomer);
				}else{
					logger.info("用户第一次付款成功时，设置为供应商客户时失败！该供应商没有默认分组！supplierId："+supplierId);
				}
			}
			
		}
		
	}

	public int getUserNewOrderCount(long userId, int orderStatus) {
    	return storeOrderMapper.getUserOrderCount(userId, orderStatus);
    }

	public List<ShopStoreOrder> getUserOrdersNew(long userId, int orderStatus, PageQuery pageQuery) {
    	return storeOrderMapper.getUserOrders(userId, orderStatus, pageQuery);
    }

	public List<ShopStoreOrderItem> getOrderNewItems(long userId, Collection<Long> orderNOs) {
    	return  orderItemMapper.getOrderNewItems(userId, orderNOs);
    }

	public List<ShopStoreOrder> getChildOrderList(long userId, Collection<Long> orderNOs) {
    	return storeOrderMapper.getChildOrderList(userId, orderNOs);
    }

	public List<ShopStoreOrderItemVO> getOrderNewItemsVO(UserDetail userDetail, Collection<Long> orderNOs) {
    	List<ShopStoreOrderItemVO> composites = new ArrayList<ShopStoreOrderItemVO>();
    	
    	List<ShopStoreOrderItem> itemList = orderItemMapper.getOrderNewItems(userDetail.getId(), orderNOs);
    	for (ShopStoreOrderItem orderItem : itemList) {
    		ShopStoreOrderItemVO vo = new ShopStoreOrderItemVO();
    		BeanUtil.copyProperties(vo, orderItem);
    		
//			平台商品状态:0已上架、1已下架、2已删除
    		String platformProductState = productNewService.getPlatformProductState(orderItem.getProductId());
    		vo.setPlatformProductState(platformProductState);
    		
    		composites.add(vo);
    	}
    	productAssembler.assemble(composites, userDetail);
    	return composites;
    }

	public List<OrderProduct> getOrderProductsByOrderNoBatch(long storeId, Collection<Long> orderNOs) {
    	return orderProductMapper.getOrderProductsByOrderNoBatch(storeId, orderNOs);
    }

	public List<Product> getProductsByOrderNoBatch(long storeId, Collection<Long> orderNOs) {
    	return orderProductMapper.getProductsByOrderNoBatch(storeId, orderNOs);
    }

	public int getUserNewOrderCountWithoutParent(long userId, int orderStatus) {
    	return storeOrderMapper.getUserOrderCountWithoutParent(userId, orderStatus);
    }


	public List<ShopStoreOrder> getUserOrdersNewWithoutParent(long userId, int orderStatus, PageQuery pageQuery) {
//    	if(orderStatus == OrderStatus.DELIVER) {
//    		return orderNewMapper.getUserDeliverOrders(userId, pageQuery);
//    	}
    	return storeOrderMapper.getUserOrdersWithoutParent(userId, orderStatus, pageQuery);
    }



	/**
	 * 通过关键字,和订单状态, 过滤查询用户订单总数
	 * @param userId
	 * @param keyword 过滤关键字
	 * @return int
	 * @auther Charlie(唐静)
	 * @date 2018/5/29 16:30
	 */
	public int countByKeyword(long userId, String keyword) {
		return storeOrderMapper.countByKeyword(userId, keyword);
	}



	/**
	 * 根据订单状态,和关键字, 过滤查询用户的订单
	 * @param userId 用户id
	 * @param pageQuery 分页参数
	 * @param keyword 查询的关键字
	 * @return java.util.List<com.store.entity.ShopStoreOrder>
	 * @auther Charlie(唐静)
	 * @date 2018/5/29 16:10
	 */
	public List<ShopStoreOrder> findByKeyword(long userId, PageQuery pageQuery, String keyword) {
		return storeOrderMapper.findByKeyword(userId, keyword, pageQuery);
	}



	public ShopStoreOrder getUserOrderNewDetailByNo(UserDetail userDetail, String orderNo) {
		ShopStoreOrder order = storeOrderMapper.getUserOrderByNo(userDetail.getId(), orderNo);

    	//订单商品树形结构
 		List<ProductVOShop> tempList;
    	List<ProductVOShop> productList = productService.getProductMapByOrderNo(orderNo);
    	List<OrderProduct> orderProductList = getOrderProductsByOrderNoBatch(userDetail.getId(), CollectionUtil.createSet(Long.parseLong(orderNo)) );

    	 //取对应item列表
    	 List<ShopStoreOrderItem>  items = orderItemMapper.getOrderNewItems(order.getStoreId(), CollectionUtil.createList(order.getOrderNo()));
    	 Map<Long, List<ShopStoreOrderItemVO>> result = new HashMap<Long, List<ShopStoreOrderItemVO>>();
    	 List<List<ShopStoreOrderItemVO>> resultList = new ArrayList<List<ShopStoreOrderItemVO>>();
    	 List<ShopStoreOrder> resultOrderList = new ArrayList<ShopStoreOrder>();
    	 List<ShopStoreOrderItemVO> orderItems = new ArrayList<ShopStoreOrderItemVO>();
    	 
    	 for (ShopStoreOrderItem orderItem : items) {
    		ShopStoreOrderItemVO vo = new ShopStoreOrderItemVO();
     		BeanUtil.copyProperties(vo, orderItem);
    		
     		//平台商品状态:0已上架、1已下架、2已删除
    		String platformProductState = productNewService.getPlatformProductState(orderItem.getProductId());
    		//如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
	    	long restrictionActivityProductId = order.getRestriction_activity_product_id();
	    	if(restrictionActivityProductId>0){
	    		RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
	    		int productStatus = restrictionActivityProduct.getProductStatus();
	    		//限购活动商品的状态0:待上架;1:已上架;2:已下架;3:已删除
	    		//平台商品状态:0已上架、1已下架、2已删除
				switch (productStatus) {
				case 1:
					platformProductState = "0";
					break;
				case 2:
					platformProductState = "1";
					break;
				case 3:
					platformProductState = "2";
					break;
				}
	    	}
	    	vo.setPlatformProductState(platformProductState);
     		
     		long wareHouseId = orderItem.getlOWarehouseId();
    		List<ShopStoreOrderItemVO> list = result.get(wareHouseId);
    		if (list == null) {
    			list = new ArrayList<ShopStoreOrderItemVO>();
    			result.put(wareHouseId, list);
    			resultList.add(list);
    		}
    		
//    		//分别获取订单sku状态
//    		Map<String,String> refundOrderMap = this.getOrderItemStatus(orderItem.getId(),order.getOrderStatus());
//    		String orderItemStatus = refundOrderMap.get("orderItemStatus");
//    		if("售后中".equals(orderItemStatus)){
//    			disableConfirmationReceipt = true;
//    		}
//    		vo.setOrderItemStatus(orderItemStatus);
//    		vo.setRefundOrderNo(refundOrderMap.get("refundOrderNo"));
//    		if("申请退款".equals(orderItemStatus) || "申请售后".equals(orderItemStatus)){
//    			//有售后按钮
//    			vo.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.applyAfterSaleButton);
//    		}else{
//    			//无售后按钮
//    			vo.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.unApplyAfterSaleButton);
//    		} 
    		
    		list.add(vo);
    		orderItems.add(vo);
     	}
    	 
    	//是否启用确认收货按钮
    	boolean disableConfirmationReceipt = false;
    	 
    	//分别获取订单状态
 		Map<String,String> refundOrderMap = this.getRefundOrderStatus(order.getOrderNo(),order.getOrderStatus());
 		String refundOrderStatus = refundOrderMap.get("refundOrderStatus");
 		if("售后中".equals(refundOrderStatus)){
 			disableConfirmationReceipt = true;
 		}
 		
 		order.setOrderItemStatus(refundOrderStatus);
 		order.setRefundOrderId(refundOrderMap.get("refundOrderId"));
 		if("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus)){
 			//有售后按钮
 			order.setIsApplyAfterSaleButton(ShopStoreOrder.applyAfterSaleButton);
 		}else{
 			//无售后按钮
 			order.setIsApplyAfterSaleButton(ShopStoreOrder.unApplyAfterSaleButton);
 		}
    		
    	//设置是否启用确认收货按钮
    	order.setDisableConfirmationReceipt(disableConfirmationReceipt);
    	
     	productAssembler.assemble(orderItems, userDetail);
     	
     	//订单详情新结构chen
     	if(productList != null && productList.size() > 0){

			for(ProductVOShop productTemp : productList){
				for(OrderProduct orderProduct : orderProductList){
					if(orderProduct.getProductId() == productTemp.getId()){
						productTemp.setIncome(orderProduct.getCommission());
					}
				}
//				productTemp.assemble(percent);
				
				
				for(ShopStoreOrderItemVO itemVO : orderItems){
					if(productTemp.getOrderItemVOList() == null){
						productTemp.setOrderItemVOList(new ArrayList<ShopStoreOrderItemVO>());
						productTemp.setColorList(new ArrayList<String>());
						productTemp.setSizeList(new ArrayList<String>());
						productTemp.setColorMap(new HashMap<String, String>());
						productTemp.setSizeMap(new HashMap<String, String>());
					}
					if(itemVO.getProductId() == productTemp.getId()){
						productTemp.getOrderItemVOList().add(itemVO);
						String skuSnapshot = itemVO.getSkuSnapshot();
//						logger.info("sku信息，skuSnapshot:"+skuSnapshot+","+JSON.toJSONString(itemVO));
						if(skuSnapshot != null && skuSnapshot.length() > 0){
							String[] parts = skuSnapshot.split("  ");
//							logger.info("sku信息，parts:"+JSON.toJSONString(parts));
							if(!productTemp.getColorMap().containsKey(parts[0].split(":")[1].trim())){
								
								productTemp.getColorList().add(parts[0].split(":")[1].trim());
							}
							if(!productTemp.getSizeMap().containsKey(parts[1].split(":")[1].trim())){
								productTemp.getSizeList().add(parts[1].split(":")[1].trim());
								
							}
							productTemp.getColorMap().put(parts[0].split(":")[1].trim(), parts[0].split(":")[1].trim());
							productTemp.getSizeMap().put(parts[1].split(":")[1].trim(), parts[1].split(":")[1].trim());
							
						}
						productTemp.setCount( productTemp.getCount() + itemVO.getBuyCount());
					}
				}
			}
			
			order.setProductList(productList);
			}
     	
    	//order.setOrderItemMap(result);
     	
//     	List<ProductVO> productList;

     	//未付款订单预拆分
     	if(OrderStatus.getNameByValue(order.getOrderStatus()) != null && order.getOrderStatus()==(OrderStatus.UNPAID.getIntValue())){
     		
     		//订单详情新结构chen productMaps
     		Map<Long, List<ProductVOShop>> productMaps = new HashMap<Long, List<ProductVOShop>>() ;
        	for(ProductVOShop productVO :  productList){
        		if(productMaps.get(productVO.getlOWarehouseId()) == null){
        			tempList = new ArrayList<ProductVOShop>();
        			tempList.add(productVO);
        			productMaps.put(productVO.getlOWarehouseId(), tempList);
        		}else{
        			productMaps.get(productVO.getlOWarehouseId()).add(productVO);
        		}
        	}
     		
     		long wareHouseId = 0;
     		int itemCount = 0;
     		List<ShopStoreOrderItemVO> itemList = new ArrayList<ShopStoreOrderItemVO>();
     		ShopStoreOrder childOrder ;
     		List<ProductVOShop> productTempList ;
     		LOWarehouse loWarehouse = new LOWarehouse();
     		//获取收货地id信息
     		//填写的市,数据表中未查到抛出异常
			LOLocation location = loLocationService.getFirstCityByName (order.getExpressInfo ());
			if (location == null) {
				logger.info("根据发货信息获得地址失败，请尽快排查问题！！！！！！！！！！！！！！！！！order.getExpressInfo()："+order.getExpressInfo());
				throw new DeliveryLocationNotFoundException();
			}

     		int distributionLocationId = location.getId();
     		for(Map.Entry<Long, List<ShopStoreOrderItemVO>>  itemListMap : result.entrySet()){
     			childOrder =  new ShopStoreOrder();
     			wareHouseId = itemListMap.getKey();
     			itemList = itemListMap.getValue();
     			for(ShopStoreOrderItemVO itemVO : itemList){
     				itemCount += itemVO.getBuyCount();
     				
     				long productId = itemVO.getProductId();
     				itemVO.setPlatformProductState( productNewService.getPlatformProductState(productId));
     			}
     			productTempList = productMaps.get(itemListMap.getKey());
     			if(productTempList != null && productTempList.size() > 0){
//     				for(ProductVO productTemp : productTempList){
//     					for(OrderItemVO itemVO : itemList){
//     						if(productTemp.getOrderItemVOList() == null){
//     							productTemp.setOrderItemVOList(new ArrayList<OrderItemVO>());
//     						}
//     						if(itemVO.getProductId() == productTemp.getId()){
//     							productTemp.getOrderItemVOList().add(itemVO);
//     						}
//     					}
//     				}
     				childOrder.setProductList(productTempList);
     			}
     			
     			childOrder.setlOWarehouseId(wareHouseId);
     			childOrder.setOrderItems(new ArrayList<ShopStoreOrderItem>(itemList));
     			
     			
     			//计算运费
     			//如果满足包邮条件
     			loWarehouse = lOWarehouseService.getById(wareHouseId);
     			if(loWarehouse == null){
     				childOrder.setTotalExpressMoney(0);
     			}else if(loWarehouse.getIsFree() == Logistics.DISCOUNT.getIntValue() && loWarehouse.getFreeCount() <= itemCount) {
	     			childOrder.setTotalExpressMoney(0);
	     		}else{
	     				//不满足包邮条件
	     				LOPostage loPostage = loPostageService.getPostage(loWarehouse.getDeliveryLocation(), distributionLocationId);
	     				if(loPostage == null) {
	     					loPostage = new LOPostage();
	     					loPostage.setPostage(0);
	     				}
	     				childOrder.setTotalExpressMoney(loPostage.getPostage());//设置子订单运费
	     		}
     			
     			resultOrderList.add(childOrder);
     		}
     		order.setChildOrderList(resultOrderList);
     	}
    	//order.setOrderItemSplitList(resultList);
     	order.setOrderItems(new ArrayList<ShopStoreOrderItem>(orderItems));
     	
    	return order;
    }

	/**
	 * 分别获取订单sku状态
	 * @return
	 */
	public Map<String,String> getRefundOrderStatus(long orderNo,int orderStatu) {
		Map<String,String> refundOrderMap = new HashMap<String,String>();
		Wrapper<RefundOrder> wrapper = new EntityWrapper<RefundOrder>().eq("order_no", orderNo).orderBy("apply_time", false);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
		if(refundOrderList.size()<1 || refundOrderList.get(refundOrderList.size()-1) == null){
			if(orderStatu == 10){
				//1、申请退款：订单已付款(10) && （无进行中的售后单）
				refundOrderMap.put("refundOrderStatus", "申请退款");
				refundOrderMap.put("refundOrderId", "");
				return refundOrderMap;
			}else if(orderStatu == 50){
				//2、申请售后：订单已发货(50) && （无进行中的售后单）
				refundOrderMap.put("refundOrderStatus", "申请售后");
				refundOrderMap.put("refundOrderId", "");
				return refundOrderMap;
			}else{
				//不能发起售后单不显示任何信息正常
				refundOrderMap.put("refundOrderStatus", "");
				refundOrderMap.put("refundOrderId", "");
				return refundOrderMap;
			}
		}else{
			//获取最后一条售后单状态
			RefundOrder refundOrder = refundOrderList.get(0);
			int refundStatus = refundOrder.getRefundStatus();
			int platformInterveneState = refundOrder.getPlatformInterveneState();
			if(refundStatus == 1 || refundStatus == 2 || refundStatus == 3 || platformInterveneState==1 || platformInterveneState==2){
				if(orderStatu == 10 || orderStatu == 50){
					//3、售后中：（订单已付款(10)  ||  订单已发货(50)）&&（有进行的售后单） 
					refundOrderMap.put("refundOrderStatus", "售后中");
					refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
					return refundOrderMap;
				}else{
//					0未付款、70交易成功、100交易关闭
//					？？？
//					0（无此情况）
//					70：业务控制，确认收货前必须控制无进行中的售后订单
//					100：
//					101买家主动取消订单（无此情况）
//					102超时未付款系统自动关闭订单（无此情况）
//					103全部退款关闭订单（无此情况）
//					104卖家关闭订单（？？？？？？？？？？？？？？？？？？？？？？）
//					105平台客服关闭订单（？？？？？？？？？？？？？？？？？？？？）
					refundOrderMap.put("refundOrderStatus", "");
					refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
					return refundOrderMap;
				}
			}else if(refundStatus == 4){
				//4(退款成功)
				//0.6、已退款：（退款成功）
				refundOrderMap.put("refundOrderStatus", "已退款");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else if(refundStatus == 5){
				//5(卖家拒绝售后关闭)、
				//0、卖家拒绝：（卖家拒绝售后关闭）
				refundOrderMap.put("refundOrderStatus", "卖家拒绝");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else if(refundStatus == 6){
				//6（买家超时未发货自动关闭）、
				//4、已失效：（买家超时未发货自动关闭）
				refundOrderMap.put("refundOrderStatus", "超时失效");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else if(refundStatus == 7){
				//7(买家主动关闭)、
				//7、已撤销：(买家主动关闭)
				refundOrderMap.put("refundOrderStatus", "已撤销");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else if(refundStatus == 8){
				//8、（平台客服主动关闭）
				//平台客服主动关闭
				refundOrderMap.put("refundOrderStatus", "平台关闭");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else if(refundStatus == 9){
				//9(卖家同意后买家主动关闭)、
				//9、已撤销：(买家主动关闭)
				refundOrderMap.put("refundOrderStatus", "已撤销");
				refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
				return refundOrderMap;
			}else{
                for (OrderStatusEnums orderStatusEnums:OrderStatusEnums.values()){
				if (orderStatusEnums.getCode()==refundStatus){
					refundOrderMap.put("refundOrderStatus",orderStatusEnums.getValue());
					refundOrderMap.put("refundOrderId", refundOrder.getId()+"");
					return refundOrderMap;
				}
			}
			}
//
//			if(StringUtils.isEmpty(refundOrderMap.get("refundOrderStatus"))){
//				logger.info("未知售后订单状态,请尽快处理");
//				throw new RuntimeException("未知售后订单状态");
//			}
		}
		return null;
	}

	public  OrderNewLog selectOrderLogByOrderNoAndStatus( long orderNo, OrderStatus orderStatus){
    	return orderNewLogMapper.selectOrderLogByOrderNoAndStatus(orderNo, orderStatus.getIntValue());
    }

	@Transactional(rollbackFor = Exception.class)
    public void updateOrderNewStatus(ShopStoreOrder order, OrderStatus newStatus, OrderStatus oldStatus, long time) {
		logger.info("updateOrderNewStatus:"+order.getOrderNo()+newStatus.getIntValue()+oldStatus.getIntValue()+time);
		int today = DateUtil.getToday();
    	int count = storeOrderMapper.updateOrderStatus(order.getOrderNo(), newStatus.getIntValue(), oldStatus.getIntValue(), time,today);
    	logger.info("updateOrderNewStatus:"+count);
    	if (count != 1) {
    		String msg = "udpate order status error!, order id:" + order.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}
    	
    	//检测该订单是否在售后进行，提示
    	Wrapper<RefundOrder> wrapper = 
				new EntityWrapper<RefundOrder>().eq("order_no",order.getOrderNo()).and(" (refund_status <= 3 or platform_intervene_state in (1,2))", null);//.le("refund_status", 3);
		List<RefundOrder> refundOrderList = refundOrderMapper.selectList(wrapper);
		if(refundOrderList.size()>0){
			return;
		}
		//TODO 重新启动自动确认收货时长
		
    	//更新门店库存
    	if(newStatus == OrderStatus.SUCCESS){

        	logger.info("updateOrderNewStatus:"+1);
    		List<ShopStoreOrderItem> storeOrderItems = this.getOrderNewItemsByOrderNO(order.getStoreId(), order.getOrderNo());
        	logger.info("updateOrderNewStatus:"+2);
    		int afterSaleCount = afterSaleService.getOrderAfterSaleCount(order.getStoreId(), order.getOrderNo());
        	logger.info("updateOrderNewStatus:"+3);
    		if(afterSaleCount > 0){
    			logger.info("updateOrderNewStatus:"+4);
    			Map<Long, OrderAfterSaleCountVO> afterSaleMap = afterSaleService.getOrderAfterSaleMap(order.getStoreId(), order.getOrderNo());
    			logger.info("updateOrderNewStatus:"+5);
    			for(ShopStoreOrderItem storeOrderItem : storeOrderItems){
    				if(afterSaleMap.containsKey(storeOrderItem.getId())){
    					storeOrderItem.setBuyCount(storeOrderItem.getBuyCount() - afterSaleMap.get(storeOrderItem.getId()).getNum());
    				}
    				
    			}
    			
    		}
    		logger.info("updateOrderNewStatus:"+6+":"+storeOrderItems);
    		if(storeOrderItems != null && storeOrderItems.size() > 0){
    			storeProductMapper.insertStoreProduct(storeOrderItems);
    			logger.info("updateOrderNewStatus:"+7);
    		}
    		
    		//门店订单确认收货时发放个人门店激活奖金和团队激活奖金和修改个人门店订单交易奖金和团队订单交易奖金时间
    		grantBonuses(order.getOrderNo(),order.getStoreId(),order.getGroundUserId());
    		    		
    	}
    	
    	// 添加待评价商品 add by dongzhong 20160530
//    	if (newStatus == OrderStatus.SUCCESS && oldStatus == OrderStatus.DELIVER) {
//    		Set<Long> ids = new HashSet<Long>();
//    		ids.add(order.getOrderNo());
//    		List<StoreOrderItem> orderItems = orderItemMapper.getOrderNewItems(order.getStoreId(), ids);
//    		commentMapper.addComments(time, orderItems);        
//    	}

    	logger.info("updateOrderNewStatus:"+8);
    	OrderNewLog orderNewLog = new OrderNewLog();
    	orderNewLog.setStoreId(order.getStoreId());
    	orderNewLog.setOrderNo(order.getOrderNo());
    	orderNewLog.setNewStatus(newStatus.getIntValue());
    	orderNewLog.setOldStatus(oldStatus.getIntValue());
    	orderNewLog.setCreateTime(time);
    	logger.info("updateOrderNewStatus:"+9+":"+orderNewLog.toString());
    	orderNewLogMapper.addOrderLog(orderNewLog);
    	logger.info("updateOrderNewStatus:"+10);
    }
	
	/**
	 * 门店订单确认收货时发放个人门店激活奖金和团队激活奖金和修改个人门店订单交易奖金和团队订单交易奖金时间
	 * @param
	 */
	private void grantBonuses(long orderNo,long storeId,Long groundUserId){
		if(groundUserId == null || groundUserId == 0){
			logger.info("该订单没有订单人员，无需发放奖金");
			return ;
		}
		//1、准备数据
		StoreBusiness storeBusinessNew = storeMapper.selectById(storeId);
		
		//2、修改交易奖金可提现时间（修改个人门店订单交易奖金和团队订单交易奖金时间）
		groundBonusGrantFacade.updateDealBonusAllowGetOutTime(orderNo,storeId);
		
		//3、发放激活奖金
		//3.1判断是否需要发放激活奖金（用户未激活且在123阶段则进行发放激活奖金）
		if(storeBusinessNew.getActivationTime()==0){
			//3.2、获取激活条件金额
			double limitMoney = groundConditionRuleService.getActiveRuleCost();
			logger.info("发放奖金limitMoney:"+limitMoney);
			//3.3、获取用户累计订单商品实付总计金额
			double accumilateSum = storeOrderMapper.getAllOrderAccumulatedSum(storeId);
			//获取用户累计订单商品退款金额
			double totalRefundCostSum = storeOrderMapper.getAllOrderTotalRefundCostSum(storeId);
			if((accumilateSum-totalRefundCostSum) >= limitMoney){
				//3.4、发放激活奖金
				groundBonusGrantFacade.grantActivateBonus(groundUserId,storeId,orderNo);
				logger.info("发放激活奖金完成");
				//3.5、修改门店激活状态
				StoreBusiness store = new  StoreBusiness();
				store.setId(storeId);
				store.setActivationTime(System.currentTimeMillis());
				storeMapper.updateById(store);
				logger.info("修改门店激活时间完成");
			}else{
				logger.info("该商家订单商品实付总计金额没有达到激活条件");
			}
		}else{
			logger.info("不需要发放激活奖金，该用户已经激活或未其他阶段");
		}
	}
	
//	public static void main(String[] args){
//		String content = "{\"name\":\"订单实付金额不低于\",\"type\":3,\"isOpen\":1,\"limitValue\":0.01}";
//		JSONObject parseObject = JSON.parseObject(content);
//		System.out.println(parseObject.toString());
//	}
	
//	/**
//	 * 门店订单确认收货时发放个人门店激活奖金和团队激活奖金
//	 * @param order
//	 */
//	private void grantBonuses(ShopStoreOrder order,int type) {
//		//1.确认收货获取地推人员id和上级ids
//		Long groundUserId = order.getGroundUserId();
//		if(groundUserId!=null && groundUserId>0){
//			//2.获取对应的地推人员信息
//			//3.获取对应的激活奖金和交易奖金发放规则
//			//4.发放奖金
//			Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>().eq("id", groundUserId);
//			List<GroundUser> groundUserList = groundUserMapper.selectList(wrapper);
//			if(groundUserList.size()>0){
//				GroundUser groundUserById = groundUserList.get(0);
//				groundBonusGrantFacade.grantBonus(groundUserById, type, order.getStoreId(), 
//						order.getOrderNo(), "");
//			}
////			List<Long> groundUserIdList = new ArrayList<Long>();
////			String superIds = order.getSuperIds();
////			if(!StringUtils.isEmpty(superIds)){
////				String[] split = superIds.split(",");
////				for (String groundSuperUserId : split) {
////					if(!StringUtils.isEmpty(groundSuperUserId)){
////						groundUserIdList.add(Long.parseLong(groundSuperUserId));
////					}
////				}
////			}
////			Wrapper<GroundUser> wrapper = new EntityWrapper<GroundUser>().in("Id", groundUserIdList);
////			List<GroundUser> groundUserList = groundUserMapper.selectList(wrapper);
////			groundUserList.add(groundUserById);
//			
//		}
//	}

	public List<ShopStoreOrderItem> getOrderNewItemsByOrderNO(long storeId, long orderNo) {
    	return orderItemMapper.getOrderNewItemsByOrderNO(storeId, orderNo);
    }

	@Transactional(rollbackFor = Exception.class)
    public void cancelOrderNew(ShopStoreOrder order, long time) {
    	int count = storeOrderMapper.cancelOrder(order.getOrderNo(), time,  order.getCancelReason());
    	if (count != 1) {
    		String msg = "cancel order error!, order id:" + order.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}
//    	orderItemGroupMapper.cancelOrder(order.getId(), time);
    }

	public List<ShopCoupon> getUserCouponListByOrderNo(long orderNo) {
    	return orderCouponMapper.getUserCouponListByOrderNo(orderNo);
    }

	public int updateCouponUnuse(long orderNo) {
    	long time = System.currentTimeMillis();
    	return orderCouponMapper.updateCouponUnuse(orderNo, time, OrderCouponStatus.UNUSED.getIntValue(), OrderCouponStatus.USED.getIntValue() );
    }

	public List<ShopCoupon> getAllMemberCouponListByOrderNo(long orderNo) {
    	return orderCouponMapper.getAllMemberCouponListByOrderNo(orderNo);
    }

	public ShopStoreOrderItem getOrderNewItemById(long userId, long orderItemId) {
    	return orderItemMapper.getOrderItemById(userId, orderItemId);
    }
	
	/**
	 * 获取订单的数量
	 * @param storeId
	 * @return
	 */
	public Map<String,String> getUserOrderCount(Long storeId){
		//未付款订单数量
//        int unpaidCount = storeOrderMapper.getUserOrderCount(storeId, OrderStatus.UNPAID.getIntValue());
		int unpaidCount = storeOrderMapper.getUserOrderCountWithoutParent(storeId, OrderStatus.UNPAID.getIntValue());
        //待发货订单数量
//        int paidCount = storeOrderMapper.getUserOrderCount(storeId, OrderStatus.PAID.getIntValue());
        int paidCount = storeOrderMapper.getUserOrderCountWithoutParent(storeId, OrderStatus.PAID.getIntValue());
        //待收货订单数量
//        int deliverCount = storeOrderMapper.getUserOrderCount(storeId, OrderStatus.DELIVER.getIntValue());
        int deliverCount = storeOrderMapper.getUserOrderCountWithoutParent(storeId, OrderStatus.DELIVER.getIntValue());
        Map<String,String> orderCount = new HashMap<String,String>();
        orderCount.put("unpaidCount", unpaidCount+"");
        orderCount.put("paidCount", paidCount+"");
        orderCount.put("deliverCount", deliverCount+"");
        return orderCount;
	}

	/**
	 * 获取供应商订单数量
	 * @param supplierId
	 * @param orderStatus
	 * @return
	 */
	public int getSupplierOrderCount(long supplierId, int orderStatus) {
		return storeOrderMapper.getSupplierOrderCount(supplierId, orderStatus);
	}

	/**
	 * 获取供应商订单
	 * @param supplierId
	 * @param orderStatus
	 * @param pageQuery
	 * @return
	 */
	public List<ShopStoreOrder> getSupplierOrdersNew(long supplierId, int orderStatus, PageQuery pageQuery) {
		return storeOrderMapper.getSupplierOrdersNew(supplierId, orderStatus, pageQuery);
	}

	/**
	 * 获取供应商订单数量(不包含母订单)
	 * @param supplierId
	 * @param orderStatus
	 * @return
	 */
	public int getSupplierOrderCountWithoutParent(long supplierId, int orderStatus) {
		return storeOrderMapper.getSupplierOrderCountWithoutParent(supplierId, orderStatus);
	}

	/**
	 * 获取供应商订单(不包含母订单)
	 * @param supplierId
	 * @param orderStatus
	 * @param pageQuery
	 * @return
	 */
	public List<ShopStoreOrder> getSupplierOrdersNewWithoutParent(long supplierId, int orderStatus, PageQuery pageQuery) {
		return storeOrderMapper.getSupplierOrdersNewWithoutParent(supplierId, orderStatus, pageQuery);
	}

	

	/**
	 * 根据订单号获取对应的item
	 * @param orderNo
	 * @return
	 */
	public List<ShopStoreOrderItem> getOrderNewItemsOnlyByOrderNO(long orderNo) {
		return orderItemMapper.getOrderNewItemsOnlyByOrderNO(orderNo);
	}
	
	/**
     * 支付成功时发放交易奖金
     * @param
     */
    private void grantDealBonuses(long groundUserId,long storeId,long orderNo){
    	//1.确认收货获取地推人员id和上级ids
		logger.info("支付成功时发放交易奖金:groundUserById："+groundUserId);

    	//门店订单确认收货时发放个人门店订单交易奖金和团队订单交易奖金
  		//获取对应的门店
		StoreBusiness  storeBusiness= storeMapper.selectById(storeId);
		logger.info("支付成功时发放交易奖金:当前阶段"+storeBusiness.toStoreStage());
		if(storeBusiness.toStoreStage()==1){
			groundBonusGrantFacade.grantOrderBonus(groundUserId, GroundConstant.BONUS_TYPE_FIRST_STAGE, storeId, orderNo);
		}else if(storeBusiness.toStoreStage()==2){
			groundBonusGrantFacade.grantOrderBonus(groundUserId, GroundConstant.BONUS_TYPE_SECOND_STAGE, storeId, orderNo);
		}else if(storeBusiness.toStoreStage()==3){
			groundBonusGrantFacade.grantOrderBonus(groundUserId, GroundConstant.BONUS_TYPE_THIRD_STAGE, storeId, orderNo);
		}else{
			logger.info("支付成功时发放交易奖金:其他阶段无需发放交易奖金");
		}
  		
    }

	public Integer isDdStore(long storeId) {
		return orderItemMapper.selectStoreById(storeId);
	}

	public Integer selectOwn(long orderNo) {
		return orderItemMapper.selectOwn(orderNo);
	}

	public Integer selectOwnShop(Long orderNo) {
		return orderItemMapper.selectType(orderNo);
	}
}