package com.jiuy.web.controller;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.AfterSaleFacade;
import com.jiuy.core.business.facade.FinanceTicketFacade;
import com.jiuy.core.business.facade.ServiceTicketFacade;
import com.jiuy.core.dao.OrderItemDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.aftersale.FinanceTicket;
import com.jiuy.core.meta.aftersale.FinanceTicketVO;
import com.jiuy.core.meta.aftersale.MessageBoard;
import com.jiuy.core.meta.aftersale.ServiceTicketVO;
import com.jiuy.core.meta.order.OrderItem;
import com.jiuy.core.meta.order.OrderNew;
import com.jiuy.core.service.StoreFinanceLogService;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.aftersale.FinanceTicketService;
import com.jiuy.core.service.aftersale.MessageBoardService;
import com.jiuy.core.service.aftersale.ServiceTicketService;
import com.jiuy.core.service.member.StoreBusinessService;
import com.jiuy.core.service.order.OrderOldService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.AfterSaleStatus;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.entity.StoreFinanceLog;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.FinancialPermisson;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@RequestMapping("/aftersale")
@Login
public class AfterSaleController {

	@Resource
	private ServiceTicketService serviceTicketService;

	@Resource
	private ServiceTicketFacade serviceTicketFacade;

	@Resource
	private FinanceTicketService financeTicketService;

	@Resource
	private FinanceTicketFacade financeTicketFacade;

	@Resource
	private MessageBoardService messageBoardService;

	@Resource
	private UserService userService;

	@Resource
	private StoreBusinessService storeBusinessService;

	@Resource
	private OrderOldService orderOldService;

	@Resource
	private StoreFinanceLogService storeFinanceLogService;

	@Resource
	private AfterSaleFacade afterSaleFaced;

	@Resource
	private OrderItemDao orderItemDao;

	@Resource
	private StoreBusinessDao storeBusinessDao;

	private static final Logger logger = LoggerFactory.getLogger(AfterSaleController.class);

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
	 * @param status
	 *            在1-7的基础上,自定义8-已完成状态（退款成功，换货成功）9-未完成状态（待审核，已驳回，待买家发货，待确认，待付款，待退款，换货处理中，已发货）
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "/service/search")
	@ResponseBody
	public JsonResponse serviceSearch(PageQuery pageQuery,
			@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
			@RequestParam(value = "order_no", required = false, defaultValue = "-1") long orderNo,
			@RequestParam(value = "sku_no", required = false, defaultValue = "-1") long skuNo,
			@RequestParam(value = "yjj_no", required = false, defaultValue = "-1") long yjjNumber,
			@RequestParam(value = "real_name", required = false, defaultValue = "") String userRealName,
			@RequestParam(value = "telephone", required = false, defaultValue = "") String userTelephone,
			@RequestParam(value = "start_apply_time", required = false, defaultValue = "") String startApplyTime,
			@RequestParam(value = "end_apply_time", required = false, defaultValue = "") String endApplyTime,
			@RequestParam(value = "start_examine_time", required = false, defaultValue = "") String startExamineTime,
			@RequestParam(value = "end_examine_time", required = false, defaultValue = "") String endExamineTime,
			@RequestParam(value = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(value = "type", required = false, defaultValue = "-1") int type) {
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

		ServiceTicketVO serviceTicket = new ServiceTicketVO(id, orderNo, skuNo, yjjNumber, userRealName, userTelephone,
				status, type, startApplyTimeL, endApplyTimeL, startExamineTimeL, endExamineTimeL);

		int totalCount = serviceTicketService.searchCount(serviceTicket);

		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

		List<Map<String, Object>> list = serviceTicketFacade.search(pageQuery, serviceTicket);

		data.put("list", list);
		data.put("total", pageQueryResult);

		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 审核退换货
	 */
	@RequestMapping(value = "/service/examine", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceApply(@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "status") int status, @RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (status < AfterSaleStatus.REJECTED.getIntValue() || status > AfterSaleStatus.TO_DELIVERY.getIntValue()) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);
		}
		// 修改退换货状态及审核退换货申请添加售后信息
		try {
			afterSaleFaced.upDateServiceTickedAndNotification(serviceId, status, message);
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@RequestMapping(value = "/service/return", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceReturn(@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "process_result") int processResult,
			@RequestParam(value = "process_money") double processMoney,
			@RequestParam(value = "process_express_money") double processExpressMoney,
			@RequestParam(value = "process_return_jiucoin") int processReturnJiuCoin,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (processResult < AfterSaleStatus.REJECTED.getIntValue()
				|| processResult > AfterSaleStatus.TO_PAY.getIntValue())
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);

		serviceTicketFacade.confirmReturn(serviceId, processResult, processMoney, processExpressMoney,
				processReturnJiuCoin, message);

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@RequestMapping(value = "/service/exchange", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse updateServiceExchange(@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "process_result") int processResult,
			@RequestParam(value = "seller_express_money") double sellerExpressMoney,
			@RequestParam(value = "seller_express_com") String sellerExpressCom,
			@RequestParam(value = "seller_express_no") String sellerExpressNo,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();

		if (processResult < 3 || processResult > 3)
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER);

		serviceTicketService.updateServiceTicket(serviceId, processResult, sellerExpressMoney, sellerExpressCom,
				sellerExpressNo, message, AfterSaleStatus.RETURNED_OR_DELIVERED.getIntValue());

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@RequestMapping(value = "/finance/search")
	@ResponseBody
	public JsonResponse financeSearch(PageQuery pageQuery,
			@RequestParam(value = "service_id", required = false, defaultValue = "-1") long serviceId,
			@RequestParam(value = "order_no", required = false, defaultValue = "-1") long orderNo,
			@RequestParam(value = "return_no", required = false, defaultValue = "") String returnNo,
			@RequestParam(value = "status", required = false, defaultValue = "-1") int status,
			@RequestParam(value = "return_type", required = false, defaultValue = "-1") int returnType,
			@RequestParam(value = "start_return_money", required = false, defaultValue = "-1") double startReturnMoney,
			@RequestParam(value = "end_return_money", required = false, defaultValue = "-1") double endReturnMoney,
			@RequestParam(value = "start_apply_time", required = false, defaultValue = "") String startApplyTime,
			@RequestParam(value = "end_apply_time", required = false, defaultValue = "") String endApplyTime,
			@RequestParam(value = "start_return_time", required = false, defaultValue = "") String startReturnTime,
			@RequestParam(value = "end_return_time", required = false, defaultValue = "") String endReturnTime,
			@RequestParam(value = "start_create_time", required = false, defaultValue = "") String startCreateTime,
			@RequestParam(value = "end_create_time", required = false, defaultValue = "") String endCreateTime) {
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

		FinanceTicketVO financeTicket = new FinanceTicketVO(serviceId, orderNo, returnNo, status, returnType,
				startReturnMoney, endReturnMoney, startApplyTimeL, endApplyTimeL, startCreateTimeL, endCreateTimeL,
				startReturnTimeL, endReturnTimeL);
		;

		int totalCount = financeTicketService.searchCount(financeTicket);

		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

		List<Map<String, Object>> list = financeTicketService.search(pageQuery, financeTicket);
		
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
	@FinancialPermisson
	public JsonResponse addProductSku(@RequestParam(value = "id") long id,
			@RequestParam(value = "service_id") long serviceId, @RequestParam(value = "return_type") int returnType,
			@RequestParam(value = "return_money") double returnMoney,
			@RequestParam(value = "real_money") double realMoney, @RequestParam(value = "return_no") String returnNo,
			@RequestParam(value = "message") String message) {
		JsonResponse jsonResponse = new JsonResponse();
		int total = userService.searchCountStoreId(serviceId);
		long belongStoreId = 0;

		if (total > 0) {
			belongStoreId = userService.searchStoreId(serviceId);
		}

		if (belongStoreId > 0) {// 是否绑定了商家 ,更新表及生成storeFinanceLog记录(扣除分销收益)
			updateStoreBusinessAndInsertStoreFinanceLog(belongStoreId, realMoney, id, serviceId);
		}
		FinanceTicket financeTicket = new FinanceTicket(id, serviceId, returnType, returnMoney, returnNo, message, 1,
				System.currentTimeMillis());
		financeTicketFacade.updateFinanceTicket(financeTicket);

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	@RequestMapping(value = "/messageboard/search")
	@ResponseBody
	public JsonResponse messageBoardSearch(PageQuery pageQuery,
			@RequestParam(value = "service_id", required = false, defaultValue = "-1") long serviceId) {
		Map<String, Object> data = new HashMap<String, Object>();

		JsonResponse jsonResponse = new JsonResponse();

		int totalCount = messageBoardService.searchCount(serviceId);

		PageQueryResult pageQueryResult = PageQueryResult.copyFrom(pageQuery, totalCount);

		List<Map<String, Object>> list = messageBoardService.search(pageQuery, serviceId);

		data.put("list", list);
		data.put("total", pageQueryResult);

		return jsonResponse.setSuccessful().setData(data);
	}

	@RequestMapping(value = "/messageboard/add", method = RequestMethod.POST)
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addMessageboard(@RequestParam(value = "service_id") long serviceId,
			@RequestParam(value = "message") String message, HttpServletRequest request) {
		JsonResponse jsonResponse = new JsonResponse();

		AdminUser userinfo = (AdminUser) request.getSession().getAttribute("userinfo");

		MessageBoard messageBoard = new MessageBoard(serviceId, userinfo.getUserId(), userinfo.getUserName(), message);
		messageBoardService.add(messageBoard);

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	private void updateStoreBusinessAndInsertStoreFinanceLog(long belongStoreId, double realMoney, long id,
			long serviceId) {

		StoreBusiness storeBusiness = storeBusinessService.searchBusinessById(belongStoreId);
		OrderNew orderNew = orderOldService.orderNewOfServiceId(serviceId);
		long orderItemId = serviceTicketService.ServiceTicketOfId(serviceId).getOrderItemId();

		double deductCommission = 0;
		boolean needUpdate = false;// 收益不为0的就需要添加记录
		int type = 4;
		
		//判断是几级会员及来源于售后还是取消订单
		if (storeBusiness.getDeep() == 1) {// 1级会员订单
			if (orderItemId == 0) {// 取消订单
				deductCommission = orderNew.getCommission();
				if (orderNew.getCommission() > 0) {
					needUpdate = true;
					type = 6;
				}
			} else {// 售后
				OrderItem orderItem = orderItemDao.orderItemsOfId(orderItemId);
				if (orderItem != null && orderItem.getTotalPay() > 0) {
					deductCommission = realMoney / orderItem.getTotalPay() * orderItem.getTotalCommission();
					if (orderItem.getTotalCommission() > 0) {
						needUpdate = true;
					}
				}
			}

			if (needUpdate) {
				updateAndInsertLog(storeBusiness, orderNew, deductCommission, id, type);
			}

		} else if (storeBusiness.getDeep() >= 2) {
			double deductCommission1 = 0;// 1级收益金额
			double deductCommission2 = 0;
			long storeId1 = 0;// 1级门店
			long storeId2 = 0;
			if (orderNew.getCommission() > 0) {// 有收益
				try {
					String[] dividedCommissions = orderNew.getDividedCommission().split(",");
					deductCommission1 = Double.parseDouble(dividedCommissions[0]) * orderNew.getTotalPay();
					deductCommission2 = Double.parseDouble(dividedCommissions[1]) * orderNew.getTotalPay();
				} catch (NumberFormatException e) {
					logger.error("com.jiuy.web.controller/aftersale/finance/confirm ERROR: orderNo="
							+ orderNew.getOrderNo() + ",dividedCommission:=" + orderNew.getDividedCommission());
				}
				needUpdate = true;
			}

			if (orderItemId == 0) {// 取消订单
				deductCommission = orderNew.getCommission();
				type = 6;

			} else {// 售后
				OrderItem orderItem = orderItemDao.orderItemsOfId(orderItemId);
				if (orderItem != null && orderItem.getTotalPay() > 0) {
					if (orderItem.getTotalCommission() > 0 && needUpdate) {
						deductCommission = realMoney/orderItem.getTotalPay() *orderItem.getTotalCommission();
						
						deductCommission1 = deductCommission * (deductCommission1 / orderNew.getCommission());
						deductCommission2 = deductCommission * (deductCommission2 / orderNew.getCommission());
						needUpdate = true;
					}
				}
			}

			try {
				String[] superStoreIds = storeBusiness.getSuperBusinessIds().split(",");
				storeId1 = Long.parseLong(superStoreIds[1]);
				if (storeBusiness.getDeep() == 2) {
					storeId2 = storeBusiness.getId();
				} else {
					storeId2 = Long.parseLong(superStoreIds[2]);
				}
			} catch (NumberFormatException e) {
				logger.error("com.jiuy.web.controller/aftersale/finance/confirm ERROR: storeBusiness   Id="
						+ storeBusiness.getId() + ",SuperBusinessIds:=" + storeBusiness.getSuperBusinessIds());
			}

			if (needUpdate) {
				if (storeId1 != 0) {
					updateAndInsertLog(storeBusinessService.searchBusinessById(storeId1), orderNew, deductCommission1,
							id, type);
				}
				if (storeId2 != 0) {
					updateAndInsertLog(storeBusinessService.searchBusinessById(storeId2), orderNew, deductCommission2,
							id, type);
				}
			}
		}
		//更新ordernew中退款金额
		orderNew.setReturnCommission(orderNew.getReturnCommission() + deductCommission);
		orderNew.setReturnMoney(orderNew.getReturnMoney() + realMoney);
		int changRow = orderOldService.updateCommission(orderNew);
		if (changRow != 1) {
			logger.error("com.jiuy.web.controller/aftersale/finance/confirm ERROR: " + "更新OrderNew数据失败parent_id = "
					+ orderNew.getParentId() + ",order_no = " + orderNew.getOrderNo());
		}

	}

	private void updateAndInsertLog(StoreBusiness storeBusiness, OrderNew orderNew, double deductCommission, long id,
			int type) {
		storeBusinessDao.reduceIncome(storeBusiness.getId(), deductCommission);
		StoreFinanceLog storeFinanceLog = new StoreFinanceLog(orderNew.getUserId(), storeBusiness.getId(), type,
				deductCommission, System.currentTimeMillis(), type == 6? orderNew.getOrderNo():id);
		storeFinanceLogService.addFinanceLogon(storeFinanceLog);
	}
}
