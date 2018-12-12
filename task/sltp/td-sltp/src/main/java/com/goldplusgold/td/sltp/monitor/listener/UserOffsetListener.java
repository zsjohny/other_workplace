package com.goldplusgold.td.sltp.monitor.listener;

import com.goldplusgold.mq.msgs.UserOffset;
import com.goldplusgold.mq.p2p.P2PSubscriber;
import com.goldplusgold.td.sltp.monitor.filter.UserOffsetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户主动平仓事件监听器
 * Created by Administrator on 2017/5/16.
 */
public class UserOffsetListener implements P2PSubscriber {
    Logger logger = LoggerFactory.getLogger(UserOffsetListener.class);

    @Autowired
    private UserOffsetHandler userOffsetHandler;

    @Override
    public void onMsg(Object msg) {
        logger.info("接收到一例主动平仓事件: " + msg);
        UserOffset userOffset = (UserOffset) msg;
        if (userOffset.getContractName()==null || userOffset.getBearBull()==null || userOffset.getUserID()==null){
            logger.warn("用户主动平仓事件数据不合法! " + userOffset);
            return;
        }
        userOffsetHandler.handle(userOffset);
    }
}
