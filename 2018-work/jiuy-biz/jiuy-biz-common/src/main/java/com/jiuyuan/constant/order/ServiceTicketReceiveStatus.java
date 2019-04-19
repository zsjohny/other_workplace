package com.jiuyuan.constant.order;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.jiuyuan.util.enumeration.IntEnum;



public enum ServiceTicketReceiveStatus implements Serializable  , IntEnum  {

	RECEIVED(1, "已收货"),

    UNRECEIVED(2, "未收货");
	
	

    private ServiceTicketReceiveStatus(int intValue, String displayName) {
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
    
    public static ServiceTicketReceiveStatus getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (ServiceTicketReceiveStatus orderStatus : ServiceTicketReceiveStatus.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
    
    

    public static ServiceTicketReceiveStatus parse(int intValue, ServiceTicketReceiveStatus defaultValue) {
        for (ServiceTicketReceiveStatus status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
    
    public static List<ServiceTicketReceiveStatus> getAllList() {
    	List<ServiceTicketReceiveStatus> receiveStatusList = new ArrayList<ServiceTicketReceiveStatus>();
    	for (ServiceTicketReceiveStatus status : ServiceTicketReceiveStatus.values()) {
    		receiveStatusList.add(status);
    		
    	}
    	return receiveStatusList;
    }
}
