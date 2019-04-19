package com.jiuy.wxa.api.controller.wxa;

import com.jiuy.rb.mapper.user.StoreBusinessRbMapper;
import com.jiuy.rb.model.user.StoreBusinessRb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.service.shop.IShopMemberReservationsOrderService;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * 预约试穿订单
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
//@Login
@RequestMapping("/member/reservations/order")
public class WxaMemberReservationsOrderController {
    private static final Logger logger = LoggerFactory.getLogger(WxaMemberReservationsOrderController.class);
    Log log = LogFactory.get();
    
	@Autowired
	private StoreBusinessRbMapper storeBusinessRbMapper;
	@Autowired
	private IShopMemberReservationsOrderService shopMemberReservationsOrderService;

	/**
     * 添加预约试穿订单
     * @return
     */
    @RequestMapping("/addReservationsOrder")
    @ResponseBody
    public JsonResponse addReservationsOrder(ShopDetail shopDetail,MemberDetail memberDetail,
    		@RequestParam("shopProductId") long shopProductId,
    		@RequestParam(value="platformProductSkuId",required=false,defaultValue="0") long platformProductSkuId,
    		@RequestParam(value="shopProductSizeName",required=false,defaultValue="") String shopProductSizeName,
    		@RequestParam(value="shopProductColorName",required=false,defaultValue="") String shopProductColorName,
    		@RequestParam("shopMemberName") String shopMemberName,
    		@RequestParam("shopMemberPhone") String shopMemberPhone,
    		@RequestParam("appointmentTime") String appointmentTime) {
//    	JsonResponse jsonResponse = new JsonResponse();
//    	try {
////    		checkStore(shopDetail);
////    	 	checkMember(memberDetail);
//
////    	 	Integer shopReservationsOrderSwitch = shopDetail.getStoreBusiness().getShopReservationsOrderSwitch();
//
//			StoreBusinessRb store = storeBusinessRbMapper.findReservationsSwitchAndUCid(storeId);
//			if (store == null) {
//				throw new RuntimeException(ResultCode.COMMON_ERROR_NOT_LOGGED_IN.getDesc());
//			}
//			Integer shopReservationsOrderSwitch = store.getShopReservationsOrderSwitch();
//			if(shopReservationsOrderSwitch !=1){
//    	 		logger.error("添加预约试穿订单:未开启预约试穿功能：shopReservationsOrderSwitch："+ shopReservationsOrderSwitch);
//    			return jsonResponse.setError("未开启预约试穿功能");
//    	 	}
//    	 	logger.info("添加预约试穿订单storeId:"+storeId+";memberId:"+memberId+";shopProductId:"+shopProductId+
//					";platformProductSkuId:"+platformProductSkuId+";shopProductSizeName:"+shopProductSizeName+";shopProductColorName:"+shopProductColorName+
//					";shopMemberName:"+shopMemberName+";shopMemberPhone:"+shopMemberPhone+";appointmentTime:"+appointmentTime);
//        	shopMemberReservationsOrderService.addSubscribeOrder(store.getId(),memberId,shopProductId,platformProductSkuId,shopProductSizeName,
//        			shopProductColorName,shopMemberName,shopMemberPhone,appointmentTime, store.getUserCID());
//        	return jsonResponse.setSuccessful();
//		} catch (Exception e) {
//			logger.error("添加预约试穿订单有误:"+e.getMessage());
//    		e.printStackTrace();
//			return jsonResponse.setError("添加预约试穿订单有误");
//		}
		return JsonResponse.successful();
    }
    


}