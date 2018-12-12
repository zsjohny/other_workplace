package com.finace.miscroservice.commons.enums;

public enum TxCodeEnum {
    SMS_CODE_APPLY("请求发送短信验证码", "smsCodeApply"),
    ACCOUNT_OPEN_PAGE("开户页面", "accountOpenPage"),
    PASSWORD_SET("密码设置", "passwordSet"),
    PASSWORD_RESET_PLUS("密码重置增强", "passwordResetPlus"),
    BIND_CARD_PAGE("绑定银行卡", "bindCardPage"),
    CARD_UN_BIND("解绑银行卡", "cardUnbind"),
    MOBILE_MODIFY_PLUS("电子账户手机号修改增强", "mobileModifyPlus"),
    DIRECT_RECHARGE_PAGE("充值页面", "directRechargePage"),
    OFFLINE_RECHARGE_CALL("线下充值回调", "offlineRechargeCall"),
    DEBT_REGISTER("借款人标的登记", "debtRegister"),
    DEBT_REGISTER_CANCEL("借款人标的撤销", "debtRegisterCancel"),
    BID_APPLY("投资人投标申请", "bidApply"),
    TENDER("资金流水 购买", "tender"),
    BATCH_REPAY("批次还款", "batchRepay"),
    UNFREEZE("资金流水 回款", "unfreeze"),
    WITHDRAW("提现", "withdraw"),
    PAYMENT_AUTH_PAGE("缴费授权", "paymentAuthPage"),
    REPAY_AUTH_PAGE("还款授权", "repayAuthPage"),
    PAYMENT_AUTH_CHANCEL("P2P产品缴费授权解约", "paymentAuthCancel"),
    FUND_TRANS_QUERY("单笔资金类业务交易查询", "fundTransQuery"),

    ACCOUNT_ID_QUERY("按证件号查询电子账号", "accountIdQuery"),
    CARD_BIND_DETAILS_QUERY("绑卡关系查询", "cardBindDetailsQuery"),
    BALANCE_QUERY("电子账户余额查询", "balanceQuery"),
    FORGET_PASS_URL("忘记密码链接", "ytjForgotPasswordUrl"),
    FEE_ACCOUNT("手续费账户", "01000");

    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    TxCodeEnum() {
    }

    TxCodeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
