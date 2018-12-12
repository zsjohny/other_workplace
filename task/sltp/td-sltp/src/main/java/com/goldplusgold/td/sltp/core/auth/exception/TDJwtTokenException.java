package com.goldplusgold.td.sltp.core.auth.exception;

/**
 * jwt token在其生命周期中出现的异常
 */
public class TDJwtTokenException extends JwtTokenException {

    public TDJwtTokenException(String errorCode, String errorMsg, String viewInfo) {
        this(errorCode, errorMsg, null, viewInfo);
    }

    public TDJwtTokenException(String errorCode, String errorMsg, Exception e, String viewInfo) {
        super(errorCode, errorMsg, e, viewInfo);
    }

    public TDJwtTokenException(String errorCode, Exception e, String viewInfo) {
        super(errorCode, e.getMessage(), e, viewInfo);
    }

}
