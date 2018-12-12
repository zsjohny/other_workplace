package com.goldplusgold.td.sltp.core.auth.exception;

/**
 * 说明：TD的rest服务抽象异常类
 */
public abstract class AbstractRestException extends Exception {

    /**
     * 错误编码
     */
    private String errorCode;

    /**
     * 真实的异常信息
     */
    private String errorMsg;

    /**
     * 真实的内部异常
     */
    private Exception innerException;

    /**
     * 用于客户端显示的异常信息
     */
    private String viewInfo;

    public AbstractRestException(String errorCode,
                                 String errorMsg,
                                 Exception e,
                                 String viewInfo) {

        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.innerException = e;
        this.viewInfo = viewInfo;
    }

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

    public Exception getInnerException() {
        return innerException;
    }

    public void setInnerException(Exception innerException) {
        this.innerException = innerException;
    }

    public String getViewInfo() {
        return viewInfo;
    }

    public void setViewInfo(String viewInfo) {
        this.viewInfo = viewInfo;
    }
}