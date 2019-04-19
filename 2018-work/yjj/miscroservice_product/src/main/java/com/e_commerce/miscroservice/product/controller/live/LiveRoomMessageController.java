package com.e_commerce.miscroservice.product.controller.live;

import com.e_commerce.miscroservice.commons.entity.live.LiveRoomMsgDTO;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.ResponseHelper;
import com.e_commerce.miscroservice.commons.utils.WxLoginHelper;
import com.e_commerce.miscroservice.product.service.LiveRoomMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/10 10:40
 * @Copyright 玖远网络
 */
@RestController
@RequestMapping( "product/live/room/message" )
public class LiveRoomMessageController {

    @Autowired
    private LiveRoomMessageService liveRoomMessageService;


    /**
     * 发言
     *
     * @param roomNum 房间号
     * @param roomCode 房间编号(如果是系统发言,可以不传)
     * @param speakerName 发言人名称
     * @param type 1,用户发言,2播主发言,11进房间,12加入购物车(暂没用),13下单,21平台通知:法律法规(暂没用)
     * @param msg 消息
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/10 10:46
     */
    @RequestMapping( "sendTalk" )
    public Response sendTalk(
            @RequestParam( "roomNum" ) Long roomNum,
            @RequestParam( "roomCode" ) Integer roomCode,
            @RequestParam( value = "storeId" ,required = false) Long storeId,
            @RequestParam( "speakerName" ) String speakerName,
            @RequestParam( "type" ) Integer type,
            @RequestParam( "msg" ) String msg
    ) {
        Optional<Long> userId = WxLoginHelper.getUserId();
        if (! userId.isPresent()) {
            return ResponseHelper.noLogin();
        }
        LiveRoomMsgDTO sendDTO = LiveRoomMsgDTO.me();
        sendDTO.setRoomNum(roomNum);
        sendDTO.setStoreId(storeId);
        sendDTO.setSpeakerName(speakerName);
        sendDTO.setType(type);
        sendDTO.setMsg(msg);
        sendDTO.setRoomCode(roomCode);
        sendDTO.setMemberId(userId.get());
        return liveRoomMessageService.sendTalk(sendDTO);
    }



    /**
     * 直播间发言(系统消息)
     *
     * @param liveProductId 直播商品id
     * @param type 1,用户发言,2播主发言,11进房间,12加入购物车(暂没用),13下单,21平台通知:法律法规(暂没用)
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/10 10:46
     */
    @RequestMapping( "sendSysTalk" )
    public Response sendSysTalk(
            @RequestParam( "memberId" ) Long memberId,
            @RequestParam( "liveProductId" ) Long liveProductId,
            @RequestParam( value = "orderItemId", required = false) Long orderItemId,
            @RequestParam( "type" ) Integer type
    ) {
        LiveRoomMsgDTO sendDTO = LiveRoomMsgDTO.me();
        sendDTO.setLiveProductId(liveProductId);
        sendDTO.setMemberId(memberId);
        sendDTO.setOrderItemId(orderItemId);
        sendDTO.setType(type);
        return liveRoomMessageService.sendSysTalk(sendDTO);
    }




    /**
     * 接收消息
     *
     * @param offset offset
     * @param roomNum 房间号
     * @param roomCode 房间编号
     * @param receiveUserType 0小程序端用户,2直播端播主
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/10 19:14
     */
    @RequestMapping( "receiveTalk" )
    public Response receive(
            @RequestParam( "storeId" ) Long storeId,
            @RequestParam( "roomCode" ) Integer roomCode,
            @RequestParam( value = "offset", defaultValue = "-1" ) Long offset,
            @RequestParam( value = "receiveUserType", defaultValue = "0" ) Integer receiveUserType,
            @RequestParam( "roomNum" ) Long roomNum
    ) {
        //验证登录
        Optional<Long> userId = WxLoginHelper.getUserId();
        if (! userId.isPresent()) {
            return ResponseHelper.noLogin();
        }

        LiveRoomMsgDTO msgDTO = LiveRoomMsgDTO.me();
        msgDTO.setOffset(offset);
        msgDTO.setStoreId(storeId);
        msgDTO.setMemberId(userId.get());
        msgDTO.setRoomCode(roomCode);
        msgDTO.setReceiveUserType(receiveUserType);
        msgDTO.setRoomNum(roomNum);
        return liveRoomMessageService.receiveTalk(msgDTO);
    }



}
