package com.finace.miscroservice.user.enums;

public enum AccountUseTypeEnum {
    COMMON_ACCOUNT("普通账户","00000"),RED_PACKET_ACCOUNT("红包账户","10000"),FEE_ACCOUNT("手续费账户","01000");

    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    AccountUseTypeEnum() {
    }

    AccountUseTypeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
