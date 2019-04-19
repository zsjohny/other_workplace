/**
 * 
 */
package com.store.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.DateConstants;
import com.jiuyuan.constant.GlobalSettingName;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.newentity.BrandNew;
import com.jiuyuan.entity.newentity.RestrictionActivityProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.UserNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.ILOWarehouseNewService;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.OrderProduct;
import com.store.entity.ShopPayTypeVO;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemVO;
import com.store.enumerate.OrderType;

/**
 * @author LWS
 */
@Service
public class OrderDelegator {
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderFacade orderFacade;
    
    @Autowired
    private ShopGlobalSettingService globalSettingService;
    
    @Autowired
    private ExpressInfoService expressInfoService;
    
    @Autowired
    private ExpressService expressService;
    
    @Autowired
    private MemcachedService memcachedService;
    
    @Autowired
    private ILOWarehouseNewService lOWarehouseService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
	private IOrderNewService orderNewService;
    
    @Autowired
    private SupplierOrderMapper supplierOrderMapper;
    
    @Autowired
    private RestrictionActivityProductMapper restrictionActivityProductMapper;
    
    @Autowired
    private IUserNewService userNewService;
    
    /**
     * 从新订单表中取列表
     * @param orderStatus
     * @param pageQuery
     * @param userDetail
     * @param type 1:进货订单列表；2：供应商订单列表
     * @return
     */
	public Map<String, Object> getNewOrderList(OrderStatus orderStatus, PageQuery pageQuery,
			UserDetail<StoreBusiness> userDetail, int type) {
    	long userId = userDetail.getId();
    	long time1 = System.currentTimeMillis();
    	long supplierId = userDetail.getUserDetail().getSupplierId();
    	if(supplierId<0){
    		throw new RuntimeException("供应商ID为空，请确认");
    	}
    	int totalCount = 0;
    	List<ShopStoreOrder> orderNews = null ;
    	PageQueryResult pageQueryResult;
    	Map<String, Object> result = new Hashtable<String, Object>();
    	Set<Long> orderNOs = new HashSet<Long>();
    	List<ShopStoreOrderItemVO> orderItemList;
    	Set<Long> orderNoMap ;
    	Map<Long, List<ShopStoreOrder>> childOrderMap;
    	Map<Long, List<OrderProduct>> orderProductMap;
    	Map<Long, List<ShopStoreOrderItemVO>> orderItemsMap;
    	List<ShopStoreOrder> resultOrderList2 = new ArrayList<ShopStoreOrder>();
    	result.put("tip", "温馨提示：商品退换货请联系客服，给您带来不便敬请谅解，谢谢");
    	long time2 = System.currentTimeMillis();
		logger.info("time2："+(time2-time1));
    	//取全部状态订单，包含母订单
    	if(orderStatus.getIntValue() == 5){
    		
    		if(type==1){
    			totalCount = orderService.getUserNewOrderCount(userId, orderStatus.getIntValue());
    			orderNews = orderService.getUserOrdersNew(userId, orderStatus.getIntValue(), pageQuery);
    			
    		}else if(type==2){
    			totalCount = orderService.getSupplierOrderCount(supplierId, orderStatus.getIntValue());
    			orderNews = orderService.getSupplierOrdersNew(supplierId, orderStatus.getIntValue(), pageQuery);
    		}
    		long time3 = System.currentTimeMillis();
			logger.info("time3："+(time3-time2));
    		
    		
    		if (CollectionUtils.isEmpty(orderNews)) {
    			return result;
    		}
    		
    		//获取某个状态的用户的所有订单(order表)
    		for (ShopStoreOrder order : orderNews) {
    			orderNOs.add(order.getOrderNo());
    		}
    		orderItemsMap = orderFacade.getOrderNewItemVOMap(userDetail, orderNOs);//拆分母订单无法取到对于ITEMS
    		long time4 = System.currentTimeMillis();
			logger.info("time4："+(time4-time3));
    		
    		childOrderMap = orderFacade.getChildOrderMap(userDetail, orderNOs);//
    		long time5 = System.currentTimeMillis();
			logger.info("time5："+(time5-time4));
    		orderProductMap = orderFacade.getOrderProductMap(userDetail, orderNOs);//
    		long time6 = System.currentTimeMillis();
			logger.info("time6："+(time6-time5));
    		for (ShopStoreOrder order : orderNews) {
    			List<ShopStoreOrderItemVO> orderItems = orderItemsMap.get(order.getOrderNo());
    			//是否启用确认收货按钮
    	    	boolean disableConfirmationReceipt = false;
    	    	
    	    	//如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
    	    	long restrictionActivityProductId = order.getRestriction_activity_product_id();
    	    	String platformProductState = "-1";
    	    	if(restrictionActivityProductId>0){
    	    		RestrictionActivityProduct restrictionActivityProduct = restrictionActivityProductMapper.selectById(restrictionActivityProductId);
    	    		int productStatus = restrictionActivityProduct.getProductStatus();
    	    		//限购活动商品的状态0:待上架;1:已上架;2:已下架;3:已删除
    	    		//平台商品状态:0已上架、1已下架、2已删除
					if(productStatus==2){
						platformProductState = "1";
					}
    	    	}
    			
    			if(orderItems != null && orderItems.size() > 0){
    				int count = 0 ;
    				for(ShopStoreOrderItemVO orderItem : orderItems){
//    					//分别获取订单sku状态
//    					Map<String,String> refundOrderMap = orderService.getOrderItemStatus(orderItem.getId(),order.getOrderStatus());
//    					String orderItemStatus = refundOrderMap.get("refundOrderMap");
//    	        		if("售后中".equals(orderItemStatus)){
//    	        			disableConfirmationReceipt = true;
//    	        		}
//    	        		orderItem.setOrderItemStatus(orderItemStatus);
//    	        		if("申请退款".equals(orderItemStatus) || "申请售后".equals(orderItemStatus)){
//    	        			//有售后按钮
//    	        			orderItem.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.applyAfterSaleButton);
//    	        		}else{
//    	        			//无售后按钮
//    	        			orderItem.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.unApplyAfterSaleButton);
//    	        		}
    					//如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
    	        		if("1".equals(platformProductState)){
    	        			orderItem.setPlatformProductState(platformProductState);
    	        		}
    					count += orderItem.getBuyCount();
    				}
    				
    				//分别获取订单状态
    		 		Map<String,String> refundOrderMap = orderService.getRefundOrderStatus(order.getOrderNo(),order.getOrderStatus());
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
    				
    				order.setTotalBuyCount(count);
    				//设置是否启用确认收货按钮
    				order.setDisableConfirmationReceipt(disableConfirmationReceipt);
    				
    				//获取未售后时自动确认收货倒计时毫秒值
    				long payTime = order.getPayTime();
    				if(payTime > 0 && order.getOrderStatus()==OrderStatus.DELIVER.getIntValue()){
    	    			long autoConfirmTime = payTime + 14*24*60*60*1000 ;
    	    			order.setAutoConfirmTime(autoConfirmTime);
    	    		}
    				//获取申请售后时自动确认收货暂停倒计时
    	    		if(order.getRefund_start_time()>0){
    	    			long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(order.getSendTime(), 
    	     					order.getRefund_start_time(), order.getAuto_take_delivery_pause_time_length());
    	    			order.setAutoConfirmTimeString(DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
    	    		}
    			}
    			if(orderItems != null){ 			
    				order.setOrderItems(new ArrayList<ShopStoreOrderItem>(orderItems));
    			}
    			List<ShopStoreOrder> childOrderList = childOrderMap.get(order.getOrderNo());
    			List<OrderProduct> orderProductList = orderProductMap.get(order.getOrderNo());
    			order.setOrderProductList(orderProductList);
//    			if(childOrderList == null || childOrderList.size() == 0 || (childOrderList != null && childOrderList.size() > 0 && orderStatus == null) ){
//    			}
    			ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), order.getOrderNo());

    			if(expressInfo==null){
    				order.setExpressOrderNo("");
    				order.setExpressCnName("");
    			}else{
        			//根据快递公司英文名获取对应的中文名
        			String expressCnName = expressInfoService.getExpressChineseNameByExpressSupplier(expressInfo.getExpressSupplier());
    				order.setExpressOrderNo(expressInfo.getExpressOrderNo());
    				order.setExpressCnName(expressCnName);
    			}
    			
    			resultOrderList2.add(order);
    				
    			if (childOrderList != null && childOrderList.size() > 0) {
    				totalCount += childOrderList.size();
    				order.setChildOrderList(new ArrayList<ShopStoreOrder>(childOrderList));
    				//插入子订单
    				resultOrderList2.addAll(childOrderList);
    				orderNoMap = new HashSet<Long>();
    				for (ShopStoreOrder orderNew : childOrderList) {
    					orderNoMap.add(orderNew.getOrderNo());
    				}
    				orderItemList = orderService.getOrderNewItemsVO(userDetail, orderNoMap);
    				order.setOrderItems(new ArrayList<ShopStoreOrderItem>(orderItemList));
    			}
    			
    		}
    		long time7 = System.currentTimeMillis();
			logger.info("time7："+(time7-time6));
    		pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	} else{
    		long time10 = System.currentTimeMillis();
    		//取某状态订单，不包含母订单
    		if(type==1){
				totalCount = orderService.getUserNewOrderCountWithoutParent(userId, orderStatus.getIntValue());
	    		orderNews = orderService.getUserOrdersNewWithoutParent(userId, orderStatus.getIntValue(), pageQuery);
			}else if(type==2){
				totalCount = orderService.getSupplierOrderCount(supplierId, orderStatus.getIntValue());
	    		orderNews = orderService.getSupplierOrdersNew(supplierId, orderStatus.getIntValue(), pageQuery);
			}
    		long time11 = System.currentTimeMillis();
			logger.info("time11："+(time11-time10));
    		pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    		if (CollectionUtils.isEmpty(orderNews)) {
    			return result;
    		}
    		
    		//获取某个状态的用户的所有订单(order表)
    		for (ShopStoreOrder order : orderNews) {
    			orderNOs.add(order.getOrderNo());
    		}
    		orderItemsMap = orderFacade.getOrderNewItemVOMap(userDetail, orderNOs);
    		orderProductMap = orderFacade.getOrderProductMap(userDetail, orderNOs);//
    		long time12 = System.currentTimeMillis();
			logger.info("time12："+(time12-time11));
    		
    		for (ShopStoreOrder order : orderNews) {
    			List<ShopStoreOrderItemVO> orderItems = orderItemsMap.get(order.getOrderNo());
    			//是否启用确认收货按钮
    	    	boolean disableConfirmationReceipt = false;
    	    	
//    	    	//判断有是否混批限制
//    	    	int totalBuyCount = order.getTotalBuyCount();//总购买件数
//    			double totalProductPrice = order.getTotalMoney();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
//    			
//    			UserNew supplierUser = userNewService.getSupplierUserInfo(order.getSupplierId());
//    			int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量 
//    			double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
//    			if((totalBuyCount < wholesaleCount) || (totalProductPrice < wholesaleCost)){
//    				order.setMatchWholesaleLimit(0);//是否符合混批限制：0不符合、1符合
//    			}else{
//    				order.setMatchWholesaleLimit(1);//是否符合混批限制：0不符合、1符合
//    			}
    	    	
    	    	//如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
    	    	long restrictionActivityProductId = order.getRestriction_activity_product_id();
    	    	String platformProductState = "-1";
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
    			
    			if(orderItems != null && orderItems.size() > 0){
    				int count = 0 ;
    				for(ShopStoreOrderItemVO orderItem : orderItems){
//    					//分别获取订单sku状态
//    					Map<String,String> refundOrderMap = orderService.getOrderItemStatus(orderItem.getId(),order.getOrderStatus());
//    					String orderItemStatus = refundOrderMap.get("refundOrderMap");
//    	        		if("售后中".equals(orderItemStatus)){
//    	        			disableConfirmationReceipt = true;
//    	        		}
//    	        		orderItem.setOrderItemStatus(orderItemStatus);
//    	        		if("申请退款".equals(orderItemStatus) || "申请售后".equals(orderItemStatus)){
//    	        			//有售后按钮
//    	        			orderItem.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.applyAfterSaleButton);
//    	        		}else{
//    	        			//无售后按钮
//    	        			orderItem.setIsApplyAfterSaleButton(ShopStoreOrderItemVO.unApplyAfterSaleButton);
//    	        		}
    					//如果是限购活动商品，取限购活动商品的状态覆盖当前商品状态
    	        		if(!"-1".equals(platformProductState)){
    	        			orderItem.setPlatformProductState(platformProductState);
    	        		}
    					count += orderItem.getBuyCount();
    				}
    				order.setTotalBuyCount(count);
    				
    				//分别获取订单sku状态
    		 		Map<String,String> refundOrderMap = orderService.getRefundOrderStatus(order.getOrderNo(),order.getOrderStatus());
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
    				
    				//获取未售后时自动确认收货倒计时毫秒值
    				long payTime = order.getPayTime();
    				if(payTime > 0 && order.getOrderStatus()==OrderStatus.DELIVER.getIntValue()){
    	    			long autoConfirmTime = payTime + 14*24*60*60*1000 ;
    	    			order.setAutoConfirmTime(autoConfirmTime);
    	    		}
    				//获取申请售后时自动确认收货暂停倒计时
    	    		if(order.getRefund_start_time()>0){
    	    			long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(order.getSendTime(), 
    	     					order.getRefund_start_time(), order.getAuto_take_delivery_pause_time_length());
    	    			order.setAutoConfirmTimeString(DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
    	    		}
    			}
    			if(orderItems != null){ 			
    				order.setOrderItems(new ArrayList<ShopStoreOrderItem>(orderItems));
    			}
    			
    			
    			List<OrderProduct> orderProductList = orderProductMap.get(order.getOrderNo());
    			order.setOrderProductList(orderProductList);
    			
    			ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), order.getOrderNo());
    			
    			if(expressInfo==null){
    				order.setExpressOrderNo("");
    				order.setExpressCnName("");
    			}else{
        			//根据快递公司英文名获取对应的中文名
        			String expressCnName = expressInfoService.getExpressChineseNameByExpressSupplier(expressInfo.getExpressSupplier());
    				order.setExpressOrderNo(expressInfo.getExpressOrderNo());
    				order.setExpressCnName(expressCnName);
    			}
    			
    			resultOrderList2.add(order);
    			
    		}
    		long time13= System.currentTimeMillis();
			logger.info("time13："+(time13-time12));
    	}
    	
    	
    	
    	result.put("orderlist", resultOrderList2);
    	
    	result.put("pageQuery", pageQueryResult);
    	
  
    	
    	List<String> cancelReasonList = new ArrayList<String>(); 
    	cancelReasonList.add("我不想买了");
    	cancelReasonList.add("信息填写错误");
    	cancelReasonList.add("其它");
    	result.put("cancelReasonList", cancelReasonList);
    	
    	return result;
    }
	
	/**
	 * 获取到订单详情
	 * @param userDetail
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getOrderDetail(UserDetail<StoreBusiness> userDetail, String orderNo) {
		long time1 = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		ShopStoreOrder order = this.getOrderNewDetail(userDetail, orderNo);
		long time2 = System.currentTimeMillis();
		logger.info("time2:"+(time2-time1));//time2:1003
		String refundOrderStatus = order.getOrderItemStatus();
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
        calendar.set(2018,1,10,0,0,0);//2月10日
        long startMillis = calendar.getTimeInMillis();
        calendar.clear();
        calendar.set(2018,1,10,0,0,0);//3月3日
        long endMillis = calendar.getTimeInMillis();
        long timeNow = System.currentTimeMillis();
        if(timeNow>=startMillis && timeNow<=endMillis && ("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus))){
        	data.put("refundOrderText", "亲爱的客户：目前工厂处于休假状态，平台暂停受理退款退货服务，于2018年3月3日重新开始受理售后，为您带来的不便敬请谅解。");
 		}else{
 			data.put("refundOrderText", "");
 		}
    	if(order!=null){
    		data.put("order", order);
    	}
    	double deductibleAmount = 0;  //玖币抵扣金额
    	double discountAmount = 0;		//优惠金额
    	double orderTotalMoney = order.getTotalMoney();		//总金额
    	data.put("orderTotalMoney", orderTotalMoney);
    	for(ShopStoreOrderItem item : order.getOrderItems() ){
    		deductibleAmount += item.getTotalMarketPrice() - item.getTotalMoney();
    	}
    	discountAmount = order.getTotalMarketPrice() - deductibleAmount - order.getTotalPay();
    	
    	if(discountAmount < 0){
    		discountAmount = 0;
    	}
    	long time3 = System.currentTimeMillis();
		logger.info("time3:"+(time3-time2));//time3:2
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
    	if(OrderStatus.getNameByValue(order.getOrderStatus()) != null && order.getOrderStatus() == 100){
    		dealTime = order.getUpdateTime();
    	}
    	data.put("dealTime", sdf.format(new Date(dealTime)));
    	if(order.getOrderStatus() > 0){
    		long payTime = 0; 
    		long shipTime = 0; 
    		
    		OrderNewLog orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(order.getOrderNo(), OrderStatus.PAID);
    		long time4 = System.currentTimeMillis();
    		logger.info("time4:"+(time4-time3));//time4:126
    		if(orderNewLog!=null){
    			payTime = orderNewLog.getCreateTime();		
    		}
    		orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(order.getOrderNo(), OrderStatus.DELIVER);
    		long time5 = System.currentTimeMillis();
    		logger.info("time5:"+(time5-time4));//time5:13
    		if(orderNewLog!=null){
    			shipTime = orderNewLog.getCreateTime();	
    			data.put("shipTime", sdf.format(new Date(shipTime)));
    		}
    		if(payTime > 0){
    			long autoConfirmTime = payTime + minuteTime * confirmMinutes ;
    			data.put("autoConfirmTime", autoConfirmTime);
    			data.put("payTime", sdf.format(new Date(payTime)));
    		} 
    		if(order.getRefund_start_time()>0){
    			long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(order.getSendTime(), 
     					order.getRefund_start_time(), order.getAuto_take_delivery_pause_time_length());
        		data.put("autoConfirmTimeString", DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
    		}
    		long time6 = System.currentTimeMillis();
    		logger.info("time6:"+(time6-time5));//time6:1
    	}
//    	else if(order.getOrderStatus().getIntValue() == 0){
//    		long expireTime = createTime + payExpMinutes * minuteTime;
//    		data.put("expireTime", expireTime);
//    	}
    	
    	data.put("deductibleAmount", deductibleAmount);
    	data.put("discountAmount", discountAmount);
    	long time7 = System.currentTimeMillis();
    	//取最新的一条物流信息
    	if(order.getOrderStatus() >= 50 && order.getOrderStatus() != 100){
    		
	    	Map<String, String> result = this.getNewestTrackInfo(userDetail.getId(), order.getOrderNo());
	    	data.put("trackContext", result.get("context"));
	    	data.put("trackTime", result.get("time"));
    	}
    	long time8 = System.currentTimeMillis();
		logger.info("time8:"+(time8-time7));//time8:1224
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
    	data.put("refundTips", "(退款金额不含收益部分，收益部分请在财务中提现)");
    	
    	ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), order.getOrderNo());
    	data.put("ExpressInfo", expressInfo);
    	long time9 = System.currentTimeMillis();
		logger.info("time9:"+(time9-time8));//time9:12
		int totalBuyCount = order.getTotalBuyCount();//总购买件数
		double totalProductPrice = order.getTotalMoney();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
		
		UserNew supplierUser = userNewService.getSupplierUserInfo(order.getSupplierId());
		int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量 
		double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
		if(wholesaleCount==0 && wholesaleCost==0){//没有设置
			order.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
		}else if((totalBuyCount < wholesaleCount) || (totalProductPrice < wholesaleCost)){
			order.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
		}else{
			order.setMatchWholesaleLimit(1);;//是否符合混批限制：0不符合、1符合
		}
		long time10 = System.currentTimeMillis();
		logger.info("time10:"+(time10-time9));//time10:22
    	return data;
    }
	public ShopStoreOrder getOrderNewDetail(UserDetail userDetail, String orderNo) {
		return orderService.getUserOrderNewDetailByNo(userDetail, orderNo);
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
    		//System.out.println(((JSONObject)expressData.get("result")).get("data").toString());
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

	public JsonResponse cancelOrderNew(String orderNo, UserDetail userDetail,  String cancelReason, ClientPlatform clientPlatform) {
    	JsonResponse jsonResponse = new JsonResponse();
    	long userId = userDetail.getId();
    	ShopStoreOrder order = orderService.getUserOrderNewByNo(userId, orderNo);
    	if (order == null) {
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN);
    	}
    	order.setCancelReason(cancelReason);
    	orderFacade.cancelOrderNew(order,clientPlatform.getVersion());
    	return jsonResponse.setSuccessful();
    }

	public Map<String, Object> orderPayChoose(UserDetail userDetail) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<ShopPayTypeVO>  payTypeVOList= new ArrayList<ShopPayTypeVO>();
    	ShopPayTypeVO payTypeVO;
    	payTypeVO = new ShopPayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-www.oss-cn-hangzhou.aliyuncs.com/FE19F80B-5C73-478D-B698-8DD372E25B0C.jpg");
    	payTypeVO.setName("余额支付");
    	payTypeVO.setType(4);
    	payTypeVOList.add(payTypeVO);
    	
    	payTypeVO = new ShopPayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/weixin.jpg");
    	payTypeVO.setName("微信支付");
    	payTypeVO.setType(3);
    	payTypeVOList.add(payTypeVO);
    	
    	payTypeVO = new ShopPayTypeVO();
    	payTypeVO.setIcon("http://yjj-img-main.oss-cn-hangzhou.aliyuncs.com/pay/zhifubao.jpg");
    	payTypeVO.setName("支付宝支付");
    	payTypeVO.setType(2);
    	payTypeVOList.add(payTypeVO);
    	data.put("paylist", payTypeVOList);
    	return data;
    }

	 public JsonResponse getExpressInfo(UserDetail userDetail, long orderNo) {
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
        	
        	ShopStoreOrder  orderNew = orderService.getUserOrderNewByNo(userDetail.getId(), orderNo + "");
        	
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
        	if(orderNew.getOrderType() == OrderType.AFTERSALE.getIntValue()){
        		ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail, orderNo);
        		if(serviceTicket != null){
        			expressNumber = serviceTicket.getSellerExpressNo();
        			itemNum = serviceTicket.getApplyReturnCount();	
        		}
        	}else{
        		ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userDetail.getId(), orderNo);
        		if(info != null){
        			expressNumber = info.getExpressOrderNo();
        		}        	
        		List<ShopStoreOrderItem> items = orderService.getOrderNewItemsByOrderNO(userDetail.getId(), orderNo);
        		if(items != null && items.size() > 0){
        			for(ShopStoreOrderItem item: items){
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

}