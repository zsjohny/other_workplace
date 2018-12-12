package com.finace.miscroservice.commons.enums;

/**
 * 消息中心 消息类型
 */
public enum  MsgCodeEnum {
    SYS_MSG_TEXT(0000,"亲爱的用户，感谢您出借【%s】，出借金额%s元，预期收益%s元，项目期限%d天，到期后自动还款，如有疑问请咨询%s"),
    SYS_MSG_TEXT2(0000,"亲爱的用户，感谢您注册一桶金，特别赠送您588元红包，请前往我的红包查看，新手宝年化收益高达15%！如有疑问可咨询客服电话：400-888-7140"),
    SYS_MSG(0,"系统消息"),
    SYS_SUBTYPE_BID(1001,"投标通知"),
    SYS_SUBTYPE_RED_PACKAGE(1002,"红包到账通知"),
    SYS_SUBTYPE_REGISTER(1003,"注册通知"),
    NOTICE_CENTER(1,"公告中心"),
    OFFICIAL_NOTICE(2,"官方通知");
    private Integer code;
    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    MsgCodeEnum(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
