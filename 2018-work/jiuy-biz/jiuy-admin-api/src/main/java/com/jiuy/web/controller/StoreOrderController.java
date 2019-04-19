package com.jiuy.web.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
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

import com.jiuy.core.dao.StoreOrderMessageBoardDao;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.core.meta.admin.AdminUser;
import com.jiuy.core.service.UserService;
import com.jiuy.core.service.storeorder.StoreOrderMessageBoardService;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.controller.util.ExcelUtil;
import com.jiuy.web.delegate.StoreOrderDelegator;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.entity.query.PageQueryResult;
import com.jiuyuan.entity.storeorder.StoreOrderDetailVO;
import com.jiuyuan.entity.storeorder.StoreOrderMessageBoard;
import com.jiuyuan.entity.storeorder.StoreOrderSO;
import com.jiuyuan.entity.storeorder.StoreOrderVO;
import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.service.common.StoreOrderNewService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.http.NumberUtil;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月7日 下午3:37:14
*/

@Controller
@Login
@RequestMapping("/storeorder")
public class StoreOrderController {
	private static final Log logger = LogFactory.get(StoreOrderController.class); 
	@Resource
	private StoreOrderDelegator storeOrderDelegator;
	
	@Autowired
	private StoreOrderMessageBoardDao storeOrderMessageBoardDao;
	
	@Autowired
	private StoreOrderMessageBoardService storeOrderMessageBoardService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private IStoreOrderNewService storeOrderNewService;
	
	@RequestMapping(value = "/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchStoreOrder(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10") int pageSize,
			@RequestParam(value = "order_status", required = false, defaultValue = "-1")int orderStatus,
			@RequestParam(value = "order_no", required = false, defaultValue = "")String orderNo,
			@RequestParam(value = "clothes_no", required = false, defaultValue = "")String clothesNum,
			@RequestParam(value = "express_no", required = false, defaultValue = "")String expressOrderNo,
			@RequestParam(value = "sku_no", required = false, defaultValue = "")String skuNo,
			@RequestParam(value = "order_type", required = false, defaultValue = "0")int orderType,
			@RequestParam(value = "store_number", required = false, defaultValue = "-1")long storeNumber,
			@RequestParam(value = "receiver", required = false, defaultValue = "")String receiver,
			@RequestParam(value = "phone", required = false, defaultValue="")String phone,
			@RequestParam(value = "starttime", required = false, defaultValue = "1970-01-01 00:00:00") String startTime,
			@RequestParam(value = "endtime", required = false, defaultValue = "")String endTime){
		
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
    	
    	String orderSeq = NumberUtil.isNumeric(orderNo) ? Long.parseLong(orderNo) + "" : orderNo;
        
        List<List<StoreOrderVO>> storeOrderVO = storeOrderDelegator.searchStoreOrders(pageQuery, orderSeq, clothesNum, expressOrderNo, 
        		skuNo, orderType, storeNumber, receiver, phone, startTimeL, endTimeL, orderStatus);
		
        int count = storeOrderDelegator.searchStoreOrdersCount(orderSeq, clothesNum, expressOrderNo, skuNo,
				orderType, storeNumber, receiver, phone, startTimeL, endTimeL, orderStatus);
		pageQueryResult.setRecordCount(count);
        
        data.put("list", storeOrderVO);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
		
	}

	@RequestMapping(value = "/detail", method = RequestMethod.GET)
	@ResponseBody
    public JsonResponse srchDetail(@RequestParam(value = "order_no") long orderNo) {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		StoreOrderDetailVO storeOrderDetailVO = storeOrderDelegator.loadStoreOrderDetailInfo(orderNo);
		data.put("order_detail", storeOrderDetailVO);
		
		return jsonResponse.setSuccessful().setData(data);
	}
	
	//订单发货
	@RequestMapping(value = "/delivery")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse delivery(HttpSession httpSession, 
    		@RequestParam(value = "order_no") long orderNo,
			@RequestParam(value = "store_id") long storeId,
			@RequestParam(value = "supplier") String supplier, 
			@RequestParam(value = "express_no") String expressNo){
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		
		if(adminUser == null){
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError("session的userinfo为空");
		}
		
		try{
			storeOrderDelegator.delivery(orderNo, storeId, supplier, expressNo, adminUser);
		}catch (Exception e) {
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
                               @RequestParam(value = "store_id") long storeId,
                               @RequestParam(value = "old_order_status") int oldStatus,
                               @RequestParam(value = "message", required = false, defaultValue = "") String message,HttpServletRequest request){
		AdminUser adminUser = (AdminUser)httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		
		ResultCode resultCode = null;
		try {
			resultCode = storeOrderDelegator.cancel(orderNo, storeId, oldStatus, message, adminUser,request);
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
	                           @RequestParam(value = "store_id") long storeId,
	                           @RequestParam(value = "old_order_status") int oldStatus){
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		JsonResponse jsonResponse = new JsonResponse();
		ResultCode resultCode = null;
		
		try {
			resultCode = storeOrderDelegator.revoke(orderNo, storeId, oldStatus, adminUser);
		} catch (ParameterErrorException e) {
			resultCode = ResultCode.COMMON_ERROR_BAD_PARAMETER;
			return jsonResponse.setResultCode(resultCode).setError(e.getMessage());
		}
		
		return jsonResponse.setSuccessful().setResultCode(resultCode);
	}
	
	@RequestMapping(value = "/undelivered/search", method = RequestMethod.GET)
	@ResponseBody
	public JsonResponse searchUndelivered(@RequestParam(value = "page", required = false, defaultValue = "1")int page,
			@RequestParam(value = "page_size", required = false, defaultValue = "10")int pageSize,
			@RequestParam(value = "order_no", required = false, defaultValue = "")String orderNo,
			@RequestParam(value = "clothes_no", required = false, defaultValue = "")String clothesNum,
			@RequestParam(value = "express_no", required = false, defaultValue = "")String expressOrderNo,
			@RequestParam(value = "sku_no", required = false, defaultValue = "")String skuNo,
			@RequestParam(value = "store_number", required = false, defaultValue = "-1")long storeNumber,
			@RequestParam(value = "receiver", required = false, defaultValue = "")String receiver,
			@RequestParam(value = "phone", required = false, defaultValue="")String phone,
			@RequestParam(value = "starttime", required = false, defaultValue = "1970-01-01 00:00:00") String startTime,
			@RequestParam(value = "endtime", required = false, defaultValue = "")String endTime) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
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
		
		List<List<StoreOrderVO>> storeOrdersVO = storeOrderDelegator.searchUndelivered(pageQuery,orderSeq, clothesNum,
            expressOrderNo, skuNo, storeNumber, receiver, phone, startTimeL, endTimeL);
		int count = storeOrderDelegator.searchUndeliveredCount(orderSeq, clothesNum, expressOrderNo, skuNo, storeNumber, receiver, phone, startTimeL, endTimeL);
		pageQueryResult.setRecordCount(count);
		
		data.put("list", storeOrdersVO);
		data.put("total", pageQueryResult);
		
		return jsonResponse.setSuccessful().setData(data);
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
                                 @RequestParam(value = "store_id") long storeId,
                                 @RequestParam(value = "old_order_status") int oldStatus) {
        JsonResponse jsonResponse = new JsonResponse();
     
        storeOrderDelegator.recovery(orderNo,mergedId,parentId,storeId,oldStatus);
        
        return jsonResponse.setSuccessful();
	}
	
	/**
	 * 批发订单 - 发货
	 * 
	 * @param pageQuery
	 * @param orderNo 订单编号
	 * @param receiver 收件人
	 * @param expressNo 快递单号
	 * @param clothesNum 商品款号
	 * @param businessNumber 商家号
	 * @param skuNo skuid
	 * @param phone 手机号
	 * @param warehouseId 订单仓库 1:自建仓 2:品牌仓
	 * @param brandOrderNo 品牌商订单编号
	 * @param brandOrderStatus 品牌商订单状态  1:待处理 2：已处理 3：已撤单 4：已退单
	 * @param startTime 创建时间
	 * @param endTime 结束时间
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/wholesale/undelivered/search")
	@ResponseBody
	@AdminOperationLog
    public JsonResponse searchWholesaleUndelivered(PageQuery pageQuery,
			@RequestParam(value = "order_no", required = false) String orderNo,
			@RequestParam(value = "receiver", required = false) String receiver,
			@RequestParam(value = "express_no", required = false) String expressNo,
			@RequestParam(value = "clothes_no", required = false) String clothesNum,
			@RequestParam(value = "business_number", required = false) Long businessNumber,
			@RequestParam(value = "sku_no", required = false) Long skuNo,
			@RequestParam(value = "phone", required = false) String phone,
			@RequestParam(value = "warehouse_id", required = false) Long warehouseId,
			@RequestParam(value = "brand_order_no", required = false) String brandOrderNo,
			@RequestParam(value = "brand_order_status", required = false) Integer brandOrderStatus,
			@RequestParam(value = "starttime", required = false, defaultValue = "1970-01-01 00:00:00") String startTime,
			@RequestParam(value = "endtime", required = false, defaultValue = "") String endTime) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		Map<String, Object> data = new HashMap<String, Object>();
		PageQueryResult pageQueryResult = new PageQueryResult();
		BeanUtils.copyProperties(pageQuery, pageQueryResult);
		
    	long endTimeL = 0L;
    	long startTimeL = DateUtil.convertToMSEL(startTime);
		if(StringUtils.equals(endTime, "")) {
			endTimeL = System.currentTimeMillis();
		} else {
			endTimeL = DateUtil.convertToMSEL(endTime);
		}
        
        StoreOrderSO so = new StoreOrderSO(orderNo, receiver, expressNo, clothesNum, businessNumber, skuNo, phone, warehouseId, brandOrderNo, brandOrderStatus, startTimeL, endTimeL);
		data.put("list", storeOrderDelegator.searchWholesaleUndelivered(pageQuery, so));
		data.put("total", PageQueryResult.copyFrom(pageQuery, storeOrderDelegator.searchWholesaleUndeliveredCount(so)));
		
		return jsonResponse.setSuccessful().setData(data);
	}

	/**
	 * 发单
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
		storeOrderDelegator.dispatchOrder(orderNo, adminUser, remark);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 撤销发单
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
		storeOrderDelegator.revokeDispatchOrder(orderNo, adminUser, remark);
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 审核
	 * 
	 * @param httpSession
	 * @param orderNo
	 * @param remark
	 * @param status -1不通过，0通过
	 * @return
	 */
	@RequestMapping("/check/pass")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse checkPass(HttpSession httpSession, @RequestParam("order_no") long orderNo,
			@RequestParam("remark") String remark, @RequestParam("status") int status) {
		JsonResponse jsonResponse = new JsonResponse();
		
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		if (status == 0) {
			storeOrderDelegator.checkPass(orderNo, adminUser, remark);
		} else if (status == -1) {
			storeOrderDelegator.checkDenyPass(orderNo, adminUser, remark);
		}
		
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	
	/**
	 * 添加业务留言
	 * @return
	 */
	@RequestMapping("/message/add")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse addMessage(HttpSession httpSession, @RequestParam(value = "order_no") long orderNo,
								@RequestParam(value = "type") int type,
								@RequestParam(value = "operation") String operation,
								@RequestParam(value = "message") String message){
		AdminUser adminUser = (AdminUser) httpSession.getAttribute("userinfo");
		StoreOrderMessageBoard storeOrderMessageBoard = new StoreOrderMessageBoard(orderNo, adminUser.getUserId(), adminUser.getUserName(), type, operation, message);
		storeOrderMessageBoard.setCreateTime(System.currentTimeMillis());
		
		storeOrderMessageBoardDao.add(storeOrderMessageBoard);
		
		JsonResponse jsonResponse = new JsonResponse();
		return jsonResponse.setSuccessful().setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
     * @param page
     * @param pageSize
     * @param orderNo
     * @param type 0:业务处理 1:业务操作 2:订单移交记录
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
        
        Map<String, Object> data = new HashMap<String, Object>();
        
        data.put("admin", (AdminUser) httpSession.getAttribute("userinfo"));
        data.put("admins", userService.search());
        if (type == 0 || type == 1) {
        	data.put("list", storeOrderMessageBoardDao.search(pageQuery, orderNo, Arrays.asList(new Integer[]{type}), adminId, startTimeL, endTimeL));
        	data.put("total", PageQueryResult.copyFrom(pageQuery, storeOrderMessageBoardDao.searchCount(orderNo, Arrays.asList(new Integer[]{type}), adminId, startTimeL, endTimeL)));
		} else if (type == 2) {
        	data.put("list", storeOrderMessageBoardService.search(pageQuery, orderNo, Arrays.asList(new Integer[]{3,4,5,6,7}), adminId, startTimeL, endTimeL));
        	data.put("total", PageQueryResult.copyFrom(pageQuery, storeOrderMessageBoardDao.searchCount(orderNo, Arrays.asList(new Integer[]{3,4,5,6,7}), adminId, startTimeL, endTimeL)));
		}
        
        JsonResponse jsonResponse = new JsonResponse();
        return jsonResponse.setSuccessful().setData(data);
	}
	
    /**
     * 导出EXCEL
     * @param response
     * @param startTime
     * @param endTime
     * @param type 0:自建仓 1:其他仓
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping("/delivery/excel")
    @AdminOperationLog
    @ResponseBody
	public JsonResponse outExcel(HttpServletResponse response,
                         @RequestParam(value = "starttime", required = false, defaultValue = "") String startTime,
                         @RequestParam(value = "endtime", required = false, defaultValue = "") String endTime,
                         @RequestParam(value = "type") int type) throws IOException, ParseException {
    	JsonResponse jsonResponse = new JsonResponse();
        long currenTime = System.currentTimeMillis();

        long startTimeL = DateUtil.convertToMSEL(startTime);
        long endTimeL = currenTime;
        if(!StringUtils.equals(endTime, "")) {
        	endTimeL = DateUtil.convertToMSEL(endTime);
        }
        
        List<Map<String, Object>> list = storeOrderDelegator.deliveryExcel(type, startTimeL, endTimeL);
		
		String columnNames[] = {"订单编号", "用户ID", "产品ID", "产品名称", "颜色", "尺码", "单价", "销售数量", "买家留言", "订单状态", "拍下时间", "付款时间", "收货人姓名", "收货人电话", "收货人地址",
				"物流单号","物流公司"};//列名
        String keys[] = {"orderNo","storeId", "productId","productName","color","size","money","buyCount","remark",
        		"orderStatus","createTime", "payTime","receiver","phone","expressInfo","expressNo","expressCompamyName"};//map中的key
        
        if(type == 1) {
        	ExcelUtil.exportExcel(response, list, keys, columnNames, "其他仓");
        } else if(type == 0) {
        	ExcelUtil.exportExcel(response, list, keys, columnNames, "自建仓");
        }
        
        return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
    
    /**
     * v3.5关闭订单
     * @param orderNo
     * @return
     */
    @RequestMapping("/closeOrder")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse closeOrder( @RequestParam(value = "order_no") long orderNo){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
			if (storeOrderNew == null) {
				return jsonResponse.setError("订单不存在！");
			}
			if (storeOrderNew.getOrderStatus() != 10) {//已付款
				return jsonResponse.setError("该订单不能关闭！");
			}
			storeOrderNewService.closeOrder(storeOrderNew);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("关闭订单失败！");
		}
	}
	
    /**
     * v3.5 挂起订单    指取消订单的自动确认收货功能，不影响其他功能。设置订单的挂起状态为hang_up:1   在自动确认收货的定时任务中排除该状态 com.jiuy.core.service.task.OrderSuccessJob
     * @param orderNo
     * @return
     */
    @RequestMapping("/hangUpOrder")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse hangUpOrder( @RequestParam(value = "order_no") long orderNo){
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			StoreOrderNew storeOrderNew = storeOrderNewService.getStoreOrderByOrderNo(orderNo);
			if (storeOrderNew == null) {
				return jsonResponse.setError("订单不存在！");
			}
			if (storeOrderNew.getOrderStatus() != 50) {//已发货
				return jsonResponse.setError("该订单不能挂起！");
			}
			if (storeOrderNew.getHangUp() == 1) {//已挂起
				return jsonResponse.setError("订单已挂起！");
			}
			storeOrderNewService.hangUpOrder(storeOrderNew);
			return jsonResponse.setSuccessful().setData("ok");
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError("挂起订单失败！");
		}
	}
	
}
