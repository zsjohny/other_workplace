package com.store.service.store;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.jiuyuan.entity.newentity.ShopOrderAfterSale;
import com.jiuyuan.util.BizUtil;
import com.store.dao.mapper.ShopRefundMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotations.TableField;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.constant.ServiceAdvice;
import com.jiuyuan.entity.newentity.ShopMemberOrder;
import com.jiuyuan.entity.newentity.weixinpay.WeixinPayConfig;
import com.jiuyuan.entity.order.ShopMemberOrderItem;
import com.jiuyuan.service.common.MemcachedService;
import com.jiuyuan.util.DateUtil;
import com.store.service.ShopMemberOrderService;

import net.dongliu.requests.Requests;
import net.dongliu.requests.Response;
/**
 * 服务通知
 * @author Administrator
 *
 */
@Service
public class ServiceAdviceService {
    private static final Logger logger = LoggerFactory.getLogger(ServiceAdviceService.class);

    //Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
	public static String weixinServiceUrl = WeixinPayConfig.getWeiXinServerUrl();
	public static String sendTemplateAdviceUrl = "/serviceAdvice/sendTemplateAdvice";
	public static String getTemplateAdviceUrl = "/serviceAdvice/getTemplateAdvice";
	public static String addTemplateAdviceUrl = "/serviceAdvice/addTemplateAdvice";
	
	  @Autowired
	    private MemcachedService memcachedService;
	  @Autowired
		private ShopMemberOrderService shopMemberOrderService;
	@Autowired
	private ShopRefundMapper shopRefundMapper;
	
	
	
    /**
	 *
	 * form_id 标示
	 * openId 微信UId
	 *appId 小程序id
	 *
	 * shopMemberOrderId 订单号
     * 发送模板通知
     */
	public void sendTemplateAdvice(String form_id,String openId,String appId,ServiceAdvice serviceAdvice,long shopMemberOrderId) {
		
		
		logger.info("----------------------");
		logger.info("------发送模板通知sendTemplateAdvice----------------form_id:"+form_id);
		logger.info("------发送模板通知sendTemplateAdvice----------------openId:"+openId);
		logger.info("------发送模板通知sendTemplateAdvice----------------appId:"+appId);
		logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+JSON.toJSONString(serviceAdvice));
		logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+shopMemberOrderId);
		logger.info("----------------------");
		if(StringUtils.isEmpty(weixinServiceUrl)){
			logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
			return ;
		}
		Map<String, String> map = new HashMap<String, String>();
		map.put("page", buildPage(serviceAdvice,shopMemberOrderId));
		map.put("form_id", form_id);
		map.put("appId", appId);
		map.put("openId", openId);
		map.put("template_id", getTemplateId(appId,serviceAdvice));
//		map.put("template_title", serviceAdvice.getTitle());
//		map.put("template_keywordIds", serviceAdvice.getKeywordIds());
//		map.put("data", JSONObject.toJSONString(buildData(serviceAdvice,shopMemberOrder)));
		fillKeyword(map,serviceAdvice,shopMemberOrderId);
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("模板通知发送参数:"+map.toString());
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
//http://weixintest.yujiejie.com/Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
		//https://local.yujiejie.com/jweixin
		Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
	 	String ret = resp.getBody();
 	 	
 		logger.info("模板通知发送结果body:"+ret);
	}
	/** 售后
	 * form_id 标示
	 * openId 微信UId
	 *appId 小程序id
	 *
	 * shopMemberOrderId 订单号
	 * 发送模板通知
	 */
	public void serviceSenda(String form_id,Long skuId,String openId,String appId,ServiceAdvice serviceAdvice,long shopMemberOrderId) {
		logger.info("----------------------");
		logger.info("------发送模板通知sendTemplateAdvice----------------form_id:"+form_id);
		logger.info("------发送模板通知sendTemplateAdvice----------------openId:"+openId);
		logger.info("------发送模板通知sendTemplateAdvice----------------appId:"+appId);
		logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+JSON.toJSONString(serviceAdvice));
		logger.info("------发送模板通知sendTemplateAdvice----------------serviceAdvice:"+shopMemberOrderId);
		logger.info("----------------------");
		if(StringUtils.isEmpty(weixinServiceUrl)){
			logger.info("向微信会员发送图片客服消息weixinServiceUrl为空，请检查配置！！！！！！");
			return ;
		}
		Map<String, String> map = new HashMap<String, String>();
		//map.put("page", buildPage(serviceAdvice,shopMemberOrderId));
		map.put("form_id", form_id);
		map.put("appId", appId);
		map.put("openId", openId);
		//获取模板
		map.put("template_id", getTemplateId(appId,serviceAdvice));
		map.put("skuId",skuId.toString());
//		map.put("template_title", serviceAdvice.getTitle());
//		map.put("template_keywordIds", serviceAdvice.getKeywordIds());
//		map.put("data", JSONObject.toJSONString(buildData(serviceAdvice,shopMemberOrder)));
		fillKeyword(map,serviceAdvice,shopMemberOrderId);
        map.put("page", buildPage1(map,shopMemberOrderId));
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("模板通知发送参数:"+map.toString());
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
		logger.info("----------------------");
//Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
		Response<String> resp = Requests.get(weixinServiceUrl + sendTemplateAdviceUrl).params(map).text();
		String ret = resp.getBody();

		logger.info("模板通知发送结果body:"+ret);
	}



	/**
	 * 根据模板类型获得模板ID（如果无则创建）
	 * @param serviceAdvice
	 * @return
	 */
	private String getTemplateId(String appId,ServiceAdvice serviceAdvice) {
		//最终返回的模板ID
		String wxaTemplateId = "";
		
		//1、先从缓存读取对于模板ID 
		String groupKey = MemcachedKey.GROUP_KEY_WENXIN_ServiceAdvice;
		String key = appId+"_"+serviceAdvice.getId();
		logger.info("开始从缓存中获取模板ID，groupKey:"+groupKey+",key:"+key);
		Object obj = memcachedService.get(groupKey, key);
		if(obj != null){
			 wxaTemplateId = (String)obj;
			 logger.info("从缓存中获取模板ID成功，wxaTemplateId:"+wxaTemplateId+"groupKey:"+groupKey+",key:"+key);
			 return wxaTemplateId;
		}else{
			 logger.info("从缓存中获取模板ID失败，obj:"+obj+"groupKey:"+groupKey+",key:"+key);
		}
	
		//2、获取小程序账号下的模板ID
		wxaTemplateId = getWxaTemplateId(appId, serviceAdvice);
		
		//3、将模板ID放入缓存
		if(StringUtils.isNotEmpty(wxaTemplateId)){
			int expiry = 7000;
			memcachedService.set(groupKey, key, expiry, wxaTemplateId);
			logger.info("账号下模板ID放入缓存成功，wxaTemplateId："+wxaTemplateId+",groupKey:"+groupKey+",key:"+key+",expiry:"+expiry);
		}else{
			logger.info("账号下模板ID为空，尽快排查问题！！！appId："+appId);
		}
		return wxaTemplateId;
	}


	/**
	 * 获取小程序账号下的模板ID
	 * 说明，如果没有该模板则进行创建
	 * @param appId
	 * @param serviceAdvice
	 * @return
	 */
	private String getWxaTemplateId(String appId, ServiceAdvice serviceAdvice) {
		String wxaTemplateId = "";
		//1、获取账号下模板列表，解析出模板ID
		String title = serviceAdvice.getTitle();
		String adviceListRet =  getTemplateAdviceList(appId);//获取通知模板列表
		logger.info("获取模板列表结果为adviceListRet"+adviceListRet);
		if(StringUtils.isEmpty(adviceListRet)){
			logger.info("获取模板列表结果为空，请排查问题");
			return wxaTemplateId;
		}
		JSONObject retJSON = JSON.parseObject(adviceListRet);//json字符串转换成jsonobject对象
		JSONArray arr = retJSON.getJSONArray("list");//jsonobject对象取得some对应的jsonarray数组
		for(int i=0;i<arr.size();i++){
			    JSONObject job = arr.getJSONObject(i);  // 遍历 jsonarray 数组，把每一个对象转成 json 对象
			    String template_title = (String)job.get("title");
			    String template_id = (String)job.get("template_id");
			    //如果标题相同则说明该模板已经存在
			    if(template_title.equals(title)){
			    	wxaTemplateId = template_id;
			    }
		}
		
		//2、如果模板不存在则进行添加模板
		if(StringUtils.isEmpty(wxaTemplateId)){
			String addRet = addTemplateAdvice(appId,serviceAdvice);
			logger.info("添加模板通知获取模板返回结果addRet："+addRet);
			if(StringUtils.isNotEmpty(addRet)){
				JSONObject addRetJSON = JSON.parseObject(addRet);//json字符串转换成jsonobject对象
				int errcode = addRetJSON.getIntValue("errcode");
				if(errcode == 0){
					wxaTemplateId = addRetJSON.getString("template_id");
					logger.info("添加模板通知获取模板ID成功，wxaTemplateId:"+wxaTemplateId+"返回结果addRet："+addRet);
				}else{
					logger.info("添加模板通知获取模板ID失败，返回结果addRet："+addRet);
				}
			}else{
				logger.info("添加模板通知获取模板ID失败，返回结果addRet："+addRet);
			}
		}else{
			logger.info("从账号模板列表中解析出模板ID成功，wxaTemplateId:"+wxaTemplateId);
		}
		return wxaTemplateId;
	}
	
	private String getTemplateAdviceList(String appId) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		//Response<String> resp = Requests.get(weixinServiceUrl + getTemplateAdviceUrl).params(map).text();
 	 	Response<String> resp = Requests.get(weixinServiceUrl + getTemplateAdviceUrl).params(map).text();
 	 	String adviceListRet = resp.getBody();
 	 	if(StringUtils.isEmpty(adviceListRet)){
			logger.info("获取模板列表结果为空，请排查问题"+weixinServiceUrl + getTemplateAdviceUrl+",map:"+JSON.toJSONString(map));
		}
 	 	return adviceListRet;
	}
	/**
	 * @param appId
	 */
	private String addTemplateAdvice(String appId,ServiceAdvice serviceAdvice) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("appId", appId);
		map.put("template_id",serviceAdvice.getId());
		map.put("template_keywordIds", serviceAdvice.getKeywordIds());
 	 	Response<String> resp = Requests.get(weixinServiceUrl + addTemplateAdviceUrl).params(map).text();
 	 	return resp.getBody();
	}

	/**
	 * 通知点击跳转的小程序页面（可带参数）
	 * @param serviceAdvice
	 * @return
	 */
	private String buildPage(ServiceAdvice serviceAdvice,long shopMemberOrderId) {
		return "pages/component/orderDetail/orderDetail?orderId="+shopMemberOrderId;
	}

	private String buildPage1(Map<String, String> map,long shopMemberOrderId) {
		String afterSaleId = map.get("afterSaleId");
		String storeId = map.get("storeId");
		String userId=map.get("userId");
		return "pages/component/refundDetail/refundDetail?saleId="+afterSaleId+"&userId="+userId+"&storeId="+storeId;
	}
	/**
	 * 填充关键字
	 * @param serviceAdvice
	 * @return
	 */
	private void fillKeyword(Map<String, String> map, ServiceAdvice serviceAdvice, long shopMemberOrderId) {
		//Long skuId=45439L;
		String skuId = map.get("skuId");
//		ShopOrderAfterSale shopOrderAfterSale =null;
//		if (skuId!=null){
//			shopOrderAfterSale=shopMemberOrderService.selectRefund(shopMemberOrderId, Long.parseLong(skuId));
//		}
		ShopOrderAfterSale shopOrderAfterSale =null;
		Long orderId=shopMemberOrderId;
		if (skuId!=null){
			 shopOrderAfterSale = shopRefundMapper.applyRefund(orderId.toString(), Long.parseLong(skuId));
			 map.put("afterSaleId",shopOrderAfterSale.getAfterSaleId());
			 map.put("storeId",shopOrderAfterSale.getStoreId().toString());
			 map.put("userId",shopOrderAfterSale.getMemberId().toString());
		}


		ShopMemberOrder shopMemberOrder = shopMemberOrderService.getMemberOrderById(shopMemberOrderId);
		
		int buyWay = shopMemberOrder.getBuyWay();//购买方式 0：普通  1：团购  2：秒杀
		logger.info("======大发顺丰====购买方式 0：普通  1：团购  2：秒杀======buyWay："+buyWay+",shopMemberOrderId:"+shopMemberOrderId);
		List<ShopMemberOrderItem> list = shopMemberOrderService.getMemberOrderItemList(shopMemberOrder.getId());
		String names = "";//商品详情，商品标题集合，逗号分隔
		for(ShopMemberOrderItem  item : list){
			names = "," + names + item.getName();
		}
		if(names.length()>0){
			names = names.substring(1,names.length() );
		}
		logger.info("======大发顺丰====订单商品名称names："+names);
		logger.info("======大发顺丰====订单类型serviceAdvice："+JSON.toJSONString(serviceAdvice));
		String id = serviceAdvice.getId();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//待付款提醒
		if(id.equals(ServiceAdvice.waitPayAdvice.getId())){
			map.put("keyword1", DateUtil.parseLongTime2Str(shopMemberOrder.getCreateTime()));//下单时间
			if(buyWay == 1){
				map.put("keyword2", "团购商品"+names);//商品详情
				map.put("keyword3",  "活动已成团，请尽快完成支付");//支付提醒
			}else if(buyWay == 2){
				map.put("keyword2", "秒杀商品"+names);//商品详情
				map.put("keyword3",  "活动即将到期，请尽快完成支付");//支付提醒
			}else{
				map.put("keyword2", names);//商品详情
				map.put("keyword3",  "保留即将到期，请尽快完成支付");//支付提醒
			}
		}else if(id.equals(ServiceAdvice.paySuccessAdvice.getId())){   //付款成功通知
			map.put("keyword1",  DateUtil.parseLongTime2Str(shopMemberOrder.getPayTime()));//付款时间
			map.put("keyword2",  names);//商品详情
			int orderType = shopMemberOrder.getOrderType();//订单类型：到店提货或送货上门(0:到店提货;1:送货上门) /**
		    if(orderType == 0){
		    	map.put("keyword3","待提货");//订单状态
		    }else{
		    	map.put("keyword3","待发货");//订单状态
		    }
			map.put("keyword4","提货二维码在订单详情中查看");//备注说明

		//订单取消通知
		}else if(id.equals(ServiceAdvice.orderCancelAdvice.getId())){
			map.put("keyword1",  names);//商品详情
			map.put("keyword2", "已取消");//订单状态
			map.put("keyword3",  "抱歉，您购买的商品目前缺货！");//取消原因
		}else if(id.equals(ServiceAdvice.applyRefund.getId())) {////申请退款
			map.put("keyword1", "您好您申请的退款，商家已受理，请注意查收");//提示
			map.put("keyword3", shopOrderAfterSale.getRefundName());//商品名称
			map.put("keyword2", shopOrderAfterSale.getApplyBackMoney().toString());//退款金额  shopOrderAfterSale.getApplyBackMoney().toString()
			map.put("keyword4",  sdf.format(shopOrderAfterSale.getApplyTime()));//受理时间  shopOrderAfterSale.getApplyTime().toString()
		}else if (id.equals(ServiceAdvice.applyRefust.getId())){//退款失败
			map.put("keyword1", "您好您申请的退款，商家已拒绝，请尽快联系商家或重新申请");//提示
			map.put("keyword2", shopOrderAfterSale.getRefundName());//商品名称
			map.put("keyword3", shopOrderAfterSale.getApplyBackMoney().toString());//退款金额  shopOrderAfterSale.getApplyBackMoney().toString()
			map.put("keyword4",  sdf.format(shopOrderAfterSale.getOperateTime()));//受理时间  shopOrderAfterSale.getApplyTime().toString()
		}else if (id.equals(ServiceAdvice.applySuccess.getId())){//退款成功
			map.put("keyword1", "您好您申请的退款，商家退款完成，请注意查收");//提示
			map.put("keyword2", shopOrderAfterSale.getRefundName());//商品名称
			map.put("keyword3", shopOrderAfterSale.getBackMoney().toString());//退款金额
			map.put("keyword4", sdf.format(shopOrderAfterSale.getOperateTime()));//受理时间
		}else{
			logger.info("位置模板通知ID，请排查问题，id:"+id);
		}
		logger.info("======大发顺丰====填充后的Map："+JSON.toJSONString(map));
	}
}
