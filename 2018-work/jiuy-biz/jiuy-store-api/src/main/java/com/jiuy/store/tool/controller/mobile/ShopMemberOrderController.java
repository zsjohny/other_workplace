package com.jiuy.store.tool.controller.mobile;

import java.util.Map;

import com.jiuy.rb.service.order.IMemberOrderService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.AdminOperationLog;
import com.jiuyuan.web.help.JsonResponse;
import com.store.service.NJShopMemberOrderService;
import com.store.service.ShopMemberOrderService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

import javax.annotation.Resource;

/**
 * <p>
 * 会员订单表 前端控制器
 * </p>
 *
 * @author 赵兴林
 * @since 2017-09-05
 */
@Controller
@RequestMapping("/mobile/memberOrder")
public class  ShopMemberOrderController{
	private static final Log logger = LogFactory.get("app订单");
	
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
	
	@Autowired
	private NJShopMemberOrderService njShopMemberOrderService;

	@Resource(name = "memberOrderService")
	private IMemberOrderService memberOrderService;

	/**
	 * 根据订单ID获取订单详情
	 * @param orderId
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/getOrderDetailById/auth")
	@ResponseBody
	public JsonResponse getOrderDetailById(long orderId,UserDetail userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		Map<String,Object> map = shopMemberOrderService.getOrderDetailById(orderId);
		return jsonResponse.setSuccessful().setData(map);
	}
	/**
	 * 获取订单
	 * @param
	 * orderStatus ：订单状态：-1:全部、0:待付款、1:待提货、4:订单完成、3:订单关闭、5:待发货;6:已发货
	 * @return
	 */
	@RequestMapping("/getMemberOrderList/auth")
	@ResponseBody
	public JsonResponse getMemberOrderList(
			@RequestParam(value = "current",defaultValue = "0",required = false)int current,
			@RequestParam(value = "size" ,defaultValue = "30",required = false) int size,
			int orderStatus,
			UserDetail userDetail
			){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
		SmallPage smallPage = shopMemberOrderService.getMemberOrderList( current, size,storeId,orderStatus);
		return jsonResponse.setSuccessful().setData(smallPage);
	}
	/**
	 * 关闭订单接口
	 */
	@RequestMapping("/stopOrderById/auth")
	@ResponseBody
	public JsonResponse stopOrderById(long orderId, UserDetail userDetail,
									  @RequestParam(value = "type",defaultValue = "0",required = false)Integer type//0 默认  1 店中店标示
	){


		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		njShopMemberOrderService.stopOrderById(orderId,userDetail.getId());
		return jsonResponse.setSuccessful();
	}
	/**
	 * 确认提货接口
	 * @param userDetail
	 * @return
	 */
	@RequestMapping("/confirmDeliveryById/auth")
	@ResponseBody
	public JsonResponse confirmDeliveryById(@RequestParam("data")String data,UserDetail userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			Map<String, String> result = njShopMemberOrderService.confirmDeliveryById (data, userDetail.getId ());
			//核销成功后增加销量,这里使用rebuild工程
			if (result != null && "核销成功".equals (result.get ("result"))) {
				logger.info ("可以提货,开始更新订单信息以及销量");
				memberOrderService.confirmDelivery(storeId, Long.parseLong (result.get ("orderId")));
				result.remove ("orderId");
			}
			return jsonResponse.setSuccessful().setData(result);
		} catch (Exception e) {
			e.printStackTrace ();
			logger.error("确认二维码获取参数"+e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 申请发货
	 */
	@RequestMapping("/applyDelivery/auth")
	@ResponseBody
	public JsonResponse applyDelivery(@RequestParam("member_order_no") Long orderNo ,
			                          UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			Map<String,Object> data = shopMemberOrderService.applyDelivery(orderNo,storeId);
			return jsonResponse.setSuccessful().setData(data);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
	/**
	 * 提交发货
	 */
	@RequestMapping("/submitDelivery/auth")
	@ResponseBody
	@AdminOperationLog
	public JsonResponse submitDelivery(@RequestParam("memberOrderNo") Long memberOrderNo,//订单号
			                           @RequestParam("expressSupplierEngName") String EngName,//快递商英文名
			                           @RequestParam("expressSupplierCnName") String CnName,//快递商中文名
			                           @RequestParam("expressNo") String expressNo,//快递号
			                           UserDetail<StoreBusiness> userDetail){
		JsonResponse jsonResponse = new JsonResponse();
		long storeId = userDetail.getId();
		if(storeId == 0){
			logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
			return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
		}
		try {
			shopMemberOrderService.submitDelivery(memberOrderNo,storeId,EngName,CnName,expressNo);
			return jsonResponse.setSuccessful();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return jsonResponse.setError(e.getMessage());
		}
	}
	
}