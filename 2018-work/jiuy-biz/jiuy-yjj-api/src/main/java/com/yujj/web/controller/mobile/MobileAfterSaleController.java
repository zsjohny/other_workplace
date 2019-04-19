package com.yujj.web.controller.mobile;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.Constants;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.AfterSaleService;
import com.yujj.business.service.ExpressService;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.afterSale.ServiceTicket;
import com.yujj.web.controller.delegate.AfterSaleDelegator;
import com.yujj.web.controller.delegate.OrderDelegator;

@Controller
@RequestMapping("/mobile/service")
public class MobileAfterSaleController {

    @Autowired
    private AfterSaleService afterSaleService;
    
    @Autowired
    private ExpressService expressService;
    
    
    @Autowired
    private OrderDelegator orderDelegator;
    
    @Autowired
    private AfterSaleDelegator afterSaleDelegator;
	

    @RequestMapping(value = "/serviceTicketList")
    @ResponseBody
    public JsonResponse serviceTicketList(@RequestParam(value = "order_search_no", required = false) String orderSearchNo, PageQuery pageQuery, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	Map<String, Object> data =  afterSaleDelegator.getUserServiceTicketList(pageQuery, userDetail.getUserId(), orderSearchNo);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    @RequestMapping(value = "/serviceTicketDetail")
    @ResponseBody
    public JsonResponse serviceTicketDetail(@RequestParam(value = "service_id", required = true) String serviceId, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	boolean isNumToId = true;
    	Map<String, Object> data = afterSaleDelegator .getServiceTicketDetail(userDetail.getUserId(), Long.parseLong(serviceId),isNumToId);
    	
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    //暂时无用
//    @RequestMapping(value = "/updateTicketDetail")
//    @ResponseBody
//    public JsonResponse updateServiceTicket( @Param("serviceTicket") ServiceTicket serviceTicket, UserDetail userDetail) {
//    	
//    	JsonResponse jsonResponse = new JsonResponse();
//    	ServiceTicket serviceTicketTemp = afterSaleService .getServiceTicketById(userDetail.getUserId(), serviceTicket.getId());
//    	Map<String, Object> data = new HashMap<String, Object>();
//    	data.put("serviceTicket", serviceTicketTemp);
//    	return jsonResponse.setSuccessful().setData(data);
//    }
    
    @RequestMapping(value = "/createTicket")
    @ResponseBody
    public JsonResponse createTicket(@RequestParam("order_item_id") String orderItemId, UserDetail userDetail) {
    	Map<String, Object> data = afterSaleDelegator.initTicket(orderItemId, userDetail.getUserId());
    	JsonResponse jsonResponse = new JsonResponse();
    	return jsonResponse.setSuccessful().setData(data);
    
    }
    
    @RequestMapping(value = "/createTicket/commit", method = RequestMethod.POST) //
    @ResponseBody
    public JsonResponse createTicketCommit(@RequestParam("type") String type,
    		@RequestParam("apply_receive_status") String applyReceiveStatus,
    		@RequestParam("apply_return_count") String applyReturnCount,
    		@RequestParam("apply_return_reason") String applyReturnReason,
    		@RequestParam(value = "apply_return_memo" , required = false) String applyReturnMemo,
    		@RequestParam(value = "apply_image_url", required = false) String applyImageUrl,
    		@RequestParam("order_item_id") String orderItemId, UserDetail userDetail) {
    	Map<String, Object> data = afterSaleDelegator.createServiceTicket(type, applyReceiveStatus, applyReturnCount, applyReturnReason, applyReturnMemo, applyImageUrl, orderItemId, userDetail);
    	JsonResponse jsonResponse = new JsonResponse();
    	return jsonResponse.setSuccessful().setData(data);
    	
    }
    
    @RequestMapping(value = "/expressService/auth")
    public String expressQuery(@RequestParam("service_id") long serviceId, UserDetail userDetail, Map<String, Object> model) {
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), serviceId);
    	if(serviceTicket.getSellerExpressCom() != null && serviceTicket.getSellerExpressNo() != null){
    		String supplier = serviceTicket.getSellerExpressCom().toLowerCase();
    		String expressOrderNo = serviceTicket.getSellerExpressNo();
    		if (!StringUtils.isBlank(expressOrderNo) && !StringUtils.isBlank(supplier)) {
    			JSON expressData = expressService.queryExpressInfo(supplier, expressOrderNo);
    			model.put("data", expressData);
    		}
    		return "mobile/express_info";
    	}else  {
    		return Constants.ERROR_PAGE_NOT_FOUND;
    	}
    }
    
    
    @RequestMapping(value = "/expressNewJS")
    @ResponseBody
    public JsonResponse expressQueryNew(@RequestParam("service_id") long serviceId, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ServiceTicket serviceTicket = afterSaleService.getServiceTicketDetailById(userDetail.getUserId(), serviceId);
    	Map<String, String> data = new HashMap<String, String>();
    	if(serviceTicket != null && serviceTicket.getProcessOrderNo() > 0){		
    		return orderDelegator.getExpressInfo(userDetail.getUserId(), serviceTicket.getProcessOrderNo());
    	}
    	return jsonResponse.setSuccessful().setData(data);
    }

    
    @RequestMapping(value = "/buyerShipConfirm")
    @ResponseBody
    public JsonResponse buyerShipConfirm(@RequestParam("service_id") String serviceId, UserDetail userDetail) {
  
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = afterSaleDelegator .getBuyerShipConfirmInfo(userDetail.getUserId(), Long.parseLong(serviceId));
    	return jsonResponse.setSuccessful().setData(data);
    	
    }
    
    @RequestMapping(value = "/buyerShipConfirmCommit" , method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse buyerShipConfirmCommit(@RequestParam("service_id") String serviceId, @RequestParam("buyer_express_com") String buyerExpressCom, @RequestParam("buyer_express_no") String buyerExpressNo, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = afterSaleDelegator .buyerShipConfirmCommit(Long.parseLong(serviceId), buyerExpressCom, buyerExpressNo, userDetail.getUserId());
    	return jsonResponse.setSuccessful().setData(data);
    	
    }
    
    @RequestMapping(value = "/buyerExpressReject" , method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse buyerExpressReject(@RequestParam("service_id") String serviceId, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = afterSaleDelegator .buyerExpressReject(Long.parseLong(serviceId), userDetail.getUserId());
    	return jsonResponse.setSuccessful().setData(data);
    	
    }
    
    @RequestMapping(value = "/buyerExchangeReceived" )
    @ResponseBody
    public JsonResponse buyerExchangeReceived(@RequestParam("service_id") String serviceId, UserDetail userDetail) {
    	
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = afterSaleDelegator .buyerExchangeReceived(Long.parseLong(serviceId), userDetail.getUserId());
    	return jsonResponse.setSuccessful().setData(data);
    	
    }
	
}
