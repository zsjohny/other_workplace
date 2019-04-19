package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.meta.order.OrderDetailVO;
import com.jiuy.core.meta.order.OrderMessageBoard;
import com.jiuy.core.meta.order.OrderNewSO;
import com.jiuy.core.meta.order.OrderNewVO;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.order.OrderMessageBoardService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuy.web.delegate.OrderNewDelegator;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.http.NumberUtil;
import com.jiuyuan.web.help.JsonResponse;

@Controller
@Login
@RequestMapping("/ordernew")
public class OrderNewController {
	
	@Autowired
	private OrderNewDelegator orderNewDelegator;
	
    @Autowired
    private OrderMessageBoardService orderMessageBoardService;
    
    @Autowired
    private ErpDelegator erpDelegator;
    
    @Autowired
    private UserService userService;

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchOrderNew(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize,
			@RequestParam(value = "order_status", required = false, defaultValue = "-1")int orderStatus,
			@RequestParam(value = "send_type", required = false, defaultValue = "-1")int sendType,//发货类型,  -1 在线发货    3门店自提    -2全部类型
			@RequestParam(value = "order_no", required = false, defaultValue = "")String orderNo,
			@RequestParam(value = "clothes_no", required = false, defaultValue = "")String clothesNum,
			@RequestParam(value = "express_no", required = false, defaultValue = "")String expressOrderNo,
			@RequestParam(value = "sku_no", required = false, defaultValue = "")String skuNo,
			@RequestParam(value = "order_type", required = false, defaultValue = "0")int orderType,
			@RequestParam(value = "yjj_number", required = false, defaultValue = "-1")long yJJNumber,
			@RequestParam(value = "receiver", required = false, defaultValue = "")String receiver,
			@RequestParam(value = "phone", required = false, defaultValue="")String phone,
			@RequestParam(value = "code", required = false, defaultValue="")String code,	//新增统计识别码
			@RequestParam(value = "starttime", required = false, defaultValue = "1970-01-01 00:00:00") String startTime,
			@RequestParam(value = "endtime", required = false, defaultValue = "")String endTime) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
    	long endTimeL = 0L;
    	long startTimeL = 0L;
    	try {
			startTimeL = DateUtil.convertToMSEL(startTime);
			if(StringUtils.equals(endTime, "")) {
				endTimeL = System.currentTimeMillis();
			} else {
				endTimeL = DateUtil.convertToMSEL(endTime);
			}
		} catch (ParseException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("startTime:" + startTime + " endTime:" + endTime);
		}
		
        String orderSeq = orderNo;
        if(NumberUtil.isNumeric(orderNo) && orderNo.length() < 10) {
        	orderSeq = Long.parseLong(orderNo) + "";
        }

        List<List<OrderNewVO>> orderNewsVO = orderNewDelegator.searchOrderNews(pageQuery, orderSeq, clothesNum, expressOrderNo, 
        		skuNo, orderType, yJJNumber, receiver, phone, startTimeL, endTimeL, orderStatus,code,sendType);
		
        int count = orderNewDelegator.searchOrderNewsCount(orderSeq, clothesNum, expressOrderNo, skuNo,
				orderType, yJJNumber, receiver, phone, startTimeL, endTimeL, orderStatus,code,sendType);
		pageQueryResult.setRecordCount(count);
		
		
		data.put("list", orderNewsVO);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
    public JsonResponse srchDetail(@RequestParam(value = "order_no") long orderNo) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		
		OrderDetailVO orderDetailVO = orderNewDelegator.loadOrderNewDetailInfo(orderNo);
		data.put("order_detail", orderDetailVO);
        
        return jsonResponse.setSuccessful().setData(data);
	}
	
	/**
	 * 
	 * @param type  1在线发货         2门店自取发货
	 * @return
	 */
	@RequestMapping(value = "/delivery")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse delivery(HttpSession httpSession, 
    		@RequestParam(value = "order_no") long orderNo,
			@RequestParam(value = "user_id") long userId,
			@RequestParam(value = "type") int type,
			@RequestParam(value = "supplier", required = false, defaultValue = "") String supplier, 
			@RequestParam(value = "express_no", required = false, defaultValue = "") String expressNo) {
        AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		
		if(adminUser == null)
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("session的userinfo为空");
		
		if(type == 1){
			if(StringUtils.isEmpty(supplier) || StringUtils.isEmpty(expressNo)){
				return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("物流或订单号不能为空");
			}
		}
		
		try {
			orderNewDelegator.delivery(orderNo, userId, supplier, expressNo, adminUser,type);
		} catch (ParameterErrorException e) {
			jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 取消订单
	 */
	@RequestMapping(value = "/cancel")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse cancel(HttpSession httpSession, @RequestParam(value = "order_no") long orderNo,
                               @RequestParam(value = "user_id") long userId,
                               @RequestParam(value = "old_order_status") int oldStatus,
                               @RequestParam(value = "message", required = false, defaultValue = "") String message,HttpServletRequest request) {
        AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = null;
		try {
            resultCode = orderNewDelegator.cancel(orderNo, userId, oldStatus, message, adminUser,request);
		} catch (ParameterErrorException e) {
			resultCode = ResultCode.COMMON_ERROR_BAD_PARAMETER;
			return jsonResponse.setResultCode(resultCode).setError(e.getMessage());
		}
		
		return jsonResponse.setSuccessful().setResultCode(resultCode);
	}
	
	/**
     * 订单发货返回(已发货-->待发货)
	 */
	@RequestMapping(value = "/revoke")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse revoke(HttpSession httpSession, @RequestParam(value = "order_no") long orderNo,
	                           @RequestParam(value = "user_id") long userId,
	                           @RequestParam(value = "old_order_status") int oldStatus) {
        AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		ResultCode resultCode = null;
		
		try {
            resultCode = orderNewDelegator.revoke(orderNo, userId, oldStatus, adminUser);
		} catch (ParameterErrorException e) {
			resultCode = ResultCode.COMMON_ERROR_BAD_PARAMETER;
			return jsonResponse.setResultCode(resultCode).setError(e.getMessage());
		}
		
		return jsonResponse.setSuccessful().setResultCode(resultCode);
	}
	

	/**
     * 商品发货
	 */
	
	

	@RequestMapping(value = "/undelivered/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchUndelivered(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize,
			@RequestParam(value = "order_no", required = false, defaultValue = "")String orderNo,
			@RequestParam(value = "clothes_no", required = false, defaultValue = "")String clothesNum,
			@RequestParam(value = "express_no", required = false, defaultValue = "")String expressOrderNo,
			@RequestParam(value = "sku_no", required = false, defaultValue = "")String skuNo,
			@RequestParam(value = "yjj_number", required = false, defaultValue = "-1")long yJJNumber,
			@RequestParam(value = "receiver", required = false, defaultValue = "")String receiver,
			@RequestParam(value = "phone", required = false, defaultValue="")String phone,
			@RequestParam(value = "code", required = false, defaultValue="")String code,	//新增统计识别码
			@RequestParam(value = "order_type", required = false, defaultValue = "1")int orderType,//1是在线    2是自提
			@RequestParam(value = "starttime", required = false, defaultValue = "1970-01-01 00:00:00") String startTime,
			@RequestParam(value = "endtime", required = false, defaultValue = "")String endTime) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
    	long endTimeL = 0L;
    	long startTimeL = DateUtil.convertToMSEL(startTime);
		if(StringUtils.equals(endTime, "")) {
			endTimeL = System.currentTimeMillis();
		} else {
			endTimeL = DateUtil.convertToMSEL(endTime);
		}
		
        String orderSeq = NumberUtil.isNumeric(orderNo) ? Long.parseLong(orderNo) + "" : orderNo;

        List<List<OrderNewVO>> orderNewsVO = orderNewDelegator.searchUndelivered(pageQuery, orderSeq, clothesNum,
            expressOrderNo, skuNo, yJJNumber, receiver, phone, startTimeL, endTimeL,code,orderType);
		
        int count = orderNewDelegator.searchUndeliveredCount(orderSeq, clothesNum, expressOrderNo, skuNo, yJJNumber, receiver, phone, startTimeL, endTimeL,code,orderType);
		pageQueryResult.setRecordCount(count);
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("list", orderNewsVO);
		data.put("total", pageQueryResult);
		jsonResponse.setData(data);
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 订单取消撤销(已取消-->待发货)
	 * @return
	 */
	@RequestMapping("/recovery")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse recovery(@RequestParam(value = "order_no") long orderNo, 
                                 @RequestParam("mergerd_id") long mergedId,
                                 @RequestParam("parent_id") long parentId,
                                 @RequestParam(value = "user_id") long userId,
                                 @RequestParam(value = "old_order_status") int oldStatus) {
        JsonResponse jsonResponse = new JsonResponse();

        orderNewDelegator.recovery(orderNo, mergedId, parentId, userId, oldStatus);

        return jsonResponse.setSuccessful();
    }

    @RequestMapping("/message/add")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse addMessage(HttpSession httpSession, @RequestParam(value = "order_no") long orderNo,
                                   @RequestParam(value = "type") int type,
                                   @RequestParam(value = "operation") String operation,
                                   @RequestParam(value = "message") String message) {
        AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
        OrderMessageBoard orderMessageBoard =
            new OrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), type, operation, message);
        orderMessageBoardService.add(orderMessageBoard);
            
        JsonResponse jsonResponse = new JsonResponse();

        return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
    }
    
    /**
     * @param page
     * @param pageSize
     * @param orderNo
     * @param type 0:业务处理 1:业务操作
     * @return
     * @throws ParseException
     */
    @RequestMapping("/message/search")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse searchMessage(HttpSession httpSession, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
			@RequestParam("order_no") long orderNo,
			@RequestParam("type") int type,
			@RequestParam(value = "admin_id", required = false, defaultValue = "-1") long adminId,
			@RequestParam(value = "start_time", required = false, defaultValue = "") String startTime,
			@RequestParam(value = "end_time", required = false, defaultValue = "") String endTime) throws ParseException {
		PageQuery pageQuery = new PageQuery(page, pageSize);
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
        long startTimeL = DateUtil.convertToMSEL(startTime);
        long endTimeL = System.currentTimeMillis();
        if (!StringUtils.equals("", endTime)) {
            DateUtil.convertToMSEL(endTime);
        }

        List<OrderMessageBoard> messageBoards =
            orderMessageBoardService.search(pageQuery, orderNo, type, adminId, startTimeL, endTimeL);
        int count = orderMessageBoardService.searchCount(orderNo, type, adminId, startTimeL, endTimeL);
		pageQueryResult.setRecordCount(count);
        
        Map<String, Object> data = new HashMap<String, Object>();
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
        data.put("admin", adminUser);
       
        List<AdminUser> adminUsers = userService.search();
        data.put("admins", adminUsers);
		data.put("list", messageBoards);
		data.put("total", pageQueryResult);
		
		JsonResponse jsonResponse = new JsonResponse();
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    /**
     * 
     * @param response
     * @param startTime
     * @param endTime
     * @param type 0:自建仓 1:其他仓
     * @param orderType  1:线上发货   2门店自取
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/delivery/excel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse outExcel(HttpServletResponse response,
                         @RequestParam(value = "starttime", required = false, defaultValue = "") String startTime,
                         @RequestParam(value = "endtime", required = false, defaultValue = "") String endTime,
                         @RequestParam(value = "type") int type,
                         @RequestParam(value = "orderType") int orderType) throws IOException, ParseException {
    	JsonResponse jsonResponse = new JsonResponse();
		long currenTime = System.currentTimeMillis();

		long startTimeL = DateUtil.convertToMSEL(startTime);
		long endTimeL = currenTime;
		if (!StringUtils.equals(endTime, "")) {
			endTimeL = DateUtil.convertToMSEL(endTime);
		}

		List<Map<String, Object>> list = orderNewDelegator.deliveryExcel(type, startTimeL, endTimeL,orderType);

		String columnNames[] = { "订单编号", "商品名", "品牌", "省", "市", "区", "收件人", "手机号", "款号", "颜色", "尺寸", "数量", "快递单号",
				"仓库名称", "地址", "货位号" };// 列名
		String keys[] = { "orderNo", "productName", "brandName", "province", "city", "district", "receiver", "phone",
				"clothNum", "color", "size", "buyCount", "expressOrderNo", "warehouse", "expressInfo", "position" };// map中的key

		if (type == 1) {
			ExcelUtil.exportExcel(response, list, keys, columnNames, "其他仓");
		} else if (type == 0) {
			ExcelUtil.exportExcel(response, list, keys, columnNames, "自建仓");
		}

		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}

	/**
	 * 
	 * @param orderNo
	 * @param mergedId
	 * @param parentId
	 * @param userId
	 * @param oldStatus
	 * @return
	 */
	@RequestMapping("/erp/push")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse recovery() {
		JsonResponse jsonResponse = new JsonResponse();

		// 获取至今的合并订单
		List<Map<String, Object>> list = erpDelegator.getToBePushedOrders(0L, DateUtil.getERPTime());

		// 推送已做处理过的订单
		Set<Long> successOrderNos = new HashSet<Long>();
		erpDelegator.pushOrders(list, successOrderNos);

		// 更新推送成功的订单时间
		if (successOrderNos.size() > 0) {
			erpDelegator.addPushTime(successOrderNos);
		}

		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS).setData(list);
	}

	/**
	 * 商品发货查询
	 */
	@RequestMapping(value = "/undelivered/search/new", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchUndeliveredNew(PageQuery pageQuery, OrderNewSO orderNewSO) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();

		data.put("total", PageQueryResult.copyFrom(pageQuery, orderNewDelegator.searchUndeliveredNewCount(orderNewSO)));
		data.put("list", orderNewDelegator.searchUndeliveredNew(orderNewSO, pageQuery));

		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 发单
	 * 
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/dispatch/order")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse dispatchOrder(HttpSession httpSession, @RequestParam("order_no") long orderNo,
			@RequestParam("remark") String remark) {
		JsonResponse jsonResponse = new JsonResponse();

		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		orderNewDelegator.dispatchOrder(orderNo, remark, adminUser);

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

	/**
	 * 撤销发单
	 * 
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/revoke/dispatch/order")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse revokeDispatchOrder(HttpSession httpSession, @RequestParam("order_no") long orderNo,
			@RequestParam("remark") String remark) {
		JsonResponse jsonResponse = new JsonResponse();

		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		orderNewDelegator.revokeDispatchOrder(orderNo, remark, adminUser);

		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}

}
