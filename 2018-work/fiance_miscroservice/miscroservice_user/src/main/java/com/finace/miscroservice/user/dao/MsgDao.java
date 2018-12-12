package com.finace.miscroservice.user.dao;

import com.finace.miscroservice.user.entity.response.MsgResponse;

import java.util.List;
import java.util.Map;

public interface MsgDao {
    /**
     * 消息中心首页
     * @param userId
     * @return
     */
    public Map<Integer,MsgResponse> findHomeMsg(Integer userId);

    /**
     * 消息中心 消息详情
     * @param userId
     * @param msgCode
     * @return
     */
    public List<MsgResponse>   findMsgDetailedList(Integer userId, Integer msgCode);


    /**
     * app首页消息通知
     * @return
     */
    public List<MsgResponse> getMsgByAppIndex();
}
