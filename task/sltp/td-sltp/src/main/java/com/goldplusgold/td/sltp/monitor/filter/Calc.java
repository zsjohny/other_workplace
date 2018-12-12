package com.goldplusgold.td.sltp.monitor.filter;

import com.goldplusgold.mq.msgs.DynamicQuotationBOWrapper;
import com.goldplusgold.td.sltp.core.vo.UserSltpRecord;
import com.goldplusgold.td.sltp.monitor.cache.QuotaPriceLimit;
import com.goldplusgold.td.sltp.monitor.comparator.SlPriceComparator;
import com.goldplusgold.td.sltp.monitor.comparator.TpPriceComparator;
import com.goldplusgold.td.sltp.monitor.conf.Constant;
import com.goldplusgold.td.sltp.monitor.model.AvailableLots;
import com.goldplusgold.td.sltp.monitor.model.KeyPointOfSltpRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 止盈止损筛选核心算法
 * Created by Administrator on 2017/5/15.
 */
@Component
public class Calc {
    Logger logger = LoggerFactory.getLogger(Calc.class);

    @Autowired
    private QuotaPriceLimit quotaPriceLimit;

    @Autowired
    private SlPriceComparator slPriceComparator;
    @Autowired
    private TpPriceComparator tpPriceComparator;
    private Comparator<UserSltpRecord> slPriceComparator_reverse = Collections.reverseOrder(slPriceComparator);
    private Comparator<UserSltpRecord> tpPriceComparator_reverse = Collections.reverseOrder(tpPriceComparator);

    /**
     * 某个合约下, 根据空/多仓分组, 根据用户ID去重
     * @param keyRecordsMap
     * @return 处理后的数据
     */
    public Map<String, Set<String>> groupAndRemDuplicate(Map<String, Set<KeyPointOfSltpRecord>> keyRecordsMap){
        Map<String, Set<String>> result = new HashMap<>();
        for (Map.Entry<String, Set<KeyPointOfSltpRecord>> entry : keyRecordsMap.entrySet()){
            if (entry.getKey().contains(Constant.ZKEY_AUTD_BULL_MODE)){
                result.put(Constant.ZKEY_AUTD_BULL_MODE, remDuplicate(entry.getValue()));
            }else if (entry.getKey().contains(Constant.ZKEY_AUTD_BEAR_MODE)){
                result.put(Constant.ZKEY_AUTD_BEAR_MODE, remDuplicate(entry.getValue()));
            }else if (entry.getKey().contains(Constant.ZKEY_MAUTD_BULL_MODE)){
                result.put(Constant.ZKEY_MAUTD_BULL_MODE, remDuplicate(entry.getValue()));
            }else if (entry.getKey().contains(Constant.ZKEY_MAUTD_BEAR_MODE)){
                result.put(Constant.ZKEY_MAUTD_BEAR_MODE, remDuplicate(entry.getValue()));
            }else if (entry.getKey().contains(Constant.ZKEY_AGTD_BULL_MODE)){
                result.put(Constant.ZKEY_AGTD_BULL_MODE, remDuplicate(entry.getValue()));
            }else if (entry.getKey().contains(Constant.ZKEY_AGTD_BEAR_MODE)){
                result.put(Constant.ZKEY_AGTD_BEAR_MODE, remDuplicate(entry.getValue()));
            }
        }
        return result;
    }

    /**
     * 筛选某个用户下, 多仓或者空仓, 符合触发条件的止盈止损记录, 并计算委托价格
     * @param detailRecords
     * @param availableLots 用户可用手数, 触发后更新
     * @param quota 最新行情
     * @return 满足触发条件的止盈止损记录
     */
    public List<UserSltpRecord> filterTriggeredRecords(List<UserSltpRecord> detailRecords, AvailableLots availableLots, DynamicQuotationBOWrapper quota){
        Double lastPrice = new Double(quota.getBo().getLastPrice());
        List<UserSltpRecord> result = new LinkedList<>();

        //根据多仓/空仓, 止盈/止损分组, 并按照止盈价/止损价排序
        Map<String, Set<UserSltpRecord>> groupedRecords = groupAndSort(detailRecords);

        //筛选符合触发条件的记录
        for (Map.Entry<String, Set<UserSltpRecord>> entry : groupedRecords.entrySet()){
            if (Constant.COM_BEAR_TP.equals(entry.getKey())){
                for (UserSltpRecord record : entry.getValue()){
                    if (record.getTpPrice() >= lastPrice && availableLots.getBearLots()!=null && availableLots.getBearLots()>0){
                        if (record.getLots() <= availableLots.getBearLots()){
                            Double commissionPrice = lastPrice + record.getFloatPrice();
                            record.setCommissionPrice(commissionPrice);
                            result.add(record);
                            availableLots.setBearLots(availableLots.getBearLots() - record.getLots());
                        }
                    }else {
                        break;
                    }
                }
            }else if (Constant.COM_BEAR_SL.equals(entry.getKey())){
                for (UserSltpRecord record : entry.getValue()){
                    if (record.getSlPrice() <= lastPrice && availableLots.getBearLots()!=null && availableLots.getBearLots()>0){
                        if (record.getLots() <= availableLots.getBearLots()){
                            Double highLimit = new Double(quotaPriceLimit.getHigh(quota.getInstType()));
                            Double commissionPrice = lastPrice + record.getSlPrice();
                            commissionPrice = commissionPrice > highLimit ? highLimit : commissionPrice;
                            record.setCommissionPrice(commissionPrice);
                            result.add(record);
                            availableLots.setBearLots(availableLots.getBearLots() - record.getLots());
                        }
                    }else {
                        break;
                    }
                }
            }else if (Constant.COM_BULL_TP.equals(entry.getKey())){
                for (UserSltpRecord record : entry.getValue()){
                    if (record.getTpPrice()<=lastPrice && availableLots.getBullLots()!=null && availableLots.getBullLots()>0){
                        if (record.getLots() <= availableLots.getBullLots()){
                            Double lowLimit = new Double(quotaPriceLimit.getLow(quota.getInstType()));
                            Double commissionPrice = lastPrice - record.getFloatPrice();
                            commissionPrice = commissionPrice < lowLimit ? lowLimit : commissionPrice;
                            record.setCommissionPrice(commissionPrice);
                            result.add(record);
                            availableLots.setBullLots(availableLots.getBullLots() - record.getLots());
                        }
                    }else {
                        break;
                    }
                }
            }else if (Constant.COM_BULL_SL.equals(entry.getKey())){
                for (UserSltpRecord record : entry.getValue()){
                    if (record.getSlPrice() >= lastPrice && availableLots.getBullLots()!=null && availableLots.getBullLots()>0){
                        if (record.getLots() <= availableLots.getBullLots()){
                            Double commissionPrice = lastPrice - record.getFloatPrice();
                            record.setCommissionPrice(commissionPrice);
                            result.add(record);
                            availableLots.setBullLots(availableLots.getBullLots() - record.getLots());
                        }
                    }else {
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * 筛选某个用户下, 多仓或者空仓, 永远不会被触发的止盈止损记录
     * @param detailRecords
     * @param availableLots 用户可用手数
     * @return uuid列表
     */
    public Set<String> filterRecordsWillNeverTriggered(List<UserSltpRecord> detailRecords, AvailableLots availableLots){
        Set<String> result = new HashSet<>();

        for (UserSltpRecord record : detailRecords){
            if (UserSltpRecord.SltpType.BEAR.toType()==record.getBearBull()){
                if (availableLots.getBearLots()==null || record.getLots()>availableLots.getBearLots()) result.add(record.getUuid());
            }else if (UserSltpRecord.SltpType.BULL.toType()==record.getBearBull()){
                if (availableLots.getBullLots()==null || record.getLots()>availableLots.getBullLots()) result.add(record.getUuid());
            }
        }

        return result;
    }

    private Set<String> remDuplicate(Set<KeyPointOfSltpRecord> records){
        Set<String> result = new HashSet<>();
        for(KeyPointOfSltpRecord record : records){
            result.add(record.getUserId());
        }
        return result;
    }

    private Map<String, Set<UserSltpRecord>> groupAndSort(List<UserSltpRecord> records){
        Map<String, Set<UserSltpRecord>> result = new HashMap<>();
        for (UserSltpRecord record : records){
            if (UserSltpRecord.SltpType.BEAR.toType()==record.getBearBull() && UserSltpRecord.SltpType.TP.toType()==record.getSltpType()){
                putAnyWay(result, Constant.COM_BEAR_TP, record, tpPriceComparator_reverse);
            }else if (UserSltpRecord.SltpType.BEAR.toType()==record.getBearBull() && UserSltpRecord.SltpType.SL.toType()==record.getSltpType()){
                putAnyWay(result, Constant.COM_BEAR_SL, record, slPriceComparator);
            }else if (UserSltpRecord.SltpType.BULL.toType()==record.getBearBull() && UserSltpRecord.SltpType.TP.toType()==record.getSltpType()){
                putAnyWay(result, Constant.COM_BULL_TP, record, tpPriceComparator);
            }else if (UserSltpRecord.SltpType.BULL.toType()==record.getBearBull() && UserSltpRecord.SltpType.SL.toType()==record.getSltpType()){
                putAnyWay(result, Constant.COM_BULL_SL, record, slPriceComparator_reverse);
            }
        }
        return result;
    }

    private void putAnyWay(Map<String, Set<UserSltpRecord>> map, String key, UserSltpRecord record, Comparator<UserSltpRecord> comparator) {
        Set<UserSltpRecord> set = map.get(key);
        if (set == null){
            set = new TreeSet<>(comparator);
            map.put(key, set);
        }
        set.add(record);
    }
}
