/**
 * 
 */
package com.yujj.web.controller.delegate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.apache.bcel.generic.ReturnaddressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuyuan.constant.Express.ExpressSupport;
import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.constant.order.ServiceTicketApplyReason;
import com.jiuyuan.constant.order.ServiceTicketReceiveStatus;
import com.jiuyuan.constant.order.ServiceTicketStatus;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.ExpressInfoService;
import com.yujj.business.service.ExpressService;
import com.yujj.business.service.OrderService;
import com.yujj.business.service.ProductSKUService;
import com.yujj.business.service.YJJUserAddressService;
import com.yujj.dao.mapper.ServiceTicketMapper;
import com.yujj.entity.account.UserDetail;
import com.jiuyuan.entity.account.Address;
import com.jiuyuan.entity.account.EnumSelection;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.yujj.entity.afterSale.FinanceTicket;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.entity.order.ExpressInfo;
import com.yujj.entity.order.OrderItem;
import com.yujj.entity.order.OrderNew;
import com.yujj.entity.product.ProductSKU;

/**
 * @author LWS
 */
@Service
public class AfterSaleDelegator {
    private static final Logger logger = LoggerFactory.getLogger("PAYMENT");
    
    @Autowired
    private ServiceTicketMapper serviceTicketMapper;
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private YJJUserAddressService userAddressService;
    
    @Autowired
    private AfterSaleService afterSaleService;
    
    
    @Autowired
    private ProductSKUService productSKUService;
    
    
    @Autowired
    private ExpressService expressService;
    
    @Autowired
    private ExpressInfoService expressInfoService;

    
    
    public Map<String, Object> getServiceTicketDetail(long userId , long id) {
    	return getServiceTicketDetail( userId ,  id, false); 
    }
    /**
     * 
     * @param userId
     * @param id
     * @param isNumToId    为了售后服务详情中商品款号改为商品ID临时改的，为了苹果不发版，后续注意联合苹果端和安卓端进行商品款号修改为商品ID
     * @return
     */
    public Map<String, Object> getServiceTicketDetail(long userId , long id,boolean isNumToId) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userId, id,isNumToId);
    	List<Address> addresses = userAddressService.getUserAddresses(userId);
    	data.put("serviceTicket", serviceTicket);
    	data.put("addresses", addresses);

    	if(serviceTicket != null){

    		if(serviceTicket.getOrderItem() != null && serviceTicket.getOrderItem().getSkuId() > 0){
    			ProductSKU productSKU = productSKUService.getProductSKU(serviceTicket.getOrderItem().getSkuId());
    			data.put("productSKU", productSKU);	
    		}
    		
    		String showStatusName = serviceTicket.getStatusName();
	    	if(serviceTicket.getStatus() == 5  ){
	    		if(serviceTicket.getProcessResult() == 3){
	    			showStatusName = "换货处理中";    			
	    		}else{
	    			showStatusName = "待退款"; 
	    			
	    		}
	    	}else if(serviceTicket.getStatus() == 6  ){
	    		if(serviceTicket.getProcessResult() == 3){
	    			showStatusName = "已发货";    
	    			//取最新的一条物流信息
    		    	Map<String, String> result = this.getNewestTrackInfo(userId, id);
    		    	data.put("trackContext", result.get("context"));
    		    	data.put("trackTime", result.get("time"));
	    	    	
	    		}else{
	    			showStatusName = "退款成功";  
	    			FinanceTicket financeTicket = afterSaleService.getFinanceTicketById(id);
	    			data.put("financeTicket", financeTicket);
	    			
	    		}
	    	}else if(serviceTicket.getStatus() == 7  ){
	    		if(serviceTicket.getProcessResult() == 3){
	    			showStatusName = "换货成功";    
	    			//取最新的一条物流信息
    		    	Map<String, String> result = this.getNewestTrackInfo(userId, id);
    		    	data.put("trackContext", result.get("context"));
    		    	data.put("trackTime", result.get("time"));
	    	    	
	    		}
	    	}
	    	data.put("showStatusName", showStatusName);
	    	
	    	
	    	//重新申请售后标记
	    	int afterSaleFlag = 0;
	    	int afterSaleCount = afterSaleService.getItemAfterSaleValidCount(userId, serviceTicket.getOrderItemId() , serviceTicket.getOrderNo());
	    	if(serviceTicket.getOrderItem() != null && serviceTicket.getOrderItem().getBuyCount() > afterSaleCount){
	    		afterSaleFlag = 1;
	    	}
	    	data.put("afterSaleFlag", afterSaleFlag);
	    	if(serviceTicket.getProcessOrderNo() > 0){
	    		OrderNew orderNew = orderService.getUserOrderNewByNo(userId, serviceTicket.getProcessOrderNo() + "");
	    		data.put("processOrder", orderNew);
	    	}
	    	
    	}
    	//驳回原因
    	String rejectReason = "提交的资料不全，请重新申请退货。";
    	data.put("rejectReason", rejectReason);
    	//退款温馨提醒
    	String refundTips = "已退还玖币，您的人民币退款将于1到3个工作日退还到支付账户，请耐心等待。";
    	data.put("refundTips", refundTips);
    	//换货破损说明
    	String replaceBrokenTips = "经审核，因退还的商品有破损，按折旧处理退还金额。";
    	data.put("replaceBrokenTip", replaceBrokenTips);
    	
    	//换货说明
    	String replaceDeclare = "换货商品已经发货，请注意查收。";
    	data.put("replaceDeclare", replaceDeclare);
    	
    	//退款温馨提醒
    	String refundDeclare = "退货汇款（银行或者第三方支付平台到款时间一般为1到3个工作日，请耐心等待，查收退款。）";
    	data.put("refundDeclare", refundDeclare);
    	
    
    	
    	
        return data;
    }
    
    public Map<String, Object> getUserServiceTicketList(PageQuery pageQuery, long userId , String orderSearchNo) {
    	PageQueryResult pageQueryResult;
    	int totalCount = afterSaleService.countUserServiceTicketList(userId, orderSearchNo);
    	pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);
    	Map<String, Object> data = new HashMap<String, Object>();
    	List<ServiceTicket> serviceTicketList = afterSaleService.getUserServiceTicketList(pageQuery, userId, orderSearchNo);
    	data.put("serviceTicketList", serviceTicketList);
    	data.put("pageQuery", pageQueryResult);
    	return data;
    }
    
    public ServiceTicket getServiceTicketDetailById(long userId , long id) {
    	
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, id);

    	return serviceTicket;
    }
    
    public Map<String, Object> getBuyerShipConfirmInfo(long userId , long serviceId) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, serviceId);
    	if(serviceTicket != null && serviceTicket.getOrderNo() > 0){
    		OrderNew orderNew = orderService.getUserOrderNewByNo(userId, serviceTicket.getOrderNo() + "");
    		data.put("orderNew", orderNew);
    		OrderItem orderItem = orderService.getOrderNewItemById(userId, serviceTicket.getOrderItemId());
    		data.put("orderItem", orderItem);
    	}
    	data.put("serviceTicket", serviceTicket);
    	
    	
    	EnumSelection enumSelection = new EnumSelection();
    	List<EnumSelection> expressCompany = new ArrayList<EnumSelection>();
    	for( ExpressSupport item : ExpressSupport.getAllList()){
    		enumSelection = new EnumSelection();
    		enumSelection.setOption(item.getChnName());
    		enumSelection.setValue(item.getValidName());
    		expressCompany.add(enumSelection);
    	}
    	
//    	List<String> expressCompany= new ArrayList<String>(); 
//    	expressCompany.add("圆通快递");
//    	expressCompany.add("申通快递");
//    	expressCompany.add("顺丰快递");
//    	expressCompany.add("百世汇通");
//    	expressCompany.add("EMS");
    	data.put("expressCompany", expressCompany);
    	
    	return data;
    }
    
    public Map<String, Object> buyerShipConfirmCommit(long serviceId, String buyerExpressCom, String buyerExpressNo, long userId) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, serviceId);
    	long sysTime =System.currentTimeMillis();
    	String buyerMemo = "";
    	int count = serviceTicketMapper.updateServiceBuyerExpress(serviceId, buyerExpressCom , buyerExpressNo , ServiceTicketStatus.BUYER_SHIPPING.getIntValue(), ServiceTicketStatus.CONFIRMING.getIntValue(),buyerMemo , sysTime);
    	if(count == 1){
    		data.put("result", "success");
    	}else{
    		data.put("result", "fail");
    	}
    	data.put("serviceTicket", serviceTicket);
    	return data;
    }
    
    public Map<String, Object> buyerExpressReject(long serviceId, long userId) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, serviceId);
    	long sysTime =System.currentTimeMillis();
    	String buyerExpressCom = "";
    	String buyerExpressNo = "";
    	ExpressInfo info = expressInfoService.getUserExpressInfoByOrderNo(userId, serviceTicket.getOrderNo());
		if(info != null){
			buyerExpressNo = info.getExpressOrderNo();
			buyerExpressCom = info.getExpressSupplier();
		} 
		String buyerMemo = "确认拒签。";
    	
		int count = serviceTicketMapper.updateServiceBuyerExpress(serviceId, buyerExpressCom , buyerExpressNo , ServiceTicketStatus.BUYER_SHIPPING.getIntValue(), ServiceTicketStatus.CONFIRMING.getIntValue(), buyerMemo, sysTime);
    	if(count == 1){
    		data.put("result", "success");
    	}else{
    		data.put("result", "fail");
    	}
    	data.put("serviceTicket", serviceTicket);
    	return data;
    }
    
    public Map<String, Object> buyerExchangeReceived(long serviceId, long userId) {
    	
    	Map<String, Object> data = new HashMap<String, Object>();
    	ServiceTicket serviceTicket = serviceTicketMapper.getServiceTicketById(userId, serviceId);
    	if (serviceTicket != null && serviceTicket.getStatus() == ServiceTicketStatus.REFUNDED.getIntValue()) {
    		long sysTime =System.currentTimeMillis();
    		
    		int count = serviceTicketMapper.updateServiceExchangeSuccess(userId, serviceId ,ServiceTicketStatus.EXCHANGE_SUCCESS.getIntValue(), ServiceTicketStatus.REFUNDED.getIntValue(), sysTime);
    		if(count == 1){
    			data.put("result", "success");
    		}else{
    			data.put("result", "fail");
    		}
    		data.put("serviceTicket", serviceTicket);
			
		}else{
			data.put("result", "fail");
		}
    	return data;
    }
    

    public Map<String , Object> initTicket(String orderItemId, long userId) {
    	
    	Map<String , Object> result = new HashMap<String , Object>();
    	OrderItem orderItem = orderService.getOrderNewItemById(userId, Long.parseLong(orderItemId));
    	OrderNew orderNew = new OrderNew();
    	if(orderItem != null && orderItem.getOrderNo() > 0){
    		orderNew= orderService.getUserOrderNewByNo(userId, orderItem.getOrderNo()+"");
    		result.put("orderNew", orderNew);
    		
    		result.put("orderItem", afterSaleService.getOrderItemWithProductInfo(orderItem));
    	}
    	
    	
    	EnumSelection enumSelection = new EnumSelection();
    	List<EnumSelection> receiveStatusList = new ArrayList<EnumSelection>();// ServiceTicketReceiveStatus.getAllList(); 
    	enumSelection.setOption("请选择收货状态");
    	enumSelection.setValue("0");
    	receiveStatusList.add(enumSelection);
    	
    	if(orderNew != null && orderNew.getOrderStatus() == OrderStatus.DELIVER){
    		
    		for( ServiceTicketReceiveStatus status : ServiceTicketReceiveStatus.getAllList()){
    			enumSelection = new EnumSelection();
    			enumSelection.setOption(status.getDisplayName());
    			enumSelection.setValue(status.getIntValue() + "");
    			receiveStatusList.add(enumSelection);
    		}
    	} else if(orderNew != null && orderNew.getOrderStatus().getIntValue() > OrderStatus.DELIVER.getIntValue()){
    		enumSelection = new EnumSelection();
			enumSelection.setOption(ServiceTicketReceiveStatus.RECEIVED.getDisplayName());
			enumSelection.setValue(ServiceTicketReceiveStatus.RECEIVED.getIntValue() + "");
			receiveStatusList.add(enumSelection);
    		
    	}
    	
    	result.put("receiveStatusList", receiveStatusList);
    	
    	
    	int afterSaleCount = afterSaleService.getItemAfterSaleValidCount(userId, orderItem.getId(), orderItem.getOrderNo());
    	List<EnumSelection> applyReturnCountList = new ArrayList<EnumSelection>();
    	enumSelection = new EnumSelection();
    	enumSelection.setOption("请选择申请数量");
    	enumSelection.setValue("0");
    	applyReturnCountList.add(enumSelection);
    	if(afterSaleCount < orderItem.getBuyCount()){ 
    		
    		for(int i = 1 ; i <= orderItem.getBuyCount() - afterSaleCount; i++ ){
    			enumSelection = new EnumSelection();
    			enumSelection.setOption(i+"");
    			enumSelection.setValue(i+"");
    			applyReturnCountList.add(enumSelection);
    		}
    	}
		result.put("applyReturnCountList", applyReturnCountList);
    	
//    	int afterSaleCount = afterSaleService.getItemAfterSaleValidCount(userId, orderItem.getId());
//    	if(afterSaleCount < orderItem.getBuyCount()){ 
//    		List<String> applyReturnCountList = new ArrayList<String>(); 
//    		for(int i = 1 ; i <= orderItem.getBuyCount() - afterSaleCount; i++ ){
//    			
//    			applyReturnCountList.add(i+"");
//    		}
//    		result.put("applyReturnCountList", applyReturnCountList);
//    		
//    	}
    	
    	
    	List<EnumSelection> applyReturnReasonList = new ArrayList<EnumSelection>();
    	enumSelection = new EnumSelection();
    	enumSelection.setOption("请选择申请原因");
    	enumSelection.setValue("无");
    	applyReturnReasonList.add(enumSelection);
    	for( ServiceTicketApplyReason status : ServiceTicketApplyReason.getAllList()){
    		enumSelection = new EnumSelection();
    		enumSelection.setOption(status.getDisplayName());
    		enumSelection.setValue(status.getDisplayName());
    		applyReturnReasonList.add(enumSelection);
    	}
    	result.put("applyReturnReasonList", applyReturnReasonList);

        return result;
    }
    
    public Map<String , Object> createServiceTicket( String type, String applyReceiveStatus, String applyReturnCount, String applyReturnReason, String applyReturnMemo, String applyImageUrl, String orderItemId, UserDetail userDetail) {
    	Map<String , Object> result = new HashMap<String , Object>();
    	ServiceTicket serviceTicket = new ServiceTicket();
    	serviceTicket.setOrderItemId(Long.parseLong(orderItemId));
    	serviceTicket.setType(Integer.parseInt(type));
    	serviceTicket.setApplyReceiveStatus(Integer.parseInt(applyReceiveStatus));
    	serviceTicket.setApplyReturnCount(Integer.parseInt(applyReturnCount));
    	serviceTicket.setApplyReturnReason(applyReturnReason);
    	serviceTicket.setApplyReturnMemo(applyReturnMemo);
    	serviceTicket.setApplyImageUrl(applyImageUrl);
    	
    	serviceTicket.setUserId(userDetail.getUserId());
    	serviceTicket.setApplyTime(System.currentTimeMillis());
    	serviceTicket.setYjjNumber(userDetail.getUser().getyJJNumber()); 
    	
    	OrderItem orderItem = orderService.getOrderNewItemById(userDetail.getUserId(), Long.parseLong(orderItemId));
    	serviceTicket.setApplyReturnJiuCoin(Integer.parseInt(applyReturnCount) * orderItem.getUnavalCoinUsed());
    	if(orderItem.getBuyCount() > 0){
    		serviceTicket.setApplyReturnMoney(Integer.parseInt(applyReturnCount) * orderItem.getTotalPay() / orderItem.getBuyCount());
    	}
    	OrderNew orderNew = new OrderNew();
    	if(orderItem != null && orderItem.getOrderNo() > 0){
    		orderNew= orderService.getUserOrderNewByNo(userDetail.getUserId(), orderItem.getOrderNo()+"");
    		
    		if(orderNew != null){
    			
    			String buyer = "";
    			String telephone = "";
    			
    			String expressInfo = orderNew.getExpressInfo();
    			if(expressInfo != null){
    				
	    			String[] expressInfoArray = expressInfo.split(",");
	    				
	    			if(expressInfo.length() >= 2) {
	    				buyer = expressInfoArray[0];
	    				telephone = expressInfoArray[1];
	    				serviceTicket.setUserRealName(buyer);
	    		    	serviceTicket.setUserTelephone(telephone);	
	    			}
	    		}
    		}
    	}
    	

    	serviceTicket.setStatus(0);
    	if(orderItem != null && orderItem.getOrderNo() > 0){
    		if(orderItem.getSkuId() > 0){
    			ProductSKU productSKU = productSKUService.getProductSKU(orderItem.getSkuId());
    			serviceTicket.setSkuNo(productSKU.getSkuNo());
    		}
    		serviceTicket.setOrderNo(orderItem.getOrderNo());
    		serviceTicket.setProcessReturnJiuCoin(Integer.parseInt(applyReturnCount) * orderItem.getUnavalCoinUsed()); ///
    	}
    	
    	int count = serviceTicketMapper.insertServiceTicket(serviceTicket);
    	if(count == 1){
    		
    		orderService.updateAfterSellStatusAndNum(orderNew.getParentId());
    		result.put("result", "success");
    	}else{
    		result.put("result", "fail");
    	}
    	result.put("serviceTicket", serviceTicket);
    	return result;
    }
    
    public int updateServiceTicketById(long userId, ServiceTicket serviceTicket) {
    	return serviceTicketMapper.updateServiceTicketById(userId, serviceTicket);
    }
    
    public Map<String, String> getNewestTrackInfo(long userId, long serviceId) {
    	//取最新的一条物流信息
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userId, serviceId);
    	String context = "";
    	String time = "";
    	Map<String, String> result = new HashMap<String, String>();
    	if (null == serviceTicket.getSellerExpressNo() || null == serviceTicket.getSellerExpressCom().toLowerCase()) {
    		context = "暂无物流信息";
    		result.put("context", context);
        	result.put("time", time); 
        	return result;
    	}
    	JSONObject object;
    	String supplier = serviceTicket.getSellerExpressCom().toLowerCase();
    	String expressOrderNo = serviceTicket.getSellerExpressNo();
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
    
    
}
