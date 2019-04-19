package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * @author jeff.zhan
 * @version 2016年9月27日下午7:17:04
 * 
 */
public enum Status implements IntEnum {
    
	NORMAL(0),
	
	DELETE(-1),
	
	HIDE(1);
    
    private Status(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

}