package com.goldplusgold.td.sltp.monitor.dbservice;

import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.conf.Constant;
import com.goldplusgold.td.sltp.monitor.model.KeyPointOfSltpRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 止盈止损关键点记录Redis操作服务
 * Created by Administrator on 2017/5/15.
 */
@Component
public class KeyRecordsOperations {
    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    private StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
    private GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

    /**
     * 批量获取并删除
     * @param keys ZSET KEYs
     * @param ranges Ranges
     * @return 批量取出的止盈止损关键点记录, key: zset key
     */
    public Map<String,Set<KeyPointOfSltpRecord>> batchGetAndRem(List<String> keys, List<RedisZSetCommands.Range> ranges){
        Map<String, Set<KeyPointOfSltpRecord>> result = new HashMap<>();
        if(keys.size() == 0){
            return result;
        }

        List<Object> redisResults = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 0; i < keys.size(); i++) {
                connection.zRangeByScore(stringRedisSerializer.serialize(keys.get(i)), ranges.get(i));
                connection.zRemRangeByScore(stringRedisSerializer.serialize(keys.get(i)), ranges.get(i));
            }
            return null;
        });
        for (int i = 0; i < keys.size(); i++) {
            //noinspection unchecked
            result.put(keys.get(i), (Set<KeyPointOfSltpRecord>) redisResults.get(i<<1));
        }
        return result;
    }

    /**
     * 新增ZSET记录
     * @param userSltpRecord 用户止盈止损记录
     */
    public void add(UserSltpRecord userSltpRecord){
        String zkey = Utils.generateZsetKeyByDetailRecord(userSltpRecord);
        KeyPointOfSltpRecord keyPointOfSltpRecord = Utils.generateZsetValueByDetailRecord(userSltpRecord);
        Double score;
        if (zkey.contains(Constant.HYPHEN + Constant.SLTP_TP)){
            score = userSltpRecord.getTpPrice();
        }else {
            score = userSltpRecord.getSlPrice();
        }
        redisTemplate.boundZSetOps(zkey).add(keyPointOfSltpRecord, score);
    }

    /**
     * 更新ZSET记录
     * @param userSltpRecord 待更新的用户止盈止损记录
     */
    public void update(UserSltpRecord userSltpRecord){
        add(userSltpRecord);
    }

    /**
     * 批量删除ZSET数据
     * @param recordsToDelete 待删除的止盈止损记录
     */
    public void batchDelete(List<UserSltpRecord> recordsToDelete){
        List<String> zkeys = new LinkedList<>();
        List<KeyPointOfSltpRecord> zvalues = new LinkedList<>();
        for (UserSltpRecord record : recordsToDelete){
            zkeys.add(Utils.generateZsetKeyByDetailRecord(record));
            zvalues.add(Utils.generateZsetValueByDetailRecord(record));
        }
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (int i = 0; i < recordsToDelete.size(); i++) {
                connection.zRem(stringRedisSerializer.serialize(zkeys.get(i)), genericJackson2JsonRedisSerializer.serialize(zvalues.get(i)));
            }
            return null;
        });
    }

    /**
     * 清空所有ZSET
     */
    public void clearAll(){
        String[] zkeys = new String[]{
            Constant.ZKEY_AUTD_BEAR_TP, Constant.ZKEY_AUTD_BEAR_SL, Constant.ZKEY_AUTD_BULL_TP, Constant.ZKEY_AUTD_BULL_SL,
            Constant.ZKEY_MAUTD_BEAR_TP, Constant.ZKEY_MAUTD_BEAR_SL, Constant.ZKEY_MAUTD_BULL_TP, Constant.ZKEY_MAUTD_BULL_SL,
            Constant.ZKEY_AGTD_BEAR_TP, Constant.ZKEY_AGTD_BEAR_SL, Constant.ZKEY_AGTD_BULL_TP, Constant.ZKEY_AGTD_BULL_SL
        };
        redisTemplate.delete(Arrays.asList(zkeys));
    }

}
