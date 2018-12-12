package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * 返回前端的类型
 * Created by Ness on 2017/5/11.
 */
public enum  ResponseTypeEnum {
    SUCCESS_PARAM(200),ERROR_PARAM(201),FAIL_PARAM(210),ILLEGAL_PARAM(204),EMPTY_PARAM(202),FORBID_PARAM(203);
    private int code;

    ResponseTypeEnum(int code) {
        this.code = code;
    }

    public int toCode() {
        return code;
    }

}
