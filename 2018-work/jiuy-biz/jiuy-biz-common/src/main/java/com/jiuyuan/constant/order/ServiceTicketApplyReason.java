package com.jiuyuan.constant.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jiuyuan.util.enumeration.IntEnum;



public enum ServiceTicketApplyReason implements  Serializable  , IntEnum {

    
	QUALITY_PROBLEM(1, "质量问题"),
	
    NOT_MATCH(2, "规格不符"),

    DONT_LIKE(3, "不喜欢"),

    DONT_FIT(4, "尺码不合身"),
    
    COLOR_SHADING(5, "有色差"),
    
    DONT_NEED(6, "不想要了"),

    EXPRESS_PROBLEM(7, "快递原因");
	
	
    private ServiceTicketApplyReason(int intValue, String displayName) {
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
    
    public static ServiceTicketApplyReason getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (ServiceTicketApplyReason orderStatus : ServiceTicketApplyReason.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
    
    

    public static ServiceTicketApplyReason parse(int intValue, ServiceTicketApplyReason defaultValue) {
        for (ServiceTicketApplyReason status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
    
    public static List<ServiceTicketApplyReason> getAllList() {
    	List<ServiceTicketApplyReason> serviceTicketApplyReasonList = new ArrayList<ServiceTicketApplyReason>();
    	for (ServiceTicketApplyReason status : ServiceTicketApplyReason.values()) {
    		serviceTicketApplyReasonList.add(status);
    	}
    	return serviceTicketApplyReasonList;
    }
}
