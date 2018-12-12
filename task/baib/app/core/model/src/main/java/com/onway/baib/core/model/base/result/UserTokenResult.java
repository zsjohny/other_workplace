/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.model.base.result;

/**
 * 
 * 
 * @author guangdong.li
 * @version $Id: UserTokenResult.java, v 0.1 17 Feb 2016 15:03:10 guangdong.li Exp $
 */
public class UserTokenResult extends ResultBase {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -5687042799522194318L;

    private String            encrpKey;

    public String getEncrpKey() {
        return encrpKey;
    }

    public void setEncrpKey(String encrpKey) {
        this.encrpKey = encrpKey;
    }

}
