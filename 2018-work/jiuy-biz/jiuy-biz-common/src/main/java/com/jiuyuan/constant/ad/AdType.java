package com.jiuyuan.constant.ad;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * @author Administrator
 *
 */
public enum AdType implements IntEnum{
	
	COMMON_AD_TYPE(1,1),  //前台通用广告定义
	APP_AD_TYPE(11,11)    //手机APP使用的广告定义
	;

	
	private AdType(int intValue, int advertisementType) {
        this.intValue = intValue;
        this.advertisementType = advertisementType;
    }

    private int intValue;
    
    private int advertisementType;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public int getAdvertisementType() {
        return advertisementType;
    }
}
