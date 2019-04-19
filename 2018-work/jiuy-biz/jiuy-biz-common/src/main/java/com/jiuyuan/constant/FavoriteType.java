package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

public enum FavoriteType implements IntEnum {
    PRODUCT(0), 
    
    PARTNER(1), 
    
    ARTICLE(2),
    
    STORE_BUSINESS(3);

    private FavoriteType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

}
