package com.store.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.jiuyuan.entity.newentity.ShopProduct;
import com.jiuyuan.entity.newentity.StoreBusiness;
import com.jiuyuan.service.common.IStoreBusinessNewService;
import com.jiuyuan.util.GetuiUtil;
import com.store.entity.member.ShopMember;
import com.store.entity.message.Message;
import com.store.enumerate.MessageSendTypeeEnum;
import com.store.enumerate.MessageTypeEnum;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 接收消息服务实现类
 * </p>
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Service
public class MessageReceiveService {
    private static final Log logger = LogFactory.get();

    public static Map<String, Long> uuidRelationMap = new ConcurrentHashMap<>();

    @Autowired
    private StoreUserService storeUserService;

    @Autowired
    private IStoreBusinessNewService storeBusinessNewService;

    /***店中店Id*/
    private final long SHOP_IN_SHOP_STORE_ID = 11878L;

    @Autowired
    MessageService messageService;

    @Autowired
    ShopProductService shopProductService;

    @Autowired
    MemberService memberService;

    public void processUserEnterSessionMsg(String toUserName, String fromUserName) {
        //1、根据toUserName获得商家信息和客服信息gh_61a5b0fe7de3
//		storeUserService.getStoreBusinessByWeiXinNum(weiXinNum)
        StoreBusiness storeBusiness = storeBusinessNewService.getStoreBusinessByWeiXinNum(toUserName);
        if (storeBusiness == null) {
            logger.info("receiveMessage接收消息处理，获取商家信息为空，请尽快排查问题，storeBusiness:" + JSONObject.toJSONString(storeBusiness) + ",toUserName:" + toUserName + ",fromUserName:" + fromUserName);
            return;
        }

        //店中店转换 根据 fromUserName(openId查询到定中店的id)
        if (storeBusiness.getId().equals(SHOP_IN_SHOP_STORE_ID)) {
            Long aLong = uuidRelationMap.get(fromUserName);
            if (aLong != null) {
                storeBusiness.setId(aLong);
            }
        }

        logger.info("小程序工程处理接收到的进入会话事件（会员进入客服会话触发）processUserEnterSessionMsg,storeBusiness:" + JSON.toJSONString(storeBusiness));

        //2、根据fromUserName获得会员信息
        ShopMember member = memberService.getMemberByBindWeixin(fromUserName, storeBusiness.getId());
        if (member == null) {
            logger.info("receiveMessage接收消息处理，获取会员信息为空，请尽快排查问题，member:" + JSONObject.toJSONString(storeBusiness) + ",storeBusiness.getId():" + storeBusiness.getId() + ",fromUserName:" + fromUserName);
            return;
        }
        logger.info("小程序工程处理接收到的进入会话事件（会员进入客服会话触发）processUserEnterSessionMsg,member:" + member);

        MessageTypeEnum type = null;
//   	问候消息发送类型:0:没有设置;1:问候语;2:问候图片
        int greetingSendType = storeBusiness.getGreetingSendType();
        String greetingWords = storeBusiness.getGreetingWords();
        String greetingImage = storeBusiness.getGreetingImage();
        logger.info("发送问候语：greetingSendType:" + greetingSendType);
        logger.info("发送问候语：greetingWords:" + greetingWords);
        logger.info("发送问候语：greetingImage:" + greetingImage);
        if (greetingSendType == 1 && StringUtils.isNotEmpty(greetingWords)) {
//   	   		type = MessageTypeEnum.getEnum("text");
            type = MessageTypeEnum.text;
            String content = greetingWords.trim();//"V3.4的问候语测试，收到即测试成功。";
            logger.info("发送问候语：content:" + content);
            //2、发送消息   发送结果状态：0（成功）、1（失败）
            int sendState = messageService.sendMessage(type, content, member, storeBusiness, MessageSendTypeeEnum.systemReplaceServerToMember, true);
            logger.info("发送问候语：content:" + content + ",sendState:" + sendState);
        } else if (greetingSendType == 2 && StringUtils.isNotEmpty(greetingImage)) {
            type = MessageTypeEnum.image;

            logger.info("发送问候语：greetingImage:" + greetingImage);
            String content = greetingImage.trim();
            logger.info("发送问候语：content:" + content);
            //2、发送消息   发送结果状态：0（成功）、1（失败）
            int sendState = messageService.sendMessage(type, content, member, storeBusiness, MessageSendTypeeEnum.serverToMember);
            logger.info("发送问候语：content:" + content + ",sendState:" + sendState);
        } else {
            logger.info("没有设置问候语和问候图片不发送");
        }

//   	   	if(sendState == 0){
//   	   		jsonResponse.setSuccessful();
//   	   	}else{
//   	   		jsonResponse.setResultCode(ResultCode.WXA_SEND_SERVER_MSG_ERROR);
//   	   	}
//   	   	return jsonResponse;


    }

    /**
     * 处理文本消息
     */
    public void processInTextMsg(String content, String msgId, String toUserName, String fromUserName, String msgType) {
        Message message = new Message();
        message.setTextContent(content);
        message.setWeixinMsgId(msgId);

        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
        message.setMessageTypeCode(MessageTypeEnum.getEnum(msgType).getCode());
        receiveMessage(message, toUserName, fromUserName);
    }

    /**
     * 处理链接消息
     */
    public void processInLinkMsg(String title, String description, String url, String msgId, String toUserName,
                                 String fromUserName, String msgType, Integer createTime) {
        Message message = new Message();
        message.setLinkTitle(title);
        message.setLinkDescription(description);
        message.setLinkUrl(url);
        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
        receiveMessage(message, toUserName, fromUserName);
    }

    /**
     * 处理图片消息
     */
    public void processInImageMsg(String picUrl, String mediaId, String msgId, String toUserName,
                                  String fromUserName, String msgType, Integer createTime) {
        Message message = new Message();
        message.setImagePicUrl(picUrl);
        message.setImageMediaId(mediaId);
        message.setWeixinMsgId(msgId);
        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
        receiveMessage(message, toUserName, fromUserName);
    }

    /**
     * 处理语音消息
     */
    public void processInVoiceMsg(
            String mediaId, String format, String msgId, String toUserName,
            String fromUserName, String msgType, Integer createTime) {
        Message message = new Message();
        message.setVideoMediaId(mediaId);
        message.setVoiceFormat(format);
        message.setWeixinMsgId(msgId);
        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
    }

    /**
     * 处理视频消息
     */
    public void processInVideoMsg(String mediaId, String thumbMediaId, String msgId, String toUserName,
                                  String fromUserName, String msgType, Integer createTime) {
        Message message = new Message();
        message.setVideoMediaId(mediaId);
        message.setVideoThumbMediaId(thumbMediaId);

        message.setWeixinMsgId(msgId);
        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
    }

    /**
     * 处理短视频消息
     */
    public void processInShortVideoMsg(String mediaId, String thumbMediaId, String msgId, String toUserName,
                                       String fromUserName, String msgType, Integer createTime) {
        Message message = new Message();
        message.setShortvideoMediaId(mediaId);
        message.setShortvideoThumbMediaId(thumbMediaId);

        message.setWeixinMsgId(msgId);
        message.setMessageType(MessageTypeEnum.getEnum(msgType).getIntCode());
    }

    /**
     * 客服接收会员发送消息
     * 发送消息:
     * <xml>
     * <ToUserName><![CDATA[oPsob0cG0vfetPNi_QbrqszuMEjg]]></ToUserName>
     * <FromUserName><![CDATA[gh_61a5b0fe7de3]]></FromUserName>
     * <>1498554122</CreateTime>
     * <MsgType><![CDATA[text]]></MsgType>
     * <Content><![CDATA[文本消息~]]></Content>
     * </xml>
     *
     * @param
     */
    private void receiveMessage(Message message, String toUserName, String fromUserName) {
        String weixinMsgId = message.getWeixinMsgId();

        if (!"-1".equals(weixinMsgId)) {//负1为平台发送给商家客服的提醒不做排重处理
            Message messageOld = messageService.getMessageByWeiXinMsgId(weixinMsgId);
            if (messageOld != null) {
                logger.info("接收消息时，该消息已经存在，所有不做处理，weixinMsgId：" + weixinMsgId + ",messageOld:" + JSONObject.toJSONString(messageOld));
                return;
            }
        }

        //1、根据toUserName获得商家信息和客服信息gh_61a5b0fe7de3
        if (StringUtils.isNotEmpty(toUserName)) {
            StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWeiXinNum(toUserName);
            if (storeBusiness == null) {
                logger.info("receiveMessage接收消息处理，获取商家信息为空，请尽快排查问题，storeBusiness:" + JSONObject.toJSONString(storeBusiness) + ",toUserName:" + toUserName + ",fromUserName:" + fromUserName);
                return;
            }


            //店中店转换 根据 fromUserName(openId查询到定中店的id)
            if (storeBusiness.getId().equals(SHOP_IN_SHOP_STORE_ID)) {
                Long aLong = uuidRelationMap.get(fromUserName);
                if (aLong != null) {
                    storeBusiness.setId(aLong);
                }
            }

            //2、根据fromUserName获得会员信息
            ShopMember member = memberService.getMemberByBindWeixin(fromUserName, storeBusiness.getId());
            if (member == null) {
                logger.info("receiveMessage接收消息处理，获取会员信息为空，请尽快排查问题，member:" + JSONObject.toJSONString(storeBusiness) + ",storeBusiness.getId():" + storeBusiness.getId() + ",fromUserName:" + fromUserName);
                return;
            }
            //3、组装消息对象
            message.setStoreId(storeBusiness.getId());
            message.setAdminId(1L);
            message.setAdminName(storeBusiness.getBusinessName());
            message.setAdminHeadimg(storeBusiness.getStoreLogo());
            message.setMemberId(member.getId());
            message.setMemberName(member.getUserNickname());
            message.setMemberHeadimg(member.getUserIcon());
            message.setSendType(MessageSendTypeeEnum.memberToServer.getIntValue());//发送类型：0客服单发会员、1会员单发客服、2客服群发会员
            long nowTime = DateUtil.current(false);
            message.setCreateTime(nowTime);
            message.setUpdateTime(nowTime);
            message.setSendState(0);

            message.setWeixinReturnJson("storeId:" + storeBusiness.getId() + ",fromUserName:" + fromUserName + ",toUserName:" + toUserName);
            //4、添加消息记录
            messageService.insertMessage(message);

            //5、新消息触发修改会员信息表中消息相关字段
            memberService.updateMemberLastMessage(message, member.getId());

            //6、向客服手机设备进行推送消息
            logger.info("storeBusiness.getId():" + storeBusiness.getId() + ",storeBusiness.getBusinessName():" + storeBusiness.getBusinessName());
            sendMessageToServer(message, storeBusiness.getUserCID());
        } else {
            logger.warn("toUserName为空");
        }

    }

    /**
     * 向客服推送会员消息
     *
     * @throws Exception
     */

    public void sendMessageToServer(Message message, String cid) {
        if (StringUtils.isEmpty(cid)) {
            logger.info("向客服推送会员消息cid空的，当前商家没有cid请检测代码,cid:" + cid);
            return;
        }
        String title = "客服消息";
        String abstracts = JSONUtil.parse(message).toString();
//	    	String abstracts = "你有一个客服消息请查看。";
//	    	if(MessageTypeEnum.getEnum(message.getMessageType()).getIntCode() == MessageTypeEnum.text.getIntCode()){
//	    	   abstracts = message.getTextContent();
//	    	}
        String linkUrl = "";
        String image = "";//TODO 后期换成applogo

        long currentTime = System.currentTimeMillis();
        String pushTime = String.valueOf(currentTime);
        boolean ret = false;
        try {
            ret = GetuiUtil.pushGeTui(cid, title, abstracts, linkUrl, image, "13", pushTime);//13为客服消息
        } catch (Exception e) {
            logger.info("向客服推送会员消息出现异常,ret:" + ret);
            e.printStackTrace();
        }
        logger.info("向客服推送会员消息,ret:" + ret);


    }

    /**
     * 获取自动回复信息
     * 根据提前记录到缓存的会员咨询商品拼接回复信息
     *
     * @param toUserName
     * @param fromUserName
     * @return
     */
    public String getReturnMsg(String toUserName, String fromUserName) {
        String returnMsg = "";

        StoreBusiness storeBusiness = storeUserService.getStoreBusinessByWeiXinNum(toUserName);
        if (storeBusiness == null) {
            logger.info("getReturnMsg获取自动回复信息时获取商家信息为空，请尽快排查问题，storeBusiness:" + JSONObject.toJSONString(storeBusiness) + ",toUserName:" + toUserName + ",fromUserName:" + fromUserName);
            return returnMsg;
        }
        //2、根据fromUserName获得会员信息
        ShopMember member = memberService.getMemberByBindWeixin(fromUserName, storeBusiness.getId());
        if (member == null) {
            logger.info("getReturnMsg获取自动回复信息时，获取会员信息为空，请尽快排查问题，member:" + JSONObject.toJSONString(storeBusiness) + ",storeBusiness.getId():" + storeBusiness.getId() + ",fromUserName:" + fromUserName);
            return returnMsg;
        }
        long memberId = member.getId();
        long shopId = storeBusiness.getId();
        long shopProductId = messageService.getMemberMessagePorductId(memberId, shopId);
        if (shopProductId > 0) {
            ShopProduct shopProduct = shopProductService.selectById(shopProductId);
            String name = shopProduct.getName();
            long productId = shopProduct.getProductId();
            returnMsg = "亲，有什么需要帮助吗，这是：\"" + name + "，款号" + productId + "\"";

//			发送商品信息客服消息给会员
            MessageTypeEnum type = MessageTypeEnum.text;
            int sendState = messageService.sendMessage(type, returnMsg, member, storeBusiness, MessageSendTypeeEnum.systemReplaceServerToMember);
            if (sendState == 0) {
                logger.info("发送商品信息客服消息给会员成功，returnMsg:" + returnMsg);
            } else {
                logger.info("发送商品信息客服消息给会员失败，returnMsg:" + returnMsg);
            }

            //返回回复信息后将咨询商品清除
            messageService.clearMemberMessagePorduct(memberId, shopId);
        } else {
            logger.info("shopProductId为空:" + shopProductId);
        }
        logger.info("getReturnMsg获取自动回复信息returnMsg:" + returnMsg);
        return returnMsg;
    }


}
