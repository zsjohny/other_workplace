package com.e_commerce.miscroservice.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.entity.application.user.ShopMemberVo;
import com.e_commerce.miscroservice.commons.entity.live.LiveRoomMsgDTO;
import com.e_commerce.miscroservice.commons.entity.order.ShopMemberOrderItemQuery;
import com.e_commerce.miscroservice.commons.entity.task.DataBase;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.entity.user.ShopMemberQuery;
import com.e_commerce.miscroservice.commons.enums.live.LiveTalkTypeEnum;
import com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums;
import com.e_commerce.miscroservice.commons.exception.ErrorHelper;
import com.e_commerce.miscroservice.commons.helper.log.Log;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;
import com.e_commerce.miscroservice.commons.utils.BeanKit;
import com.e_commerce.miscroservice.product.dao.LiveProductDao;
import com.e_commerce.miscroservice.product.entity.LiveProduct;
import com.e_commerce.miscroservice.product.rpc.ShopMemberOrderRpcService;
import com.e_commerce.miscroservice.product.rpc.ShopMemberRpcService;
import com.e_commerce.miscroservice.product.rpc.TaskRpcService;
import com.e_commerce.miscroservice.product.service.LiveRoomMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.LIVE_TALK_RECEIVE;
import static com.e_commerce.miscroservice.commons.enums.task.TaskTypeEnums.LIVE_TALK_SEND;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/18 13:47
 * @Copyright 玖远网络
 */
@Service
public class LiveRoomMessageServiceImpl implements LiveRoomMessageService {

    private static final String LIVE_ORDER_TALK = "已剁手%s件";

    private Log logger = Log.getInstance(LiveRoomMessageServiceImpl.class);

    @Autowired
    private ShopMemberOrderRpcService shopMemberOrderRpcService;
    @Autowired
    private ShopMemberRpcService shopMemberRpcService;
    @Autowired
    private LiveProductDao liveProductDao;

    @Autowired
    private TaskRpcService taskRpcService;

    @Override
    public Response sendTalk(LiveRoomMsgDTO sendDTO) {
        logger.info("直播间用户发言 {}", sendDTO);
        //系统通知只管发,不用校验直播间的状态
        LiveTalkTypeEnum type = LiveTalkTypeEnum.me(sendDTO.getType());
        switch (type) {
            case SYS_POLICY:
            case ADD_CART:
            case ORDER:
            case MEMBER_IN_ROOM:
                sendDTO.setNeedVerify(false);
                break;
            default:
                sendDTO.setNeedVerify(true);
        }
        //调度
        DataBase dataBase = DataBase.me();
        dataBase.setRequestDataJson(JSONObject.toJSONString(sendDTO));
        dataBase.setCode(LIVE_TALK_SEND.getKey());
        dataBase.setModuleType(TaskTypeEnums.MODEL_TYPE_LIVE.getKey());
        LiveData liveData = new LiveData();
        liveData.setCode(sendDTO.getRoomCode());
        dataBase.setObj(liveData);
        return taskRpcService.disposeDistribute(dataBase);
    }

    @Override
    public Response receiveTalk(LiveRoomMsgDTO msgDTO) {
        logger.info("读取直播平台发言 {}", msgDTO);
        DataBase dataBase = DataBase.me();
        dataBase.setRequestDataJson(JSONObject.toJSONString(msgDTO));
        dataBase.setModuleType(TaskTypeEnums.MODEL_TYPE_LIVE.getKey());
        dataBase.setCode(LIVE_TALK_RECEIVE.getKey());

        LiveData liveData = new LiveData();
        liveData.setCode(msgDTO.getRoomCode());
        dataBase.setObj(liveData);
        return taskRpcService.disposeDistribute(dataBase);
    }

    /**
     * 平台,系统发言
     *
     * @param sendDTO sendDTO
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/21 14:45
     */
    @Override
    public Response sendSysTalk(LiveRoomMsgDTO sendDTO) {
        Long liveProductId = sendDTO.getLiveProductId();
        logger.info("系统发言sendSysTalk dto", sendDTO);

        LiveProduct lp = liveProductDao.findById(liveProductId);
        ErrorHelper.declareNull(lp, "没有找到直播商品");

        ShopMemberQuery userQuery = new ShopMemberQuery();
        userQuery.setId(sendDTO.getMemberId());
        ShopMemberVo user = shopMemberRpcService.findOne(userQuery);
        if (user == null) {
            logger.warn("未找到用户");
            return Response.errorMsg("未找到用户");
        }
        sendDTO.setStoreId(user.getStoreId());

        String userName = BeanKit.strOf(user.getUserNickname(), "**");
        String userIcon = BeanKit.strOf(user.getUserIcon(), "");

        int type = sendDTO.getType();
        switch (type) {
            case 13:
                //下单
                ShopMemberOrderItemQuery orderItem = shopMemberOrderRpcService.findItemByItemId(sendDTO.getOrderItemId());
                if (orderItem == null) {
                    logger.warn("未找到订单");
                    return Response.errorMsg("未找到订单");
                } else if (orderItem.getLiveProductId() < 1) {
                    logger.warn("并不是直播商品");
                    return Response.errorMsg("并不是直播商品");
                }
                Integer count = orderItem.getCount();
                String productName = BeanKit.strOf(orderItem.getProductName(), "商品");
                String msg = String.format(LIVE_ORDER_TALK, count);
                sendDTO.setSpeakerName(userName);
                sendDTO.setSpeakerIcon(userIcon);
                sendDTO.setProductName(productName);
                sendDTO.setProductCount(count);
                sendDTO.setMsg(msg);
                sendDTO.setRoomCode(1001);
                break;
            default:
                logger.warn("未知的系统消息发送类型");
                return Response.errorMsg("未知的系统消息发送类型");
        }
        sendDTO.setRoomNum(lp.getRoomNum());
        return sendTalk(sendDTO);
    }
}
