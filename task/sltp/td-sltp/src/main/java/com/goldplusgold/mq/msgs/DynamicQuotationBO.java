package com.goldplusgold.mq.msgs;

/**
 * 动态行情的BO对象
 * Created by Administrator on 2017/5/19.
 */
public class DynamicQuotationBO {
    /**
     * 时间
     */
    private String time;

    /**
     * 开盘价
     */
    private String openPrice;

    /**
     * 涨跌额
     */
    private String upDown;

    /**
     * 涨跌幅度
     */
    private String upDownRate;

    /**
     * 涨停板
     */
    private String highestPrice;


    /**
     * 跌停板
     */
    private String lowestPrice;

    /**
     * 最新价
     */
    private String lastPrice;

    /**
     * 昨收盘
     */
    private String lastClose;

    /**
     * 昨结算
     */
    private String lastSettle;

    /**
     * 总量（成交量）
     */
    private String volume;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getUpDownRate() {
        return upDownRate;
    }

    public void setUpDownRate(String upDownRate) {
        this.upDownRate = upDownRate;
    }

    public String getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }

    public String getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(String lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastClose() {
        return lastClose;
    }

    public void setLastClose(String lastClose) {
        this.lastClose = lastClose;
    }

    public String getLastSettle() {
        return lastSettle;
    }

    public void setLastSettle(String lastSettle) {
        this.lastSettle = lastSettle;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "DynamicQuotationBO{" +
                "time='" + time + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", upDown='" + upDown + '\'' +
                ", upDownRate='" + upDownRate + '\'' +
                ", highestPrice='" + highestPrice + '\'' +
                ", lowestPrice='" + lowestPrice + '\'' +
                ", lastPrice='" + lastPrice + '\'' +
                ", lastClose='" + lastClose + '\'' +
                ", lastSettle='" + lastSettle + '\'' +
                ", volume='" + volume + '\'' +
                '}';
    }
}
