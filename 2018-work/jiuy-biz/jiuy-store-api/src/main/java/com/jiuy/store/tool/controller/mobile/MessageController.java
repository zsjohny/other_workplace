package com.jiuy.store.tool.controller.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.plugins.Page;
import com.jiuyuan.constant.ResultCode;
import com.jiuyuan.entity.ClientPlatform;
import com.jiuyuan.entity.UserDetail;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.ext.spring.web.method.ClientIp;
import com.jiuyuan.util.DateUtil;
import com.jiuyuan.util.SmallPage;
import com.jiuyuan.util.anno.Login;
import com.jiuyuan.web.help.JsonResponse;
import com.store.entity.member.ShopMember;
import com.store.entity.message.GroupMessage;
import com.store.entity.message.Message;
import com.store.enumerate.MessageSendTypeeEnum;
import com.store.enumerate.MessageTypeEnum;
import com.store.service.MemberService;
//import com.store.entity.message.Message;
import com.store.service.MessageService;
import com.store.service.NotificationService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;

/**
 * <p>
 * 客服聊天记录表 前端控制器
 * </p>
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/mobile/message")
public class MessageController {
	 private static final Log logger = LogFactory.get();
    @Autowired
    MemberService memberService;
    
    @Autowired
    MessageService messageService;
    
    @Autowired
    private NotificationService notificationService;
    
    
    /**
     * 
     * @return
     */
    @RequestMapping("/getAllNoReadCount/auth")
    @Login
    @ResponseBody
	public JsonResponse getAllNoReadCount(UserDetail<StoreBusiness> userDetail, HttpServletResponse response,
			@ClientIp String ip, ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	try {
	   	 	
	   	 	Map<String,String> retMap = new HashMap<String,String>();
	   	 	long storeBusinessId = userDetail.getId();
	   	 	int allNoReadCount = 0;
	   	 	int notificationCountNoRead = 0;
		 	if(storeBusinessId != 0){
		 		StoreBusiness storeBusiness= userDetail.getUserDetail();
		 		//未读的门店客服消息条数
		 		allNoReadCount = memberService.getAllNoReadCount(storeBusiness.getId());
		 		
		 		 //未读的系统通知条数
		   	    notificationCountNoRead = notificationService.getCountExclude910(storeBusinessId, storeBusiness.getCreateTime());
		 	}
		    
	   	    retMap.put("allNoReadCount", String.valueOf(allNoReadCount));
	   	    retMap.put("notificationCountNoRead", String.valueOf(notificationCountNoRead));
	   	 	
	   	 	//返回数据
	   	 	return jsonResponse.setSuccessful().setData(retMap);
    	} catch (Exception e) {
    		e.printStackTrace();
			return jsonResponse.setError(e.getMessage());
		}
    }
    
   
    /**
     * 客服消息列表（群发报告）
     * @param current	非必填  当前页数
     * @param size	非必填  每页条数  默认每页10条
     * @return
     */
    @RequestMapping("/groupMessageList/auth")
    @Login
    @ResponseBody
    public JsonResponse groupMessageList(int current, int size ,
			UserDetail<StoreBusiness> userDetail, HttpServletResponse response, @ClientIp String ip,
			ClientPlatform client) {
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	
   	 	long storeBusinessId = userDetail.getId();
	 	if(storeBusinessId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
   	 	
   	 	StoreBusiness storeBusiness= userDetail.getUserDetail();
	 	
   	 	//获取数据
   	 	Page<GroupMessage> page = messageService.groupMessageList(new Page(current,size),storeBusiness.getId());
   	 	//组装数据
   	 	SmallPage smallPage = new SmallPage(page);
   	 	List<GroupMessage> list = smallPage.getRecords();
   	 	List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
   	 	for(GroupMessage groupMessage : list){
   	 		Map<String,String> messageMap = new HashMap<String,String>();
   	 		messageMap.put("id", String.valueOf(groupMessage.getId()));
   	 		messageMap.put("storeId", String.valueOf(groupMessage.getStoreId()));
   	 		messageMap.put("adminId", String.valueOf(groupMessage.getAdminId()));
   	 		messageMap.put("adminName", String.valueOf(groupMessage.getAdminName()));
   	 		messageMap.put("adminHeadimg", String.valueOf(groupMessage.getAdminHeadimg()));
   	 		messageMap.put("content", groupMessage.getContent());
   	 		messageMap.put("messageType", String.valueOf(groupMessage.getMessageType()));
   	 		messageMap.put("createTime", DateUtil.parseLongTime2Str(groupMessage.getCreateTime()));
   	 		int sendTotalMemberCount = groupMessage.getSendTotalMemberCount();
   	 		int sendSeccessMemberCount = groupMessage.getSendSeccessMemberCount();
   	 		int sendFailMemberCount = groupMessage.getSendFailMemberCount();
   	 		messageMap.put("sendStatistics", "本次群发"+sendTotalMemberCount+"人，成功"+sendSeccessMemberCount+"人，"+sendFailMemberCount+"人过期");
    	   	mapList.add(messageMap);
   	 	}
   	 	smallPage.setRecords(mapList);
   	 	//返回数据
   	 	return jsonResponse.setSuccessful().setData(smallPage);
    }
    
    

    /**
     * 设置客服未读的会员消息为已读
     * @param memberId 会员ID    
     * @param readState 阅读状态：-1全部、0未读、1已读
     * @param current	非必填  当前页数
     * @param size	非必填  每页条数  默认每页10条
     * @return
     */
    @RequestMapping("/setMessageIsRead/auth")
    @Login
    @ResponseBody
    public JsonResponse setMessageIsRead(@RequestParam(required = true) String messageId,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	
   	 	long storeBusinessId = userDetail.getId();
	 	if(storeBusinessId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
	 	
	 	messageService.setServerNoReadIsRead(Long.parseLong(messageId));
	 	//返回数据
   	 	return jsonResponse.setSuccessful();
    }

    /**
     * 客服消息列表
     * @param memberId 会员ID    
     * @param readState 阅读状态：-1全部、0未读、1已读
     * @param current	非必填  当前页数
     * @param size	非必填  每页条数  默认每页10条
     * @return
     */
    @RequestMapping("/messageList/auth")
    @Login
    @ResponseBody
    public JsonResponse messageList(@RequestParam(required = true) String memberId,
    		@RequestParam(required = true) int readState,int current, int size ,
    		UserDetail userDetail,HttpServletResponse response ,@ClientIp String ip, ClientPlatform client) {
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	
   	 	long storeBusinessId = userDetail.getId();
	 	if(storeBusinessId == 0){
	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
	 	}
   	
   	 	
//   	 	StoreBusiness storeBusiness= userDetail.getStoreBusiness();
	 	
   	 	//获取数据
   	 	Page<Message> page = messageService.messageList(Long.parseLong(memberId),readState,new Page(current,size),storeBusinessId);
   	 	//组装数据
   	 	SmallPage smallPage = new SmallPage(page);
   	 	List<Message> list = smallPage.getRecords();
   	 	List<Map<String,String>> mapList = new ArrayList<Map<String,String>>();
   	 	for(Message message : list){
   	 		//1、组装消息列表
   	 		Map<String,String> messageMap = new HashMap<String,String>();
   	 		messageMap.put("id", String.valueOf(message.getId()));
   	 		messageMap.put("storeId", String.valueOf(message.getStoreId()));
   	 		messageMap.put("adminId", String.valueOf(message.getAdminId()));
   	 		messageMap.put("adminName", message.getAdminName());
   	 		messageMap.put("adminHeadimg", message.getAdminHeadimg());
   	 		messageMap.put("memberId", String.valueOf(message.getMemberId()));
    	   	messageMap.put("memberName", message.getMemberName());
    	   	messageMap.put("memberHeadimg", message.getMemberHeadimg());
    	   	messageMap.put("sendType", String.valueOf(message.getSendType()));
    	   	messageMap.put("messageType", String.valueOf(message.getMessageType()));
    	   	messageMap.put("textContent", message.getTextContent());
    		messageMap.put("imagePicUrl", message.getImagePicUrl());
    		messageMap.put("imageMediaId", message.getImageMediaId());
    		messageMap.put("linkTitle", message.getLinkTitle());
    		messageMap.put("linkDescription", message.getLinkDescription());
    		messageMap.put("linkUrl", message.getLinkUrl());
    		messageMap.put("readState",String.valueOf( message.getReadState()));
    		messageMap.put("createTime", String.valueOf(message.getCreateTime()));
    		messageMap.put("sendState", String.valueOf(message.getSendState()));
    	   	mapList.add(messageMap);
   	 	}
   	 	smallPage.setRecords(mapList);
   	 	//返回数据
   	 	return jsonResponse.setSuccessful().setData(smallPage);
    }

	
    /**
     * 发送信息  
     * @return
     */
    @RequestMapping("/sendMessage/auth")
    @Login
    @ResponseBody
    public JsonResponse sendMessage(@RequestParam(required = true) int messageType,@RequestParam(required = true) String content,
			@RequestParam(required = true) long memberId, UserDetail<StoreBusiness> userDetail,
			HttpServletResponse response, @ClientIp String ip, ClientPlatform client) {
   	 	JsonResponse jsonResponse = new JsonResponse();
   	 	
   	 	long storeBusinessId = userDetail.getId();
   	 	if(storeBusinessId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 	
   	 	//1、准备数据
   	   	StoreBusiness storeBusiness= userDetail.getUserDetail();
   	   	MessageTypeEnum type = MessageTypeEnum.getEnum(messageType);
   	   	ShopMember member = memberService.getMemberById(memberId);
   	   	//2、发送消息   发送结果状态：0（成功）、1（失败）
   	   	int sendState = messageService.sendMessage(type,content,member,storeBusiness,MessageSendTypeeEnum.serverToMember);
   	   	if(sendState == 0){
   	   		jsonResponse.setSuccessful();
   	   	}else{
   	   		jsonResponse.setResultCode(ResultCode.WXA_SEND_SERVER_MSG_ERROR);
   	   	}
   	   	return jsonResponse;
    }
    
    
    /**
     * 发送群发信息
     * @return
     * 
     */
    @RequestMapping("/sendGroupMessage/auth")
    @Login
    @ResponseBody
    public JsonResponse sendGroupMessage(String imgs ,String text,
			UserDetail<StoreBusiness> userDetail, HttpServletResponse response, @ClientIp String ip,
			ClientPlatform client) {
    	JsonResponse jsonResponse = new JsonResponse();
    	
    	long storeBusinessId = userDetail.getId();
   	 	if(storeBusinessId == 0){
   	 		logger.info("商家信息不能为空，该接口需要登陆，请排除问题");
   	 		return jsonResponse.setResultCode(ResultCode.COMMON_ERROR_NOT_LOGGED_IN);
   	 	}
   	 
   	 	//1、准备数据
   	 	StoreBusiness storeBusiness= userDetail.getUserDetail();
   	 	//2、发送消息
   	 	messageService.sendGroupMessage(imgs,text,storeBusiness);
    
   	 	return jsonResponse.setSuccessful();
    }
    
    
    
    
    	
}


