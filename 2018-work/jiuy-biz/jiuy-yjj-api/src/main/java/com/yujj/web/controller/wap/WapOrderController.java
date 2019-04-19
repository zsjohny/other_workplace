/**
 * 网页订单页面
 */
package com.yujj.web.controller.wap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.OrderType;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.util.oauth.sns.weixin.WeiXinV2API;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.business.service.OrderService;
import com.yujj.entity.account.UserDetail;
import com.yujj.web.controller.delegate.OrderDelegator;

/**
* @author DongZhong
* @version 创建时间: 2016年12月15日 上午11:42:29
*/
@Login
@Controller
@RequestMapping("/wap/order")
public class WapOrderController {

    private static final Logger logger = LoggerFactory.getLogger("MobileOrderController");

    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderDelegator orderDelegator;

    @Autowired
    @Qualifier("weiXinV2API4MP")
    private WeiXinV2API weiXinV2API;
    /**
     * 构建订单
     * 
     * @param cash
     * @param userDetail
     * @param clientPlatform
     * @param ip
     * @return
     */
    @RequestMapping("/buildCash")
    @ResponseBody
    public JsonResponse build(@RequestParam("cash") double cash, @RequestParam("storeId") long storeId, UserDetail userDetail, ClientPlatform clientPlatform, @ClientIp String ip) {
    	
    	
    		JsonResponse jsonResponse = new JsonResponse();
    	    return jsonResponse.setError("此功能正在开发中");
    	    
//    		logger.error("----------------buildCash = "+"cash:"+cash + ", userDetail:"+userDetail.getUserId());
//
//    		long time = System.currentTimeMillis();
//    		
//        	OrderNew orderNew = new OrderNew();
//        	orderNew.setCoinUsed(0);
//        	orderNew.setExpressInfo("浙江省杭州市滨江区秋溢路288号1幢1208室");
//        	orderNew.setIp(ip);
//        	orderNew.setOrderStatus(OrderStatus.UNPAID);
//        	orderNew.setPlatform(clientPlatform.getPlatform().toString());
//        	orderNew.setPlatformVersion(clientPlatform.getVersion());
//        	orderNew.setRemark("门店现金支付订单");
//        	orderNew.setStatus(0);
//        	orderNew.setTotalExpressMoney(0);
//        	orderNew.setTotalMoney(cash);
//        	orderNew.setTotalPay(cash);
//        	orderNew.setUserId(userDetail.getUserId());
//        	orderNew.setCommission(cash);
//        	orderNew.setAvailableCommission(cash);
//        	orderNew.setOrderType(2);
//        	orderNew.setBelongBusinessId(storeId);
//        	orderNew.setCreateTime(time);
//        	orderNew.setUpdateTime(time);
//        	
//            //计算订单支付过期时间
//            JSONArray jsonArray = globalSettingService.getJsonArray(GlobalSettingName.ORDER_SETTING);
//            int expiredTime = 24 * 60;
//        	for(Object obj : jsonArray) {
//        		expiredTime = (int) ((JSONObject)obj).get("overdueMinutes");
//        		break;
//        	}
//
//        	orderNew.setExpiredTime(System.currentTimeMillis() + expiredTime * 60 * 1000);
//      	
//        	Map<String, Object> data = new HashMap<String, Object>();
//        	orderNewMapper.insertOrder(orderNew);
//        	
//        	OrderItem orderItem = new OrderItem();
//        	orderItem.setOrderId(0);
//        	orderItem.setProductId(0);
//        	orderItem.setSkuId(0);
//        	orderItem.setTotalMoney(cash);
//        	orderItem.setTotalExpressMoney(0);
//        	orderItem.setMoney(cash);
//        	orderItem.setExpressMoney(0);
//        	orderItem.setBuyCount(1);
//        	orderItem.setSkuSnapshot("");
//        	orderItem.setCreateTime(time);
//        	orderItem.setUpdateTime(time);
//        	orderItem.setBrandId(0);
//        	orderItem.setlOWarehouseId(0);
//        	orderItem.setTotalPay(cash);        	
//        	orderItem.setOrderNo(orderNew.getOrderNo());
//        	orderItem.setUserId(userDetail.getUserId());
//        	
//        	orderItemMapper.insertOrderItems(orderNew.getOrderNo(), CollectionUtil.createList(orderItem));
//        	data.put("orderNo", orderNew.getOrderNo());
//        	
//    		return jsonResponse.setSuccessful().setData(data);
    }
    /*
     * @param orderNo
     */
    @RequestMapping("/getOrder")
    @ResponseBody
    public JsonResponse getOrder(@RequestParam("orderNo") long orderNo, UserDetail userDetail) {
    	JsonResponse jsonResponse = new JsonResponse();
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	data.put("orderInfo", orderService.getUserOrderNewByNoAll(userDetail.getUserId(), orderNo+""));
    	//data.put("orderInfo", orderService.getUserOrderNewByNoAll(19, 2+""));
    	//System.out.println(data);
    	return jsonResponse.setSuccessful().setData(data);
    }
    
    

    /**
     * 构建订单,并返回给确认订单页面
     * 
     * @param reqdev
     * @param skuId
     * @param count
     * @param userDetail
     * @return
     */
    
    @RequestMapping("/build")
    @ResponseBody
    public JsonResponse build(@RequestParam(value="sid_count") String[] skuCountPairArray, UserDetail userDetail,
    		@RequestParam(value = "cityName", required = false, defaultValue = "")String cityName, ClientPlatform clientPlatform) {
    	return new JsonResponse().setResultCode(ResultCode.ORDER_OUT_OF_SERVICE);
    	
    	
//    		//skuCountPairArray = new String[]{"479:1"};
//    		logger.error("--------WAP--------build = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", userDetail:"+userDetail.getUserId()+", cityName:"+cityName);
//    		
//    		JsonResponse jsonResponse = orderDelegator.buildOrder185(userDetail.getUserId(), cityName, skuCountPairArray, clientPlatform,0);
//    		//System.out.println(jsonResponse.getData());
//    		//JsonResponse jsonResponse = orderDelegator.buildOrder185(userDetail.getUserId(), "", skuCountPairArray, null);
//    		return jsonResponse;
    }
    
    
    
    /**
     * 微信网站端创建订单接口，生成订单
     * @param skuCountPairArray
     * @param addressId
     * @param orderType
     * @param expressSupplier
     * @param expressOrderNo
     * @param phone
     * @param remark
     * @param cartIds 
     * @param totalCash 优惠后的商品价格 + 邮费价格
     * @param userSharedRecordId 分享
     * @param clientPlatform
     * @param ip
     * @param userDetail
     * @param takeGood        1自提取货
     * @return
     */
    @RequestMapping(value = "/confirm", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse confirm(@RequestParam(value="sid_count") String[] skuCountPairArray,
    		@RequestParam(value="addr_id") int addressId, @RequestParam(value="type",required=false) OrderType orderType,
    		@RequestParam(value = "express_supplier", required = false) String expressSupplier,
    		@RequestParam(value = "express_order_no", required = false) String expressOrderNo,
    		@RequestParam(value = "phone", required = false) String phone, @RequestParam(value="remark",required=false)String remark,
    		@RequestParam(value = "cartIds", required = false) Long[] cartIds,
    		@RequestParam(value = "couponId", required = false) String couponId,
    		@RequestParam(value = "statisticsIds", required = false) String statisticsIds, //应为CODE
    		@RequestParam(value = "payCash", required = false, defaultValue = "0") double payCash,
    		@RequestParam(value = "userSharedRecordId", required = false, defaultValue = "0") long userSharedRecordId,
    		@RequestParam(value = "coinDeductFlag", required = false, defaultValue = "0") int coinDeductFlag,
    		@RequestParam(value = "take_good", required = false, defaultValue = "0") int takeGood,
    		ClientPlatform clientPlatform, @ClientIp String ip, UserDetail userDetail) {
    	return new JsonResponse().setResultCode(ResultCode.ORDER_OUT_OF_SERVICE);
//    	
//    		logger.error("---------WAP-------confirm = "+"sid_count:"+Arrays.toString(skuCountPairArray) + ", payCash:"+payCash);
//    		orderType = OrderType.PAY;
//    		clientPlatform = ClientPlatform .getWeiXinClient(); 		
//    		JsonResponse jsonResponse = orderDelegator.confirmOrder185(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
//    				phone, remark, cartIds, clientPlatform, ip, userDetail, payCash, couponId, statisticsIds ,userSharedRecordId, coinDeductFlag,takeGood);
//    		
//    		
//    		//JsonResponse jsonResponse = orderDelegator.confirmOrder185(skuCountPairArray, addressId, orderType, expressSupplier, expressOrderNo,
//    				//phone, remark, cartIds, clientPlatform, ip, userDetail, payCash, couponId, statisticsIds);
//    		//System.out.println(jsonResponse.getData());
//    		return jsonResponse;
    	
    }
	private OrderType OrderType(int i, int j) {
		
		return null;
	}
    
}
