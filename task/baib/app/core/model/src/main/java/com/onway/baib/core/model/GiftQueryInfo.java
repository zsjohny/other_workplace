package com.onway.baib.core.model;


/**
 * 优惠卷查询info
 * 
 * @author jiaming.zhu
 * @version $Id: GiftQueryInfo.java, v 0.1 2017年2月18日 下午1:52:38 ZJM Exp $
 */
public class GiftQueryInfo {
    //优惠卷总量
    private int    totalNum;
    //剩余优惠卷
    private int    alUseNum;
    //截止时间
    private String limitDate;
    //满减
    private String rule;
    //标题
    private String title;
    //说明
    private String des;
    //金额
    private String amount;
    //优惠卷Id
    private String giftId;

    public String getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(String limitDate) {
        this.limitDate = limitDate;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getAlUseNum() {
        return alUseNum;
    }

    public void setAlUseNum(int alUseNum) {
        this.alUseNum = alUseNum;
    }

}
