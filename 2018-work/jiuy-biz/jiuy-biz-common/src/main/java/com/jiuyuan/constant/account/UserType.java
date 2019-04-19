package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

public enum UserType implements IntEnum {
    EMAIL(0),

    PHONE(1),

    WEIXIN(2);
    
    private UserType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public static UserType parse(int intValue) {
        for (UserType type : values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        return null;
    }

}
