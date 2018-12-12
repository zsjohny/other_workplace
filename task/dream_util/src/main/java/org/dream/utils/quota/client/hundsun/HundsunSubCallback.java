package org.dream.utils.quota.client.hundsun;

import com.hundsun.mcapi.interfaces.ISubCallback;
import com.hundsun.mcapi.subscribe.MCSubscribeParameter;
import com.hundsun.t2sdk.interfaces.share.dataset.IDataset;
import com.hundsun.t2sdk.interfaces.share.event.IEvent;
import org.apache.commons.lang3.StringUtils;
import org.dream.model.quota.Quota;
import org.dream.utils.log.HundsunLog;
import org.dream.utils.math.Arith;
import org.dream.utils.quota.helper.QuotaCacheHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.concurrent.Executor;

import static org.dream.utils.quota.SimpleDateFormatUtil.DATE_FORMAT2;
import static org.dream.utils.quota.SimpleDateFormatUtil.DATE_FORMAT4;

/**
 * client = hundsun
 * 用于编写恒生的回调数据
 * Created by yhj on 16/11/9.
 */
public class HundsunSubCallback implements ISubCallback {

    private static final Logger LOG = LoggerFactory.getLogger(HundsunSubCallback.class);


    @Override
    public void OnReceived(String topicName, IEvent event) {

        HundsunDataHelper.flushLastUpdateTime();

        IDataset data = event.getEventDatas().getDataset(0);

        String future_code = data.getString("future_code");
        String product_id = data.getString("product_id");

        String instrumentID = product_id + future_code;

        HundsunLog.log(data);

        if (HundsunDataHelper.containContractCode(instrumentID)) {
            final Quota quota = new Quota();
            quota.setInstrumentId(instrumentID);

            quota.setExchangeId(data.getString("exchange_type"));

            quota.setAskPrice(data.getDouble("ask_price1"));
            quota.setAskVolume(data.getInt("ask_volume1"));
            quota.setBidPrice(data.getDouble("bid_price1"));
            quota.setBidVolume(data.getInt("bid_volume1"));
            quota.setLastPrice(data.getDouble("last_price"));
            quota.setPreSetPrice(data.getDouble("pre_settle_price"));
            quota.setPreClsPrice(data.getDouble("prev_price"));
            quota.setOpenPrice(data.getDouble("open_price"));
            quota.setHighestPrice(data.getDouble("high_price"));
            quota.setLowestPrice(data.getDouble("low_price"));
            quota.setTurnover(data.getDouble("business_balance"));

            quota.setSettlePrice(Double.isInfinite(data.getDouble("settle_price")) ? 0 : data.getDouble("settle_price"));
            quota.setUpLimitPrice(Double.isInfinite(data.getDouble("up_price")) ? 0 : data.getDouble("up_price"));
            quota.setDownLimitPrice(Double.isInfinite(data.getDouble("down_price")) ? 0 : data.getDouble("down_price"));

            quota.setUpDropPrice(Arith.subtract(6, quota.getLastPrice(), quota.getPreSetPrice()));
            quota.setUpDropSpeed(Arith.divides(10, Arith.subtract(6, quota.getLastPrice(), quota.getPreSetPrice()), quota.getPreSetPrice()));

            quota.setPositionVolume(data.getLong("position"));
            quota.setOpenInterest(data.getDouble("position"));
            quota.setVolume((double) (data.getLong("business_amount")));

            try {
                //使用转化前的合约代码更新定时器
//                updateTime(data.InstrumentID);
                quota.setUpTime(DATE_FORMAT4.parse(data.getString("date_time")).getTime());
                quota.setUpTimeFormat(data.getString("date_time"));
                quota.setTradeDay(DATE_FORMAT2.format(DATE_FORMAT4.parse(data.getString("date_time"))));

            } catch (ParseException e) {
                LOG.error(" parse date error ", e);
            }

            //FIXME 这几个没找到
            quota.setPrePositionVolume(0L);
            quota.setPreOpenInterest(0.0);

            Executor executor = HundsunDataHelper.getWorkThread(quota.getInstrumentId());

            if(executor!=null){
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        String instrumentId = QuotaCacheHelper.quotaCode2VarietyCode.get(quota.getInstrumentId());
                        if (StringUtils.isEmpty(instrumentId)) {
                            LOG.debug("行情数据转化instrumentId{} 为空,进行过滤{}", quota.getInstrumentId(), QuotaCacheHelper.quotaCode2VarietyCode);
                            return;
                        }
                        quota.setInstrumentId(instrumentId);

//                        if (quota.getAskPrice().equals(0) || quota.getAskVolume().equals(0) || quota.getBidPrice().equals(0) || quota.getBidVolume().equals(0)) {
//                            LOG.debug("买一或者卖一的价格或者量值,没有进行过滤{}", quota);
//                            return;
//                        }

                        HundsunDataHelper.getQuotaDataHandleService().quotaHandle(quota);
                    }
                });
            }

        }


    }


    @Override
    public void OnRecvTickMsg(MCSubscribeParameter mcSubscribeParameter, String s) {

    }



}
