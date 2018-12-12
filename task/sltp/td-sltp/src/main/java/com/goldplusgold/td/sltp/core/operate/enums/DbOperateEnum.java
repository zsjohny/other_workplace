package com.goldplusgold.td.sltp.core.operate.enums;

/**
 * Db的操作类型
 * Created by Ness on 2017/5/15.
 */
public enum DbOperateEnum {
    MYSQL(Boolean.TRUE), REDIS(Boolean.FALSE), MIXED(Boolean.TRUE);
    private boolean flag;

    DbOperateEnum(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }


}
