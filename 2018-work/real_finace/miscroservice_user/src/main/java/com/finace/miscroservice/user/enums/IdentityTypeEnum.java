package com.finace.miscroservice.user.enums;

public enum IdentityTypeEnum {
    LENDER("出借角色","1"),BORROWER("借款角色","2"),COMPENSATED_ROLE("代偿角色","3");
    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    IdentityTypeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
