package com.finace.miscroservice.getway.enums;

import com.finace.miscroservice.commons.utils.JwtToken;

/**
 * push的类型的枚举
 */
public enum PushTypeEnum {

    UID(JwtToken.UID), IMEI("imei"), VERSION("version");

    private String type;

    PushTypeEnum(String type) {
        this.type = type;
    }

    public String toType() {
        return type;
    }

    /**
     * 根据类型获取push枚举
     *
     * @param type push类型枚举
     * @return
     */
    public static PushTypeEnum converPushTypeByStr(String type) {
        PushTypeEnum pushTypeEnum = null;
        PushTypeEnum[] values = PushTypeEnum.values();
        for (PushTypeEnum typeEnum : values) {
            if (typeEnum.type.equals(type)) {
                pushTypeEnum = typeEnum;
                break;
            }
        }

        return pushTypeEnum;


    }
}
