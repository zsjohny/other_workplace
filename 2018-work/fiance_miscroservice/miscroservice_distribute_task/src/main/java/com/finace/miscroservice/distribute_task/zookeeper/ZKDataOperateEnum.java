package com.finace.miscroservice.distribute_task.zookeeper;

/**
 * zk的数据操作枚举
 */
public enum ZKDataOperateEnum {

    CREATE_SERVER("create_server"), ACCEPT_DATA("accept_data");
    private String operate;

    ZKDataOperateEnum(String operate) {
        this.operate = operate;
    }

    public String getOperate() {
        return operate;
    }

    public static ZKDataOperateEnum getOperateByName(String name) {
        ZKDataOperateEnum operateEnum = null;
        if (name == null || name.isEmpty()) {
            return operateEnum;
        }
        for (ZKDataOperateEnum dataOperateEnum : ZKDataOperateEnum.values()) {
            if (dataOperateEnum.operate.equals(name)) {
                operateEnum = dataOperateEnum;
                break;
            }
        }

        return operateEnum;
    }


}
