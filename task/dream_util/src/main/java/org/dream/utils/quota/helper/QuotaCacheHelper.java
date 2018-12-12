package org.dream.utils.quota.helper;

import com.google.common.collect.Maps;
import org.dream.model.quota.CandlestickCharts;
import org.dream.model.quota.Quota;
import org.dream.model.quota.SimpleQuota;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 用于保存一些数据中得缓存.
 * 使用的请要注意
 * Created by yhj on 16/10/27.
 */
public final class QuotaCacheHelper {

    public static String CLIENT_XTRADER = "xtrader";
    public static String CLIENT_HUNDSUN = "hundsun";
    public static String CLIENT_CTP = "ctp";


    public static Integer MAX_QUOTA = 10;
    /**
     * 前端的10条最新的数据map
     */
    public static ConcurrentMap<String, Quota[]> quotaDataNewMap = new ConcurrentHashMap<>();

    /**
     * 用于保存当前在线的合约的状态
     */
    public static ConcurrentMap<String, Integer> quotaContractOnlineMap = new ConcurrentHashMap<>();

    /**
     * 所有对应的代码转化关系
     */
    public static HashMap<String,String> quotaCode2VarietyCode = new HashMap<>();

    /**
     * 保存 K线的 每分钟的
     */
    public static Map<String ,Map<String ,CandlestickCharts>> candlestickDatas  = Maps.newHashMap();


    /**
     * 清理合约数据
     * @param varietyType
     * @param contractsCode
     */
    public static void clearCache(String varietyType,String contractsCode){
        quotaDataNewMap.remove(contractsCode);
        QuotaCacheHelper.quotaContractOnlineMap.remove(contractsCode);
 
		candlestickDatas.remove(contractsCode);
 
//        QuotaCacheHelper.newestQuota.remove(varietyType);
//        QuotaCacheHelper.newestQuota.remove(varietyType);
    }


    /**
     * 将quota 数据纺织到内存里面
     * @param quota
     */
    public static void putLastQuota(Quota quota){
        SimpleQuota simpleQuota = new SimpleQuota();
        BeanUtils.copyProperties(quota, simpleQuota);
        // 保存最新行情到缓存，方便调用
        SimpleQuota.newestQuota.put(quota.getInstrumentId() + SimpleQuota.SIMPLE_QUOTA_SUFFIX, simpleQuota);
        SimpleQuota.newestQuota.put(quota.getInstrumentId(), quota);
    }
}
