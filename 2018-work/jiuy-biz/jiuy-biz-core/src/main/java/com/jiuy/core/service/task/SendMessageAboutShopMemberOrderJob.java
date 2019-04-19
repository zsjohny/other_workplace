/**
 * 
 */
package com.jiuy.core.service.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 待付款提醒通知定时任务
 */
@Component
public class SendMessageAboutShopMemberOrderJob {

	private static final Logger logger = Logger.getLogger(SendMessageAboutShopMemberOrderJob.class);

	@Autowired
	private ShopMemberOrderService shopMemberOrderService;

	public void execute() {
		sendMessage();
	}

	/**
	 * 待付款提醒通知定时任务
	 */
	@Transactional(rollbackFor = Exception.class)
	private void sendMessage() {
		// logger.info("启动待付款提醒通知定时任务");
		// //1、获取大于一小时未支付会员订单
		// List<ShopMemberOrder> shopMemberOrderList =
		// shopMemberOrderService.getWaitPayTipOrderList();
		// for (ShopMemberOrder shopMemberOrder : shopMemberOrderList) {
		// long orderId = shopMemberOrder.getId();
		// String wxaServiceUrl = AdminConstants.WXA_SERVER_URL;
		// String apiUrl = "/miniapp/advice/waitPayAdvice.json";
		// String url = wxaServiceUrl + apiUrl;
		// Map<String, String> map = new HashMap<String, String>();
		// map.put("orderId", String.valueOf(orderId));
		// logger.info("发送模板通知url:"+url+",map:"+map);
		// Response<String> resp = Requests.get(url).params(map).text();
		// String ret = resp.getBody();
		// logger.info("模板通知发送结果ret:"+ret);
		// if(StringUtils.isNotEmpty(ret)){
		// JSONObject retJSON = JSON.parseObject(ret);//json字符串转换成jsonobject对象
		// boolean successful = retJSON.getBoolean("successful");
		// if(successful){
		// logger.info("更改待支付发送标记，successful"+successful);
		// //更改发送待支付提醒标记为已发送
		// shopMemberOrderService.updateOrderSendMessage(orderId);
		// }else{
		// logger.info("发送通知返回结果为失败，请排查问题，successful"+successful);
		// }
		// }else{
		// logger.info("发送待付款通知失败请排查问题，ret:"+ret+",url："+url+",map:"+map);
		// }
		// }
		// logger.info("完成待付款提醒通知定时任务");
	}

}