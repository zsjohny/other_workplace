package com.onway.baib.core.model;

import com.onway.platform.common.enums.EnumBase;

/**
 * 基础错误码
 * 
 * @author guangdong.li
 * @version $Id: ErrorCode.java, v 0.1 18 Feb 2016 18:37:32 guangdong.li Exp $
 */
public enum ErrorCode implements EnumBase {

    GRANT_FAILURE("GRANT_FAILURE", "发放失败，请联系客服"),

    CLIENTCREDITLIMIT_NOT_ENOUGH("CLIENTCREDITLIMIT_NOT_ENOUGH", "授信额度不够"),

    CLIENTCREDITLIMIT_RECORD_OVERDUE("CLIENTCREDITLIMIT_RECORD_OVERDUE", "授信过期，请重新授信"),

    CLIENTCREDITLIMIT_RECORD_INVALID("CLIENTCREDITLIMIT_RECORD_INVALID", "授信信息无效"),

    CLIENTCREDITLIMIT_RECORD_NOT_EXIT("CLIENTCREDITLIMIT_RECORD_NOT_EXIT", "授信信息不存在"),

    PAY_MONEY_UN_CORRECT("PAY_MONEY_UN_CORRECT", "支付金额不正确"),

    USER_BANKACCOUNT_EXIT("USER_BANKACCOUNT_EXIT", "用户银行卡账户信息不存在"),

    LOAN_RECORD_NOT_EXIT("LOAN_RECORD_NOT_EXIT", "借款记录不存在"),

    LOAN_ERROR("LOAN_ERROR", "申请借款失败"),

    PRODUCT_DETIL_NOT_EXIT("PRODUCT_DETIL_NOT_EXIT", "产品详情不存在"),

    UPDATE_PAY_PWD_ERROR("UPDATE_PAY_PWD_ERROR", "修改交易密码失败"),

    CREATE_BEANCOPIER_ERROR("CREATE_BEANCOPIER_ERROR", "创建对象复制失败"),

    IMAGE_IS_NULL("IMAGE_IS_NULL", "图片为空"),

    CELL_EXSIT("CELL_EXSIT", "手机号码对应的账户已经存在"),

    /** 操作成功 */
    SUCCESS("SUCCESS", "操作成功"), SYSTEM_ERROR("SYSTEM_ERROR", "服务异常，请稍后尝试"),

    NOT_EQUAL_LOGIN_PASSWD("NOT_EQUAL_LOGIN_PASSWD", "您两次输入的密码不相符，请重新输入"),

    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "非法参数"),

    ERROR_CHECK_CODE("ERROR_CHECK_CODE", "验证码输入错误，请重新输入！"),

    NETWORD_CONNECT_ERROR("NETWORD_CONNECT_ERROR", "网络不太给力,请重新尝试"),

    INVALID_REQUEST("INVALID_REQUEST", "非法请求"),

    USER_NOT_EXSIT("USER_NOT_EXSIT", "系统不存在此用户"),

    LOGIN_PWD_UPDATE_SUCC("LOGIN_PWD_UPDATE_SUCC", "登录密码修改成功"),

    USERID_NOT_BLANK("USERID_NOT_BLANK", "用户号不正确"),

    TOKEN_NOT_BLANK("TOKEN_NOT_BLANK", "token不正确"),

    FORCE_LOG_OUT("FORCE_LOG_OUT", "账号已在其他设备上登录，为了账号安全，请重新登录"),

    BANK_INFO_ERROR("BANK_INFO_ERROR", "银行卡信息有误"),

    /**{0} 要用 @see MessageFormat.format */
    EXCEED_BANK_S("EXCEED_BANK_S", "申购金额大于银行卡单笔最大支付限额{0}元"),

    //超过单日剩余交易额度  对不起，您所绑定的银行卡当日交易额度已达上限
    EXCEED_BANK_R1("EXCEED_BANK_R1", "您当日交易额度剩余{0}元，请提升交易额度"),

    EXCEED_BANK_R2("EXCEED_BANK_R2", "对不起，您的银行卡所支持的该类产品每日交易额度已达上限"),

    EXCEED_CUSTOM_S("EXCEED_CUSTOM_S", "首次理财，佑米建议您进行{0}元以下申购"),

    EXCEED_BANK_D("EXCEED_BANK_D", "申购金额大于银行卡单日最大支付限额{0}元"),

    EXCEED_QUICK_PAY("EXCEED_QUICK_PAY", "因交易系统升级，此次交易采用快捷支付，且本次银行卡支付的金额上限暂为{0}元"),

    EXCEED_QUICK_PAY_MODIFY("EXCEED_QUICK_PAY_MODIFY",
                            "因交易系统升级，此次交易采用快捷支付，且本次银行卡支付的金额上限暂为{0}元，请返回修改金额"),

    EXCEED_AVAIL_WITHDRAW("EXCEED_AVAIL_WITHDRAW", "转出金额大于可提现金额{0}元"),

    DEFAULT("DEFAULT", "默认错误"),

    NEED_AUTH("NEED_AUTH", "需要鉴权"),

    NEW_USER_AUTH("NEW_USER_AUTH", "新用户鉴权"),

    LITTLE_AUTH_MONEY_ERROR("LITTLE_AUTH_MONEY_ERROR", "小额鉴权输入金额出错"),

    EXCEED_BANK_M("EXCEED_BANK_M", "申购金额大于银行卡单月最大支付限额{0}元"),

    ERROR_LOGIN_PWD("ERROR_LOGIN_PWD", "登录密码不正确，请重新输入"),
    
    ERROR_PAY_PWD("ERROR_PAY_PWD", "支付密码不正确，请重新输入"),
    
    ERROR_OLD_PAY_PWD("ERROR_OLD_PAY_PWD", "原支付密码不正确，请重新输入"),

    CHECK_CODE_ERROR("CHECK_CODE_ERROR", "验证码错误"),

    TEMPLET_CODE_NULL("TEMPLET_CODE_NULL", "分期分类编码为空错误"),

    PARAM_IS_NULL("PARAM_IS_NULL", "参数为空"),

    NULL_OF_THIS_RECORD("NULL_OF_THIS_RECORD", "该记录在系统中不存在") ,
    
    BIND_CARD_ERROR("BIND_CARD_ERROR","绑卡错误"),

    NULL_OF_USER_REAL_NAME("NULL_OF_USER_REAL_NAME","该用户昵称不存在");

//    NULL_OF_PRODUCT_BEYOND_TOWN_FEE("NULL_OF_PRODUCT_BEYOND_TOWN_FEE","查询不到该产品的出城费");



    private String code;

    private String desc;

    /**
     * @param code
     * @param desc
     */
    private ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * Getter method for property <tt>code</tt>.
     * 
     * @return property value of code
     */
    public String getCode() {
        return code;
    }

    /**
     * Setter method for property <tt>code</tt>.
     * 
     * @param code value to be assigned to property code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Getter method for property <tt>desc</tt>.
     * 
     * @return property value of desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Setter method for property <tt>desc</tt>.
     * 
     * @param desc value to be assigned to property desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String message() {
        return desc;
    }

    @Override
    public Number value() {
        return null;
    }

}
