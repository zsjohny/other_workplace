package com.goldplusgold.td.sltp.monitor.dbservice;

import com.goldplusgold.td.sltp.core.operate.enums.InstTypeEnum;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.conf.Constant;
import com.goldplusgold.td.sltp.monitor.model.KeyPointOfSltpRecord;
import org.springframework.data.redis.connection.RedisZSetCommands;

import java.util.LinkedList;
import java.util.List;

/**
 * 辅助工具类
 * Created by Administrator on 2017/5/15.
 */
public class Utils {
    /**
     * 根据合约名称生成止盈止损关键点记录ZSET KEYs
     * @param contract 合约名称
     * @return ZSET KEYs
     */
    public static List<String> generateZsetKeys(String contract){
        List<String> result = new LinkedList<>();
        if (Constant.CONTRACT_NAME_AU.equals(contract)){
            result.add(Constant.ZKEY_AUTD_BEAR_SL);
            result.add(Constant.ZKEY_AUTD_BEAR_TP);
            result.add(Constant.ZKEY_AUTD_BULL_SL);
            result.add(Constant.ZKEY_AUTD_BULL_TP);
        }else if (Constant.CONTRACT_NAME_MAU.equals(contract)){
            result.add(Constant.ZKEY_MAUTD_BEAR_SL);
            result.add(Constant.ZKEY_MAUTD_BEAR_TP);
            result.add(Constant.ZKEY_MAUTD_BULL_SL);
            result.add(Constant.ZKEY_MAUTD_BULL_TP);
        }else if (Constant.CONTRACT_NAME_AG.equals(contract)){
            result.add(Constant.ZKEY_AGTD_BEAR_SL);
            result.add(Constant.ZKEY_AGTD_BEAR_TP);
            result.add(Constant.ZKEY_AGTD_BULL_SL);
            result.add(Constant.ZKEY_AGTD_BULL_TP);
        }
        return result;
    }

    /**
     * 根据止盈止损关键记录ZSET KEYs和行情最新价生成Ranges
     * @param zsetKsys ZSET KEYs
     * @param lastPrice 行情最新价
     * @return Ranges
     */
    public static List<RedisZSetCommands.Range> generateRanges(List<String> zsetKsys, String lastPrice){
        List<RedisZSetCommands.Range> result = new LinkedList<>();
        for (String zsetKey : zsetKsys){
            String[] strings = zsetKey.split(Constant.HYPHEN);
            String bearBull = strings[2];
            String sltp = strings[3];
            if ((Constant.BEARBULL_BEAR.equals(bearBull)&&Constant.SLTP_TP.equals(sltp)) || (Constant.BEARBULL_BULL.equals(bearBull)&&Constant.SLTP_SL.equals(sltp))){
                result.add(new RedisZSetCommands.Range().lte("+inf").gte(lastPrice));
            }else if ((Constant.BEARBULL_BEAR.equals(bearBull)&&Constant.SLTP_SL.equals(sltp)) || (Constant.BEARBULL_BULL.equals(bearBull)&&Constant.SLTP_TP.equals(sltp))){
                result.add(new RedisZSetCommands.Range().lte(lastPrice).gte("-inf"));
            }
        }
        return result;
    }

    /**
     * 根据用户止盈止损记录生成ZSET KEY
     * @param userSltpRecord
     * @return 生成的ZSET KEY
     */
    public static String generateZsetKeyByDetailRecord(UserSltpRecord userSltpRecord) {
        String zkey = null;
        if (InstTypeEnum.AG.toName().equals(userSltpRecord.getContract())){
            if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AGTD_BEAR_TP;
            }else if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AGTD_BEAR_SL;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AGTD_BULL_TP;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AGTD_BULL_SL;
            }
        }else if (InstTypeEnum.AU.toName().equals(userSltpRecord.getContract())){
            if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AUTD_BEAR_TP;
            }else if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AUTD_BEAR_SL;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AUTD_BULL_TP;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_AUTD_BULL_SL;
            }
        }else if (InstTypeEnum.MAU.toName().equals(userSltpRecord.getContract())){
            if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_MAUTD_BEAR_TP;
            }else if (UserSltpRecord.SltpType.BEAR.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_MAUTD_BEAR_SL;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.TP.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_MAUTD_BULL_TP;
            }else if (UserSltpRecord.SltpType.BULL.toType()==userSltpRecord.getBearBull() && UserSltpRecord.SltpType.SL.toType()==userSltpRecord.getSltpType()){
                zkey = Constant.ZKEY_MAUTD_BULL_SL;
            }
        }
        return zkey;
    }

    /**
     * 根据用户止盈止损记录生成关键点记录
     * @param userSltpRecord
     * @return
     */
    public static KeyPointOfSltpRecord generateZsetValueByDetailRecord(UserSltpRecord userSltpRecord) {
        KeyPointOfSltpRecord keyPointOfSltpRecord = new KeyPointOfSltpRecord();
        keyPointOfSltpRecord.setUuid(userSltpRecord.getUuid());
        keyPointOfSltpRecord.setUserId(userSltpRecord.getUserId());
        return keyPointOfSltpRecord;
    }
}
