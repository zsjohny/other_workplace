package com.finace.miscroservice.commons.enums;

public enum AccountLogTypeEnums {
    TENDER("投资成功","tender"),//  tender --投资成功
    TENDER_ERROR("投资失败","tender_error"),// tender_error  --投资失败
    UNFREEZE("还款成功","unfreeze"),// unfreeze-还款成功
    UNFREEZE_ERROR("还款失败","unfreeze_error"),// unfreeze_error还款失败
    CASH_SUCCESS("提现成功","cash_success"),// cash_success提现成功
    CASH_FAIL("提现失败","cash_fail"),// cash_fail提现失败
    CASH("正在提现","cash"),//
    RECHARGE("正在充值","recharge"),// 充值中
    RECHARGE_FAIL("充值失败","recharge_fail"),// 充值失败
    RECHARGE_SUCCESS("充值成功","recharge_success"),// recharge_success--充值成功
    RECHARGE_REMARK("&s,充值金额为%s","recharge_remark"),
    CASH_REMARK("%s,提现金额为%s","cash_remark"),
    ;

    private String value;
    private String code;

    AccountLogTypeEnums(String value, String code) {
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
