package com.jiuy.core.service.notifacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.TransmissionTemplate;
import com.jiuy.core.business.assemble.composite.ProductPropAssembler;
import com.jiuy.core.dao.NotificationDao;
import com.jiuy.core.dao.StoreBusinessDao;
import com.jiuy.core.dao.modelv2.ProductMapper;
import com.jiuy.core.dao.modelv2.ProductSKUMapper;
import com.jiuy.core.meta.account.User;
import com.jiuy.core.meta.notification.Notification;
import com.jiuy.core.service.BrandLogoServiceImpl;
import com.jiuy.core.service.CategoryService;
import com.jiuy.core.service.ProductCategoryService;
import com.jiuy.core.service.ProductService;
import com.jiuy.core.service.PropertyService;
import com.jiuy.core.service.UserManageService;
import com.jiuy.core.service.UserVisitHistoryService;
import com.jiuy.core.service.actionlog.ActionLogService;
import com.jiuy.core.service.logistics.LOWarehouseService;
import com.jiuy.core.service.product.ProductSKUService;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.Product;
import com.jiuyuan.entity.notification.UserNotification;
import com.jiuyuan.entity.query.PageQuery;
import com.jiuyuan.util.GetuiUtil;

@Service
public class NotifacationServiceImpl implements NotifacationService {

	private static final Logger logger = Logger.getLogger(NotifacationServiceImpl.class);
	
	@Resource
	private NotificationDao notificationDaoSqlImpl;
	
	@Autowired
	private UserManageService userManageService;
	
	@Autowired
	private ActionLogService actionLogService;	
	@Autowired
	private ProductService productService;

	@Autowired
	private ProductSKUMapper productSKUMapper;
	
	@Autowired
	private ProductSKUService productSKUService;
	
	@Autowired
	private ProductPropAssembler productPropAssembler;
	
	@Autowired
	private PropertyService propertyService;
	
	@Autowired
	private LOWarehouseService loWarehouseService;
	
	@Autowired
	private BrandLogoServiceImpl brandLogoService;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private UserVisitHistoryService userVisitHistoryService;
	
	@Autowired
	private StoreBusinessDao storeBusinessDao;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
	private CategoryService categoryService;
	
//	@Autowired
//	private NotifacationService notificationService;
	
	/**
	 * 商品更新發送系統通知
	 * @param productId 
	 */
	public void updateProductSendNotification(long productId,List<Long> storeIdList){
		Product product = productService.getProductById(productId);

		String storeIdArrays = "";
		for (Long storeId : storeIdList) {
			storeIdArrays = storeIdArrays + "," + storeId;
		}
//		List<String> userCIDList = storeBusinessDao.getStoreBusinessUserCIDByStoreId(storeIdList);
		try {
			//type//跳转类型0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
//			GetuiUtil.pushGeTui(userCIDList, "商品更新提醒", "本次更新商品:"+product.getName(), "", product.getId()+"","3",System.currentTimeMillis()+"");
			Notification notification = new Notification();
			notification.setTitle("商品更新提醒");
			notification.setAbstracts("本次更新商品:"+product.getName());
			notification.setPushStatus(1);
			notification.setImage("");
			notification.setType(Notification.TYPE_PRODUCT);
			notification.setStatus(0);
			notification.setLinkUrl(product.getId()+"");
			notification.setStoreIdArrays(storeIdArrays);
			addNotification(notification);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("發送系統通知有誤:本次更新商品ID"+product.getId()+",本次更新商品"+product.getName());
		}
	}
	
	/**
	 * 商品下架發送系統通知
	 * @param productSKUIds 
	 */
	public void sendSoldoutProductSKUSendNotification(long  productId,List<Long> storeIdList){
		String storeIdArrays = "";
		for (Long storeId : storeIdList) {
			storeIdArrays = storeIdArrays + "," + storeId;
		}
		
			Product product = productService.getProductById(productId);
			try {
				//type//跳转类型0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
//				GetuiUtil.pushGeTui(userCIDList, "商品下架提醒", "本次下架商品:"+product.getName(), "", "","0",System.currentTimeMillis()+"");
				Notification notification = new Notification();
				notification.setTitle("商品下架提醒");
				notification.setAbstracts("本次下架商品:"+product.getName());
				notification.setPushStatus(1);
				notification.setImage("");
				notification.setType("0");
				notification.setStatus(0);
				notification.setLinkUrl(product.getId()+"");
				notification.setStoreIdArrays(storeIdArrays);
				addNotification(notification);
				logger.info("发送商品下架提醒系统消息完成！productId："+productId);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("發送系統通知有誤:本次下架商品ID"+productId+",本次下架商品"+product.getName());
			}
	}
	/**
	 * 商品上架發送系統通知
	 * @param productSKUIds 
	 */
	public void putAwayProductSKUSendNotification(long  productId,List<Long> storeIdList){
		String storeIdArrays = "";
		for (Long storeId : storeIdList) {
			storeIdArrays = storeIdArrays + "," + storeId;
		}
		
//		List<Long> productIdBySKUIDS = productSKUMapper.getProductIdBySKUID(productSKUId);
//		List<Long> productIdList = new ArrayList<Long>();
//		for (Long productId : productIdBySKUIDS) {
//			List<ProductSKU> productSKUs = productSKUMapper.getProductSKUs(productId, "");
//			boolean flag = true;
//			for (ProductSKU productSKU : productSKUs) {
//				if(productSKU.getId()==productSKUId){
//					continue;
//				}
//				if(productSKU.isSKUOnSaling()){
//					flag = false;
//				}
//			}
//			if(flag && !(productIdList.contains(productId))){
//				productIdList.add(productId);
//			}
//		}
//			List<String> userCIDList = storeBusinessDao.getStoreBusinessUserCIDByStoreId(storeIdList);
		Product product = productService.getProductById(productId);
			try {
				//type//跳转类型0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：代金券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后  11 指定用户优惠券 12：品牌首页
//				GetuiUtil.pushGeTui(userCIDList, "商品上新提醒", "本次上新商品:"+product.getName(), "", "","3",System.currentTimeMillis()+"");
				Notification notification = new Notification();
				notification.setTitle("商品上新提醒");
				notification.setAbstracts("本次上新商品:"+product.getName());
				notification.setPushStatus(1);
				notification.setImage("");
				notification.setType("3");
				notification.setStatus(0);
				notification.setLinkUrl(product.getId()+"");
				notification.setStoreIdArrays(storeIdArrays);
				addNotification(notification);
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("發送商品上新提醒系統通知有誤:productId:"+product.getId());
			}
	}
	
	@Override
	public List<Notification> searchNotification(String title, PageQuery pageQuery) {
		List<Notification> notifications = notificationDaoSqlImpl.searchNotification(title, pageQuery);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		
		for(Notification nf : notifications) {
			long time = nf.getPushTime();
			if(time != 0) {
				calendar.setTimeInMillis(time);
				nf.setPushTimeString(sdf.format(calendar.getTime()));
			} else {
				nf.setPushTimeString("无");
			}
		}
		
		return notifications;
	}

	@Override
	public int searchNotificationCount(String title) {
		return notificationDaoSqlImpl.searchNotificationCount(title);
	}
	

	/**
	 * 添加消息和消息用户关联信息
	 * @param type 跳转类型 	0 ：未选择， 1：URL， 2： 文章，3：商品，4：商品类目 5：优惠券 6: 采购商品 7： 消息中心 8：品牌商品列表 9：物流 10：售后
	 * @param image 可以是对应的存储ID或图片路径
	 * @param userId
	 * @param titleString
	 * @param abstractsString
	 */
	public void addNotificationAndUserNotification(int type, String image,String linkUrl,  long userId, String title, String abstracts) {
		Notification notification = new Notification();
		notification.setType(String.valueOf(type));
        notification.setTitle(title);
        notification.setAbstracts(abstracts);
        notification.setImage(image);
        notification.setLinkUrl(linkUrl);
        long pushTimeString = System.currentTimeMillis();
        notification.setPushTimeString(String.valueOf(pushTimeString));
        Notification newNotification = addNotification(notification);
        
        //添加用户和消息关联表信息
        UserNotification userNotification = new UserNotification();
		userNotification.setUserId(userId);
		userNotification.setNotificationId(newNotification.getId());
        addUserNotification(userNotification);
	}


	@Override
	public ResultCode addNotificationObj(Notification notification) {
		long createTime = System.currentTimeMillis();
		
		notification.setCreateTime(createTime);
		notification.setUpdateTime(createTime);
		notificationDaoSqlImpl.addNotification(notification);
		
		return ResultCode.COMMON_SUCCESS;
	}

	public ResultCode addNotificationBasicInfo(Notification notification) {
		long createTime = System.currentTimeMillis();
		
		notification.setCreateTime(createTime);
		notification.setUpdateTime(createTime);
        notification.setStoreIdArrays("");
		notificationDaoSqlImpl.addNotificationBasicInfo(notification);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	@Override
	public Notification addNotification(Notification notification) {
		long createTime = System.currentTimeMillis();
		
		notification.setCreateTime(createTime);
		notification.setUpdateTime(createTime);
		notification.setPushTime(createTime);
		return notificationDaoSqlImpl.addNotification(notification);
	}
//		return ResultCode.COMMON_SUCCESS;
	@Override
	public ResultCode addFullNotification(Notification notification) {
		long createTime = System.currentTimeMillis();
		
		notification.setCreateTime(createTime);
		notification.setUpdateTime(createTime);
		notificationDaoSqlImpl.addFullNotification(notification);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	@Override
	public ResultCode addUserNotificationList(List<UserNotification> userNotificationList) {

		notificationDaoSqlImpl.addUserNotificationList(userNotificationList);
		
		return ResultCode.COMMON_SUCCESS;
	}

	
	public ResultCode addUserNotification(UserNotification userNotification) {
		long createTime = System.currentTimeMillis();
		
		userNotification.setCreateTime(createTime);
		userNotification.setUpdateTime(createTime);
		notificationDaoSqlImpl.addUserNotification(userNotification);
		
		return ResultCode.COMMON_SUCCESS;
	}
	
	
	@Override
	public ResultCode rmNotification(long id) {
		long updateTime = System.currentTimeMillis();
		
		notificationDaoSqlImpl.rmNotification(id, updateTime);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	public ResultCode updateNotification(Notification notification) {
		long updateTime = System.currentTimeMillis();
		String pushTime = notification.getPushTimeString();
		
		if(pushTime == "" || pushTime == null) {
			notification.setPushTime(0L);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				notification.setPushTime(sdf.parse(pushTime).getTime());
			} catch (ParseException e) {
				e.printStackTrace();
				return ResultCode.COMMON_ERROR_BAD_PARAMETER;
			}
		}
		
		notification.setUpdateTime(updateTime);
		notificationDaoSqlImpl.updateNotification(notification);
		
		return ResultCode.COMMON_SUCCESS;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateSendNotification(String appId, String appKey, String master, String host, long sendLenth) throws Exception {
		long currentTime = System.currentTimeMillis();
		long startTime = currentTime - sendLenth;
		long endTime = currentTime + sendLenth;
        List<Notification> notifications = notificationDaoSqlImpl.getPushingNotification(startTime,endTime);
//        logger.info("检查是否有消息要推送currentTime："+ currentTime+",notifications.size():"+notifications.size());
//        logger.info("startTime:"+ startTime+",endTime:"+endTime);
		for(Notification nf : notifications) {
			String type = nf.getType();//9：物流 10：售后
			if(type.equals("9")||type.equals("10")){//物流9和10售后要推送给指定用户集合
				long notificationId = nf.getId();
				logger.info("物流信息或售后信息发送推送开始,notificationId:"+notificationId);
	            String title = nf.getTitle();
	            String abstracts = nf.getAbstracts();
	            String linkUrl = nf.getLinkUrl();
	            String image = nf.getImage();
	            String pushTime = String.valueOf(currentTime);
	            List<String> cidList = getCidList(notificationId);
				boolean ret = GetuiUtil.pushGeTui(cidList,title, abstracts, linkUrl, image, type , pushTime);
				if(ret){
					notificationDaoSqlImpl.updatePushStatus(nf.getId());
				}
//				logger.info("物流信息或售后信息发送推送完成，notificationId："+notificationId+",发送用户集合："+cidList.toString());
			}else{//其他的类型则推送所有用户
//				logger.info("非物流信息或售后信息发送推送开始");
				pushList(appId, appKey, master, host, sendLenth, nf);
				notificationDaoSqlImpl.updatePushStatus(nf.getId());
//				logger.info("非物流信息或售后信息发送推送完成");
			}
			
		}
//		logger.info("检查到的消息推送完成");
	}
	/**
	 * 获取消息关联用户集合的CID集合
	 * @param notificationId
	 * @return
	 */
	private List<String> getCidList(long notificationId) {
		List<String> cidList = new ArrayList<>();
		List<UserNotification> userNotificationList = notificationDaoSqlImpl.getUserNotificationList(notificationId);
		for(UserNotification userNotification : userNotificationList){
			long userId = userNotification.getUserId();
			User user = userManageService.getByUserId(userId);
			cidList.add(user.getUserCID());
//			String CID = "59229d3cb0494ab25f94f679878da44d"; 
//			cidList.add(CID);
		}
		return cidList;
	}
	
	public void pushList(String appId, String appKey, String master, String host, long sendLenth, Notification nf) throws Exception {
    	IGtPush push = new IGtPush(host, appKey, master);
    	
    	// 通知透传模板
    	TransmissionTemplate template = getTemplate(appId, appKey, nf);
    	
    	
        List<String> appIds = new ArrayList<String>();
        appIds.add(appId);

        AppMessage messagea = new AppMessage();
        messagea.setData(template);
        messagea.setAppIdList(appIds);
        messagea.setOffline(true);
        messagea.setOfflineExpireTime(1000 * 600);

        IPushResult ret = push.pushMessageToApp(messagea);
        logger.info("appId:" + appId + " ,content: " + nf.toString());
	}

    public static TransmissionTemplate getTemplate(String appId, String appKey, Notification nf) {
        TransmissionTemplate template = new TransmissionTemplate();
        template.setAppId(appId);
        template.setAppkey(appKey);
        template.setTransmissionType(2);
        template.setTransmissionContent(JSONObject.toJSONString(nf));
        APNPayload payload = new APNPayload();
        payload.setBadge(1);
        payload.setContentAvailable(1);
        payload.setSound("default");
        payload.addCustomMsg("content", JSONObject.toJSONString(nf));
//      简单模式APNPayload.SimpleMsg
        payload.setAlertMsg(new APNPayload.SimpleAlertMsg(nf.getAbstracts()));
//      字典模式使用下者
//      payload.setAlertMsg(getDictionaryAlertMsg(contentJson));
        
        template.setAPNInfo(payload);
        return template;
    }

	

}
