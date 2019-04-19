package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.StoreFinanceTicketFacade;
import com.jiuy.core.business.facade.StoreServiceTicketFacade;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.aftersale.StoreMessageBoard;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.core.service.storeaftersale.StoreFinanceTicketService;
import com.jiuy.core.service.storeaftersale.StoreMessageBoardService;
import com.jiuy.core.service.storeaftersale.StoreServiceTicketService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.AfterSaleStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicket;
import com.jiuyuan.entity.storeaftersale.StoreFinanceTicketVO;
import com.jiuyuan.entity.storeaftersale.StoreServiceTicketVO;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/storeaftersale")
@Login
public class StoreAfterSaleController {
	
	@Resource
	private StoreServiceTicketService storeServiceTicketService;
	
	@Resource
	private StoreFinanceTicketService storeFinanceTicketService;
	
	@Resource
	private StoreFinanceTicketFacade storeFinanceTicketFacade;
	
	@Resource
	private StoreServiceTicketFacade storeServiceTicketFacade;
	
	@Resource
	private OrderOldService orderOldService;
	
	@Resource
	private StoreMessageBoardService storeMessageBoardService;
	
	/**
	 * 
	 * @param pageQuery
	 * @param id
	 * @param orderNo
	 * @param skuNo
	 * @param yjjNumber
	 * @param userRealName
	 * @param userTelephone
	 * @param startApplyTime
	 * @param endApplyTime
	 * @param startExamineTime
	 * @param endExamineTime
	 * @param status 在1-7的基础上,自定义8-已完成状态（退款成功，换货成功）9-未完成状态（待审核，已驳回，待买家发货，待确认，待付款，待退款，换货处理中，已发货）
	 * @param type
	 * @return
	 */
	@RequestMapping(value="/service/search")
	@ResponseBody	
	public JsonResponse serviceSearch(PageQuery pageQuery,
			@RequestParam(value="id", required=false, defaultValue="-1") long id,
			@RequestParam(value="order_no", required=false, defaultValue="-1") long orderNo,
			@RequestParam(value="sku_no", required=false, defaultValue="-1") long skuNo,
			@RequestParam(value="business_no", required=false, defaultValue="-1") long businessNumber,
			@RequestParam(value="real_name", required=false, defaultValue="") String userRealName,
			@RequestParam(value="telephone", required=false, defaultValue="") String userTelephone,
			@RequestParam(value="start_apply_time", required=false, defaultValue="") String startApplyTime,
			@RequestParam(value="end_apply_time", required=false, defaultValue="") String endApplyTime,
			@RequestParam(value="start_examine_time", required=false, defaultValue="") String startExamineTime,
			@RequestParam(value="end_examine_time", required=false, defaultValue="") String endExamineTime,
			@RequestParam(value="status", required=false, defaultValue="-1") int status,
			@RequestParam(value="type", required=false, defaultValue="-1") int type) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startApplyTimeL = 0L;
        long endApplyTimeL = 0L;
    	long startExamineTimeL = 0L;
    	long endExamineTimeL = 0L;
    	try {
    		startApplyTimeL = DateUtil.convertToMSEL(startApplyTime);	
    		endApplyTimeL = DateUtil.convertToMSEL(endApplyTime);			
    		startExamineTimeL = DateUtil.convertToMSEL(startExamineTime);		
    		endExamineTimeL = DateUtil.convertToMSEL(endExamineTime);
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	List<Long> orderList =null;
    	if(orderNo != -1 && orderNo != 0){
    		orderList = orderOldService.searchOfParentId(orderNo);
    		orderList.add(orderNo);
    	}
    	StoreServiceTicketVO serviceTicket = new StoreServiceTicketVO (id, orderList, skuNo, businessNumber, userRealName, userTelephone, status, type, startApplyTimeL, endApplyTimeL, startExamineTimeL, endExamineTimeL);;
		    	
        int totalCount = storeServiceTicketService.searchCount(serviceTicket);
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, Object>> list = storeServiceTicketFacade.search(pageQuery, serviceTicket);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/service/examine", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceApply(
			@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "status") int status,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (status < AfterSaleStatus.REJECTED.getIntValue() || status > AfterSaleStatus.TO_DELIVERY.getIntValue()) 
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		
		storeServiceTicketService.updateServiceTicket(serviceId, status, message);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value = "/service/return", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceReturn(
			@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "process_result") int processResult,
			@RequestParam(value = "process_money") double processMoney,
			@RequestParam(value = "process_express_money") double processExpressMoney,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (processResult < AfterSaleStatus.REJECTED.getIntValue() || processResult > AfterSaleStatus.TO_PAY.getIntValue()) 
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		
		try {
			storeServiceTicketFacade.confirmReturn(serviceId, processResult, processMoney, processExpressMoney, message);
		} catch (Exception e) {
			return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
		}

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}	
	
	@RequestMapping(value = "/service/exchange", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceExchange(
			@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "process_result") int processResult,
			@RequestParam(value = "seller_express_money") double sellerExpressMoney,
			@RequestParam(value = "seller_express_com") String sellerExpressCom,
			@RequestParam(value = "seller_express_no") String sellerExpressNo,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (processResult < 3 || processResult > 3) 
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		
		storeServiceTicketService.updateServiceTicket(serviceId, processResult, sellerExpressMoney, sellerExpressCom, sellerExpressNo, message, AfterSaleStatus.RETURNED_OR_DELIVERED.getIntValue());

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	@RequestMapping(value="/finance/search")
	@ResponseBody	
	public JsonResponse financeSearch(PageQuery pageQuery,
			@RequestParam(value="service_id", required=false, defaultValue="-1") long serviceId,
			@RequestParam(value="order_no", required=false, defaultValue="-1") long orderNo,
			@RequestParam(value="return_no", required=false, defaultValue="") String returnNo,
			@RequestParam(value="status", required=false, defaultValue="-1") int status,
			@RequestParam(value="return_type", required=false, defaultValue="-1") int returnType,
			@RequestParam(value="start_return_money", required=false, defaultValue="-1") double startReturnMoney,
			@RequestParam(value="end_return_money", required=false, defaultValue="-1") double endReturnMoney,
			@RequestParam(value="start_apply_time", required=false, defaultValue="") String startApplyTime,
			@RequestParam(value="end_apply_time", required=false, defaultValue="") String endApplyTime,
			@RequestParam(value="start_return_time", required=false, defaultValue="") String startReturnTime,
			@RequestParam(value="end_return_time", required=false, defaultValue="") String endReturnTime,
			@RequestParam(value="start_create_time", required=false, defaultValue="") String startCreateTime,
			@RequestParam(value="end_create_time", required=false, defaultValue="") String endCreateTime) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

        long startApplyTimeL = 0L;
        long endApplyTimeL = 0L;
        long startCreateTimeL = 0L;
        long endCreateTimeL = 0L;
    	long startReturnTimeL = 0L;
    	long endReturnTimeL = 0L;
    	try {
    		startApplyTimeL = DateUtil.convertToMSEL(startApplyTime);	
    		endApplyTimeL = DateUtil.convertToMSEL(endApplyTime);			
    		startCreateTimeL = DateUtil.convertToMSEL(startCreateTime);		
    		endCreateTimeL = DateUtil.convertToMSEL(endCreateTime);
    		startReturnTimeL = DateUtil.convertToMSEL(startReturnTime);		
    		endReturnTimeL = DateUtil.convertToMSEL(endReturnTime);
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("time");
		}
    	
    	StoreFinanceTicketVO financeTicket = new StoreFinanceTicketVO (serviceId, orderNo, returnNo, status, returnType, startReturnMoney, endReturnMoney, startApplyTimeL, endApplyTimeL, startCreateTimeL, endCreateTimeL, startReturnTimeL, endReturnTimeL);
		    	
        int totalCount = storeFinanceTicketService.searchCount(financeTicket);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String,Object>> list = storeFinanceTicketService.search(pageQuery, financeTicket);
		
		for(Map<String, Object> item : list){
			item.put("ReturnTypeStr", getPayTypeStr((int)item.get("ReturnType")));
		}
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	public String getPayTypeStr(int type){
		if(type==PaymentType.ALIPAY.getIntValue() || type == PaymentType.ALIPAY_SDK.getIntValue()){
			return "支付宝";
		}else if(type == PaymentType.WEIXINPAY_NATIVE.getIntValue() || type == PaymentType.WEIXINPAY_PUBLIC.getIntValue() || type == PaymentType.WEIXINPAY_SDK.getIntValue()){
			return "微信";
		}else if(type == PaymentType.BANKCARD_PAY.getIntValue()){
			return "银行汇款";
		}else{
			return "无";
		}
	}
	
	@RequestMapping(value = "/finance/confirm", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addProductSku(
			@RequestParam(value = "id") long id,
			@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "return_type") int returnType,
			@RequestParam(value = "return_money") double returnMoney,
			@RequestParam(value = "return_no") String returnNo,
			@RequestParam(value = "message") String message) {            
		JsonResponse jsonResponse = new JsonResponse();

		StoreFinanceTicket financeTicket = new StoreFinanceTicket(id, serviceId, returnType, returnMoney, returnNo, message, 1, System.currentTimeMillis());
		storeFinanceTicketFacade.updateFinanceTicket(financeTicket);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	
	@RequestMapping(value="/messageboard/search")
	@ResponseBody	
	public JsonResponse messageBoardSearch(PageQuery pageQuery,
			@RequestParam(value="service_id", required=false, defaultValue="-1") long serviceId) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();
    	
        int totalCount = storeMessageBoardService.searchCount(serviceId);
        
        PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);	

		List<Map<String, Object>> list = storeMessageBoardService.search(pageQuery, serviceId);
		
		data.put("list", list);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/messageboard/add", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addMessageboard(
			@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "message") String message, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();
		
		AdminUser userinfo = (AdminUser)request.getSession().getAttribute("userinfo");
		
		StoreMessageBoard messageBoard = new StoreMessageBoard(serviceId, userinfo.getUserId(), userinfo.getUserName(), message);
		storeMessageBoardService.add(messageBoard);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}	

}
