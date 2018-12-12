package com.goldplusgold.td.sltp.monitor.filter;

import com.goldplusgold.mq.msgs.DynamicQuotationBOWrapper;
import com.goldplusgold.mq.pubsub.MQPublisher;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import com.goldplusgold.td.sltp.core.operate.component.RedisHashOperateComponent;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.cache.QuotaPriceLimit;
import com.goldplusgold.td.sltp.monitor.conf.Constant;
import com.goldplusgold.td.sltp.monitor.dbservice.AvailableLotsOperations;
import com.goldplusgold.td.sltp.monitor.dbservice.KeyRecordsOperations;
import com.goldplusgold.td.sltp.monitor.dbservice.Utils;
import com.goldplusgold.td.sltp.monitor.model.AvailableLots;
import com.goldplusgold.td.sltp.monitor.model.KeyPointOfSltpRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisZSetCommands;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 行情事件处理器
 * Created by Administrator on 2017/5/15.
 */
@Configuration
public class QuotaHandler {
    Logger logger = LoggerFactory.getLogger(QuotaHandler.class);

    @Autowired
    private KeyRecordsOperations keyRecordsOperations;

    @Autowired
    private AvailableLotsOperations availableLotsOperations;

    @Autowired
    private RedisHashOperateComponent redisHashOperateComponent;

    @Autowired
    private Calc calc;

    @Autowired
    private QuotaPriceLimit quotaPriceLimit;

    private MQPublisher mqPublisher;

    @Autowired
    public QuotaHandler(@Qualifier("msgChannelBusSimpleImpl") MsgChannelBus msgChannelBus) {
        mqPublisher = msgChannelBus.registerPublisher(PubSubChannels.CH_SLTP_TRIGGER);
    }

    public void handle(DynamicQuotationBOWrapper quota){
        long start = System.currentTimeMillis();

        //缓存涨跌停板
        quotaPriceLimit.setHigh(quota.getInstType(), quota.getBo().getHighestPrice());
        quotaPriceLimit.setLow(quota.getInstType(), quota.getBo().getLowestPrice());

        //一, 获取并删除ZSET数据
        List<String> zsetKeys = Utils.generateZsetKeys(quota.getInstType());
        List<RedisZSetCommands.Range> ranges = Utils.generateRanges(zsetKeys, quota.getBo().getLastPrice());
        Map<String, Set<KeyPointOfSltpRecord>> keyRecordsMap = keyRecordsOperations.batchGetAndRem(zsetKeys, ranges);

        //二, ZSET数据根据空/多仓分组, 根据用户ID去重
        Map<String, Set<String>> usersMap = calc.groupAndRemDuplicate(keyRecordsMap);

        for (Map.Entry<String, Set<String>> entry : usersMap.entrySet()){
            String[] strings = entry.getKey().split(Constant.HYPHEN);
            String bearBullStr = strings[2];
            Integer bearBull;
            if (Constant.BEARBULL_BULL.equals(bearBullStr)){
                bearBull = UserSltpRecord.SltpType.BULL.toType();
            }else{
                bearBull = UserSltpRecord.SltpType.BEAR.toType();
            }
            Set<String> toDelete = new HashSet<>();
            for(String userID : entry.getValue()){
                AvailableLots availableLots = availableLotsOperations.getAvailableLots(quota.getInstType(), userID);

                //三, 获取止盈止损记录详细信息
                List<UserSltpRecord> detailRecords = redisHashOperateComponent.get(quota.getInstType(), userID, bearBull);

                //四, 筛选出符合触发条件的止盈止损记录
                List<UserSltpRecord> triggeredRecords = calc.filterTriggeredRecords(detailRecords, availableLots, quota);

                for (UserSltpRecord userSltpRecord : triggeredRecords){

                    //五, 通知核心模块
                    mqPublisher.publish(userSltpRecord.trasferTrade());

                    toDelete.add(userSltpRecord.getUuid());
                }
                Set<String> recordsWillNeverTriggered = calc.filterRecordsWillNeverTriggered(detailRecords, availableLots);
                toDelete.addAll(recordsWillNeverTriggered);
            }
            //六, 删除已触发的和永远不能触发的止盈止损记录
            redisHashOperateComponent.delete(toDelete);
            //七, 推送删除通知到用户
            //TODO
        }

        long end =System.currentTimeMillis();
        logger.info("处理完一条行情, 用时: " + (end - start) + "ms.");
    }
}
