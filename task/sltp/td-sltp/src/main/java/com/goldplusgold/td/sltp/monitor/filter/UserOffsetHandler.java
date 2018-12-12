package com.goldplusgold.td.sltp.monitor.filter;

import com.goldplusgold.mq.msgs.UserOffset;
import com.goldplusgold.td.sltp.core.operate.component.RedisHashOperateComponent;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.dbservice.AvailableLotsOperations;
import com.goldplusgold.td.sltp.monitor.model.AvailableLots;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Set;

/**
 * 用户主动平仓处理器
 * Created by Administrator on 2017/5/15.
 */
@Component
public class UserOffsetHandler {
    Logger logger = LoggerFactory.getLogger(QuotaHandler.class);

    @Autowired
    private RedisHashOperateComponent redisHashOperateComponent;

    @Autowired
    private AvailableLotsOperations availableLotsOperations;

    @Autowired
    private Calc calc;

    public void handle(UserOffset userOffset){
        long start = System.currentTimeMillis();

        AvailableLots availableLots = availableLotsOperations.getAvailableLots(userOffset.getContractName(), userOffset.getUserID());

        //一, 获取止盈止损记录详细信息
        LinkedList<UserSltpRecord> detailRecords = redisHashOperateComponent.get(userOffset.getContractName(), userOffset.getUserID(), userOffset.getBearBull());

        //二,删除永远不能触发的止盈止损记录
        Set<String> toDelete = calc.filterRecordsWillNeverTriggered(detailRecords, availableLots);
        redisHashOperateComponent.delete(toDelete);

        //三, 推送删除通知到用户
        //TODO

        long end =System.currentTimeMillis();
        logger.info("处理完一例用户主动平仓事件, 用时: " + (end - start) + "ms.");
    }
}
