/**
 * 
 */
package com.yujj.business.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.Logistics;
import com.jiuyuan.constant.account.UserCoinOperation;
import com.jiuyuan.constant.order.OrderCouponStatus;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.FinanceLog;
import com.jiuyuan.entity.UserSharedRecordOrderNew;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.account.UserMember;
import com.jiuyuan.entity.logistics.LOLocation;
import com.jiuyuan.entity.logistics.LOPostage;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.alipay.direct.UtilDate;
import com.jiuyuan.entity.order.OrderItemGroup;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.BeanUtil;
import com.jiuyuan.util.CollectionUtil;
import com.yujj.business.assembler.ProductAssembler;
import com.yujj.dao.mapper.CommentMapper;
import com.yujj.dao.mapper.CouponUseLogMapper;
import com.yujj.dao.mapper.FinanceLogMapper;
import com.yujj.dao.mapper.OrderCouponMapper;
import com.yujj.dao.mapper.OrderDiscountLogMapper;
import com.yujj.dao.mapper.OrderItemGroupMapper;
import com.yujj.dao.mapper.OrderItemMapper;
import com.yujj.dao.mapper.OrderLogMapper;
import com.yujj.dao.mapper.OrderMapper;
import com.yujj.dao.mapper.OrderNewLogMapper;
import com.yujj.dao.mapper.OrderNewMapper;
import com.yujj.dao.mapper.SendBackMapper;
import com.yujj.dao.mapper.StoreBusinessMapper;
import com.yujj.dao.mapper.UserMemberMapper;
import com.yujj.dao.mapper.UserSharedRecordOrderNewMapper;
import com.yujj.entity.Brand;
import com.yujj.entity.StoreBusiness;
//import com.yujj.entity.account.Address;
import com.yujj.entity.order.Coupon;
import com.yujj.entity.order.CouponUseLog;
import com.yujj.entity.order.Order;
import com.yujj.entity.order.OrderDiscountLog;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderItemVO;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.order.OrderNewLog;
import com.yujj.entity.order.OrderNewVO;
import com.yujj.entity.product.Category;
import com.yujj.entity.product.ProductCategory;
import com.yujj.exception.order.DeliveryLocationNotFoundException;
import com.yujj.exception.order.DeliveryLocationNullException;
import com.yujj.exception.order.PostageNotFoundException;

/**
 * @author LWS
 *
 */
@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");
    @Autowired
    private UserSharedService userSharedService;
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderNewMapper orderNewMapper;
    
    @Autowired
    private FinanceLogMapper financeLogMapper;
    
    @Autowired
    private OrderLogMapper orderLogMapper;
    
    @Autowired
    private OrderNewLogMapper orderNewLogMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderItemGroupMapper orderItemGroupMapper;

    @Autowired
    private SendBackMapper sendBackMapper;
    
    
    @Autowired
    private BrandService brandService;
    
    @Autowired
    private UserCoinService userCoinService;
    
    
    @Autowired
    private CouponUseLogMapper couponUseLogMapper;
    
    @Autowired
    private OrderCouponMapper orderCouponMapper ;
    
    @Autowired
    private StoreBusinessMapper storeBusinessMapper ;
    
    @Autowired
    private UserMemberMapper userMemberMapper ;
    
    @Autowired
    private CommentMapper commentMapper;    
    
    @Autowired
    private LOLocationService loLocationService;    
    
    @Autowired
    private LOPostageService loPostageService;    
    
    @Autowired
    private LOWarehouseService lOWarehouseService;    
    
    @Autowired
    private OrderDiscountLogMapper orderDiscountLogMapper;
    
    @Autowired
    private YJJUserAddressService userAddressService;
    
    @Autowired
    private CategoryService categoryService;
    
    
    @Autowired
    private GlobalSettingService globalSettingService;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    

    @Autowired
    private ProductAssembler productAssembler;

    @Autowired
    private UserSharedRecordOrderNewMapper userSharedRecordOrderNewMapper;

//    @Transactional(rollbackFor = Exception.class)
//    public void createOrder17(Order order) {
//        if (CollectionUtils.isEmpty(order.getOrderItems())) {
//            String msg = "order items can not empty!";
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        orderMapper.insertOrder(order);
//
//        long orderId = order.getId();
//        for (OrderItemGroup orderItemGroup : order.getOrderItemGroups()) {  
//            orderItemGroup.setOrderId(orderId);
//            orderItemGroup.setOrderStatus(order.getOrderStatus());
//            
//            double totalPayGroup = 0.00;
//            if(order.getPrePayment() - 0 > 0.001) {
//            	totalPayGroup = orderItemGroup.getTotalMoney() * order.getTotalMoney() / order.getPrePayment();
//            }
//            orderItemGroup.setTotalPay(totalPayGroup);
//
//            orderItemGroupMapper.insertOrderItemGroup(orderItemGroup);
//            long groupId = orderItemGroup.getId();
//
//            for (OrderItem orderItem : order.getOrderItems()) {
//                if (orderItem.getlOWarehouseId() == orderItemGroup.getlOWarehouseId()) {
//                    orderItem.setGroupId(groupId);
//                    double totalPayItem = 0.00;
//                    if(orderItemGroup.getTotalMoney() - 0 > 0.001) {
//                    	totalPayItem = orderItem.getTotalMoney() * totalPayGroup / orderItemGroup.getTotalMoney();
//                    }
//                    orderItem.setTotalPay(totalPayItem);
//                }
//            }
//        }
//        orderItemMapper.insertOrderItems(orderId, order.getOrderItems());
//        if(order.getOrderDiscountLogs().size() > 0) {
//        	orderDiscountLogMapper.insertOrderDiscountLogs(orderId, order.getOrderDiscountLogs());
//        }
//    }
    
    
    /**
     * 这里的逻辑需要重新梳理
     * @param order
     * @param couponId
     * @param userSharedRecordId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public OrderNew createOrder18(Order order, String couponId,long userSharedRecordId,int takeGood) {
    	if (CollectionUtils.isEmpty(order.getOrderItems())) {
    		String msg = "order items can not empty!";
    		logger.error(msg);
    		throw new IllegalArgumentException(msg);
    	}
    	//旧表删除，无需在添加旧表订单记录
//    	orderMapper.insertOrder(order);
    	
    	
    	//1、插入新订单表
    	OrderNew orderNew = createNewOrderFromOrderObj(order, order.getOrderItems(),userSharedRecordId,takeGood);
    //	double discount = orderNew.getTotalPay()/orderNew.getTotalMoney();
    	
    	//2、扣除使用旧表数
    	if(order.getDeductCoinNum() > 0){
    		userCoinService.updateUserCoin(orderNew.getUserId(), 0, -order.getDeductCoinNum(), orderNew.getOrderNo() + "", System.currentTimeMillis(), UserCoinOperation.JIUCOIN_DEDUCT, "", "");	
    	}

    	//3、添加分享下单记录
    	if(userSharedRecordId!=0){
    		UserSharedRecordOrderNew userSharedRecordOrderNew = new UserSharedRecordOrderNew();
        	userSharedRecordOrderNew.setOrderNo(orderNew.getOrderNo());
        	userSharedRecordOrderNew.setUserSharedRecordId(userSharedRecordId);
        	userSharedRecordOrderNew.setCreateTime(System.currentTimeMillis());
        	userSharedRecordOrderNewMapper.insertUserSharedRecordOrderNew(userSharedRecordOrderNew);
    	}
    	
    	//4、根据分组继续组装orderItem
//    	long orderId = order.getId();
    	List<OrderItem>  orderItems = order.getOrderItems();
    	for (OrderItemGroup orderItemGroup : order.getOrderItemGroups()) {
//     		orderItemGroup.setOrderId(orderId);
     		orderItemGroup.setOrderId(0);
//    		orderItemGroup.setOrderStatus(order.getOrderStatus());
    		double totalPayGroup = 0.00;
            if(order.getPrePayment() - 0 > 0.001) {
            	totalPayGroup = orderItemGroup.getTotalMoney() * order.getTotalMoney() / order.getPrePayment();
            }
//            orderItemGroup.setTotalPay(totalPayGroup);
    		//itemGroup已经删除，不需要再创建记录
//    		orderItemGroupMapper.insertOrderItemGroup(orderItemGroup);
//    		long groupId = orderItemGroup.getId();
    		
    		for (OrderItem orderItem : orderItems) {
    			if (orderItem.getlOWarehouseId() == orderItemGroup.getlOWarehouseId()) {
//    				orderItem.setGroupId(groupId);
    				orderItem.setGroupId(0);
    				double totalPayItem = 0.00;
                    if(orderItemGroup.getTotalMoney() - 0 > 0.001) {
                    	totalPayItem = orderItem.getTotalMoney() * totalPayGroup / orderItemGroup.getTotalMoney();
                    }
                    orderItem.setTotalPay(totalPayItem);
    				orderItem.setOrderNo(orderNew.getOrderNo());//插入新订单orderno
    				orderItem.setParentId(orderNew.getOrderNo());//插入新订单orderno
    				//orderItem.setTotalPay(discount * orderItem.getTotalMoney());
    			}
    		}
    	}
    	//5、按照优惠券折扣计算item实际价格
    	//List<Coupon> couponList = getUserCouponListByOrderNo(orderNew.getOrderNo());
    	if(couponId != null && couponId.length() > 0){
    		calculateItemPrice(orderNew, orderItems ,  couponId);
    	}
    	//6、创建orderItem记录
    	orderItemMapper.insertOrderItems(orderItems);
    	
    	//7、创建订单优惠记录
    	if(order.getOrderDiscountLogs().size() > 0) {
    		for(OrderDiscountLog orderDiscountLog : order.getOrderDiscountLogs()){
    			orderDiscountLog.setOrderNo(orderNew.getOrderNo());
    		}
    		orderDiscountLogMapper.insertOrderDiscountLogs( order.getOrderDiscountLogs());
    	}
    	return orderNew;
    }
    
    
    public OrderNew createNewOrderFromOrderObj(Order order, List<OrderItem> itemList,long userSharedRecordId,int takeGood) {
    
    	//插入新订单表
    	OrderNew orderNew = new OrderNew();
    	orderNew.setCoinUsed(order.getUnavalCoinUsed());
    	if(order.getDeductCoinNum() > 0){
    		orderNew.setCoinUsed(order.getDeductCoinNum());
    	}
    	orderNew.setExpressInfo(order.getExpressInfo());
    	orderNew.setIp(order.getIp());
    	orderNew.setOrderStatus(order.getOrderStatus());
    	orderNew.setPaymentNo(order.getPaymentNo());
    	//orderNew.setPaymentType(order.getpayment);
    	orderNew.setPlatform(order.getPlatform());
    	orderNew.setPlatformVersion(order.getPlatformVersion());
    	orderNew.setRemark(order.getRemark());
    	orderNew.setStatus(order.getStatus());
    	if(order.getOrderStatus() == OrderStatus.PAID){
    		orderNew.setPayTime(System.currentTimeMillis());
    		
    	}
    	orderNew.setTotalExpressMoney(order.getTotalExpressMoney());
    	
    	double originalPrice = 0;
    	double originalMarketPrice = 0;
    	for(OrderItem item : itemList){
    		originalPrice += item.getTotalMoney();
    		originalMarketPrice += item.getTotalMarketPrice();
    	}
    	orderNew.setTotalMoney(originalPrice);
    	orderNew.setTotalMarketPrice(originalMarketPrice);
    	//orderNew.setTotalPay(Double.parseDouble(PayUtil.formatPrice(order.getPayAmountInCents()))-order.getTotalExpressMoney());
    	orderNew.setTotalPay(order.getTotalMoney());
    	if(orderNew.getTotalMoney() == 0 && orderNew.getTotalExpressMoney() == 0){
    		orderNew.setPayTime(System.currentTimeMillis());
    	}
    	orderNew.setCreateTime(System.currentTimeMillis());
    	orderNew.setUpdateTime(System.currentTimeMillis());
    	orderNew.setUserId(order.getUserId());
    	orderNew.setExpiredTime(order.getExpiredTime());
    	if(takeGood == 1){
        	orderNew.setOrderType(3);
    	}else{
        	orderNew.setOrderType(0);
    	}
    	//orderNew.setCoinUsed(coinUsed);
    	
    	int totalBuyCount = 0;
    	for (OrderItem orderItem : itemList) {
    		totalBuyCount += orderItem.getBuyCount();
    	}
    	orderNew.setTotalBuyCount(totalBuyCount);
    	
//    	//门店商家ID，佣金记录
//    	StoreBusiness storeBusiness = this.getBelongStoreBusiness(order.getUserId());
//    	UserMember userMember = this.getUserMemberById(order.getUserId());
//    	orderNew.setDistributionStatus(1);
//    	if(storeBusiness != null && userMember != null){
//    		orderNew.setCommission(0.01 * storeBusiness.getCommissionPercentage() * orderNew.getTotalPay());
//    		if(storeBusiness.getDistributionStatus() == 0 && storeBusiness.getStatus() == 0 && userMember.getDistributionStatus() == 0 ){
//    			orderNew.setDistributionStatus(0);
//
//    		}
//
//    		orderNew.setBelongBusinessId(storeBusiness.getId());
//    	}
    	
    	orderNewMapper.insertOrder(orderNew);
//    	long time = System.currentTimeMillis();
//    	orderNewMapper.updateOrderCommission(orderNew, time);
    	return orderNew;
    
    }
    //计算orderItem价格
    public void calculateItemPrice(OrderNew orderNew, List<OrderItem> itemList, String couponId) {
    	

		int needDeal = 0;
		double actualMatchAmount = 0;
		double unmatchAmount = 0;
		double actualDiscountAmount = 0;
		double actualDiscount = 0;
		double unmatchDiscount = 0;
		double allDiscount = 0;
		List<Coupon> couponList = getCouponByIdArr(couponId, orderNew.getUserId());
		for(Coupon coupon : couponList){
			needDeal = 0;
//		Coupon coupon = this.getCouponById(Long.parseLong(couponId), orderNew.getUserId());
			if(coupon != null && (coupon.getRangeType() == 1 || coupon.getRangeType() == 4)){
				needDeal = 1;
			}
			
			if(needDeal == 1 && coupon.getRangeContent() != null && coupon.getRangeContent().length() > 1){
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
					Set<Long> productIds = new HashSet<Long>();
					for (OrderItem orderItem : itemList) {
						productIds.add(orderItem.getProductId());
					}
					
					
//					//计算首单优惠金额
//			    	int count = getUserOrderCountForFirstDiscount(orderNew.getUserId());
//			    	double firstDiscountCash = 0;
//			    	if (count == 0 ){
//			    		//首单优惠
//			        	firstDiscountCash = globalSettingService.getDouble(GlobalSettingName.FIRST_DISCOUNT);
//			    		if(firstDiscountCash < totalCash ){
//			    			totalCash -= firstDiscountCash;
//			    			
//			    		}else{
//			    			firstDiscountCash = totalCash;
//			    			totalCash = 0.0;
//			    		}
//			    	}else {
//			    		firstDiscountCash = 0.0;
//			    		
//			    	}
					
					//子分类匹配
					List<Category> categorieListAll = new ArrayList<Category>();
					List<String> idsList = new ArrayList<String>();
					if(coupon.getRangeType() == 1){
						categorieListAll = categoryService.getCategories();
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
										for (OrderItem orderItem : itemList) {
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
							
							for (OrderItem orderItem : itemList) {
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
					unmatchAmount = orderNew.getTotalMoney() - actualMatchAmount;
					if((unmatchAmount + (actualMatchAmount - actualDiscountAmount)) > 0){
						
						unmatchDiscount = (unmatchAmount + (actualMatchAmount - actualDiscountAmount) - (orderNew.getTotalMoney() - orderNew.getTotalPay() - actualDiscountAmount)) /(unmatchAmount + (actualMatchAmount - actualDiscountAmount));
					}else{
						unmatchDiscount = 0;
					}
										
					allDiscount = (orderNew.getTotalPay() + actualDiscountAmount) / orderNew.getTotalMoney();
					if(allDiscount > 1){
						allDiscount = 1;
					}
					if(actualMatchAmount > 0){
						actualDiscount = (orderNew.getTotalPay() - unmatchAmount * unmatchDiscount)/actualMatchAmount;
					}
					if(actualDiscount < 0){
						actualDiscount = 0;
					}
					if(actualDiscount > 1){
						actualDiscount = 1;
					}
									
					//第二次遍历设置实际优惠价格
					if( coupon.getRangeType() == 1 && idsArr != null && idsArr.length > 0){
						for (OrderItem orderItem : itemList) {
							orderItem.setTotalPay(orderItem.getTotalMoney() * unmatchDiscount);	
						}
						List<ProductCategory> productCategoryList =  productCategoryService.getProductCategoryListByProductIds(productIds);
						if(productCategoryList != null && productCategoryList.size() > 0 ){	
							String productSKUIdStr = "";
							for(String id : idsList){
								
								for (ProductCategory productCategory : productCategoryList) {
									
									if((productCategory.getCategoryId() + "" ).equals(id) ){
										for (OrderItem orderItem : itemList) {
											
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
						for (OrderItem orderItem : itemList) {
							orderItem.setTotalPay(orderItem.getTotalMoney() * unmatchDiscount);	
						}
						for(String arr : idsArr){
							
							for (OrderItem orderItem : itemList) {
								
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

   
//    public OrderNew queryOrderNewFromOld(Order order) {
//    	OrderNew orderNew;
//    	long orderNewNo = 0;
//    	List<OrderItem>  items = orderItemMapper.getOrderItems(order.getUserId(), CollectionUtil.createList(order.getId()));
//        if(items!=null&&items.size()>0){
//          	orderNewNo = items.get(0).getOrderNo();
//          	orderNew = orderNewMapper.getOrderByNo(orderNewNo);
//          	return orderNew;
//          	
//          }else { 
//        	return null;
//          }
//    }
    //删除
//    public OrderNew queryOrderNewFromOrderId(long orderId) {
//    	OrderNew orderNew;
//    	long orderNewNo = 0;
//    	List<OrderItem>  items = orderItemMapper.getOrderItemsByOrderId(orderId);
//    	if(items!=null&&items.size()>0){
//    		orderNewNo = items.get(0).getOrderNo();
//    		orderNew = orderNewMapper.getOrderByNo(orderNewNo);
//    		return orderNew;
//    		
//    	}else { 
//    		return null;
//    	}
//    }
    
    public OrderItem getOrderNewItemById(long userId, long orderItemId) {
    	return orderItemMapper.getOrderItemById(userId, orderItemId);
    }
    
    public Coupon getCouponById(long id, long userId) {
    	return orderCouponMapper.getCouponById(id, userId);
    }
    
    public List<Coupon> getCouponByIdArr(String ids, long userId) {
    	return orderCouponMapper.getCouponByIdArr(ids, userId);
    }
    
    //删除旧表
//    public Order queryOrderOldFromNew(OrderNew orderNew) {
//    	Order order;
//    	long orderId = 0;
//    	List<OrderItem>  items = orderItemMapper.getOrderNewItems(orderNew.getUserId(), CollectionUtil.createList(orderNew.getOrderNo()));
//    	if(items!=null&&items.size()>0){
//    		orderId = items.get(0).getOrderId();
//    		order = orderMapper.getOrderById(orderId);
//    		return order;
//    		
//    	}else { 
//    		return null;
//    	}
//    }
    
    public void splitOrderNew(long userId,long orderNo) {
    	long orderNewNo = 0;
//    	List<OrderItem>  items = orderItemMapper.getOrderItems(order.getUserId(), CollectionUtil.createList(order.getId()));
    	List<OrderItem>  items = orderItemMapper.getOrderNewItems(userId, CollectionUtil.createList(orderNo));
        if(items!=null&&items.size()>0){
        	orderNewNo = items.get(0).getOrderNo();
        	OrderNew orderNew = orderNewMapper.getOrderByNo(orderNewNo);
        	//int count = orderNewMapper.updateOrderPayStatus(orderNew.getOrderNo(), "1702488579", PaymentType.BANKCARD_PAY, OrderStatus.PAID, OrderStatus.UNPAID, System.currentTimeMillis());
			
        	splitOrderNewDetail(orderNew , items, 0);
        }	
    }
    public void splitOrderNewDetail(OrderNew orderNew, List<OrderItem> items, double commission) {
    	
    		long orderNewNo = orderNew.getOrderNo();
    		OrderNew orderTemp = orderNewMapper.getOrderByNo(orderNewNo);
//    		orderTemp.setPaymentNo(orderNew.getPaymentNo());
//    		orderTemp.setPaymentType(orderNew.getPaymentType());
    		
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
    			for(OrderItem orderItem : items ){
    				parentlOWarehouseId = orderItem.getlOWarehouseId();
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
    				
    			}
    			
    			//目的地为空抛出异常
    	    	if(StringUtils.isEmpty(orderNew.getExpressInfo()) && orderNew.getOrderType() != 3) {
    	    		throw new DeliveryLocationNullException();
    	    	}

    	    	//获取收货地id信息
    	    	//填写的市,数据表中未查到抛出异常
    	    	int distributionLocationId = 0;
    	    	if(orderNew.getOrderType() != 3){
	    	    	LOLocation location = loLocationService.getByName(orderNew.getExpressInfo());
	    	    	if(location == null) {
	    	    		throw new DeliveryLocationNotFoundException();
	    	    	}
	    	    	distributionLocationId = location.getId();
    	    	}else{
    	    		lOWarehouseIdCount =1;
    	    	}
    	    	LOWarehouse loWarehouse = new LOWarehouse();

    			lOWarehouseId = 0;
    			//需要拆分子订单
    			if(lOWarehouseIdCount > 1 ){
    				int i = 0 ;
    				double cms = 0;
    				double partCms = 0;
    				for(OrderItem orderItem : items ){
    					i++;
    					if(orderItem.getlOWarehouseId() != lOWarehouseId){
    						lOWarehouseId = orderItem.getlOWarehouseId();
    						orderTemp.setParentId(orderNewNo);
    						orderTemp.setTotalMoney(priceMap.get(lOWarehouseId));//item总价
    						orderTemp.setTotalPay(itemPayMap.get(lOWarehouseId));//item总折后价
//    						orderTemp.setTotalPay(priceMap.get(lOWarehouseId) * discount);//item总折后价
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
    						orderNewMapper.insertOrder(orderTemp);
    						

    					}
    					//待优化 ，批量更新item 的orderNo
    					orderItemMapper.updateOrderNo(orderItem.getId(), orderTemp.getOrderNo(), System.currentTimeMillis());
    				}
    				orderNewMapper.updateOrderParentId(orderNewNo , -1 ,0 , System.currentTimeMillis());
    				//不需要拆分子订单
    			}else if(lOWarehouseIdCount == 1){
    				
    				orderNewMapper.updateOrderParentId(orderNew.getOrderNo() , orderNew.getOrderNo(), parentlOWarehouseId , System.currentTimeMillis());
    				
    			}
    			
    		}
    		
    }
    //删除旧表
//    public void createOrder188bak(Order order) {
//    	if (CollectionUtils.isEmpty(order.getOrderItems())) {
//    		String msg = "order items can not empty!";
//    		logger.error(msg);
//    		throw new IllegalArgumentException(msg);
//    	}
//    	OrderNew orderNew =new OrderNew();
//    	orderNew.setCoinUsed(order.getUnavalCoinUsed());
//    	orderNew.setExpressInfo(order.getExpressInfo());
//    	orderNew.setIp(order.getIp());
//    	orderNew.setOrderStatus(order.getOrderStatus());
//    	orderNew.setPaymentNo(order.getPaymentNo());
//    	//orderNew.setPaymentType(order.getpayment);
//    	orderNew.setPlatform(order.getPlatform());
//    	orderNew.setPlatformVersion(order.getPlatformVersion());
//    	orderNew.setRemark(order.getRemark());
//    	orderNew.setStatus(order.getStatus());
//    	orderNew.setTotalExpressMoney(order.getTotalExpressMoney());
//    	orderNew.setTotalMoney(order.getTotalMoney());
//    	orderNew.setTotalPay(Double.parseDouble(PayUtil.formatPrice(order.getPayAmountInCents())));
//    	orderNew.setUserId(order.getUserId());
//    	
//    	
//    	orderNewMapper.insertOrder(orderNew);
//    	
//    	long orderId = order.getId();
//    	for (OrderItemGroup orderItemGroup : order.getOrderItemGroups()) {
//    		orderItemGroup.setOrderId(orderId);
//    		orderItemGroup.setOrderStatus(order.getOrderStatus());
//    		
//    		orderItemGroupMapper.insertOrderItemGroup(orderItemGroup);
//    		long groupId = orderItemGroup.getId();
//    		
//    		for (OrderItem orderItem : order.getOrderItems()) {
//    			if (orderItem.getlOWarehouseId() == orderItemGroup.getlOWarehouseId()) {
//    				orderItem.setGroupId(groupId);
//    			}
//    		}
//    	}
//    	orderItemMapper.insertOrderItems(orderId, order.getOrderItems());
//    	if(order.getOrderDiscountLogs().size() > 0) {
//    		orderDiscountLogMapper.insertOrderDiscountLogs(orderId, order.getOrderDiscountLogs());
//    	}
//    }

    //删除旧表
//    @Transactional(rollbackFor = Exception.class)
//    public void createOrderXX(Order order) {
//        if (CollectionUtils.isEmpty(order.getOrderItems())) {
//            String msg = "order items can not empty!";
//            logger.error(msg);
//            throw new IllegalArgumentException(msg);
//        }
//
//        orderMapper.insertOrder(order);
//
//        // long orderId = order.getId();
//        // orderItemMapper.insertOrderItems(orderId, order.getOrderItems());
//        // orderItemGroupMapper.insertOrderItemGroups(orderId, order.getOrderStatus(), order.getOrderItemGroups());
//
//        long orderId = order.getId();
//        for (OrderItemGroup orderItemGroup : order.getOrderItemGroups()) {
//            orderItemGroup.setOrderId(orderId);
//            orderItemGroup.setOrderStatus(order.getOrderStatus());
//
//            orderItemGroupMapper.insertOrderItemGroup(orderItemGroup);
//            long groupId = orderItemGroup.getId();
//
//            for (OrderItem orderItem : order.getOrderItems()) {
//                if (orderItem.getBrandId() == orderItemGroup.getBrandId()) {
//                    orderItem.setGroupId(groupId);
//                }
//            }
//        }
//        orderItemMapper.insertOrderItems(orderId, order.getOrderItems());
//    }

    
//    public Order getOrderByNo(String orderNo) {
//        return orderMapper.getOrderByNo(orderNo);
//    }
    
//    public Order getOrderByNoAllStatus(String orderNo) {
//    	return orderNewMapper.getOrderByNoAllStatus(orderNo);
//    }
    
    public OrderNew getOrderByNoAllStatus(String orderNo) {
    	return orderNewMapper.getOrderByNoAllStatus(Long.parseLong(orderNo));
    }
//删除旧表
//    public Order getUserOrderByNo(long userId, String orderNo) {
//        return orderMapper.getUserOrderByNo(userId, orderNo);
//    }
    public OrderNew getUserOrderNewByNo(long userId, String orderNo) {
    	return orderNewMapper.getUserOrderByNo(userId, orderNo);
    }
    public OrderNew getUserOrderNewByNo(String orderNo) {
    	return orderNewMapper.getUserOrderByNoOnly(orderNo);
    }
    public OrderNewVO getUserOrderNewDetailByNo(long userId, String orderNo) {
    	OrderNewVO order = orderNewMapper.getUserOrderByNo(userId, orderNo);
    	 //取对应item列表
    	 List<OrderItem>  items = orderItemMapper.getOrderNewItems(order.getUserId(), CollectionUtil.createList(order.getOrderNo()));
    	 Map<Long, List<OrderItemVO>> result = new HashMap<Long, List<OrderItemVO>>();
    	 List<List<OrderItemVO>> resultList = new ArrayList<List<OrderItemVO>>();
    	 List<OrderNewVO> resultOrderList = new ArrayList<OrderNewVO>();
    	 List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
    	 for (OrderItem orderItem : items) {
     		OrderItemVO vo = new OrderItemVO();
     		BeanUtil.copyProperties(vo, orderItem);
     		
     		long wareHouseId = orderItem.getlOWarehouseId();
    		List<OrderItemVO> list = result.get(wareHouseId);
    		if (list == null) {
    			list = new ArrayList<OrderItemVO>();
    			result.put(wareHouseId, list);
    			resultList.add(list);
    		}
    		list.add(vo);
     		composites.add(vo);
     	}
     	productAssembler.assemble(composites);
    	//order.setOrderItemMap(result);
     	
     	//未付款订单预拆分
     	if(order.getOrderType() != 2 && order.getOrderStatus() != null && order.getOrderStatus().equals(OrderStatus.UNPAID)){
     		
     		long wareHouseId = 0;
     		int itemCount = 0;
     		List<OrderItemVO> itemList = new ArrayList<OrderItemVO>();
     		OrderNewVO childOrder ;
     		LOWarehouse loWarehouse = new LOWarehouse();
     		//获取收货地id信息
     		//填写的市,数据表中未查到抛出异常
     		int distributionLocationId = 0;
     		if(order.getOrderType() != 3){
	     		LOLocation location = loLocationService.getByName(order.getExpressInfo());
	     		if(location == null) {
	     			throw new DeliveryLocationNotFoundException();
	     		}
	     		distributionLocationId = location.getId();
     		}
     		
     		for(Map.Entry<Long, List<OrderItemVO>>  itemListMap : result.entrySet()){
     			childOrder =  new OrderNewVO();
     			wareHouseId = itemListMap.getKey();
     			itemList = itemListMap.getValue();
     			for(OrderItemVO itemVO : itemList){
     				itemCount += itemVO.getBuyCount();
     			}
     			
     			childOrder.setlOWarehouseId(wareHouseId);
     			childOrder.setOrderItems(new ArrayList<OrderItem>(itemList));
     			
     			
     			//计算运费
     			//如果满足包邮条件
     			loWarehouse = lOWarehouseService.getById(wareHouseId);
     			if((loWarehouse.getIsFree() == Logistics.DISCOUNT.getIntValue() && loWarehouse.getFreeCount() <= itemCount) || order.getOrderType() == 3) {
     				childOrder.setTotalExpressMoney(0);
     			}else{
     				//不满足包邮条件
     				LOPostage loPostage = loPostageService.getPostage(loWarehouse.getDeliveryLocation(), distributionLocationId);
     				if(loPostage == null) {
     					throw new PostageNotFoundException();
     				}
     				childOrder.setTotalExpressMoney(loPostage.getPostage());//设置子订单运费
     			}
     			
     			resultOrderList.add(childOrder);
     		}
     		order.setChildOrderList(resultOrderList);
     	}
    	//order.setOrderItemSplitList(resultList);
     	order.setOrderItems(new ArrayList<OrderItem>(composites));
    	return order;
    }
    //删除旧表
//    public Order getUserOrderByNoAll(long userId, String orderNo) {
//    	return orderMapper.getUserOrderByNoAll(userId, orderNo);
//    }

//    public List<OrderItem> getOrderItems(long userId, Collection<Long> orderNos) {
//        return orderItemMapper.getOrderItems(userId, orderNos);
//    }
    
    public List<OrderItemVO> getOrderNewItemsVO(long userId, Collection<Long> orderNOs) {
    	List<OrderItemVO> composites = new ArrayList<OrderItemVO>();
    	
    	List<OrderItem> itemList = orderItemMapper.getOrderNewItems(userId, orderNOs);
    	for (OrderItem orderItem : itemList) {
    		OrderItemVO vo = new OrderItemVO();
    		BeanUtil.copyProperties(vo, orderItem);
    		composites.add(vo);
    	}
    	productAssembler.assemble(composites);
    	return composites;
    }
    public List<OrderItem> getOrderNewItems(long userId, Collection<Long> orderNOs) {
    
    	
    	return  orderItemMapper.getOrderNewItems(userId, orderNOs);
    	
    	 
    }
    public List<OrderItem> getOrderNewItemsByItemIds(long userId, Collection<Long> orderItemIds) {
    	
    	
    	return  orderItemMapper.getOrderNewItemsByItemIds(userId, orderItemIds);
    	
    	
    }
    public List<OrderItem> getOrderNewItemsByOrderNO(long userId, long orderNo) {
    	return orderItemMapper.getOrderNewItemsByOrderNO(userId, orderNo);
    }

//    public List<OrderItemGroup> getOrderItemGroups(long userId, Collection<Long> orderIds) {
//        return orderItemGroupMapper.getOrderItemGroups(userId, orderIds);
//    }
    //删除旧表
//    public int getUserOrderCount(long userId, OrderStatus orderStatus) {
//        if(orderStatus == OrderStatus.DELIVER) {
//        	return orderMapper.getUserDeliverOrderCount(userId);
//    	}
//        return orderMapper.getUserOrderCount(userId, orderStatus);
//    }
    
    
//    public int getUserNewOrderCountByOrderStatus(long userId, OrderStatus orderStatus) {
//    	return orderNewMapper.getUserOrderCountByOrderStatus(userId, orderStatus);
//    }
    public int getUserNewOrderCount(long userId, OrderStatus orderStatus) {
    	return orderNewMapper.getUserOrderCount(userId, orderStatus);
    }
    
    public int getUserOrderCountAll(long userId) {
    	
    	
    	return orderNewMapper.getUserOrderCountAll(userId);
    }
    public int getUserOrderDeductCoinNum(long orderNo) {
    	
    	
    	return orderNewMapper.getUserOrderDeductCoinNum(orderNo);
    }
    
    public int getUserOrderCountForFirstDiscount(long userId) {
    	
    	
    	return orderNewMapper.getUserOrderCountForFirstDiscount(userId);
    }
    
    
    public int updateOrderAddressAfterSale(long userId, long orderNo, long addrId ) {
    	Address address = userAddressService.getUserAddress(userId, addrId);
    	
    	String expressInfo = "";
    	if(address != null){
    		expressInfo = address.getExpressInfo();
    	}
    	return orderNewMapper.updateOrderAddressAfterSale(userId, orderNo, expressInfo, System.currentTimeMillis());
    }
    
    public int getUserNewOrderCountWithoutParent(long userId, OrderStatus orderStatus) {
    	

    	return orderNewMapper.getUserOrderCountWithoutParent(userId, orderStatus);
    }
    
    public int getUserNewOrdersCountByStatuses(long userId, Collection<Integer> statuses , String orderSearchNo) {
    	
    	
    	return orderNewMapper.getUserOrderCountByStatuses(userId, statuses, orderSearchNo);
    }
    
    public int getUserNewOrdersCountAfterSale(long userId, Collection<Integer> statuses , String orderSearchNo) {
    	
    	long time = UtilDate.getPlanDayFromDate(new Date(), -15).getTime();
    	return orderNewMapper.getUserNewOrdersCountAfterSale(userId, statuses, orderSearchNo, time);
    }
    //删除旧表
//    public List<Order> getUserOrders(long userId, OrderStatus orderStatus, PageQuery pageQuery) {
//    	if(orderStatus == OrderStatus.DELIVER) {
//    		return orderMapper.getUserDeliverOrders(userId, pageQuery);
//    	}
//        return orderMapper.getUserOrders(userId, orderStatus, pageQuery);
//    }
    
    
    
//    public List<OrderNewVO> getUserOrdersByOrderStatus(long userId, OrderStatus orderStatus, PageQuery pageQuery) {
//    	return orderNewMapper.getUserOrdersByOrderStatus(userId, orderStatus, pageQuery);
//    }
    
    public List<OrderNewVO> getUserOrdersNew(long userId, OrderStatus orderStatus, PageQuery pageQuery) {
    	return orderNewMapper.getUserOrders(userId, orderStatus, pageQuery);
    }
    
    public List<OrderNewVO> getUserOrdersNewWithoutParent(long userId, OrderStatus orderStatus, PageQuery pageQuery) {
//    	if(orderStatus == OrderStatus.DELIVER) {
//    		return orderNewMapper.getUserDeliverOrders(userId, pageQuery);
//    	}
    	return orderNewMapper.getUserOrdersWithoutParent(userId, orderStatus, pageQuery);
    }
    
    public List<OrderNew> getUserOrdersNewByStatuses(long userId, Collection<Integer> statuses, PageQuery pageQuery , String orderSearchNo) {
//    	if(orderStatus == OrderStatus.DELIVER) {
//    		return orderNewMapper.getUserDeliverOrders(userId, pageQuery);
//    	}
    	return orderNewMapper.getUserOrdersNewByStatuses(userId, statuses, pageQuery , orderSearchNo);
    }
    
    public List<OrderNew> getUserOrdersNewAfterSale(long userId, Collection<Integer> statuses, PageQuery pageQuery , String orderSearchNo) {
//    	if(orderStatus == OrderStatus.DELIVER) {
//    		return orderNewMapper.getUserDeliverOrders(userId, pageQuery);
//    	}
    	int afterSaleMinutes = 21600;
        
        JSONArray jsonArrayConfirm = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
        try {
			
        	for(Object obj : jsonArrayConfirm) {
        		
        		if(((JSONObject)obj).get("afterSaleMinutes") != null ){
        			afterSaleMinutes = (int) ((JSONObject)obj).get("afterSaleMinutes");
        		}  
        	} 
		} catch (Exception e) {
			e.printStackTrace();
		}
    	long time = UtilDate.getPlanDayFromDate(new Date(), 0 - afterSaleMinutes/1440).getTime();
    	
    	return orderNewMapper.getUserOrdersNewAfterSale(userId, statuses, pageQuery , orderSearchNo, time);
    }
    
    public List<OrderNewVO> getChildOrderList(long userId, Collection<Long> orderNOs) {
    	
    	return orderNewMapper.getChildOrderList(userId, orderNOs);
    }
    //删除旧表
//    public List<Map<String, Integer>> getOrderCountMap(long userId) {
//        return orderMapper.getOrderCountMap(userId);
//    }
    public List<Map<String, Integer>> getOrderNewCountMap(long userId) {
    	return orderNewMapper.getOrderCountMap(userId);
    }
    //删除旧表
//    @Transactional(rollbackFor = Exception.class)
//    public int addSendBack(Order order, String expressSupplier, String expressOrderNo, String phone) {
//        if (order.getOrderType() != OrderType.SEND_BACK || order.getOrderStatus() != OrderStatus.UNCHECK ||
//            order.isSended()) {
//            String msg = "bad args! not a valid send back order, order id:" + order.getId();
//            logger.error(msg);
//            throw new IllegalStateException(msg);
//        }
//        return addSendBackUncheck(order, expressSupplier, expressOrderNo, phone);
//    }
//		删除旧表
//    @Transactional(rollbackFor = Exception.class)
//    public int addSendBackUncheck(Order order, String expressSupplier, String expressOrderNo, String phone) {
//        long time = System.currentTimeMillis();
//        int count = orderMapper.updateOrderAsSended(order.getId(), time);
//        if (count != 1) {
//            String msg = "update order send error!, order id:" + order.getId();
//            logger.error(msg);
//            throw new IllegalStateException(msg);
//        }
//
//        SendBack sendBack = new SendBack();
//        sendBack.setUserId(order.getUserId());
//        sendBack.setOrderId(order.getId());
//        sendBack.setCreateTime(time);
//        sendBack.setUpdateTime(time);
//        sendBack.setStatus(0);
//        sendBack.setExpressSupplier(expressSupplier);
//        sendBack.setExpressOrderNo(expressOrderNo);
//        sendBack.setPhone(phone);
//        return sendBackMapper.addSendBack(sendBack);
//    }

    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPayStatus(OrderNew orderNew, String paymentNo, PaymentType paymentType, OrderStatus newStatus,
                                  OrderStatus oldStatus, long time) {
    	int count = 0;
    	logger.info("<测试分享下单5orderNew.orderNew.getOrderType>:"+orderNew.getOrderType());
    	if(orderNew.getOrderType() == 0){
    		
    		//删除旧表
//    		Order order = this.queryOrderOldFromNew(orderNew);
//    		count = orderMapper.updateOrderPayStatus(order.getId(), paymentNo, paymentType, newStatus, oldStatus, time);
//    		if (count != 1) {
//    			String msg = "udpate order status error!, order id:" + order.getId();
//    			logger.error(msg);
//    			throw new IllegalStateException(msg);
//    		}
//    		
//    		count = orderItemGroupMapper.updateOrderStatus(order.getId(), newStatus, oldStatus, time);
//    		if (count <= 0) {
//    			String msg = "udpate group order status error!, order id:" + order.getId();
//    			logger.error(msg);
//    			throw new IllegalStateException(msg);
//    		}       
//    		OrderLog orderLog = new OrderLog();
//    		orderLog.setUserId(order.getUserId());
//    		orderLog.setOrderId(order.getId());
//    		orderLog.setNewStatus(newStatus);
//    		orderLog.setOldStatus(oldStatus);
//    		orderLog.setCreateTime(time);
//    		orderLogMapper.addOrderLog(orderLog);

    	}
        
        //orderNew操作
        List<OrderItem>  items = orderItemMapper.getOrderNewItems(orderNew.getUserId(), CollectionUtil.createList(orderNew.getOrderNo()));

		Map<String, Object> params = new HashMap<String, Object>();
    	if(orderNew != null){
    		
        	//更新支付状态
        	count = orderNewMapper.updateOrderPayStatus(orderNew.getOrderNo(), paymentNo, paymentType, newStatus, oldStatus, time);
        	logger.info("<测试分享下单6count>:"+count);
        	if(count == 1 && orderNew.getOrderType() == 0){//0: 普通订单 
//	        		orderNew.setPaymentNo(paymentNo);
//	        		orderNew.setPaymentType(paymentType.getIntValue()+"");
        		//门店商家ID，佣金记录
            	StoreBusiness storeBusiness = this.getBelongStoreBusiness(orderNew.getUserId());
            	UserMember userMember = this.getUserMemberById(orderNew.getUserId());
            	orderNew.setDistributionStatus(1);
            	double commission = 0;
            	//如果该用户是门店绑定用户
            	double memberCommissionPercentage = 0;
            	double memberCommissionPercentageSecond = 0;
            	StoreBusiness storeBusinessFirst  = new StoreBusiness();
				StoreBusiness storeBusinessSecond  = new StoreBusiness();
            	//如果该用户是门店绑定用户
            	if(storeBusiness != null && userMember != null){
            		
            		if(storeBusiness.getDeep() == 1){
            			memberCommissionPercentage = storeBusiness.getMemberCommissionPercentage();
            			storeBusinessFirst = storeBusiness;
            			orderNew.setDividedCommission(memberCommissionPercentage/100 + "");
            		}else if(storeBusiness.getDeep() == 2 && storeBusiness.getSuperBusinessIds().trim().length() > 1 && storeBusiness.getSuperBusinessIds().trim().startsWith(",")){
            			storeBusinessSecond = storeBusiness;
            			memberCommissionPercentageSecond = storeBusiness.getMemberCommissionPercentage();
            			String[] idsArr = storeBusiness.getSuperBusinessIds().substring(1).split(",");
            			if(idsArr.length > 0){
            				storeBusinessFirst = storeBusinessMapper.getById(Long.parseLong(idsArr[0]));
            				memberCommissionPercentage = storeBusinessFirst.getMemberCommissionPercentage();
            				
            			}
            			orderNew.setDividedCommission(memberCommissionPercentage * (100 - memberCommissionPercentageSecond)/10000 + "," + memberCommissionPercentage * memberCommissionPercentageSecond/10000);
            		}else if(storeBusiness.getDeep() > 2 && storeBusiness.getSuperBusinessIds().trim().length() > 1 && storeBusiness.getSuperBusinessIds().trim().startsWith(",")){
            			String[] idsArr = storeBusiness.getSuperBusinessIds().substring(1).split(",");
            			if(idsArr.length > 1){
            				storeBusinessFirst = storeBusinessMapper.getById(Long.parseLong(idsArr[0]));
            				storeBusinessSecond = storeBusinessMapper.getById(Long.parseLong(idsArr[1]));
            				memberCommissionPercentage = storeBusinessFirst.getMemberCommissionPercentage();
            				memberCommissionPercentageSecond = storeBusinessSecond.getMemberCommissionPercentage();
            			}
            			orderNew.setDividedCommission(memberCommissionPercentage * (100 - memberCommissionPercentageSecond)/10000 + "," + memberCommissionPercentage * memberCommissionPercentageSecond/10000);
            		}
            		orderItemMapper.updateItemsCommssion(orderNew.getOrderNo() ,0.01 * memberCommissionPercentage, time); //更新item表commission
//            		commission = 0.01 * storeBusiness.getCommissionPercentage() * orderNew.getTotalPay();
            		commission = orderItemMapper.getItemsCommssionTotal(orderNew.getOrderNo());
            		orderNew.setCommission(commission);
            		if(storeBusiness.getDistributionStatus() == 0 && storeBusiness.getStatus() == 0 && userMember.getDistributionStatus() == 0 ){
            			orderNew.setDistributionStatus(0);
            		}
            		orderNew.setBelongBusinessId(storeBusiness.getId());
            		//orderNew.setDividedCommission("");
            		//更新订单提成金额等字段
            		orderNewMapper.updateOrderCommission(orderNew, time);
            		
            		//更新storebusiness的cashIncome
            		if(storeBusiness.getDeep() == 1 && commission > 0){
            			params.put("commission", commission);
            			storeBusinessMapper.updateStoreIncome(storeBusiness.getId(), params);
            			
            			//记录门店商家财务数据 订单提成收入
            			FinanceLog financeLog = new FinanceLog();
            			financeLog.setCash(commission);
            			financeLog.setCreateTime(time);
            			financeLog.setRelatedId(orderNew.getOrderNo());
            			financeLog.setStoreId(storeBusiness.getId());
            			financeLog.setType(1);
            			financeLog.setUserId(orderNew.getUserId());
            			financeLogMapper.addFinanceLog(financeLog);
            			
            		}else if(storeBusiness.getDeep() >= 2 && memberCommissionPercentageSecond <= 100){
            			double commissionSecond = commission * memberCommissionPercentageSecond / 100;
            			if(commissionSecond > 0){
            				params.put("commission", commissionSecond);
            				storeBusinessMapper.updateStoreIncome(storeBusinessSecond.getId(), params);
            				
            				//记录门店商家财务数据 订单提成收入
            				FinanceLog financeLog = new FinanceLog();
            				financeLog.setCash(commissionSecond);
            				financeLog.setCreateTime(time);
            				financeLog.setRelatedId(orderNew.getOrderNo());
            				financeLog.setStoreId(storeBusinessSecond.getId());
            				financeLog.setType(1);
            				financeLog.setUserId(orderNew.getUserId());
            				financeLogMapper.addFinanceLog(financeLog);
            				
            			}
            			if(commission - commissionSecond > 0){
            				params.put("commission", commission - commissionSecond);
            				storeBusinessMapper.updateStoreIncome(storeBusinessFirst.getId(), params);
            				
            				//记录门店商家财务数据 订单提成收入
            				FinanceLog financeLog = new FinanceLog();
            				financeLog.setCash(commission - commissionSecond);
            				financeLog.setCreateTime(time);
            				financeLog.setRelatedId(orderNew.getOrderNo());
            				financeLog.setStoreId(storeBusinessFirst.getId());
            				financeLog.setType(1);
            				financeLog.setUserId(orderNew.getUserId());
            				financeLogMapper.addFinanceLog(financeLog);
            			}
            			
            		}
            	}
            	
            	
//            	if(storeBusiness != null && userMember != null && storeBusiness.getCommissionPercentage() > 0){
//            		orderItemMapper.updateItemsCommssion(orderNew.getOrderNo() ,0.01 * storeBusiness.getCommissionPercentage(), time); //更新item表commission
////            		commission = 0.01 * storeBusiness.getCommissionPercentage() * orderNew.getTotalPay();
//            		commission = orderItemMapper.getItemsCommssionTotal(orderNew.getOrderNo());
//            		orderNew.setCommission(commission);
//            		if(storeBusiness.getDistributionStatus() == 0 && storeBusiness.getStatus() == 0 && userMember.getDistributionStatus() == 0 ){
//            			orderNew.setDistributionStatus(0);
//
//            		}
//            		orderNew.setBelongBusinessId(storeBusiness.getId());
//            		//更新订单提成金额等字段
//            		orderNewMapper.updateOrderCommission(orderNew, time);
//            		
//            		//更新storebusiness的cashIncome
//            		params.put("commission", commission);
//            		storeBusinessMapper.updateStoreIncome(storeBusiness.getId(), params);
//            		
//            		//记录门店商家财务数据 订单提成收入
//            		FinanceLog financeLog = new FinanceLog();
//            		financeLog.setCash(commission);
//            		financeLog.setCreateTime(time);
//            		financeLog.setRelatedId(orderNew.getOrderNo());
//            		financeLog.setStoreId(storeBusiness.getId());
//            		financeLog.setType(1);
//            		financeLog.setUserId(orderNew.getUserId());
//            		financeLogMapper.addFinanceLog(financeLog);
//            	}
        		//拆单
        		splitOrderNewDetail(orderNew , items, commission);
        		logger.info("<测试分享下单7newStatus>:"+newStatus.getIntValue());
        		logger.info("<测试分享下单8OrderStatus.PAID.getIntValue>:"+OrderStatus.PAID.getIntValue());
        		logger.info("<测试分享下单9oldStatus>:"+oldStatus.getIntValue());
        		//如果是将待支付订单改为已支付操作则进行发放支付收益
        		if(newStatus.getIntValue() == OrderStatus.PAID.getIntValue() && oldStatus.getIntValue() == OrderStatus.UNPAID.getIntValue() ){
        			//TODO 微信支付成功后进行发放分享注册收益
        	  	  	userSharedService.addOrderJiuCoin(orderNew.getOrderNo());
        		}
        		
        		
        	}else if(count == 1 && orderNew.getOrderType() == 2){// 2:当面付订单
        		//记录门店商家财务数据 订单提成收入
        		FinanceLog financeLog = new FinanceLog();
        		financeLog.setCash(orderNew.getCommission());
        		financeLog.setCreateTime(time);
        		financeLog.setRelatedId(orderNew.getOrderNo());
        		financeLog.setStoreId(orderNew.getBelongBusinessId());
        		financeLog.setType(2);
        		financeLog.setUserId(orderNew.getUserId());
        		financeLogMapper.addFinanceLog(financeLog);
        		
        		//更新storebusiness的cashIncome
        		params.put("availableBalance", orderNew.getTotalPay());
        		params.put("commission", orderNew.getTotalPay());
        		storeBusinessMapper.updateStoreIncome(orderNew.getBelongBusinessId(), params);
        	}
        	
        	OrderNewLog orderNewLog = new OrderNewLog();
        	orderNewLog.setUserId(orderNew.getUserId());
        	orderNewLog.setOrderNo(orderNew.getOrderNo());
        	orderNewLog.setNewStatus(newStatus);
        	orderNewLog.setOldStatus(oldStatus);
        	orderNewLog.setCreateTime(time);
        	orderNewLog.setUpdateTime(time);
        	orderNewLogMapper.addOrderLog(orderNewLog);
    	}
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderNewPayStatus(OrderNew orderNew, String paymentNo, PaymentType paymentType, OrderStatus newStatus,
    		OrderStatus oldStatus, long time) {
    	int count = orderNewMapper.updateOrderPayStatus(orderNew.getOrderNo(), paymentNo, paymentType, newStatus, oldStatus, time);
    	if (count != 1) {
    		String msg = "udpate order status error!, orderno:" + orderNew.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}
    	
//    	count = orderItemGroupMapper.updateOrderStatus(orderNew.getOrderNo(), newStatus, oldStatus, time);
//    	if (count <= 0) {
//    		String msg = "udpate group order status error!, order no:" + orderNew.getOrderNo();
//    		logger.error(msg);
//    		throw new IllegalStateException(msg);
//    	}       
    	
//    	OrderLog orderLog = new OrderLog();
//    	orderLog.setUserId(orderNew.getUserId());
//    	orderLog.setOrderId(orderNew.getId());
//    	orderLog.setNewStatus(newStatus);
//    	orderLog.setOldStatus(oldStatus);
//    	orderLog.setCreateTime(time);
//    	orderLogMapper.addOrderLog(orderLog);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(long orderNo, long time) {
    	//删除旧表
//        int count = orderMapper.cancelOrder(order.getId(), time);
//        if (count != 1) {
//            String msg = "cancel order error!, order id:" + order.getId();
//            logger.error(msg);
//            throw new IllegalStateException(msg);
//        }
//        orderItemMapper.cancelOrder(order.getId(), time);
//        orderItemGroupMapper.cancelOrder(order.getId(), time);
        
      //兼容性操作
//    	OrderNew orderNew = this.queryOrderNewFromOld(order);
    	OrderNew orderNew = this.getUserOrderNewByNo(String.valueOf(orderNo));
    	orderNewMapper.cancelOrder(orderNew.getOrderNo(), time, "");
    }
    
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderNew(OrderNew orderNew, long time) {
    	int count = orderNewMapper.cancelOrder(orderNew.getOrderNo(), time,  orderNew.getCancelReason());
    	if (count != 1) {
    		String msg = "cancel order error!, order id:" + orderNew.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}
   }
 

    
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderNewStatus(OrderNew order, OrderStatus newStatus, OrderStatus oldStatus, long time) {
    	int count = orderNewMapper.updateOrderStatus(order.getOrderNo(), newStatus, oldStatus, time);
    	if (count != 1) {
    		String msg = "udpate order status error!, order id:" + order.getOrderNo();
    		logger.error(msg);
    		throw new IllegalStateException(msg);
    	}
    	
    	// 添加待评价商品 add by dongzhong 20160530
    	if (newStatus == OrderStatus.SUCCESS && oldStatus == OrderStatus.DELIVER) {
    		Set<Long> ids = new HashSet<Long>();
    		ids.add(order.getOrderNo());
    		List<OrderItem> orderItems = orderItemMapper.getOrderNewItems(order.getUserId(), ids);
    		commentMapper.addComments(time, orderItems);        
    	}
    	
    	OrderNewLog orderNewLog = new OrderNewLog();
    	orderNewLog.setUserId(order.getUserId());
    	orderNewLog.setOrderNo(order.getOrderNo());
    	orderNewLog.setNewStatus(newStatus);
    	orderNewLog.setOldStatus(oldStatus);
    	orderNewLog.setCreateTime(time);
    	orderNewLog.setUpdateTime(time);
    	orderNewLogMapper.addOrderLog(orderNewLog);
    }
    
    public  OrderNewLog selectOrderLogByOrderNoAndStatus( long orderNo, OrderStatus orderStatus){
    	
    	return orderNewLogMapper.selectOrderLogByOrderNoAndStatus(orderNo, orderStatus);
    }
    

    public List<Coupon> getUserCouponList(long userId, OrderCouponStatus status ,PageQuery pageQuery) {
    	if(status == null){
    		status = OrderCouponStatus.UNUSED;
    	}
        return orderCouponMapper.getUserCoupons(userId, status.getIntValue(), pageQuery);
    }
    
    public List<Coupon> getUserCouponListHistory(long userId, PageQuery pageQuery) {
    	OrderCouponStatus status = OrderCouponStatus.USED;
    	return orderCouponMapper.getUserCoupons(userId, status.getIntValue(), pageQuery);
    	//return orderCouponMapper.getUserCouponsHistory(userId,  pageQuery);
    }
    
    public List<Coupon> getUserCouponListByOrderNo(long orderNo) {
    	return orderCouponMapper.getUserCouponListByOrderNo(orderNo);
    }
    
    public int userExchangeCouponByCode(long userId, String exchangeCode, long yjjNumber) {
    	long time = System.currentTimeMillis();
    	return orderCouponMapper.userExchangeCouponByCode(userId, exchangeCode, yjjNumber, time);
    }
    //不分页的优惠券列表
    public List<Coupon> getUserOrderCoupon(long userId, OrderCouponStatus status) {
    	return orderCouponMapper.getUserOrderCoupon(userId, status.getIntValue());
    }
    
    public int updateCouponUsed(String[] idArr, long orderNo) {
    	long time = System.currentTimeMillis();
    	
    	if(idArr != null && idArr.length > 0){
    		return orderCouponMapper.updateCouponUsed(idArr, orderNo, time, OrderCouponStatus.USED.getIntValue(), OrderCouponStatus.UNUSED.getIntValue() );
    		
    	}else {
    		return 0;
    	}
    }
    
    public int insertCouponUseLog(CouponUseLog couponUseLog) {
    
    	return couponUseLogMapper.insertCouponUseLog(couponUseLog);
    }
    
    public int updateCouponUnuse(long orderNo) {
    	long time = System.currentTimeMillis();
    	return orderCouponMapper.updateCouponUnuse(orderNo, time, OrderCouponStatus.UNUSED.getIntValue(), OrderCouponStatus.USED.getIntValue() );
    }
    
    public int updateAfterSellStatusAndNum(long orderNo) {
    	long time = System.currentTimeMillis();
    	return orderNewMapper.updateAfterSellStatusAndNum(orderNo, time);
    }
    
    public int getUserCouponCount(long userId, OrderCouponStatus status) {
    	if(status == null){
    		status = OrderCouponStatus.UNUSED;
    	}
    	return orderCouponMapper.getUserCouponCount(userId, status.getIntValue());

    }
    
    public int getUserCouponCountHistory(long userId) {
    	OrderCouponStatus status = OrderCouponStatus.USED;
    	return orderCouponMapper.getUserCouponCount(userId, status.getIntValue());
    	//return orderCouponMapper.getUserCouponCountHistory(userId);
    }
    
    public int getUserCouponCountByCode(String exchangeCode) {
    	
    	return orderCouponMapper.getUserCouponCountByCode(exchangeCode);
    }
    
    public StoreBusiness getBelongStoreBusiness(long userId) {
    	
    	return storeBusinessMapper.getBelongStoreBusinessByUserId(userId);
    }
    
    public UserMember getUserMemberById(long userId) {
    	
    	return userMemberMapper.getByUserId(userId);
    }
    
    
    //取优惠券限制文字
    public Map<Long, Coupon>  getCouponLimitContentMap(Map<Long, Coupon> couponMap) {
    	List<Coupon> couponList = new ArrayList<Coupon>();
    	for (Map.Entry<Long, Coupon> entry : couponMap.entrySet()) {
    		couponList.add(entry.getValue());
    	}
    	getCouponLimitContentList(couponList);
    	
    	return couponMap;
    }
	//取优惠券限制文字
	public List<Coupon>  getCouponLimitContentList(List<Coupon> couponList) {
	for(Coupon coupon : couponList){
		List<String> useTipsList = new ArrayList<String>();
		
		String useTips = "本券使用说明：\n";
		String useTag = "";
		if( coupon.getIsLimit() == 1){
			useTips += ("1、本券不可使用于优惠活动。\n");
		}else{
			useTips += ("1、本券可与优惠活动同时使用。\n");
		}

		if(coupon.getRangeType() == 0 || coupon.getRangeType() == 5 ){

			useTips += "2、平台所有商品均可使用。";
			
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
					String limitContent = "";
					if(limitIds.length() > 0 ){
						String[] idsArr = limitIds.split(",");
						if( coupon.getRangeType() == 1){
							useTag = "特定品类可用";
							coupon.setRelatedUrl(Constants.SERVER_URL + "/mobile/mainview/productbycategory186.json?categoryid=" + limitIds);
							List<Category> categorieList = categoryService.getCategoriesByIdsArr(idsArr);
							if(categorieList != null && categorieList.size() > 0){
								String categoryName = "";
								for(Category category : categorieList){
									categoryName += category.getCategoryName()+"，";
								}
								categoryName = categoryName.substring(0,categoryName.length()-1);
								limitContent = "2、仅限用于购买" + categoryName + "品类中的商品";
								if(coupon.getLimitMoney() > 0){
									limitContent += "，满" + coupon.getLimitMoney() + "可用。";
								}else{
									limitContent += "。";
								}
							}
						}else if( coupon.getRangeType() == 2){
							limitContent = "2、全场不限品类满" + limitIds + "元可以使用。";
							useTag = "满" + limitIds + "可用";
						}else if( coupon.getRangeType() == 4){
							useTag = "特定品牌可用";
							coupon.setRelatedUrl(Constants.SERVER_URL + "/mobile/mainview/productbycategory186.json?categoryid=40&propGroup=6:" + limitIds);
							List<Brand> brandList = brandService.getBrandListByArr(idsArr);
							if(brandList != null && brandList.size() > 0){
								String brandName = "";
								for(Brand brand : brandList){
									brandName += brand.getBrandName()+"，";
								}
								brandName = brandName.substring(0,brandName.length()-1);
								limitContent = "2、仅限用于购买" + brandName + "品牌的商品";
								if(coupon.getLimitMoney() > 0){
									limitContent += "，满" + coupon.getLimitMoney() + "可用。";
								}else{
									limitContent += "。";
								}
						}
						
					}
	    			
	    		}
					useTips += limitContent;
			}
		}
		if(coupon.getCoexist() == 1){
			useTips += "\n3、本券可以与其他优惠券同时使用。";
		}else {
			useTips += "\n3、本券不可以与其他优惠券同时使用。";
		}
		//范围类型 0: 通用, 1:分类, 2:限额订单, 4:品牌 ，5:免邮券
		useTipsList.add(useTips);
		useTipsList.add(useTag);
		coupon.setCouponUseTips(useTipsList);
    		
    	}
	return couponList;
}

	/**
	 * @param userId
	 * @param string
	 * @return
	 */
	public Object getUserOrderNewByNoAll(long userId, String orderNo) {
		return orderNewMapper.getUserOrderByNo(userId, orderNo);
	}
}
