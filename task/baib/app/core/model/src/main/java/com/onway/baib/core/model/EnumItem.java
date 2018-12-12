package com.onway.baib.core.model;

import com.onway.platform.common.base.ToString;

public class EnumItem extends ToString {
    /**  */
    private static final long serialVersionUID = 1L;

    /** Ã¶¾Ù±àºÅ */
    private String            code;

    /** Ã¶¾ÙÏêÇé */
    private String            message;
    
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EnumItem(String code, String message,String value) {
        super();
        this.code = code;
        this.message = message;
        this.value = value;
    }
    
    public EnumItem(String code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public EnumItem() {
        super();
    }

}
