package com.jiuyuan.constant;

import com.jiuyuan.constant.order.OrderStatus;
import com.jiuyuan.util.enumeration.IntEnum;

public enum GroundUserType implements IntEnum{
	REGION_MANAGER(1, "大区总监"),
    
	PROVINCE_MANAGER(2, "省区经理"),

	AREA_MANAGER(3, "区域主管"),

	CITY_MANAGER(4, "城市经理");

 

    private GroundUserType(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    private int intValue;

    private String displayName;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean is(int intValue) {
        return getIntValue() == intValue;
    }
    
    public static GroundUserType getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (GroundUserType groundUserType : GroundUserType.values()) {
            if (groundUserType.getIntValue()==intValue) {
                return groundUserType;
            }
        }

        return null;
    }
    
    public static GroundUserType getByIntValue(int intValue){
    	GroundUserType groundUserType = null;
        for(GroundUserType g : GroundUserType.values()){
            if(g.getIntValue() == intValue){
            	groundUserType = g;
                break;
            }
        }
        return groundUserType;
    }
    

    public static GroundUserType parse(int intValue, GroundUserType defaultValue) {
        for (GroundUserType g : values()) {
            if (g.is(intValue)) {
                return g;
            }
        }
        return defaultValue;
    }
}
