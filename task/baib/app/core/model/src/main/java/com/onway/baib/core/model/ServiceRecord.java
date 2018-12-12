/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model;

import com.onway.platform.common.base.ToString;

/**
 * @author senze.liu
 * @version $Id: ServiceRecord.java, v 0.1 2016/9/7 14:04 senze.liu Exp $
 */
public class ServiceRecord extends ToString{

    /**
     * This property corresponds to db column <tt>ID</tt>.
     */
    private Integer id;

    /**
     * This property corresponds to db column <tt>ORDER_NO</tt>.
     */
    private String orderNo;


   /* *//**
     * This property corresponds to db column <tt>ORDER_STATUS</tt>.
     *//*
    private String orderStatus;*/


    /**
     * This property corresponds to db column <tt>CHANNEL</tt>.
     */
    private String channel;


    /**
     * This property corresponds to db column <tt>TYPE</tt>.
     */
    private String type;

    /**
     * This property corresponds to db column <tt>SUB_TYPE</tt>.
     */
    private String subType;

    /**
     * This property corresponds to db column <tt>GMT_CREATE</tt>.
     */
    private String gmtCreate;

    /**
     * This property corresponds to db column <tt>GMT_MODIFIED</tt>.
     */
    private String gmtModified;

    /**
     * This property corresponds to db column <tt>yudao_NO</tt>.
     */
    private String yudaoNo;

    /**
     * This property corresponds to db column <tt>ICON_URL</tt>.
     */
    private String iconUrl;

    /**
     * This property corresponds to db column <tt>ENTERPRISE_NAME</tt>.
     */
    private String enterpriseName;

    public String getyudaoNo() {
        return yudaoNo;
    }

    public void setyudaoNo(String yudaoNo) {
        this.yudaoNo = yudaoNo;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }
}
