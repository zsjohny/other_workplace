package com.jiuyuan.constant.order;


import com.jiuyuan.util.enumeration.IntEnum;
//订单状态：0未付款、10已付款、50已发货、70交易成功、100交易关闭、5所有（已废弃）、20待审核（已废弃）、30已审核（废弃）、40审核不通过（已废弃）、60已签收（已废弃）、80退款中（已废弃）、90退款成功(已废弃)
public enum OrderStatus implements IntEnum {
    UNPAID(0, "未付款"),
    
    ALL(5, "所有"),

    PAID(10, "已付款"),

    UNCHECK(20, "待审核"),

    CHECKED(30, "已审核"),

    CHECK_FAIL(40, "审核不通过"),

    DELIVER(50, "已发货"),

    SIGNED(60, "已签收"),

    SUCCESS(70, "交易成功"),

    REFUNDING(80, "退款中"),

    REFUNDED(90, "退款成功"),

    CLOSED(100, "交易关闭");

    private OrderStatus(int intValue, String displayName) {
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
    
    public static OrderStatus getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
    
    public static OrderStatus getByIntValue(int intValue){
        OrderStatus orderStatus = null;
        for(OrderStatus os : OrderStatus.values()){
            if(os.getIntValue() == intValue){
                orderStatus = os;
                break;
            }
        }
        return orderStatus;
    }
    

    public static OrderStatus parse(int intValue, OrderStatus defaultValue) {
        for (OrderStatus status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
}
