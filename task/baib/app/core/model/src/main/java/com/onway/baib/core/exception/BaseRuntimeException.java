/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.exception;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 运行时异常
 * 
 * @author guangdong.li
 * @version $Id: BaseRuntimeException.java, v 0.1 2013-10-30 下午3:10:35  Exp $
 */
public class BaseRuntimeException extends RuntimeException {

    /**  serialVersionUID */
    private static final long serialVersionUID = 8321149154706648074L;

    protected String          message;

    /**
     * 空构造器。
     */
    public BaseRuntimeException() {
        super();
    }

    /**
     * 构造器。
     * 
     * @param message
     *            消息
     */
    public BaseRuntimeException(String message) {
        super(message);
    }

    /**
     * 构造器。
     * 
     * @param cause
     *            原因
     */
    public BaseRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * 构造器。
     * 
     * @param message
     *            消息
     * @param cause
     *            原因
     */
    public BaseRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @see Throwable#toString()
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
