package com.onway.web.controller.result;

/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */

import java.io.Serializable;
import java.util.Date;

import com.onway.common.lang.DateUtils;

/**
 * json调用默认结果
 * 
 * @author guangdong.li
 * @version $Id: JsonResult.java, v 0.1 2013-10-30 下午5:38:55  Exp $
 */
public class JsonResult implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 1475348231900998033L;

    private String            time             = DateUtils.format(new Date(), DateUtils.newFormat);

    // 业务操作结果
    private boolean           bizSucc          = true;

    // 是否强制下线，账号已在另一设备登录
    //private boolean           isForceLogOut    = false;

    private String            errMsg           = "";

    // 错误码
    private String            errCode          = "";

    public JsonResult(boolean bizSucc) {
        this.bizSucc = bizSucc;
    }

    public JsonResult(boolean bizSucc, String errCode, String message) {
        this.bizSucc = bizSucc;
        this.errMsg = message;
        this.errCode = errCode;
    }

    public void markResult(boolean bizSucc, String errCode, String message) {
        this.bizSucc = bizSucc;
        this.errMsg = message;
        this.errCode = errCode;
    }

    public boolean isBizSucc() {
        return bizSucc;
    }

    public void setBizSucc(boolean bizSucc) {
        this.bizSucc = bizSucc;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
