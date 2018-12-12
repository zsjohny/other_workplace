package com.finace.miscroservice.commons.entity;

import java.io.Serializable;

/**
 * 路由操作
 */
public class Routes  implements Serializable {
    private static final long serialVersionUID = 522979765766458877L;

    public enum OperateEnum {
        ADD, DELETE
    }

    private OperateEnum openEnum;

    private String service;

    public OperateEnum getOpenEnum() {
        return openEnum;
    }

    public void setOpenEnum(OperateEnum openEnum) {
        this.openEnum = openEnum;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service.toLowerCase();
    }

    @Override
    public String toString() {
        return "Routes{" +
                "openEnum=" + openEnum +
                ", service='" + service + '\'' +
                '}';
    }
}