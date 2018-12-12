package com.goldplusgold.td.sltp.monitor.dbservice;

import com.goldplusgold.td.sltp.monitor.model.AvailableLots;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 用户可用手数Redis操作服务
 * Created by Administrator on 2017/5/18.
 */
@Component
public class AvailableLotsOperations {
    private static final String TD_BEAR_NAME = "SHORT";
    private static final String TD_BULL_NAME = "LONG";

    @Autowired
    @Qualifier("tDRedisTemplate")
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 获取可用手数
     * @param contractName 合约名称
     * @param userID 用户ID
     * @return
     */
    public AvailableLots getAvailableLots(String contractName, String userID){
        String key_bear = TD_BEAR_NAME + "_" + userID + "_" + contractName;
        String key_bull = TD_BULL_NAME + "_" + userID + "_" + contractName;
        String bearLots = redisTemplate.boundValueOps(key_bear).get();
        String bullLots = redisTemplate.boundValueOps(key_bull).get();
        AvailableLots availableLots = new AvailableLots();
        availableLots.setContractName(contractName);
        if (bearLots!=null){
            availableLots.setBearLots(Integer.parseInt(bearLots));
        }
        if (bullLots!=null){
            availableLots.setBullLots(Integer.parseInt(bullLots));
        }
        //TODO 测试代码, 待删除
        availableLots.setBullLots(5);
        availableLots.setBearLots(5);
        return availableLots;
    }
}
