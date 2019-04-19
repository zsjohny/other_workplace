package com.store.service.store;

import com.enums.PropertiesEnums;
import com.jiuyuan.entity.store.StoreWxa;
import com.store.dao.mapper.ShopRefundMapper;
import com.util.ConstantId;
import com.util.PropertiesUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyuan.constant.ServiceAdvice;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.store.entity.member.ShopMember;
import com.store.service.MemberService;
import com.store.service.ShopMemberOrderService;
import com.store.service.StoreUserService;

import java.util.List;

/**
 * 服务通知
 * @author Administrator
 *
 */
@Service
public class ServiceAdviceFacade {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAdviceFacade.class);

   // public static String weixinServiceUrlWx=WeixinPayConfig.getWeiXinServerUrl();
	//public static String weixinServiceUrl = WeixinPayConfig.getWeiXinServerUrl();
     public static String weixinServiceUrl="https://local.yujiejie.com/jweixin";//测试先用  上线后变动
//	public static String waitPayAdviceUrl = "/serviceAdvice/waitPayAdvice";
	public static String paySuccessAdviceUrl = "/serviceAdvice/paySuccessAdvice";
	public static String orderCancelAdviceUrl = "/serviceAdvice/orderCancelAdvice";
	@Autowired
	ServiceAdviceService serviceAdviceService;
	
	@Autowired
	private ShopMemberOrderService shopMemberOrderService;
	
    @Autowired
    private StoreUserService storeUserService;
    
	@Autowired
	private ShopRefundMapper shopRefundMapper;

	@Autowired
	MemberService memberService;
	
    /**
     * 待付款提醒
     */
	public void waitPayAdvice(long orderId ) {
		payAdvice(orderId,ServiceAdvice.waitPayAdvice);
	}
	 /**
     * 付款成功通知
     */
	public void paySuccessAdvice(long orderId) {
		payAdvice(orderId,ServiceAdvice.paySuccessAdvice);
	}
	 /**
     * 订单取消通知
     */
	public void orderCancelAdvice(long orderId) {
		payAdvice(orderId,ServiceAdvice.orderCancelAdvice);
	}

	 /**
     * 支付相关通知
     */
	private void payAdvice(long shopMemberOrderId,ServiceAdvice serviceAdvice) {
		if(StringUtils.isEmpty(weixinServiceUrl)){
			logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
			return ;
		}
		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(shopMemberOrderId);
		if(shopMemberOrder == null){
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			logger.info("发送支付相关通知时，根据shopMemberOrderId"+shopMemberOrderId+"获取订单为空，请尽快排查问题！！！！！！！！！！！");
			return ;
		}
		//获取APPID
		Long storeId = shopMemberOrder.getStoreId();
		//StoreBusiness storeBusiness = storeUserService.getStoreBusinessById(storeId);

		StoreBusiness storeBusiness = shopRefundMapper.selectType(storeId);
		//获取OpenId
		long memberId = shopMemberOrder.getMemberId();
		ShopMember member = memberService.getMemberById(memberId);
		String openId =null;
		String appId=null;
		// TODO: 2018/12/18 判断是否是店中店
		if (storeBusiness.getWxaBusinessType()==1){//1是共享版的
			Long inShopMemberId = member.getInShopMemberId();
			if (inShopMemberId==null){
				inShopMemberId=memberId;
			}
			ShopMember shopMember = shopRefundMapper.selectBindWeixin(inShopMemberId);

			openId=shopMember.getBindWeixin();
            //storeId=ConstantId.skipShopStoreId();
			storeId=Long.parseLong( PropertiesUtil.getPropertiesByKey(PropertiesEnums.PROPERTIES_CONSTANTS.getKey(),PropertiesEnums.CONSTANTS_SHOP_IN_ID.getKey()));
			List<StoreWxa> storeWxas = shopRefundMapper.selectAppid(storeId);
			StoreWxa storeWxa = null;
			if (storeWxas.size() > 0) {
				storeWxa = storeWxas.get(0);
			}
			appId=storeWxa.getAppId();
		}else {
			openId = member.getBindWeixin();
			appId=storeBusiness.getWxaAppId();
		}

		String form_id = shopMemberOrder.getPayFormId();
		if(StringUtils.isNotEmpty(form_id) && StringUtils.isNotEmpty(openId) && StringUtils.isNotEmpty(appId)){
			logger.info("开始发送支付相关模板通知");
			serviceAdviceService.sendTemplateAdvice(form_id,openId,appId,serviceAdvice,shopMemberOrderId);
		}else{
			logger.info("必要条件缺失无法发送服务通知，请注意排查问题！");
		}
	}
}
