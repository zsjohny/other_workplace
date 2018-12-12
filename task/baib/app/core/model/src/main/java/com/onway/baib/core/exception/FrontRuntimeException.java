package com.onway.baib.core.exception;

/**
 * onway.com Inc.
 * Copyright (c) 2013-2013 All Rights Reserved.
 */

import com.onway.platform.common.enums.EnumBase;
import com.onway.platform.common.exception.BaseRuntimeException;

/**
 * 系统内部运行异常
 * 
 * @author guangdong.li
 * @version $Id: FrontRuntimeException.java, v 0.1 2013-9-12 下午2:27:43 WJL Exp $
 */
public class FrontRuntimeException extends BaseRuntimeException {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public FrontRuntimeException() {
        super();
    }

    public FrontRuntimeException(EnumBase baseEnum, String message) {
        super(baseEnum, message);
    }

    public FrontRuntimeException(EnumBase baseEnum) {
        super(baseEnum);
    }

    public FrontRuntimeException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public FrontRuntimeException(String errorCode, String message) {
        super(errorCode, message);
    }

    public FrontRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrontRuntimeException(String message) {
        super(message);
    }

    public FrontRuntimeException(Throwable cause) {
        super(cause);
    }

}
