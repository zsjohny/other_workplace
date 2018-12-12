/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model;

import com.onway.platform.common.base.ToString;

/**
 * 客户端通用请求参数模型
 * 
 * @author yuanlin.wang
 * @version $Id: CommonParam.java, v 0.1 22 Feb 2016 17:04:18 yuanlin.wang Exp $
 */
public class CommonParam extends ToString {

    /** serialVersionUID */
    private static final long serialVersionUID = 6181741738037261888L;

    /** 用户编号 */
    private String            userId;

    /** 手机token */
    private String            token;

    /** 客户端类型 */
    private String            vjson;

    /** app版本号 */
    private String            ver;

    /** 渠道号 */
    private String            registerId;

    /** 加密key */
    private String            sign;

    /** MD5 */
    private String            sign_t;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVjson() {
        return vjson;
    }

    public void setVjson(String vjson) {
        this.vjson = vjson;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getRegisterId() {
        return registerId;
    }

    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign_t() {
        return sign_t;
    }

    public void setSign_t(String sign_t) {
        this.sign_t = sign_t;
    }

}
