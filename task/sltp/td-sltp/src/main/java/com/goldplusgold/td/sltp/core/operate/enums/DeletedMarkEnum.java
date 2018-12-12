package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * 删除的标识
 * Created by Ness on 2017/5/11.
 */
public enum DeletedMarkEnum {
    DELETED(Boolean.TRUE),
    NOT_DELETED(Boolean.FALSE);
    private Boolean mark;

    DeletedMarkEnum(Boolean mark) {
        this.mark = mark;
    }

    public Boolean getMark() {
        return mark;
    }

}
