package com.goldplusgold.mq.msgs;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 交易和止盈止损的对接类
 * Created by Ness on 2017/5/17.
 */
public class Trade2Sltp implements Serializable {

    private static final long serialVersionUID = -1931722302166063737L;
    /**
     * 交易方式 平多  平空
     */
    private String tradeType;


    /**
     * 合约代码
     */
    private String prodCode;


    /**
     * 委托价格
     */
    private Double price;


    /**
     * 委托手数
     */
    private Integer volume;


    /**
     * 用户Id
     */
    private String userId;


    /**
     * 止盈止损订单的Id
     */
    private String sltpId;


    /**
     * 交易结果
     */
    private Integer result;


    /**
     * 多头或者空头 0 空头 1 多头
     */
    @JsonIgnore
    private Integer bearBull;


    /**
     * 说明：交易类型（exch_type）
     */
    private enum TradeTypeEnum {

        TD_CLOSE_LONG("4043", "平多", 1),
        TD_CLOSE_SHORT("4044", "平空", 0);

        /**
         * 常量编码
         */
        String code;

        /**
         * 常量名称
         */
        String name;

        private int value;

        TradeTypeEnum(String code, String name, int value) {
            this.code = code;
            this.name = name;
            this.value = value;
        }

        public static String toTradeType(Integer bearBull) {
            if (bearBull == null) {
                return "";
            }
            return Arrays.stream(TradeTypeEnum.values()).filter(x -> bearBull.equals(x.value)).findFirst().get().code;
        }


    }

    /**
     * 委托类型
     */
    private enum commissType {

        COMMISS_SUCCESS(0), COMMISS_ERROR(1);

        private int type;

        commissType(int type) {
            this.type = type;
        }

        public int toType() {
            return type;
        }
    }


    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(Integer bearBull) {
        this.tradeType = TradeTypeEnum.toTradeType(bearBull);
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSltpId() {
        return sltpId;
    }

    public void setSltpId(String sltpId) {
        this.sltpId = sltpId;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }
}
