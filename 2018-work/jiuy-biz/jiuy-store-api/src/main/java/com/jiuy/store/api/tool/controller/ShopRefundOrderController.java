package com.jiuy.store.api.tool.controller;

import java.util.HashMap;
import java.util.Map;

import com.jiuyuan.service.common.*;
import com.jiuyuan.util.BizUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.servlet.http.HttpServletRequest;

/**
* 门店售后订单Controller
* @author Qiuyuefan
*/
//@Login
@Controller
@RequestMapping("/shop/refundOrder")
public class ShopRefundOrderController {

	private static final Log logger = LogFactory.get(ShopRefundOrderController.class);
	

    
	@Autowired
	private IRefundOrderFacade refundOrderFacade;

	@Autowired
	private IRefundOrderService refundOrderService;
	
	@Autowired
	private IRefundOrderFacadeNJ nJRefundOrderFacade;
	
	/**
     * 买家申请平台介入
     * @param
     * @param userDetail
     * @return
     */
    @RequestMapping("/storeApplyPlatformIntervene/auth")
    @ResponseBody
    public JsonResponse storeApplyPlatformIntervene(long refundOrderId,//售后订单ID
    		                        UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
    	try {
    		refundOrderFacade.storeApplyPlatformIntervene(refundOrderId,storeId);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
	
	
	/**
     * 获取售后订单列表
     * @return
     */
    @RequestMapping("/getRefundOrderList/auth")
    @ResponseBody
    public JsonResponse getRefundOrderList(
    		 @RequestParam(value="current",required=false,defaultValue="1")Integer current,
			 @RequestParam(value="size",required=false,defaultValue="10")Integer size,
			 UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
			SmallPage data = refundOrderFacade.getRefundOrderList(storeId,current,size);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取售后订单列表:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 检测重复申请售后
     */
    @RequestMapping("/checkApplyRefund/auth")
    @ResponseBody
    public JsonResponse checkApplyRefund(@RequestParam("orderNo") Long orderNo,
    		                             UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	    try {
   	    	logger.info("开始重新售后！");
 		//检测重复申请售后
 		refundOrderFacade.checkApplyRefund(orderNo,storeId);
 		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    /**
     * 申请售后
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping("/applyRefund/auth")
    @ResponseBody
    public JsonResponse applyRefund(@RequestParam("orderNo") Long orderNo,//订单orderNo
    		                        @RequestParam("refundType") Integer refundType,//退款类型 1:仅退款 2:退货退款
    		                        UserDetail<StoreBusiness> userDetail,
									HttpServletRequest request
									){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
   	 	if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
    	try {
    		//获取售后退款信息
			Integer version = BizUtil.getVersion(request);
    		Map<String,Object> data =  refundOrderFacade.applyRefund(orderNo,storeId,refundType,version);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    

    
    /**
     * 提交售后订单
     * @param refundType
     * @param refundReason
     * @param refundCost
     * @param refundRemark
     * @param refundProofImages
     * @param
     * @param orderNo
     * @param userDetail
     * @return
     */
    @RequestMapping("/submitRefundOrder/auth")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse submitRefundOrder(@RequestParam("refundType") Integer refundType,//售后类型 1：仅退款 2：退货退款
    		                              @RequestParam("refundReason") String refundReason,//退款原因
    		                              @RequestParam("refundReasonId") Long refundReasonId,//退款原因ID
    		                              @RequestParam("orderNo") Long orderNo,//订单ID
    		                              @RequestParam("refundCost") Double refundCost,//退款申请金额
    		                              @RequestParam(value = "refundRemark",required = false,defaultValue = "") String refundRemark,//退款说明
    		                              @RequestParam(value = "refundProofImages",required = false,defaultValue = "") String refundProofImages,//退款证明图片
    		                               UserDetail<StoreBusiness> userDetail,
										  HttpServletRequest request
										  ){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	if(storeId == 0){
    		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	try {
    		Map<String,Object> data = new HashMap<>();
    		logger.info("提交售后订单orderNo："+orderNo);
    		synchronized(refundOrderFacade){
				Integer version = BizUtil.getVersion(request.getHeader("X-User-Agent"));
    			data = refundOrderFacade.submitRefundOrder(refundType,refundReason,refundReasonId,orderNo,refundCost,refundRemark,refundProofImages,storeId,userDetail.getUserDetail().getBusinessName(),userDetail.getUserDetail().getPhoneNumber(),
						version);
    		}
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
    		e.printStackTrace();
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
   
    }
    /**
     * 获取售后订单详情
     * @return
     */
    @RequestMapping("/getRefundOrderInfo/auth")
    @ResponseBody
    public JsonResponse getRefundOrderInfo(long refundOrderId,
			 UserDetail<StoreBusiness> userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		long storeId = userDetail.getId();
    		Map<String,Object> data = refundOrderFacade.getRefundOrderInfo(refundOrderId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("获取售后订单详情:"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
	/**
	 * 退货退款
	 * 卖家已同意，待买家发货
	 * 申请退货发货接口
	 */
	@RequestMapping("/customerDelivery/auth")
	@ResponseBody
	public JsonResponse customerDelivery(@RequestParam("orderNo") Long orderNo,
			                             UserDetail<StoreBusiness> userDetail){
    	JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	if(storeId == 0){
    		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	try {
    		Map<String,Object> data = refundOrderFacade.customerDelivery(orderNo,storeId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 买家提交售后发货
	 */
	@RequestMapping("/submitDelivery/auth")
	@ResponseBody 
	public JsonResponse submitDelivery(@RequestParam("expressNo") String expressNo,//快递单号
			                           @RequestParam("expressSupplierId") Long expressSupplierId,//供应商ID
			                           @RequestParam("expressSupplierEngName") String expressSupplierEngName,//供应商名称
			                           @RequestParam("expressSupplierCNName") String expressSupplierCNName,//供应商中文名称
			                           @RequestParam("refundOrderId") Long refundOrderId,//售后订单ID
									   UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	if(storeId == 0){
    		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	try {
    		refundOrderFacade.submitDelivery(expressNo,expressSupplierId,expressSupplierEngName,storeId,refundOrderId,expressSupplierCNName);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
    /**
     * 买家撤销售后订单
     */
	@RequestMapping("/cancelRefundOrder/auth")
	@ResponseBody
	public JsonResponse cancelRefundOrder(@RequestParam("refundOrderId") Long refundOrderId,
			                              UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
    	long storeId = userDetail.getId();
    	if(storeId == 0){
    		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
    		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
    	}
    	try {
    		nJRefundOrderFacade.cancelRefundOrder(refundOrderId);
    		return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}

	@RequestMapping("/refund/newRefundMoney/auth")
	@ResponseBody
	public JsonResponse dealMoney(@Param("refundOrderId")Long refundOrderId) {
		JsonResponse jsonResponse = refundOrderFacade.dealMoney(refundOrderId);
		return jsonResponse;
	}

	
}