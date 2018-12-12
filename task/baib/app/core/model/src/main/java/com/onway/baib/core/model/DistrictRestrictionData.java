/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model;

import java.io.Serializable;

/**
 * 银行卡对省、市、支行的限制信息
 */
public class DistrictRestrictionData implements Serializable {

    private static final long serialVersionUID = -8505593275367809567L;

    //银行编码
    private String            bankCode;

    //银行图标地址
    private String            bankIconUrl;

    //银行名称
    private String            bankName;

    //是否显示省份标识
    private String            needProvince;

    //是否显示城市标识
    private String            needCity;

    //是否显示支行标识
    private String            needSubBank;

    // 提示消息
    private String            tipMsg;

    public String getTipMsg() {
        return tipMsg;
    }

    public void setTipMsg(String tipMsg) {
        this.tipMsg = tipMsg;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankIconUrl() {
        return bankIconUrl;
    }

    public void setBankIconUrl(String bankIconUrl) {
        this.bankIconUrl = bankIconUrl;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNeedProvince() {
        return needProvince;
    }

    public void setNeedProvince(String needProvince) {
        this.needProvince = needProvince;
    }

    public String getNeedCity() {
        return needCity;
    }

    public void setNeedCity(String needCity) {
        this.needCity = needCity;
    }

    public String getNeedSubBank() {
        return needSubBank;
    }

    public void setNeedSubBank(String needSubBank) {
        this.needSubBank = needSubBank;
    }

}
