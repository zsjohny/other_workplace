package com.jiuyuan.constant.order;

import com.jiuyuan.util.enumeration.IntEnum;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月8日 下午2:11:14
*/
public enum StoreOrderStatus implements IntEnum{
	UNPAID(0, "未付款"),
    
    ALL(5, "所有"),

    PAID(10, "已付款"),

    UNCHECK(20, "待审核"),

    CHECKED(30, "已审核"),

    CHECK_FAIL(40, "审核不通过"),

    DELIVER(50, "已发货"),

    SIGNED(60, "已签收"),

    SUCCESS(70, "交易成功"),
    
    REFUNDED (80, "待退款"),
	
	CANCELED(90, "已取消"),
	
    CLOSED(	100,"交易关闭");

    private StoreOrderStatus(int intValue, String displayName) {
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
}
