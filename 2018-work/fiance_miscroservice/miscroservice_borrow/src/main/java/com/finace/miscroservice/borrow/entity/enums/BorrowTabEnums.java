package com.finace.miscroservice.borrow.entity.enums;

/**
 * 标的 标签
 */
public enum BorrowTabEnums {
    TAB_BKJX("加息2.88%",0),
    TAB_XSB("新手标",0);
    private String value;
    private Integer code;

    BorrowTabEnums(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
