package com.onway.web.controller.result;

/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */

/**
 * 获取版本号结果集
 * 
 * @author guangdong.li
 * @version $Id: EmpLoginResult.java, v 0.1 2016年4月11日 下午10:28:00 guangdong.li Exp $
 */
public class AppVersionResult extends JsonResult {

    /**
     * @param bizSucc
     */
    public AppVersionResult(boolean bizSucc) {
        super(bizSucc);
    }

    /**  */
    private static final long serialVersionUID = 1L;

    private String            appVersion;

    private boolean           hasNew;

    /**
     * Getter method for property <tt>appVersion</tt>.
     * 
     * @return property value of appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * Setter method for property <tt>appVersion</tt>.
     * 
     * @param appVersion value to be assigned to property appVersion
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     * Getter method for property <tt>hasNew</tt>.
     * 
     * @return property value of hasNew
     */
    public boolean isHasNew() {
        return hasNew;
    }

    /**
     * Setter method for property <tt>hasNew</tt>.
     * 
     * @param hasNew value to be assigned to property hasNew
     */
    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }
}
