/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model.base.result;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 系统结果集基类 
 * 
 * @author guangdong.li
 * @version $Id: ResultBase.java, v 0.1 17 Feb 2016 15:03:46 guangdong.li Exp $
 */
public class ResultBase implements Serializable {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -7280532312547559797L;

    protected boolean         success;

    private String            code;

    protected String          message;

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
