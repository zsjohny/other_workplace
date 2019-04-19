package com.store.enumerate;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 会员审核状态枚举

 * @author czy
 */
public enum StoreAuditStatusEnum implements IntEnum {
	fail(-1),
	
    submit(0),
    
    pass(1),
    
    forbidden(-2);

    private StoreAuditStatusEnum(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }


}
