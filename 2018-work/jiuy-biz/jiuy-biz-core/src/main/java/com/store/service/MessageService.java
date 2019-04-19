package com.store.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.constant.MemcachedKey;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.MemcachedService;
import com.store.dao.mapper.GroupMessageMapper;
import com.store.dao.mapper.MessageMapper;
import com.store.dao.mapper.StoreUserMapper;
import com.store.entity.member.ShopMember;
import com.store.entity.message.GroupMessage;
import com.store.entity.message.Message;
import com.store.entity.message.MessageVo;
import com.store.enumerate.Member48MarkEnum;
import com.store.enumerate.MessageSendTypeeEnum;
import com.store.enumerate.MessageTypeEnum;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 客服聊天记录表 服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 * extends ServiceImpl<MessageMapper, Message>
 */
@Service
public class MessageService {
    private static final Log logger = LogFactory.get();

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    MessageMapper messageMapper;
    @Autowired
    MemberService memberService;
    @Autowired
    MessageSendService messageSendService;

    @Autowired
    private MemcachedService memcachedService;

    @Autowired
    private MessageReceiveService messageReceiveService;


    @Autowired
    GroupMessageMapper groupMessageMapper;

    /**
     * 根据微信消息ID获取消息记录
     *
     * @return
     */
    public Message getMessageByWeiXinMsgId(String weiXinMsgId) {
        Wrapper<Message> wrapper = new EntityWrapper<Message>().eq("weixin_msg_id", weiXinMsgId);
        List<Message> list = messageMapper.selectList(wrapper);
        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }

    /**
     * 获取群发消息列表
     *
     * @param page
     * @return
     */
    public Page<GroupMessage> groupMessageList(Page page, long storeId) {
        Wrapper<GroupMessage> wrapper = new EntityWrapper<GroupMessage>().eq("status", 0).eq("store_id", storeId).orderBy("create_time", false);
        page.setRecords(groupMessageMapper.selectPage(page, wrapper));
        return page;
    }

    /**
     * 指定会员消息记录列表
     *
     * @param memberId
     * @param readState 阅读状态：-1全部、0未读、1已读
     * @param page
     * @return
     */
    public Page<Message> messageList(long memberId, int readState, Page<Message> page, long storeId) {
        Wrapper<Message> wrapper = new EntityWrapper<Message>().eq("member_id", memberId).eq("store_id", storeId).eq("status", 0);
        if (readState == 0 || readState == 1) {
            wrapper.eq("read_state", readState);
        }
        wrapper.orderBy("create_time", false);
        page.setRecords(messageMapper.selectPage(page, wrapper));

//		logger.info("===========================获取指定会员消息列表,memberId:"+memberId+",page:"+JSONObject.toJSONString(page));
        List<Message> records = page.getRecords();

        //将获取的列表消息设为已读， //TODO 该方式会对数据库信息大量的写操作，后期该处需要优化
        setServerNoReadIsRead(records, memberId);
//		logger.info("===========================获取指定会员消息列表接口返回数据,memberId:"+memberId+",page:"+JSONObject.toJSONString(page));
        return page;

    }


    /**
     * 会员会员未读消息数量
     *
     * @param memberId
     * @param storeId
     * @return
     */
    public int getMemberNoReadMessageCount(long memberId, Long storeId) {
        Wrapper<Message> wrapper = getMemberNoReadMessageWrapper(memberId, storeId);
        return messageMapper.selectCount(wrapper);
    }


    /**
     * 拼接获取会员未读消息条件构造器
     *
     * @param memberId
     * @param storeId
     * @return
     */
    private Wrapper<Message> getMemberNoReadMessageWrapper(long memberId, Long storeId) {
        Wrapper<Message> wrapper = new EntityWrapper<Message>();
        wrapper.eq("member_id", memberId);
//		wrapper.eq("store_id", storeId);
        wrapper.eq("read_state", 0);
        wrapper.eq("status", 0);
        wrapper.eq("send_state", 0);
        wrapper.andNew("").eq("send_type", MessageSendTypeeEnum.serverToMember.getIntValue());
        wrapper.or("").eq("send_type", MessageSendTypeeEnum.serverToMemberGroup.getIntValue());
        wrapper.or("").eq("send_type", MessageSendTypeeEnum.systemReplaceServerToMember.getIntValue());
        wrapper.orderBy("create_time");
        return wrapper;
    }


    /**
     * 设置会员未读消息为已读接口（会员进入小程序时调用）
     *
     * @return
     */
    public void setMemberNoReadIsRead(Long memberId, Long storeId) {
	    /*
	    老的
	    Wrapper<Message> wrapper = getMemberNoReadMessageWrapper(memberId, storeId);
		List<Message> records = messageMapper.selectList(wrapper);
//		logger.info("获取会员未读消息列表:"+JSONObject.toJSONString(records));

		//2、将消息设置已读和统计减少已读数量
		for(Message message : records ){
			//是0、2、3将已读设为未读
			if(message.getSendType() == MessageSendTypeeEnum.serverToMember.getIntValue() ||
					message.getSendType() == MessageSendTypeeEnum.serverToMemberGroup.getIntValue()||
							message.getSendType() == MessageSendTypeeEnum.systemReplaceServerToMember.getIntValue()){
				int messageReadState = message.getReadState();//阅读状态:0未读，1已读
				if(messageReadState == 0){
//					logger.info("将未读消息设置为已读,messageId:"+message.getId()+",messageReadState:"+messageReadState);
					Message newMessage = new Message();
					newMessage.setId(message.getId());
					newMessage.setReadState(1);
					messageMapper.updateById(newMessage);
				}else{
					logger.info("消息已为已读无需设置为已读,请排查问题！！！！！！！！！！！！！！！！！！，messageId:"+message.getId()+",messageReadState:"+messageReadState);
				}
			}else{
				logger.info("消息不是客服发送的消息，清尽快排查问题！！！！！！！！！！！！！！！！！！！,messageId:"+message.getId());
			}
		}
	     */


        //1、获取会员未读消息列表
        Wrapper<Message> wrapper = getMemberNoReadMessageWrapper(memberId, storeId);
        wrapper.in("send_type",
                Stream.of(
                        MessageSendTypeeEnum.serverToMember.getIntValue(),
                        MessageSendTypeeEnum.serverToMemberGroup.getIntValue(),
                        MessageSendTypeeEnum.systemReplaceServerToMember.getIntValue()
                ).collect(Collectors.toList()));
        wrapper.eq("read_state", 0);
        List<Message> records = messageMapper.selectList(wrapper);
        if (!records.isEmpty()) {
            Message newMessage = new Message();
            newMessage.setReadState(1);
            for (Message message : records) {
                newMessage.setId(message.getId());
                messageMapper.updateById(newMessage);
            }
        }

		/*
		//2、将消息设置已读和统计减少已读数量
		for(Message message : records ){
			//是0、2、3将已读设为未读
			if(message.getSendType() == MessageSendTypeeEnum.serverToMember.getIntValue() ||
					message.getSendType() == MessageSendTypeeEnum.serverToMemberGroup.getIntValue()||
							message.getSendType() == MessageSendTypeeEnum.systemReplaceServerToMember.getIntValue()){
				int messageReadState = message.getReadState();//阅读状态:0未读，1已读
				if(messageReadState == 0){
//					logger.info("将未读消息设置为已读,messageId:"+message.getId()+",messageReadState:"+messageReadState);
					Message newMessage = new Message();
					newMessage.setId(message.getId());
					newMessage.setReadState(1);
					messageMapper.updateById(newMessage);
				}else{
					logger.info("消息已为已读无需设置为已读,请排查问题！！！！！！！！！！！！！！！！！！，messageId:"+message.getId()+",messageReadState:"+messageReadState);
				}
			}else{
				logger.info("消息不是客服发送的消息，清尽快排查问题！！！！！！！！！！！！！！！！！！！,messageId:"+message.getId());
			}
		}*/
        //2、减少会员未读消息数量 TODO V2.1   注意客服设置未读消息是不要设置客服消息为已读
    }


    /**
     * 未读消息设置为已读
     *
     * @param messageId
     */
    public void setServerNoReadIsRead(long messageId) {
        Message message = messageMapper.selectById(messageId);
        List<Message> list = new ArrayList<Message>();
        list.add(message);
        setServerNoReadIsRead(list, message.getMemberId());
    }

//	 TODO v2.1 设置已读未读方案需要重新考虑一下，代码重构一下

    /**
     * 设置指定商家的消息设置为已读（不将未读数量减少）
     *
     * @param records
     * @param memberId
     */
    private void setServerNoReadIsRead(List<Message> records, long memberId) {
        //1、将消息设置已读和统计减少已读数量
        int subtractNoStateCount = 0;
        for (Message message : records) {
            if (message.getSendType() == MessageSendTypeeEnum.memberToServer.getIntValue()) {
                //2、将会员发送给客服的未读消息设置为已读  发送类型：0客服单发会员、1会员单发客服、2客服群发会员
                int messageReadState = message.getReadState();//阅读状态:0未读，1已读
                if (messageReadState == 0) {
                    logger.info("将未读消息设置为已读,messageId:" + message.getId() + ",messageReadState:" + messageReadState);
                    Message newMessage = new Message();
                    newMessage.setId(message.getId());
                    newMessage.setReadState(1);
                    messageMapper.updateById(newMessage);
                    //统计需要减少的未读数量
                    subtractNoStateCount = subtractNoStateCount + 1;
                } else {
                    logger.info("消息已为已读无需设置为已读,messageId:" + message.getId() + ",messageReadState:" + messageReadState);
                }
            }
        }

        //2、减少已读数量
        if (subtractNoStateCount == 0) {
            return;
        }
        ShopMember member = memberService.getMemberById(memberId);
        logger.info("============未读消息数量减" + subtractNoStateCount + "开始===================member：" + JSONObject.toJSONString(member));
        int count = member.getNotReadMessageCount() - subtractNoStateCount;
        logger.info("============未读消息数量减少===================count：" + count);
        if (count < 0) {
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("============未读消息数量错误，不应该为负数，请尽快排查问题==================="
                    + "count:" + count + ",memberId:" + memberId + "notReadMessageCount:" + member.getNotReadMessageCount() + ",subtractNoStateCount:" + subtractNoStateCount);
            logger.info("=======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            logger.info("======================================================================");
            //为负数是重置为0，可能是会多账号登陆导致或其他问题导致
            count = 0;
        }
        ShopMember memberNew = new ShopMember();
        memberNew.setId(memberId);
        memberNew.setNotReadMessageCount(count);
        memberService.updateMemberById(memberNew);
//		logger.info("============未读消息数量减少完成===================member："+JSONObject.toJSONString(member));
    }


    /**
     * 客服给会员发送群发消息
     *
     * @param messageType
     * @param message
     * @param member
     * @param storeBusiness
     */
    public void sendGroupMessage(String imgs, String text, StoreBusiness storeBusiness) {
        //1、获取48小时内会员列表
        List<ShopMember> memberList = memberService.getMemberList(Member48MarkEnum.before, storeBusiness.getId());

        //2、组装发送数据
        List<MessageVo> messageVoList = buildData(imgs, text);

        //3、循环发送信息
        for (ShopMember member : memberList) {
            //3.1发送消息
            for (MessageVo messageVo : messageVoList) {
                MessageTypeEnum messageType = messageVo.getMessageType();
                String content = messageVo.getContent();
//				发送结果状态：0（成功）、1（失败）           发送类型：0客服单发会员、1会员单发客服、2客服群发会员
                int sendState = sendMessage(messageType, content, member, storeBusiness, MessageSendTypeeEnum.serverToMemberGroup);
                if (sendState == 0) {
                    int sendSeccessMemberCount = messageVo.getSendSeccessMemberCount() + 1;
                    messageVo.setSendSeccessMemberCount(sendSeccessMemberCount);
                } else {
                    int sendFailMemberCount = messageVo.getSendFailMemberCount() + 1;
                    messageVo.setSendFailMemberCount(sendFailMemberCount);
                }
                messageVo.setSendTotalMemberCount(memberList.size());
            }
        }

        //4、添加群发信息
        for (MessageVo messageVo : messageVoList) {
            addGroupMessage(messageVo, storeBusiness);
        }
    }


    /**
     * 添加群发消息信息
     */
    private void addGroupMessage(MessageVo messageVo, StoreBusiness storeBusiness) {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setStoreId(storeBusiness.getId());
        groupMessage.setAdminId(1L);
        groupMessage.setAdminName(storeBusiness.getBusinessName());
        groupMessage.setAdminHeadimg(storeBusiness.getStoreLogo());
        groupMessage.setContent(messageVo.getContent());
        groupMessage.setMessageType(messageVo.getMessageType().getIntCode());
        groupMessage.setSendTotalMemberCount(messageVo.getSendTotalMemberCount());
        groupMessage.setSendSeccessMemberCount(messageVo.getSendSeccessMemberCount());
        groupMessage.setSendFailMemberCount(messageVo.getSendFailMemberCount());
        long nowTime = new Date().getTime();
        groupMessage.setCreateTime(nowTime);
        groupMessage.setUpdateTime(nowTime);
        groupMessageMapper.insert(groupMessage);
    }

    /**
     * 构造发送数据集合
     *
     * @param imgs
     * @param text
     */
    private List<MessageVo> buildData(String imgs, String text) {
        List<MessageVo> messageVoList = new ArrayList<MessageVo>();
        if (StringUtils.isNotEmpty(imgs)) {
            String[] imgArr = imgs.split("____");
            for (String img : imgArr) {
                MessageVo messageVo = new MessageVo();
                messageVo.setMessageType(MessageTypeEnum.image);
                messageVo.setContent(img);
                messageVoList.add(messageVo);
            }
        }
        if (StringUtils.isNotEmpty(text)) {
            MessageVo messageVo = new MessageVo();
            messageVo.setMessageType(MessageTypeEnum.text);
            messageVo.setContent(text);
            messageVoList.add(messageVo);
        }
        return messageVoList;
    }


    /**
     * 客服发送消息给会员
     *
     * @param messageType
     * @param content       文字或图片地址
     * @param member
     * @param storeBusiness
     * @return 发送结果状态：0（成功）、1（失败）
     */
    public int sendMessage(MessageTypeEnum messageType, String content, ShopMember member, StoreBusiness storeBusiness, MessageSendTypeeEnum messageSendType) {
        return sendMessage(messageType, content, member, storeBusiness, messageSendType, false);
    }


    @Autowired
    private StoreUserMapper userMapper;


    /**
     * 客服发送消息给会员
     *
     * @param messageType
     * @param content       文字或图片地址
     * @param member
     * @param storeBusiness
     * @param isGreeting    是否是问候语，问候语则消息为已读
     * @return 发送结果状态：0（成功）、1（失败）
     */
    public int sendMessage(MessageTypeEnum messageType, String content, ShopMember member, StoreBusiness storeBusiness, MessageSendTypeeEnum messageSendType, boolean isGreeting) {

        //店中店
        if (storeBusiness.getWxaBusinessType() == 1) {
            storeBusiness = userMapper.getStoreBusinessById(11878L);
        }
        //1、向微信会员发送客服消息
        int sendState = 1;//发送结果状态：0（成功）、1（失败）
        String appId = storeBusiness.getWxaAppId();
        if (StringUtils.isEmpty(appId)) {
            logger.info("客服发送消息给会员失败，商家的appId不能为空，请尽快排查问题，storeBusiness：" + storeBusiness.toString());
            return sendState;
        }
        logger.info("客服发送消息给会员storeBusiness：" + storeBusiness.toString());
        String ret = sendMessageToWeiXin(messageType, content, member.getBindWeixin(), appId);
        logger.info("客服发送消息结果ret：" + ret);
        if (StringUtils.isNotEmpty(ret)) {
            JSONObject retObject = (JSONObject) JSONObject.parse(ret);
//			{"errcode":45015,"errmsg":"response out of time limit or subscription is canceled hint: [M18c60466ge21]"}
            int errcode = retObject.getIntValue("errcode");
            if (errcode == 0) {//0则为成功
                sendState = 0;
            }
        }

        addNewMessage(messageType, content, member, storeBusiness, messageSendType, sendState, ret, isGreeting);


        return sendState;
    }

    /**
     * 添加信息消息
     *
     * @param messageType
     * @param content
     * @param member
     * @param storeBusiness
     * @param messageSendType
     * @param sendState
     * @param isGreeting      是否是问候语，问候语则消息为已读
     * @param ret
     */
    private void addNewMessage(MessageTypeEnum messageType, String content, ShopMember member, StoreBusiness storeBusiness,
                               MessageSendTypeeEnum messageSendType, int sendState, String ret, boolean isGreeting) {
        //2、添加消息记录
        Message message = new Message();
        message.setStoreId(storeBusiness.getId());
        message.setAdminId(1L);
        message.setAdminName(storeBusiness.getBusinessName());
        message.setAdminHeadimg(storeBusiness.getStoreLogo());
        message.setMemberId(member.getId());
        message.setMemberName(member.getUserNickname());
        message.setMemberHeadimg(member.getUserIcon());
        message.setSendType(messageSendType.getIntValue());//发送类型：0客服单发会员、1会员单发客服、2客服群发会员
        if (messageType.getIntCode() == MessageTypeEnum.text.getIntCode()) {
            message.setTextContent(content);
        } else if (messageType.getIntCode() == MessageTypeEnum.image.getIntCode()) {
            message.setImagePicUrl(content);
        }
        message.setMessageType(messageType.getIntCode());
        message.setMessageTypeCode(messageType.getCode());
        long nowTime = DateUtil.current(false);
        message.setCreateTime(nowTime);
        message.setUpdateTime(nowTime);
        message.setWeixinReturnJson(ret);
        message.setSendState(sendState);
        if (isGreeting) {
            message.setReadState(1);   //阅读状态:0未读，1已读
        }
        messageMapper.insert(message);


        //如果是发送成功，是系统代替客服发送会员消息则对APP端客服发送个推推送
        int messageSendState = message.getSendState();
        if (messageSendState == 0 && MessageSendTypeeEnum.systemReplaceServerToMember.getIntValue() == message.getSendType()) {
            messageReceiveService.sendMessageToServer(message, storeBusiness.getUserCID());
        }

        //3、修改会员最后发送消息内容
        memberService.updateMemberLastMessage(message, member.getId());
    }


    private String sendMessageToWeiXin(MessageTypeEnum messageType, String content, String bindWeixin, String appId) {
        logger.info("向微信会员发送客服消息,messageType:" + messageType.getIntCode() + ",bindWeixin:" + bindWeixin + ",appId:" + appId);
        if (messageType.getIntCode() == MessageTypeEnum.text.getIntCode()) {
            return messageSendService.sendText(content, bindWeixin, appId);
        } else if (messageType.getIntCode() == MessageTypeEnum.image.getIntCode()) {
            return messageSendService.sendImage(content, bindWeixin, appId);
        } else {
            logger.info("向微信会员发送客服消息消息类型错误,messageType:" + messageType.getIntCode() + ",bindWeixin:" + bindWeixin + ",appId:" + appId);
            return null;
        }
    }

    public void insertMessage(Message message) {
        messageMapper.insert(message);
    }

    /**
     * 记录会员咨询商家商品接口
     * 说明：用户在商品详情进入客服会话时记录咨询的商家商品
     *
     * @return
     */
    public void setMemberMessagePorduct(long shopProductId, long memberId, long shopId) {
        String groupKey = MemcachedKey.GROUP_KEY_MEMBER_MESSAGE_PORDUCT;
        String key = "_" + memberId + "_" + shopId;
        int expiry = 10;//十秒消失
        memcachedService.set(groupKey, key, expiry, shopProductId);
        logger.info("记录会员咨询商家商品,groupKey:" + groupKey + ",key:" + key + ",shopProductId:" + shopProductId);
    }

    /**
     * 清除会员咨询商家商品接口
     * 说明：用户在商品详情进入客服会话时记录咨询的商家商品，
     *
     * @return
     */
    public void clearMemberMessagePorduct(long memberId, long shopId) {
        String groupKey = MemcachedKey.GROUP_KEY_MEMBER_MESSAGE_PORDUCT;
        String key = "_" + memberId + "_" + shopId;
        memcachedService.remove(groupKey, key);
        logger.info("清除会员咨询商家商品,groupKey:" + groupKey + ",key:" + key);
    }

    /**
     * 获取会员咨询商家商品
     * 说明：在会员发送消息后自动回复
     *
     * @return
     */
    public long getMemberMessagePorductId(long memberId, long shopId) {
        String groupKey = MemcachedKey.GROUP_KEY_MEMBER_MESSAGE_PORDUCT;
        String key = "_" + memberId + "_" + shopId;
        Object shopProductIdObject = memcachedService.get(groupKey, key);
        long shopProductId = 0;
        if (shopProductIdObject != null) {
            shopProductId = (Long) shopProductIdObject;
        }
        logger.info("获取会员咨询商家商品,groupKey:" + groupKey + ",key:" + key + ",shopProductId:" + shopProductId);
        return shopProductId;
    }


}
