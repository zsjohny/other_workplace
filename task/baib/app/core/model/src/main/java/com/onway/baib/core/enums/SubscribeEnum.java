/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.enums;

/**
 * 预约订单状态
 * @author junjie.lin
 * @version $Id: SubscribeEnum.java, v 0.1 2016/9/12 15:17 junjie.lin Exp $
 */
public enum SubscribeEnum {

    INIT("init","未处理"),
    ACCEPT("accept","已接受"),
    REJECT("reject","已拒绝"),
    CANCEL("cancel","已取消"),
    FINISHED("finished","已完成"),
    ;

    /** 枚举编号 */
    private String code;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     *
     * @param code         枚举编号
     * @param message      枚举详情
     */
    private SubscribeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     *
     * @param code         枚举编号
     * @return
     */
    public static SubscribeEnum getSubscribeEnumByCode(String code) {
        for (SubscribeEnum param : values()) {
            if (org.apache.commons.lang.StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
