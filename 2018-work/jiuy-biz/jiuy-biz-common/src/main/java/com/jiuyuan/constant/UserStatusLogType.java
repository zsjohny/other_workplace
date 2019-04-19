package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月15日 上午11:18:25
*/
public enum UserStatusLogType implements IntEnum {
	USERSTATUS(0, "会员账号状态"),
	USERDISTRIBUTIONSTATUS(1, "会员分销状态"),
	STORESTATUS(2, "门店商家账号状态"),
	STOREDISTRIBUTIONSTATUS(3, "门店商家分销状态"),
	BRANDSTATUS(4, "品牌商家账号状态")
	;
	
	private int intValue;

    private String displayName;
    
	private UserStatusLogType(int intValue, String displayName) {
		this.intValue = intValue;
        this.displayName = displayName;
	}

	@Override
	public int getIntValue() {
		return intValue;
	}

	public String getDisplayName() {
        return displayName;
    }
}
