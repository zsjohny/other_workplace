package com.e_commerce.miscroservice.task.job.jobDispose;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.beust.jcommander.internal.Lists;
import com.e_commerce.miscroservice.commons.entity.live.LiveRoomMsgDTO;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.enums.colligate.RedisKeyEnum;
import com.e_commerce.miscroservice.commons.enums.live.LiveTalkTypeEnum;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.MapHelper;
import com.e_commerce.miscroservice.commons.utils.SensitiveWordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/18 14:31
 * @Copyright 玖远网络
 */
@Component
public class TaskDispatcherLiveTalkBus {

    private Log logger = Log.getInstance(TaskDispatcherLiveTalkBus.class);

    @Autowired
    private TaskDispatcherLiveBus taskDispatcherLiveBus;

    @Resource( name = "listStringRedisTemplate" )
    private ListOperations<String, String> listStringRedisTemplate;


    /**
     * 用户发言
     *
     * @param dataBase dataBase
     * @author Charlie
     * @date 2019/1/18 14:34
     */
    public void sendTalk(DataBase dataBase) {
        String reqData = dataBase.getRequestDataJson();
        LiveRoomMsgDTO dto = JSONObject.parseObject(reqData, new TypeReference<LiveRoomMsgDTO>() {});

        LiveTalkTypeEnum talkType = LiveTalkTypeEnum.me(dto.getType());
        if (talkType.equals(LiveTalkTypeEnum.UNKNOWN)) {
            dataBase.getObjectCompletableFuture().complete(Response.errorMsg("未知的用户发言类型"));
            return;
        }

        Long memberId = dto.getMemberId();
        Long storeId = dto.getStoreId();
        Long roomNum = dto.getRoomNum();
        Integer roomCode = dto.getRoomCode();
        boolean needVerify = dto.getNeedVerify();

        //查询直播间信息
        DataBase queryRoomDataBase = DataBase.me();
        queryRoomDataBase.setRoomId(roomNum);
        queryRoomDataBase.setStoreId(storeId);
        dataBase.setCode(roomCode);
        queryRoomDataBase.setObj(dataBase.getObj());
        LiveData liveInformation = taskDispatcherLiveBus.findLiveInformation(queryRoomDataBase);
        if (liveInformation == null) {
            logger.warn("直播间不存在 roomNum={},storeId={},roomCode={}", roomNum, storeId, roomCode);
            dataBase.getObjectCompletableFuture().complete(Response.errorMsg("直播间不存在"));
            return;
        }
        //校验
        if (! verifyCanSendTalk(needVerify, liveInformation, memberId)) {
            dataBase.getObjectCompletableFuture().complete(Response.success("发言,没有直播间权限"));
            return;
        }

        //放到redis
        String roomKey = findRoomTalkRedisKey(roomNum);
        JSONObject subjectMsg = new JSONObject();
        subjectMsg.put("memberId", memberId);
        subjectMsg.put("roomNum", roomNum);
//        subjectMsg.put("speakerName", talkType.formatName(dto.getSpeakerName()));
        subjectMsg.put("speakerName", dto.getSpeakerName());
        subjectMsg.put("speakerIcon", dto.getSpeakerIcon());
        subjectMsg.put("productName", dto.getProductName());
        subjectMsg.put("productCount", dto.getProductCount());
        subjectMsg.put("msg", SensitiveWordUtil.replaceSensitiveWord(dto.getMsg(), "*"));
        subjectMsg.put("type", dto.getType());
        listStringRedisTemplate.rightPush(roomKey, subjectMsg.toJSONString());
        dataBase.getObjectCompletableFuture().complete(Response.success("直播平台用户发言--成功"));
    }


    /**
     * 判断用户是否可以发言
     *
     * @param needVerify 是否需要校验
     * @param liveInfo 直播信息
     * @param memberId 预留
     * @return boolean
     * @author Charlie
     * @date 2019/1/18 15:26
     */
    private boolean verifyCanSendTalk(boolean needVerify, LiveData liveInfo, Long memberId) {
        if (! needVerify) {
            return Boolean.TRUE;
        }
        Integer status = liveInfo.getStatus();
        boolean isOpen = status.equals(LiveData.DEFAULT_OPEN_STATUS);
        if (! isOpen) {
            logger.info("用户发言--直播间状态不正常={}", status);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    /**
     * 获取发言的key
     *
     * @param roomNum roomNum
     * @return java.lang.String
     * @author Charlie
     * @date 2019/1/18 14:57
     */
    public String findRoomTalkRedisKey(Long roomNum) {
        return RedisKeyEnum.LIVE_ROOM_TALK.append(roomNum);
    }


    /**
     * 删除直播间的发言
     *
     * @param roomNum roomNum
     * @return true 删除成功
     * @author Charlie
     * @date 2019/1/18 14:56
     */
    public boolean clearRoomTalk(Long roomNum) {
        logger.info("清空直播间发言缓存 roomNum={}", roomNum);
        if (null == roomNum) {
            return Boolean.TRUE;
        }

        String key = findRoomTalkRedisKey(roomNum);
        listStringRedisTemplate.getOperations().delete(key);
        return ! listStringRedisTemplate.getOperations().hasKey(key);
    }


    /**
     * 直播间接收别人发言
     *
     * @param dataBase dataBase
     * @author Charlie
     * @date 2019/1/18 14:35
     */
    public void receiveTalk(DataBase dataBase) {
        String reqData = dataBase.getRequestDataJson();
        LiveRoomMsgDTO dto = JSONObject.parseObject(reqData, new TypeReference<LiveRoomMsgDTO>() {});

        Long roomNum = dto.getRoomNum();
        Long memberId = dto.getMemberId();
        Long offset = dto.getOffset();
        Long storeId = dto.getStoreId();
        Integer roomCode = dto.getRoomCode();
        Integer receiveUserType = dto.getReceiveUserType();
        String roomKey = findRoomTalkRedisKey(roomNum);

        //查询直播间信息
        DataBase queryRoomDataBase = DataBase.me();
        queryRoomDataBase.setRoomId(roomNum);
        queryRoomDataBase.setCode(roomCode);
        queryRoomDataBase.setStoreId(storeId);
        LiveData liveDataQuery = new LiveData();
        liveDataQuery.setCode(roomCode);
        queryRoomDataBase.setObj(liveDataQuery);
        LiveData liveInformation = taskDispatcherLiveBus.findLiveInformation(queryRoomDataBase);
        if (liveInformation == null) {
            logger.info("直播间不存在 roomNum={},storeId={},roomCode={}", roomNum, storeId, roomCode);
            dataBase.getObjectCompletableFuture().complete(Response.errorMsg("直播间不存在"));
            return;
        }
        logger.info("直播间信息 roomId={},code={}", liveInformation.getRoomId(), liveInformation.getCode());

        //校验
        if (! verifyCanReceiveTalk(liveInformation, memberId)) {
            dataBase.getObjectCompletableFuture().complete(Response.errorMsg("直播已关闭"));
            return;
        }

        MapHelper retVal = MapHelper.me(2);
        Long newOffset;
        if (offset < 0) {
            //没有则是首次请求
            newOffset = listStringRedisTemplate.size(roomKey);
            retVal.put("dataList", Collections.EMPTY_LIST);
        } else {
            List<String> msgStrList = listStringRedisTemplate.range(roomKey, offset, - 1);
            newOffset = offset + msgStrList.size();

            boolean isPlatformLive = liveInformation.getCode().equals(1002);
            if (isPlatformLive) {
                //如果是平台门店,过滤消息类型
                msgStrList = msgStrList.stream().filter(msgJsonStr -> {
                    if (StringUtils.isBlank(msgJsonStr)) {
                        return Boolean.FALSE;
                    }
                    JSONObject userTalk = JSONObject.parseObject(msgJsonStr);

                    //过滤自己的发言
                    long talkUserId = userTalk.getLongValue("memberId");
                    if (memberId.equals(talkUserId)) {
                        return false;
                    }
                    //小程序的用户,屏蔽用户的发言
                    if (receiveUserType.equals(0)) {
                        //1,用户发言,2播主发言,11进房间,12加入购物车(暂没用),13下单,21平台通知:法律法规(暂没用)
                        int talkType = userTalk.getIntValue("type");
                        return talkType > 10;
                    }
                    return true;
                }).collect(Collectors.toList());
            }
            else {
                //其他,则屏蔽自己的发言
                msgStrList = msgStrList.stream().filter(msgJsonStr -> {
                    if (StringUtils.isBlank(msgJsonStr)) {
                        return Boolean.FALSE;
                    }
                    JSONObject userTalk = JSONObject.parseObject(msgJsonStr);
                    //过滤自己的发言
                    long talkUserId = userTalk.getLongValue("memberId");
                    return ! memberId.equals(talkUserId);
                }).collect(Collectors.toList());
            }
            List<JSONObject> msgJsons = Lists.newArrayList(msgStrList.size());
            msgStrList.stream().forEach(msg-> msgJsons.add(JSONObject.parseObject(msg)));
            retVal.put("dataList", msgJsons);
        }
        retVal.put("offset", newOffset);
        dataBase.getObjectCompletableFuture().complete(Response.success(retVal));
    }

    /**
     * 校验是否可以拉取消息
     *
     * @param liveInfo 直播间信息
     * @param memberId 发言接收者
     * @return boolean
     * @author Charlie
     * @date 2019/1/18 17:37
     */
    private boolean verifyCanReceiveTalk(LiveData liveInfo, Long memberId) {
        Integer status = liveInfo.getStatus();
        boolean isOpen = status.equals(LiveData.DEFAULT_OPEN_STATUS);
        if (! isOpen) {
            logger.info("用户读取直播间发言--直播间状态不正常={}", status);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
