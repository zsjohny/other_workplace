package com.goldplusgold.td.sltp.core.listener;

import com.goldplusgold.mq.msgs.Trade2Sltp;
import com.goldplusgold.td.sltp.core.dao.UserSltpDao;
import com.goldplusgold.td.sltp.core.operate.component.OrderDeputeExchangeSubscribeComponent;
import com.goldplusgold.td.sltp.core.operate.component.OrderTradeNotifyPublishComponent;
import com.goldplusgold.td.sltp.core.operate.enums.DbOperateEnum;
import com.goldplusgold.td.sltp.core.service.UserSltpService;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 止盈止损的委托平仓监听
 * Created by Ness on 2017/5/10.
 */
@Component
public class SltpOffsetListener extends OrderDeputeExchangeSubscribeComponent {
    private Logger logger = LoggerFactory.getLogger(SltpOffsetListener.class);
    @Autowired
    private UserSltpDao userSltpDao;

    @Autowired
    private OrderTradeNotifyPublishComponent orderTradeNotifyPublishComponent;

    private ExecutorService saveExecutor = Executors.newSingleThreadExecutor();

    @Override
    public void onMsg(Object msg) {
        if (msg == null) {
            logger.warn("监听止盈止损的委托所接受的参数为空");
            return;
        }

        //--具体协议
        UserSltpRecord userSltpRecord = (UserSltpRecord) msg;


        if (!userSltpRecord.isAccordTrasferTrade()) {
            logger.warn("监听止盈止损的委托实体类参数不符合");
            return;
        }


        logger.info("开始处理{}止盈止损的委托协议", msg);

        userSltpRecord.setAutoTriggle(Boolean.TRUE);

        //传送到交易
        Trade2Sltp trade2Sltp = new Trade2Sltp();
        trade2Sltp.setPrice(userSltpRecord.getCommissionPrice());
        trade2Sltp.setUserId(userSltpRecord.getUserId());
        trade2Sltp.setProdCode(userSltpRecord.getContract());
        trade2Sltp.setSltpId(userSltpRecord.getUuid());
        trade2Sltp.setVolume(userSltpRecord.getLots());
        trade2Sltp.setTradeType(userSltpRecord.getBearBull());

        orderTradeNotifyPublishComponent.publish(trade2Sltp);

        System.out.println(userSltpRecord.getUserId());
        saveExecutor.execute(() -> userSltpDao.saveUserSltpRecordInfo(userSltpRecord, DbOperateEnum.MYSQL.getFlag()));


        logger.info("结束处理{}止盈止损的委托协议", msg);


    }

    //测试
//    @PostConstruct
    public void test() {
        new Thread(() -> {
            while (true) {
                UserSltpRecord userSltpRecord = new UserSltpRecord();
                userSltpRecord.setSlPrice(24.00);
                userSltpRecord.setUserId("1089117625");
                userSltpRecord.setContract("22222");
                userSltpRecord.setUuid("220100202");
                userSltpRecord.setLots(1);
                userSltpRecord.setBearBull(1);
                userSltpRecord.setCommissionPrice(275.00);
                onMsg(userSltpRecord);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        }).start();


    }
}
