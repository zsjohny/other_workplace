/**
 * 
 */
package com.yujj.web.controller.wap;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.yujj.entity.account.UserDetail;
import com.yujj.entity.order.Coupon;
import com.yujj.web.controller.delegate.OrderDelegator;

/**
 * @author LWS
 *
 */
@Login
@Controller
@RequestMapping("/wap/coupon")
public class WapCouponController {
    
    @Autowired
    private OrderDelegator orderDelegator;
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    //public JsonResponse list(@RequestParam("payCash") double payCash,PageQuery pageQuery,UserDetail userDetail,ClientPlatform clientPlatform) {
    public JsonResponse list(@RequestParam("payCash") double payCash,@RequestParam(value="sid_count") String[] skuCountPairArray, UserDetail userDetail,
    		@RequestParam(value = "cityName", required = false, defaultValue = "")String cityName, ClientPlatform clientPlatform) {
        //JsonResponse jsonResponse = new JsonResponse();
        // TODO 为了解决冲突先注释掉了
        //List<Coupon> couponList = orderDelegator.getUserCouponList(userDetail.getUserId(), OrderCouponStatus.UNUSED);
        //couponList.addAll(orderDelegator.getUserCouponList(userDetail.getUserId(), OrderCouponStatus.USED));
        //couponList.addAll(orderDelegator.getUserCouponList(userDetail.getUserId(), OrderCouponStatus.SCRAP));
        //System.out.println(userDetail.getUserId());
        //System.out.println(couponList);
        //Map<String,Object> result = orderDelegator.getCouponList(null, pageQuery, userDetail);
        JsonResponse jsonResponse = orderDelegator.buildOrder185(userDetail.getUserId(), cityName, skuCountPairArray, clientPlatform,0);
        List<Coupon> couponList = (List<Coupon>) ((Map<String,Object>)jsonResponse.getData()).get("couponList");
        //System.out.println(couponList);
        //List<Coupon> couponList = (List<Coupon>) result.get("couponList");
        for (int i = couponList.size()-1; i >=0; i--) {
        	if(couponList.get(i).getMoney()>=payCash){
				couponList.remove(i);
			}
		}
        //System.out.println(couponList);
        return jsonResponse.setSuccessful().setData(couponList);
    }
}
