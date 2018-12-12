/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model;

import com.onway.platform.common.base.ToString;

/**
 * 协议信息web数据
 * 
 * @author guangdong.li
 * @version $Id: ProtocolInfoData.java, v 0.1 22 Feb 2016 17:16:37 guangdong.li Exp $
 */
public class ProtocolInfoData extends ToString {

    private static final long serialVersionUID = -3762010055069770333L;

    // 协议名称
    private String            name;

    // 协议地址
    private String            url;

    // 协议展示顺序
    private int               orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

}
