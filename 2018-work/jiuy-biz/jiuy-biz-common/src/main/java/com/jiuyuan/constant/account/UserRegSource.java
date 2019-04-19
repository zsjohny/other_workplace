package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

public enum UserRegSource implements IntEnum {
    IPHONE(0),

    ANDROID(1),

    WEB(2),
	
	OTHER(9);
    
    private UserRegSource(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public static UserRegSource parse(int intValue) {
        for (UserRegSource type : values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        return null;
    }

}
