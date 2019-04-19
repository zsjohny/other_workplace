package com.jiuyuan.constant.order;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 
 * @author zhaoxinglin
 *
 * @version 2017年5月3日下午5:38:36
 */
public enum OrderType implements IntEnum {
    PAY(0, OrderConstants.PAY_CENTS_PER_UNIT),

    SEND_BACK(1, OrderConstants.SEND_BACK_PAY_CENTS_PER_UNIT);

    private OrderType(int intValue, int payCentsPerUnit) {
        this.intValue = intValue;
        this.payCentsPerUnit = payCentsPerUnit;
    }

    private int intValue;
    
    private int payCentsPerUnit;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public int getPayCentsPerUnit() {
        return payCentsPerUnit;
    }
    
    public static OrderType getByIntValue(int intValue){
    	OrderType orderType = null;
        for(OrderType ot : OrderType.values()){
            if(ot.getIntValue() == intValue){
            	orderType = ot;
                break;
            }
        }
        return orderType;
    }

}
