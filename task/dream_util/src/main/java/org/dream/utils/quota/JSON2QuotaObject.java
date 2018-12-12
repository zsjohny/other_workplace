package org.dream.utils.quota;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.dream.model.quota.Quota;
import org.dream.model.quota.SimpleQuota;
import org.dream.utils.math.Arith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 将JSON数据转换为行情对象
 *
 * @author wangd
 */
public class JSON2QuotaObject {
	 private static Logger logger = LoggerFactory.getLogger(JSON2QuotaObject.class);

    /**
     * 自定义格式化时间
     */
    private static ThreadLocal<SimpleDateFormat> simpleDateLocal = new ThreadLocal<SimpleDateFormat>(


    ) {
        @Override
        protected SimpleDateFormat initialValue() {


            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        }
    };

    /**
     * 行情JSON字符串转换成简单行情对象{@link SimpleQuota}
     * <p>
     * <strong>
     * 行情对象中所有Double类型的数据都会有可能是正的无穷大，那样的数据是无意义的，但是行情对象中是不能不保存此类数据，在调用此方法时在操作行情中Double类型的数据之前需要对该数据进行验证
     * Double.isInfinite()
     * </strong>
     *
     * @param quotaJason
     * @param
     * @return
     * @throws ParseException
     */
    public static SimpleQuota json2SimpleQuota(String quotaJason) throws ParseException {

        JSONObject jsonObject = JSON.parseObject(quotaJason);
        return map2SimpleQuota(jsonObject);
    }

    /**
     * 将行情的JSON字符串转换成行情对象{@link Quota}
     * <strong>
     * 行情对象中所有Double类型的数据都会有可能是正的无穷大，那样的数据是无意义的，但是行情对象中是不能不保存此类数据，
     * 在调用此方法时在操作行情中Double类型的数据之前需要对该数据进行验证
     * Double.isInfinite()
     * </strong>
     *
     * @param quotaJason
     * @param
     * @return
     * @throws ParseException
     */
    public static Quota json2Quota(String quotaJason) throws ParseException {

        JSONObject jsonObject = JSON.parseObject(quotaJason);
        return map2Quota(jsonObject);
    }

    private static Quota map2Quota(JSONObject jsonObject) throws ParseException {
        if (jsonObject == null) {
            return null;
        }

        if (jsonObject.get("InstrumentID") != null) {
            return map2Quota_domestic(jsonObject);
        } else {
            return map2Quota_international(jsonObject);
        }
    }

    /**
     * 国际行情数据JSON转为行情对象{@link Quota}
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    private static Quota map2Quota_international(JSONObject jsonObject) throws ParseException {
        Quota quota = new Quota();

        String instrumentId = jsonObject.getString("CommodityNo") + (jsonObject.getString("ContractNo1") != null ? jsonObject.getString("ContractNo1") : jsonObject.getString("ContractNo1"));
        quota.setInstrumentId(instrumentId);
        quota.setExchangeId(jsonObject.getString("ExchangeNo"));

        Double askPrice = jsonObject.getDouble("QAskPrice");
        quota.setAskPrice(askPrice);

        Double bidPrice = jsonObject.getDouble("QBidPrice");
        quota.setBidPrice(bidPrice);

        Double lastPrice = jsonObject.getDouble("QLastPrice");
        quota.setLastPrice(lastPrice);

        quota.setAskVolume(jsonObject.getInteger("QAskQty"));
        quota.setBidVolume(jsonObject.getInteger("QBidQty"));

        String upTime = jsonObject.getString("DateTimeStamp").toString();
        Long upDate = getDate2Long(upTime, "yyyy-MM-dd HH:mm:ss.SSS");
        Integer updateMillisec = jsonObject.getInteger("UpdateMillisec");
        if (updateMillisec != null && updateMillisec < 1000) {
            upDate = upDate + updateMillisec;
        }

        quota.setUpTime(upDate);
        //国际行情交易日在行情时间中截取
        quota.setTradeDay(upTime.substring(0, (upTime.lastIndexOf("-") + 2)));

        quota.setUpLimitPrice(jsonObject.getDouble("QLimitUpPrice"));
        quota.setDownLimitPrice(jsonObject.getDouble("QLimitDownPrice"));
        quota.setHighestPrice(jsonObject.getDouble("QHighPrice"));
        quota.setLowestPrice(jsonObject.getDouble("QLowPrice"));
        quota.setUpDropPrice(jsonObject.getDouble("QChangeValue"));
        quota.setUpDropSpeed(jsonObject.getDouble("QChangeSpeed"));
        quota.setPreSetPrice(jsonObject.getDouble("QPreSettlePrice"));
        quota.setPreClsPrice(jsonObject.getDouble("QPreClosingPrice"));
        quota.setVolume(jsonObject.getDouble("QTotalValue"));//保存当日成交量
        quota.setTurnover(jsonObject.getDouble("QTotalTurnover"));
        quota.setOpenInterest(jsonObject.getDouble("QPositionQty"));//国际期货数据持仓量只有一个，暂且认为是今日持仓量
//	quota.setPreOpenInterest(jsonObject.getDouble("QPositionQty"));
        quota.setOpenPrice(jsonObject.getDouble("QOpeningPrice"));
        quota.setSettlePrice(jsonObject.getDouble("QSettlePrice"));

        return quota;
    }

    /**
     * 国内行情数据JSON转为行情对象{@link Quota}
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    private static Quota map2Quota_domestic(JSONObject jsonObject) throws ParseException {
        Quota quota = new Quota();

        quota.setExchangeId(jsonObject.getString("ExchangeNo"));
        quota.setInstrumentId(jsonObject.getString("InstrumentID"));

        Double askPrice = jsonObject.getDouble("AskPrice1");
        quota.setAskPrice(askPrice);

        Double bidPrice = jsonObject.getDouble("BidPrice1");
        quota.setBidPrice(bidPrice);

        Double lastPrice = jsonObject.getDouble("LastPrice");
        quota.setLastPrice(lastPrice);

        quota.setAskVolume(jsonObject.getInteger("AskVolume1"));
        quota.setBidVolume(jsonObject.getInteger("BidVolume1"));

        String upTime = jsonObject.getString("ActionDay") + " " + jsonObject.getString("UpdateTime");
        Long upDate = getDate2Long(upTime, "yyyyMMdd HH:mm:ss");
        Integer updateMillisec = jsonObject.getInteger("UpdateMillisec");
        if (updateMillisec != null && updateMillisec < 1000) {
            upDate = upDate + updateMillisec;
        }

        quota.setUpTime(upDate);
        quota.setTradeDay(jsonObject.getString("TradingDay"));

//	quota.setUpLimitPrice(jsonObject.getDouble("")); 国内行情数据好像没有这个价格啊，但是行算不出来的价格啊
        quota.setDownLimitPrice(jsonObject.getDouble("LowerLimitPrice"));
        quota.setHighestPrice(jsonObject.getDouble("HighestPrice"));
        quota.setLowestPrice(jsonObject.getDouble("LowestPrice"));
        Double upDropPrice = jsonObject.getDouble("LastPrice") - jsonObject.getDouble("PreSettlementPrice");
        quota.setUpDropPrice(upDropPrice);
        Double upDropSpeed = upDropPrice / jsonObject.getDouble("PreSettlementPrice");
        quota.setUpDropSpeed(upDropSpeed);
        quota.setPreSetPrice(jsonObject.getDouble("PreSettlementPrice"));
        quota.setPreClsPrice(jsonObject.getDouble("PreClosePrice"));
        quota.setVolume(jsonObject.getDouble("Volume"));//保存当日成交量
        quota.setTurnover(jsonObject.getDouble("Turnover"));
        quota.setOpenInterest(jsonObject.getDouble("OpenInterest"));
        quota.setPreOpenInterest(jsonObject.getDouble("PreOpenInterest"));
        quota.setOpenPrice(jsonObject.getDouble("OpenPrice"));
        quota.setSettlePrice(jsonObject.getDouble("SettlementPrice"));//多内结算价为本次结算价


        return quota;
    }


    /**
     * 将由JSON转换成的map转换成{@link SimpleQuota}
     * <strong>所有涉及价格时，传递时可能会是一个正的无穷大的Double数据，当此情况出现时该值会是0</strong>
     *
     * @param jsonObject
     * @return
     * @throws ParseException
     */
    public static SimpleQuota map2SimpleQuota(JSONObject jsonObject) throws ParseException {

        return map2SimpleQuota(map2Quota(jsonObject));
    }

    /**
     * 获得SimpleQuota
     *
     * @param quota
     * @return
     * @throws ParseException
     */
    public static SimpleQuota map2SimpleQuota(Quota quota) throws ParseException {

        SimpleQuota simpleQuota = new SimpleQuota();
        BeanUtils.copyProperties(quota, simpleQuota);

        return simpleQuota;
    }

    /**
     * 根据时间字符串和，解析模板获得时间的Long值
     *
     * @param dateStr
     * @param parsePatterns
     * @return
     * @throws ParseException
     */
    private static Long getDate2Long(String dateStr, String parsePatterns) throws ParseException {
        Date date = DateUtils.parseDate(dateStr, parsePatterns);

        return date.getTime();
    }


    /**
     * 根据国内的C++行情数据 转换为行情对象
     *
     * @param json 国内的行情数据
     * @return
     */
    public static Quota getQuota2InterJson(JSONObject json) {

        Quota quota = null;

        if (json == null) {
            return quota;

        }
        try {
            JSONObject jsonObject = json.getJSONObject("MsgData");
            quota = new Quota();

            quota.setExchangeId(jsonObject.getString("ExchangeNo"));
            String instrumentId = jsonObject.getString("CommodityNo") + (jsonObject.getString("ContractNo1") != null ? jsonObject.getString("ContractNo1") : jsonObject.getString("ContractNo2"));
            quota.setInstrumentId(instrumentId);
            Double askPrice = jsonObject.getDouble("AskPrice1");
            quota.setAskPrice(askPrice);
            Double bidPrice = jsonObject.getDouble("BidPrice1");
            quota.setBidPrice(bidPrice);


            Double lastPrice = jsonObject.getDouble("LastPrice");
            quota.setLastPrice(lastPrice);

            quota.setAskVolume(jsonObject.getInteger("AskVolume1"));
            quota.setBidVolume(jsonObject.getInteger("BidVolume1"));


            quota.setUpTime(simpleDateLocal.get().parse(jsonObject.getString("DateTimeStamp")).getTime());
            quota.setUpTimeFormat( jsonObject.getString("DateTimeStamp"));

            //国际行情交易日在行情时间中截取
            quota.setTradeDay(jsonObject.getString("DateTimeStamp").split(" ")[0]);

            quota.setHighestPrice(jsonObject.getDouble("HighPrice"));

            quota.setLowestPrice(jsonObject.getDouble("LowPrice"));


            //暂定
            quota.setUpDropPrice(Arith.subtract(6, jsonObject.getDouble("LastPrice"), jsonObject.getDouble("PreSettlePrice")));
            // 涨跌停 保留6位小数
            quota.setUpDropSpeed(Arith.divides(10, Arith.subtract(6, jsonObject.getDouble("LastPrice"), jsonObject.getDouble("PreSettlePrice")), jsonObject.getDouble("PreSettlePrice")));


            quota.setPreSetPrice(jsonObject.getDouble("PreSettlePrice"));

            quota.setPreClsPrice(jsonObject.getDouble("PreClosingPrice"));

            quota.setPositionVolume(jsonObject.getLong("PositionVolume"));//今日持仓量
            quota.setPrePositionVolume(jsonObject.getLong("PrePositionVolume"));//昨日持仓量


         /*       quota.setTurnover(jsonObject.getDouble("QTotalTurnover"));*/

//            quota.setOpenInterest(jsonObject.getDouble("QPositionQty"));//国际期货数据持仓量只有一个，暂且认为是今日持仓量
//	quota.setPreOpenInterest(jsonObject.getDouble("QPositionQty"));

            Double limitUpPrice = jsonObject.getDouble("LimitUpPrice");
            Double limitDownPrice = jsonObject.getDouble("LimitDownPrice");

            quota.setUpLimitPrice( ( (limitUpPrice == null || limitUpPrice < 0  || limitUpPrice.isInfinite()) ? 0 : limitUpPrice ) ); // 涨停板
            quota.setDownLimitPrice( (limitDownPrice == null || limitDownPrice < 0 || limitDownPrice.isInfinite()) ? 0 : limitDownPrice ); // 跌停板


            quota.setOpenPrice(jsonObject.getDouble("OpeningPrice"));
            quota.setSettlePrice(jsonObject.getDouble("ClosingPrice"));
            quota.setVolume(0.0);

        } catch (Exception e) {
        	logger.error("行情转化异常", e);
            return null;


        }


        return quota;
    }


}
