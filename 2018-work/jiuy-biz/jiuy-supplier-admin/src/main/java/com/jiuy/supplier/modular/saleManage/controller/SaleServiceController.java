package com.jiuy.supplier.modular.saleManage.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuyuan.dao.mapper.supplier.SupplierOrderMapper;
import com.jiuyuan.entity.newentity.StoreOrderNew;
import com.jiuyuan.service.common.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.admin.common.constant.factory.PageFactory;
import com.admin.core.base.controller.BaseController;
import com.baomidou.mybatisplus.plugins.Page;
import com.jiuy.supplier.core.shiro.ShiroKit;
import com.jiuy.supplier.core.shiro.ShiroUser;
import com.jiuy.supplier.modular.myProduct.controller.AllProductController;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.dao.mapper.supplier.RefundOrderMapper;
import com.jiuyuan.entity.newentity.RefundOrder;
import com.jiuyuan.service.common.IMyAccountSupplierService;
import com.jiuyuan.service.common.IRefundOrderSupplierFacade;
import com.jiuyuan.service.common.RefundOrderService;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;

import javax.annotation.Resource;

/**
 * 售后工单控制器
 *
 * @author fengshuonan
 * @Date 2017-12-15 14:14:12
 */
@Controller
@RequestMapping("/saleService")
public class SaleServiceController extends BaseController {
	@Autowired
	private RefundService refundService;

	private static final Logger logger = LoggerFactory.getLogger(AllProductController.class);
	  
    private String PREFIX = "/saleManage/saleService/";
    
	@Autowired
	private IRefundOrderSupplierFacade refundOrderSupplierFacade;
	
	@Autowired
	private IMyAccountSupplierService myAccountSupplierService;
	
	@Autowired
	private RefundOrderMapper refundOrderMapper;

	@Resource(name = "couponServerNew")
	private ICouponServerNew couponServerNew;

	@Autowired
	private SupplierOrderMapper supplierOrderMapper;

	/**
     * 供应商申请平台介入
     * refundOrderId 售后订单ID
     * @return
     */
    @RequestMapping(value = "/supplierApplyPlatformIntervene")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse supplierApplyPlatformIntervene( long refundOrderId) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser shiroUser = ShiroKit.getUser();
    	long userId = shiroUser.getId();
    	RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
   	 	if (refundOrder.getSupplierId() != userId) {
			return 	 jsonResponse.setError("没有权限操作该售后单！");
		}
		try {
			refundOrderSupplierFacade.supplierApplyPlatformIntervene(refundOrderId);
    		return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     *  获取售后订单列表
     * @return
     */
    @RequestMapping(value = "/getRefundOrderList")
    @ResponseBody
    public Object getRefundOrderList(@RequestParam(value="refundOrderNo", required=false, defaultValue = "0") long refundOrderNo,
    		@RequestParam(value="orderNo", required=false, defaultValue = "0") long orderNo,
    		@RequestParam(value="customerPhone", required=false, defaultValue = "") String storePhone,
    		@RequestParam(value="customerName", required=false, defaultValue = "") String storeName,
    		@RequestParam(value="refundStatus", required=false, defaultValue = "0") int refundStatus,
    		@RequestParam(value="refundType", required=false, defaultValue = "0") int refundType,
    		@RequestParam(value="refundReason", required=false, defaultValue = "") String refundReason,
    		@RequestParam(value="refundCostMin", required=false, defaultValue = "0") double refundCostMin,
    		@RequestParam(value="refundCostMax", required=false, defaultValue = "0") double refundCostMax,
    		@RequestParam(value="returnCountMin", required=false, defaultValue = "0") int returnCountMin,
    		@RequestParam(value="returnCountMax", required=false, defaultValue = "0") int returnCountMax,
    		@RequestParam(value="applyTimeMin", required=false, defaultValue = "") String applyTimeMin,
    		@RequestParam(value="applyTimeMax", required=false, defaultValue = "") String applyTimeMax,
    		@RequestParam(value="storeDealRefundTimeMin", required=false, defaultValue = "") String storeDealRefundTimeMin,
    		@RequestParam(value="storeDealRefundTimeMax", required=false, defaultValue = "") String storeDealRefundTimeMax) {
    	Page<Map<String,Object>> page = new PageFactory<Map<String,Object>>().defaultPage();
		JsonResponse jsonResponse = new JsonResponse();
		ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			//获取所有订单
			List<Map<String,Object>> selectList = refundOrderSupplierFacade.getRefundOrderList(userId,refundOrderNo,orderNo,storePhone,storeName,
					refundStatus,refundType,refundReason,refundCostMin,refundCostMax,returnCountMin,returnCountMax,applyTimeMin,applyTimeMax,
					storeDealRefundTimeMin,storeDealRefundTimeMax,page);
			page.setRecords(selectList);
			return super.packForBT(page);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取售后订单列表:"+e.getMessage());
    		throw new RuntimeException("获取售后订单列表:"+e.getMessage());
		}
    }
    
    /**
     *  获取售后订单原因列表
     * @return
     */
    @RequestMapping(value = "/getRefundReasonList")
    @ResponseBody
    public JsonResponse getRefundReasonList() {
		JsonResponse jsonResponse = new JsonResponse();
		try {
			//获取售后订单原因
			List<String> data = refundOrderSupplierFacade.getRefundReasonList();
			return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.error("获取售后订单列表:"+e.getMessage());
//    		throw new RuntimeException("获取售后订单列表:"+e.getMessage());
    		return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 获取收货地址列表
     */
    @RequestMapping("/getSupplierDeliveryAddressList")
    @ResponseBody
    public JsonResponse getSupplierDeliveryAddressList(){
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
    		int userId =  ShiroKit.getUser().getId();
    		Map<String,Object> data = refundOrderSupplierFacade.getSupplierDeliveryAddressList(userId);
    		return jsonResponse.setSuccessful().setData(data);
		} catch (RuntimeException e) {
			logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		} catch (Exception e) {
			logger.info(e.getMessage());
			return jsonResponse.setError("系统繁忙，请稍后再试！");
		}
    }
    
    
    /**
     *  获取售后订单信息
     * productId 商品ID
     * @return
     */
    @RequestMapping(value = "/getRefundOrderInfo")
    @ResponseBody
    public JsonResponse getRefundOrderInfo(long refundOrderId// 售后订单ID
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
		try {
			Map<String,Object>  data = refundOrderSupplierFacade.getRefundOrderInfo(refundOrderId);
    		return jsonResponse.setSuccessful().setData(data);
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 受理售后订单
     * @param dealType
     * @param dealRemark
     * @return
     */
    @RequestMapping(value = "/dealRefundOrder")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse dealRefundOrder(@RequestParam(value="refundOrderId") long refundOrderId,// 售后订单ID
    		@RequestParam(value="dealType") int dealType,//1：同意；2：拒绝
    		@RequestParam(value="dealRemark", required=false, defaultValue = "") String dealRemark,
    		@RequestParam("verifyCode") String verifyCode,//短信验证码
            @RequestParam(value = "send_type",required=false,defaultValue = "sms") String sendType,
            @RequestParam(value = "receiver",required =false,defaultValue="") String receiver,
            @RequestParam(value = "receiverPhone",required =false,defaultValue="") String receiverPhone,
            @RequestParam(value = "supplierReceiveAddress",required =false,defaultValue="") String supplierReceiveAddress
//										@RequestParam(value = "dealMoney")Double dealMoney//买家同意退款金额
    		) {
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
	 	if (refundOrder.getSupplierId() != userId) {
			return 	 jsonResponse.setError("没有权限操作该售后单！");
		}
   	 	String phoneNumber = shiroUser.getPhoneNumber();
		try {
			refundOrderSupplierFacade.dealRefundOrder(refundOrderId,dealType,dealRemark,phoneNumber,verifyCode,sendType, receiver, receiverPhone, supplierReceiveAddress);

			//3.7.9 优惠券退款


			double refundCost = refundOrder.getRefundCost();
			StoreOrderNew storeOrderNew = supplierOrderMapper.selectById(refundOrder.getOrderNo());


			BigDecimal dd = new BigDecimal(refundCost);
			BigDecimal cc = new BigDecimal(storeOrderNew.getTotalPay().doubleValue());
			int i=dd.compareTo(cc);
			String orderNo = refundOrder.getOrderNo().toString();
			logger.info("开始进行优惠券的回退："+refundCost+":"+storeOrderNew.getTotalPay()+":"+i+":"+orderNo);

			if(i>=0){
				logger.info("开始进行优惠券的回退：111");
				couponServerNew.rollbackCoupon(orderNo);
			}else {
				logger.info("开始进行优惠券的回退：2222");
			}

    		return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }


	/**
	 * 新系统退款
	 */
	@RequestMapping(value = "/refundMoneyNew")
	@ResponseBody
	public JsonResponse refundMoneyNew(@RequestParam("paymentNo")String  paymentNo,//支付凭证
									   @RequestParam("totalMoney")BigDecimal totalMoney,//订单总金额
									   @RequestParam("resons")String resons,//处理理由
									   @RequestParam("refundOrderNo")String refundOrderNo,//退款订单号
									   @RequestParam("refundMoney")String refundMoney//退款金额

									   ){

		try {
			Map<String, String> stringStringMap = refundService.weixinRefundNew(totalMoney, paymentNo, resons, refundOrderNo, refundMoney);
			return JsonResponse.successful();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return JsonResponse.fail(e.getMessage());
		}
	}

	/**
     * 卖家确认收货

     * @return
     */
    @RequestMapping(value = "/confirmReceipt")
    @ResponseBody
    @AdminOperationLog
    public JsonResponse confirmReceipt(@RequestParam(value="refundOrderId") long refundOrderId) {// 售后订单ID
    	JsonResponse jsonResponse = new JsonResponse();
    	ShiroUser shiroUser = ShiroKit.getUser();
		long userId = shiroUser.getId();
   	 	if(userId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	RefundOrder refundOrder = refundOrderMapper.selectById(refundOrderId);
   	 	if (refundOrder.getSupplierId() != userId) {
			return 	 jsonResponse.setError("没有权限操作该售后单！");
		}
		try {
			refundOrderSupplierFacade.confirmReceipt(refundOrderId);
    		return jsonResponse.setSuccessful();
    	} catch (Exception e) {
    		e.printStackTrace();
    		logger.info(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
    }
    
    /**
     * 受理获取验证码
     */
	@RequestMapping("/sendVerifyCode")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse sendVerifyCode(@RequestParam("phoneNumber") String phoneNumber){
		JsonResponse jsonResponse = new JsonResponse();
		long supplierId = ShiroKit.getUser().getId();
		if(supplierId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		try {
			Map<String,Object> map = myAccountSupplierService.sendVerifyCode(phoneNumber);
            return jsonResponse.setSuccessful().setData(map);
		} catch (Exception e) {
			return jsonResponse.setError(e.getMessage());
		}
	}
    /**
     * 跳转到售后工单首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "saleService.html";
    }

    /**
     * 跳转到添加售后工单
     */
    @RequestMapping("/saleService_add")
    public String saleServiceAdd() {
        return PREFIX + "saleService_add.html";
    }
    /**
     * 跳转到售后工单详情
     */
    @RequestMapping("/saleService_detail")
    public String saleServiceDetail() {
        return PREFIX + "saleService_detail.html";
    }
    /**
     * 跳转到修改售后工单
     */
    @RequestMapping("/saleService_update/{saleServiceId}")
    public String saleServiceUpdate(@PathVariable Integer saleServiceId, Model model) {
        return PREFIX + "saleService_edit.html";
    }

    /**
     * 获取售后工单列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        return null;
    }

    /**
     * 新增售后工单
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    public Object add() {
        return super.SUCCESS_TIP;
    }

    /**
     * 删除售后工单
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    public Object delete() {
        return SUCCESS_TIP;
    }


    /**
     * 修改售后工单
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    public Object update() {
        return super.SUCCESS_TIP;
    }

    /**
     * 售后工单详情
     */
    @RequestMapping(value = "/detail")
    @ResponseBody
    public Object detail() {
        return null;
    }
}
