package com.jiuy.wxa.api.controller.wxa;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.enums.PropertiesEnums;
import com.jiuy.rb.mapper.user.ShopMemberRbMapper;
import com.jiuy.rb.service.account.ICoinsAccountService;
import com.jiuy.rb.service.coupon.ICouponServerNew;
import com.jiuy.rb.service.payment.IPaymentService;
import com.jiuyuan.dao.mapper.supplier.ProductSkuNewMapper;
import com.jiuyuan.dao.mapper.supplier.ShopProductMapper;
import com.jiuyuan.entity.newentity.*;
import com.jiuyuan.service.common.MyStoreActivityService;
import com.jiuyuan.util.BizUtil;
import com.jiuyuan.util.HttpClientUtils;
import com.store.dao.mapper.ShopMemberOrderItemMapper;
import com.store.dao.mapper.ShopRefundMapper;
import com.store.dao.mapper.StoreBusinessMapper;
import com.store.entity.YjjStoreBusinessAccountLog;
import com.util.CallBackUtil;
import com.util.MapTrunPojo;
import com.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.constant.order.PaymentType;
import com.jiuyuan.dao.mapper.supplier.SecondBuyActivityMapper;
import com.jiuyuan.dao.mapper.supplier.TeamBuyActivityMapper;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.newentity.weixinpay.Signature;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayCore;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.entity.store.StoreWxa;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.ResultCodeException;
import com.jiuyuan.web.help.JsonResponse;
import com.store.dao.mapper.ShopMemberOrderMapper;
import com.store.entity.MemberDetail;
import com.store.entity.ShopDetail;
import com.store.entity.member.ShopMember;
import com.store.service.MemberService;
import com.store.service.ShopMemberOrderService;
import com.store.service.StoreWxaService;
import com.store.service.WxaPayService;
import com.store.service.store.ServiceAdviceFacade;
import com.yujj.web.controller.wap.pay2.WxPayResultData;

/**
 * 微信小程序支付
 *
 * @author zhaoxinglin
 * 一、页面请求调用微信的统一下单接口，获得prepay_id返回页面。
 * 二、通过页面JS请求微信支付接口（注：只能在微信自带的浏览器使用）
 * 
 */
@Controller
@RequestMapping("/miniapp/pay")
public class WxaPayController {
    private static final Logger logger = LoggerFactory.getLogger(WxaPayController.class);
    
    @Autowired
	private StoreBusinessMapper storeBusinessMapper;
    @Autowired
    ServiceAdviceFacade serviceAdviceFacade;
    @Autowired
	private ShopMemberOrderItemMapper shopMemberOrderItemMapper;
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
	
    @Autowired
    MemberService memberService;
    
    @Autowired
	StoreWxaService storeWxaService;
    
    @Autowired
	private ShopMemberRbMapper shopMemberRbMapper;
    @Autowired
    private WxaPayService wxaPayService;
    
    @Autowired
	private TeamBuyActivityMapper teamBuyActivityMapper;

    @Autowired
    private ShopRefundMapper shopRefundMapper;
    @Autowired
    private SecondBuyActivityMapper secondBuyActivityMapper;
    
    @Autowired
	private ShopMemberOrderMapper shopMemberOrderMapper;

    @Resource(name = "paymentService")
	private IPaymentService paymentService;

    @Autowired
    private ICoinsAccountService coinsAccountService;

	@Resource(name = "couponServerNew")
	private ICouponServerNew couponServerNew;

	@Autowired
	private ShopProductMapper shopProductMapper;

	@Autowired
	private ProductSkuNewMapper productSkuNewMapper;




	/**
	 * 将待入账的玖币转到已入账
	 *
	 * @author Aison
	 * @date 2018/7/23 9:16
	 * @return com.jiuyuan.web.help.JsonResponse
	 */
	@RequestMapping("/release")
	@ResponseBody
	public JsonResponse release() {
//		try{
//			coinsAccountService.wait2in();
//			return new JsonResponse().setSuccessful();
//		}catch (Exception e) {
//		    e.printStackTrace();
//			return BizUtil.exceptionHandler(e);
//		}
		return new JsonResponse().setSuccessful();
	}




    /**
     * 公众号微信支付回调
     * http://wxalocal.yujiejie.com/miniapp/pay/notify_url/weixin_wxa.do
     * @param requestBody
     * @return
     * 	小程序支付回调返回结果requestBody: <xml><appid><![CDATA[wxf99f985dc7f79695]]></appid>
	 * 	<attach><![CDATA[赵东豪]]></attach>
	 * 	<bank_type><![CDATA[CFT]]></bank_type>
	 * 	<cash_fee><![CDATA[1]]></cash_fee>
	 * 	<fee_type><![CDATA[CNY]]></fee_type>
	 * 	<is_subscribe><![CDATA[N]]></is_subscribe>
	 * 	<mch_id><![CDATA[1459044102]]></mch_id>
	 * 	<nonce_str><![CDATA[95e1533eb1b20a97777749fb94fdb944]]></nonce_str>
	 * 	<openid><![CDATA[o01of0dPAIy1w2HXQmfyKgJi2qxo]]></openid>
	 * 	<out_trade_no><![CDATA[15051167929554309]]></out_trade_no>
	 * 	<result_code><![CDATA[SUCCESS]]></result_code>
	 * 	<return_code><![CDATA[SUCCESS]]></return_code>
	 * 	<sign><![CDATA[CA50E7F5BF0A98726361325A3EC9B5FA]]></sign>
	 * 	<time_end><![CDATA[20170911160039]]></time_end>
	 * 	<total_fee>1</total_fee>
	 * 	<trade_type><![CDATA[JSAPI]]></trade_type>
	 * 	<transaction_id><![CDATA[4000122001201709111503831435]]></transaction_id>
	 * 	</xml>
     */
    @RequestMapping("/notify_url_wxa")
    @ResponseBody
    public String weixinPayCallbackPublic(@RequestBody String requestBody
										 ) {
        logger.info("小程序支付回调返回结果requestBody:"+requestBody);
        PaymentType paymentType = PaymentType.WEIXINPAY_WXA;

        Map<String, String> params = WeixinPayCore.decodeXml(requestBody);
        logger.info("解析回调数据map，params:"+params.toString());
        //订单编号
        String orderNumber = params.get("out_trade_no");

        ShopMemberOrder shopMemberOrder =  shopMemberOrderService.getMemberOrderByNum(orderNumber);
        if (shopMemberOrder == null) {
            logger.error("小程序支付回调处理时根据订单编号["+orderNumber+"]没有找到订单，请尽快排查!!!");
            return "";
        }

        Integer type = shopRefundMapper.selectStoreBusinessNew(shopMemberOrder.getStoreId());
        if (type==1){//店中店 共享
			String id = PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(),PropertiesEnums.CONSTANTS_SHOP_IN_ID.getKey());
			shopMemberOrder.setStoreId(Long.valueOf(id));
//			shopMemberOrder.setStoreId(3L);
//        	shopMemberOrder.setStoreId(11878L);//测试线3L  线上为11878L
		}
        logger.info("根据订单out_trade_no:"+orderNumber+",shopMemberOrder:"+shopMemberOrder);

        long orderId = shopMemberOrder.getId();
        String paymentNo = params.get("transaction_id");
        logger.info("微信订单号paymentNo：{}", paymentNo);

        try {
        	//支付秘钥
        	long storeId = shopMemberOrder.getStoreId();
        	StoreWxa storeWxa = storeWxaService.getStoreWxaByStoreId(String.valueOf(storeId));
    		String apiKey = storeWxa.getPayKey();
            if (Signature.checkIsSignValidFromResponseString(params,apiKey)) {
                String resultCode = params.get("result_code");
                logger.error("微信支付回调结果resultCode：{}, out_trade_no:{}, transactionId:{}",resultCode, orderNumber, paymentNo);
                if (resultCode.equals("SUCCESS") && shopMemberOrder.getOrderStatus() == ShopMemberOrder.order_status_pending_payment) {
                	//更新订单支付信息
                    //shopMemberOrderService.updateOrderAlreadyPayStatus(shopMemberOrder, paymentNo, paymentType);
					paymentService.updateOrderAlreadyPayStatus(shopMemberOrder, paymentNo, paymentType);
					//发送付款成功通知给微信会员
					serviceAdviceFacade.paySuccessAdvice(orderId);

					liveRoomMsgSend(shopMemberOrder);


					try{
						couponServerNew.grantWxaOrder(orderNumber);
					}catch (Exception e) {
						e.printStackTrace();
					}
                }else{
                	logger.error("微信支付回调后未进行修改订单状态，结果resultCode：{}, out_trade_no:{}, paymentNo:{}",resultCode, orderNumber, paymentNo);
                }
            } else {
                logger.error("回调验证失败, orderNumber:{}, transactionId:{}", orderNumber, paymentNo);
            }
        } catch (Exception e) {
        	  logger.error("小程序支付回调处理时出现错误，请尽快排查!!!");
        	e.printStackTrace();
            logger.error("", e);
            return "";
        }
        return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    }

	private void liveRoomMsgSend(ShopMemberOrder shopMemberOrder) {
		//直播商品购买平台推送
    	long orderId = shopMemberOrder.getId();
		long storeId = shopMemberOrder.getStoreId();

		Wrapper<ShopMemberOrderItem> itemQuery = new EntityWrapper<>();
		itemQuery.eq("order_id", orderId);
		List<ShopMemberOrderItem> shopMemberOrderItems = shopMemberOrderItemMapper.selectList(itemQuery);
        logger.info("发送直播间下单通知 items={}", shopMemberOrderItems);
		if (! shopMemberOrderItems.isEmpty()) {
			Long memberId = shopMemberOrder.getMemberId();
			boolean needLiveSendTalk = false;
			for (ShopMemberOrderItem shopMemberOrderItem : shopMemberOrderItems) {
				Long liveProductId = shopMemberOrderItem.getLiveProductId();
				if (liveProductId != null && liveProductId > 0) {
					needLiveSendTalk = true;
					break;
				}
			}
			if (needLiveSendTalk) {
				for (ShopMemberOrderItem item : shopMemberOrderItems) {
					Long liveProductId = item.getLiveProductId();
					if (liveProductId != null && liveProductId > 0) {
						JSONObject paramJson = new JSONObject();
						paramJson.put("liveProductId", liveProductId);
						paramJson.put("orderItemId", item.getId());
						paramJson.put("memberId", memberId);
						paramJson.put("type", 13);
						CallBackUtil.send(paramJson.toJSONString(),"product/product/live/room/message/sendSysTalk","get");
					}
				}
			}
		}
	}

	/**
     * 微信支付下单
     * http://dev.yujiejie:31080/miniapp/pay/wxaPay.json?storeId=1&memberId=126&orderId=1
     * @return
     */
    @RequestMapping("/wxaPay")
    @ResponseBody
    public JsonResponse wxaPay(
//    		ShopDetail shopDetail,MemberDetail memberDetail,
			long orderId,
			@ClientIp String ip, @RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId) {
				JsonResponse jsonResponse = new JsonResponse();
				try {
//   		 	checkStoreId(shopDetail);
//		  	checkMemberId(memberDetail);

//	    	String bindWeixin = member.getBindWeixin();
			String bindWeixin =	shopMemberRbMapper.findBindWeiXin(memberId);
	    	ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(orderId);

					//###########################################################改动分割线
//					Integer wxaBusinessType = storeBusiness.getWxaBusinessType();
//					//小程序店铺类型 0从未开通,1共享版(过期不设为0),2专项版(过期不设为0)
//					if (wxaBusinessType==1){
//						storeBusiness.setId(3L);//店中店的用户id先写死
//						Long inShopMemberId = member.getInShopMemberId();
//						ShopMember shopMember = memberService.selectShopMember(inShopMemberId);
//					}
					//########################################################
	    	//判断是否满足付款条件
//	    	if (!isFulfil(shopMemberOrder).isSuccessful()){
//	    		return isFulfil(shopMemberOrder);
//	    	}
	    	
	    	//根据购买方式进行一系列判断
			if(shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_miaosha){
				//秒杀活动
				SecondBuyActivity secondBuyActivity = secondBuyActivityMapper.selectById(shopMemberOrder.getSecondId());
				//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
				int haveActivityStatusInt = secondBuyActivity.haveActivityStatusInt();
				//秒杀活动手动结束或者订单超时
				if ((haveActivityStatusInt == 3 && secondBuyActivity.getActivityHandEndTime()!= 0)|| shopMemberOrderService.isOverTime(shopMemberOrder)) {
					logger.info(haveActivityStatusInt+""+shopMemberOrderService.isOverTime(shopMemberOrder));
//					throw new ResultCodeException(ResultCode.ACTIVE_OVER_TIME);
					return jsonResponse.setResultCode(ResultCode.ACTIVE_OVER_TIME);
				}
				//判断秒杀活动的结束类型
//				int type = secondBuyActivity.getSecondActivityFinishType();
				
			}
			
			if(shopMemberOrder.getBuyWay() == ShopMemberOrder.buy_way_tuangou){
				//团购活动
				TeamBuyActivity teamBuyActivity = teamBuyActivityMapper.selectById(shopMemberOrder.getTeamId());
				//活动状态：1待开始，2进行中，3已结束（手工结束、过期结束）
				int haveActivityStatusInt = teamBuyActivity.haveActivityStatusInt();
				//判断活动是否结束 
				if (haveActivityStatusInt == 3) {
					return jsonResponse.setResultCode(ResultCode.ACTIVE_OVER_TIME);
//					throw new ResultCodeException(ResultCode.ACTIVE_OVER_TIME);
				}

				//下单减库存,表明有订单一定有库存,这里只判断拼团是否成功
				boolean isTeamSuccess = ShopMemberOrderService.isTeamSuccess (teamBuyActivity);
				if (isTeamSuccess) {
					logger.info ("已达到成团条件{activityId:"+teamBuyActivity.getId ()+
							","+teamBuyActivity.getConditionType ()+
							""+teamBuyActivity.getUserCount ()+
							""+teamBuyActivity.getActivityMemberCount () +
							""+teamBuyActivity.getMeetProductCount () +
							""+teamBuyActivity.getOrderedProductCount ()
					);
				}
				else {
					StringBuilder builder = new StringBuilder ("当前活动参团数量不足，无法完成付款！还差");
					if (MyStoreActivityService.TEAM_TYPE_PRODUCT == teamBuyActivity.getConditionType ()) {
						builder.append (teamBuyActivity.getMeetProductCount () - teamBuyActivity.getOrderedProductCount ()).append ("件就可成团啦!");
					}
					else {
						builder.append (teamBuyActivity.getUserCount () - teamBuyActivity.getActivityMemberCount ()).append ("人就可成团啦!");
					}
					String tip = builder.toString ();
					return jsonResponse.setResultCode(ResultCode.ACTIVE_NEED_PEOPLE).setError(tip);
				}
			}

			StoreBusiness storeBusiness = storeBusinessMapper.getById(storeId);
			SortedMap<Object,Object> signMap = unifiedorder(storeBusiness,orderId,bindWeixin, ip, storeId, storeBusiness.getBusinessName());
    	 	return jsonResponse.setSuccessful().setData(signMap);
   	 	} catch (Exception e) {
   	 	logger.error("微信支付的接口:"+e.getMessage());
   	 	e.printStackTrace();
		if (e instanceof ResultCodeException) {
			ResultCodeException rce = (ResultCodeException)e;
			return jsonResponse.setResultCode(rce.getCode(),rce.getMessage());
		}
		return jsonResponse.setError(e.getMessage());
		}
    	
    }
  

	/**
     * 退款
     * http://dev.yujiejie:31080/miniapp/pay/wxaPay.json?storeId=1&memberId=126&orderId=1
     * @return
     */
    @RequestMapping("/refund")
    @ResponseBody
    public JsonResponse refund(ShopDetail shopDetail,MemberDetail memberDetail,long orderId,
							   @RequestParam("storeId")Long storeId, @RequestParam("memberId")Long memberId,
    		@ClientIp String ip) {
    	JsonResponse jsonResponse = new JsonResponse();
   	 	try {
//   		 	checkStoreId(shopDetail);
//		  	checkMemberId(memberDetail);
//		  	ShopMember member = memberDetail.getMember();
//	    	String bindWeixin = member.getBindWeixin();
//	    	StoreBusiness storeBusiness = shopDetail.getStoreBusiness();
	    	SortedMap<Object,Object> signMap = refund(storeId,orderId,shopMemberRbMapper.findBindWeiXin(memberId), ip);
    	 	return jsonResponse.setSuccessful().setData(signMap);
   	 	} catch (Exception e) {
   	 		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    	
    }
    /**
     * 退款（测试开发本机可退回）
     * @param orderId
     * @param bindWeixin
     * @param ip
     * @return
     */
    private SortedMap<Object, Object> refund(Long storeId, long orderId, String bindWeixin, String ip) {
    	StoreWxa storeWxa = storeWxaService.getStoreWxaByStoreId(String.valueOf(storeId));
    	String appId = storeWxa.getAppId();
		String mchId = storeWxa.getMchId();
		String payKey = storeWxa.getPayKey();
		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(orderId);
		String  orderNo = String.valueOf(shopMemberOrder.getOrderNumber());
		int total_fee = (int)(shopMemberOrder.getPayMoney()*100);//shopMemberOrder.getPayMoney();//1单位分    = 1;//
    	//1、退款
		String result = wxaPayService.refund(orderNo, bindWeixin,appId,mchId,payKey, ip, total_fee, total_fee);
    	//2、解析
    	logger.info("退款时微信返回数据result："+result);
		return null;
	}

	/**
     * 统一支付订单
     */
	private SortedMap<Object, Object> unifiedorder(StoreBusiness storeBusiness, long orderId, String bindWeixin, String ip, Long storeId, String businessName) {
		
		StoreWxa storeWxa = storeWxaService.getStoreWxaByStoreId(String.valueOf(storeId));
		if(storeWxa == null){
			throw new RuntimeException("该商家绑定小程序");
		}
		String appId = storeWxa.getAppId();
		String mchId = storeWxa.getMchId();
		if(mchId == null){
			throw new RuntimeException("该商家没有填写商户号");
		}
		String payKey = storeWxa.getPayKey();
		if(payKey == null){
			throw new RuntimeException("该商家没有填写商家秘钥");
		}
		
		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(orderId);
		if(shopMemberOrder == null){
			throw new RuntimeException("订单不存在");
		}
		
		String  orderNo = String.valueOf(shopMemberOrder.getOrderNumber());

		List<ShopMemberOrderItem> memberOrderItemList = shopMemberOrderService.getMemberOrderItemList(orderId);


		ShopMemberOrderItem shopMemberOrderItem = memberOrderItemList.get(0);
    	String productName = shopMemberOrderItem.getName();
    	int total_fee = (int)(shopMemberOrder.getPayMoney()*100);//shopMemberOrder.getPayMoney();//1单位分    = 1;//

		BizUtil.todo ("测试(已关)----下单钱统一1分钱");
//		total_fee = 1;


    	//2、订单统一支付
    	String result = wxaPayService.unifiedorder(orderNo,productName, total_fee, bindWeixin,businessName,appId,mchId,payKey, ip);
    	 
    	//3、解析微信下单接口返回的XML信息
	    WxPayResultData resultData = wxaPayService.buildWxPayResultData(result);
	 	System.out.println("微信支付下单返回,resultData:"+JSON.toJSONString(resultData));
		SortedMap<Object,Object> signMap = new TreeMap<Object,Object>();
    	if("SUCCESS".equals(resultData.getResult_code()) && "SUCCESS".equals(resultData.getReturn_code())){
	            //生成sign
	            signMap = wxaPayService.buildSignData(resultData,payKey);
	            String jsonStr = JSON.toJSONString(signMap);
	            logger.info("返回页面参数json："+jsonStr);
	            System.out.println("返回页面参数json："+jsonStr);
	            
	            //修改订单支付标识码

	            String prepay_id = resultData.getPrepay_id();
	            logger.info("开始修改订单支付标识prepay_id："+prepay_id+",orderId:"+orderId);
	            int ret = shopMemberOrderService.updatePrepayId(orderId,prepay_id);
	            logger.info("修改订单支付标识完成ret:"+ret+",prepay_id："+prepay_id+",orderId:"+orderId);
	           
	    }else{
	        	logger.info("微信支付下单错误,resultData:"+JSON.toJSONString(resultData));
	        	throw new RuntimeException(ResultCode.WAP_WEIXIN_PAY_UNIFIEDORDER_ERROE.getDesc());
	    }
    	return signMap;
	}

	@RequestMapping("test")
	@ResponseBody
	public String test(@RequestParam(value = "userId", required = false) Long userId,
					   @RequestParam(value = "orderNo", required = false) String orderNo
					   ){
		ShopMemberOrder shopMemberOrder =  shopMemberOrderService.getMemberOrderByNum(orderNo);
		liveRoomMsgSend(shopMemberOrder);

//
//		YjjStoreBusinessAccountLog yjjStoreBusinessAccountLog = new YjjStoreBusinessAccountLog();
//		yjjStoreBusinessAccountLog.setUserId(userId);
////				支出
//		yjjStoreBusinessAccountLog.setInOutType(1);
//		yjjStoreBusinessAccountLog.setOperMoney(30D);
//		yjjStoreBusinessAccountLog.setRemarks("商品消费-APP购买商品");
//		logger.info ("请求流水修改 url={},yjjStoreBusinessAccountLog={}", "/user/account/update",yjjStoreBusinessAccountLog);
//		Map map= MapTrunPojo.object2Map(yjjStoreBusinessAccountLog);
//		System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmm="+map);
//		CallBackUtil.send(JSONObject.toJSONString(map),"/user/account/update","get");
//		String id = PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(),PropertiesEnums.CONSTANTS_SHOP_IN_ID.getKey());
//		System.out.println(id);
		return "100";
	}

}
