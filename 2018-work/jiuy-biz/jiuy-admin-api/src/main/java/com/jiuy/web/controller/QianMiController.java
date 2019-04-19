package com.jiuy.web.controller;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuy.core.business.facade.QianMiFacade;
import com.jiuy.core.exception.ParameterErrorException;
import com.jiuy.web.controller.util.DateUtil;
import com.jiuy.web.delegate.ErpDelegator;
import com.jiuy.web.delegate.QianMiDelegator;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.web.help.JsonResponse;
import com.qianmi.open.api.ApiException;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
@Controller
@RequestMapping("/qianmi")
public class QianMiController {
	
	@Autowired
	private QianMiDelegator qianMiDelegator;
	
	@Autowired
	private QianMiFacade qianMiFacade;
	
	@Autowired
	private ErpDelegator erpDelegator;
	
	/**
	 * 批量上传商品到千米
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/product/batch/add")
	@ResponseBody
	public JsonResponse batchAddProduct() throws ApiException {
		JsonResponse jsonResponse = new JsonResponse();
		qianMiDelegator.batchAddProduct();
		return jsonResponse.setSuccessful();
	} 
	
	/**
	 * 上传商品到千米
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/product/add")
	@ResponseBody
	public JsonResponse addProduct(@RequestParam("product_id") Long id) {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			qianMiDelegator.addProduct(id, null, null);
		} catch (ApiException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		} catch (MalformedURLException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		return jsonResponse.setSuccessful();
	}
	
	/**
	 * 将千米订单里的百事汇通仓库的部分导入到本地
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping("/orders/pull")
	@ResponseBody
	public JsonResponse pullOrders(@RequestParam(value = "start_time", defaultValue = "2016-09-12 12:20:20", required = false) String startTime, 
			@RequestParam(value = "end_time", defaultValue = "2016-12-12 12:20:20", required = false) String endTime) {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			qianMiDelegator.pullOrders(startTime, endTime);
		} catch (ApiException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		} catch (ParseException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 合并千米订单
	 * 
	 * @return
	 */
	@RequestMapping("/orders/merge")
	@ResponseBody
	public JsonResponse mergeOrders() {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			qianMiFacade.mergeOrders();
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 推送本地千米订单到旺店通
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/wdt/push/orders")
	@ResponseBody
	public JsonResponse pushQMOrdersToWdt(@RequestParam("start_time") String startTime, @RequestParam("end_time") String endTime) throws ParseException {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			List<Map<String ,Object>> eRPqmOrders = erpDelegator.assembleQMOrders(DateUtil.convertToMSEL(startTime), DateUtil.convertToMSEL(endTime));	
			Set<Long> successOrderNos = erpDelegator.pushQMOrders(eRPqmOrders);
			if (successOrderNos.size() > 0) {
				qianMiFacade.recordPushTime(successOrderNos);
	        }
		} catch (ParameterErrorException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 物流记录：包括本地记录、ERP物流回写状态、千米物流记录
	 * 
	 * @return
	 */
	@RequestMapping("/wdt/writeback/logistics")
	@ResponseBody
	public JsonResponse writeBackLogistics() {
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			qianMiDelegator.writeBackLogistics();
		} catch (ParameterErrorException e) {
			
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_BAD_PARAMETER).setError(e.getMessage());
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 物流记录：千米物流记录
	 * 
	 * @param orderNo
	 * @return
	 */
	@RequestMapping("/wdt/writeback/logistics/item")
	@ResponseBody
	public JsonResponse writeBackLogisticsItem(@RequestParam("order_no")Long orderNo) { 
		JsonResponse jsonResponse = new JsonResponse();
		
		Set<Long> orderNos = new HashSet<>();
		orderNos.add(orderNo);
		try {
			qianMiDelegator.syncLogisticsToQianMi(orderNos);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
	/**
	 * 功能还没好stash- 废弃
	 * @return
	 */
	@RequestMapping("/category/add")
	@ResponseBody
	@Deprecated
	public JsonResponse addCategory() { 
		JsonResponse jsonResponse = new JsonResponse();
		
		try {
			qianMiDelegator.addCategories();
		} catch (ApiException e) {
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_UNKNOWN).setError(e.getMessage());
		}
		
		return jsonResponse.setResultCode(ResultCode.COMMON_SUCCESS);
	}
	
}
