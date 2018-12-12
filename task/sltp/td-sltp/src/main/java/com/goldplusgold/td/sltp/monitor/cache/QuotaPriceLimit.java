package com.goldplusgold.td.sltp.monitor.cache;

import com.goldplusgold.td.sltp.monitor.conf.Constant;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 行情当日涨跌停板
 * Created by Administrator on 2017/5/16.
 */
@Component
public class QuotaPriceLimit {
    private static final String HIGH = "HIGH";
    private static final String LOW = "LOW";

    private static final String AUTD_HIGH = Constant.CONTRACT_NAME_AU + Constant.HYPHEN + HIGH;
    private static final String AUTD_LOW = Constant.CONTRACT_NAME_AU + Constant.HYPHEN + LOW;
    private static final String AGTD_HIGH = Constant.CONTRACT_NAME_AG + Constant.HYPHEN + HIGH;
    private static final String AGTD_LOW = Constant.CONTRACT_NAME_AG + Constant.HYPHEN + LOW;
    private static final String MAUTD_HIGH = Constant.CONTRACT_NAME_MAU + Constant.HYPHEN + HIGH;
    private static final String MAUTD_LOW = Constant.CONTRACT_NAME_MAU + Constant.HYPHEN + LOW;

    private Map<String, String> limitsMap = new HashMap<>();

    public void setAuTDHighLimit(String highLimit){
        if (limitsMap.get(AUTD_HIGH) == null){
            limitsMap.put(AUTD_HIGH, highLimit);
        }
    }

    public void setAuTDLowLimit(String lowLimit){
        if (limitsMap.get(AUTD_LOW) == null){
            limitsMap.put(AUTD_LOW, lowLimit);
        }
    }

    public void setAgTDHigh(String highLimit){
        if(limitsMap.get(AGTD_HIGH) == null){
            limitsMap.put(AGTD_HIGH, highLimit);
        }
    }

    public void setAgTDLow(String lowLimit){
        if (limitsMap.get(AGTD_HIGH) == null){
            limitsMap.put(AGTD_HIGH, lowLimit);
        }
    }

    public void setMAuTDHigh(String highLimit){
        if (limitsMap.get(MAUTD_HIGH) == null){
            limitsMap.put(MAUTD_HIGH, highLimit);
        }
    }

    public void setMAuTDLow(String lowLimit){
        if (limitsMap.get(MAUTD_LOW) == null){
            limitsMap.put(MAUTD_LOW, lowLimit);
        }
    }

    public void setHigh(String contractName, String highLimit){
        String key = contractName + Constant.HYPHEN + HIGH;
        if (limitsMap.get(key) == null){
            limitsMap.put(key, highLimit);
        }
    }

    public void setLow(String contractName, String lowLimit){
        String key = contractName + Constant.HYPHEN + LOW;
        if (limitsMap.get(key) == null){
            limitsMap.put(key, lowLimit);
        }
    }

    public String getAuTDHigh(){
        return limitsMap.get(AUTD_HIGH);
    }

    public String getAuTDLow(){
        return limitsMap.get(AUTD_LOW);
    }

    public String getAgTDHigh(){
        return limitsMap.get(AGTD_HIGH);
    }

    public String getAgTDLow(){
        return limitsMap.get(AGTD_LOW);
    }

    public String getMAuTDHigh(){
        return limitsMap.get(MAUTD_HIGH);
    }

    public String getMAuTDLow(){
        return limitsMap.get(MAUTD_LOW);
    }

    public String getHigh(String contractName){
        String key = contractName + Constant.HYPHEN + HIGH;
        return limitsMap.get(key);
    }

    public String getLow(String contractName){
        String key = contractName + Constant.HYPHEN + LOW;
        return limitsMap.get(key);
    }

    public void clearCache(){
        limitsMap.clear();
    }

}
