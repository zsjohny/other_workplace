package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

public enum BinaryState implements IntEnum {

    NO(0), YES(1);

    private BinaryState(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }
    
    public static BinaryState fromIntValue(int intValue, BinaryState defaultValue) {
        for (BinaryState state : values()) {
            if (state.intValue == intValue) {
                return state;
            }
        }
        return defaultValue;
    }
}
