package com.jiuyuan.service.common;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.jiuyuan.dao.mapper.supplier.ShopNotificationMapper;
import com.jiuyuan.entity.newentity.ShopNotification;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.util.GetuiUtil;

@Service
public class RefundSMSNotificationService {
	
	private static final Logger logger = LoggerFactory.getLogger(RefundSMSNotificationService.class);
	
	private static final String THREE_DAY = "3";
	
	private static final String FIFTEEN_DAYS = "15";
	
	private List<Integer> messageTypeCollection = new ArrayList<Integer>(); 
	
    private static final String accountSid = "bf556fda1feaeab46ff2d06c6dd96927";
    
    private static final String token = "1fcdf24ff833a26e86964abbcc72235a";
    
//    private static final String appId = "7828de81a08244729e6869657f5fb364";//语音appId
    
    private static final String appId = "b298d6cc182a48da9ba7f6084d92b6ff";//短信appId
	
	public RefundSMSNotificationService(){
		messageTypeCollection.add(2);
		messageTypeCollection.add(3);
		messageTypeCollection.add(5);
		messageTypeCollection.add(6);
		messageTypeCollection.add(7);
		messageTypeCollection.add(8);
	}
	
    @Autowired
    private UcpaasService ucpaasService;
	@Autowired
	private IStoreBusinessNewService storeBusinessNewService;
	
	@Autowired
	private YunXinSmsService yunXinSmsService;
	
	@Autowired
	private ShopNotificationMapper shopNotificationMapper;
	
	
	
	/**
	 * 发送短信，个推，以及站内信，若发送的信息不是买家，就不发送个推和站内信
	 * @param cids app的cid，个推需要
	 * @param title 为null 系统编写为"售后详情"
	 * @param messageType  
	 * 1、【俞姐姐门店宝】生成售后单，待卖家确认 (发送给卖家) <br> 
	 * 2、【俞姐姐门店宝】卖家拒绝买家申请的售后，卖家已拒绝(发送给买家) <br> 
	 * 3、【俞姐姐门店宝】卖家同意买家申请的售后,通知发货(发送给买家)<br> 
	 * 4、【俞姐姐门店宝】买家发货(发送给卖家)<br> 
	 * 5、【俞姐姐门店宝】买家3天内没发货，售后状态失效(发送给买家)<br> 
	 * 6、【俞姐姐门店宝】您提交的退货申请，款项已到帐。希望您的退货体验还不错，一定要继续进货哦。(发送给买家)<br> 
	 * 7、【俞姐姐门店宝】给买家增加1次售后机会的短信提醒内容(发送给买家)<br> 
	 * 8、【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭(发送给双方)<br> 
	 *   【俞姐姐门店宝】经平台介入沟通，买家提交的退货申请已被平台关闭。<br> 
	 * @param storeIdList 需要发送门店ID集合
	 * @param pushTime 推送时间
	 * @param type 跳转类型 0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后 11 指定用户优惠券 12：品牌首页
	 * @param abstracts
	 * @param linkUrl
	 * @param image
	 * @param phoneMap 请存放key-value "storePhone"-"15155555555" "supplierPhone"-"15155555555"
	 * @return
	 */
	
	private boolean sendSMSNotificationAndGEPush(String cid,String title,int messageType,String pushTime,Long storeId,String type,List<String> abstracts,String linkUrl,String image,List<Integer> templateIds, List<String> paramses,List<String> phones){
        
		//发送短信
		try {
			for(int i = 0; i < abstracts.size() ; i++){
				Integer templateId = templateIds.get(i);
				String params = paramses.get(i);
//				yunXinSmsService.sendNotice(phones.get(i), params, templateId);
				boolean success = ucpaasService.testTemplateSMS(true,accountSid,token,appId,String.valueOf(templateId),phones.get(i),params);
				if(success){
					logger.info("短信发送成功！");
				}else{
					logger.info("短信发送失败！templateId:"+templateId);
				}
				//发送推送和站内信
				if(i == 0&& messageTypeCollection.contains(messageType)){
					List<String> cids = new ArrayList<String>();
					cids.add(cid);
					List<Long> storeIdList = new ArrayList<Long>();
					storeIdList.add(storeId);
					sendNotificationAndGetui(cids, title, abstracts.get(i), linkUrl, image, type, pushTime, storeIdList);
				}
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		
	}
	/**
	 * 售后发送短信个推以及站内信
	 * @param title 为null 系统编写为"售后详情"
	 * @param messageType  
	 * 1、【俞姐姐门店宝】生成售后单，待卖家确认 (发送给卖家) <br> 
	 * 2、【俞姐姐门店宝】卖家拒绝买家申请的售后，卖家已拒绝(发送给买家) <br> 
	 * 3、【俞姐姐门店宝】卖家同意买家申请的售后,通知发货(发送给买家)<br> 
	 * 4、【俞姐姐门店宝】买家发货(发送给卖家)<br> 
	 * 5、【俞姐姐门店宝】买家3天内没发货，售后状态失效(发送给买家)<br> 
	 * 6、【俞姐姐门店宝】您提交的退货申请，款项已到帐。希望您的退货体验还不错，一定要继续进货哦。(发送给买家)<br> 
	 * 7、【俞姐姐门店宝】给买家增加1次售后机会的短信提醒内容(发送给买家)<br> 
	 * 8、【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭(发送给双方)<br> 
	 *   【俞姐姐门店宝】经平台介入沟通，买家提交的退货申请已被平台关闭。<br>
	 * @param storeId
	 * @param supplierPhone 供应商手机号
	 * @return
	 */
	public boolean sendSMSNotificationAndGEPush(String title ,int messageType, Long storeId, String supplierPhone){
		//获取当前时间戳
		String currentTime = String.valueOf(System.currentTimeMillis());
		List<Integer> templateIds = new ArrayList<Integer>();
		//获取商家的手机号码
		StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessById(storeId);
		String storePhone = storeBusiness.getPhoneNumber();
		String cid = storeBusiness.getUserCID();
		List<String> phones = new ArrayList<String>();
		List<String> paramses = new ArrayList<String>();
		//生成发送短信的参数
//		params.add(businessNumber);//初始账号
//		params.add(password);//初始密码
		List<String> abstracts = new ArrayList<String>();
		String abstract1 = "";
		String abstract2 = "";
		String threeDays = THREE_DAY;
		String fifteenDays = FIFTEEN_DAYS;
		switch (messageType) {
		case 1:
			abstract1 = "【俞姐姐门店宝】您的买家提交了新售后申请，等待您的确认，"+threeDays+"天内您没有拒绝就会默认同意退货哦。请及时登陆供应商后台处理。";
			paramses.add(threeDays);
			templateIds.add(255180);
			phones.add(supplierPhone);
			break;
		case 2:
			abstract1 = "【俞姐姐门店宝】您提交的售后申请卖家拒绝了哦。可以联系客服帮您处理。";
			templateIds.add(255184);
			phones.add(storePhone);
			break;
		case 3:
			abstract1 = "【俞姐姐门店宝】您提交的退货卖家已经同意了哦，请及时发货。"+threeDays+"天内无物流更新系统将关闭您的退货申请哦。";
			paramses.add(threeDays);
			templateIds.add(255185);
			phones.add(storePhone);
			break;
		case 4:
			abstract1 = "【俞姐姐门店宝】退货订单买家已发货，请及时查收。如不做任何操作，平台将在"+fifteenDays+"天内将该笔货款退还买家。";
			paramses.add(fifteenDays);
			templateIds.add(255187);
			phones.add(supplierPhone);
			break;
		case 5:
			abstract1 = "【俞姐姐门店宝】因超过"+threeDays+"天没有填写退货物流信息，您提交的退货申请已经关闭，如果需退货请重新提交。";
			templateIds.add(255186);
			paramses.add(threeDays);
			phones.add(storePhone);
			break;
		case 6:
			abstract1 = "【俞姐姐门店宝】您提交的退货申请，款项已到帐。希望您的退货体验还不错，一定要继续进货哦。";
			templateIds.add(255190);
			phones.add(storePhone);
			break;
//		case 7:
//			abstract1 = "【俞姐姐门店宝】经平台介入沟通，为您增加1次售后申请机会，请您按钮协商结果重新申请售后。";
//			templateIds.add(3148005);
//			phones.add(storePhone);
//			break;
		case 8:
			abstract1 = "【俞姐姐门店宝】经平台介入沟通，您提交的退货申请已被平台关闭。";
			templateIds.add(255195);
			phones.add(storePhone);
			abstract2 = "【俞姐姐门店宝】经平台介入沟通，买家提交的退货申请已被平台关闭。";
			templateIds.add(255196);
			phones.add(supplierPhone);
			break;

		default:
			break;
		}
		abstracts.add(abstract1);
		if(!abstract2.equals("")){
			abstracts.add(abstract2);
		}
		for(int i = paramses.size();i < 2;i++){
			paramses.add("");
		}
		this.sendSMSNotificationAndGEPush(cid,  title == null?"售后进度提醒":title, messageType, currentTime, storeId, "7", abstracts, "", "", templateIds, paramses, phones);
	    return true;	
	}
	
	
	
	
	//发送系统通知和推送
		private void sendNotificationAndGetui(List<String> cids, String title,String abstracts,String linkUrl,String image,String type ,String pushTime
				,List<Long> storeIdList){
				ShopNotification notification = new ShopNotification();
				notification.setTitle(title);
				notification.setAbstracts(abstracts);
				notification.setPushStatus(1);
				notification.setImage(image);
				notification.setType(Integer.parseInt(type));
				notification.setStatus(0);
				notification.setLinkUrl(linkUrl);
				notification.setCreateTime(System.currentTimeMillis());
				notification.setUpdateTime(System.currentTimeMillis());
				notification.setPushTime(System.currentTimeMillis());
				String storeIdArrays = "";
				if(storeIdList.size()>0){
					for (Long storeId : storeIdList) {
						if(storeId!=null){
							storeIdArrays = storeIdArrays + "," + storeId;
						}
					}
				}
				notification.setStoreIdArrays(storeIdArrays);
				addNotification(notification);
			try {
				if(cids.size()>0){
					boolean ret = GetuiUtil.pushGeTui(cids,abstracts,"" , linkUrl, image, type , pushTime);
				}else{
					boolean ret = GetuiUtil.pushGeTui(abstracts, "", linkUrl, image, type , pushTime);
				}
//				if(ret){
//				}
			} catch (Exception e) {
				logger.error("发送推送时发生异常:"+e.getMessage());
			}
		}

	private void addNotification(ShopNotification notification) {
		shopNotificationMapper.insert(notification);
	}
	

}
