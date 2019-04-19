package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

public enum ShopMemberOrderStatus implements IntEnum{
	    UNPAID(0, "未付款"),
	    
	    TAKING_DELIVERY(1, "待提货"),

	    REFUNDING(2, "退款中"),

	    CLOSE_ORDER(3, "订单关闭"),

	    FINISH_ORDER(4, "订单完成"),

	    UNDELIVERED(5, "待发货"),

	    DELIVERED(6, "已发货");

	    private ShopMemberOrderStatus(int intValue, String displayName) {
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
	    
	    public static ShopMemberOrderStatus getNameByValue(int intValue ) {
	        if (intValue<0) {
	            return null;
	        }

	        for (ShopMemberOrderStatus orderStatus : ShopMemberOrderStatus.values()) {
	            if (orderStatus.getIntValue()==intValue) {
	                return orderStatus;
	            }
	        }

	        return null;
	    }
	    
	    public static ShopMemberOrderStatus getByIntValue(int intValue){
	    	ShopMemberOrderStatus orderStatus = null;
	        for(ShopMemberOrderStatus os : ShopMemberOrderStatus.values()){
	            if(os.getIntValue() == intValue){
	                orderStatus = os;
	                break;
	            }
	        }
	        return orderStatus;
	    }
	    

	    public static ShopMemberOrderStatus parse(int intValue, ShopMemberOrderStatus defaultValue) {
	        for (ShopMemberOrderStatus status : values()) {
	            if (status.is(intValue)) {
	                return status;
	            }
	        }
	        return defaultValue;
	    }

}
