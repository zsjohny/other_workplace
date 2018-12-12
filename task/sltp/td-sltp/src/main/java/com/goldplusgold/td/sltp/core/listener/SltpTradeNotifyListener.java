package com.goldplusgold.td.sltp.core.listener;

import com.goldplusgold.mq.msgs.Trade2Sltp;
import com.goldplusgold.td.sltp.core.operate.component.OrderTradeNotifySubscribeComponent;
import com.goldplusgold.td.sltp.core.operate.enums.DbOperateEnum;
import com.goldplusgold.td.sltp.core.service.UserSltpService;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 止盈止损交易回调处理
 * <p>
 * Created by Ness on 2017/5/15.
 */
@Component
public class SltpTradeNotifyListener extends OrderTradeNotifySubscribeComponent {
    private Logger logger = LoggerFactory.getLogger(SltpTradeNotifyListener.class);

    @Autowired
    private UserSltpService userSltpService;

    @Override
    public void onMsg(Object msg) {
        if (msg == null) {
            logger.warn("监听止盈止损的交易回调所接受的参数为空");
            return;
        }


        Trade2Sltp trade2Sltp = (Trade2Sltp) msg;
        if (trade2Sltp.getResult() == null || StringUtils.isEmpty(trade2Sltp.getSltpId())) {

            logger.warn("监听止盈止损的交易回调所接受的强转后参数不符合规范");
            return;
        }

        //处理----result和uuid
        UserSltpRecord userSltpRecord = new UserSltpRecord();
        userSltpRecord.setCommissionResult(trade2Sltp.getResult());
        userSltpRecord.setUuid(trade2Sltp.getSltpId());
        userSltpRecord.setAutoTriggle(Boolean.TRUE);
        logger.info("开始更新止盈止损的交易回调数据处理...");
        userSltpService.updateUserSltpRecordInfoByUuid(userSltpRecord, DbOperateEnum.MYSQL.getFlag());
        logger.info("结束更新止盈止损的交易回调数据处理...");


    }
}
