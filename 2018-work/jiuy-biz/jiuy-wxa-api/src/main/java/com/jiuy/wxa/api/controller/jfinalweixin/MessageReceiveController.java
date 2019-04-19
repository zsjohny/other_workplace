package com.jiuy.wxa.api.controller.jfinalweixin;

import com.jiuyuan.web.help.JsonResponse;
import com.store.service.MemberService;
import com.store.service.MessageReceiveService;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 接收处理消息接口
 * <p>
 * 为JFinal 微信提供的业务http业务接口
 *
 * @author 赵兴林
 * @since 2017-06-21
 */
@Controller
@RequestMapping("/wxa")
public class MessageReceiveController {
    private static final Log logger = LogFactory.get();
    @Autowired
    MemberService memberService;
    @Autowired
    MessageReceiveService messageReceiveService;

    /**
     * 获取自动回复文本信息
     *
     * @param toUserName
     * @param fromUserName
     * @return
     */
    private JsonResponse getJsonResponse(String toUserName, String fromUserName) {
        //处理自动回复信息
        JsonResponse jsonResponse = new JsonResponse();
        String returnMsg = messageReceiveService.getReturnMsg(toUserName, fromUserName);
        Map<String, String> data = new HashMap<String, String>();
        data.put("returnMsg", returnMsg);
        return jsonResponse.setSuccessful().setData(data);

    }


    @RequestMapping("/processUserEnterSessionMsg")
    @ResponseBody
    public JsonResponse processUserEnterSessionMsg(
            String toUserName,
            String fromUserName,
            Integer createTime,
            String msgType,//消息类型1：text 文本消息  2：image 图片消息 	3: Event 事件消息
            String event,
            String sessionFrom) {
        logger.info("小程序工程处理接收到的进入会话事件（会员进入客服会话触发）processUserEnterSessionMsg,toUserName:" + toUserName + ",fromUserName:" + fromUserName);

        messageReceiveService.processUserEnterSessionMsg(toUserName, fromUserName);
        return getJsonResponse(toUserName, fromUserName);
    }

    /**
     * 接收文本消息
     */
    @RequestMapping("/processInTextMsg")
    @ResponseBody
    public JsonResponse processInTextMsg(@RequestParam(required = true) String content, String msgId, String toUserName, String fromUserName, String msgType, Integer createTime) {
        messageReceiveService.processInTextMsg(content, msgId, toUserName, fromUserName, msgType);
        return getJsonResponse(toUserName, fromUserName);
    }


    /**
     * 接收链接消息
     */
    @RequestMapping("/processInLinkMsg")
    @ResponseBody
    public JsonResponse processInLinkMsg(@RequestParam(required = true) String title, String description, String url, String msgId, String toUserName,
                                         String fromUserName, String msgType, Integer createTime) {
        messageReceiveService.processInLinkMsg(title, description, url, msgId, toUserName,
                fromUserName, msgType, createTime);
        return getJsonResponse(toUserName, fromUserName);
    }

    /**
     * 接收图片消息
     */
    @RequestMapping("/processInImageMsg")
    @ResponseBody
    public JsonResponse processInImageMsg(
            @RequestParam(required = true) String picUrl, String mediaId, String msgId, String toUserName,
            String fromUserName, String msgType, Integer createTime) {

        messageReceiveService.processInImageMsg(picUrl, mediaId, msgId, toUserName,
                fromUserName, msgType, createTime);
        return getJsonResponse(toUserName, fromUserName);
    }

    /**
     * 接收语音消息
     */
    @RequestMapping("/processInVoiceMsg")
    @ResponseBody
    public JsonResponse processInVoiceMsg(
            @RequestParam(required = true) String mediaId, String format, String msgId, String toUserName,
            String fromUserName, String msgType, Integer createTime) {
        messageReceiveService.processInVoiceMsg(
                mediaId, format, msgId, toUserName,
                fromUserName, msgType, createTime);
        return getJsonResponse(toUserName, fromUserName);
    }

    /**
     * 接收视频消息
     */
    @RequestMapping("/processInVideoMsg")
    @ResponseBody
    public JsonResponse processInVideoMsg(
            @RequestParam(required = true) String mediaId, String thumbMediaId, String msgId, String toUserName,
            String fromUserName, String msgType, Integer createTime) {
        messageReceiveService.processInVideoMsg(mediaId, thumbMediaId, msgId, toUserName,
                fromUserName, msgType, createTime);
        return getJsonResponse(toUserName, fromUserName);
    }

    /**
     * 接收小视频消息
     */
    @RequestMapping("/processInShortVideoMsg")
    @ResponseBody
    public JsonResponse processInShortVideoMsg(@RequestParam(required = true) String mediaId, String thumbMediaId, String msgId, String toUserName,
                                               String fromUserName, String msgType, Integer createTime) {
        messageReceiveService.processInShortVideoMsg(mediaId, thumbMediaId, msgId, toUserName,
                fromUserName, msgType, createTime);
        return getJsonResponse(toUserName, fromUserName);
    }

}


