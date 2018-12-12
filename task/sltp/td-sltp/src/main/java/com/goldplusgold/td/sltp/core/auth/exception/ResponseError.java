package com.goldplusgold.td.sltp.core.auth.exception;

import java.io.Serializable;

/**
 * 错误消息对象
 */
public class ResponseError implements Serializable {

    /**
     * 异常状态码
     */
    private String errorCode;

    /**
     * 异常信息
     */
    private String errorMsg;


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
