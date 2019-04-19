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

import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.util.BizUtil;
import com.store.dao.mapper.RefundMapperNew;
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
import com.jiuyuan.dao.mapper.supplier.ProductNewMapper;
import com.jiuyuan.dao.mapper.supplier.RestrictionActivityProductMapper;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.ExpressInfo;
import com.jiuyuan.entity.OrderNewLog;
import com.jiuyuan.entity.ServiceTicket;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.logistics.LOWarehouse;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.service.common.ILOWarehouseNewService;
import com.jiuyuan.service.common.IOrderNewService;
import com.jiuyuan.service.common.IProductNewService;
import com.jiuyuan.service.common.IUserNewService;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.service.common.ShopGlobalSettingService;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.OrderProduct;
import com.store.entity.ShopPayTypeVO;
import com.store.entity.ShopStoreOrder;
import com.store.entity.ShopStoreOrderInfoVo;
import com.store.entity.ShopStoreOrderItem;
import com.store.entity.ShopStoreOrderItemNewVO;
import com.store.entity.ShopStoreOrderItemVO;
import com.store.enumerate.OrderType;

/**
 * @author LWS
 */
@Service
public class OrderInfoDelegator {
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
	private IProductNewService productNewService;
	@Autowired
	private ProductNewMapper productNewMapper;
    @Autowired
    private IUserNewService userNewService;

	@Autowired
	private RefundMapperNew refundMapperNew;

    public ShopStoreOrderInfoVo buildOrderInfoVo(StoreOrderNew orderObj,List<StoreOrderItemNew> orderItems) {
//		return orderService.getUserOrderNewDetailByNo(userDetail, orderNo);
    	ShopStoreOrderInfoVo orderInfoVo = new ShopStoreOrderInfoVo();
    	orderInfoVo.setOrderNo(orderObj.getOrderNo());
    	orderInfoVo.setOrderStatus(orderObj.getOrderStatus());
    	orderInfoVo.setCreateTime(DateUtil.format(orderObj.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
    	orderInfoVo.setExpiredTime(orderObj.getExpiredTime());
    	orderInfoVo.setParentId(orderObj.getParentId());
    	orderInfoVo.setRestrictionActivityProductId(orderObj.getRestrictionActivityProductId());//;
    	orderInfoVo.setTotalExpressMoney(orderObj.getTotalExpressMoney());
    	orderInfoVo.setHangUp(orderObj.getHangUp());
    	orderInfoVo.setTotalPay(orderObj.getTotalPay());
    	
    	orderInfoVo.setTotalMoney(orderObj.getTotalMoney());
    	orderInfoVo.setPaymentNo(orderObj.getPaymentNo());
    	orderInfoVo.setExpressInfo(orderObj.getExpressInfo());
    	
    	List<Map<String,Object>> itemMapList = new ArrayList<Map<String,Object>>();
    	for(StoreOrderItemNew item : orderItems){
    		Map<String,Object> itemMap = new HashMap<String,Object>();
    		itemMap.put("productId", item.getProductId());
    		itemMap.put("marketPrice", item.getMarketPrice());
    		itemMap.put("money", item.getMoney());
    		itemMap.put("buyCount", item.getBuyCount());
    		itemMap.put("skuSnapshot", item.getSkuSnapshot());
    		itemMap.put("colorStr", item.getSkuSnapshot().split("  ")[0].trim());
    		itemMap.put("sizeStr", item.getSkuSnapshot().split("  ")[1].trim());


    		ProductNew productNew = productNewMapper.selectById(item.getProductId());
    		String platformProductState = productNewService.getPlatformProductState(productNew);//  平台商品状态:0已上架、1已下架、2已删除
    		itemMap.put("platformProductState", platformProductState);
    		itemMap.put("clothesNumber",productNew.getClothesNumber() );//商品款号
    		itemMap.put("productName",productNew.getName() );//商品名称
    		itemMap.put("productMainImg",productNew.getMainImg() );//商品主图
    		itemMap.put("brandName",productNew.getBrandName() );//品牌名称
			//新添加的代码
			//获取该商品的orderno
			Long orderNo = item.getOrderNo();
			//获取到该商品的skuid
			Long skuId = item.getSkuId();
			RefundOrder refundOrder = refundMapperNew.selectRefundOrder(orderNo, skuId);
			if (refundOrder!=null){
				itemMap.put("refundStatus",refundOrder.getRefundStatus());//售后状态
				//'售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、
				// 6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)',
			}else {
				itemMap.put("refundStatus",0);//该订单没收售后信息,显示可以申请退款
			}
			itemMapList.add(itemMap);
    	}
    	orderInfoVo.setOrderItems(itemMapList);
//    	disableConfirmationReceipt
//    	refundMoney
		return orderInfoVo;
    }
	
    /**
	 * 获取到订单详情
	 * @param
	 * @param orderNo
	 * @return
	 */
	public Map<String, Object> getOrderInfo(long storeId, long orderNo) {
		long time1 = System.currentTimeMillis();
		Map<String, Object> data = new HashMap<String, Object>();
		StoreOrderNew storeOrder =  orderNewService.selectById(orderNo);
		List<StoreOrderItemNew> orderItems = orderNewService.getOrderNewItemsByOrderNO(storeId,orderNo);
		ShopStoreOrderInfoVo orderVo = this.buildOrderInfoVo(storeOrder,orderItems);
		data.put("order", orderVo);
		long time2 = System.currentTimeMillis();
		logger.info("time2:"+(time2-time1));//time2:1003

		/**
		 * 添加售后信息,根据订单号进行查询
		 */
		ArrayList list=new ArrayList();
		List<StoreRefundOrderActionLog> listNew=orderNewService.selectRefundLog(orderNo);
		if (listNew.size()!=0){
			for (StoreRefundOrderActionLog refundOrderActionLog : listNew) {
				Map<String,Object> map=new HashMap<>();
				map.put("time",refundOrderActionLog.getActionTime());
				map.put("name",refundOrderActionLog.getActionName());
				list.add(map);
			}
			data.put("userRefundLogList",list);
		}

		/**
		 * 平台代发的订单商家不能进行售后申请
		 */
		Integer type=orderService.selectOwnShop(orderNo);
		if (type==1){
			data.put("refundButton","不给显示申请售后按钮");
		}
		double deductibleAmount = 0;  //玖币抵扣金额
    	double discountAmount = 0;		//优惠金额
    	double orderTotalMoney = storeOrder.getTotalMoney();		//总金额
    	data.put("orderTotalMoney", orderTotalMoney);
    	for(StoreOrderItemNew item : orderItems ){
    		deductibleAmount += item.getTotalMarketPrice() - item.getTotalMoney();
    	}
    	discountAmount = storeOrder.getTotalMarketPrice() - deductibleAmount - storeOrder.getTotalPay();
		discountAmount = BizUtil.savepoint(discountAmount,2);
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
		
    	long createTime = storeOrder.getCreateTime();
    	long dealTime = createTime;
    	if(OrderStatus.getNameByValue(storeOrder.getOrderStatus()) != null && storeOrder.getOrderStatus() == 100){
    		dealTime = storeOrder.getUpdateTime();
    	}
    	data.put("dealTime", sdf.format(new Date(dealTime)));
    	if(storeOrder.getOrderStatus() > 0){
    		long payTime = 0; 
    		long shipTime = 0; 
    		
    		OrderNewLog orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(storeOrder.getOrderNo(), OrderStatus.PAID);
    		long time4 = System.currentTimeMillis();
    		logger.info("time4:"+(time4-time3));//time4:126
    		if(orderNewLog!=null){
    			payTime = orderNewLog.getCreateTime();		
    		}
    		orderNewLog = orderService.selectOrderLogByOrderNoAndStatus(storeOrder.getOrderNo(), OrderStatus.DELIVER);
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
    		if(storeOrder.getRefundStartTime()>0){
    			long buildSurplusSupplierAutoTakeTime = orderNewService.buildSurplusSupplierAutoTakeTime(storeOrder.getSendTime(),
    					storeOrder.getRefundStartTime(), storeOrder.getAutoTakeGeliveryPauseTimeLength());
        		data.put("autoConfirmTimeString", DateUtil.formatDuring(buildSurplusSupplierAutoTakeTime));
    		}
    		long time6 = System.currentTimeMillis();
    		logger.info("time6:"+(time6-time5));//time6:1
    	}
    	
    	data.put("deductibleAmount", deductibleAmount);
    	data.put("discountAmount", discountAmount);
    	long time7 = System.currentTimeMillis();
    	//取最新的一条物流信息
    	if(storeOrder.getOrderStatus() >= 50 && storeOrder.getOrderStatus() != 100){
	    	Map<String, String> result = this.getNewestTrackInfo(storeId, storeOrder.getOrderNo());
	    	data.put("trackContext", result.get("context"));
	    	data.put("trackTime", result.get("time"));
    	}
    	long time8 = System.currentTimeMillis();
		logger.info("time8:"+(time8-time7));//time8:1224
    	String shipMsg = "订单正在处理中，请耐心等待";
    	String closedMsg = "我不想买了";
    	if(storeOrder.getCancelReason() != null && storeOrder.getCancelReason().length() > 0){
    		closedMsg += "（"+storeOrder.getCancelReason()+"）";
    	}
    	String cancelingMsg = "请耐心等待系统处理";
    	String splitMsg = "本订单已按配送包裹拆分成多个订单";
    	
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
    	
    	ExpressInfo expressInfo = expressInfoService.getUserExpressInfoByOrderNo(storeId, storeOrder.getOrderNo());
    	data.put("ExpressInfo", expressInfo);
    	long time9 = System.currentTimeMillis();
		logger.info("time9:"+(time9-time8));//time9:12
		int totalBuyCount = storeOrder.getTotalBuyCount();//总购买件数
		double totalProductPrice = storeOrder.getTotalMoney();//商品总价格（所有商品的商品总价相加（商品总价=商品单价*购买数量））不包含邮费 订单金额原价总价，不包含邮费
		
		UserNew supplierUser = userNewService.getSupplierUserInfo(storeOrder.getSupplierId());
		int wholesaleCount = supplierUser.getWholesaleCount();//批发起定量 
		double wholesaleCost = supplierUser.getWholesaleCost();//批发起定额
		if(wholesaleCount==0 && wholesaleCost==0){//没有设置
			orderVo.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
		}else if((totalBuyCount < wholesaleCount) || (totalProductPrice < wholesaleCost)){
			orderVo.setMatchWholesaleLimit(0);;//是否符合混批限制：0不符合、1符合
		}else{
			orderVo.setMatchWholesaleLimit(1);;//是否符合混批限制：0不符合、1符合
		}
		long time10 = System.currentTimeMillis();
		logger.info("time10:"+(time10-time9));//time10:22
		
		//是否启用确认收货按钮
    	boolean disableConfirmationReceipt = false;
		Map<String,String> refundOrderMap = orderService.getRefundOrderStatus(orderNo,storeOrder.getOrderStatus());
 		String refundOrderStatus = refundOrderMap.get("refundOrderStatus");
 		if("售后中".equals(refundOrderStatus)){
 			disableConfirmationReceipt = true;
 		}
 		orderVo.setOrderItemStatus(refundOrderStatus);
 		orderVo.setRefundOrderId(refundOrderMap.get("refundOrderId"));
 		if("申请退款".equals(refundOrderStatus) || "申请售后".equals(refundOrderStatus)){
 			//有售后按钮
 			orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.applyAfterSaleButton);
 		}else{
 			//无售后按钮
 			orderVo.setIsApplyAfterSaleButton(ShopStoreOrder.unApplyAfterSaleButton);
 		}
 		//设置是否启用确认收货按钮
 		orderVo.setDisableConfirmationReceipt(disableConfirmationReceipt);
		
		
    	return data;
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
	
	
	
}