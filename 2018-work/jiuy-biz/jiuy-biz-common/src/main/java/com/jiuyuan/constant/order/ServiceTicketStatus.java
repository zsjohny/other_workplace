package com.jiuyuan.constant.order;

import com.jiuyuan.util.enumeration.IntEnum;

public enum ServiceTicketStatus implements IntEnum {

    UNCHECK(0, "待审核"),
    
    CHECK_FAIL(1, "已驳回"),

    BUYER_SHIPPING(2, "待买家发货"),

    CONFIRMING(3, "待确认"),

    UNPAID(4, "待付款"),
    
    REFUNDING(5, "待退款(换货处理中)"),

    REFUNDED(6, "退款成功(已发货)"),
    
    EXCHANGE_SUCCESS(7, "换货成功");
	
	

    private ServiceTicketStatus(int intValue, String displayName) {
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
    
    public static ServiceTicketStatus getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (ServiceTicketStatus orderStatus : ServiceTicketStatus.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
    
    

    public static ServiceTicketStatus parse(int intValue, ServiceTicketStatus defaultValue) {
        for (ServiceTicketStatus status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
}
