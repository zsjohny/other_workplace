package com.jiuyuan.constant.account;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * 
 * 第三方应用类型：0其他，1微信公众号1账号
 * @author zhaoxinglin
 *
 * @version 2017年4月13日下午5:55:14
 */
public enum ThirdAppType implements IntEnum {
    QITA(0),

    WEIXIN_PUBLIC_NUM1(1);
    
    private ThirdAppType(int intValue) {
        this.intValue = intValue;
    }

    private int intValue;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public static ThirdAppType parse(int intValue) {
        for (ThirdAppType type : values()) {
            if (type.getIntValue() == intValue) {
                return type;
            }
        }
        return null;
    }

}
