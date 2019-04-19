package com.e_commerce.miscroservice.product.service;


import com.e_commerce.miscroservice.commons.entity.live.LiveRoomMsgDTO;
import com.e_commerce.miscroservice.commons.helper.util.service.Response;

/**
 * @author Charlie
 * @version V1.0
 * @date 2019/1/18 13:47
 * @Copyright 玖远网络
 */
public interface LiveRoomMessageService {


    /**
     * 直播发言
     *
     * @param sendDTO {
     * roomNum:房间号
     * speakerName:发言人姓名
     * type:发言类型
     * msg:内容
     * }
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/18 13:59
     */
    Response sendTalk(LiveRoomMsgDTO sendDTO);

    /**
     * 接收消息
     *
     * @param msgDTO msgDTO
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/18 17:26
     */
    Response receiveTalk(LiveRoomMsgDTO msgDTO);

    /**
     * 平台,系统发言
     *
     * @param sendDTO sendDTO
     * @return com.e_commerce.miscroservice.commons.helper.util.service.Response
     * @author Charlie
     * @date 2019/1/21 14:45
     */
    Response sendSysTalk(LiveRoomMsgDTO sendDTO);
}
