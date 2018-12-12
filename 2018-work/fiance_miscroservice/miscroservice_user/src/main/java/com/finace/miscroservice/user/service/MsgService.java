package com.finace.miscroservice.user.service;

import com.finace.miscroservice.commons.utils.Response;
import com.finace.miscroservice.user.entity.response.MsgResponse;

import java.util.List;

public interface MsgService {
    Response findHomeMsg(Integer userId);

    Response findMsgDetailedList(Integer userId,Integer page,Integer msgCode);

    /**
     * app首页消息通知
     * @return
     */
    List<MsgResponse> getAppIndexMsg();
}
