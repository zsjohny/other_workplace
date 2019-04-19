package com.store.enumerate;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 会员48小时标记枚举
 * unknown 未知
 * all 全部
 * befor  48小时没
 * after  48小时之后
 * @author zhaoxinglin
 */
public enum Member48MarkEnum implements IntEnum {
//	0（全部）、1（48小时内）、2（48小时前） 
	unknown(-1),
    all(0),
    before(1),
    after(2);

    private Member48MarkEnum(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

	public static Member48MarkEnum getEnum(int type) {
		if (type == 0){
			return all;
		}else if (type == 1){
			return before;
		}else if (type == 2){
			return after;
		}else{
			return unknown;
		}
		
	}

}
