package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

public enum UserStatus implements IntEnum {
    
	/*
	 * 正常
	 */
	NORMAL(0),
	
	/*
	 * 删除
	 */
	DELETE(-1);
    
    private UserStatus(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public static UserStatus getByIntValue(int intValue) {
    	for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getIntValue() == intValue) {
				return userStatus;
			}
		}
    	return null;
    }
}
