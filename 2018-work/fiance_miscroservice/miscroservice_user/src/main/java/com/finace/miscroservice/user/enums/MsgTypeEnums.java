package com.finace.miscroservice.user.enums;

public enum  MsgTypeEnums {
//    mobileModifyPlus
//passwordResetPlus
//autoBidAuthPlus
//autoCreditInvestAuthPlus
    MOBILE_MODIFY_PLUS("电子账户手机号修改增强","mobileModifyPlus"),
    PASSWORD_RESET_PLUS("密码重置增强","passwordResetPlus"),
    AUTO_BID_AUTH_PLUS("自动投标签约","autoBidAuthPlus"),
    AUTO_CREDIT_INVEST_AUTH_PLUS("投资人自动债权转让签约增强","autoCreditInvestAuthPlus");

    private String value;
    private String code;

    MsgTypeEnums(String value, String code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
