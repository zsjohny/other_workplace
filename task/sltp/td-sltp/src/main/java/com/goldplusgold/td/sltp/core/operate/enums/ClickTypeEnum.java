package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * 点击的类型
 * Created by Ness on 2017/5/23.
 */
public enum ClickTypeEnum {
    ALREADY_TRIGGER(0), NOT_TRIGGER(1),MAX_LEN(1);

    private int code;

    ClickTypeEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }



}
