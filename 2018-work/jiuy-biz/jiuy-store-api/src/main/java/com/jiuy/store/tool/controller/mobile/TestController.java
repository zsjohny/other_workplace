package com.jiuy.store.tool.controller.mobile;


import com.jiuyuan.web.help.JsonResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyuan.service.common.IStoreOrderNewService;
import com.jiuyuan.service.common.RefundSMSNotificationService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 客服聊天记录表 前端控制器
 * </p>
 * 
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/test")
public class TestController {
	 private static final Log logger = LogFactory.get();


		@Autowired
		private IStoreOrderNewService storeOrderNewService;
		
		@Autowired
		private RefundSMSNotificationService refundSMSNotificationService;



	@RequestMapping(value = "/test",method=RequestMethod.POST)
	@ResponseBody
	public JsonResponse test(@RequestParam("word") String word) {
		JsonResponse jsonResponse = new JsonResponse();

		return jsonResponse.setSuccessful().setData("调用成功");

	}
}

/*	@Autowired
	MemberMapper memberMapper;
    *//**
		 * 发送群发信息
		 * @return
		 *
		 *//*
    @RequestMapping("/test")
    @ResponseBody
    public JsonResponse test(String word) {
    	JsonResponse jsonResponse = new JsonResponse();
    	logger.info("word:"+word);
    	Wrapper<ShopMember> wrapper = new EntityWrapper<ShopMember>();
    			wrapper.eq("status", 0).eq("last_message_type", 0);
    	wrapper.andNew("").like("user_nickname", word);
    	wrapper.or("").like("memo_name", word);
    	wrapper.orderBy("create_time");
		List<ShopMember> list = memberMapper.selectList(wrapper);


   	 	return jsonResponse.setSuccessful().setData(list);
    }*/

//    /**
//     * 获取优惠券
//     */
//    @RequestMapping("/test2")
//    @ResponseBody
//    public JsonResponse test2(@RequestParam(required=true) long brandId,
//    		                  @RequestParam(required=true) double amount,
//    		                  UserDetail<StoreBusiness> userDetail){
//    	JsonResponse jsonResponse = new JsonResponse();
//    	long storeId = userDetail.getId();
//    	logger.info("storeId:"+storeId+"brandId:"+brandId+"amount:"+amount);
//    	List<StoreCouponNew> list = storeOrderNewService.availableCoupon(storeId, brandId, amount);
//    	return jsonResponse.setSuccessful().setData(list);
//    	
//    }



		/* ======================== 会员表测试 ============================
		 */
/*    @Autowired
	private IYjjMemberService yjjMemberService;

	@RequestMapping( "testMember" )
	@ResponseBody
	public JsonResponse test(Integer code, Double totalMoney, Integer type, String orderNo) {
//        yjjMemberService.findMemberByUserId (SystemPlatform.STORE, 3L);
//		yjjMemberService.buyMemberPackageOK (code, 3L, totalMoney, type, orderNo);

		return JsonResponse.getInstance ().setSuccessful ();
	}*/



