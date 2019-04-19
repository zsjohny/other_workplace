package com.jiuyuan;


import com.jiuyuan.util.enumeration.IntEnum;

// 售后订单状态：1(待卖家确认、默认)、2（待买家发货）、3（待卖家确认收货）、4(退款成功)、5(卖家拒绝售后关闭)、6（买家超时未发货自动关闭）、7(卖家同意前买家主动关闭)、8（平台客服主动关闭）、9(卖家同意后买家主动关闭)
public enum RefundStatus implements IntEnum{
    CUSTOMER_APPLY_REFUND(1, "待卖家确认、默认"),
    
    SELLER_ACCEPT(2, "待买家发货"),

    CUSTOMER_DELIVERY(3, "待卖家确认收货"),

    REFUND_SUCCESS(4, "退款成功"),

    SELLER_REFUSE(5, "卖家拒绝售后关闭"),

    CUSTOMER_OVERTIME_UNDELIVERY(6, "买家超时未发货自动关闭"),

    CUSTOMER_CLOSE(7, "买家主动关闭"),

    ADMIN_CLOSE(8, "平台客服主动关闭"),
    
    CUSTOMER_CLOSE_AFTER_AGREE(9, "卖家同意后买家主动关闭");


    private RefundStatus(int intValue, String displayName) {
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
    
    public static RefundStatus getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (RefundStatus refundStatus : RefundStatus.values()) {
            if (refundStatus.getIntValue()==intValue) {
                return refundStatus;
            }
        }

        return null;
    }
    
    public static RefundStatus getByIntValue(int intValue){
    	RefundStatus refundStatus = null;
        for(RefundStatus os : RefundStatus.values()){
            if(os.getIntValue() == intValue){
            	refundStatus = os;
                break;
            }
        }
        return refundStatus;
    }
    

    public static RefundStatus parse(int intValue, RefundStatus defaultValue) {
        for (RefundStatus status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
  

}
