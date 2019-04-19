//package com.jiuy.supplier.modular.web.controller;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.baomidou.mybatisplus.plugins.Page;
//import com.jiuy.supplier.core.shiro.ShiroKit;
//import com.jiuy.supplier.core.shiro.ShiroUser;
//import com.jiuy.web.helper.JsonResponse;
//import com.jiuyuan.constant.ResultCode;
//import com.jiuyuan.util.SmallPage;
//import com.jiuyuan.util.anno.AdminOperationLog;
//import com.supplier.entity.WithdrawApply;
//import com.supplier.service.MyFinanceSupplierService;
//import com.xiaoleilu.hutool.log.Log;
//import com.xiaoleilu.hutool.log.LogFactory;
//
//@Controller
//@RequestMapping("/myFinance")
//public class MyFinanceController {
//	private static final Log logger = LogFactory.get("MyFinanceController");
//	
//	@Autowired
//	private MyFinanceSupplierService myFinanceSupplierService;
//	
//	
//	/**
//	 * 我的收入
//	 * 销售总额
//	 */
//	@RequestMapping("/getTotalOrderAmount")
//	@ResponseBody
//	public JsonResponse getTotalOrderAmount(){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		try {
//			Map<String,Object> data = myFinanceSupplierService.getTotalOrderAmount(supplierId);
//			return jsonResponse.setSuccessful().setData(data);
//		} catch (Exception e) {
//			logger.error(e);
//			return jsonResponse.setError(e.getMessage());
//		}
//	}
//	
//	/**
//	 * 提现记录列表接口
//	 * @param supplierId 供应商ID
//	 * @param minApplyMoney 申请金额下限
//	 * @param maxApplyMoney 申请金额上限
//	 * @param minCreateTime 格式：yyyy-MM-dd HH:mm:ss
//	 * @param maxCreateTime 格式：yyyy-MM-dd HH:mm:ss
//	 * @param status -1：全部，0：未处理，1：处理完成
//	 * @return
//	 */
//	@RequestMapping("/getWithdrawList")
//	@ResponseBody
//	public JsonResponse getWithdrawList(@RequestParam(value = "minApplyMoney",required = false,defaultValue = "0.0") double minApplyMoney,
//			                            @RequestParam(value = "maxApplyMoney",required = false,defaultValue = "0.0") double maxApplyMoney,
//			                            @RequestParam(value = "minCreateTime",required = false,defaultValue = "1970-1-1 00:00:00") String minCreateTime,
//			                            @RequestParam(value = "maxCreateTime",required = false,defaultValue = "") String maxCreateTime,
//			                            @RequestParam(value = "status",required = false,defaultValue = "-1") int status,
//			                            @RequestParam(value = "current",required = false,defaultValue = "1") int current,
//			                            @RequestParam(value = "size",required = false,defaultValue = "14") int size){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		Page page = new Page(current,size);
//		try {
//			List<WithdrawApply> data = myFinanceSupplierService.getWithdrawOrderList(page,supplierId,minApplyMoney,maxApplyMoney,minCreateTime,maxCreateTime,status);
//			page.setRecords(data);
//			return jsonResponse.setSuccessful().setData(new SmallPage(page));
//		} catch (Exception e) {
//			logger.error(e);
//			return jsonResponse.setError(e.getMessage());
//		}
//	}
//	
//    /**
//     * 通过订单ID查看提现订单详情
//     * @param id
//     * @return
//     */
//	@RequestMapping("/getWithdrawOrderInfo")
//	@ResponseBody
//	public JsonResponse getWithdrawOrderInfo(@RequestParam("id") long id){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		WithdrawApply data = myFinanceSupplierService.getWithdrawOrderInfo(id);
//		return jsonResponse.setSuccessful().setData(data);
//	}
//    /**
//     * 获取正在处理的提现申请订单数目
//     * @param supplierId
//     * @return
//     */
//	@RequestMapping("/getCountOfDealingWDOrder")
//	@ResponseBody
//	public JsonResponse getCountOfDealingWDOrder(){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		int count = myFinanceSupplierService.getCountOfDealingWDOrder(supplierId);
//		return jsonResponse.setSuccessful().setData(count);
//	}
//	/**
//	 * 可提现金额数
//	 */
//	@RequestMapping("/availableBalance")
//	@ResponseBody
//	public JsonResponse availableBalance(){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		Map<String, Object> map = new HashMap<>();
//		double availableBalance = myFinanceSupplierService.getAvailableBalance(supplierId);
//		map.put("availableBalance", availableBalance);
//		return jsonResponse.setData(map);
//	}
//	/**
//	 * 提交提现按钮
//	 * @param applyMoney
//	 * @return
//	 */
//	@RequestMapping("/submitApply")
//	@ResponseBody
//	@AdminOperationLog
//	public JsonResponse submitApply(@RequestParam("applyMoney") double applyMoney){
//		JsonResponse jsonResponse = new JsonResponse();
//		long supplierId = ShiroKit.getUser().getId();
//		if(supplierId == 0){
//   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
//   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
//   	 	}
//		try {
//			myFinanceSupplierService.submitApply(supplierId,applyMoney);
//			return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			logger.error(e);
//			return jsonResponse.setError(e.getMessage());
//		}
//	}
//	
//
//}
